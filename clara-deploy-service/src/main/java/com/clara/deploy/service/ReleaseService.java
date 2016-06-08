package com.clara.deploy.service;

import com.clara.deploy.domain.ReleaseFile;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
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

    private String RELEASEURL;
    private List<ReleaseFile> releaseFileList = new ArrayList<ReleaseFile>();
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/M/d H:mm:ss");

    public Boolean build (String path) {
        Boolean result = true;
        try {
            Runtime rt = Runtime.getRuntime();
            Process pr = rt.exec(new String[]{path});
            System.out.println(pr);
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * generate Release.xml
     * @param version
     * @param Des
     * @param path
     * @return
     */
    public Boolean generateReleaseList (String version, String Des, String path) {

        generateFileList(new File(path), "");

        try {
            Document document = DocumentHelper.createDocument();
            Element autoUpdater = document.addElement("AutoUpdater");
            Element appName = autoUpdater.addElement("AppName");
            appName.addText("WMS");
            Element releaseUrl = autoUpdater.addElement("ReleaseURL");
            releaseUrl.addText(RELEASEURL);
            Element releaseDate = autoUpdater.addElement("ReleaseDate");
            releaseDate.addText(simpleDateFormat.format(new Date()));
            Element releaseVersion = autoUpdater.addElement("ReleaseVersion");
            releaseVersion.addText(version);
            Element minVersion = autoUpdater.addElement("MinVersion");
            minVersion.addText(version);
            Element updateDes = autoUpdater.addElement("UpdateDes");
            updateDes.addText(Des);
            Element applicationStart = autoUpdater.addElement("ApplicationStart");
            applicationStart.addText("JD.WMSAO1.WPFAPP.exe");
            Element shortcutIcon = autoUpdater.addElement("ShortcutIcon");
            shortcutIcon.addText("null");
            Element releases = autoUpdater.addElement("Releases");
            for(ReleaseFile releaseFile: releaseFileList) {
                Element file = releases.addElement("File");
                file.addAttribute("name",releaseFile.getName());
                file.addAttribute("date",releaseFile.getDate());
                file.addAttribute("size",releaseFile.getSize());
                file.addAttribute("md5",releaseFile.getMd5());
            }
            System.out.println(document.asXML());
            FileWriter fileWriter = new FileWriter("E:\\ReleaseList.xml");
            OutputFormat outputFormat = OutputFormat.createPrettyPrint();
            outputFormat.setEncoding("utf-8");
            XMLWriter xmlWriter = new XMLWriter(fileWriter,outputFormat);
            xmlWriter.write(document);
            xmlWriter.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return true;
    }

    /**
     * To generate a file list in /Release
     * @param path
     * @return
     */
//    public List<ReleaseFile> generateFileList (String path) {
//        File dir = new File(path);
//        FileUtils fileUtils = new FileUtils();
//        List<ReleaseFile> releaseFileList = new ArrayList<ReleaseFile>();
//        Collection<File> files = fileUtils.listFiles(dir, null, true);
////        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/M/d H:mm:ss");
//        try {
//            for(File f : files) {
//                ReleaseFile releaseFile = new ReleaseFile();
//                releaseFile.setName(f.getPath().substring(f.getPath().lastIndexOf("Release\\")));
//                releaseFile.setDate(simpleDateFormat.format(new Date(f.lastModified())));
//                releaseFile.setSize(Long.toString(f.length() >> 10));
//                releaseFile.setMd5(getMd5ByFile(f));
//                releaseFileList.add(releaseFile);
//            }
//        } catch (Exception e) {
//                e.printStackTrace();
//        }
//        return releaseFileList;
//    }

    /**
     * generate a file list under a given path
     * @param folder
     * @return
     */
    public void generateFileList (File folder, String relativePath) {
        for(File file : folder.listFiles()) {
            if(file.isDirectory()) {
                generateFileList(file, relativePath + file.getName() + "\\");
            }
            else {
                ReleaseFile releaseFile = new ReleaseFile();
                releaseFile.setName(relativePath+file.getName());
                releaseFile.setDate(simpleDateFormat.format(new Date(file.lastModified())));
                releaseFile.setSize(Long.toString(file.length() >> 10));
                releaseFile.setMd5(getMd5ByFile(file));
                releaseFileList.add(releaseFile);
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

    public void setRELEASEURL(String RELEASEURL) {
        this.RELEASEURL = RELEASEURL;
    }
}

