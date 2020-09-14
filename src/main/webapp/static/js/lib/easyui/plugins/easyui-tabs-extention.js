//扩展easyui-tabs邮件关闭按钮
/**
 * @author Johnathan.zhang
 */
$.extend($.fn.tabs.methods,{
    allTabs:function(jq){
        return $(jq).tabs('tabs');
    },
    //显示右键菜单
    showCloseBar:function(jq, opt){
    	var e =opt.event;
    	var title = opt.tabTitle;
    	var closeList = $(jq).tabs("allCanCloseTabs", title);
    	if($("#easyui-tabs-close-bar-menu-panel").length == 0){
    		$(jq).data('tab-close-bar', $("<div id='easyui-tabs-close-bar-menu-panel' class='easyui-menu' style='width:120px;'></div>").appendTo("body"));
    		$(jq).data('tab-close-bar').menu({}).menu("appendItem",{
    			text: '关闭当前',
    			iconCls: 'Bulletcross',
    			onclick: function(){
    				$(jq).tabs("closeTabs", closeList[0]);
    			}
    		}).menu("appendItem",{separator: true}).menu("appendItem",{
    			text: '关闭所有',
    			onclick: function(){
    				$(jq).tabs("closeTabs", closeList[1]);
    			}
    		}).menu("appendItem",{
    			text: '关闭其他',
    			onclick: function(){
    				$(jq).tabs("closeTabs", closeList[2]);
    			}
    		}).menu("appendItem",{separator: true}).menu("appendItem",{
    			text: '关闭左侧',
    			onclick: function(){
    				$(jq).tabs("closeTabs", closeList[3]);
    			}
    		}).menu("appendItem",{
    			text: '关闭右侧',
    			onclick: function(){
    				$(jq).tabs("closeTabs", closeList[4]);
    			}
    		});
    	}else if(!$(jq).data('tab-close-bar')){
    		$(jq).data('tab-close-bar', $("#easyui-tabs-close-bar-menu-panel"));
    	}
    	$(jq).data('tab-close-bar').menu('show', {
	        left: e.pageX,
	        top: e.pageY
	    }).data('tabTitle',title);
    	//全部启用
    	$(jq).tabs("enableAllCloseMenu");
    	//当前不可关闭
    	if(closeList[0].length == 0){
    		$(jq).data('tab-close-bar').menu("disableItem", $(jq).data('tab-close-bar').menu("findItem", "关闭当前").target);
    	}
    	if(closeList[1].length == 0){
    		$(jq).data('tab-close-bar').menu("disableItem", $(jq).data('tab-close-bar').menu("findItem", "关闭所有").target);
    	}
    	if(closeList[2].length == 0){
    		$(jq).data('tab-close-bar').menu("disableItem", $(jq).data('tab-close-bar').menu("findItem", "关闭其他").target);
    	}
    	if(closeList[3].length == 0){
    		$(jq).data('tab-close-bar').menu("disableItem", $(jq).data('tab-close-bar').menu("findItem", "关闭左侧").target);
    	}
    	if(closeList[4].length == 0){
    		$(jq).data('tab-close-bar').menu("disableItem", $(jq).data('tab-close-bar').menu("findItem", "关闭右侧").target);
    	}
    },
    enableAllCloseMenu: function(jq){
    	$(jq).data('tab-close-bar').menu("enableItem", $(jq).data('tab-close-bar').menu("findItem", "关闭当前").target);
    	$(jq).data('tab-close-bar').menu("enableItem", $(jq).data('tab-close-bar').menu("findItem", "关闭所有").target);
    	$(jq).data('tab-close-bar').menu("enableItem", $(jq).data('tab-close-bar').menu("findItem", "关闭其他").target);
    	$(jq).data('tab-close-bar').menu("enableItem", $(jq).data('tab-close-bar').menu("findItem", "关闭左侧").target);
    	$(jq).data('tab-close-bar').menu("enableItem", $(jq).data('tab-close-bar').menu("findItem", "关闭右侧").target);
    },
    allCanCloseTabs:function(jq, title){
    	var canCloseTabs = [];
    	var tabs = $(jq).tabs('allTabs');
    	var currentList = [];
    	var allList = [];
    	var otherList = [];
    	var leftSideList = [];
    	var rightSideList = [];
    	var currentTabIndex = $(jq).tabs("getTabIndex", $(jq).tabs("getTab", title));
    	$.each(tabs, function(index, item){
    		var tab = this;
    		var opts = tab.panel("options");
    		var mtitle = opts.title;
    		var closable = opts.closable;
    		if(closable){
    			if(index < currentTabIndex){//左边的
        			leftSideList.push(mtitle);
        		}
    			if(index > currentTabIndex){//左边的
        			rightSideList.push(mtitle);
        		}
    			if(index == currentTabIndex){//当前的
    				currentList.push(mtitle);
    			}
    			if(index != currentTabIndex){//其他的
    				otherList.push(mtitle);
    			}
    			allList.push(mtitle);//全部的
    		}
    	});
    	canCloseTabs.push(currentList);//当前的为0
    	canCloseTabs.push(allList);//全部的为1
    	canCloseTabs.push(otherList);//其他的为2
    	canCloseTabs.push(leftSideList);//左侧的为3
    	canCloseTabs.push(rightSideList);//右侧的为4
    	return canCloseTabs;
    },
    closeTabs:function(jq, list){ //关闭全部
        $.each(list,function(i,n){
            $(jq).tabs('close', n);
        });
    },
});