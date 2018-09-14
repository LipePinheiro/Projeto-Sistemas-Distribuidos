package com.exercicios.model;


public class User {
	
	public String username;
	public String password;
	public String email;
	public String name;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public User(String username, String password, String email, String name) {
		super();
		this.username = username;
		this.password = password;
		this.email = email;
		this.name = name;
	}
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

}
