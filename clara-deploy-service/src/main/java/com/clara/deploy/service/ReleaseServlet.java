package com.clara.deploy.service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: marui5
 * Date: 16-6-12
 * Time: 下午1:56
 * To change this template use File | Settings | File Templates.
 */
public class ReleaseServlet extends HttpServlet {
    public void init() throws ServletException {

    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response)
            throws ServletException, IOException {
        ReleaseService releaseService = new ReleaseService();
        releaseService.build();
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws ServletException, IOException {

    }
}