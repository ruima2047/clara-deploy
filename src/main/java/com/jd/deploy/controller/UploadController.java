package com.jd.deploy.controller;

import com.jd.deploy.domain.BaseInfo;
import com.jd.deploy.domain.PathUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by marui5 on 2016/8/15.
 */

@Controller
@RequestMapping("/upload")
public class UploadController {
    private final Logger logger = Logger.getLogger(getClass());
    @Resource
    private HashMap baseInfoMap;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public void upload(HttpServletRequest request,
                         @RequestParam("userName") String userName,
                         @RequestParam("module") String module,
                         @RequestParam("warehouse") List<String> warehouseList,
                         @RequestParam("Filedata") MultipartFile file) {
        OutputStream os = null;
        InputStream is = null;
        try {
            for (String warehouse : warehouseList) {
                BaseInfo baseInfo = (BaseInfo) baseInfoMap.get(warehouse);

                //ex. E:\export\Update\guan\PC\Release\DLL\Check
                String uploadPath = PathUtil.getPath(baseInfo.getRootPath(), "Release", "DLL", module);
                //ex. E:\export\Update\guan\PC\Release\DLL\Check\JD.WMS3.PC.Common.Check.dll
                File destFile = new File(PathUtil.getPath(uploadPath, file.getOriginalFilename()));
                os = new FileOutputStream(destFile);
                is = file.getInputStream();
                byte buf[] = new byte[1024];
                while (is.read(buf) > 0) {
                    os.write(buf, 0, buf.length);
                }
                os.flush();
                os.close();
                is.close();
                logger.info("库房："+warehouse+" 用户："+userName+" 文件："+file.getOriginalFilename()+" 路径："+uploadPath);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if(is != null)
                try{
                    is.close();
                }
                catch(IOException e){

                }
            if(os != null)
                try{
                    os.close();
                }
                catch(IOException e){

                }
        }
    }
}
