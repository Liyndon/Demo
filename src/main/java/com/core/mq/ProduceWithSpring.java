package com.core.mq;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.SendResult;
import com.aliyun.openservices.ons.api.exception.ONSClientException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ProduceWithSpring {
	
    public static void main(String[] args) throws ParseException {
    	
        /**
         * 生产者Bean配置在producer.xml中,可通过ApplicationContext获取或者直接注入到其他类(比如具体的Controller)中.
         */
    	
        ApplicationContext context = new ClassPathXmlApplicationContext("MessageQueue.xml");
        Producer producer = (Producer) context.getBean("producer");
        //循环发送消息
        for (int i = 0; i < 100; i++) {
            Message msg = new Message( 
                    // Message Topic
                    "lixyon",
                    // Message Tag 可理解为Gmail中的标签，对消息进行再归类，方便Consumer指定过滤条件在MQ服务器过滤
                    "TagA",
                    // Message Body 可以是任何二进制形式的数据， MQ不做任何干预，
                    // 需要Producer与Consumer协商好一致的序列化和反序列化方式
                    "Hello MQ".getBytes());
            // 设置代表消息的业务关键属性，请尽可能全局唯一。
            // 以方便您在无法正常收到消息情况下，可通过MQ 控制台查询消息并补发。
            // 注意：不设置也不会影响消息正常收发
            msg.setKey("ORDERID_100");
            // deliver time 单位 ms，指定一个时刻，在这个时刻之后才能被消费，这个例子表示 3s 后才能被消费
            long delayTime = 3000;
            msg.setStartDeliverTime(System.currentTimeMillis() + delayTime);
            /**
             * 定时消息投递，设置投递的具体时间戳，单位毫秒例如2016-03-07 16:21:00投递
            */
           // long timeStamp =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2016-03-07 16:21:00").getTime();
          //  msg.setStartDeliverTime(timeStamp);
            
            
            // 发送消息，只要不抛异常就是成功
            try {
                SendResult sendResult = producer.send(msg);
                assert sendResult != null;
                System.out.println("send success: " + sendResult.getMessageId());
            }catch (ONSClientException e) {
                System.out.println("发送失败");
            }
        }
    }
}