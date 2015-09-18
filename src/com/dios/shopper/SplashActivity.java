package com.dios.shopper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Random;

import com.dios.model.Catalogue;
import com.dios.model.Product;
import com.dios.model.User;
import com.dios.shopper.global.Constants;
import com.dios.shopper.global.Data;
import com.dios.shopper.global.DataSingleton;
import com.dios.shopper.util.Helper;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SplashActivity extends Activity  {
	// Splash screen timer
	private static int SPLASH_TIME_OUT = 3000;
	private ProgressBar loadingProgress;
	private TextView loadingMsg;
	private FrameLayout loadingLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		loadingMsg = (TextView) findViewById(R.id.loadingMsg);
		loadingLayout = (FrameLayout) findViewById(R.id.loadingWrapper);
		loadingProgress = (ProgressBar) findViewById(R.id.progressBar);
		loadingProgress.setMax(100);
		loadingProgress.setProgress(0);
		/*Animation an = new RotateAnimation(0.0f, 90.0f, 250f, 273f);
		an.setFillAfter(true);
		loadingProgress.startAnimation(an);*/
		new Handler().postDelayed(new Runnable() {

			/*
			 * Showing splash screen with a timer. This will be useful when you
			 * want to show case your app logo / company
			 */

			@Override
			public void run() {
				// This method will be executed once the timer is over
				// Start your app main activity
				
				//Helper.loadPreference(SplashActivity.this);
				//Intent intent = new Intent(SplashActivity.this, MainActivity.class);
				/*if (Helper.isUserLoggedIn()) {
					intent = new Intent(SplashActivity.this, MainActivity.class);

				} else {
					intent = new Intent(SplashActivity.this,
							LoginActivity.class);
				}*/
				//startActivity(intent);

				// close this activity
				//finish();
			}
		}, SPLASH_TIME_OUT);
		
		LoadData loading = new LoadData();
		loading.execute();
	}
	
	class LoadData extends AsyncTask<Void, Integer, Long>{
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			//loadingLayout.setVisibility(View.VISIBLE);
			loadingProgress.setProgress(0);
			loadingMsg.setText(getString(R.string.loading_message_begin));
			super.onPreExecute();
		}

		@Override
		protected Long doInBackground(Void... params) {
			// TODO Auto-generated method stub
			long start = new Date().getTime();
			publishProgress(0);
	        // do the work
			
			int maxImageUrlCollectionIndex = Data.URLS.length-1;
			int minImageUrlCollectionIndex = 0;
			int randomNum;
			String[] tokoList = {"Toko 1","Toko 2","Toko 3","Toko 4","Toko 5"};
			String[] agenList = {"Agen 1","Agen 2","Agen 3","Agen 4","Agen 5"};
			String[] kategoriList = {"Kategori 1","Kategori 2","Kategori 3","Kategori 4", "Kategori 5"};
			User user1 = new User(1, "user1", "user1", Constants.LEVEL_NONE);
			User user2 = new User(2, "agen1", "agen1", Constants.LEVEL_AGENT);
			User user3 = new User(2, "shopowner1", "shopowner1", Constants.LEVEL_TOKO);
			DataSingleton.getInstance().toko.clear();
			DataSingleton.getInstance().agen.clear();
			DataSingleton.getInstance().kategori.clear();
			DataSingleton.getInstance().bags.clear();
			DataSingleton.getInstance().wishList.clear();
			DataSingleton.getInstance().account.clear();
			DataSingleton.getInstance().account.put(user1.getName(), user1);
			DataSingleton.getInstance().account.put(user2.getName(), user2);
			DataSingleton.getInstance().account.put(user3.getName(), user3);
			
			int max_data_count = tokoList.length+agenList.length+kategoriList.length;
			
			Random rand = new Random();
			
			int i = 1;
			for (String toko : tokoList) {
				publishProgress((int)i*100/max_data_count);
				
				rand = new Random();
				randomNum = rand.nextInt((maxImageUrlCollectionIndex - minImageUrlCollectionIndex) + 1) + minImageUrlCollectionIndex;
				
				Catalogue newCatalogue = new Catalogue(i, toko, Constants.LEVEL_TOKO);
				newCatalogue.setRepresentativeImageUrl(Data.URLS[randomNum]);
				
				ArrayList<Product> items = new ArrayList<Product>();
				
				Product p1 = new Product("Baju 1 "+toko, 400000,(long)(400000+(400000*0.3)),R.drawable.image_3);
				p1.setImageURLs(Data.imagesColl1);
				
				Product p2 = new Product("Baju 2 "+toko, 250000,(long)(250000+(250000*0.3)),R.drawable.image_5);
				p2.setImageURLs(Data.imagesColl2);
				
				Product p3 = new Product("Baju 3 "+toko, 300000,(long)(300000+(300000*0.3)),R.drawable.image_14);
				p3.setImageURLs(Data.imagesColl3);
				
				Product p4 = new Product("Baju 4 "+toko, 220000,(long)(220000+(220000*0.3)),R.drawable.image_12);
				p4.setImageURLs(Data.imagesColl4);
				
				Product p5 = new Product("Baju 5 "+toko, 4050000,(long)(4050000+(4050000*0.3)),R.drawable.image_1);
				p5.setImageURLs(Data.imagesColl5);
				
				Product p6 = new Product("Baju 6 "+toko, 2500000,(long)(2500000+(2500000*0.3)),R.drawable.image_9);
				p6.setImageURLs(Data.imagesColl6);
				
				items.add(p1);items.add(p2);
				items.add(p3);items.add(p4);
				items.add(p5);items.add(p6);
				
				/*long seed = System.nanoTime();
				Collections.shuffle(items, new Random(seed));*/
				newCatalogue.setProducts(items);
				String username = "agen";
				String password = "agen";
				User _user = new User(100+i, username+i+"", password+i+"", Constants.LEVEL_TOKO, newCatalogue.getId());
				
				DataSingleton.getInstance().account.put(_user.getName(), _user);
				DataSingleton.getInstance().toko.add(newCatalogue);			
				i++;
			}
			i = 1;
			for (String agen : agenList) {
				publishProgress((int)i*100/max_data_count);
				
				rand = new Random();
				randomNum = rand.nextInt((maxImageUrlCollectionIndex - minImageUrlCollectionIndex) + 1) + minImageUrlCollectionIndex;
				
				Catalogue newCatalogue = new Catalogue(i, agen, Constants.LEVEL_AGENT);
				newCatalogue.setRepresentativeImageUrl(Data.URLS[randomNum]);
				
				ArrayList<Product> items = new ArrayList<Product>();
				
				Product p1 = new Product("Baju 1 "+agen, 400000,(long)(400000+(400000*0.3)),R.drawable.image_2);
				p1.setImageURLs(Data.imagesColl1);
				Product p2 = new Product("Baju 2 "+agen, 250000,(long)(250000+(250000*0.3)),R.drawable.image_1);
				p2.setImageURLs(Data.imagesColl2);
				Product p3 = new Product("Baju 3 "+agen, 300000,(long)(300000+(300000*0.3)),R.drawable.image_14);
				p3.setImageURLs(Data.imagesColl3);
				Product p4 = new Product("Baju 4 "+agen, 220000,(long)(220000+(220000*0.3)),R.drawable.image_20);
				p4.setImageURLs(Data.imagesColl4);
				Product p5 = new Product("Baju 5 "+agen, 4050000,(long)(4050000+(4050000*0.3)),R.drawable.image_10);
				p5.setImageURLs(Data.imagesColl5);
				Product p6 = new Product("Baju 6 "+agen, 2500000,(long)(2500000+(2500000*0.3)),R.drawable.image_19);
				p6.setImageURLs(Data.imagesColl6);
				
				items.add(p1);items.add(p2);
				items.add(p3);items.add(p4);
				items.add(p5);items.add(p6);
				
				/*long seed = System.nanoTime();
				Collections.shuffle(items, new Random(seed));*/
				newCatalogue.setProducts(items);
				
				String username = "subagen";
				String password = "subagen";
				User _user = new User(200+i, username+i+"", password+i+"", Constants.LEVEL_AGENT, newCatalogue.getId());
				
				DataSingleton.getInstance().account.put(_user.getName(), _user);
				DataSingleton.getInstance().agen.add(newCatalogue);
				i++;
			}
			i = 1;
			for (String kategori : kategoriList) {
				publishProgress((int)i*100/max_data_count);
				
				rand = new Random();
				randomNum = rand.nextInt((maxImageUrlCollectionIndex - minImageUrlCollectionIndex) + 1) + minImageUrlCollectionIndex;
				
				Catalogue newCatalogue = new Catalogue(i, kategori, Constants.LEVEL_NONE);
				newCatalogue.setRepresentativeImageUrl(Data.URLS[randomNum]);
				
				ArrayList<Product> items = new ArrayList<Product>();
				
				Product p1 = new Product("Baju 1 "+kategori, 400000,(long)(400000+(400000*0.3)),R.drawable.image_23);
				p1.setImageURLs(Data.imagesColl1);
				
				Product p2 = new Product("Baju 2 "+kategori, 250000,(long)(250000+(250000*0.3)),R.drawable.image_15);
				p2.setImageURLs(Data.imagesColl2);
				
				Product p3 = new Product("Baju 3 "+kategori, 300000,(long)(300000+(300000*0.3)),R.drawable.image_24);
				p3.setImageURLs(Data.imagesColl3);
				
				Product p4 = new Product("Baju 4 "+kategori, 220000,(long)(220000+(220000*0.3)),R.drawable.image_10);
				p4.setImageURLs(Data.imagesColl4);
				
				Product p5 = new Product("Baju 5 "+kategori, 4050000,(long)(4050000+(4050000*0.3)),R.drawable.image_13);
				p5.setImageURLs(Data.imagesColl5);
				
				Product p6 = new Product("Baju 6 "+kategori, 2500000,(long)(2500000+(2500000*0.3)),R.drawable.image_19);
				p6.setImageURLs(Data.imagesColl6);
				
				items.add(p1);items.add(p2);
				items.add(p3);items.add(p4);
				items.add(p5);items.add(p6);
				
				/*long seed = System.nanoTime();
				Collections.shuffle(items, new Random(seed));*/
				newCatalogue.setProducts(items);
				
				DataSingleton.getInstance().kategori.add(newCatalogue);
				i++;
			}
			publishProgress(100);

	        long end = new Date().getTime();

	        if ( end-start < SPLASH_TIME_OUT ){
	        	try {
					Thread.sleep( SPLASH_TIME_OUT-(end-start));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }

			return null;
		}

		@Override
		protected void onPostExecute(Long result) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(SplashActivity.this, MainActivity.class);
			/*if (Helper.isUserLoggedIn()) {
				intent = new Intent(SplashActivity.this, MainActivity.class);

			} else {
				intent = new Intent(SplashActivity.this,
						LoginActivity.class);
			}*/
			startActivity(intent);

			// close this activity
			finish();
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			//super.onProgressUpdate(values);
			Log.e("Progress", ""+values[0]);
			loadingProgress.setProgress(values[0]);
			if(values[0]==100){
				loadingMsg.setText(getString(R.string.loading_message_finish));
			}
		}
	}
}
