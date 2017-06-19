package org.lee.coderepo.db;

import java.io.Serializable;

/**
 * Created by lee on 2017/6/14.
 */
public class Database implements Serializable{
	
	private static final long serialVersionUID = -8109251954903284588L;
	
	private String ip;
	private Integer port;
	private String sid;
	private String type;
	private String username;
	private String password;
	
	public Database(){}
	
	public Database(String ip, Integer port, String sid, String type, String username, String password){
		this.ip = ip;
		this.port = port;
		this.sid = sid;
		this.type = type;
		this.username = username;
		this.password = password;
	}
	
	public String getIp() {
		return ip;
	}
	
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public Integer getPort() {
		return port;
	}
	
	public void setPort(Integer port) {
		this.port = port;
	}
	
	public String getSid() {
		return sid;
	}
	
	public void setSid(String sid) {
		this.sid = sid;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
}
