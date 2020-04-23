package com.gittors.gateway.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.embedded.netty.NettyServerCustomizer;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import reactor.netty.http.server.HttpServer;

//@Component
public class NettyWebServerFactoryPortCustomizer implements WebServerFactoryCustomizer<NettyReactiveWebServerFactory> {

	@Override
	public void customize(NettyReactiveWebServerFactory serverFactory) {
		serverFactory.setPort(8088);
		List list = new ArrayList<>();
		list.add(new EventLoopNettyCustomizer());
		serverFactory.setServerCustomizers(list);

	}
}

class EventLoopNettyCustomizer implements NettyServerCustomizer {

	/**
	 * Netty 值得统计的信息 外在信息 连接统计信息：channelActive / channelInactive 收数据统计：channelRead
	 * 发数据统计：write（写到缓存里），用 ctx.write(msg).addListener() 更准确 异常统计：exceptionCaught /
	 * ChannelFuture 内在信息 线程数：根据不同实现计算，比如：nioEventLoopGroup.executorCount()；
	 * 待处理任务：executor.pendingTask()； 积累的数据：channelOutBoundBuffer.totalPendingSize，这是
	 * Channel 级别的； 可写状态切换：channelWritabilityChanged； 触发事件统计：userEventTriggered，比如
	 * IdleStateEvent； ByteBuf 分配细节：Pooled/UnpooledByteAllocator.DEFAULT.metric()；
	 * ———————————————— 版权声明：本文为CSDN博主「乌鲁木齐001号程序员」的原创文章，遵循 CC 4.0 BY-SA
	 * 版权协议，转载请附上原文出处链接及本声明。
	 * 原文链接：https://blog.csdn.net/weixin_33669968/java/article/details/104552393
	 */
	  NioEventLoopGroup boss = new NioEventLoopGroup(0, new DefaultThreadFactory("boss"));
      NioEventLoopGroup worker = new NioEventLoopGroup(0, new DefaultThreadFactory("worker"));
      
	MetricHandler childHandler = new MetricHandler();

	@Override
	public HttpServer apply(HttpServer httpServer) {
		
		// TODO Auto-generated method stub
		new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(1000);
						System.out.println("executorCount:" + boss.executorCount());
						System.out.println("连接数:" + childHandler.totalConnectionNumber.get());
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}.start();
		
		return httpServer.tcpConfiguration(tcpServer -> tcpServer.bootstrap(serverBootstrap ->serverBootstrap
				.group(boss, worker).childHandler(childHandler).channel(NioServerSocketChannel.class)));

		/*return httpServer.tcpConfiguration(tcpServer -> tcpServer.bootstrap(serverBootstrap -> serverBootstrap
				.group(boss, worker).childHandler(childHandler).channel(NioServerSocketChannel.class)));*/
	}

}