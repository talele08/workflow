CREATE TABLE eg_wf_processinstance(

    id character varying(64),
    tenantid character varying(128),
    businessService character varying(128),
    businessId character varying(128),
    action character varying(128),
    status character varying(128),
    comment character varying(128),
    assigner character varying(128),
    assignee character varying(128),
    sla bigint,
    previousStatus character varying(128),
    createdBy character varying(64),
    lastModifiedBy character varying(64),
    createdTime bigint,
    lastModifiedTime bigint,
    CONSTRAINT uk_eg_wf_processinstance UNIQUE (id)
);


CREATE TABLE eg_wf_Document(
    id character varying(64),
    tenantId character varying(64),
    documentType character varying(64),
    documentUid character varying(64),
    filestoreid character varying(64),
    processinstanceid character varying(64),
    active boolean,
    createdBy character varying(64),
    lastModifiedBy character varying(64),
    createdTime bigint,
    lastModifiedTime bigint,

    CONSTRAINT uk_eg_wf_Document PRIMARY KEY (id),
    CONSTRAINT fk_eg_wf_Document FOREIGN KEY (processinstanceid) REFERENCES eg_wf_processinstance (id)

    ON UPDATE CASCADE
    ON DELETE CASCADE
);


CREATE TABLE eg_wf_businessservice_v2
(
  businessservice character varying(256) NOT NULL,
  business character varying(256) NOT NULL,
  tenantid character varying(256) NOT NULL,
  uuid character varying(256) NOT NULL,
  geturi character varying(1024),
  posturi character varying(1024),
  createdby character varying(256) NOT NULL,
  createdtime bigint,
  lastmodifiedby character varying(256) NOT NULL,
  lastmodifiedtime bigint,

  CONSTRAINT uk_eg_wf_businessservice PRIMARY KEY (tenantid,businessService)
);


CREATE TABLE eg_wf_state_v2
(
  uuid character varying(256) NOT NULL,
  tenantid character varying(256) NOT NULL,
  businessserviceid character varying(256) NOT NULL,
  state character varying(256),
  docuploadrequired boolean,
  isstartstate boolean,
  isterminatestate boolean,
  createdby character varying(256) NOT NULL,
  createdtime bigint,
  lastmodifiedby character varying(256) NOT NULL,
  lastmodifiedtime bigint,

  CONSTRAINT uk_eg_wf_state PRIMARY KEY (uuid),
  CONSTRAINT fk_eg_wf_state FOREIGN KEY (tenantid,businessServiceid) REFERENCES eg_wf_businessservice_v2 (tenantid,businessService)
);


CREATE TABLE eg_wf_action_v2
(
  uuid character varying(256) NOT NULL,
  tenantid character varying(256) NOT NULL,
  state character varying(256),
  stateid character varying(256) NOT NULL,
  action character varying(256) NOT NULL,
  nextstateid character varying(256) NOT NULL,
  roles character varying(1024) NOT NULL,
  createdby character varying(256) NOT NULL,
  createdtime bigint,
  lastmodifiedby character varying(256) NOT NULL,
  lastmodifiedtime bigint,

   CONSTRAINT uk_eg_wf_action PRIMARY KEY (uuid),
   CONSTRAINT fk_eg_wf_action FOREIGN KEY (stateid) REFERENCES eg_wf_state_v2 (uuid)
)



