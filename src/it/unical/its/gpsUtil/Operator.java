package it.unical.its.gpsUtil;

public class Operator {
	
	private int code;
	private String name;

	public Operator(int code, String name) {
		// TODO Auto-generated constructor stub
		this.setCode(code);
		this.setName(name);
	}
	public Operator() {
		// TODO Auto-generated constructor stub
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
