package org.lee.coderepo.excel;

import java.util.Date;

/**
 * Created by lee on 2017/2/14.
 */
public class Person {

	private String name;
	private int age;
	private boolean sex;
	private Date brithday;

	public Person(String name, int age, boolean sex, Date brithday){
		this.name = name;
		this.age = age;
		this.sex = sex;
		this.brithday = brithday;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public boolean isSex() {
		return sex;
	}

	public void setSex(boolean sex) {
		this.sex = sex;
	}

	public Date getBrithday() {
		return brithday;
	}

	public void setBrithday(Date brithday) {
		this.brithday = brithday;
	}
}
