-- h2数据库初始化脚本
insert into packet_interface_conf (CODE, PACKET_HANDLER, PACKET_TRANSFER, SEND_ENCODE, BACK_ENCODE, URL, EXT_CONFIG, TEMPLATE)
values ('test', 'xmlTemplate', 'http', 'UTF-8', 'UTF-8', 'http://localhost:80/carHelp/app/sendToDx/testReceivePacket', '{host: "127.0.0.1", port: 8080}', 'classpath:/interface/template/Test.ftl');

insert into packet_interface_conf (CODE, PACKET_HANDLER, PACKET_TRANSFER, SEND_ENCODE, BACK_ENCODE, URL, EXT_CONFIG, TEMPLATE)
values ('test001', 'xmlTemplate', 'http', 'UTF-8', 'UTF-8', 'http://localhost:80/base/test/testReceivePacket', '{proxy: {host: "127.0.0.1", port: 8080}}', 'classpath:/interface/template/Test.ftl');
