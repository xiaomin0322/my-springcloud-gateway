/*package com.gittors.gateway.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yammer.metrics.Metrics;
import com.yammer.metrics.core.Gauge;
import com.yammer.metrics.core.Meter;
import com.yammer.metrics.core.Metric;
import com.yammer.metrics.core.MetricName;
import com.yammer.metrics.core.MetricsRegistry;

import io.netty.monitor.yammer.YammerMonitorRegistry;
import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;
import oshi.util.ExecutingCommand;

*//**
 * Java系统监控测试类
 * 
 * @ClassName: SystemInfoTest
 * @Description:TODO(这里用一句话描述这个类的作用)
 * @author: 哒哒
 * @date: 2018年3月1日 下午5:33:51
 * 
 * @Copyright: 2018 www.sundablog.com Inc. All rights reserved.
 *//*
public class SystemInfoTest {

	private static void printProcesses2(OperatingSystem os, GlobalMemory memory) {
		System.out.println("Processes: " + os.getProcessCount() + ", Threads: " + os.getThreadCount());
		// Sort by highest CPU
		OSProcess osProcess = os.getProcess(27876);

		System.out.println("getThreadCount==" + osProcess.getThreadCount());
		System.out.println("getOpenFiles==" + osProcess.getOpenFiles());

		System.out.println("==============================================");
		osProcess = os.getProcess(6728);
		System.out.println("getThreadCount==" + osProcess.getThreadCount());
		System.out.println("getOpenFiles==" + osProcess.getOpenFiles());

	}

	public static void main(String[] args) {
		Logger LOG = LoggerFactory.getLogger(SystemInfoTest.class);
		LOG.info("Initializing System...");
		SystemInfo si = new SystemInfo();
		HardwareAbstractionLayer hal = si.getHardware();

		OperatingSystem os = si.getOperatingSystem();
		System.out.println("================================");
		printProcesses2(os, hal.getMemory());

		// SysctlUtil.sysctl("net.inet.tcp.stats", tcpstat);
		ExecutingCommand.runNative("");

	}
	
	static {
		new Thread() {
			@Override
			public void run() {
				while(true) {
					try {
						Thread.sleep(1000);
						getStats();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	public static Map<String, Object> getStats() {
		System.out.println("=================getStats===============================");
		Map<String, Object> stats = new HashMap<String, Object>();
		MetricsRegistry registry = Metrics.defaultRegistry();
		
		
		YammerMonitorRegistry	registry2= new YammerMonitorRegistry();
		
		
		
		System.out.println("registry.allMetrics()=="+registry.allMetrics());
		
		System.out.println("registry.allMetrics2()=="+registry.groupedMetrics());
		
		
		for (Entry<MetricName, Metric> e : registry.allMetrics().entrySet()) {
			MetricName name = e.getKey();
			System.out.println("name====="+name);
			Metric metric = e.getValue();
			if (metric instanceof Meter) {
				Meter m = (Meter) metric;
				stats.put(name.toString(), m);
			} else if (metric instanceof Gauge) {
				Gauge<?> g = (Gauge<?>) metric;
				stats.put(name.toString(), g.value());
			}
		}
		return stats;
	}
	
}*/