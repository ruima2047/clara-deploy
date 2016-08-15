package com.jd.deploy.controller;

import com.jd.deploy.domain.BaseInfo;
import com.jd.deploy.domain.PathUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 *
 * Created by marui5 on 2016/8/15.
 */

@Controller
@RequestMapping("/upload")
public class UploadController {
    private HashMap baseInfoMap;
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String upload(@RequestParam("module") String module) {

        System.out.println(module);
//        boolean fileExits = false;
//        response.setContentType("text/html");
//
//
//        DiskFileItemFactory factory = new DiskFileItemFactory();
//        ServletFileUpload upload = new ServletFileUpload(factory);
//        try {
//            // Parse the request to get file items.
//            List fileItems = upload.parseRequest(request);
//            // Process the uploaded file items
//            Iterator i = fileItems.iterator();
//            List<FileItem> fileItemList = new ArrayList<FileItem>();
//            List<String> warehouseList = new ArrayList<String>();
//            String moduleName = null;
//            String userName = null;
//            String description = null;
//            while (i.hasNext()) {
//                FileItem fileItem = (FileItem) i.next();
//                if (fileItem.isFormField() && fileItem.getFieldName().equals("moduleName")) {
//                    moduleName = fileItem.getString();
//                } else if(fileItem.isFormField() && fileItem.getFieldName().equals("userName")) {
//                    userName = fileItem.getString();
//                }else if(fileItem.isFormField() && fileItem.getFieldName().equals("description")) {
//                    description = fileItem.getString();
//                }else if (fileItem.isFormField() && fileItem.getFieldName().equals("warehouse")) {
//                    warehouseList.add(fileItem.getString());
//
//                } else if (!fileItem.isFormField()) {
//                    fileExits = true;
//                    fileItemList.add(fileItem);
//                }
//            }
//            if(moduleName == null) {
//                throw new RuntimeException("moduleName is null!");
//            }
//            if(warehouseList.size() == 0) {
//                throw new RuntimeException("warehouse is null!");
//            }
//            List<String> filePathList = new ArrayList<String>();
//
//            for(String warehouse : warehouseList) {
//                BaseInfo baseInfo = (BaseInfo)baseInfoMap.get(warehouse);
//                String uploadPath = PathUtil.getPath(baseInfo.getRootPath(),"Release","DLL",moduleName);
//                filePathList.add(uploadPath);
//                for(FileItem fileItem : fileItemList) {
//                    File file = new File(PathUtil.getPath(uploadPath,fileItem.getName()));
//                    OutputStream os = new FileOutputStream(file);
//                    InputStream is = fileItem.getInputStream();
//                    byte buf[] = new byte[1024];
//                    while(is.read(buf) > 0) {
//                        os.write(buf,0,buf.length);
//                    }
//                    os.flush();
//                    os.close();
//                    is.close();
//                }
//            }
//            if (!fileExits) {
//                throw new RuntimeException("you need to choose at least one file");
//            }
//
//            PrintWriter writer = response.getWriter();
//            writer.print("true");
//            writer.flush();
//            writer.close();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        } finally {
//
//        }
        return "String";
    }

    public void setBaseInfoMap(HashMap baseInfoMap) {
        this.baseInfoMap = baseInfoMap;
    }
}
