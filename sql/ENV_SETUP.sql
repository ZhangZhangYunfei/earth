
#earth
#oauth
drop TABLE IF EXISTS earth.oauth_access_token;
create table IF NOT EXISTS earth.oauth_access_token
(
	token_id varchar(256) null,
	token blob null,
	authentication_id varchar(256) null,
	user_name varchar(256) null,
	client_id varchar(256) null,
	authentication blob null,
	refresh_token varchar(256) null
);

drop TABLE IF EXISTS earth.oauth_client_details;
create table IF NOT EXISTS earth.oauth_client_details
(
	client_id varchar(60) not null
		primary key,
	access_token_validity int null,
	additional_information varchar(255) null,
	authorities varchar(255) null,
	authorized_grant_types varchar(255) null,
	autoapprove varchar(255) null,
	client_secret varchar(255) null,
	refresh_token_validity int null,
	web_server_redirect_uri varchar(255) null,
	resource_ids varchar(255) null,
	scope varchar(255) null
);

drop TABLE IF EXISTS earth.oauth_code;
create table IF NOT EXISTS earth.oauth_code
(
	code varchar(256) null,
	authentication blob null
);

drop TABLE IF EXISTS earth.oauth_refresh_token;
create table IF NOT EXISTS earth.oauth_refresh_token
(
	token_id varchar(256) null,
	token blob null,
	authentication blob null
);

DROP TABLE IF EXISTS earth.user;
CREATE TABLE IF NOT EXISTS earth.user
(
  id                      BIGINT AUTO_INCREMENT
    PRIMARY KEY,
  version BIGINT                           NULL,
  id_no   VARCHAR(60) NOT NULL,
  telephone VARCHAR(13) NOT NULL,
  username  VARCHAR(60) NOT NULL,
  password_hash VARCHAR(64) NOT NULL,
  salt          VARCHAR(16) NOT NULL,
  authorities   VARCHAR(64) NOT NULL,
  created_time  DATETIME(6) NULL,
  updated_time  DATETIME(6) NULL,
  account_non_expired CHAR DEFAULT 'Y' NOT NULL,
  account_non_locked  CHAR DEFAULT 'Y' NOT NULL,
  credentials_non_expired CHAR DEFAULT 'Y' NOT NULL,
  enabled                 CHAR DEFAULT 'Y' NOT NULL,
  CONSTRAINT uniq_id_no
  UNIQUE (id_no),
  CONSTRAINT uniq_telephone
  UNIQUE (telephone)
);

DROP TABLE IF EXISTS earth.merchant;
CREATE TABLE IF NOT EXISTS earth.merchant (
  id             BIGINT AUTO_INCREMENT
    PRIMARY KEY,
  version BIGINT             NULL,

  name    VARCHAR(64) NOT NULL,
  address VARCHAR(255) NOT NULL,
  telephone VARCHAR(13) NOT NULL,
  description VARCHAR(64) NOT NULL,
  contact_person VARCHAR(64) NOT NULL,

  created_time   DATETIME(6) NULL,
  updated_time   DATETIME(6) NULL,

  CONSTRAINT uniq_merchant_name
  UNIQUE (name)
);

DROP TABLE IF EXISTS earth.merchant_employee;
CREATE TABLE IF NOT EXISTS earth.merchant_employee (
  id           BIGINT AUTO_INCREMENT
    PRIMARY KEY,
  version BIGINT           NULL,

  merchant_id BIGINT NOT NULL,
  employee_id BIGINT NOT NULL,

  created_time DATETIME(6) NULL,
  updated_time DATETIME(6) NULL,

  CONSTRAINT uniq_merchant_employee
  UNIQUE (merchant_id, employee_id)
);

DROP TABLE IF EXISTS earth.registration;
CREATE TABLE IF NOT EXISTS earth.registration (
  id                 BIGINT AUTO_INCREMENT
    PRIMARY KEY,
  version BIGINT                  NULL,

  examination_id BIGINT NOT NULL,
  examinee_id    BIGINT NOT NULL,
  examinee_id_no VARCHAR(60) NOT NULL,
  examinee_telephone VARCHAR(13) NOT NULL,
  examinee_name      VARCHAR(60) NOT NULL,

  others             VARCHAR(255) NOT NULL,
  status             VARCHAR(16)  NOT NULL,

  created_time       DATETIME(6)  NULL,
  updated_time       DATETIME(6)  NULL,

  CONSTRAINT uniq_examination_examinee
  UNIQUE (examination_id, examinee_id)
);

ALTER TABLE earth.registration ADD COLUMN pay_no VARCHAR(32);


DROP TABLE IF EXISTS earth.examination;
CREATE TABLE IF NOT EXISTS earth.examination (
  id           BIGINT                 AUTO_INCREMENT
    PRIMARY KEY,
  version BIGINT            NULL,
  merchant_id BIGINT NOT NULL,
  subject     VARCHAR(64) NOT NULL,
  requirement VARCHAR(64) NOT NULL,
  price       DECIMAL(6, 2) NOT NULL DEFAULT 0,
  description VARCHAR(64)   NOT NULL,
  status      VARCHAR(16)   NOT NULL,

  created_time DATETIME(6)  NULL,
  updated_time DATETIME(6)  NULL,

  CONSTRAINT uniq_merchant_id_subject
  UNIQUE (merchant_id, subject)
);

DROP TABLE IF EXISTS earth.order;
DROP TABLE IF EXISTS earth.transaction;
CREATE TABLE IF NOT EXISTS earth.transaction (
  id            BIGINT AUTO_INCREMENT
    PRIMARY KEY,
  version BIGINT            NULL,

  merchant_id BIGINT NOT NULL,
  no          VARCHAR(32) NOT NULL,
  user_id     BIGINT      NOT NULL,
  amount      DECIMAL(6, 2) NOT NULL,
  status      VARCHAR(16)   NOT NULL,
  payment_type VARCHAR(16)  NOT NULL,

  code         VARCHAR(64),
  message      VARCHAR(255),

  #callback_url  VARCHAR(128) NOT NULL,
  product_id    VARCHAR(64)  NOT NULL,
  description  VARCHAR(128) NOT NULL,
  pay_url    VARCHAR(255),

  created_time DATETIME(6)  NULL,
  executed_time DATETIME(6) NULL,
  updated_time  DATETIME(6) NULL,

  CONSTRAINT uniq_merchant_id_no
  UNIQUE (merchant_id, no)
);


DROP TABLE IF EXISTS earth.secret;
CREATE TABLE IF NOT EXISTS earth.secret (
  id           BIGINT AUTO_INCREMENT
    PRIMARY KEY,
  version BIGINT            NULL,

  merchant_id BIGINT NOT NULL,
  merchant_no VARCHAR(32) NOT NULL,
  type        VARCHAR(16) NOT NULL,

  app_id      VARCHAR(128) NOT NULL,
  api_key     VARCHAR(1000) NOT NULL,

  created_time DATETIME(6)  NOT NULL,
  updated_time DATETIME(6)  NOT NULL,

  CONSTRAINT uniq_merchant_id_type
  UNIQUE (merchant_id, type)
);
