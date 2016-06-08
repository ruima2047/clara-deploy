package com.clara.deploy.service;

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


    public void init() throws ServletException {

    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
        throws ServletException, IOException {
        System.out.println("<h1>UploadService</h1>");

    }

}
