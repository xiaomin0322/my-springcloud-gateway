package com.gittors.gateway;

import java.lang.reflect.Field;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
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
import io.netty.util.internal.ThreadExecutorMap;
import reactor.netty.http.server.HttpServer;

/**
 * https://www.baeldung.com/spring-boot-reactor-netty
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

		ThreadPoolExecutor work = new ThreadPoolExecutor(5, 5, 5L, TimeUnit.SECONDS,
				// new LinkedBlockingQueue<Runnable>()
				new ArrayBlockingQueue<>(5));

		// int nThreads, Executor executor；nThreads > executor.nThreads

		NioEventLoopGroup parentGroup = new NioEventLoopGroup(4, boss);
		NioEventLoopGroup childGroup = new NioEventLoopGroup(10,work);

		EventLoop eventLoop = childGroup.next();

		final io.netty.util.concurrent.ThreadPerTaskExecutor workExecutor = (io.netty.util.concurrent.ThreadPerTaskExecutor)getExecutor(eventLoop);
		
		
		
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

		return httpServer.tcpConfiguration(tcpServer -> tcpServer.bootstrap(serverBootstrap -> serverBootstrap
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
