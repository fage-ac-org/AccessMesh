package org.dromara.permission.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * PostgreSQL BIGINT[] 与 Java String（逗号分隔）的 TypeHandler
 *
 * @author RuoYi-Cloud-Plus
 */
@MappedTypes(String.class)
public class PgLongArrayToStringTypeHandler extends BaseTypeHandler<String> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
        if (parameter == null || parameter.isEmpty()) {
            ps.setArray(i, null);
            return;
        }
        Long[] longs = Arrays.stream(parameter.split(","))
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .map(Long::parseLong)
            .toArray(Long[]::new);
        ps.setObject(i, longs, java.sql.Types.ARRAY);
    }

    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return arrayToString(rs.getArray(columnName));
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return arrayToString(rs.getArray(columnIndex));
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return arrayToString(cs.getArray(columnIndex));
    }

    private static String arrayToString(java.sql.Array arr) throws SQLException {
        if (arr == null) {
            return null;
        }
        Object o = arr.getArray();
        if (o == null) {
            return null;
        }
        if (o instanceof long[]) {
            return Arrays.stream((long[]) o)
                .mapToObj(Long::toString)
                .collect(Collectors.joining(","));
        }
        if (o instanceof Long[]) {
            return Arrays.stream((Long[]) o)
                .map(Object::toString)
                .collect(Collectors.joining(","));
        }
        if (o instanceof Object[]) {
            return Arrays.stream((Object[]) o)
                .map(Object::toString)
                .collect(Collectors.joining(","));
        }
        return o.toString();
    }
}
