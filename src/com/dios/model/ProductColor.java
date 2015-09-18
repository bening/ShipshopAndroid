package com.dios.model;

public class ProductColor {
	private String colorString;
	private String colorName;
	
	public ProductColor(){
		
	}

	public ProductColor(String colorString, String colorName) {
		super();
		this.colorString = colorString;
		this.colorName = colorName;
	}

	public String getColorString() {
		return colorString;
	}

	public void setColorString(String colorString) {
		this.colorString = colorString;
	}

	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}		
	
}
