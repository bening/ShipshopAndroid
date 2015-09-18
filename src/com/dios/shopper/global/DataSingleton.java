package com.dios.shopper.global;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.dios.model.Catalogue;
import com.dios.model.Product;
import com.dios.model.User;

public class DataSingleton {
	private static DataSingleton instance;
	public ArrayList<Product> bags = new ArrayList<Product>();
	public ArrayList<Product> wishList = new ArrayList<Product>();
	public ArrayList<Catalogue> toko = new ArrayList<Catalogue>();
	public ArrayList<Catalogue> agen = new ArrayList<Catalogue>();
	public ArrayList<Catalogue> kategori = new ArrayList<Catalogue>();
	
	public static Map<String, User> account = new HashMap<String, User>();
	public static User loggedInUser = null;
	
	protected DataSingleton() {

	}

	public static DataSingleton getInstance() {
		if (instance == null) {
			instance = new DataSingleton();
		}

		return instance;
	}
}
