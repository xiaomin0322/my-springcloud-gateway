package com.gittors.gateway;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.embedded.netty.NettyServerCustomizer;
import org.springframework.context.annotation.Bean;

import com.gittors.gateway.config.MetricHandler;

import io.netty.channel.ServerChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import reactor.netty.http.server.HttpServer;

/**
 * https://www.baeldung.com/spring-boot-reactor-netty
 * @author Zengmin.Zhang
 *
 */
@SpringBootApplication
public class GateWayApplication {
	
	@Bean
	public NettyReactiveWebServerFactory nettyReactiveWebServerFactory() {
	    NettyReactiveWebServerFactory webServerFactory = new NettyReactiveWebServerFactory();
	    webServerFactory.addServerCustomizers(new EventLoopNettyCustomizer());
	    return webServerFactory;
	}
	
	

	public static void main(String[] args) {
		SpringApplication.run(GateWayApplication.class, args);
	}
}

class EventLoopNettyCustomizer implements NettyServerCustomizer {
	 
    @Override
    public HttpServer apply(HttpServer httpServer) {
    	ThreadPoolExecutor newCachedThreadPoolparentGroup = (ThreadPoolExecutor) Executors.newFixedThreadPool(4);
    	ThreadPoolExecutor newCachedThreadPoolchildGroup = (ThreadPoolExecutor) Executors.newFixedThreadPool(4);
    	NioEventLoopGroup parentGroup = new NioEventLoopGroup(4,newCachedThreadPoolparentGroup);
    	NioEventLoopGroup childGroup = new NioEventLoopGroup(4,newCachedThreadPoolchildGroup);
        MetricHandler childHandler = new MetricHandler();
        
     		new Thread() {
     			@Override
     			public void run() {
     				while (true) {
     					try {
     						Thread.sleep(1000);
     						System.out.println("bossexecutorCount:" + parentGroup.executorCount());
    						System.out.println("workerexecutorCount:" + childGroup.executorCount());
    						System.out.println("newCachedThreadPoolparentGroup待处理任务:" + newCachedThreadPoolparentGroup.getQueue().size());
    						System.out.println("newCachedThreadPoolchildGroup:待处理任务" + newCachedThreadPoolparentGroup.getQueue().size());
     						System.out.println("连接数:" + childHandler.totalConnectionNumber.get());
     					} catch (InterruptedException e) {
     						// TODO Auto-generated catch block
     						e.printStackTrace();
     					}
     				}
     			}
     		}.start();
        
        
     		
        return httpServer.tcpConfiguration(tcpServer -> tcpServer
          .bootstrap(serverBootstrap -> serverBootstrap
            .group(parentGroup, childGroup).childHandler(childHandler)
            .channel(NioServerSocketChannel.class)));
    }
}
