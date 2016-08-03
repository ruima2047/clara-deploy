package com.clara.deploy.service;


import java.io.*;
import java.util.*;

import com.clara.deploy.domain.BaseInfo;
import com.clara.deploy.domain.PathUtil;
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
public class DescriptSerlet extends HttpServlet {
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


		response.setContentType("text/html");



		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		try {
			// Parse the request to get file items.
			List fileItems = upload.parseRequest(request);
			// Process the uploaded file items
			Iterator i = fileItems.iterator();
			List<FileItem> fileItemList = new ArrayList<FileItem>();
			List<String> warehouseList = new ArrayList<String>();
			String moduleName = null;
			String userName = null;
			String description = null;
			while (i.hasNext()) {
				FileItem fileItem = (FileItem) i.next();
				if (fileItem.isFormField() && fileItem.getFieldName().equals("moduleName")) {
					moduleName = fileItem.getString();
				} else if(fileItem.isFormField() && fileItem.getFieldName().equals("userName")) {
					userName = fileItem.getString();
				}else if(fileItem.isFormField() && fileItem.getFieldName().equals("description")) {
					description = fileItem.getString();
				}else if (fileItem.isFormField() && fileItem.getFieldName().equals("warehouse")) {
					warehouseList.add(fileItem.getString());

				} else if (!fileItem.isFormField()) {
					fileItemList.add(fileItem);
				}
			}
			if(moduleName == null) {
				logger.error("moduleName is null!");
				throw new RuntimeException("moduleName is null!");
			}
			if(warehouseList.size() == 0) {
				logger.error("warehouse is null!");
				throw new RuntimeException("warehouse is null!");
			}
			List<String> filePathList = new ArrayList<String>();

			for(String warehouse : warehouseList) {
				BaseInfo baseInfo = (BaseInfo)baseInfoMap.get(warehouse);
				String uploadPath = PathUtil.getPath(baseInfo.getRootPath(),"Release","DLL",moduleName);
				filePathList.add(uploadPath);
				for(FileItem fileItem : fileItemList) {
					File file = new File(PathUtil.getPath(uploadPath,fileItem.getName()));
					OutputStream os = new FileOutputStream(file);
					InputStream is = fileItem.getInputStream();
					byte buf[] = new byte[1024];
					while(is.read(buf) > 0) {
						os.write(buf,0,buf.length);
					}
					os.flush();
					os.close();
					is.close();
					logger.warn(String.format("模块:%s 用户:%s 文件:%s",moduleName,userName,fileItem.getName()));
				}
			}

			PrintWriter writer = response.getWriter();
			writer.print("true");
			writer.flush();
			writer.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {

		}
	}

	public void doGet(HttpServletRequest request,
					  HttpServletResponse response)
			throws ServletException, IOException {

	}
}
