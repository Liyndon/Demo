<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="header.jsp"%>
<script type="text/javascript"
	src="${ctx}/js/lib/ext-3.2.0/examples/ux/CheckColumn.js"></script>
	 <script type="text/javascript" src="${ctx}/js/lib/ext-3.2.0/examples/shared/examples.js"></script>

<div id="grid5"></div>
<script type="text/javascript">
	Ext.onReady(function() {
		//日期格式化
		function formatDate(value) {
			return value ? value.dateFormat('M d, Y') : '';
		}
		//创建 一个表单对象
		var fm = Ext.form;
		
		var checkColumn = new Ext.grid.CheckColumn({
			header : 'Indoor',
			dataIndex : 'indoor',
			width : 55
		});
		
	    var cm = new Ext.grid.ColumnModel({
	        defaults: {
	        	//排序
	            sortable: true 
	        },
	        columns: [
	            {
	                id: 'common',
	                header: 'Common Name',
	                dataIndex: 'common',
	                width: 220,
	                editor: new fm.TextField({
	                	//不允许为空
	                    allowBlank: false
	                })
	            }, {
	            	id:'light',
	                header: 'Light',
	                dataIndex: 'light',
	                width: 130,
	                editor: new fm.ComboBox({
	                    typeAhead: true,
	                    triggerAction: 'all',
	                   //transform: 'light',
	                    lazyRender: true,
	                    listClass: 'x-combo-list-small'
	                })
	            }, {
	                header: 'Price',
	                dataIndex: 'price',
	                width: 70,
	                align: 'right',
	                renderer: 'usMoney',
	                editor: new fm.NumberField({
	                    allowBlank: false,
	                    allowNegative: false,
	                    maxValue: 100000
	                })
	            }, {
	                header: 'Available',
	                dataIndex: 'availDate',
	                width: 95,
	                renderer: formatDate,
	                editor: new fm.DateField({
	                    format: 'm/d/y',
	                    minValue: '01/01/06',
	                    disabledDays: [0, 6],
	                    disabledDaysText: 'Plants are not available on the weekends'
	                })
	            },
	            checkColumn 
	        ]
	    });
	    
	    //从远程加载 数据
	    var store = new Ext.data.Store({
	       autoDestroy: true,
	        url: 'plants.xml',
	        reader: new Ext.data.XmlReader({
	            record: 'plant',
	            fields: [
	                {name: 'common', type: 'string'},
	                {name: 'botanical', type: 'string'},
	                {name: 'light',type: 'string'},
	                {name: 'price', type: 'float'},             
	                {name: 'availDate', mapping: 'availability', type: 'date', dateFormat: 'm/d/Y'},
	                {name: 'indoor', type: 'bool'}
	            ]
	        }),
	        sortInfo: {field:'common', direction:'ASC'}
	    });
	    
	    // create the editor grid
	    var grid = new Ext.grid.EditorGridPanel({
	        store: store,
	        cm: cm,
	        renderTo: 'grid5', //渲染div
	        autoWidth: true,
	        autoHeight: true,
	      //  autoExpandColumn: 'common', // 自动展开列
	        title: 'Edit Plants?',
	        frame: true,
	        plugins: checkColumn,
	        clicksToEdit: 1,
	        loadMask: true,
	        tbar: [{
	            text: 'Add Plant',
	            handler : function(){
	                var Plant = grid.getStore().recordType;
	                //新建一个plant对象
	                var p = new Plant({
	                    common: 'New Plant 1',
	                    light: 'Mostly Shade',
	                    price: 0,
	                    availDate: (new Date()).clearTime(),
	                    indoor: false
	                });
	                grid.stopEditing();
	                store.insert(0, p);
	                grid.startEditing(0, 0);
	            }
	        }],
	        bbar: new Ext.PagingToolbar({
	            pageSize: 10,
	            store: store,
	            displayInfo: true,
	            displayMsg: 'Displaying topics {0} - {1} of {2}',
	            emptyMsg: "No topics to display",
	            items:[
	                '-', {
	                pressed: true,
	                enableToggle:true,
	                text: 'Show Preview',
	                cls: 'x-btn-text-icon details',
	                toggleHandler: function(btn, pressed){
	                    var view = grid.getView();
	                    view.showPreview = pressed;
	                    view.refresh();
	                }
	            }]
	        })
	    });
	    store.load({params:{start:0, limit:10}});
	});
</script>
<%@include file="foot.jsp"%>