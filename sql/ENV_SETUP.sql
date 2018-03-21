
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

#user
drop TABLE IF EXISTS earth.user;
create table IF NOT EXISTS earth.user
(
	id bigint auto_increment
		primary key,
	version bigint null,
	id_no varchar(60) not null,
	telephone varchar(13) not null,
	username varchar(60) not null,
	password_hash varchar(64) not null,
	salt varchar(16) not null,
	authorities varchar(64) not null,
	created_time datetime(6) null,
	updated_time datetime(6) null,
	account_non_expired char default 'Y' not null,
	account_non_locked char default 'Y' not null,
	credentials_non_expired char default 'Y' not null,
	enabled char default 'Y' not null,
	constraint uniq_id_no
		unique (id_no),
	constraint uniq_telephone
		unique (telephone)
);

drop TABLE IF EXISTS earth.examination;
CREATE TABLE IF NOT EXISTS earth.examination(
  id bigint auto_increment
		primary key,
	version bigint null,
	merchant_id bigint not null,
  subject varchar(64) not null,
	requirement varchar(64) not null,
	description varchar(64) not null,
	status varchar(16) not null,

	created_time datetime(6) null,
	updated_time datetime(6) null,

	constraint uniq_merchant_id_subject
		unique (merchant_id, subject)
);

drop TABLE IF EXISTS earth.merchant;
CREATE TABLE IF NOT EXISTS earth.merchant(
  id bigint auto_increment
		primary key,
	version bigint null,

  name varchar(64) not null,
	address varchar(255) not null,
	telephone varchar(13) not null,
	description varchar(64) not null,
  contact_person varchar(64) not null,

	created_time datetime(6) null,
	updated_time datetime(6) null,

	constraint uniq_merchant_name
		unique (name)
);

drop TABLE IF EXISTS earth.registration;
CREATE TABLE IF NOT EXISTS earth.registration(
  id bigint auto_increment
		primary key,
	version bigint null,

  examination_id bigint not null,
	examinee_id bigint not null,
	examinee_id_no varchar(60) not null,
	examinee_telephone varchar(13) not null,
  examinee_name varchar(60) not null,

  others varchar(255) not null,
  status varchar(16) not null,

	created_time datetime(6) null,
	updated_time datetime(6) null,

	constraint uniq_examination_examinee
		unique (examination_id, examinee_id)
);

drop TABLE IF EXISTS earth.merchant_employee;
CREATE TABLE IF NOT EXISTS earth.merchant_employee(
  id bigint auto_increment
		primary key,
	version bigint null,

  merchant_id bigint not null,
	employee_id bigint not null,

	created_time datetime(6) null,
	updated_time datetime(6) null,

	constraint uniq_merchant_employee
		unique (merchant_id, employee_id)
);
