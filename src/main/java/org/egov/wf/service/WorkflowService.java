package org.egov.wf.service;

import org.egov.tracer.model.CustomException;
import org.egov.wf.web.models.*;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;


@Service
public class WorkflowService {











    private List<ProcessStateAndAction> getProcessAndApplicables(ProcessInstanceRequest request, BusinessService businessService){
        List<ProcessStateAndAction> processStateAndActions = new LinkedList<>();

        for(ProcessInstance processInstance: request.getProcessInstances()){
            ProcessStateAndAction processStateAndAction = new ProcessStateAndAction();

            for(State state : businessService.getStates()){
                if(state.getUuid().equalsIgnoreCase(processInstance.getStatus())){
                    processStateAndAction.setCurrentState(state);
                    break;
                }
            }

            if(processStateAndAction.getCurrentState()==null)
                throw new CustomException("INVALID STATUS","No state found in config for the businessId: "
                        +processStateAndAction.getProcessInstance().getBusinessId() + " and status: "+
                        processStateAndAction.getProcessInstance().getStatus());

            for (Action action : processStateAndAction.getCurrentState().getActions()){
                if(action.getUuid().equalsIgnoreCase(processInstance.getAction())){
                    processStateAndAction.setAction(action);
                    break;
                }
            }

            if(processStateAndAction.getAction()==null)
                throw new CustomException("INVALID ACTION","Action "+processStateAndAction.getProcessInstance().getAction()
                        + " not found in config for the businessId: "
                        +processStateAndAction.getProcessInstance().getBusinessId());

            for(State state : businessService.getStates()){
                if(state.getUuid().equalsIgnoreCase(processStateAndAction.getAction().getNextStateId())){
                    processStateAndAction.setPostActionState(state);
                    break;
                }
            }

            processStateAndActions.add(processStateAndAction);

        }
        return processStateAndActions;
    }



}
