package org.egov.wf.repository;

import lombok.extern.slf4j.Slf4j;
import org.egov.wf.repository.querybuilder.BusinessServiceQueryBuilder;
import org.egov.wf.repository.rowmapper.BusinessServiceRowMapper;
import org.egov.wf.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class BusinessServiceRepository {


    private BusinessServiceQueryBuilder queryBuilder;

    private JdbcTemplate jdbcTemplate;

    private BusinessServiceRowMapper rowMapper;


    @Autowired
    public BusinessServiceRepository(BusinessServiceQueryBuilder queryBuilder, JdbcTemplate jdbcTemplate,
                                     BusinessServiceRowMapper rowMapper) {
        this.queryBuilder = queryBuilder;
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = rowMapper;
    }






    public List<BusinessService> getBusinessServices(BusinessServiceSearchCriteria criteria){
        List<Object> preparedStmtList = new ArrayList<>();
        String query = queryBuilder.getBusinessServices(criteria, preparedStmtList);
        log.info("Query BusinessServices: "+query);
        log.info("preparedStmtList: : "+preparedStmtList);
        return jdbcTemplate.query(query, preparedStmtList.toArray(), rowMapper);
    }






}
