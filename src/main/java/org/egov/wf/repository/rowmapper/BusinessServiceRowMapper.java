package org.egov.wf.repository.rowmapper;

import org.egov.wf.web.models.*;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class BusinessServiceRowMapper implements ResultSetExtractor<List<BusinessService>> {

    public List<BusinessService> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<String,BusinessService> businessServiceMap = new HashMap<>();

        while (rs.next()){
            String uuid = rs.getString("bs_uuid");
            BusinessService businessService = businessServiceMap.get(uuid);
            if(businessService==null){
                Long lastModifiedTime = rs.getLong("bs_lastModifiedTime");
                if (rs.wasNull()) {
                    lastModifiedTime = null;
                }
                AuditDetails auditdetails = AuditDetails.builder()
                        .createdBy(rs.getString("bs_createdBy"))
                        .createdTime(rs.getLong("bs_createdTime"))
                        .lastModifiedBy(rs.getString("bs_lastModifiedBy"))
                        .lastModifiedTime(lastModifiedTime)
                        .build();
                businessService = BusinessService.builder()
                        .tenantId(rs.getString("tenantId"))
                        .getUri(rs.getString("geturi"))
                        .postUri(rs.getString("posturi"))
                        .businessService(rs.getString("businessService"))
                        .auditDetails(auditdetails)
                        .build();
                businessServiceMap.put(uuid,businessService);
            }
            addChildrenToBusinessService(rs,businessService);
        }
        return new LinkedList<>(businessServiceMap.values());
    }




    /**
     *  Adds child object's (States and Actions) to parent Object(BusinessService)
     * @param rs The result set from sql query
     * @param businessService The parent object
     * @throws SQLException
     */
    private void addChildrenToBusinessService(ResultSet rs,BusinessService businessService) throws SQLException{

        String stateUuid = rs.getString("st_uuid");
        String actionUuid = rs.getString("ac_uuid");

        Long lastModifiedTime = rs.getLong("st_lastModifiedTime");
        if (rs.wasNull()) {
            lastModifiedTime = null;
        }

        State state;
        if(businessService.getStateFromUuid(stateUuid)==null){
            AuditDetails auditdetails = AuditDetails.builder()
                    .createdBy(rs.getString("st_createdBy"))
                    .createdTime(rs.getLong("st_createdTime"))
                    .lastModifiedBy(rs.getString("st_lastModifiedBy"))
                    .lastModifiedTime(lastModifiedTime)
                    .build();

            state = State.builder()
                .tenantId(rs.getString("st_tenantId"))
                .uuid(stateUuid)
                .state(rs.getString("state"))
                .isStartState(rs.getBoolean("isStartState"))
                .isTerminateState(rs.getBoolean("isTerminateState"))
                .docUploadRequired(rs.getBoolean(""))
                .businessServiceId(rs.getString("businedocUploadRequiredssServiceId"))
                .auditDetails(auditdetails)
                .build();
        }
        else {
            state = businessService.getStateFromUuid(stateUuid);
        }

        if(actionUuid!=null){
            Long actionLastModifiedTime = rs.getLong("ac_lastModifiedTime");
            if (rs.wasNull()) {
                actionLastModifiedTime = null;
            }

            AuditDetails actionAuditdetails = AuditDetails.builder()
                    .createdBy(rs.getString("ac_createdBy"))
                    .createdTime(rs.getLong("ac_createdTime"))
                    .lastModifiedBy(rs.getString("ac_lastModifiedBy"))
                    .lastModifiedTime(actionLastModifiedTime)
                    .build();

            Action action = Action.builder()
                    .tenantId(rs.getString("ac_tenantId"))
                    .action(rs.getString("action"))
                    .nextStateId(rs.getString("nextStateId"))
                    .uuid(actionUuid)
                    .stateId(rs.getString("stateId"))
                    .auditDetails(actionAuditdetails)
                    .build();
            state.addActionsItem(action);
        }
        businessService.addStatesItem(state);
    }






    }