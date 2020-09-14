<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="header.jsp"%>
<div id="grid"></div>
<script type="text/javascript">
	Ext.onReady(function() {
		/*
			  指定url  指定fields  就可以一部请求后台数据，当然后台必须返回的数组格式的数据。很简单吧，这个store常用语表单中的下拉框取值。
		 */
		/*
		var store = new Ext.data.SimpleStore({
		    autoLoad : true,
			url : ctx + '/system/loadItemDictionary.do',
			fields : ['proTypeId', 'typeName'],
			baseParams : {
		      	itemName : label
		     }
		   });
		 */
		/* sonStore将JsonReader和HttpProxy整合在一起了，提供了一个从后台获取json数据的简便方法，分页也非常方便。
		  	 后台返回标准的json数据既可以。 
		 */

		var myStore = new Ext.data.JsonStore({
			url : ctx + "/loadData",
			root : "root",
			totalProperty : "totalProperty",
			remoteSort : true,
			fields : [ {
				name : "id",
				type : "int"
			}, {
				name : 'name'
			}, {
				name : "date",
				type : 'date',
				dateFormat : 'time'
			} ]
		});
		myStore.setDefaultSort("id", "desc");
		

		var grid = new Ext.grid.GridPanel({
			id : 'gridPanel',
			title : 'Demo',
			width : 800,
			autoHeight: true,
			renderTo : 'grid',
			store : myStore,
			sm: new Ext.grid.RowSelectionModel({singleSelect:true}), //单选
			frame: true,
			bbar: new Ext.PagingToolbar({
		        store: myStore,
		        autoHeight:true,
		        autoWidth:true,
		        displayInfo: true,
		        pageSize: 20,
		        prependButtons: true,
		        displayMsg: '{0} - {1} of {2}',
		        emptyMsg :'没有记录',
		        prevText :'上一页'，
		        refreshText：'刷新',
		        items: [
		           // 'text 1'
		        ]
		    }),
			columns:[ {
				header : 'ID',
				dataIndex : 'id'
			}, {
				header : 'Name',
				dataIndex : 'name'
			}, {
				header : 'Date',
				dataIndex : 'date',
				type : 'date',
				renderer : new Ext.util.Format.dateRenderer('Y-m-d H:i:s'),
				width:200
			} ],
			
		});
		
		myStore.load({
			params : {
				start : 0,
				limit : 25
			}
		});

		/*
			groupField标识按某一个字段进行分组显示
		 */
		/*
		   store = new Ext.data.GroupingStore({
		        proxy : new Ext.data.HttpProxy({
		        url : ctx + "/flow/nodesFieldRights.do?defId="+ this.defId
		        }),
		        reader : new Ext.data.JsonReader({
		            root : "result",
		            id : "id",
		            fields : [ {
		                name : "rightId",
		                type : "int"
		            }, {
		                name : "mappingId",
		                type : "int"
		            }, "taskName", {
		                name : "readWrite",
		                type : "int"
		            }, {
		                name : "refieldId",
		                type : "int"
		            }, "fieldName", "fieldLabel" ]
		        }),
		        groupField : "taskName"
		    });
		    this.store.load();
		 */

	});
</script>
<%@include file="foot.jsp"%>