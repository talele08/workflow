package org.egov.wf.service;

import org.apache.kafka.common.metrics.Stat;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.Role;
import org.egov.tracer.model.CustomException;
import org.egov.wf.repository.WorflowRepository;
import org.egov.wf.util.WorkflowUtil;
import org.egov.wf.web.models.AuditDetails;
import org.egov.wf.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;


@Service
public class EnrichmentService {


    private WorkflowUtil workflowUtil;
    private WorflowRepository repository;

    @Autowired
    public EnrichmentService(WorkflowUtil workflowUtil, WorflowRepository repository) {
        this.workflowUtil = workflowUtil;
        this.repository = repository;
    }


    /**
     * Enriches incoming request
     * @param requestInfo The RequestInfo of the request
     * @param processStateAndActions List of ProcessStateAndAction containing ProcessInstance to be created
     */
    public void enrichProcessRequest(RequestInfo requestInfo,List<ProcessStateAndAction> processStateAndActions){
        AuditDetails auditDetails = workflowUtil.getAuditDetails(requestInfo.getUserInfo().getUuid(),true);
        getStatus(processStateAndActions);
        processStateAndActions.forEach(processStateAndAction -> {
            processStateAndAction.getProcessInstance().setId(UUID.randomUUID().toString());
            processStateAndAction.getProcessInstance().setAuditDetails(auditDetails);
            processStateAndAction.getProcessInstance().setAssigner(requestInfo.getUserInfo().getUuid());
            if(!CollectionUtils.isEmpty(processStateAndAction.getProcessInstance().getDocuments())){
                processStateAndAction.getProcessInstance().getDocuments().forEach(document -> {
                    document.setAuditDetails(auditDetails);
                    document.setId(UUID.randomUUID().toString());
                });
            }
            setNextActions(requestInfo,processStateAndActions);
        });
    }




    /**
     * Searches the db and sets the status of the processInstance based on businessId
     * @param processStateAndActions The list of processStateAndActions containing ProcessInstance to be created
     */
    private void getStatus(List<ProcessStateAndAction> processStateAndActions){
        ProcessInstanceSearchCriteria criteria = new ProcessInstanceSearchCriteria();
        processStateAndActions.forEach(processStateAndAction -> {
            criteria.setTenantId(processStateAndAction.getProcessInstance().getTenantId());
            criteria.setBusinessId(processStateAndAction.getProcessInstance().getBusinessId());
            List<ProcessInstance> processInstances = repository.getProcessInstances(criteria);
            if(!CollectionUtils.isEmpty(processInstances)){
                processStateAndAction.getProcessInstance().setStatus(processInstances.get(0).getStatus());
            }
        });
    }


    /**
     * Enriches the processInstance with next possible action depenending on current state
     * @param requestInfo The RequestInfo of the request
     * @param processStateAndActions
     */
    private void setNextActions(RequestInfo requestInfo,List<ProcessStateAndAction> processStateAndActions){
        List<Role> roles = requestInfo.getUserInfo().getRoles();

        processStateAndActions.forEach(processStateAndAction -> {
            State state = processStateAndAction.getPostActionState();
            List<String> nextAction = new LinkedList<>();
            state.getActions().forEach(action -> {
                if(isRoleAvailable(roles,action.getRoles()))
                    nextAction.add(action.getAction());
            });
            processStateAndAction.getProcessInstance().setNextActions(nextAction);
        });

    }




    /**
     * Checks if the user has role allowed for the action
     * @param userRoles The roles available with the user
     * @param actionRoles The roles for which action is allowed
     * @return True if user can perform the action else false
     */
    private Boolean isRoleAvailable(List<Role> userRoles,List<String> actionRoles){
        Boolean flag = false;
        for(Role role : userRoles) {
            if (actionRoles.contains(role.getCode())) {
                flag = true;
                break;
            }
        }
        return flag;
    }







}
