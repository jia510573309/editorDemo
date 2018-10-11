package com.demo.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.demo.model.Student;

/**
 * 
 * @author jiamin
 * @date 2018-10-11 此导出工具需要poi工具包的支持 <!-- 导出excel -->
 *       <dependency> <groupId>org.apache.poi</groupId>
 *       <artifactId>poi</artifactId> <version>4.0.0</version> </dependency>
 */
@Controller
@RequestMapping("/export")
public class ExcelExportController {

	@RequestMapping("/excel")
	public void getExcel(HttpServletResponse response) throws UnsupportedEncodingException {
		response.setContentType("octets/stream");
		String excelName = "Excel信息表";
		// 转码防止乱码
		response.addHeader("Content-Disposition",
				"attachment;filename=" + new String(excelName.getBytes("gb2312"), "ISO8859-1") + ".xls");
		// 设置Excel表格的列
		String[] headers = new String[] { "编号", "姓名", "年龄", "性别" };
		
		try {
			OutputStream out = response.getOutputStream();
			exportExcel(excelName, headers, getList(), out);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @Description: 从数据库中查询出来的数据，一般是数据表中的几列
	 * @Auther: 贾敏
	 * @Date: 2013-8-22 下午2:53:58
	 */
	public List<Map<String, Object>> getList() {
		// 需要导出的数据列表(此处自定义的学生信息)
		List<Student> clist = new ArrayList<>();
		for (int i = 1; i <= 5; i++) {
			Student s = new Student();
			s.setStuNo(i + "");
			s.setStuName("张三" + i);
			s.setStuAge(i);
			s.setStuSex((int)(Math.random() * 2));
			s.setBirthday(new Date());
			clist.add(s);
		}
		// 封装成map存进list
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (int i = 0; i < clist.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("stuNo", clist.get(i).getStuNo());
			map.put("stuName", clist.get(i).getStuName());
			map.put("stuAge", clist.get(i).getStuAge());
			map.put("stuSex", clist.get(i).getStuSex());
			String d = sdf.format(clist.get(i).getBirthday());
			map.put("birthday", d);
			list.add(map);
		}
		return list;
	}

	/**
	 * 
	 * @Description: 生成excel并导出到客户端（本地）
	 * @Auther: 贾敏
	 * @Date: 2016-12-06 下午3:05:49
	 */
	protected void exportExcel(String title, String[] headers, List<?> mapList, OutputStream out) {
		// 声明一个工作簿
		@SuppressWarnings("resource")
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个表格
		HSSFSheet sheet = workbook.createSheet(title);
		// 设置表格默认列宽度为15个字符
		sheet.setDefaultColumnWidth(20);
		// 生成一个样式，用来设置标题样式
		HSSFCellStyle style = workbook.createCellStyle();
		// 产生表格标题行
		HSSFRow row = sheet.createRow(0);
		for (int i = 0; i < headers.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellStyle(style);
			HSSFRichTextString text = new HSSFRichTextString(headers[i]);
			cell.setCellValue(text);
		}
		// 读取表格数据
		for (int i = 0; i < mapList.size(); i++) {
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) mapList.get(i);
			row = sheet.createRow(i + 1);
			int j = 0;
			row.createCell(j++).setCellValue(map.get("stuNo").toString());
			row.createCell(j++).setCellValue(map.get("stuName").toString());
			row.createCell(j++).setCellValue(map.get("stuAge").toString());
			row.createCell(j++).setCellValue("0".equals(map.get("stuSex").toString()) ? "女" : "男");
			row.createCell(j++).setCellValue(map.get("birthday").toString());
		}
		try {
			workbook.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}