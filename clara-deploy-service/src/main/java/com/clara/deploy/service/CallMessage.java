package com.clara.deploy.service;


import com.clara.deploy.domain.Message;

/**
 * Created with IntelliJ IDEA.
 * User: marui5
 * Date: 16-5-23
 * Time: 下午3:13
 * To change this template use File | Settings | File Templates.
 */
public class CallMessage {
    public String show() {
        System.out.println(Message.msg);
        return Message.msg;

    }
}
