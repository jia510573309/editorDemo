package com.demo.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.WebUtils;


/**
 * 
 * @author XuLei
 * @date 2018年3月22日 16:38:14
 * @Descrption:图片上传工具
 * @version 1.0
 */
@Controller
@RequestMapping("img")
public class UeditorController {


	@RequestMapping(value = "/ueditor")
	@ResponseBody
	public String ueditor(HttpServletRequest request, HttpServletResponse response, String action) {

		System.out.println(action);
		if (action.equals("config")) {
			String s = "{\n" + "                \"imageActionName\": \"imgUpload\",\n"
					+ "                \"imageFieldName\": \"file\", \n"
					+ "                \"imageMaxSize\": 2048000, \n"
					+ "                \"imageAllowFiles\": [\".png\", \".jpg\", \".jpeg\", \".gif\", \".bmp\"], \n"
					+ "                \"imageCompressEnable\": true, \n"
					+ "                \"imageCompressBorder\": 150, \n"
					+ "                \"imageInsertAlign\": \"center\","
					+ "                \"imageUrlPrefix\": \"\",\n"
					+ "                \"imagePathFormat\": \"/ueditor/jsp/upload/image/{yyyy}{mm}{dd}/{time}{rand:6}\" }";
			return s;
		} else {
			String contentType = request.getContentType();
			if (contentType != null && contentType.toLowerCase().startsWith("multipart/")) {
				MultipartHttpServletRequest multipartRequest = WebUtils.getNativeRequest(request,
						MultipartHttpServletRequest.class);
				MultipartFile file = multipartRequest.getFile("file");
				return this.imgUpdate(file);
			}
			return null;
		}
	}

	public String imgUpdate(MultipartFile file) {
		String uploadPath = "F:/imgTest/";
		if (file.isEmpty()) {
			return "error";
		}
		// 获取文件名
		String fileName = file.getOriginalFilename();
		// 获取文件的后缀名
		String suffixName = fileName.substring(fileName.lastIndexOf("."));
		// 这里我使用随机字符串来重新命名图片
		fileName = Calendar.getInstance().getTimeInMillis() + UUID.randomUUID().toString() + suffixName;
		// 这里的路径为Nginx的代理路径，这里是/data/images/xxx.png
		File dest = new File(uploadPath + fileName);
		// 检测是否存在目录
		if (!dest.getParentFile().exists()) {
			dest.getParentFile().mkdirs();
		}
		try {
			file.transferTo(dest);
			// 创建图片对象
			// url的值为图片的实际访问地址 这里我用了Nginx代理，访问的路径是http://localhost/xxx.png
			String config = "{\"state\": \"SUCCESS\"," + "\"url\": \"" + "/img/getImage?path=" + uploadPath + fileName + "\","
					 + "\"," + "\"original\": \"" + fileName + "\"}";

			return config;

		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "error";
	}
	
	
	
	
	@RequestMapping(value="getImage")
	@ResponseBody
	public void getImageById(@RequestParam("path") String path, HttpServletResponse response) {
		if (path != null && !"".equals(path)) {
			File file = new File(path);
			response.setContentType("image/jpg");
			InputStream in = null;
			try {
				in = new FileInputStream(file);
				try {
					IOUtils.copy(in, response.getOutputStream());
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}


}