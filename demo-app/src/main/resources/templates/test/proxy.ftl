<!DOCTYPE html>
<html>
<head>
    <title>Proxy</title>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
    <script type="text/javascript" src="${ctx}/js/jquery/jquery-1.11.3.min.js"></script>

    <style type="text/css">
    </style>
</head>
<body>
    <form method="post" action="${ctx}/proxy/forward/testParam">
        <input type="text" name="code" value="123" />
        <input type="submit" value="提交" />
    </form>

    <script type="text/javascript">
        var appBaseUrl = "${ctx}";

        $(function () {
        });
    </script>
</body>
</html>