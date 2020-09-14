package com.core.mq;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.SendResult;

public class ProducerTest {
    public static void main(String[] args) {
    	InputStream in =null;
    	in=ProducerTest.class.getResourceAsStream("/MessageQueue.properties");
    	 Properties properties = new Properties();
    	 try {
    		 properties.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	 /*
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.ProducerId, "xxx");//您在控制台创建的Producer ID
        properties.put(PropertyKeyConst.AccessKey,"xxx");// AccessKey 身份验证，在阿里云服务器管理控制台创建
        properties.put(PropertyKeyConst.SecretKey, "xxxx");// SecretKey 身份验证，在阿里云服务器管理控制台创建
        */
    	
        Producer producer = ONSFactory.createProducer(properties);
        
        // 在发送消息前，必须调用start方法来启动Producer，只需调用一次即可。
        producer.start();
        //循环发送消息
        for (int i = 0; i < 100; i++){
            Message msg = new Message( //
                // Message Topic
                "lixyon",
                // Message Tag 可理解为Gmail中的标签，对消息进行再归类，方便Consumer指定过滤条件在MQ服务器过滤
                "TagA",
                // Message Body 可以是任何二进制形式的数据， MQ不做任何干预，
                // 需要Producer与Consumer协商好一致的序列化和反序列化方式
                "Hello MQ".getBytes());
            // 设置代表消息的业务关键属性，请尽可能全局唯一。
            // 以方便您在无法正常收到消息情况下，可通过MQ控制台查询消息并补发。
            // 注意：不设置也不会影响消息正常收发
            msg.setKey("ORDERID_" + i);
            // deliver time 单位 ms，指定一个时刻，在这个时刻之后才能被消费，这个例子表示 3s 后才能被消费
            long delayTime = 3000;
            msg.setStartDeliverTime(System.currentTimeMillis() + delayTime);
            
            // 发送消息，只要不抛异常就是成功
            SendResult sendResult = producer.send(msg);
            System.out.println(sendResult);
        }
        // 在应用退出前，销毁Producer对象
        // 注意：如果不销毁也没有问题
        producer.shutdown();
    }
}