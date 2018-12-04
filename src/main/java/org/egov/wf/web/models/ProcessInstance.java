package org.egov.wf.web.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.egov.wf.web.models.AuditDetails;
import org.egov.wf.web.models.Document;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

/**
 * A Object holds the basic data for a Trade License
 */
@ApiModel(description = "A Object holds the basic data for a Trade License")
@Validated
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2018-12-04T11:26:25.532+05:30")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProcessInstance   {
        @JsonProperty("id")
        private String id = null;

        @NotNull
        @JsonProperty("tenantId")
        private String tenantId = null;

        @NotNull
        @JsonProperty("businessService")
        private String businessService = null;

        @NotNull
        @JsonProperty("businessId")
        private String businessId = null;

        @NotNull
        @JsonProperty("action")
        private String action = null;

        @JsonProperty("status")
        private String status = null;

        @JsonProperty("comment")
        private String comment = null;

        @JsonProperty("documents")
        @Valid
        private List<Document> documents = null;

        @JsonProperty("assigner")
        private String assigner = null;

        @JsonProperty("assignee")
        private String assignee = null;

        @JsonProperty("nextActions")
        @Valid
        private List<String> nextActions = null;

        @JsonProperty("sla")
        private Long sla = null;

        @JsonProperty("previousStatus")
        private String previousStatus = null;

        @JsonProperty("entity")
        private Object entity = null;

        @JsonProperty("auditDetails")
        private AuditDetails auditDetails = null;


        public ProcessInstance addDocumentsItem(Document documentsItem) {
            if (this.documents == null) {
            this.documents = new ArrayList<>();
            }
        this.documents.add(documentsItem);
        return this;
        }

        public ProcessInstance addNextActionsItem(String nextActionsItem) {
            if (this.nextActions == null) {
            this.nextActions = new ArrayList<>();
            }
        this.nextActions.add(nextActionsItem);
        return this;
        }

}

