package com.jd.deploy.test;

import java.io.File;
import java.net.SocketPermission;
import java.text.SimpleDateFormat;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by marui5 on 2016/8/29.
 */
public class testDate {
//    public static void main(String []args) {
////        File file = new File("/export/Update/gz/PC/Release/DLL/CenterPrint/JD.WMS3.CPrint.WFApp.dll.config");
////        long dateLong = file.lastModified();
////        String dateString = new SimpleDateFormat("yyyy/M/d H:mm:ss").format(dateLong);
////        System.out.println(dateLong);
////        System.out.println(dateString);
//        testUpload();
//        public static void main(String[] args) {
//            System.out.println(System.nanoTime());
//            System.out.println(System.nanoTime());
//            System.out.println(System.nanoTime());
//        }
//    }

//    public static void main(String[] args) {
//        byte buf[] = new byte[2];
//        byte a = 'a';
//        byte b ='b';
//        buf[0] = a;
//        buf[1] = b;
//        buf = "a".getBytes();
//        System.out.println(buf);
//    }


    public static void main(String[] args) {
//        ApplicationContext context = new XmlWebApplicationContext("spring-config.xml");
        ApplicationContext ctx = new ClassPathXmlApplicationContext("E:\\Github\\workespace\\clara-deploy\\src\\main\\java\\com\\jd\\deploy\\test\\spring-config.xml");
        TestService testService= ctx.getBean("TestService",TestService.class);
        testService.servicePrint();

    }
    public static void testUpload() {
        FTPClient ftpClient = new FTPClient();
        FileInputStream fis = null;

        try {
            ftpClient.connect("10.79.160.40");
            ftpClient.login("y1test", "marui@123");

            File srcFile = new File("C:\\Users\\marui5\\Documents\\dll-check\\JD.WMS3.PC.WSClient.dll");
            fis = new FileInputStream(srcFile);
            //设置上传目录
            ftpClient.changeWorkingDirectory("/home/y1test");
            ftpClient.setBufferSize(1024);
            ftpClient.setControlEncoding("GBK");
            //设置文件类型（二进制）
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.storeFile("3.gif", fis);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("FTP客户端出错！", e);
        } finally {
            IOUtils.closeQuietly(fis);
            try {
                ftpClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("关闭FTP连接发生异常！", e);
            }
        }
    }

}
