package org.egov.wf.repository.querybuilder;

import org.egov.wf.web.models.BusinessServiceSearchCriteria;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class BusinessServiceQueryBuilder {



     private static final String INNER_JOIN = " INNER JOIN ";

     private static final String LEFT_OUTER_JOIN = " LEFT OUTER JOIN ";

     private static final String BASE_QUERY = "SELECT bs.*,st.*,ac.* FROM eg_wf_businessService_v2 bs " +
            INNER_JOIN + " eg_wf_state_v2 st ON st.businessServiceId = bs.businessService " +
            LEFT_OUTER_JOIN  + " eg_wf_action_v2 ac ON ac.stateId = st.uuid WHERE ";



    public String getStateAndActionSearchQuery(BusinessServiceSearchCriteria criteria, List<Object> preparedStmtList) {

        StringBuilder builder = new StringBuilder(BASE_QUERY);

        builder.append(" bs.tenantid=? ");
        preparedStmtList.add(criteria.getTenantId());

        builder.append(" AND bs.businessService=? ");
        preparedStmtList.add(criteria.getBusinessService());

        if(criteria.getStateUuid()!=null){
            builder.append(" AND st.uuid=? ");
            preparedStmtList.add(criteria.getStateUuid());
        }
        else {
            builder.append(" AND st.state IS NULL ");
        }

        builder.append(" AND ac.action=? ");
        preparedStmtList.add(criteria.getAction());

        return builder.toString();
    }


    public String getResultantState(BusinessServiceSearchCriteria criteria, List<Object> preparedStmtList){
        StringBuilder builder = new StringBuilder(BASE_QUERY);
        builder.append(" st.uuid=? ");
        preparedStmtList.add(criteria.getTenantId());
        return builder.toString();
    }


}
