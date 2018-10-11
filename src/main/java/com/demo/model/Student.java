package com.demo.model;

import java.util.Date;

public class Student {

	private String stuNo;
	private String stuName;
	private int stuAge;
	private int stuSex;
	private Date birthday;
	
	
	public String getStuNo() {
		return stuNo;
	}
	public void setStuNo(String stuNo) {
		this.stuNo = stuNo;
	}
	public String getStuName() {
		return stuName;
	}
	public void setStuName(String stuName) {
		this.stuName = stuName;
	}
	public int getStuAge() {
		return stuAge;
	}
	public void setStuAge(int stuAge) {
		this.stuAge = stuAge;
	}
	public int getStuSex() {
		return stuSex;
	}
	public void setStuSex(int sex) {
		this.stuSex = sex;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	

}
