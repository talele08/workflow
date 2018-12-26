package org.egov.wf.validator;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.Role;
import org.egov.tracer.model.CustomException;
import org.egov.wf.util.BusinessUtil;
import org.egov.wf.util.WorkflowUtil;
import org.egov.wf.web.models.Action;
import org.egov.wf.web.models.BusinessService;
import org.egov.wf.web.models.ProcessStateAndAction;
import org.egov.wf.web.models.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import java.util.*;


@Component
public class WorkflowValidator {



    private WorkflowUtil util;

    private BusinessUtil businessUtil;


    @Autowired
    public WorkflowValidator(WorkflowUtil util, BusinessUtil businessUtil) {
        this.util = util;
        this.businessUtil = businessUtil;
    }



    /**
     * Validates the request
     * @param requestInfo RequestInfo of the request
     * @param processStateAndActions The processStateAndActions containing processInstances to be validated
     */
    public void validateRequest(RequestInfo requestInfo, List<ProcessStateAndAction> processStateAndActions){
        String tenantId = processStateAndActions.get(0).getProcessInstanceFromRequest().getTenantId();
        String businessServiceCode = processStateAndActions.get(0).getProcessInstanceFromRequest().getBusinessService();
        BusinessService businessService = businessUtil.getBusinessService(tenantId,businessServiceCode);
        validateAction(requestInfo,processStateAndActions,businessService);
        validateDocuments(processStateAndActions);
    }


    /**
     * Validates if documents are required to perform currentState change
     * @param processStateAndActions ProcessStateAndAction to be validated
     */
    private void validateDocuments(List<ProcessStateAndAction> processStateAndActions){
        Map<String,String> errorMap = new HashMap<>();
        for (ProcessStateAndAction processStateAndAction : processStateAndActions){
            if(processStateAndAction.getResultantState().getDocUploadRequired()){
                if(CollectionUtils.isEmpty(processStateAndAction.getProcessInstanceFromRequest().getDocuments()))
                    errorMap.put("INVALID DOCUMENT","Documents cannot be null for status: "+processStateAndAction.getResultantState().getState());
            }
        }
        if(!errorMap.isEmpty())
            throw new CustomException(errorMap);
    }


    /**
     * Validates if the action can be performed
     * @param requestInfo The RequestInfo of the incoming request
     * @param processStateAndActions The processStateAndActions containing processInstances to be validated
     */
    private void validateAction(RequestInfo requestInfo,List<ProcessStateAndAction> processStateAndActions
             ,BusinessService businessService){
        Map<String,String> errorMap = new HashMap<>();
        List<Role> roles = requestInfo.getUserInfo().getRoles();
        for(ProcessStateAndAction processStateAndAction : processStateAndActions){
            Action action = processStateAndAction.getAction();
            if(action==null && !processStateAndAction.getCurrentState().getIsTerminateState())
                errorMap.put("INVALID ACTION","Action not found for businessIds: "+
                        processStateAndAction.getCurrentState().getBusinessServiceId());

            Boolean isRoleAvailable = util.isRoleAvailable(roles,action.getRoles());
            Boolean isStateChanging = (action.getCurrentState().equalsIgnoreCase( action.getNextState())) ? false : true;
            List<String> transitionRoles = getRolesForTransition(processStateAndAction.getCurrentState());
            Boolean isRoleAvailableForTransition = util.isRoleAvailable(roles,transitionRoles);
            Boolean isAssigneeUserInfo = false;
            if(processStateAndAction.getProcessInstanceFromDb()!=null && processStateAndAction.getProcessInstanceFromDb().getAssignee()!=null)
                isAssigneeUserInfo = processStateAndAction.getProcessInstanceFromDb().getAssignee().getUuid().equalsIgnoreCase(requestInfo.getUserInfo().getUuid());
            if(!isStateChanging && !isAssigneeUserInfo && !isRoleAvailableForTransition)
                throw new CustomException("INVALID MARK ACTION","The processInstanceFromRequest cannot be marked by the user");

            if(action!=null && isStateChanging && !isRoleAvailable)
                errorMap.put("INVALID ROLE","User is not authorized to perform action");
            if(action!=null && !isStateChanging && !util.isRoleAvailable(roles,util.rolesAllowedInService(businessService)))
                errorMap.put("INVALID ROLE","User is not authorized to perform action");

            if(processStateAndAction.getProcessInstanceFromRequest().getState()!=null &&
                    processStateAndAction.getResultantState().getUuid().equalsIgnoreCase(processStateAndAction.getProcessInstanceFromRequest().getState().getUuid())){
                if(processStateAndAction.getProcessInstanceFromRequest().getAssignee()==null)
                    errorMap.put("INVALID PROCESSINSTANCE","Assignee cannot be null for no state change for BusinessId: "
                            +processStateAndAction.getProcessInstanceFromRequest().getBusinessId());
            }
        }
        if(!errorMap.isEmpty())
            throw new CustomException(errorMap);
    }


    private List<String> getRolesForTransition(State state){
        List<String> transitionRoles = new LinkedList<>();
        if(!CollectionUtils.isEmpty(state.getActions())){
            state.getActions().forEach(action -> {
                if(!action.getCurrentState().equalsIgnoreCase(action.getNextState()))
                    transitionRoles.addAll(action.getRoles());
            });
        }
        return transitionRoles;
    }





}
