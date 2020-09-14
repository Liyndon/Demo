package com.core.mq;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.SendResult;
import com.aliyun.openservices.ons.api.transaction.LocalTransactionExecuter;
import com.aliyun.openservices.ons.api.transaction.TransactionProducer;
import com.aliyun.openservices.ons.api.transaction.TransactionStatus;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ProduceTransMsgWithSpring {
	
	public static void main(String[] args) {
		/**
		 * 事务消息生产者Bean配置在transactionProducer.xml中,
		 * 可通过ApplicationContext获取或者直接注入到其他类(比如具体的Controller)中. 请结合例子"发送事务消息"
		 */
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"applicationContext.xml");
		TransactionProducer transactionProducer = (TransactionProducer) context
				.getBean("transactionProducer");
		Message msg = new Message("lixyon", "TagA",
				"Hello MQ transaction===".getBytes());
		SendResult sendResult = transactionProducer.send(msg,
				new LocalTransactionExecuter() {
					@Override
					public TransactionStatus execute(Message msg, Object arg) {
						System.out.println("执行本地事务");
						return TransactionStatus.CommitTransaction; // 根据本地事务执行结果来返回不同的TransactionStatus
					}
				}, null);
	}
}