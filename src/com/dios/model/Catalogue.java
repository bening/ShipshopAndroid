package com.dios.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Catalogue implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1837962885404541722L;
	private int id;
	private String name;
	private int level;
	private String representativeImageUrl;
	private transient Bitmap image = null;
	private ArrayList<Product> products = new ArrayList<Product>();
	
	public Catalogue(int id, String name, int level) {
		super();
		this.id = id;
		this.name = name;
		this.level = level;
	}

	public Catalogue(int id, String name, int level, ArrayList<Product> products) {
		super();
		this.id = id;
		this.name = name;
		this.level = level;
		this.products = products;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public ArrayList<Product> getProducts() {
		return products;
	}

	public void setProducts(ArrayList<Product> products) {
		this.products = products;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRepresentativeImageUrl() {
		return representativeImageUrl;
	}

	public void setRepresentativeImageUrl(String representativeImageUrl) {
		this.representativeImageUrl = representativeImageUrl;
	}

	public Bitmap getRepresentativeImage() {
		return image;
	}

	public void setRepresentativeImage(Bitmap image) {
		this.image = image;
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
