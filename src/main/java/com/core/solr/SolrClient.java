package com.core.solr;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;


public class SolrClient {

	private static String tomcat_solr = "http://localhost:8983/solr/core/";
	public static HttpSolrServer solr = null;

	/**
	 * 构造函数，根据solr的路径，返回一个HttpSolrServer
	 */
	@SuppressWarnings("deprecation")
	public static void clientTest() {
		solr = new HttpSolrServer(tomcat_solr);
		solr.setConnectionTimeout(100);
		solr.setDefaultMaxConnectionsPerHost(100);
        solr.setMaxTotalConnections(100);
	}

	

	/**
	 * 添加索引 bean
	 */
	public static void addIndex() {
		Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
		
		Person p =new Person();
		
		//设置每个字段不得为空，可以在提交索引前进行检查
		SolrInputDocument doc = new SolrInputDocument();
		//在这里请注意date的格式，要进行适当的转化，上文已提到
		doc.addField("id", p.getId());
		doc.addField("name", p.getName());
		docs.add(doc);
 
        try {
        	solr.add(docs);
        	//对索引进行优化
        	solr.optimize();
        	solr.commit();
        } catch (Exception e) {
               e.printStackTrace();
        }
	}

	/**
	 * 刪除所有索引
	 */
	public static void deleteAll() {
		try {
			// 删除所有的索引
			solr.deleteByQuery("*:*");
			solr.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static QueryResponse search() {

		SolrQuery query = null;
		try {
			// 初始化查询对象
			query = new SolrQuery();
			query.set("q", "*:*");
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		QueryResponse rsp = null;
		try {
			rsp = solr.query(query);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		// 返回查询结果
		return rsp;
	}
	
	public static void selectBean() {
		
		QueryResponse rsp = search();
		
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

	/**
	 * 根据索引号删除索引
	 */
	public static void deleteById() {
		try {
			List<String> ids = new ArrayList<String>();
			solr.deleteById(ids);
			solr.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		
		clientTest();

		deleteAll();
		
		addIndex();
		
		selectBean();
	}
}
