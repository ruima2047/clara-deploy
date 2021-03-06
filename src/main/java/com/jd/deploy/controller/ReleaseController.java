package com.jd.deploy.controller;

import com.jd.deploy.domain.BaseInfo;
import com.jd.deploy.domain.PathUtil;
import com.jd.deploy.domain.ReleaseFileInfo;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;

/**
 * Created by marui5 on 2016/8/17.
 */

@Controller
@RequestMapping("/release")
public class ReleaseController {
    @Resource
    private HashMap baseInfoMap;
    private List<ReleaseFileInfo> releaseFileInfoList;
    private List<String> zipInfoList;
    private SimpleDateFormat simpleDateFormat;
    private String versionFilePath;
    private String updateLogPath;
    private BaseInfo baseInfo;

    @RequestMapping(value = "/release", method = RequestMethod.POST)
    public void release(HttpServletRequest request, @RequestParam("warehouse[]") List<String> warehouseList, HttpServletResponse response) {
        try {
            simpleDateFormat = new SimpleDateFormat("yyyy/M/d H:mm:ss");
            for (String warehouse : warehouseList) {
                //initialize settings
                releaseFileInfoList = new ArrayList<ReleaseFileInfo>();
                zipInfoList = new ArrayList<String>();
                baseInfo = (BaseInfo) baseInfoMap.get(warehouse);
                //ex. E:\export\Update\gz\PC\Release
                File releaseFile = new File(PathUtil.getPath(baseInfo.getRootPath(), "Release"));
                updateLogPath = PathUtil.getPath(baseInfo.getRootPath(), "VersionInfo", "versionInfo");

                //get the new version number and update description
                File updateLogFile = new File(updateLogPath);
                if (!updateLogFile.exists()) {
                    throw new RuntimeException("读取更新内容失败");
                }
                BufferedReader bufferedReader = null;
                String versionNum = "";
                String updateLog = "";
                try {
                    bufferedReader = new BufferedReader(new FileReader(updateLogFile));
                    String lineTxt;
                    int i = 1;
                    if ((lineTxt = bufferedReader.readLine()) == null) {
                        throw new RuntimeException("获取版本号失败");
                    }
                    versionNum = lineTxt;
                    while ((lineTxt = bufferedReader.readLine()) != null) {
                        if (!StringUtils.isBlank(lineTxt)) {
                            updateLog += Integer.toString(i) + ". " + lineTxt + "\n";
                            i++;
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException("获取版本号失败");
                } finally {
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }

                //create an empty folder named version
//                versionFilePath = PathUtil.getPath(baseInfo.getRootPath(), versionNum);
//                File versionFile = new File(versionFilePath);
//                if (!versionFile.exists()) {
//                    if (!versionFile.mkdir())
//                        throw new RuntimeException(new Exception());
//                }

                //get all files' information
                generateFileList(releaseFile, "");

                //generate Release.xml
                generateReleaseList(versionNum, updateLog);

                //zip Release.xml and version folder
                zipIt(warehouse, versionNum);

                //update updateLog
                File appendVersion = new File(updateLogPath.concat("_").concat(versionNum));
                updateLogFile.renameTo(appendVersion);
                FileWriter fw = new FileWriter(updateLogPath);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(updateVersionNum(versionNum));
                bw.close();
                fw.close();

            }
            response.setStatus(200);
            PrintWriter pw = response.getWriter();
            pw.write("success");
            System.out.println("it's a test");
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
//            if(bw != null)
//                try{
//                    is.close();
//                }
//                catch(IOException e){
//                    throw new RuntimeException(e);
//                }
//            if(os != null)
//                try{
//                    os.close();
//                }
//                catch(IOException e){
//                    throw new RuntimeException(e);
//                }
        }
    }

    /**
     * @return
     */
    public Boolean generateReleaseList(String newVersionNum, String updateLog) {

        Document documentEle = DocumentHelper.createDocument();
        Element autoUpdaterEle = documentEle.addElement("AutoUpdater");
        Element appNameEle = autoUpdaterEle.addElement("AppName");
        appNameEle.addText(baseInfo.getAppName());
        Element releaseUrlEle = autoUpdaterEle.addElement("ReleaseURL");
        releaseUrlEle.addText(baseInfo.getReleaseURL());
        Element releaseDateEle = autoUpdaterEle.addElement("ReleaseDate");
        releaseDateEle.addText(simpleDateFormat.format(new Date()));
        Element releaseVersionEle = autoUpdaterEle.addElement("ReleaseVersion");
        releaseVersionEle.addText(newVersionNum);
        Element minVersionEle = autoUpdaterEle.addElement("MinVersion");
        minVersionEle.addText(newVersionNum);
        Element updateDesEle = autoUpdaterEle.addElement("UpdateDes");
        updateDesEle.addText(updateLog);
        Element applicationStartEle = autoUpdaterEle.addElement("ApplicationStart");
        applicationStartEle.addText(baseInfo.getApplicationStart());
        Element shortcutIconEle = autoUpdaterEle.addElement("ShortcutIcon");
        shortcutIconEle.addText(baseInfo.getShortcutIcon());
        Element releasesEle = autoUpdaterEle.addElement("Releases");
        for (ReleaseFileInfo releaseFileInfo : releaseFileInfoList) {
            Element fileEle = releasesEle.addElement("File");
            fileEle.addAttribute("name", releaseFileInfo.getName());
            fileEle.addAttribute("date", releaseFileInfo.getDate());
            fileEle.addAttribute("size", releaseFileInfo.getSize());
            fileEle.addAttribute("md5", releaseFileInfo.getMd5());
        }
        OutputFormat outputFormat = OutputFormat.createPrettyPrint();
        outputFormat.setEncoding("UTF-8");
        XMLWriter xmlWriter = null;
        try {
            xmlWriter = new XMLWriter(
                    new FileOutputStream(PathUtil.getPath(baseInfo.getRootPath(), "ReleaseList.xml")), outputFormat);

            xmlWriter.write(documentEle);
            xmlWriter.flush();
            xmlWriter.close();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (xmlWriter != null) {
                try {
                    xmlWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }


    /**
     * generate a file list under a given path
     *
     * @param folder
     * @return
     */
    public void generateFileList(File folder, String relativePath) {
        try {
            for (File file : folder.listFiles()) {
                if (file.isDirectory()) {
                    generateFileList(file, PathUtil.getRelativePath(relativePath, file.getName()));
                } else {
                    ReleaseFileInfo releaseFileInfo = new ReleaseFileInfo();
//                    releaseFileInfo.setName(PathUtil.separatorUniform(relativePath).concat(file.getName()));
                    releaseFileInfo.setName(relativePath.replace('/', '\\').concat(file.getName()));
                    releaseFileInfo.setZipInfo(relativePath.concat(file.getName()));
                    //linux文件系统时间精度问题
                    releaseFileInfo.setDate(simpleDateFormat.format(new Date(file.lastModified() - 2000)));
                    releaseFileInfo.setSize(Long.toString(file.length() >> 10));
                    releaseFileInfo.setMd5(getMd5ByFile(file));
                    releaseFileInfoList.add(releaseFileInfo);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * copy a file to a given directory,which may not exist
     *
     * @param file
     * @param path
     */
    public void copyFileToDirectory(File file, String path) {
        File desDirectory = new File(path);
        if (!desDirectory.exists()) {
            if (!desDirectory.mkdirs()) {
                throw new RuntimeException("创建文件夹失败");
            }
        }
        File destFile = new File(PathUtil.getPath(path, file.getName()));
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(file).getChannel();
            outputChannel = new FileOutputStream(destFile).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
            destFile.setLastModified(file.lastModified());
        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (inputChannel != null && outputChannel != null) {
                try {
                    inputChannel.close();
                    outputChannel.close();
                } catch (IOException io) {
                    io.printStackTrace();
                }
            }
        }

    }


    /**
     * get md5 of a file
     *
     * @param file
     * @return
     */
    public static String getMd5ByFile(File file) {
        String value = null;
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            value = DigestUtils.md5Hex(IOUtils.toByteArray(in)).toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return value;
    }

//    /**
//     * get md5 of a file
//     *
//     * @param file
//     * @return
//     */
//    public static String getMd5ByFile(File file) {
//        String value = null;
//        FileInputStream in = null;
//        try {
//            in = new FileInputStream(file);
//            FileChannel fc = in.getChannel();
//            MappedByteBuffer byteBuffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
//            MessageDigest md5 = MessageDigest.getInstance("MD5");
//            md5.update(byteBuffer);
//            BigInteger bi = new BigInteger(1, md5.digest());
//            value = String.format("%032X", bi);
//            fc.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if(in != null) {
//                try {
//                    in.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return value;
//    }

//    public Boolean packAndZip(File versionFile, File xmlFile) {
//        try {
//            ZipFile zipFile = new ZipFile(versionFilePath + ".zip");
//            zipFile.createZipFileFromFolder(versionFile, new ZipParameters(), false, 0);
//            zipFile.addFile(xmlFile, new ZipParameters());
//        } catch (Exception e) {
//
//        }
//        return true;
//    }

    /**
     * update properties file
     */
    public void updateProperties(File properties) {


    }

    public String updateVersionNumNew(String oldVersion) {
        long begin = System.nanoTime();
        String newVersion = "";
        String bits[] = oldVersion.split("\\.");
        String str = "";
        for (int j = 0; j < bits.length; j++) {
            str += bits[j];
        }
        String numFormat = Integer.toString(Integer.parseInt(str) + 1);
        for (int i = 0; i < numFormat.length(); i++) {
            int s = numFormat.codePointCount(0, 0);
            newVersion += numFormat.charAt(i) + ".";
        }
        return newVersion.substring(0, newVersion.length() - 1);
    }

    /**
     * update the version number
     * e.g:
     * updateVersionNum(0.1.2) will return 0.1.3
     * updateVersionNum(9.9.9) will return 1.0.0.0
     *
     * @param oldVersion
     * @return
     */
    public String updateVersionNum(String oldVersion) {
        String newVersion = "";
        if (oldVersion == null || !oldVersion.contains(".")) {
            throw new RuntimeException("the format of version number is wrong");
        }
        String bits[] = oldVersion.split("\\.");
        boolean allBitCarry = true;
        int i = bits.length - 1;
        while (i >= 0) {
            if (Integer.parseInt(bits[i]) == 9) {
                bits[i] = ".0";
                newVersion = bits[i--] + newVersion;
            } else {
                bits[i] = Integer.toString(Integer.parseInt(bits[i]) + 1);
                newVersion = "." + bits[i--] + newVersion;
                allBitCarry = false;
                break;
            }
        }
        if (i >= 0) {
            String s = oldVersion.substring(0, 2 * i);
            return oldVersion.substring(0, 2 * i + 1) + newVersion;
        } else if (allBitCarry) {
            return "1" + newVersion;
        } else {
            return newVersion.substring(1, newVersion.length());
        }
    }

    /**
     * Zip it
     */
    public void zipIt (String wareHouse, String versionNum) throws Exception{

        byte[] buffer = new byte[1024];

        try{

            FileOutputStream fos = new FileOutputStream(baseInfo.getRootPath()+ File.separator + wareHouse+versionNum+".zip");
            ZipOutputStream zos = new ZipOutputStream(fos);

            System.out.println("Output to Zip : " + versionNum);

            for(ReleaseFileInfo releaseFileInfo : this.releaseFileInfoList){

                ZipEntry ze= new ZipEntry(PathUtil.getPath(versionNum, releaseFileInfo.getZipInfo()));
                zos.putNextEntry(ze);
                FileInputStream in = new FileInputStream(baseInfo.getRootPath()+ File.separator + "Release" + File.separator + releaseFileInfo.getZipInfo());
                int len;
                while ((len = in.read(buffer)) > 0) {
                    zos.write(buffer, 0, len);
                }
                ze.setTime(simpleDateFormat.parse(releaseFileInfo.getDate()).getTime() + 2000);
                in.close();
            }
            ZipEntry ze= new ZipEntry("ReleaseList.xml");
            zos.putNextEntry(ze);
            FileInputStream in = new FileInputStream(baseInfo.getRootPath()+ File.separator + "ReleaseList.xml");
            int len;
            while ((len = in.read(buffer)) > 0) {
                zos.write(buffer, 0, len);
            }
            in.close();
            zos.closeEntry();
            //remember close it
            zos.close();
            fos.close();
            System.out.println("Done");
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
}
