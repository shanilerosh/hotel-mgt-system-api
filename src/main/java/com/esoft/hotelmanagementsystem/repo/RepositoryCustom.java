package com.esoft.hotelmanagementsystem.repo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface RepositoryCustom {

    <T> Page<T> executeCustomQuery(Pageable page,
                                   String sql, MapSqlParameterSource mapSqlParameterSource,
                                   Class<T> type, Map<String, String> fieldNameVsColumnNameMap,
                                   String countQuery);

}
