package com.jd.deploy.controller;

import com.jd.deploy.domain.BaseInfo;
import com.jd.deploy.domain.PathUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.List;

/**
 * 记录更新内容
 * Created by marui5 on 2016/8/15.
 */

@Controller
@RequestMapping("/log")
public class LogController {
    @Resource
    private HashMap baseInfoMap;

    @RequestMapping(value = "/log", method = RequestMethod.POST)
    public void logDescription(@RequestParam("description") String description,
                               @RequestParam("warehouse[]") List<String> warehouseList,
                               HttpServletRequest request) {
        System.out.println(description);
        OutputStream os = null;
        try {
            for (String warehouse : warehouseList) {
                BaseInfo baseInfo = (BaseInfo) baseInfoMap.get(warehouse);

                //ex. E:\export\Update\guan\PC\VersionInfo\versionInfo
                String versionInfoFile = PathUtil.getPath(baseInfo.getRootPath(), "VersionInfo", "versionInfo");
                os = new FileOutputStream(versionInfoFile, true);
                os.write("\r\n".getBytes());
                os.write(description.getBytes());
                os.flush();
                os.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if(os != null)
                try{
                    os.close();
                }
                catch(IOException e){

                }
        }


    }
}
