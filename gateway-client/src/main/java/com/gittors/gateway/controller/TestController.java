package com.gittors.gateway.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author zlliu
 * @date 2019/1/23 15:14
 */
@Slf4j
@Controller
public class TestController {

    @GetMapping("/test")
    @ResponseBody
    public String test() {
    	// log.info("test success");
    	try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return "success";
    }

    @GetMapping("/gateway")
    @ResponseBody
    public String test2() {
    	// log.info("test success");
    	try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        //log.info("gateway success");
        return "gateway success";
    }

    @GetMapping("/test/gateway")
    @ResponseBody
    public String test3() {
    	// log.info("test gateway success");
        return "test gateway success";
    }
}
