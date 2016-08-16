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
import org.springframework.web.multipart.MultipartFile;

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
    public String upload(HttpServletRequest request,
                         @RequestParam("userName") String userName,
                         @RequestParam("module") String module,
                         @RequestParam("warehouse") List<String> warehouseList,
                         @RequestParam("Filename") String fileName,
                         @RequestParam("Filedata")  MultipartFile file) {

        System.out.println(module);
        try {
            List<String> filePathList = new ArrayList<String>();
            for(String warehouse : warehouseList) {
                BaseInfo baseInfo = (BaseInfo)baseInfoMap.get(warehouse);
                String uploadPath = PathUtil.getPath(baseInfo.getRootPath(),"Release","DLL",module);
                filePathList.add(uploadPath);
                    File destFile = new File(PathUtil.getPath(uploadPath,file.getName()));
                    OutputStream os = new FileOutputStream(destFile);
                    InputStream is = file.getInputStream();
                    byte buf[] = new byte[1024];
                    while(is.read(buf) > 0) {
                        os.write(buf,0,buf.length);
                    }
                    os.flush();
                    os.close();
                    is.close();
            }
//            if (!fileExits) {
//                throw new RuntimeException("you need to choose at least one file");
//            }
//
//            PrintWriter writer = response.getWriter();
//            writer.print("true");
//            writer.flush();
//            writer.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {

        }
        return "String";
    }

    public void setBaseInfoMap(HashMap baseInfoMap) {
        this.baseInfoMap = baseInfoMap;
    }
}
