package com.dios.shopper.model;

import java.util.ArrayList;

import com.dios.model.Catalogue;

import android.graphics.Bitmap;

public class NavDrawerItem {
	
	private String title;
	private int icon;
	private String count = "0";
	// boolean to set visiblity of the counter
	private boolean isCounterVisible = false;
	private Bitmap imageIcon = null;
	private boolean asBackNavigation = false;
	private boolean asTabCell = false;
	private DrawerSelection selection1 = null;
	private DrawerSelection selection2 = null;
	private Catalogue store = null;
	
	public NavDrawerItem(){}

	public NavDrawerItem(String title, int icon){
		this.title = title;
		this.icon = icon;
	}
	
	public NavDrawerItem(String title, int icon, boolean isCounterVisible, String count){
		this.title = title;
		this.icon = icon;
		this.isCounterVisible = isCounterVisible;
		this.count = count;
	}
	
	public NavDrawerItem(String title, Bitmap image, boolean isCounterVisible, String count){
		this.title = title;
		this.icon = -1;
		this.imageIcon = image;
		this.isCounterVisible = isCounterVisible;
		this.count = count;
	}
	
	public NavDrawerItem(Catalogue store, Bitmap image, boolean isCounterVisible, String count){
		this.store = store;
		this.title = store.getName();
		this.icon = -1;
		this.imageIcon = image;
		this.isCounterVisible = isCounterVisible;
		this.count = count;
	}
	
	public NavDrawerItem(String title, Bitmap image, boolean isCounterVisible, String count, boolean isBackNavigation){
		this.title = title;
		this.icon = -1;
		this.imageIcon = image;
		this.isCounterVisible = isCounterVisible;
		this.count = count;
		this.asBackNavigation = isBackNavigation;
	}
	
	public NavDrawerItem(String title, Bitmap image, boolean isCounterVisible, String count, boolean isTabCell, DrawerSelection menu1, DrawerSelection menu2){
		this.title = title;
		this.icon = -1;
		this.imageIcon = image;
		this.isCounterVisible = isCounterVisible;
		this.count = count;
		this.asTabCell = isTabCell;
		this.selection1 = menu1;
		this.selection2 = menu2;
	}
	
	public String getTitle(){
		return this.title;
	}
	
	public int getIcon(){
		return this.icon;
	}
	
	public String getCount(){
		return this.count;
	}
	
	public boolean getCounterVisibility(){
		return this.isCounterVisible;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public void setIcon(int icon){
		this.icon = icon;
	}
	
	public void setCount(String count){
		this.count = count;
	}
	
	public void setCounterVisibility(boolean isCounterVisible){
		this.isCounterVisible = isCounterVisible;
	}

	public Bitmap getImageIcon() {
		return imageIcon;
	}

	public void setImageIcon(Bitmap imageIcon) {
		this.imageIcon = imageIcon;
	}

	public boolean isAsBackNavigation() {
		return asBackNavigation;
	}

	public void setAsBackNavigation(boolean special) {
		this.asBackNavigation = special;
	}

	public boolean isAsTabCell() {
		return asTabCell;
	}

	public void setAsTabCell(boolean asTabCell) {
		this.asTabCell = asTabCell;
	}

	public DrawerSelection getSelection1() {
		return selection1;
	}

	public void setSelection1(DrawerSelection selection1) {
		this.selection1 = selection1;
	}

	public DrawerSelection getSelection2() {
		return selection2;
	}

	public void setSelection2(DrawerSelection selection2) {
		this.selection2 = selection2;
	}
	
	
	
	
}
