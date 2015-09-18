package com.dios.model;

public class User {
	private int id;
	private String name;
	private String password;
	private int level;
	private int belongToCatalogue;
	
	public User(int id, String name, String password, int level) {
		super();
		this.id = id;
		this.name = name;
		this.password = password;
		this.level = level;
	}
	
	public User(int id, String name, String password, int level, int catalogueCode) {
		super();
		this.id = id;
		this.name = name;
		this.password = password;
		this.level = level;
		this.belongToCatalogue = catalogueCode;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getBelongToCatalogue() {
		return belongToCatalogue;
	}

	public void setBelongToCatalogue(int belongToCatalogue) {
		this.belongToCatalogue = belongToCatalogue;
	}
	
}
