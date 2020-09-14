<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="header.jsp"  %>
	<div id="grid1"></div>
	<script type="text/javascript">
	Ext.onReady(function(){
		Ext.state.Manager.setProvider(new Ext.state.CookieProvider());
		//数据
		 var myData = [
		               ['1','于中'],
		               ['2','欠妥'],
		               ['3','闪在'],
		           ];
		// 数据格式
		    var store = new Ext.data.ArrayStore({
		        fields: [
		           {name: 'id'},
		           {name: 'name'},
		        ]
		    });
		//加载数据
		    store.loadData(myData);
		    // create the Grid
		    var grid = new Ext.grid.GridPanel({
		        store: store,
		        columns: [
		                  //列标题
		            {id:'id',header: 'ID', width: 160, sortable: true, dataIndex: 'id'},
		            {header: 'name', width: 75, sortable: true, dataIndex: 'name'},
		        ],
		        stripeRows: true,
		        height: 150,
		        width: 300,
		        title: 'Array Grid',
		        stateful: true,
		        stateId: 'grid'        
		    });
		    //渲染
		    grid.render('grid1');
	});
	
	
	</script>
<%@include file="foot.jsp" %>