package com.clara.deploy.service;


import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: marui5
 * Date: 16-5-24
 * Time: 上午11:31
 * To change this template use File | Settings | File Templates.
 */
public class UploadService extends HttpServlet {
    private ReleaseService releaseService;
    public void init() throws ServletException {

    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response)
            throws ServletException, IOException {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-config.xml");
        releaseService = ctx.getBean("releaseService",ReleaseService.class);
//        String s= PropertiesHolder.get("test");
       releaseService.build();
//        System.out.println(s);
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
        throws ServletException, IOException {
        System.out.println("<h1>UploadService</h1>");

    }


    public void setReleaseService(ReleaseService releaseService) {
        this.releaseService = releaseService;
    }
}
