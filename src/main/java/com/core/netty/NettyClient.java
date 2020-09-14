package com.core.netty;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * Netty 客户端
 * @author LXY
 *
 */
public class NettyClient {
	public static void main(String[] args) throws Exception {
		
		//通道
		ChannelFactory factory = new NioClientSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool());
		//通道初始化辅助类 提供了初始化通道或子通道所需要的数据结构
		ClientBootstrap bootstrap = new ClientBootstrap(factory);
		//通道传输
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() {
				ChannelPipeline pipeline = Channels.pipeline();
				pipeline.addLast("encode", new StringEncoder());
				pipeline.addLast("decode", new StringDecoder());
				//数据
				pipeline.addLast("handler", new NettyClientHandler());
				return pipeline;
			}
		});
		//无延迟
		bootstrap.setOption("tcpNoDelay", true);
		//保持连接
		bootstrap.setOption("keepAlive", true);
		//连接服务器
		ChannelFuture future = bootstrap.connect(new InetSocketAddress(
				"127.0.0.1", 8089));
		// Wait until the connection is closed or the connection attempt fails.
		future.getChannel().getCloseFuture().awaitUninterruptibly();

		// Shut down thread pools to exit.
		bootstrap.releaseExternalResources();

	}
}
