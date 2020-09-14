package com.core.mq;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.transaction.LocalTransactionChecker;
import com.aliyun.openservices.ons.api.transaction.TransactionStatus;

public class DemoLocalTransactionChecker implements LocalTransactionChecker {
	
	public TransactionStatus check(Message msg) {
		System.out.println("开始回查本地事务状态");
		return TransactionStatus.CommitTransaction; // 根据本地事务状态检查结果返回不同的TransactionStatus
	}
	
}
