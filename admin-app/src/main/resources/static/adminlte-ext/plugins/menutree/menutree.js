"use strict";

(function(pluginCtx, $) {
	/**
	 * 基于bootstrap树扩展的菜单树组件
	 */
	pluginCtx.MenuTree = {
		/**
		 * 初始化菜单树，菜单树的数据源可以为ajax后台加载或者静态数据
		 */
		init: function(config) {
			var me = this;

			me.ct = config.ct;
			me.queryType = config.type;
			me.queryUrl = config.url;
			me.queryParams = config.data;
			me.treeJson = config.treeJson;

			// 菜单项绑定点击事件
			me.ct.on("click", "li a", function() {
				var a = $(this),
					li = a.parent(),
					action = a.attr("action");

				if (action == "navtab" || action == "navtab-iframe") {
					// 打开新的tab页
					if (pluginCtx.NavTab) {
						pluginCtx.NavTab.openTab({
							id: a.attr("navtab-id"),
							title: $($("span", a)[0]).text(),
							url: a.attr("navtab-url"),
							iframe: action == "navtab-iframe"
						});
					}
				}

				// 设置对应的一级菜单的选中样式
				li = li.attr("level") == "1" ? li : li.parents("li[level=1]");
				li.addClass("activeItem").siblings().removeClass("activeItem");
			});

			// 加载菜单树
			me.loadTree();
		},

		loadTree: function(params) {
			var me = this;

			params = $.extend({}, params);
			me.queryType = params.type || me.queryType;
			me.queryUrl = params.url || me.queryUrl;
			me.queryParams = params.data || me.queryParams;
			me.treeJson = params.treeJson || me.treeJson;

			if (me.queryUrl) {
				// ajax请求后台加载树
				$.ajax({
					type: me.queryType,
					url: me.queryUrl,
					data: me.queryParams,
					showLoading: true,
					cache: false,
					dataType: "json",
					success: function(resp) {
						if (resp instanceof Array) {
							me.buildTree(resp);
						} else {
							if (resp.code == 0) {
								me.buildTree(resp.data);
							} else {
								me.showError(resp.message);
							}
						}
					},
					error: function(xhr, status, err) {
						me.showError(err);
					}
				});
			} else {
				// 直接根据静态数据加载树
				me.buildTree(me.treeJson);
			}
		},

		buildTree: function(json) {
			var me = this,
				html = "";

			for (var i = 0; i < json.length; i++) {
				html += me.createTreeHtml(json[i], 1);
			}

			me.ct.html(html);
		},

		createTreeHtml: function(json, level) {
			var html = "";

			html += "<li" + (json.children ? " class='treeview'" : "") + " level='" + level + "'>";
			html += "<a href='#' action='" + json.action + "'";
			if (json.action == "navtab" || json.action == "navtab-iframe") {
				html += " navtab-url='" + json.url + "'" + (json.code ? " navtab-id='" + json.code + "'" : "");
			}
			html += ">";
			html += "<i class='fa " + json.icon + "'></i> <span>" + json.title + "</span>";
			if (json.children) {
				html += "<span class='pull-right-container'><i class='fa fa-angle-left pull-right'></i></span>";
			}
			html += "</a>";
			if (json.children) {
				html += "<ul class='treeview-menu'>";
				for (var i = 0; i < json.children.length; i++) {
					html += this.createTreeHtml(json.children[i], level + 1);
				}
				html += "</ul>";
			}
			html += "</li>";

			return html;
		},

		showError: function(errMsg) {
			if (pluginCtx.MsgBox) {
				pluginCtx.MsgBox.error(errMsg);
			} else if (window.console) {
				console.log(errMsg);
			} else {
				alert(errMsg);
			}
		}
	};
})(window, jQuery);