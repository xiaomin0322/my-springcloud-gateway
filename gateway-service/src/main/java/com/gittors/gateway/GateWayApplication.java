package com.gittors.gateway;

import java.lang.reflect.Field;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.embedded.netty.NettyServerCustomizer;
import org.springframework.context.annotation.Bean;

import com.gittors.gateway.config.MetricHandler;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import reactor.netty.http.server.HttpServer;

/**
 * https://www.baeldung.com/spring-boot-reactor-netty
 * 
 * https://s0projectreactor0io.icopy.site/docs/netty/release/reference/index.html#_metrics_3
 * 
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
		ThreadPoolExecutor boss = (ThreadPoolExecutor) Executors.newFixedThreadPool(4);
		// ThreadPoolExecutor work = (ThreadPoolExecutor)
		// Executors.newFixedThreadPool(50);
		// ThreadPoolExecutor work = (ThreadPoolExecutor)
		// Executors.newCachedThreadPool();

		//连接数 受限于work线程数，如果设置work最大线程数10，那么最多连接是10
		ThreadPoolExecutor work = new ThreadPoolExecutor(5, 10, 5L, TimeUnit.SECONDS,
				// new LinkedBlockingQueue<Runnable>()
				new ArrayBlockingQueue<>(5));
		
		System.out.println("work class = "+work);

		// int nThreads, Executor executor；nThreads > executor.nThreads

		NioEventLoopGroup parentGroup = new NioEventLoopGroup(4, boss);
		//io.netty.util.concurrent.ThreadPerTaskExecutor 不指定连接池，默认是新启动一个线程执行任务
		NioEventLoopGroup childGroup = new NioEventLoopGroup(4);
		

		EventLoop eventLoop = childGroup.next();

		//final io.netty.util.concurrent.ThreadPerTaskExecutor workExecutor = (io.netty.util.concurrent.ThreadPerTaskExecutor)getExecutor(eventLoop);
		//当显示得指定了work线程返回得是ThreadPoolExecutor  ：不指定则返回得io.netty.util.concurrent.ThreadPerTaskExecutor
		//final java.util.concurrent.ThreadPoolExecutor workExecutor = (java.util.concurrent.ThreadPoolExecutor)getExecutor(eventLoop);
	//	System.out.println("反射拿得  work class = "+workExecutor);
		
		MetricHandler childHandler = new MetricHandler();

		new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(1000);
						System.out.println("boss:总线程数" + parentGroup.executorCount());
						System.out.println("work:总线程数" + childGroup.executorCount());
						System.out.println("boss待处理任务:" + boss.getQueue().size());
						System.out.println("work:待处理任务" + work.getQueue().size());

						System.out.println("boss当前活跃线程数:" + boss.getActiveCount());
						System.out.println("work:当前活跃线程数" + work.getActiveCount());

						System.out.println("boss最高线程数:" + boss.getLargestPoolSize());
						System.out.println("work:最高线程数" + work.getLargestPoolSize());

						System.out.println("连接数:" + childHandler.totalConnectionNumber.get());


						System.out.println("====================================================:");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();

		return httpServer.metrics(true).tcpConfiguration(tcpServer -> tcpServer.bootstrap(serverBootstrap -> serverBootstrap
				.group(parentGroup, childGroup).childHandler(childHandler).channel(NioServerSocketChannel.class)));
	}

	public static Object getExecutor(EventLoop eventLoop) {
		Object executor = null;
		try {
			Field declaredField = getClassField(eventLoop.getClass(), "executor");
			declaredField.setAccessible(true);
			executor =  declaredField.get(eventLoop);
			Field[] declaredFields = executor.getClass().getDeclaredFields();
			for(Field f:declaredFields) {
				System.out.println("f=="+f.getName());
			}
			declaredField = executor.getClass().getDeclaredField("val$executor");
			declaredField.setAccessible(true);
			executor = declaredField.get(executor);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return executor;
	}

	public static Field getClassField(Class<?> clazz, String fieldName) {
		Field declaredField = null;
		try {
			declaredField = clazz.getDeclaredField(fieldName);
		} catch (Exception e) {
		}
		if (declaredField != null) {
			declaredField.setAccessible(true);
			System.out.println("calss===="+clazz.getName());
			return declaredField;
		}
		if (clazz.getSuperclass() != null) {
			return getClassField(clazz.getSuperclass(), fieldName);
		}
		return null;
	}
}
