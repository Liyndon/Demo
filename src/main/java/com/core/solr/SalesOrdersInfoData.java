package com.core.solr;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Properties;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;


public class SalesOrdersInfoData {
	
	private static String tomcat_solr = "http://192.168.2.211:8983/solr/SalesOrdersInfo/";
	public static HttpSolrServer solr = null;


	public static void generateData() {
		
		solr = new HttpSolrServer(tomcat_solr);
		solr.setConnectionTimeout(100);
		solr.setDefaultMaxConnectionsPerHost(100);
        solr.setMaxTotalConnections(100);
		
		InputStream is = SalesOrdersInfoData.class.getResourceAsStream("/jdbc.properties");
    	Properties prop = new Properties();
		try {
			prop.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		JDBCSource jdbcSource = new JDBCSource(prop.getProperty("connection.driver_class"),
				prop.getProperty("connection.url"),
				prop.getProperty("connection.username"),
				prop.getProperty("connection.password"), null);
		
		
		Object[][] obj = jdbcSource.DoQuerry(" select * from SalesOrdersInfo t; ");
		
		
		Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
		
		for (int i = 1; i < obj.length; i++) {
			//设置每个字段不得为空，可以在提交索引前进行检查
			SolrInputDocument doc = new SolrInputDocument();
			//在这里请注意date的格式，要进行适当的转化，上文已提到
			for (int j = 0; j < obj[i].length; j++) {
				doc.addField(obj[0][j].toString(), obj[i][j].toString());
				docs.add(doc);
				System.out.println(obj[0][j].toString() + ":" + obj[i][j].toString());
			}
		}
		
		try {
        	solr.add(docs);
        	//对索引进行优化
        	solr.optimize();
        	solr.commit();
        } catch (Exception e) {
               e.printStackTrace();
        }
	}
	
	public static void selectData() {
		
		solr = new HttpSolrServer(tomcat_solr);
		solr.setConnectionTimeout(100);
		solr.setDefaultMaxConnectionsPerHost(100);
        solr.setMaxTotalConnections(100);
        
        SolrQuery query = null;
		try {
			// 初始化查询对象
			query = new SolrQuery();
			query.set("q", "*:*");
			query.addDateRangeFacet("OrderDate", new Date(2015, 11, 01, 00, 00, 00), new Date(2015, 11, 03, 23, 59, 59), "+1DAY");
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		QueryResponse rsp = null;
		try {
			rsp = solr.query(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(null == rsp){
            return;
	     }
	     SolrDocumentList tmpLists = rsp.getResults();
	     
         System.out.println(rsp.getResponseHeader());
         System.out.println("搜索条数 = " + tmpLists.size());
         for (int i = 0; i < tmpLists.size(); ++i) {
             System.out.println(i + " --> " + tmpLists.get(i));
         }
		
	}
	
	
	
	public static void main(String[] args) {
		//generateData();
		selectData();
	}
}
