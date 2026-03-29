package org.dromara.auth.controller;

import cn.dev33.satoken.exception.NotLoginException;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.dromara.auth.domain.vo.LoginTenantVo;
import org.dromara.auth.domain.vo.LoginVo;
import org.dromara.auth.domain.vo.TenantListVo;
import org.dromara.auth.form.AuthBindingBody;
import org.dromara.auth.form.RegisterBody;
import org.dromara.auth.form.SocialLoginBody;
import org.dromara.auth.form.TenantListBody;
import org.dromara.auth.form.UnlockSocialBody;
import org.dromara.auth.service.IAuthStrategy;
import org.dromara.auth.service.SysLoginService;
import org.dromara.common.core.constant.SystemConstants;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.domain.model.LoginBody;
import org.dromara.common.core.utils.*;
import org.dromara.common.encrypt.annotation.ApiEncrypt;
import org.dromara.common.json.utils.JsonUtils;
import org.dromara.common.ratelimiter.annotation.RateLimiter;
import org.dromara.common.ratelimiter.enums.LimitType;
import org.dromara.common.satoken.utils.LoginHelper;
import org.dromara.common.social.config.properties.SocialLoginConfigProperties;
import org.dromara.common.social.config.properties.SocialProperties;
import org.dromara.common.social.utils.SocialUtils;
import org.dromara.common.tenant.helper.TenantHelper;
import org.dromara.resource.api.RemoteMessageService;
import org.dromara.system.api.RemoteClientService;
import org.dromara.system.api.RemoteConfigService;
import org.dromara.system.api.RemoteSocialService;
import org.dromara.system.api.RemoteTenantService;
import org.dromara.system.api.domain.vo.RemoteClientVo;
import org.dromara.system.api.domain.vo.RemoteTenantVo;
import org.springframework.web.bind.annotation.*;

import org.springframework.validation.annotation.Validated;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * token 控制
 *
 * @author Lion Li
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class TokenController {

    private final SocialProperties socialProperties;
    private final SysLoginService sysLoginService;
    private final ScheduledExecutorService scheduledExecutorService;

    @DubboReference
    private final RemoteConfigService remoteConfigService;
    @DubboReference
    private final RemoteTenantService remoteTenantService;
    @DubboReference
    private final RemoteClientService remoteClientService;
    @DubboReference
    private final RemoteSocialService remoteSocialService;
    @DubboReference(stub = "true")
    private final RemoteMessageService remoteMessageService;

    /**
     * 登录方法
     *
     * @param body 登录信息
     * @return 结果
     */
    @ApiEncrypt
    @PostMapping("/login")
    public R<LoginVo> login(@RequestBody String body) {
        LoginBody loginBody = JsonUtils.parseObject(body, LoginBody.class);
        ValidatorUtils.validate(loginBody);
        // 授权类型和客户端id
        String clientId = loginBody.getClientId();
        String grantType = loginBody.getGrantType();
        RemoteClientVo clientVo = remoteClientService.queryByClientId(clientId);

        // 查询不到 client 或 client 内不包含 grantType
        if (ObjectUtil.isNull(clientVo) || !StringUtils.contains(clientVo.getGrantType(), grantType)) {
            log.info("客户端id: {} 认证类型：{} 异常!.", clientId, grantType);
            return R.fail(MessageUtils.message("auth.grant.type.error"));
        } else if (!SystemConstants.NORMAL.equals(clientVo.getStatus())) {
            return R.fail(MessageUtils.message("auth.grant.type.blocked"));
        }
        // 校验租户
        sysLoginService.checkTenant(loginBody.getTenantId());
        // 登录
        LoginVo loginVo = IAuthStrategy.login(body, clientVo, grantType);

        Long userId = LoginHelper.getUserId();
        scheduledExecutorService.schedule(() -> {
            remoteMessageService.publishMessage(List.of(userId), DateUtils.getTodayHour(new Date()) + "好，欢迎登录 RuoYi-Cloud-Plus 后台管理系统");
        }, 5, TimeUnit.SECONDS);
        return R.ok(loginVo);
    }

    /**
     * 第三方登录请求
     *
     * @param body 绑定入参（source、tenantId、domain）
     * @return 结果
     */
    @PostMapping("/binding")
    public R<String> authBinding(@RequestBody @Validated AuthBindingBody body) {
        String source = body.getSource();
        SocialLoginConfigProperties obj = socialProperties.getType().get(source);
        if (ObjectUtil.isNull(obj)) {
            return R.fail(source + "平台账号暂不支持");
        }
        AuthRequest authRequest = SocialUtils.getAuthRequest(source, socialProperties);
        Map<String, String> map = new HashMap<>();
        map.put("tenantId", body.getTenantId());
        map.put("domain", body.getDomain());
        map.put("state", AuthStateUtils.createState());
        String authorizeUrl = authRequest.authorize(Base64.encode(JsonUtils.toJsonString(map), StandardCharsets.UTF_8));
        return R.ok("操作成功", authorizeUrl);
    }

    /**
     * 第三方登录回调业务处理 绑定授权
     *
     * @param loginBody 请求体
     * @return 结果
     */
    @PostMapping("/social/callback")
    public R<Void> socialCallback(@RequestBody SocialLoginBody loginBody) {
        // 获取第三方登录信息
        AuthResponse<AuthUser> response = SocialUtils.loginAuth(
            loginBody.getSource(), loginBody.getSocialCode(),
            loginBody.getSocialState(), socialProperties);
        AuthUser authUserData = response.getData();
        // 判断授权响应是否成功
        if (!response.ok()) {
            return R.fail(response.getMsg());
        }
        sysLoginService.socialRegister(authUserData);
        return R.ok();
    }


    /**
     * 取消授权
     *
     * @param body socialId
     */
    @PostMapping("/unlock")
    public R<Void> unlockSocial(@RequestBody @Validated UnlockSocialBody body) {
        Boolean rows = remoteSocialService.deleteWithValidById(body.getSocialId());
        return rows ? R.ok() : R.fail("取消授权失败");
    }

    /**
     * 登出方法
     */
    @PostMapping("logout")
    public R<Void> logout() {
        sysLoginService.logout();
        return R.ok();
    }

    /**
     * 用户注册
     */
    @ApiEncrypt
    @PostMapping("register")
    public R<Void> register(@RequestBody RegisterBody registerBody) {
        if (!remoteConfigService.selectRegisterEnabled(registerBody.getTenantId())) {
            return R.fail("当前系统没有开启注册功能！");
        }
        // 用户注册
        sysLoginService.register(registerBody);
        return R.ok();
    }

    /**
     * 登录页面租户下拉框
     *
     * @param body 可选入参（如 domain 过滤），可为空体
     * @param request 用于获取 referer/host
     * @return 租户列表
     */
    @RateLimiter(time = 60, count = 20, limitType = LimitType.IP)
    @PostMapping("/tenant/list")
    public R<LoginTenantVo> tenantList(@RequestBody(required = false) TenantListBody body,
                                      HttpServletRequest request) throws Exception {
        // 返回对象
        LoginTenantVo result = new LoginTenantVo();
        boolean enable = TenantHelper.isEnable();
        result.setTenantEnabled(enable);
        // 如果未开启租户这直接返回
        if (!enable) {
            return R.ok(result);
        }

        List<RemoteTenantVo> tenantList = remoteTenantService.queryList();
        List<TenantListVo> voList = MapstructUtils.convert(tenantList, TenantListVo.class);
        try {
            // 如果只超管返回所有租户
            if (LoginHelper.isSuperAdmin()) {
                result.setVoList(voList);
                return R.ok(result);
            }
        } catch (NotLoginException ignored) {
        }

        // 获取域名
        String host;
        String referer = request.getHeader("referer");
        if (StringUtils.isNotBlank(referer)) {
            // 这里从referer中取值是为了本地使用hosts添加虚拟域名，方便本地环境调试
            host = referer.split("//")[1].split("/")[0];
        } else {
            host = new URL(request.getRequestURL().toString()).getHost();
        }
        // 若 body 中指定了 domain 则优先使用，否则用 request 解析的 host
        String filterDomain = (body != null && StringUtils.isNotBlank(body.getDomain())) ? body.getDomain() : host;
        // 根据域名进行筛选
        List<TenantListVo> list = StreamUtils.filter(voList, vo ->
            StringUtils.equalsIgnoreCase(vo.getDomain(), filterDomain));
        result.setVoList(CollUtil.isNotEmpty(list) ? list : voList);
        return R.ok(result);
    }

}
