package com.dios.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Product implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -329934833140159300L;
	private int id;
	private String name="";
	private long price = 0;
	private String tag = "";
	private String mark = "";
	private int icon = -1;
	private transient Bitmap image = null;
	private int[] images;
	private String description = "";
	private int availableStock = 0;
	private long grosirPrice = 0;
	private ArrayList<String> availableColor;
	private String imageURL;
	private String[] imageURLs;
	
	public Product(){
		
	}
	
	public Product(String name) {
		super();
		this.name = name;
	}
	
	public Product(String name, long grocierPrice, long retailPrice) {
		super();
		this.name = name;
		this.grosirPrice = grocierPrice;
		this.price = retailPrice;
	}
	
	public Product(String name, long grocierPrice, long retailPrice, int _icon) {
		super();
		this.name = name;
		this.grosirPrice = grocierPrice;
		this.price = retailPrice;
		this.icon = _icon;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getPrice() {
		return price;
	}

	public void setPrice(long price) {
		this.price = price;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getAvailableStock() {
		return availableStock;
	}

	public void setAvailableStock(int availableStock) {
		this.availableStock = availableStock;
	}

	public long getGrosirPrice() {
		return grosirPrice;
	}

	public void setGrosirPrice(long grosirPrice) {
		this.grosirPrice = grosirPrice;
	}

	public ArrayList<String> getAvailableColor() {
		return availableColor;
	}

	public void setAvailableColorList(ArrayList<String> availableColor) {
		this.availableColor = availableColor;
	}

	public int[] getImages() {
		return images;
	}

	public void setImages(int[] images) {
		this.images = images;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public String[] getImageURLs() {
		return imageURLs;
	}

	public void setImageURLs(String[] imageURLs) {
		this.imageURLs = imageURLs;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}	
	
	private void writeObject(ObjectOutputStream oos) throws IOException{
       // This will serialize all fields that you did not mark with 'transient'
       // (Java's default behaviour)
        oos.defaultWriteObject();
       // Now, manually serialize all transient fields that you want to be serialized
        if(image!=null){
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            boolean success = image.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
            if(success){
                oos.writeObject(byteStream.toByteArray());
            }
        }
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException{
       // Now, all again, deserializing - in the SAME ORDER!
       // All non-transient fields
        ois.defaultReadObject();
       // All other fields that you serialized
        byte[] images = (byte[]) ois.readObject();
        if(images != null && images.length > 0){
        	this.image = BitmapFactory.decodeByteArray(images, 0, images.length);
        }
    }
	
		
}
