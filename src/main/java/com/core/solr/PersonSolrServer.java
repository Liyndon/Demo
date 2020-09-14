package com.core.solr;

import java.net.MalformedURLException;  
import java.util.ArrayList;  
import java.util.List;  
    
import org.apache.solr.client.solrj.SolrQuery;  
import org.apache.solr.client.solrj.impl.BinaryRequestWriter;  
import org.apache.solr.client.solrj.impl.HttpSolrServer;
//import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;  
import org.apache.solr.client.solrj.response.QueryResponse;  
import org.apache.solr.common.SolrDocument;  
import org.apache.solr.common.SolrInputDocument;  
import org.slf4j.Logger;  
import org.slf4j.LoggerFactory;  
    
/** 
 * @author Sam 时间：2011-9-16 下午3:25:15 
 */  
public class PersonSolrServer {  
        
    private final static String URL = "http://localhost/solr/person";  
    private Logger logger = LoggerFactory.getLogger(this.getClass());  
    private final static Integer SOCKE_TTIMEOUT = 1000; // socket read timeout  
    private final static Integer CONN_TIMEOUT = 100;  
    private final static Integer MAXCONN_DEFAULT = 100;  
    private final static Integer MAXCONN_TOTAL = 100;  
    private final static Integer MAXRETRIES = 1;  
    private HttpSolrServer server = null;  
    private final static String ASC = "asc";  
        
    public PersonSolrServer() throws MalformedURLException {  
        System.out.println("初始化solr服务..");  
        server = new HttpSolrServer( URL );//使用HTTPClient 和solr服务器进行通信  
        server.setRequestWriter(new BinaryRequestWriter());//使用流输出方式  
        server.setSoTimeout(SOCKE_TTIMEOUT);// socket read timeout  
        server.setConnectionTimeout(CONN_TIMEOUT);  
        server.setDefaultMaxConnectionsPerHost(MAXCONN_DEFAULT);  
        server.setMaxTotalConnections(MAXCONN_TOTAL);  
        server.setFollowRedirects(false);  
        server.setAllowCompression(true);  
        server.setMaxRetries(MAXRETRIES); // defaults to 0.  > 1 not recommended.  
    }  
        
    /** 
     * 创建索引 
     */  
    public void createIndex(Person pd) throws Exception {  
        SolrInputDocument doc = new SolrInputDocument();  
        doc.addField("id", pd.getId());  
        doc.addField("name", pd.getName());  
        doc.addField("remark", pd.getRemark());
        server.add(doc);  
        server.optimize();  
        server.commit();  
        System.out.println("----索引创建完毕!!!");      
    }  
    /** 
     * 删除索引 
     * @author Sam 时间：2011-9-16 下午3:32:55    
     * @throws Exception 
     */  
    public void delIndex() throws Exception {  
        server.deleteByQuery("*:*");  
        server.commit();  
        System.out.println("----索引清除完毕!!!");  
    }  
        
    /** 
     * 查询 
     * @author Sam 时间：2011-9-16 下午3:33:14    
     * @param key 
     * @param startPage 
     * @param pageSize 
     * @throws Exception 
     */  
    public List<Integer> queryList(String key, Integer start, Integer rows) throws Exception {  
        SolrQuery query = new SolrQuery(getkey(key));  
        query.setHighlight(true); //开启高亮组件  
        query.addHighlightField("id");  
        query.addHighlightField("chName");//高亮字段  
        query.addHighlightField("enName");  
        query.setHighlightSimplePre("<font color='red'>");//前缀  
        query.setHighlightSimplePost("</font>");//后缀  
        query.set("hl.usePhraseHighlighter", true);  
        query.set("hl.highlightMultiTerm", true);  
        query.set("hl.snippets", 3);//三个片段,默认是1  
        query.set("hl.fragsize", 50);//每个片段50个字，默认是100  
        query.setStart(start); //起始位置 …分页  
        query.setRows(rows);//文档数  
            
        QueryResponse rep = server.query(query);  
        List<SolrDocument> docs = rep.getResults();//得到结果集  
        List<Integer> idList = new ArrayList<Integer>();  
        for(SolrDocument doc : docs) {  
            idList.add(Integer.parseInt((String) doc.getFieldValue("id")));  
            System.out.println(doc.getFieldValue("chName") + "|" + doc.getFieldValue("enName"));  
        }  
        return idList;  
    }  
        
    public String getkey(String strWord) {  
        if(strWord.indexOf(" ") > 0 ){  
            String wordAnd = strWord.replace(" ", "* AND *");  
            String wordOr = strWord.replace(" ", "* *");  
            String rt = "(*" + wordAnd + "*) *" + wordOr + "* " + strWord;  
            return rt;  
        } else {  
            return "*" + strWord + "* " + strWord;  
        }  
            
    }  
    
 
    
}