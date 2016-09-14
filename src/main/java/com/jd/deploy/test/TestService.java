package com.jd.deploy.test;

import javax.annotation.Resource;

/**
 * Created by marui5 on 2016/9/14.
 */
public class TestService {
    @Resource
    TestDao testDao;

    public void servicePrint() {
        testDao.daoPrint();
        System.out.println("TestService.");
    }
}
