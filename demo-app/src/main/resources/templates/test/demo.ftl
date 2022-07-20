<!DOCTYPE html>
<html>
<head>
    <title>Demo</title>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
    <script type="text/javascript" src="${ctx}/js/jquery/jquery-1.11.3.min.js"></script>

    <style type="text/css">
        .queryDiv {
            border: 1px solid black;
            width: 600px;
            padding: 5px;
            border-bottom: none;
        }

        .listDiv {
            border: 1px solid black;
            width: 600px;
            padding: 5px;
            height: 450px;
        }

        #resultTab {
            width: 100%;
            height: 400px;
        }

        #resultTab thead {
            border: 1px solid black;
        }

        .floatDiv {
            z-index: 99;
            top: 150px;
            left: 150px;
            border: 1px solid black;
            position: absolute;
            background-color: aliceblue;
            display: none;
        }
    </style>
</head>
<body>
    <div>
        <div style="background-color: white">
            <div class="queryDiv">
                <div style="margin-bottom: 10px;">
                    代码：<input name="query.code" />
                    名称：<input name="query.name" />
                    <img id="captcha" style="width:100px; height:35px; margin-left:10px;" src="${ctx}/test/testCaptcha" />
                </div>
                <input type="button" id="newBtn" value="新增" />
                <input type="button" id="queryBtn" value="查询" />
            </div>

            <div class="listDiv">
                <table id="resultTab">
                    <thead>
                        <tr>
                            <th width="20%">代码</th>
                            <th width="20%">名称</th>
                            <th width="30%">备注</th>
                            <th width="30%">操作</th>
                        </tr>
                    </thead>
                    <tbody>
                    </tbody>
                </table>
                <div id="footBar" style="display: none;">
                    <input type="button" id="firstBtn" value="首页" />
                    <input type="button" id="preBtn" value="上一页" />
                    <input type="button" id="nextBtn" value="下一页" />
                    <input type="button" id="lastBtn" value="末页" />

                    <span>第<a id="pageNo"></a>页， 共<a id="pageCount"></a>页，每页<a id="pageSize">5</a>条记录</span>
                </div>
            </div>

            <div class="floatDiv" id="newDiv">
                代码：<input class="inputField" name="code" /> </br>
                名称：<input class="inputField" name="name" /> </br>
                备注：<input class="inputField" name="remark" /> </br>
                <input type="hidden" class="inputField" name="id" />
                <input type="button" id="submitBtn" value="提交" />
                <input type="button" id="closeBtn" value="关闭" />
            </div>
        </div>
    </div>

    <script type="text/javascript">
        var appBaseUrl = "${ctx}";

        $(function () {
            var renderResult = function (data) {
                var ct = $("#resultTab tbody");

                // 结果列表
                var list = data.list;
                var html = "";
                for (var i = 0; i < list.length; i++) {
                    var d = list[i];
                    html += "<tr rid='" + d.id + "'>";
                    html += "<td>" + d.code + "</td>";
                    html += "<td>" + d.name + "</td>";
                    html += "<td>" + d.remark + "</td>";
                    html += "<td>" +
                            "<input type='button' value='修改' class='updateBtn' />" +
                            "<input type='button' value='删除' class='delBtn' />" +
                            "</td>";
                    html += "</tr>"
                }

                ct.html(html);

                // 分页信息
                $("#footBar").show();
                $("#pageNo").text(data.pageNo);
                $("#pageCount").text(data.pageCount);
                $("#pageSize").text(data.pageSize);
            };

            var ajaxQuery = function (pageNo, pageSize) {
                $.ajax({
                    type: "POST",
                    url: appBaseUrl + "/test/ajaxQuery",
                    data: {
                        code: $("[name='query.code']").val(),
                        name: $("[name='query.name']").val(),
                        pageNo: pageNo,
                        pageSize: pageSize
                    },
                    success: function (data) {
                        renderResult(data);
                    }
                });
            };

            // 查询
            $("#queryBtn").on("click", function () {
                ajaxQuery(1, parseInt($("#pageSize").text()));
            });

            // 首页
            $("#firstBtn").on("click", function () {
                ajaxQuery(1, parseInt($("#pageSize").text()));
            });

            // 上一页
            $("#preBtn").on("click", function () {
                var pageNo = $("#pageNo").text();
                pageNo = pageNo != "" ? parseInt(pageNo) : 0;
                if (--pageNo > 0) {
                    ajaxQuery(pageNo, parseInt($("#pageSize").text()));
                }
            });

            // 下一页
            $("#nextBtn").on("click", function () {
                var pageNo = $("#pageNo").text() != "" ? parseInt($("#pageNo").text()) : 0,
                        pageCount = $("#pageCount").text() != "" ? parseInt($("#pageCount").text()) : 0;
                if (++pageNo <= pageCount) {
                    ajaxQuery(pageNo, parseInt($("#pageSize").text()));
                }
            });

            // 末页
            $("#lastBtn").on("click", function () {
                var pageCount = $("#pageCount").text() != "" ? parseInt($("#pageCount").text()) : 0;
                ajaxQuery(pageCount, parseInt($("#pageSize").text()));
            });

            // 删除
            $("#resultTab").on("click", ".delBtn", function () {
                var id = $(this).parents("tr").attr("rid");
                $.ajax({
                    url: appBaseUrl + "/test/ajaxDelete?id=" + id,
                    success: function (data) {
                        if (data == "success") {
                            $("#queryBtn").trigger("click");
                        }
                    }
                });
            });

            // 新增
            $("#newBtn").on("click", function () {
                var ct = $("#newDiv");
                $(".inputField", ct).each(function () {
                    $(this).val("");
                });
                ct.show();
            });

            // 保存提交
            $("#submitBtn").on("click", function () {
                var ct = $("#newDiv");
                $.ajax({
                    type: "POST",
                    url: appBaseUrl + "/test/ajaxSave",
                    data: {
                        id: $("[name='id']", ct).val(),
                        code: $("[name='code']", ct).val(),
                        name: $("[name='name']", ct).val(),
                        remark: $("[name='remark']", ct).val()
                    },
                    success: function (data) {
                        if (data == "success") {
                            $(".inputField", ct).each(function () {
                                $(this).val("");
                            });
                            ct.hide();
                            $("#queryBtn").trigger("click");
                        }
                    }
                });
            });

            // 关闭
            $("#closeBtn").on("click", function () {
                $("#newDiv").hide();
            });

            // 打开修改页面
            $("#resultTab").on("click", ".updateBtn", function () {
                var id = $(this).parents("tr").attr("rid");
                $.ajax({
                    url: appBaseUrl + "/test/ajaxGet?id=" + id,
                    success: function (data) {
                        if (data) {
                            var ct = $("#newDiv");
                            $(".inputField", ct).each(function () {
                                $(this).val("");
                            });
                            $("[name='id']", ct).val(data.id);
                            $("[name='code']", ct).val(data.code);
                            $("[name='name']", ct).val(data.name);
                            $("[name='remark']", ct).val(data.remark);
                            ct.show();
                        }
                    }
                });
            });

            // 刷新验证码
            $("#captcha").on("click", function () {
                var el = $(this);

                var url = el.attr("src");
                if (url.indexOf("?") != -1) {
                    url = url.substring(0, url.indexOf("?"));
                }
                el.attr("src", url + "?code=" + Math.random());
            });
        });
    </script>
</body>
</html>