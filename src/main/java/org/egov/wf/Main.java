package org.egov.wf;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "org.egov.wf", "org.egov.wf.web.controllers" , "org.egov.wf.config"})
public class org.egov.codegen.Main {


    public static void main(String[] args) throws Exception {
        SpringApplication.run(org.egov.codegen.Main.class, args);
    }

}
