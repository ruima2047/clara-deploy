package com.clara.deploy.service;

import com.clara.deploy.domain.BaseInfo;
import com.clara.deploy.domain.PropertiesHolder;
import com.clara.deploy.domain.ReleaseFileInfo;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.core.ZipFile;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.*;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: marui5
 * Date: 16-5-24
 * Time: 下午2:50
 * To change this template use File | Settings | File Templates.
 */
public class ReleaseService {

    private BaseInfo baseInfo;
    private List<ReleaseFileInfo> releaseFileInfoList;
    private SimpleDateFormat simpleDateFormat;
    private File releaseFile;
    private File versionFile;
    private String versionFilePath;

    public Boolean build () {
        try {
            String s= PropertiesHolder.get("test");
            ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-config.xml");
            baseInfo = ctx.getBean("baseInfo",BaseInfo.class);

            //initialize settings
            releaseFileInfoList = new ArrayList<ReleaseFileInfo>();
            simpleDateFormat = new SimpleDateFormat("yyyy/M/d H:mm:ss");
            releaseFile = new File(baseInfo.getReleasePath()+"\\Release");
            versionFilePath = baseInfo.getReleasePath()+"\\"+baseInfo.getMinVersion();
            //create an empty folder named version
            versionFile = new File(versionFilePath);
            if(!versionFile.exists()) {
                if(!versionFile.mkdir())
                    throw new RuntimeException(new Exception());
            }

            //copy files to version folder and get all files' information
            generateFileList(releaseFile, "");

            //generate Release.xml
            generateReleaseList();

            //zip Release.xml and version folder
            packAndZip();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return true;
    }

    /**
     *
     * @return
     */
    public Boolean generateReleaseList () {

        try {
            Document documentEle = DocumentHelper.createDocument();
            Element autoUpdaterEle = documentEle.addElement("AutoUpdater");
            Element appNameEle = autoUpdaterEle.addElement("AppName");
            appNameEle.addText(baseInfo.getAppName());
            Element releaseUrlEle = autoUpdaterEle.addElement("ReleaseURL");
            releaseUrlEle.addText(baseInfo.getReleaseURL());
            Element releaseDateEle = autoUpdaterEle.addElement("ReleaseDate");
            releaseDateEle.addText(simpleDateFormat.format(new Date()));
            Element releaseVersionEle = autoUpdaterEle.addElement("ReleaseVersion");
            releaseVersionEle.addText(baseInfo.getMinVersion());
            Element minVersionEle = autoUpdaterEle.addElement("MinVersion");
            minVersionEle.addText(baseInfo.getMinVersion());
            Element updateDesEle = autoUpdaterEle.addElement("UpdateDes");
            updateDesEle.addText(baseInfo.getUpdateDes());
            Element applicationStartEle = autoUpdaterEle.addElement("ApplicationStart");
            applicationStartEle.addText(baseInfo.getApplicationStart());
            Element shortcutIconEle = autoUpdaterEle.addElement("ShortcutIcon");
            shortcutIconEle.addText(baseInfo.getShortcutIcon());
            Element releasesEle = autoUpdaterEle.addElement("Releases");
            for(ReleaseFileInfo releaseFileInfo : releaseFileInfoList) {
                Element fileEle = releasesEle.addElement("File");
                fileEle.addAttribute("name", releaseFileInfo.getName());
                fileEle.addAttribute("date", releaseFileInfo.getDate());
                fileEle.addAttribute("size", releaseFileInfo.getSize());
                fileEle.addAttribute("md5", releaseFileInfo.getMd5());
            }
            System.out.println(documentEle.asXML());
            FileWriter fileWriter = new FileWriter(baseInfo.getReleasePath()+"\\ReleaseList.xml");
            OutputFormat outputFormat = OutputFormat.createPrettyPrint();
            outputFormat.setEncoding("utf-8");
            XMLWriter xmlWriter = new XMLWriter(fileWriter,outputFormat);
            xmlWriter.write(documentEle);
            xmlWriter.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return true;
    }


    /**
     * generate a file list under a given path
     * @param folder
     * @return
     */
    public void generateFileList (File folder, String relativePath) {
        try {
            for(File file : folder.listFiles()) {
                if(file.isDirectory()) {
                    generateFileList(file, relativePath + file.getName() + "\\");
                }
                else {
                    ReleaseFileInfo releaseFileInfo = new ReleaseFileInfo();
                    releaseFileInfo.setName(relativePath + file.getName());
                    releaseFileInfo.setDate(simpleDateFormat.format(new Date(file.lastModified())));
                    releaseFileInfo.setSize(Long.toString(file.length() >> 10));
                    releaseFileInfo.setMd5(getMd5ByFile(file));
                    releaseFileInfoList.add(releaseFileInfo);
                    copyFileToDirectory(file,versionFilePath+"\\"+relativePath);
//                    FileUtils.copyFileToDirectory(file,new File(versionFilePath));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * copy a file to a given directory,which may not exist
     * @param file
     * @param path
     */
    public void copyFileToDirectory(File file, String path) {
        File desDirectory = new File(path);
        if(!desDirectory.exists()) {
            if(!desDirectory.mkdirs()) {
                throw new RuntimeException("创建文件夹失败");
            }
        }
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel= new FileInputStream(file).getChannel();
            outputChannel = new FileOutputStream(new File(path+"\\"+file.getName())).getChannel();
            outputChannel.transferFrom(inputChannel,0,inputChannel.size());
        }
        catch (IOException io) {
            io.printStackTrace();
        } finally {
            if(inputChannel != null && outputChannel != null) {
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
     * @param file
     * @return
     */
    public static String getMd5ByFile(File file) {
        String value = null;
        try {
            FileInputStream in = new FileInputStream(file);
            MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteBuffer);
            BigInteger bi = new BigInteger(1, md5.digest());
            value = String.format("%032X", bi);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public Boolean packAndZip() {
        try {
            ZipFile zipFile = new ZipFile(versionFilePath+".zip");
            zipFile.createZipFileFromFolder(versionFile,new ZipParameters(),false,0);
            zipFile.addFile(new File(baseInfo.getReleasePath()+"\\ReleaseList.xml"),new ZipParameters());
        } catch (Exception e) {

        }
        return true;
    }

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
        for(int j = 0; j < bits.length; j++) {
            str += bits[j];
        }
        String numFormat = Integer.toString(Integer.parseInt(str) + 1);
        for(int i = 0; i < numFormat.length(); i++) {
            int s = numFormat.codePointCount(0,0);
            newVersion += numFormat.charAt(i)+".";
        }
        return newVersion.substring(0,newVersion.length() - 1);
    }

    /**
     * update the version number
     * e.g:
     * updateVersionNum(0.1.2) will return 0.1.3
     * updateVersionNum(9.9.9) will return 1.0.0.0
     * @param oldVersion
     * @return
     */
    public String updateVersionNum(String oldVersion) {
        String newVersion = "";
        if(oldVersion == null || !oldVersion.contains(".")) {
            throw new RuntimeException("the format of version number is wrong");
        }
        String bits[] = oldVersion.split("\\.");
        boolean allBitCarry = true;
        int i = bits.length - 1;
        while(i >= 0) {
            if(Integer.parseInt(bits[i]) == 9) {
                bits[i] = ".0";
                newVersion = bits[i --] +newVersion;
            } else {
                bits[i] = Integer.toString(Integer.parseInt(bits[i]) +1);
                newVersion = "." + bits[i --] +newVersion ;
                allBitCarry = false;
                break;
            }
        }
        if(i >= 0) {
            String s = oldVersion.substring(0, 2 * i);
            return oldVersion.substring(0, 2 * i + 1) + newVersion;
        }
        else if(allBitCarry){
            return "1"+ newVersion;
        }
        else {
            return newVersion.substring(1, newVersion.length());
        }
    }

    public void setBaseInfo(BaseInfo baseInfo) {
        this.baseInfo = baseInfo;
    }

    public void setReleaseFileInfoList(List<ReleaseFileInfo> releaseFileInfoList) {
        this.releaseFileInfoList = releaseFileInfoList;
    }

    public void setSimpleDateFormat(SimpleDateFormat simpleDateFormat) {
        this.simpleDateFormat = simpleDateFormat;
    }
}

