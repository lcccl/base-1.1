"use strict";


/**
 * 事件处理器，提供处理事件的能力
 */
(function(pluginCtx, $) {

	var EventHandler = function(events) {
		this.events = {};

		if (events) {
			for (var eventName in events) {
				this.addListener(eventName, events[eventName]);
			}
		}
	};

	EventHandler.prototype = {
		constructor: EventHandler,

		addListener: function(eventName, callback) {
			var queue = this._getEventQueue(eventName);
			queue.push(callback);
		},

		fireEvent: function(eventName) {
			var queue = this._getEventQueue(eventName),
				args = [],
				flag = true;

			for (var i = 1; i < arguments.length; i++) {
				args.push(arguments[i]);
			}

			for (i = 0; i < queue.length; i++) {
				flag = false === queue[i].apply(this, args) ? false : flag;
			}

			return flag;
		},

		removeListener: function(eventName, callback) {
			var queue = this._getEventQueue(eventName);
			for (var i = 0; i < queue.length; i++) {
				if (queue[i] == callback) {
					queue.splice(i, 1);
					break;
				}
			}
		},

		clearListeners: function(eventName) {
			var queue = this._getEventQueue(eventName);
			queue.length = 0;
		},

		_getEventQueue: function(eventName) {
			var queue = this.events[eventName];
			if (!queue) {
				this.events[eventName] = queue = [];
			}

			return queue;
		}
	};

	/**
	 * 扩展原型，将EventHandler的方法添加到其他原型上
	 */
	EventHandler.extend = function(proto) {
		var eventProto = EventHandler.prototype;
		for (var name in eventProto) {
			if (name != "constructor") {
				proto[name] = eventProto[name];
			}
		}
	};

	pluginCtx.EventHandler = EventHandler;
})(window, jQuery);


(function(pluginCtx, $) {
	/**
	 * 导航栏tab组件
	 */
	pluginCtx.NavTab = {
		/**
		 * 初始化
		 */
		init: function(ct) {
			var me = this;

			me.$ct = ct;
			me.$tabHeader = $(".navtab-header", ct);
			me.$tabContent = $(".navtab-content", ct);
			me.$tabs = $(".navtab-tabs", me.$tabHeader);
			me.$tabsLeft = $(".navab-tabs-left", me.$tabHeader);
			me.$tabsRight = $(".navab-tabs-right", me.$tabHeader);

			// 初始化tab页
			me.tabs = [];
			me.tabsMap = {};
			$("ul li", me.$tabHeader).each(function() {
				var li = $(this),
					div = $("[ref-tabId='" + li.attr("tabId") + "']", me.$tabContent),
					tab = new Tab(li, div);

				me.addTab(tab);
				if (li.hasClass("active")) {
					me.switchTab(tab);
				}
			});

			// 点击tab页选项卡，切换tab页
			me.$tabs.on("click", "ul li", function() {
				var tabId = $(this).attr("tabId");
				if (me.currentTab && me.currentTab.id != tabId) {
					me.switchTab(tabId);
				}
				me.scrollCurrentTab();
			});

			// 点击tab页选项卡的"×"按钮，关闭tab页
			me.$tabs.on("click", "ul li .navtab-close", function(event) {
				event.stopPropagation();
				var tabId = $(this).parent().attr("tabId");
				me.closeTab(tabId);
				me.doLayout();
			});

			// tab页选项卡左右滚动按钮
			me.$tabsLeft.on("click", function() {
				me.scrollLeft(150);
			});
			me.$tabsRight.on("click", function() {
				me.scrollRight(150);
			});

			// tab页选项卡鼠标右键菜单初始化
			if ($.fn.contextMenu) {
				// 屏蔽浏览器鼠标右键菜单
				me.$tabs.on("contextmenu", function(event) {
					event.preventDefault();
				});
				// 手动绑定鼠标右键事件，创建弹出菜单
				me.$tabs.on("mouseup", "ul li", function(event) {
					event.stopPropagation();
					if (event.which == 3) {
						var li = $(this),
							tabId = li.attr("tabId"),
							tab = me.findTab(tabId).tab;

						new $.fn.contextMenu.Menu({
							x: event.clientX,
							y: event.clientY,
							menus: [{
								text: "刷新标签页",
								handler: function() {
									me.switchTab(tab);
									me.scrollCurrentTab();
									tab.load();
								}
							}, {
								text: "关闭标签页",
								handler: function() {
									me.closeTab(tab);
									me.doLayout();
								}
							}, {
								text: "关闭其他标签页",
								handler: function() {
									me.closeOtherTabs(tab);
								}
							}, {
								text: "关闭全部标签页",
								handler: function() {
									me.closeAllTabs();
								}
							}]
						});
					}
				});
			}

			// 页面尺寸变化时调整布局
			$(window).on("resize", function() {
				me.doLayout();
			});
		},

		/** 
		 * 打开tab页
		 * 说明：
		 *   1.如果指定了id且该id对应的tab页已存在时会重新加载，否则打开新的tab页；
		 *   2.tab页的内容可以ajax加载或加载指定html生成；
		 * 参数说明：
		 *   id：tab页的id，id未定义时会自动生成id；
		 *   title：tab页的标题；
		 *   html：加载指定的html；
		 *   type/url/data：通过ajax请求后台加载，参数同jQuery的ajax；
		 *   iframe：通过内嵌iframe加载
		 */
		openTab: function(opts) {
			var me = this,
				tab = me.findTab(opts.id).tab;

			opts = $.extend({}, opts);
			if (opts.iframe) {
				opts.html = "<iframe class='navtab-iframe' src='" + opts.url + "'></iframe>";
			}

			if (tab) {
				// 已存在的tab页重新加载
				me.switchTab(tab);
				tab.load(opts);
			} else {
				// 创建新的tab页  
				opts.headerCt = $("ul", me.$tabHeader);
				opts.contentCt = me.$tabContent;

				tab = new Tab(opts);
				me.addTab(tab);
				me.switchTab(tab);
				tab.load();
			}

			me.doLayout();
			me.scrollCurrentTab();
		},

		switchTab: function(tabId) {
			var me = this,
				tab = me.findTab(tabId).tab;

			if (me.currentTab) {
				me.currentTab.hide();
			}
			tab.show();
			me.currentTab = tab;
		},

		closeTab: function(p) {
			var me = this,
				tabInfo = me.findTab(p),
				tabIdx = tabInfo.tabIdx,
				tab = tabInfo.tab;

			// 关闭指定的tab页
			tab.close();
			me.removeTab(tab);

			// 关闭当前tab页时，指定新的当前tab页
			if (tab == me.currentTab) {
				if (me.tabs.length > 0) {
					var idx = tabIdx < me.tabs.length ? tabIdx : me.tabs.length - 1;
					me.currentTab = me.tabs[idx];
					me.currentTab.show();
				} else {
					me.currentTab = null;
				}
			}
		},

		getCurrentTab: function() {
			return this.currentTab;
		},

		closeCurrentTab: function() {
			if (this.currentTab) {
				this.closeTab(this.currentTab.id);
			}
		},

		getCurrentTabIndex: function() {
			return this.getTabIndex(this.currentTab);
		},

		closeAllTabs: function() {
			var me = this;

			for (var i = 0; i < me.tabs.length; i++) {
				me.tabs[i].close();
			}

			me.tabs = [];
			me.tabsMap = {};
			me.currentTab = null;

			me.doLayout();
		},

		closeOtherTabs: function(tab) {
			var me = this;
			for (var i = 0; i < me.tabs.length; i++) {
				var t = me.tabs[i];
				if (t != tab) {
					t.close();
				}
			}

			me.tabs = [tab];
			me.tabsMap = {};
			me.tabsMap[tab.id] = tab;
			me.switchTab(tab);

			me.doLayout();
		},

		getTabIndex: function(tab) {
			for (var i = 0; i < this.tabs.length; i++) {
				if (tab == this.tabs[i]) {
					return i;
				}
			}
			return -1;
		},

		findTab: function(p) {
			var me = this,
				tabId = null,
				tabIdx = null,
				tab = null;

			if (typeof p == "string") {
				tab = me.tabsMap[p];
				tabId = p;
				tabIdx = me.getTabIndex(tab);
			} else if (typeof p == "number") {
				tab = me.tabs[p];
				tabId = tab.id;
				tabIdx = p;
			} else if (typeof p == "object") {
				tab = p;
				tabId = tab.id;
				tabIdx = me.getTabIndex(tab);
			}

			return {
				tabId: tabId,
				tabIdx: tabIdx,
				tab: tab
			};
		},

		addTab: function(tab) {
			var me = this;

			me.tabs.push(tab);
			me.tabsMap[tab.id] = tab;

			// tab页加载完成后，如果内嵌iframe则设置iframe的高度
			tab.addListener("load", function() {
				var iframe = $(".navtab-iframe", this.$tabContent);
				if (iframe[0]) {
					var h = me.$ct.height() - me.$tabHeader.outerHeight(true) -
						(me.$tabContent.outerHeight(true) - me.$tabContent.height()) - 6;
					iframe.css("height", h);
				}
			});
		},

		removeTab: function(tab) {
			var idx = this.getTabIndex(tab);
			this.tabs.splice(idx, 1);
			delete this.tabsMap[tab.id];
		},

		/**
		 * 调整tab页的布局
		 */
		doLayout: function() {
			var me = this,
				ctW = me.$tabs.width(),
				tabsW = me.getTabsWidth(),
				left = me.getLeft();

			// tab页较多导致长度过长时，显示左右两侧的滚动按钮
			if (tabsW >= ctW) {
				me.$tabs.addClass("navtab-tabs-scrollbtn");
				me.$tabsLeft.show();
				me.$tabsRight.show();
			} else {
				me.$tabs.removeClass("navtab-tabs-scrollbtn");
				me.$tabsLeft.hide();
				me.$tabsRight.hide();
			}

			// 检查tab页的总长度，调整tab页栏滚动的位置
			if (tabsW <= ctW) {
				me.scrollTab(0);
			} else if (tabsW + left < ctW) {
				me.scrollTab(ctW - tabsW);
			}
		},

		scrollLeft: function(offset) {
			var me = this,
				ctW = me.$tabs.width(),
				tabsW = me.getTabsWidth(),
				left = me.getLeft(),
				iLeft = 0;

			if (left < 0 - offset) {
				iLeft = left + offset;
			} else {
				iLeft = 0;
			}

			me.scrollTab(iLeft);
		},

		scrollRight: function(offset) {
			var me = this,
				ctW = me.$tabs.width(),
				tabsW = me.getTabsWidth(),
				left = me.getLeft(),
				iLeft = 0;

			if (ctW - left + offset < tabsW) {
				iLeft = left - offset;
			} else {
				iLeft = ctW - tabsW;
			}

			me.scrollTab(iLeft);
		},

		scrollCurrentTab: function() {
			var me = this,
				ctW = me.$tabs.width(),
				left = me.getLeft(),
				curTabW = me.currentTab.$tabHeader.outerWidth(true),
				tabsW = me.getTabsWidth(0, me.getCurrentTabIndex());

			if (tabsW + left > ctW) {
				me.scrollTab(ctW - tabsW);
			} else if (tabsW - curTabW + left < 0) {
				me.scrollTab(curTabW - tabsW);
			}
		},

		getLeft: function() {
			return $("ul", this.$tabs).position().left;
		},

		getTabsWidth: function(startIdx, endIdx) {
			var w = 0,
				tabs = this.tabs;

			startIdx = undefined != startIdx ? startIdx : 0,
			endIdx = undefined != endIdx ? endIdx : tabs.length - 1;

			for (var i = startIdx; i <= endIdx; i++) {
				w += tabs[i].$tabHeader.outerWidth(true);
			}
			return w;
		},

		scrollTab: function(iLeft) {
			$("ul", this.$tabs).animate({
				left: iLeft
			}, 150);
		}
	};

	/** 
	 * Tab页
	 */
	var Tab = function(config) {
		var me = this;

		if (arguments.length == 2) {
			// tab页dom已存在，直接初始化
			me.$tabHeader = arguments[0];
			me.$tabContent = arguments[1];
			me.id = me.$tabHeader.attr("tabId");
			me.title = $("a", me.$tabHeader).text();
			me.html = me.$tabContent.html();
		} else {
			// 创建新的tab页
			me.id = config.id || ("navtab_" + new Date().getTime() + "_" + parseInt(Math.random() * 1000000));
			me.title = config.title;
			me.queryType = config.type;
			me.queryUrl = config.url;
			me.queryParams = config.data;
			me.html = config.html;

			// 创建tab页dom
			me.$tabHeader = $("<li tabId='" + me.id + "'><a>" + me.title + "</a><span class='navtab-close'>×</span></li>");
			me.$tabContent = $("<div class='navtab-content-box' ref-tabId='" + me.id + "'></div>");
			config.headerCt.append(me.$tabHeader);
			config.contentCt.append(me.$tabContent);
		}

		// 附加数据
		me.data = $.extend({}, config.extData);

		// 调用EventHandler的构造函数初始化事件
		pluginCtx.EventHandler.call(me, config.events);
	};

	Tab.prototype = {
		constructor: Tab,

		show: function() {
			this.$tabHeader.addClass("active");
			this.$tabContent.addClass("active");
			this.$tabContent.fadeIn(150);

			this.fireEvent("show");
		},

		hide: function() {
			this.$tabHeader.removeClass("active");
			this.$tabContent.removeClass("active");
			this.$tabContent.hide();

			this.fireEvent("hide");
		},

		close: function() {
			// 触发tab页关闭事件，用于资源的销毁释放
			if (false !== this.fireEvent("close")) {
				this.$tabHeader.remove();
				this.$tabContent.remove();
			}
		},

		load: function(params) {
			var me = this;

			if (params) {
				me.queryType = params.type;
				me.queryUrl = params.url,
				me.queryParams = params.data,
				me.html = params.html;
				// 更新附加数据
				$.extend(me.data, params.extData);
			}

			if (me.html) {
				// 加载指定html
				me.$tabContent.html(me.html);
				me.fireEvent("load");
			} else if (me.queryUrl) {
				// ajax请求后台加载内容
				$.ajax({
					type: me.queryType,
					url: me.queryUrl,
					data: me.queryParams,
					cache: false,
					beforeSend: function() {
						me.$tabContent.html('<div class="overlay"><i class="fa fa-refresh fa-spin"></i></div>');
					},
					success: function(resp) {
						var data = parseJSON(resp);
						if (typeof data == "string") {
							me.$tabContent.html(resp);
						} else {
							me.$tabContent.html("");
							showError(data.message);
						}
						me.fireEvent("load");
					},
					error: function(xhr, status, err) {
						me.$tabContent.html(xhr.responseText || err || status);
						showError(err);
					}
				});
			}
		},

		/**
		 * get/set附加数据
		 */
		data: function(name, value) {
			var data = this.data;

			if (arguments.length == 1) {
				return data[name];
			} else {
				data[name] = value;
			}
		}
	};

	// 扩展Tab的原型，增加事件处理的能力
	EventHandler.extend(Tab.prototype);

	/**
	 * 显示错误消息
	 */
	var showError = function(errMsg) {
		if (pluginCtx.MsgBox) {
			pluginCtx.MsgBox.error(errMsg);
		} else if (window.console) {
			console.log(errMsg);
		} else {
			alert(errMsg);
		}
	};

	/**
	 * 解析JSON字符串
	 */
	var parseJSON = function(data) {
		if (typeof data == "string") {
			try {
				return $.parseJSON(data);
			} catch (e) {
				return data;
			}
		} else {
			return data;
		}
	};
})(window, jQuery);