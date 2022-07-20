-- h2数据库初始化脚本
-- h2中注册uuid函数
create alias uuid for "base.utils.h2.H2FunctionLib.uuid";

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
create table C_CODETRANS
(
  id         VARCHAR2(32) not null,
  codetype   VARCHAR2(32) not null,
  code       VARCHAR2(32) not null,
  parentcode VARCHAR2(32),
  codename   VARCHAR2(400),
  flag       VARCHAR2(10),
  codelevel  NUMBER(4),
  display_no NUMBER(4),
  validind   CHAR(1) not null,
  remark     VARCHAR2(4000)
);
-- Add comments to the table
comment on table C_CODETRANS
  is '数据字典表';
-- Add comments to the columns
comment on column C_CODETRANS.id
  is '逻辑主键';
comment on column C_CODETRANS.codetype
  is '代码类型';
comment on column C_CODETRANS.code
  is '代码值';
comment on column C_CODETRANS.parentcode
  is '父节点代码';
comment on column C_CODETRANS.codename
  is '名称';
comment on column C_CODETRANS.flag
  is '标志';
comment on column C_CODETRANS.codelevel
  is '代码层级';
comment on column C_CODETRANS.display_no
  is '显示序号';
comment on column C_CODETRANS.validind
  is '有效标志';
comment on column C_CODETRANS.remark
  is '备注';
-- Create/Recreate primary, unique and foreign key constraints
alter table C_CODETRANS
  add constraint PK_C_CODETRANS primary key (ID);


-- Create table
create table C_CODECONFIG
(
  id           VARCHAR2(32) not null,
  configtype   VARCHAR2(32) not null,
  configcode   VARCHAR2(32) not null,
  configdesc   VARCHAR2(200),
  configvalue  VARCHAR2(4000) not null,
  remark       VARCHAR2(4000),
  validind     CHAR(1) not null,
  date_created DATE,
  last_updated DATE
);
-- Add comments to the table
comment on table C_CODECONFIG
  is '参数配置表';
-- Add comments to the columns
comment on column C_CODECONFIG.id
  is '逻辑主键';
comment on column C_CODECONFIG.configtype
  is '配置类型代码';
comment on column C_CODECONFIG.configcode
  is '配置代码';
comment on column C_CODECONFIG.configdesc
  is '配置描述';
comment on column C_CODECONFIG.configvalue
  is '配置值';
comment on column C_CODECONFIG.remark
  is '备注';
comment on column C_CODECONFIG.validind
  is '有效标志';
comment on column C_CODECONFIG.date_created
  is '创建日期';
comment on column C_CODECONFIG.last_updated
  is '最后更新日期';
-- Create/Recreate primary, unique and foreign key constraints
alter table C_CODECONFIG
  add constraint PK_C_CODECONFIG primary key (ID);


-- Create table
create table T_USER
(
  id              VARCHAR2(32) not null,
  username        VARCHAR2(32 CHAR),
  password        VARCHAR2(128 CHAR),
  password_salt   VARCHAR2(128 CHAR),
  name            VARCHAR2(128 CHAR),
  birthday        VARCHAR2(10),
  sex             CHAR(1),
  identify_type   VARCHAR2(2),
  identify_no     VARCHAR2(32),
  email           VARCHAR2(128),
  tel             VARCHAR2(32),
  mobile          VARCHAR2(32),
  address         VARCHAR2(128 CHAR),
  head_img        VARCHAR2(400),
  date_created    TIMESTAMP(6),
  last_login_time TIMESTAMP(6),
  validind        CHAR(1),
  remark          VARCHAR2(4000)
);
-- Add comments to the table
comment on table T_USER
  is '用户表';
-- Add comments to the columns
comment on column T_USER.id
  is 'ID主键';
comment on column T_USER.username
  is '用户名';
comment on column T_USER.password
  is '密码';
comment on column T_USER.password_salt
  is '密码salt';
comment on column T_USER.name
  is '姓名';
comment on column T_USER.birthday
  is '生日（格式：yyyy-MM-dd）';
comment on column T_USER.sex
  is '性别';
comment on column T_USER.identify_type
  is '证件类型';
comment on column T_USER.identify_no
  is '证件号';
comment on column T_USER.email
  is '电子邮箱';
comment on column T_USER.tel
  is '电话号码';
comment on column T_USER.mobile
  is '手机号码';
comment on column T_USER.address
  is '地址';
comment on column T_USER.head_img
  is '头像';
comment on column T_USER.date_created
  is '创建日期';
comment on column T_USER.last_login_time
  is '最后登录日期';
comment on column T_USER.validind
  is '有效标志';
comment on column T_USER.remark
  is '备注';
-- Create/Recreate primary, unique and foreign key constraints
alter table T_USER
  add constraint PK_T_USER primary key (ID);

-- shiro权限链配置表
create table shiro_filter_chain
(
  url     varchar2(1000),
  filter  varchar2(1000)
);




