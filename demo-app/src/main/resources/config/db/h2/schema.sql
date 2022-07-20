-- h2数据库初始化脚本
-- Create table
create table PACKET_INTERFACE_CONF
(
  code            VARCHAR2(32) not null,
  packet_handler  VARCHAR2(32),
  packet_transfer VARCHAR2(32),
  send_encode     VARCHAR2(16),
  back_encode     VARCHAR2(16),
  url             VARCHAR2(400),
  ext_config      VARCHAR2(400),
  template        VARCHAR2(64)
);
-- Add comments to the table
comment on table PACKET_INTERFACE_CONF
  is '接口配置表';
-- Add comments to the columns
comment on column PACKET_INTERFACE_CONF.code
  is '接口代码';
comment on column PACKET_INTERFACE_CONF.packet_handler
  is '报文处理器类型';
comment on column PACKET_INTERFACE_CONF.packet_transfer
  is '报文传输器类型，即传输方式';
comment on column PACKET_INTERFACE_CONF.send_encode
  is '发送字符集';
comment on column PACKET_INTERFACE_CONF.back_encode
  is '接收字符集';
comment on column PACKET_INTERFACE_CONF.url
  is '请求url';
comment on column PACKET_INTERFACE_CONF.ext_config
  is '扩展配置，以json串的形式保存，如：{host: "127.0.0.1", port: 8080}';
comment on column PACKET_INTERFACE_CONF.template
  is '模板路径，如：classpath:/interface/template/xxx.ftl';
-- Create/Recreate primary, unique and foreign key constraints
alter table PACKET_INTERFACE_CONF
  add constraint PK_PACKET_INTERFACE_CONF primary key (CODE);



-- Create table
create table TEST_TAB
(
  id           VARCHAR2(32) not null,
  code         VARCHAR2(16 CHAR),
  name         VARCHAR2(100 CHAR),
  remark       VARCHAR2(4000),
  date_created TIMESTAMP(6),
  last_updated TIMESTAMP(6),
  version      NUMBER(8)
);
-- Create/Recreate primary, unique and foreign key constraints
alter table TEST_TAB
  add primary key (ID);
