package org.egov.wf.web.controllers;


import org.egov.wf.web.models.ErrorRes2;
import org.egov.wf.web.models.ProcessInstanceRequest;
import org.egov.wf.web.models.ProcessInstanceResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import java.io.IOException;
import java.util.*;

    import javax.validation.constraints.*;
    import javax.validation.Valid;
    import javax.servlet.http.HttpServletRequest;

@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2018-12-04T11:26:25.532+05:30")

@Controller
    @RequestMapping("/egov-wf")
    public class V1ApiController{

        private final ObjectMapper objectMapper;

        private final HttpServletRequest request;

        @Autowired
        public V1ApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
        }

                @RequestMapping(value="/v1/process/_transition", method = RequestMethod.POST)
                public ResponseEntity<ProcessInstanceResponse> v1ProcessTransitionPost(@ApiParam(value = "Details for the new TradeLicense(s) + RequestInfo meta data." ,required=true )  @Valid @RequestBody ProcessInstanceRequest processInstanceRequest) {
                        String accept = request.getHeader("Accept");
                            if (accept != null && accept.contains("application/json")) {
                            try {
                            return new ResponseEntity<ProcessInstanceResponse>(objectMapper.readValue("{  \"ResponseInfo\" : {    \"ver\" : \"ver\",    \"resMsgId\" : \"resMsgId\",    \"msgId\" : \"msgId\",    \"apiId\" : \"apiId\",    \"ts\" : 0,    \"status\" : \"SUCCESSFUL\"  },  \"ProceInstance\" : [ {    \"businessService\" : \"businessService\",    \"documents\" : [ {      \"documentType\" : \"documentType\",      \"documentUid\" : \"documentUid\",      \"auditDetails\" : {        \"lastModifiedTime\" : 1,        \"createdBy\" : \"createdBy\",        \"lastModifiedBy\" : \"lastModifiedBy\",        \"createdTime\" : 6      },      \"tenantId\" : \"tenantId\",      \"fileStoreId\" : \"fileStoreId\"    }, {      \"documentType\" : \"documentType\",      \"documentUid\" : \"documentUid\",      \"auditDetails\" : {        \"lastModifiedTime\" : 1,        \"createdBy\" : \"createdBy\",        \"lastModifiedBy\" : \"lastModifiedBy\",        \"createdTime\" : 6      },      \"tenantId\" : \"tenantId\",      \"fileStoreId\" : \"fileStoreId\"    } ],    \"currentStatus\" : \"currentStatus\",    \"businessId\" : \"businessId\",    \"assigner\" : \"assigner\",    \"sla\" : 5,    \"auditDetails\" : {      \"lastModifiedTime\" : 1,      \"createdBy\" : \"createdBy\",      \"lastModifiedBy\" : \"lastModifiedBy\",      \"createdTime\" : 6    },    \"tenantId\" : \"tenantId\",    \"action\" : \"action\",    \"nextActions\" : [ \"nextActions\", \"nextActions\" ],    \"comment\" : \"comment\",    \"id\" : \"id\",    \"assignee\" : \"assignee\",    \"entity\" : \"{}\",    \"status\" : \"status\",    \"previousStatus\" : \"previousStatus\"  }, {    \"businessService\" : \"businessService\",    \"documents\" : [ {      \"documentType\" : \"documentType\",      \"documentUid\" : \"documentUid\",      \"auditDetails\" : {        \"lastModifiedTime\" : 1,        \"createdBy\" : \"createdBy\",        \"lastModifiedBy\" : \"lastModifiedBy\",        \"createdTime\" : 6      },      \"tenantId\" : \"tenantId\",      \"fileStoreId\" : \"fileStoreId\"    }, {      \"documentType\" : \"documentType\",      \"documentUid\" : \"documentUid\",      \"auditDetails\" : {        \"lastModifiedTime\" : 1,        \"createdBy\" : \"createdBy\",        \"lastModifiedBy\" : \"lastModifiedBy\",        \"createdTime\" : 6      },      \"tenantId\" : \"tenantId\",      \"fileStoreId\" : \"fileStoreId\"    } ],    \"currentStatus\" : \"currentStatus\",    \"businessId\" : \"businessId\",    \"assigner\" : \"assigner\",    \"sla\" : 5,    \"auditDetails\" : {      \"lastModifiedTime\" : 1,      \"createdBy\" : \"createdBy\",      \"lastModifiedBy\" : \"lastModifiedBy\",      \"createdTime\" : 6    },    \"tenantId\" : \"tenantId\",    \"action\" : \"action\",    \"nextActions\" : [ \"nextActions\", \"nextActions\" ],    \"comment\" : \"comment\",    \"id\" : \"id\",    \"assignee\" : \"assignee\",    \"entity\" : \"{}\",    \"status\" : \"status\",    \"previousStatus\" : \"previousStatus\"  } ]}", ProcessInstanceResponse.class), HttpStatus.NOT_IMPLEMENTED);
                            } catch (IOException e) {
                            return new ResponseEntity<ProcessInstanceResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
                            }
                            }

                        return new ResponseEntity<ProcessInstanceResponse>(HttpStatus.NOT_IMPLEMENTED);
                }

                @RequestMapping(value="/v1/_search", method = RequestMethod.POST)
                public ResponseEntity<ProcessInstanceResponse> v1SearchPost(@NotNull @ApiParam(value = "Unique id for a tenant.", required = true) @Valid @RequestParam(value = "tenantId", required = true) String tenantId, @ApiParam(value = "Parameter to carry Request metadata in the request body"  )  @Valid @RequestBody RequestInfo requestInfo, @Size(max=50) @ApiParam(value = "unique identifier of trade licence") @Valid @RequestParam(value = "ids", required = false) List<Long> ids, @Size(min=2,max=64) @ApiParam(value = "Unique application number for a trade license application.") @Valid @RequestParam(value = "businessService", required = false) String businessService, @Size(min=2,max=64) @ApiParam(value = "The unique  license number  for a Trade license.") @Valid @RequestParam(value = "businessId", required = false) String businessId, @Size(min=2,max=64) @ApiParam(value = "The unique  Old license number for a Trade license.") @Valid @RequestParam(value = "assignee", required = false) String assignee) {
                        String accept = request.getHeader("Accept");
                            if (accept != null && accept.contains("application/json")) {
                            try {
                            return new ResponseEntity<ProcessInstanceResponse>(objectMapper.readValue("{  \"ResponseInfo\" : {    \"ver\" : \"ver\",    \"resMsgId\" : \"resMsgId\",    \"msgId\" : \"msgId\",    \"apiId\" : \"apiId\",    \"ts\" : 0,    \"status\" : \"SUCCESSFUL\"  },  \"ProceInstance\" : [ {    \"businessService\" : \"businessService\",    \"documents\" : [ {      \"documentType\" : \"documentType\",      \"documentUid\" : \"documentUid\",      \"auditDetails\" : {        \"lastModifiedTime\" : 1,        \"createdBy\" : \"createdBy\",        \"lastModifiedBy\" : \"lastModifiedBy\",        \"createdTime\" : 6      },      \"tenantId\" : \"tenantId\",      \"fileStoreId\" : \"fileStoreId\"    }, {      \"documentType\" : \"documentType\",      \"documentUid\" : \"documentUid\",      \"auditDetails\" : {        \"lastModifiedTime\" : 1,        \"createdBy\" : \"createdBy\",        \"lastModifiedBy\" : \"lastModifiedBy\",        \"createdTime\" : 6      },      \"tenantId\" : \"tenantId\",      \"fileStoreId\" : \"fileStoreId\"    } ],    \"currentStatus\" : \"currentStatus\",    \"businessId\" : \"businessId\",    \"assigner\" : \"assigner\",    \"sla\" : 5,    \"auditDetails\" : {      \"lastModifiedTime\" : 1,      \"createdBy\" : \"createdBy\",      \"lastModifiedBy\" : \"lastModifiedBy\",      \"createdTime\" : 6    },    \"tenantId\" : \"tenantId\",    \"action\" : \"action\",    \"nextActions\" : [ \"nextActions\", \"nextActions\" ],    \"comment\" : \"comment\",    \"id\" : \"id\",    \"assignee\" : \"assignee\",    \"entity\" : \"{}\",    \"status\" : \"status\",    \"previousStatus\" : \"previousStatus\"  }, {    \"businessService\" : \"businessService\",    \"documents\" : [ {      \"documentType\" : \"documentType\",      \"documentUid\" : \"documentUid\",      \"auditDetails\" : {        \"lastModifiedTime\" : 1,        \"createdBy\" : \"createdBy\",        \"lastModifiedBy\" : \"lastModifiedBy\",        \"createdTime\" : 6      },      \"tenantId\" : \"tenantId\",      \"fileStoreId\" : \"fileStoreId\"    }, {      \"documentType\" : \"documentType\",      \"documentUid\" : \"documentUid\",      \"auditDetails\" : {        \"lastModifiedTime\" : 1,        \"createdBy\" : \"createdBy\",        \"lastModifiedBy\" : \"lastModifiedBy\",        \"createdTime\" : 6      },      \"tenantId\" : \"tenantId\",      \"fileStoreId\" : \"fileStoreId\"    } ],    \"currentStatus\" : \"currentStatus\",    \"businessId\" : \"businessId\",    \"assigner\" : \"assigner\",    \"sla\" : 5,    \"auditDetails\" : {      \"lastModifiedTime\" : 1,      \"createdBy\" : \"createdBy\",      \"lastModifiedBy\" : \"lastModifiedBy\",      \"createdTime\" : 6    },    \"tenantId\" : \"tenantId\",    \"action\" : \"action\",    \"nextActions\" : [ \"nextActions\", \"nextActions\" ],    \"comment\" : \"comment\",    \"id\" : \"id\",    \"assignee\" : \"assignee\",    \"entity\" : \"{}\",    \"status\" : \"status\",    \"previousStatus\" : \"previousStatus\"  } ]}", ProcessInstanceResponse.class), HttpStatus.NOT_IMPLEMENTED);
                            } catch (IOException e) {
                            return new ResponseEntity<ProcessInstanceResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
                            }
                            }

                        return new ResponseEntity<ProcessInstanceResponse>(HttpStatus.NOT_IMPLEMENTED);
                }

        }
