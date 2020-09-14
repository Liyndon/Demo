<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="header.jsp"%>
<div id="grid2"></div>
<script type="text/javascript">
	Ext.onReady(function() {
		Ext.state.Manager.setProvider(new Ext.state.CookieProvider());
		
		var store = new Ext.data.Store({
			//远程加载 xml数据
			url : 'sheldon.xml',
			//XMl数据解析器
			reader : new Ext.data.XmlReader({
				record : 'Item',
				id : 'ASIN',
				totalRecords : '@total'
			}, [ {
				name : 'Author',mapping : 'ItemAttributes > Author'
			}, 'Title', 'Manufacturer', 'ProductGroup', 'DetailPageURL' ])
		});
		//创建grid
		  var grid = new Ext.grid.GridPanel({
		        store: store,
		        columns: [
		                  //表头
		            {header: "Author", width: 120, dataIndex: 'Author', sortable: true},
		            {header: "Title", width: 180, dataIndex: 'Title', sortable: true},
		            {header: "Manufacturer", width: 115, dataIndex: 'Manufacturer', sortable: true},
		            {header: "Product Group", width: 100, dataIndex: 'ProductGroup', sortable: true}
		        ],
		        //单选
				sm: new Ext.grid.RowSelectionModel({singleSelect: true}),
				viewConfig: {
					forceFit: true
				},
		        height:210,
				split: true,
				region: 'north'
		    });
			//定义一个模板
			var bookTplMarkup = [
			             		'Title: <a href="{DetailPageURL}" target="_blank">{Title}</a><br/>',
			             		'Author: {Author}<br/>',
			             		'Manufacturer: {Manufacturer}<br/>',
			             		'Product Group: {ProductGroup}<br/>'
			             	];
		
		
			var bookTpl = new Ext.Template(bookTplMarkup);
		
		//创建一个panel 面板
			var ct = new Ext.Panel({
				renderTo: 'grid2',
				frame: true,
				title: 'Book List',
				width: 540,
				height: 400,
				layout: 'border',
				items: [
					grid,
					{
						id: 'detailPanel',
						region: 'center',
						bodyStyle: {
							background: '#ffffff',
							padding: '7px'
						},
						html: 'Please select a book to see additional details.'
					}
				]
			});
			grid.getSelectionModel().on('rowselect', function(sm, rowIdx, r) {
				//将选择行的详细信息显示出来
				var detailPanel = Ext.getCmp('detailPanel');
				//将数据写入
				bookTpl.overwrite(detailPanel.body, r.data);
			});
			//加载 数据
		    store.load();
	});
</script>
<%@include file="foot.jsp"%>