package com.jd.deploy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created with IntelliJ IDEA.
 * User: marui5
 * Date: 16-8-6
 * Time: 下午6:23
 * To change this template use File | Settings | File Templates.
 */

@Controller
@RequestMapping("/")
public class DefaultController {
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(HttpServletRequest request,HttpServletResponse response) {
        System.out.println("hello");
        return "/common/test";
    }
}