var contextPath = "/admin";

// NavTab组件初始化
NavTab.init($(".content-wrapper"));

// 菜单树初始化
MenuTree.init({
    ct: $("[data-widget='tree']"),
    url: contextPath + "/menu.json"
});

// 初始化加载用户信息
$.ajax({
    type: "POST",
    url: contextPath + "/user/getSessionUserInfo",
    success: function (resp) {
        if (resp.code == 0) {
            var userInfo = resp.data;
            $("#userInfo-tip-left .main-info").text(userInfo.name);
            $("#userInfo-tip-left .sub-info").text(userInfo.userName);
            $("#userInfo-tip-top .main-info").text(userInfo.name);
            $("#userInfo-tip-top .user-header p").html(userInfo.name + " - "
                + userInfo.userName + "<small>最后登录时间：" + userInfo.lastLoginTimeStr + "</small>");
            // 用户头像，未设置时显示默认头像
            var headImg = contextPath + (userInfo.headImg ? headImg : "/img/user2-160x160.jpg");
            $(".index-userImg").attr("src", headImg);
        } else {
            MsgBox.error(resp.message);
        }
    },
    error: function (xhr, status, err) {
        MsgBox.error(err);
    }
});

// 点击修改密码按钮，弹出密码修改页面消息框
$("#modifyPassword-btn").on("click", function () {
    MsgBox.open({
        title: "修改密码",
        url: contextPath + "/pages/system/modifyPassword.html"
    });
});