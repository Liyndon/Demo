<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="header.jsp"%>
<script type="text/javascript" src="${ctx}/js/lib/ext-3.2.0/examples/ux/ColumnHeaderGroup.js"></script>

<div id="grid4"></div>
<script type="text/javascript">
	Ext.onReady(function(){
		  var structure = {
			        Asia: ['Beijing', 'Tokyo'],
			        Europe: ['Berlin', 'London', 'Paris']
			    },
			    products = ['ProductX', 'ProductY'],
			    fields = [],
			    columns = [],
			    data = [],
			    continentGroupRow = [],
			    cityGroupRow = [];
		  
		  function generateConfig(){
		        var arr,
		            numProducts = products.length;
		            
		        Ext.iterate(structure, function(continent, cities){
		            continentGroupRow.push({
		                header: continent,
		                align: 'center',
		                colspan: cities.length * numProducts
		            });
		            
		            Ext.each(cities, function(city){
		                cityGroupRow.push({
		                    header: city,
		                    colspan: numProducts,
		                    align: 'center'
		                });
		                
		                Ext.each(products, function(product){
		                    fields.push({
		                        type: 'int',
		                        name: city + product
		                    });
		                    columns.push({
		                        dataIndex: city + product,
		                        header: product,
		                        renderer: Ext.util.Format.usMoney
		                    });
		                });
		                arr = [];
		                for(var i = 0; i < 20; ++i){
		                    arr.push((Math.floor(Math.random()*11) + 1) * 100000);
		                }
		                data.push(arr);
		            });
		        })
		    }
		
		  generateConfig();
		  
		  var group = new Ext.ux.grid.ColumnHeaderGroup({
		        rows: [continentGroupRow, cityGroupRow]
		    });
		  
		  var grid = new Ext.grid.GridPanel({
		        renderTo: 'grid4',
		        title: 'Sales By Location',
		        width: 1000,
		        height: 400,
		        store: new Ext.data.ArrayStore({
		            fields: fields,
		            data: data
		        }),
		        columns: columns,
		        viewConfig: {
		            forceFit: true
		        },
		        plugins: group
		    });
	

});

</script>
<%@include file="foot.jsp"%>