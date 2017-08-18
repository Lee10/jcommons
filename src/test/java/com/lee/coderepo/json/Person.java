package com.lee.coderepo.json;

import java.util.Date;
import java.util.List;

/**
 * Created by 10064350 on 2017/8/18.
 */
public class Person {

	private String name;
	private int age;
	private Date brithday;
	private Person wift;
	private List<Person> family;

	public Person(){}

	public Person(String name, int age){
		this.name = name;
		this.age = age;
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

	public Date getBrithday() {
		return brithday;
	}

	public void setBrithday(Date brithday) {
		this.brithday = brithday;
	}

	public Person getWift() {
		return wift;
	}

	public void setWift(Person wift) {
		this.wift = wift;
	}

	public List<Person> getFamily() {
		return family;
	}

	public void setFamily(List<Person> family) {
		this.family = family;
	}
}
