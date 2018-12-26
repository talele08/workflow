package org.egov.wf.service;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.Role;
import org.egov.common.contract.request.User;
import org.egov.tracer.model.CustomException;
import org.egov.wf.util.WorkflowUtil;
import org.egov.wf.web.models.AuditDetails;
import org.egov.wf.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;


@Service
public class EnrichmentService {


    private WorkflowUtil util;

    private UserService userService;

    private TransitionService transitionService;

    @Autowired
    public EnrichmentService(WorkflowUtil util, UserService userService,TransitionService transitionService) {
        this.util = util;
        this.userService = userService;
        this.transitionService = transitionService;
    }




    /**
     * Enriches incoming request
     * @param requestInfo The RequestInfo of the request
     * @param processStateAndActions List of ProcessStateAndAction containing ProcessInstance to be created
     */
    public void enrichProcessRequest(RequestInfo requestInfo,List<ProcessStateAndAction> processStateAndActions){
        AuditDetails auditDetails = util.getAuditDetails(requestInfo.getUserInfo().getUuid(),true);
        processStateAndActions.forEach(processStateAndAction -> {
            String tenantId = processStateAndAction.getProcessInstanceFromRequest().getTenantId();
            processStateAndAction.getProcessInstanceFromRequest().setId(UUID.randomUUID().toString());
            if(processStateAndAction.getAction().getNextState().equalsIgnoreCase(processStateAndAction.getAction().getCurrentState())){
                auditDetails.setCreatedBy(processStateAndAction.getProcessInstanceFromDb().getAuditDetails().getCreatedBy());
                auditDetails.setCreatedTime(processStateAndAction.getProcessInstanceFromDb().getAuditDetails().getCreatedTime());
            }
            processStateAndAction.getProcessInstanceFromRequest().setAuditDetails(auditDetails);
            processStateAndAction.getProcessInstanceFromRequest().setAssigner(requestInfo.getUserInfo());
            if(!CollectionUtils.isEmpty(processStateAndAction.getProcessInstanceFromRequest().getDocuments())){
                processStateAndAction.getProcessInstanceFromRequest().getDocuments().forEach(document -> {
                    document.setAuditDetails(auditDetails);
                    document.setTenantId(tenantId);
                    document.setId(UUID.randomUUID().toString());
                });
            }
            processStateAndAction.getProcessInstanceFromRequest().setSla(processStateAndAction.getResultantState().getSla());
            setNextActions(requestInfo,processStateAndActions,true);
        });
        enrichUsers(processStateAndActions);
    }





    /**
     * Enriches the processInstanceFromRequest with next possible action depending on current currentState
     * @param requestInfo The RequestInfo of the request
     * @param processStateAndActions
     */
    private void setNextActions(RequestInfo requestInfo,List<ProcessStateAndAction> processStateAndActions,Boolean isTransition){
        List<Role> roles = requestInfo.getUserInfo().getRoles();

        processStateAndActions.forEach(processStateAndAction -> {
            State state;
            if(isTransition)
             state = processStateAndAction.getResultantState();
            else state = processStateAndAction.getCurrentState();
            List<String> nextAction = new LinkedList<>();
            if(!CollectionUtils.isEmpty( state.getActions())){
                state.getActions().forEach(action -> {
                    if(util.isRoleAvailable(roles,action.getRoles()))
                        nextAction.add(action.getAction());
                });
            }
            processStateAndAction.getProcessInstanceFromRequest().setNextActions(nextAction);
        });
    }

    /**
     * Enriches the assignee and assigner user object from user search response
     * @param processStateAndActions The List of ProcessStateAndAction containing processInstanceFromRequest to be enriched
     */
    public void enrichUsers(List<ProcessStateAndAction> processStateAndActions){
        List<String> uuids = new LinkedList<>();
        processStateAndActions.forEach(processStateAndAction -> {
            if(processStateAndAction.getProcessInstanceFromRequest().getAssignee()!=null)
                uuids.add(processStateAndAction.getProcessInstanceFromRequest().getAssignee().getUuid());
            uuids.add(processStateAndAction.getProcessInstanceFromRequest().getAssigner().getUuid());
        });

        Map<String,User> idToUserMap = userService.searchUser(uuids);
        Map<String,String> errorMap = new HashMap<>();
        processStateAndActions.forEach(processStateAndAction -> {
            User assignee=null,assigner;
            if(processStateAndAction.getProcessInstanceFromRequest().getAssignee()!=null)
                 assignee = idToUserMap.get(processStateAndAction.getProcessInstanceFromRequest().getAssignee().getUuid());
            assigner = idToUserMap.get(processStateAndAction.getProcessInstanceFromRequest().getAssigner().getUuid());
            if(processStateAndAction.getProcessInstanceFromRequest().getAssignee()!=null && assignee==null)
                errorMap.put("INVALID UUID","User not found for uuid: "+processStateAndAction.getProcessInstanceFromRequest().getAssignee().getUuid());
            if(assigner==null)
                errorMap.put("INVALID UUID","User not found for uuid: "+processStateAndAction.getProcessInstanceFromRequest().getAssigner().getUuid());
            processStateAndAction.getProcessInstanceFromRequest().setAssignee(assignee);
            processStateAndAction.getProcessInstanceFromRequest().setAssigner(assigner);
        });
        if(!errorMap.isEmpty())
            throw new CustomException(errorMap);
    }


    /**
     * Enriches processInstanceFromRequest from the search response
     * @param processInstances The list of processInstances from search
     */
    public void enrichUsersFromSearch(List<ProcessInstance> processInstances){
        List<String> uuids = new LinkedList<>();
        processInstances.forEach(processInstance -> {
            if(processInstance.getAssignee()!=null)
                uuids.add(processInstance.getAssignee().getUuid());
            uuids.add(processInstance.getAssigner().getUuid());
        });
        Map<String,User> idToUserMap = userService.searchUser(uuids);
        Map<String,String> errorMap = new HashMap<>();
        processInstances.forEach(processInstance -> {
            User assignee=null,assigner;
            if(processInstance.getAssignee()!=null)
                assignee = idToUserMap.get(processInstance.getAssignee().getUuid());
            assigner = idToUserMap.get(processInstance.getAssigner().getUuid());
            if(processInstance.getAssignee()!=null && assignee==null)
                errorMap.put("INVALID UUID","User not found for uuid: "+processInstance.getAssignee().getUuid());
            if(assigner==null)
                errorMap.put("INVALID UUID","User not found for uuid: "+processInstance.getAssigner().getUuid());
            processInstance.setAssignee(assignee);
            processInstance.setAssigner(assigner);
        });
        if(!errorMap.isEmpty())
            throw new CustomException(errorMap);
    }


    public void enrichNextActionForSearch(RequestInfo requestInfo,List<ProcessInstance> processInstances){
        List<ProcessStateAndAction> processStateAndActions =
                transitionService.getProcessStateAndActions(new ProcessInstanceRequest(requestInfo,processInstances),false);
        setNextActions(requestInfo,processStateAndActions,false);
    }


    /**
     * Enriches the incoming list of businessServices
     * @param request The BusinessService request to be enriched
     */
    public void enrichCreateBusinessService(BusinessServiceRequest request){
        RequestInfo requestInfo = request.getRequestInfo();
        List<BusinessService> businessServices = request.getBusinessServices();
        AuditDetails auditDetails = util.getAuditDetails(requestInfo.getUserInfo().getUuid(),true);
        businessServices.forEach(businessService -> {
            businessService.setUuid(UUID.randomUUID().toString());
            businessService.setAuditDetails(auditDetails);
            businessService.getStates().forEach(state -> {
                state.setAuditDetails(auditDetails);
                state.setUuid(UUID.randomUUID().toString());
                if(!CollectionUtils.isEmpty(state.getActions()))
                    state.getActions().forEach(action -> {
                        action.setAuditDetails(auditDetails);
                        action.setUuid(UUID.randomUUID().toString());
                        action.setCurrentState(state.getUuid());
                    });
            });
            enrichNextState(businessService);
        });
    }

    /**
     * Enriches update request
     * @param request The update request
     */
    public void enrichUpdateBusinessService(BusinessServiceRequest request){
        RequestInfo requestInfo = request.getRequestInfo();
        List<BusinessService> businessServices = request.getBusinessServices();
        AuditDetails updateAuditDetails = util.getAuditDetails(requestInfo.getUserInfo().getUuid(),false);
        AuditDetails createAuditDetails = util.getAuditDetails(requestInfo.getUserInfo().getUuid(),false);
        businessServices.forEach(businessService -> {
            businessService.setUuid(UUID.randomUUID().toString());
            businessService.setAuditDetails(updateAuditDetails);
            businessService.getStates().forEach(state -> {
                if(state.getUuid()==null){
                    state.setAuditDetails(createAuditDetails);
                    state.setUuid(UUID.randomUUID().toString());
                }
                else state.setAuditDetails(updateAuditDetails);
                if(!CollectionUtils.isEmpty(state.getActions()))
                    state.getActions().forEach(action -> {
                        if(action.getUuid()==null){
                            action.setAuditDetails(createAuditDetails);
                            action.setUuid(UUID.randomUUID().toString());
                            action.setCurrentState(state.getUuid());
                        }
                        else action.setAuditDetails(updateAuditDetails);
                    });
            });
            enrichNextState(businessService);
        });
    }

    /**
     * Enriches the nextState varibale in BusinessService
     * @param businessService The businessService whose action objects are to be enriched
     */
    private void enrichNextState(BusinessService businessService){
        Map<String,String> statusToUuidMap = new HashMap<>();
        businessService.getStates().forEach(state -> {
            statusToUuidMap.put(state.getState(),state.getUuid());
        });
        businessService.getStates().forEach(state -> {
            if(!CollectionUtils.isEmpty(state.getActions())){
                state.getActions().forEach(action -> {
                    action.setNextState(statusToUuidMap.get(action.getNextState()));
                });
            }
        });
    }


}
