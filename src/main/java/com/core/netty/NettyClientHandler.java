package com.core.netty;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

public class NettyClientHandler extends SimpleChannelUpstreamHandler {

	private BufferedReader sin = new BufferedReader(new InputStreamReader(
			System.in));

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		System.out.println("已经与Server建立连接。。。。");
		System.out.println("\n请输入要发送的信息：");
		try {
			super.channelConnected(ctx, e);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			e.getChannel().write(sin.readLine());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		if (e.getMessage() instanceof String) {
			String message = (String) e.getMessage();
			System.out.println(message);
			try {
				e.getChannel().write(sin.readLine());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
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
		e.getChannel().close();
	}
}
