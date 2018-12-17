package org.egov.wf.service;

import org.egov.tracer.model.CustomException;
import org.egov.wf.repository.BusinessServiceRepository;
import org.egov.wf.web.models.Action;
import org.egov.wf.web.models.BusinessService;
import org.egov.wf.web.models.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.util.List;

@Service
public class BusinessUtilService {

    private BusinessServiceRepository businessServiceRepository;

    @Autowired
    public BusinessUtilService(BusinessServiceRepository businessServiceRepository) {
        this.businessServiceRepository = businessServiceRepository;
    }




    /**
     *  Fetches and validates the current state and action for the processInstance
     * @param processInstance The ProcessInstance whose current state and action have to be fetched
     * @return BusinessService containing the current state and action
     */
    public BusinessService getCurrentStateAndAction(ProcessInstance processInstance){
        List<BusinessService> businessServices = businessServiceRepository.getCurrentStateAndAction(processInstance);
        if(CollectionUtils.isEmpty(businessServices))
            throw new CustomException("INVALID PROCESSINSTANCE","No businessService found for the current state of processInstance");
        if(businessServices.size()!=1)
            throw new CustomException("INVALID PROCESSINSTANCE","Multiple businessService found for the current state of processInstance");
        if(CollectionUtils.isEmpty(businessServices.get(0).getStates()))
            throw new CustomException("INVALID PROCESSINSTANCE","No current State found for the processInstance");
        if(businessServices.get(0).getStates().size()!=1)
            throw new CustomException("INVALID PROCESSINSTANCE","Multiple current States found for the processInstance");
        if(businessServices.get(0).getStates().get(0).getActions().size()!=1)
            throw new CustomException("INVALID PROCESSINSTANCE","Multiple Actions found for the processInstance");
        return businessServices.get(0);
    }



    /**
     *  Fetches and validates the next state of the action object
     * @param action The Action object whose next state have to be fetched
     * @return BusinessService containing the next state
     */
    public BusinessService getResultantState(Action action){
        List<BusinessService> businessServices = businessServiceRepository.getResultantState(action);
        if(CollectionUtils.isEmpty(businessServices))
            throw new CustomException("INVALID PROCESSINSTANCE","No businessService found for the next state of processInstance");
        if(businessServices.size()!=1)
            throw new CustomException("INVALID PROCESSINSTANCE","Multiple businessService found for the next state of processInstance");
        if(CollectionUtils.isEmpty(businessServices.get(0).getStates()))
            throw new CustomException("INVALID PROCESSINSTANCE","No next State found for the processInstance");
        if(businessServices.get(0).getStates().size()!=1)
            throw new CustomException("INVALID PROCESSINSTANCE","Multiple next States found for the processInstance");
        return businessServices.get(0);
    }




}
