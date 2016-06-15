package com.clara.deploy.service;


import java.io.*;
import java.util.*;

import com.clara.deploy.domain.BaseInfo;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created with IntelliJ IDEA.
 * User: marui5
 * Date: 16-5-24
 * Time: 上午11:31
 * To change this template use File | Settings | File Templates.
 */
public class UploadServlet extends HttpServlet {
    private final Logger logger = Logger.getLogger(getClass());
    private String releasePath;
    private HashMap baseInfoMap;

    public void init() throws ServletException {
        // Get the file location where it would be stored.
        logger.info("created for test by marui");

        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-config.xml");
        baseInfoMap = ctx.getBean("baseInfoMap", HashMap.class);
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response)
            throws ServletException, IOException {

        File file;
        // sum of path number
        int pathCount = 0;
        // file exits
        boolean fileExits = false;
        //check the enctype
        response.setContentType("text/html");

        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        try {
            // Parse the request to get file items.
            List fileItems = upload.parseRequest(request);
            // Process the uploaded file items
            Iterator i = fileItems.iterator();
            List<String> filePathList = new ArrayList<String>();
            String moduleName = null;
            while (i.hasNext()) {
                FileItem fileItem = (FileItem) i.next();
                String testr = fileItem.getFieldName();
                if (fileItem.isFormField() && fileItem.getFieldName().equals("moduleName")) {
                    moduleName = fileItem.getFieldName();
                } else if (fileItem.isFormField() && fileItem.getFieldName().equals("warehouse")) {
                    if(moduleName == null || moduleName == "") {
                        throw new RuntimeException("failed to load module name!");
                    }
                    String st = fileItem.getFieldName();
                    BaseInfo baseInfo = (BaseInfo)baseInfoMap.get(fileItem.getFieldName());
                    if(baseInfo == null) {
                        throw new RuntimeException("failed to load baseInfo!");
                    }
                    String uploadPath = baseInfo.getReleasePath() + "\\Release\\" +moduleName;
                    filePathList.add(uploadPath);

                } else if (!fileItem.isFormField()) {
                    fileExits = true;
                    // Get the uploaded file parameters
                    String fileName = fileItem.getName();
                    long sizeInBytes = fileItem.getSize();
                    logger.info("write file " + fileName + " size " + sizeInBytes);
                    for (int k = 0; k < pathCount; k++) {
                        String strp = filePathList.get(k) +"\\" + fileName;
                        file = new File(filePathList.get(k) +
                                fileName.substring(fileName.lastIndexOf("\\") + 1));
                        OutputStream os = new FileOutputStream(file);
                        InputStream is = fileItem.getInputStream();
                        byte buf[] = new byte[1024];
                        int length = 0;
                        while ((length = is.read(buf)) > 0) {
                            os.write(buf, 0, length);
                        }
                        os.flush();
                        os.close();
                        is.close();
                    }
                }
            }
            if (!fileExits) {
                throw new RuntimeException("you need to choose at least one file");
            }

            PrintWriter writer = response.getWriter();
            writer.print("{");
            writer.print("msg:\"文件大小: ,文件名:" + "\"");
            writer.print(",picUrl:\"" + "http" + "\"");
            writer.print("}");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws ServletException, IOException {

    }
}
