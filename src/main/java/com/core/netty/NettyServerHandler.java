package com.core.netty;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

public class NettyServerHandler extends SimpleChannelUpstreamHandler {
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		if (e.getMessage() instanceof String) {
			String message = (String) e.getMessage();
			System.out.println("Client发来:" + message);
			e.getChannel().write("Server已收到刚发送的:" + message);
			System.out.println("\n等待客户端输入。。。");
		}
		try {
			super.messageReceived(ctx, e);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		e.getCause().printStackTrace();
		Channel ch = e.getChannel();
		ch.close();
	}
	
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		 System.out.println("有一个客户端注册上来了。。。");  
         System.out.println("Client:" + e.getChannel().getRemoteAddress());  
         System.out.println("Server:" + e.getChannel().getLocalAddress());  
         System.out.println("\n等待客户端输入。。。");  
         super.channelConnected(ctx, e);  
	}
}