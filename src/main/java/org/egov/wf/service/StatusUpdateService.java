package org.egov.wf.service;

import org.egov.common.contract.request.RequestInfo;
import org.egov.wf.config.WorkflowConfig;
import org.egov.wf.producer.Producer;
import org.egov.wf.web.models.ProcessInstance;
import org.egov.wf.web.models.ProcessInstanceRequest;
import org.egov.wf.web.models.ProcessStateAndAction;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedList;
import java.util.List;

public class StatusUpdateService {

    private Producer producer;

    private WorkflowConfig config;


    @Autowired
    public StatusUpdateService(Producer producer, WorkflowConfig config) {
        this.producer = producer;
        this.config = config;
    }


    /**
     * Updates the status and pushes the request on kafka to persist
      * @param requestInfo
     * @param processStateAndActions
     */
    public void updateStatus(RequestInfo requestInfo,List<ProcessStateAndAction> processStateAndActions){

        for(ProcessStateAndAction processStateAndAction : processStateAndActions){
            processStateAndAction.getProcessInstance().setStatus(processStateAndAction.getPostActionState().getState());
        }
        List<ProcessInstance> processInstances = new LinkedList<>();
        processStateAndActions.forEach(processStateAndAction -> {
            processInstances.add(processStateAndAction.getProcessInstance());
        });
        ProcessInstanceRequest processInstanceRequest = new ProcessInstanceRequest(requestInfo,processInstances);
        producer.push(config.getSaveTopic(),processInstanceRequest);
    }







}
