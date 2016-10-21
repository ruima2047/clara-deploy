package com.jd.deploy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
public class DefaultController {
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(HttpServletRequest request, Model view) {
        //String username = com.jd.common.web.LoginContext.getLoginContext().getNick();
        System.out.println("hello");

        //view.addAttribute("username", username);
        return "common/default";
    }

    @RequestMapping(value = "logout", method = RequestMethod.POST)
    @ResponseBody
    public String logout(HttpServletRequest request, HttpServletResponse response) {
//        return "http://test.ssa.jd.com/sso/logout?ReturnUrl=http://clara.deploy.jd.net";
//        try {
//            PrintWriter pw = response.getWriter();
//            pw.write("http://test.ssa.jd.com/sso/logout?ReturnUrl=http://clara.deploy.jd.net");
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
        return "http://test.ssa.jd.com/sso/logout?ReturnUrl=http://clara.deploy.jd.net";
    }
}
