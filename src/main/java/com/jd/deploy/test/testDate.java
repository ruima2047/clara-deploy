//package com.jd.deploy.test;
//
//import java.io.File;
//import java.net.SocketPermission;
//import java.text.SimpleDateFormat;
//
//import org.apache.commons.io.IOUtils;
//import org.apache.commons.net.ftp.FTPClient;
//import org.apache.log4j.Logger;
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.support.AbstractApplicationContext;
//import org.springframework.context.support.ClassPathXmlApplicationContext;
//import org.springframework.web.context.support.XmlWebApplicationContext;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.FileOutputStream;
//import java.util.HashMap;
//import java.util.Map;
//
//import static javafx.scene.input.KeyCode.K;
//import static javafx.scene.input.KeyCode.V;
//
//
///**
// * Created by marui5 on 2016/8/29.
// */
//public class testDate {
////    public static void main(String []args) {
//////        File file = new File("/export/Update/gz/PC/Release/DLL/CenterPrint/JD.WMS3.CPrint.WFApp.dll.config");
//////        long dateLong = file.lastModified();
//////        String dateString = new SimpleDateFormat("yyyy/M/d H:mm:ss").format(dateLong);
//////        System.out.println(dateLong);
//////        System.out.println(dateString);
////        testUpload();
////        public static void main(String[] args) {
////            System.out.println(System.nanoTime());
////            System.out.println(System.nanoTime());
////            System.out.println(System.nanoTime());
////        }
////    }
//
////    public static void main(String[] args) {
////        byte buf[] = new byte[2];
////        byte a = 'a';
////        byte b ='b';
////        buf[0] = a;
////        buf[1] = b;
////        buf = "a".getBytes();
////        System.out.println(buf);
////    }
//
//    final static Logger logger = Logger.getLogger(testDate.class);
//
//    public static void main(String[] args) {
////        ApplicationContext context = new XmlWebApplicationContext("spring-config.xml");
////        ApplicationContext ctx = new ClassPathXmlApplicationContext("E:\\Github\\workespace\\clara-deploy\\src\\main\\java\\com\\jd\\deploy\\test\\spring-config.xml");
////        TestService testService= ctx.getBean("TestService",TestService.class);
////        testService.servicePrint();
//
////        logger.debug("debug");
////        logger.info("info");
////        logger.error("error");
//        Map[] maps = new Map[2];
//        Map a = new HashMap(5);
//        a.put("1","a");
//        maps[0] = a;
//        System.out.println(a);
//        System.out.println(maps[0]);
//        Map b = new HashMap(5);
//        b.put("2","b");
//        a = b;
//        System.out.println(a);
//        System.out.println(maps[0]);
//
//    }
//    static class Entry<K,V> implements Map.Entry<K,V> {
//        final K key;
//        V value;
//        Entry<K,V> next;
//        final int hash;
//
//        /**
//         * Creates new entry.
//         */
//        Entry(int h, K k, V v, Entry<K,V> n) {
//            value = v;
//            next = n;
//            key = k;
//            hash = h;
//        }
//
//        public final K getKey() {
//            return key;
//        }
//
//        public final V getValue() {
//            return value;
//        }
//
//        public final V setValue(V newValue) {
//            V oldValue = value;
//            value = newValue;
//            return oldValue;
//        }
//
//        public final boolean equals(Object o) {
//            if (!(o instanceof Map.Entry))
//                return false;
//            Map.Entry e = (Map.Entry)o;
//            Object k1 = getKey();
//            Object k2 = e.getKey();
//            if (k1 == k2 || (k1 != null && k1.equals(k2))) {
//                Object v1 = getValue();
//                Object v2 = e.getValue();
//                if (v1 == v2 || (v1 != null && v1.equals(v2)))
//                    return true;
//            }
//            return false;
//        }
//
//        public final int hashCode() {
//            return (key==null   ? 0 : key.hashCode()) ^
//                    (value==null ? 0 : value.hashCode());
//        }
//
//        public final String toString() {
//            return getKey() + "=" + getValue();
//        }
//
//        /**
//         * This method is invoked whenever the value in an entry is
//         * overwritten by an invocation of put(k,v) for a key k that's already
//         * in the HashMap.
//         */
//        void recordAccess(HashMap<K,V> m) {
//        }
//
//        /**
//         * This method is invoked whenever the entry is
//         * removed from the table.
//         */
//        void recordRemoval(HashMap<K,V> m) {
//        }
//    }
//
//
//    public static void testUpload() {
//        FTPClient ftpClient = new FTPClient();
//        FileInputStream fis = null;
//
//        try {
//            ftpClient.connect("10.79.160.40");
//            ftpClient.login("y1test", "marui@123");
//
//            File srcFile = new File("C:\\Users\\marui5\\Documents\\dll-check\\JD.WMS3.PC.WSClient.dll");
//            fis = new FileInputStream(srcFile);
//            //设置上传目录
//            ftpClient.changeWorkingDirectory("/home/y1test");
//            ftpClient.setBufferSize(1024);
//            ftpClient.setControlEncoding("GBK");
//            //设置文件类型（二进制）
//            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
//            ftpClient.storeFile("3.gif", fis);
//        } catch (IOException e) {
//            e.printStackTrace();
//            throw new RuntimeException("FTP客户端出错！", e);
//        } finally {
//            IOUtils.closeQuietly(fis);
//            try {
//                ftpClient.disconnect();
//            } catch (IOException e) {
//                e.printStackTrace();
//                throw new RuntimeException("关闭FTP连接发生异常！", e);
//            }
//        }
//    }
//
//}
