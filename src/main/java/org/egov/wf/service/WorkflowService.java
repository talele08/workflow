package org.egov.wf.service;

import org.egov.common.contract.request.RequestInfo;
import org.egov.wf.repository.BusinessServiceRepository;
import org.egov.wf.repository.WorKflowRepository;
import org.egov.wf.util.WorkflowUtil;
import org.egov.wf.validator.WorkflowValidator;
import org.egov.wf.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;


@Service
public class WorkflowService {

    private MDMSService mdmsService;

    private TransitionService transitionService;

    private EnrichmentService enrichmentService;

    private WorkflowValidator workflowValidator;

    private StatusUpdateService statusUpdateService;

    private WorKflowRepository workflowRepository;

    private WorkflowUtil util;

    private BusinessServiceRepository businessServiceRepository;


    @Autowired
    public WorkflowService(MDMSService mdmsService, TransitionService transitionService,
                           EnrichmentService enrichmentService, WorkflowValidator workflowValidator,
                           StatusUpdateService statusUpdateService, WorKflowRepository workflowRepository,
                           WorkflowUtil util,BusinessServiceRepository businessServiceRepository) {
        this.mdmsService = mdmsService;
        this.transitionService = transitionService;
        this.enrichmentService = enrichmentService;
        this.workflowValidator = workflowValidator;
        this.statusUpdateService = statusUpdateService;
        this.workflowRepository = workflowRepository;
        this.util = util;
        this.businessServiceRepository = businessServiceRepository;
    }


    /**
     * Creates or updates the processInstance
     * @param request The incoming request for workflow transition
     * @return The list of processInstance objects after taking action
     */
    public List<ProcessInstance> transition(ProcessInstanceRequest request){
        RequestInfo requestInfo = request.getRequestInfo();

      /*  String businessServiceName = request.getProcessInstances().get(0).getBusinessService();
        BusinessService businessService = util.getBusinessService(mdmsData,businessServiceName);
        List<ProcessStateAndAction> processStateAndActions = transitionService.getProcessStateAndActions(request,businessService);
*/
        List<ProcessStateAndAction> processStateAndActions = transitionService.getProcessStateAndActions(request,true);
        workflowValidator.validateRequest(requestInfo,processStateAndActions);
        enrichmentService.enrichProcessRequest(requestInfo,processStateAndActions);
        statusUpdateService.updateStatus(requestInfo,processStateAndActions);
        return request.getProcessInstances();
    }


    /**
     * Fetches ProcessInstances from db based on processSearchCriteria
     * @param requestInfo The RequestInfo of the search request
     * @param criteria The object containing Search params
     * @return List of processInstances based on search criteria
     */
    public List<ProcessInstance> search(RequestInfo requestInfo,ProcessInstanceSearchCriteria criteria){
        List<ProcessInstance> processInstances;
        if(criteria.isNull())
            processInstances = getUserBasedProcessInstances(requestInfo,criteria);
        else processInstances = workflowRepository.getProcessInstances(criteria);
        if(CollectionUtils.isEmpty(processInstances))
            return processInstances;
        enrichmentService.enrichUsersFromSearch(processInstances);
        enrichmentService.enrichNextActionForSearch(requestInfo,processInstances);
        return processInstances;
    }


    /**
     * Searches the processInstances based on user and its roles
     * @param requestInfo The RequestInfo of the search request
     * @param criteria The object containing Search params
     * @return List of processInstances based on search criteria
     */
    private List<ProcessInstance> getUserBasedProcessInstances(RequestInfo requestInfo,
                                       ProcessInstanceSearchCriteria criteria){
        BusinessServiceSearchCriteria businessServiceSearchCriteria = new BusinessServiceSearchCriteria();
        businessServiceSearchCriteria.setTenantId(criteria.getTenantId());
        List<BusinessService> businessServices = businessServiceRepository.getBusinessServices(businessServiceSearchCriteria);
        List<String> actionableStatuses = util.getActionableStatusesForRole(requestInfo,businessServices);
        criteria.setAssignee(requestInfo.getUserInfo().getUuid());
        criteria.setStatus(actionableStatuses);
        List<ProcessInstance> processInstancesForAssignee = workflowRepository.getProcessInstancesForAssignee(criteria);
        List<ProcessInstance> processInstancesForStatus = workflowRepository.getProcessInstancesForStatus(criteria);
        Set<ProcessInstance> processInstanceSet = new LinkedHashSet<>(processInstancesForStatus);
        processInstanceSet.addAll(processInstancesForAssignee);
        return new LinkedList<>(processInstanceSet);
    }











}
