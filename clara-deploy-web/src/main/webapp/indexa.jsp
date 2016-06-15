<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>

		<title>Uploadify上传</title>
        <meta http-equiv="pragma" content="no-cache">
        <meta http-equiv="cache-control" content="no-cache">
        <meta http-equiv="expires" content="0">
        <link rel="stylesheet" href="uploadify/uploadify.css" type="text/css"/>
        <!--<script src="http://code.jquery.com/jquery-2.1.4.min.js"></script>-->
        <script type="text/javascript"
                src="uploadify/jquery.min.js"></script>
        <script type="text/javascript"
                src="uploadify/jquery.uploadify.min.js"></script>
        <script type="text/javascript">
			  
			$(function() {
			    $("#file_upload").uploadify({
			    	'height'        : 27, 
			    	'width'         : 80,  
			    	'buttonText'    : '浏 览',
			        'swf'           : '/uploadify/uploadify.swf',
			        'uploader'      : '/testAction',
			        'auto'          : false,
			        'fileTypeExts'  : '*.xls',
			        'formData'      : {'userName':'','qq':''},
			        'onUploadStart' : function(file) {
			        	 
			        	//校验   
		                var name=$('#userName').val();    
			             if(name.replace(/\s/g,'') == ''){   
			                  alert("名称不能为空！");   
			                  return false;   
			             } 
			             
			             //校验   
		                var qq=$('#qq').val();    
			             if(qq.replace(/\s/g,'') == ''){   
			                  alert("QQ不能为空！");   
			                  return false;   
			             }
			             
			        	$("#file_upload").uploadify("settings", "formData", {'userName':name,'qq':qq});
			        	//$("#file_upload").uploadify("settings", "qq", );
			        }
			    });
			});
		
	</script>
	</head>

	<body>

		名称: <input type="text" id="userName" name="userName" value="妞见妞爱">
		<br>
		 QQ: <input type="text" id="qq" name="qq" value="609865047">
		<br>
		<input type="file" name="uploadify" id="file_upload" />
		<hr>
		<a href="javascript:$('#file_upload').uploadify('upload','*')">开始上传</a>&nbsp;   
        <a href="javascript:$('#file_upload').uploadify('cancel', '*')">取消所有上传</a> 
	</body>
</html>
