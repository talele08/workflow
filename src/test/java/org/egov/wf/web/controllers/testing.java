package org.egov.wf.web.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.wf.web.models.ProcessInstance;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class testing {


    @Test
    public void name() {
   /*     List<String> a;

        String fileName = "/home/aniket/test";
        try {
            String data = new String(Files.readAllBytes(Paths.get(fileName)));
            ObjectMapper mapper = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

            List<ProcessInstance> list = mapper.readValue(data, new TypeReference<List<ProcessInstance>>(){});
            HashSet<ProcessInstance> set = new HashSet();
            set.addAll(list);
            System.out.println("set: "+set.size()+ " list: "+list.size());

        }catch (Exception e)
        {e.printStackTrace();}*/


    }
}
