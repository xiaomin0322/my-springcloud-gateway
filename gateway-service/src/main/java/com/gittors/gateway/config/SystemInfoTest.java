package com.gittors.gateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;
import oshi.util.ExecutingCommand;
import oshi.util.platform.mac.SysctlUtil;

/**
 * Java系统监控测试类
 * 
 * @ClassName: SystemInfoTest
 * @Description:TODO(这里用一句话描述这个类的作用)
 * @author: 哒哒
 * @date: 2018年3月1日 下午5:33:51
 * 
 * @Copyright: 2018 www.sundablog.com Inc. All rights reserved.
 */
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
		
	//	SysctlUtil.sysctl("net.inet.tcp.stats", tcpstat);
		ExecutingCommand.runNative("");
		
	}
}