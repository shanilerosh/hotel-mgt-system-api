package com.esoft.hotelmanagementsystem.repo.impl;

import com.esoft.hotelmanagementsystem.repo.RepositoryCustom;
import com.esoft.hotelmanagementsystem.util.ReflectUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author ShanilErosh
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class RepoCustomImpl implements RepositoryCustom {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public <T> Page<T> executeCustomQuery(Pageable page, String sql, MapSqlParameterSource mapSqlParameterSource, Class<T> type, Map<String, String> fieldNameVsColumnNameMap, String countQuery) {
        List<T> list = namedParameterJdbcTemplate.query(sql,
                mapSqlParameterSource,
                (rs, rowNum) ->
                        this.getResultObject(rs, fieldNameVsColumnNameMap, type)
        );

        return new PageImpl<>(list, page, null != countQuery ? getCountGeneric(mapSqlParameterSource, countQuery) : 1);
    }

    public Long getCountGeneric(MapSqlParameterSource mapSqlParameterSource, String sql) {
        try {
            return namedParameterJdbcTemplate.queryForObject(sql, mapSqlParameterSource, Long.class);
        } catch (EmptyResultDataAccessException e) {
            return 0L;
        }
    }

    private <T> T getResultObject(ResultSet rs, Map<String, String> fieldNameVsColumnNameMap, Class<T> type) {
        T instance = ReflectUtil.newInstanceOf(type);


        List<Field> fields = Arrays.asList(type.getSuperclass().getDeclaredFields());
        List<Field> fields1 = Arrays.asList(type.getDeclaredFields());

        fields.forEach(field -> {
            extracted(rs, fieldNameVsColumnNameMap, instance, field);
        });
        fields1.forEach(field -> {
            extracted(rs, fieldNameVsColumnNameMap, instance, field);
        });
        return instance;
    }

    private <T> void extracted(ResultSet rs, Map<String, String> fieldNameVsColumnNameMap, T instance, Field field) {
        try {
            if (null != fieldNameVsColumnNameMap && fieldNameVsColumnNameMap.containsKey(field.getName())) {
                Object castValue = ReflectUtil.castValue(field, rs.getString(fieldNameVsColumnNameMap.get(field.getName())));
                ReflectUtil.setFieldData(instance, field, castValue);
            } else {
                Object castValue = ReflectUtil.castValue(field, rs.getString(field.getName()));
                ReflectUtil.setFieldData(instance, field, castValue);
            }
        } catch (Exception ignored) {

        }
    }
}
