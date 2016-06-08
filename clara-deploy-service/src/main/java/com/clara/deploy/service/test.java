package com.clara.deploy.service;

/**
 * Created with IntelliJ IDEA.
 * User: marui5
 * Date: 16-5-25
 * Time: 上午10:48
 * To change this template use File | Settings | File Templates.
 */
public class test {
    public static void main(String []args) {
        ReleaseService releaseService = new ReleaseService();
        //releaseService.generateFileList("E:\\A上线\\广州\\PC线上版本\\JD.WMS3.Main\\JD.WMS3.Main.WFApp\\bin\\0.4.1.8");
        releaseService.generateReleaseList("1.0","更新内容:\n1.离线发票更新","E:\\A上线\\广州\\PC线上版本\\JD.WMS3.Main\\JD.WMS3.Main.WFApp\\bin\\Release");
        System.out.println("hi");
    }
}
