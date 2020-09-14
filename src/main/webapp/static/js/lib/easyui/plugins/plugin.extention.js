$.fn.panel.defaults.loadingMessage = '数据加载中....';
$.fn.datagrid.defaults.loadMsg = '数据加载中....';
$.fn.dialog.defaults.loadingMessage = '数据加载中....';
/**
 * 扩展 datagrid/treegrid 增加表头菜单，用于显示或隐藏列，注意：冻结列不在此菜单中
 * 
 * @param e
 * @param field
 */
var createGridHeaderContextMenu = function(e, field) {
	e.preventDefault();
	var grid = $(this);/* grid本身 */
	var headerContextMenu = this.headerContextMenu;/* grid上的列头菜单对象 */
	if (!headerContextMenu) {
		var tmenu = $('<div style="width:150px;"></div>').appendTo('body');
		var fields = grid.datagrid('getColumnFields');
		for ( var i = 0; i < fields.length; i++) {
			var fildOption = grid.datagrid('getColumnOption', fields[i]);
			if(fildOption == null || fildOption.checkbox == true){
				continue;
			}
			if (fildOption.canHideEdit != false) {// 添加一个属性，如果有canHideEdit =
				// true则可以显示与隐藏
				if (!fildOption.hidden) {
					$('<div iconCls="OkCheck" field="' + fields[i] + '"/>')
							.html(fildOption.title).appendTo(tmenu);
				} else {
					$('<div iconCls="EmptyCheck" field="' + fields[i] + '"/>')
							.html(fildOption.title).appendTo(tmenu);
				}
			}
		}
		headerContextMenu = this.headerContextMenu = tmenu.menu({
			onClick : function(item) {
				var field = $(item.target).attr('field');
				if (item.iconCls == 'OkCheck') {
					grid.datagrid('hideColumn', field);
					$(this).menu('setIcon', {
						target : item.target,
						iconCls : 'EmptyCheck'
					});
				} else {
					grid.datagrid('showColumn', field);
					$(this).menu('setIcon', {
						target : item.target,
						iconCls : 'OkCheck'
					});
				}
			}
		});
	}
	headerContextMenu.menu('show', {
		left : e.pageX,
		top : e.pageY
	});
};
$.fn.datagrid.defaults.onHeaderContextMenu = createGridHeaderContextMenu;
$.fn.treegrid.defaults.onHeaderContextMenu = createGridHeaderContextMenu;

/**
 * 扩展 用于datagrid/treegrid/tree/combogrid/combobox/form加载数据出错时的操作
 * 
 * @param XMLHttpRequest
 */
var easyuiErrorFunction = function(XMLHttpRequest) {
	$.messager.progress('close');
	$.messager.alert('错误', XMLHttpRequest.responseText);
};
$.fn.datagrid.defaults.onLoadError = easyuiErrorFunction;
$.fn.treegrid.defaults.onLoadError = easyuiErrorFunction;
$.fn.tree.defaults.onLoadError = easyuiErrorFunction;
$.fn.combogrid.defaults.onLoadError = easyuiErrorFunction;
$.fn.combobox.defaults.onLoadError = easyuiErrorFunction;
$.fn.form.defaults.onLoadError = easyuiErrorFunction;
/**
 * 防止panel/window/dialog组件超出浏览器边界
 * 
 * @param left
 * @param top
 */
var easyuiPanelOnMove = function(left, top) {
	var l = left;
	var t = top;
	if (l < 1) {
		l = 1;
	}
	if (t < 1) {
		t = 1;
	}
	var width = parseInt($(this).parent().css('width')) + 14;
	var height = parseInt($(this).parent().css('height')) + 14;
	var right = l + width;
	var buttom = t + height;
	var browserWidth = $(window).width();
	var browserHeight = $(window).height();
	if (right > browserWidth) {
		l = browserWidth - width;
	}
	if (buttom > browserHeight) {
		t = browserHeight - height;
	}
	$(this).parent().css({/* 修正面板位置 */
		left : l,
		top : t
	});
};
$.fn.dialog.defaults.onMove = easyuiPanelOnMove;
$.fn.window.defaults.onMove = easyuiPanelOnMove;
$.fn.panel.defaults.onMove = easyuiPanelOnMove;

/**
 * 扩展easyui validatebox的两个方法.移除验证和还原验证
 */
$.extend($.fn.validatebox.methods, {
	remove : function(jq, newposition) {
		return jq.each(function() {
			$(this).removeClass("validatebox-text validatebox-invalid").unbind(
					'focus.validatebox').unbind('blur.validatebox');
			// remove tip
			// $(this).validatebox('hideTip', this);
		});
	},
	reduce : function(jq, newposition) {
		return jq.each(function() {
			var opt = $(this).data().validatebox.options;
			$(this).addClass("validatebox-text").validatebox(opt);
			// $(this).validatebox('validateTip', this);
		});
	},
	validateTip : function(jq) {
		jq = jq[0];
		var opts = $.data(jq, "validatebox").options;
		var tip = $.data(jq, "validatebox").tip;
		var box = $(jq);
		var value = box.val();
		function setTipMessage(msg) {
			$.data(jq, "validatebox").message = msg;
		}
		;
		var disabled = box.attr("disabled");
		if (disabled == true || disabled == "true") {
			return true;
		}
		if (opts.required) {
			if (value == "") {
				box.addClass("validatebox-invalid");
				setTipMessage(opts.missingMessage);
				$(jq).validatebox('showTip', jq);
				return false;
			}
		}
		if (opts.validType) {
			var result = /([a-zA-Z_]+)(.*)/.exec(opts.validType);
			var rule = opts.rules[result[1]];
			if (value && rule) {
				var param = eval(result[2]);
				if (!rule["validator"](value, param)) {
					box.addClass("validatebox-invalid");
					var message = rule["message"];
					if (param) {
						for ( var i = 0; i < param.length; i++) {
							message = message.replace(new RegExp("\\{" + i
									+ "\\}", "g"), param[i]);
						}
					}
					setTipMessage(opts.invalidMessage || message);
					$(jq).validatebox('showTip', jq);
					return false;
				}
			}
		}
		box.removeClass("validatebox-invalid");
		$(jq).validatebox('hideTip', jq);
		return true;
	},
	showTip : function(jq) {
		jq = jq[0];
		var box = $(jq);
		var msg = $.data(jq, "validatebox").message
		var tip = $.data(jq, "validatebox").tip;
		if (!tip) {
			tip = $(
					"<div class=\"validatebox-tip\">"
							+ "<span class=\"validatebox-tip-content\">"
							+ "</span>"
							+ "<span class=\"validatebox-tip-pointer\">"
							+ "</span>" + "</div>").appendTo("body");
			$.data(jq, "validatebox").tip = tip;
		}
		tip.find(".validatebox-tip-content").html(msg);
		tip.css({
			display : "block",
			left : box.offset().left + box.outerWidth(),
			top : box.offset().top
		});
	},
	hideTip : function(jq) {
		jq = jq[0];
		var tip = $.data(jq, "validatebox").tip;
		if (tip) {
			tip.remove;
			$.data(jq, "validatebox").tip = null;
		}
	}
});

/**
 * 相同连续列合并扩展
 */
$.extend($.fn.datagrid.methods, {
	autoMergeCells : function(jq, fields) {
		return jq.each(function() {
			var target = $(this);
			if (!fields) {
				fields = target.datagrid("getColumnFields");
			}
			var rows = target.datagrid("getRows");
			var i = 0, j = 0, temp = {};
			for (i; i < rows.length; i++) {
				var row = rows[i];
				j = 0;
				for (j; j < fields.length; j++) {
					var field = fields[j];
					var tf = temp[field];
					if (!tf) {
						tf = temp[field] = {};
						tf[row[field]] = [ i ];
					} else {
						var tfv = tf[row[field]];
						if (tfv) {
							tfv.push(i);
						} else {
							tfv = tf[row[field]] = [ i ];
						}
					}
				}
			}
			$.each(temp, function(field, colunm) {
				$.each(colunm, function() {
					var group = this;
					if (group.length > 1) {
						var before, after, megerIndex = group[0];
						for ( var i = 0; i < group.length; i++) {
							before = group[i];
							after = group[i + 1];
							if (after && (after - before) == 1) {
								continue;
							}
							var rowspan = before - megerIndex + 1;
							if (rowspan > 1) {
								target.datagrid('mergeCells', {
									index : megerIndex,
									field : field,
									rowspan : rowspan
								});
							}
							if (after && (after - before) != 1) {
								megerIndex = after;
							}
						}
					}
				});
			});
		});
	}
});

/**
 * radio - jQuery EasyUI
 */
(function($) {
	var STYLE = {
		radio : {
			cursor : "pointer",
			background : "transparent url('data:image/gif;base64,R0lGODlhDwAmANUAAP////z8/Pj4+Ovr6/v7++7u7uPj493d3ff39/Ly8vT09ICAgPX19a+vr+Li4urq6vn5+fr6+v39/dXV1efn5+bm5uTk5ODg4N7e3v7+/vHx8fDw8O3t7e/v74eHh+Hh4c3NzdfX1+np6eXl5cDAwNra2t/f38/Pz/Pz8/b29sbGxsHBwc7OzsLCwujo6NHR0by8vL29vcXFxb+/v7m5udPT09jY2MPDw7u7u7i4uNLS0uzs7AAAAAAAAAAAAAAAACH5BAAAAAAALAAAAAAPACYAAAb/QIBwSCwaj0VGSIbLzUAXQfFDap1KjktJVysMHTHQ4WKgPAqaymQDYJBYh4/FNSgkGIjBgRC6YRwjIjsddwIRAQ4cKi8GFSIcGygphgEBBRYwB2ZoCggQBJUBCBg0FHV3nqChBAcrFYQMlKGVAgYnJpKyswEJDwYTqbuhAwoQEw+qwgoDEgAbNhzCAQoiUkIaGAYdCAQCCQMD1kMEBSMmBxbEzUjs7e7v8EJKTE5Q4kJUVlhaXF5CYGLIbEqzps2bOHNO4dHDxw+gAgIyREBAKdGiRgUMNPDQwICqS5nMQGiwoGSDDJVGlaqTwUPJBR4AVGLlihABkiZRBqh1S1IELY0cDUio1OtXKgkZAGQYWomYMWTSpjFzBk0aNXHYtHHzBu4eAHLm0KmLR5ZIEAA7') no-repeat center top",
			verticalAlign : "middle",
			height : "19px",
			width : "18px",
			display : "block"
		},
		span : {
			"float" : "left",
			display : "block",
			margin : "0px 4px",
			marginTop : "5px"
		},
		label : {
			marginTop : "4px",
			marginRight : "8px",
			display : "block",
			"float" : "left",
			fontSize : "16px",
			cursor : "pointer"
		}
	};

	function rander(target) {
		var jqObj = $(target);
		jqObj.css('display', 'inline-block');
		var radios = jqObj.children('input[type=radio]');
		var checked;

		radios.each(function() {
			var jqRadio = $(this);
			var jqWrap = $('<span/>').css(STYLE.span);
			var jqLabel = $('<label/>').css(STYLE.label);
			var jqRadioA = $('<a/>').data('lable', jqLabel).addClass("RadioA")
					.css(STYLE.radio).text(' ');
			var labelText = jqRadio.data('lable', jqLabel).attr('label');
			jqRadio.hide();
			jqRadio.after(jqLabel.text(labelText));
			jqRadio.wrap(jqWrap);
			jqRadio.before(jqRadioA);
			if (jqRadio.prop('checked')) {
				checked = jqRadioA;
			}

			jqLabel.click(function() {
				(function(rdo) {
					rdo.prop('checked', true);
					radios.each(function() {
						var rd = $(this);
						var y = 'top';
						if (rd.prop('checked')) {
							y = 'bottom';
						}
						rd.prev().css('background-position', 'center ' + y);
					});
				})(jqRadio);
			});

			jqRadioA.click(function() {
				$(this).data('lable').click();
			});
		});
		checked.css('background-position', 'center bottom');
	}

	$.fn.radio = function(options, param) {
		if (typeof options == 'string') {
			return $.fn.radio.methods[options](this, param);
		}

		options = options || {};
		return this.each(function() {
			if (!$.data(this, 'radio')) {
				$.data(this, 'radio', {
					options : $.extend({}, $.fn.radio.defaults, options)
				});
				rander(this);
			} else {
				var opt = $.data(this, 'radio').options;
				$.data(this, 'radio', {
					options : $.extend({}, opt, options)
				});
			}
		});
	};

	$.fn.radio.methods = {
		getValue : function(jq) {
			var checked = jq.find('input:checked');
			var val = {};
			if (checked.length)
				val[checked[0].name] = checked[0].value;

			return val;
		},
		check : function(jq, val) {
			if (val && typeof val != 'object') {
				var ipt = jq.find('input[value=' + val + ']');
				ipt.prop('checked', false).data('lable').click();
			}
		}
	};

	$.fn.radio.defaults = {
		style : STYLE
	};

	if ($.parser && $.parser.plugins) {
		$.parser.plugins.push('radio');
	}

})(jQuery);
/**
 * checkbox - jQuery EasyUI
 */
(function($) {

	var STYLE = {
		checkbox : {
			cursor : "pointer",
			background : "transparent url('data:image/gif;base64,R0lGODlhDwAmAKIAAPr6+v///+vr68rKyvT09Pj4+ICAgAAAACH5BAAAAAAALAAAAAAPACYAAANuGLrc/mvISWcYJOutBS5gKIIeUQBoqgLlua7tC3+yGtfojes1L/sv4MyEywUEyKQyCWk6n1BoZSq5cK6Z1mgrtNFkhtx3ZQizxqkyIHAmqtTsdkENgKdiZfv9w9bviXFxXm4KP2g/R0uKAlGNDAkAOw==') no-repeat center top",
			verticalAlign : "middle",
			height : "19px",
			width : "18px",
			display : "block"
		},
		span : {
			"float" : "left",
			display : "block",
			margin : "0px 4px",
			marginTop : "5px"
		},
		label : {
			marginTop : "4px",
			marginRight : "8px",
			display : "block",
			"float" : "left",
			fontSize : "16px",
			cursor : "pointer"
		}
	};

	function rander(target) {
		var jqObj = $(target);
		jqObj.css('display', 'inline-block');
		var Checkboxs = jqObj.children('input[type=checkbox]');
		Checkboxs.each(function() {
			var jqCheckbox = $(this);
			var jqWrap = $('<span/>').css(STYLE.span);
			var jqLabel = $('<label/>').css(STYLE.label);
			var jqCheckboxA = $('<a/>').data('lable', jqLabel).css(
					STYLE.checkbox).text(' ');
			var labelText = jqCheckbox.data('lable', jqLabel).attr('label');
			jqCheckbox.hide();
			jqCheckbox.after(jqLabel.text(labelText));
			jqCheckbox.wrap(jqWrap);
			jqCheckbox.before(jqCheckboxA);
			if (jqCheckbox.prop('checked')) {
				jqCheckboxA.css('background-position', 'center bottom');
			}

			jqLabel.click(function() {
				(function(ck, cka) {
					ck.prop('checked', !ck.prop('checked'));
					var y = 'top';
					if (ck.prop('checked')) {
						y = 'bottom';
					}
					cka.css('background-position', 'center ' + y);
				})(jqCheckbox, jqCheckboxA);
			});

			jqCheckboxA.click(function() {
				$(this).data('lable').click();
			});
		});
	}

	$.fn.checkbox = function(options, param) {
		if (typeof options == 'string') {
			return $.fn.checkbox.methods[options](this, param);
		}

		options = options || {};
		return this.each(function() {
			if (!$.data(this, 'checkbox')) {
				$.data(this, 'checkbox', {
					options : $.extend({}, $.fn.checkbox.defaults, options)
				});
				rander(this);
			} else {
				var opt = $.data(this, 'checkbox').options;
				$.data(this, 'checkbox', {
					options : $.extend({}, opt, options)
				});
			}
		});
	};

	function check(jq, val, check) {
		var ipt = jq.find('input[value=' + val + ']');
		if (ipt.length) {
			ipt.prop('checked', check).each(function() {
				$(this).data('lable').click();
			});
		}
	}

	$.fn.checkbox.methods = {
		getValue : function(jq) {
			var checked = jq.find('input:checked');
			var val = {};
			checked.each(function() {
				var kv = val[this.name];
				if (!kv) {
					val[this.name] = this.value;
					return;
				}

				if (!kv.sort) {
					val[this.name] = [ kv ];
				}
				val[this.name].push(this.value);
			});
			return val;
		},
		check : function(jq, vals) {
			if (vals && typeof vals != 'object') {
				check(jq, vals);
			} else if (vals.sort) {
				$.each(vals, function() {
					check(jq, this);
				});
			}
		},
		unCheck : function(jq, vals) {
			if (vals && typeof vals != 'object') {
				check(jq, vals, true);
			} else if (vals.sort) {
				$.each(vals, function() {
					check(jq, this, true);
				});
			}
		},
		checkAll : function(jq) {
			jq.find('input').prop('checked', false).each(function() {
				$(this).data('lable').click();
			});
		},
		unCheckAll : function(jq) {
			jq.find('input').prop('checked', true).each(function() {
				$(this).data('lable').click();
			});
		}
	};

	$.fn.checkbox.defaults = {
		style : STYLE
	};

	if ($.parser && $.parser.plugins) {
		$.parser.plugins.push('checkbox');
	}
})(jQuery);

/**
 * 1）扩展jquery easyui tree的节点检索方法。使用方法如下： $("#treeId").tree("search",
 * searchText); 其中，treeId为easyui tree的根UL元素的ID，searchText为检索的文本。
 * 如果searchText为空或""，将恢复展示所有节点为正常状态
 */
(function($) {

	$
			.extend(
					$.fn.tree.methods,
					{
						/**
						 * 扩展easyui tree的搜索方法
						 * 
						 * @param tree
						 *            easyui tree的根DOM节点(UL节点)的jQuery对象
						 * @param searchText
						 *            检索的文本
						 * @param this-context
						 *            easyui tree的tree对象
						 */
						search : function(jqTree, searchText) {
							// easyui
							// tree的tree对象。可以通过tree.methodName(jqTree)方式调用easyui
							// tree的方法
							var tree = this;

							// 获取所有的树节点
							var nodeList = getAllNodes(jqTree, tree);

							// 如果没有搜索条件，则展示所有树节点
							searchText = $.trim(searchText);
							if (searchText == "") {
								for ( var i = 0; i < nodeList.length; i++) {
									$(".tree-node-targeted", nodeList[i].target)
											.removeClass("tree-node-targeted");
									$(nodeList[i].target).show();
								}
								// 展开已选择的节点（如果之前选择了）
								var selectedNode = tree.getSelected(jqTree);
								if (selectedNode) {
									tree.expandTo(jqTree, selectedNode.target);
								}
								return;
							}

							// 搜索匹配的节点并高亮显示
							var matchedNodeList = [];
							if (nodeList && nodeList.length > 0) {
								var node = null;
								for ( var i = 0; i < nodeList.length; i++) {
									node = nodeList[i];
									if (isMatch(searchText, node.text)) {
										matchedNodeList.push(node);
									}
								}

								// 隐藏所有节点
								for ( var i = 0; i < nodeList.length; i++) {
									$(".tree-node-targeted", nodeList[i].target)
											.removeClass("tree-node-targeted");
									$(nodeList[i].target).hide();
								}

								// 折叠所有节点
								tree.collapseAll(jqTree);

								// 展示所有匹配的节点以及父节点
								for ( var i = 0; i < matchedNodeList.length; i++) {
									showMatchedNode(jqTree, tree,
											matchedNodeList[i]);
								}
							}
						},

						/**
						 * 展示节点的子节点（子节点有可能在搜索的过程中被隐藏了）
						 * 
						 * @param node
						 *            easyui tree节点
						 */
						showChildren : function(jqTree, node) {
							// easyui
							// tree的tree对象。可以通过tree.methodName(jqTree)方式调用easyui
							// tree的方法
							var tree = this;

							// 展示子节点
							if (!tree.isLeaf(jqTree, node.target)) {
								var children = tree.getChildren(jqTree,
										node.target);
								if (children && children.length > 0) {
									for ( var i = 0; i < children.length; i++) {
										if ($(children[i].target).is(":hidden")) {
											$(children[i].target).show();
										}
									}
								}
							}
						},

						/**
						 * 将滚动条滚动到指定的节点位置，使该节点可见（如果有滚动条才滚动，没有滚动条就不滚动）
						 * 
						 * @param param {
						 *            treeContainer: easyui
						 *            tree的容器（即存在滚动条的树容器）。如果为null，则取easyui
						 *            tree的根UL节点的父节点。 targetNode: 将要滚动到的easyui
						 *            tree节点。如果targetNode为空，则默认滚动到当前已选中的节点，如果没有选中的节点，则不滚动 }
						 */
						scrollTo : function(jqTree, param) {
							// easyui
							// tree的tree对象。可以通过tree.methodName(jqTree)方式调用easyui
							// tree的方法
							var tree = this;

							// 如果node为空，则获取当前选中的node
							var targetNode = param && param.targetNode ? param.targetNode
									: tree.getSelected(jqTree);

							if (targetNode != null) {
								// 判断节点是否在可视区域
								var root = tree.getRoot(jqTree);
								var $targetNode = $(targetNode.target);
								var container = param && param.treeContainer ? param.treeContainer
										: jqTree.parent();
								var containerH = container.height();
								var nodeOffsetHeight = $targetNode.offset().top
										- container.offset().top;
								if (nodeOffsetHeight > (containerH - 30)) {
									var scrollHeight = container.scrollTop()
											+ nodeOffsetHeight - containerH
											+ 30;
									container.scrollTop(scrollHeight);
								}
							}
						}
					});

	/**
	 * 展示搜索匹配的节点
	 */
	function showMatchedNode(jqTree, tree, node) {
		// 展示所有父节点
		$(node.target).show();
		$(".tree-title", node.target).addClass("tree-node-targeted");
		var pNode = node;
		while ((pNode = tree.getParent(jqTree, pNode.target))) {
			$(pNode.target).show();
		}
		// 展开到该节点
		tree.expandTo(jqTree, node.target);
		// 如果是非叶子节点，需折叠该节点的所有子节点
		if (!tree.isLeaf(jqTree, node.target)) {
			tree.collapse(jqTree, node.target);
		}
	}

	/**
	 * 判断searchText是否与targetText匹配
	 * 
	 * @param searchText
	 *            检索的文本
	 * @param targetText
	 *            目标文本
	 * @return true-检索的文本与目标文本匹配；否则为false.
	 */
	function isMatch(searchText, targetText) {
		return $.trim(targetText) != "" && targetText.indexOf(searchText) != -1;
	}

	/**
	 * 获取easyui tree的所有node节点
	 */
	function getAllNodes(jqTree, tree) {
		var allNodeList = jqTree.data("allNodeList");
		if (!allNodeList) {
			var roots = tree.getRoots(jqTree);
			allNodeList = getChildNodeList(jqTree, tree, roots);
			jqTree.data("allNodeList", allNodeList);
		}
		return allNodeList;
	}

	/**
	 * 定义获取easyui tree的子节点的递归算法
	 */
	function getChildNodeList(jqTree, tree, nodes) {
		var childNodeList = [];
		if (nodes && nodes.length > 0) {
			var node = null;
			for ( var i = 0; i < nodes.length; i++) {
				node = nodes[i];
				childNodeList.push(node);
				if (!tree.isLeaf(jqTree, node.target)) {
					var children = tree.getChildren(jqTree, node.target);
					childNodeList = childNodeList.concat(getChildNodeList(
							jqTree, tree, children));
				}
			}
		}
		return childNodeList;
	}
})(jQuery);