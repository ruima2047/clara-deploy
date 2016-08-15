package com.jd.deploy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 记录更新内容，与更新记录
 * Created by marui5 on 2016/8/15.
 */

@Controller
@RequestMapping("/log")
public class LogController {
    @RequestMapping(value = "/log", method = RequestMethod.GET)
    public String home(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("hello");
        return "/common/test";
    }
}
