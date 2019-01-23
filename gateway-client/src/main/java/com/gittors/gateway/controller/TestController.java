package com.gittors.gateway.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author zlliu
 * @date 2019/1/23 15:14
 */
@Controller
//@RequestMapping("/gateway")
public class TestController {

    @GetMapping("/test")
    @ResponseBody
    public String test() {
        return "success";
    }
}
