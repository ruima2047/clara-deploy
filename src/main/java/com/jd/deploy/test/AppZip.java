package com.jd.deploy.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.util.Date;

public class AppZip
{
    List<String> fileList;
    private static final String OUTPUT_ZIP_FILE = "F:\\file\\1.6.8.3.zip";
    private static final String SOURCE_FOLDER = "F:\\file\\1.6.8.3";

    AppZip(){
        fileList = new ArrayList<String>();
    }

    public static void main( String[] args )
    {
        AppZip appZip = new AppZip();
        appZip.generateFileList(new File(SOURCE_FOLDER));

        try{
            appZip.zipIt(OUTPUT_ZIP_FILE);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Zip it
     * @param zipFile output ZIP file location
     */
    public void zipIt(String zipFile) throws Exception{

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/M/d H:mm:ss");
        String date = "2016/4/19 14:28:48";

        byte[] buffer = new byte[1024];

        try{

            FileOutputStream fos = new FileOutputStream(zipFile);
            ZipOutputStream zos = new ZipOutputStream(fos);

            System.out.println("Output to Zip : " + zipFile);

            for(String file : this.fileList){

                System.out.println("File Added : " + file);
                ZipEntry ze= new ZipEntry("1.6.8.3"+ File.separator +file);
                zos.putNextEntry(ze);
                FileInputStream in =
                        new FileInputStream(SOURCE_FOLDER + File.separator + file);

                int len;
                while ((len = in.read(buffer)) > 0) {
                    zos.write(buffer, 0, len);
                }
                Date datetemp = simpleDateFormat.parse(date);
                ze.setTime(datetemp.getTime());
            }
            ZipEntry ze= new ZipEntry("ReleaseList.xml");
            zos.putNextEntry(ze);
            FileInputStream in =
                    new FileInputStream(SOURCE_FOLDER + File.separator + "ReleaseList.xml");
            int len;
            while ((len = in.read(buffer)) > 0) {
                zos.write(buffer, 0, len);
            }
            in.close();

            zos.closeEntry();
            //remember close it
            zos.close();

            System.out.println("Done");
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    /**
     * Traverse a directory and get all files,
     * and add the file into fileList
     * @param node file or directory
     */
    public void generateFileList(File node){

        //add file only
        if(node.isFile()){
            fileList.add(generateZipEntry(node.getAbsoluteFile().toString()));
        }

        if(node.isDirectory()){
            String[] subNote = node.list();
            for(String filename : subNote){
                generateFileList(new File(node, filename));
            }
        }

    }

    /**
     * Format the file path for zip
     * @param file file path
     * @return Formatted file path
     */
    private String generateZipEntry(String file){
        return file.substring(SOURCE_FOLDER.length()+1, file.length());
    }
}