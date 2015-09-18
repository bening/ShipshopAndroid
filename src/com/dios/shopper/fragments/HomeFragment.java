package com.dios.shopper.fragments;


import java.util.ArrayList;

import org.lucasr.twowayview.TwoWayView;

import com.dios.model.Catalogue;
import com.dios.model.Product;
import com.dios.shopper.MainActivity;
import com.dios.shopper.R;
import com.dios.shopper.global.Constants;
import com.dios.shopper.global.DataSingleton;
import com.dios.shopper.listadapter.ProductAdapter;
import com.dios.shopper.listadapter.ProductAdapterHorizontal;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.VideoView;

@SuppressLint("NewApi")
public class HomeFragment extends Fragment{
	
	private static final String TAG ="HomeFragment";
	private Activity context;
	View rootView;
	ListView productsListView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.e(TAG, "OnCreate");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.e(TAG, "OnCreateView");
		rootView = inflater.inflate(R.layout.fragment_home, container, false);
		context = getActivity();
		//read: http://code.tutsplus.com/tutorials/streaming-video-in-android-apps--cms-19888
		try {
            final VideoView videoView =(VideoView)rootView.findViewById(R.id.video);
      //1   //mediaController = new MediaController(Splashscreen.this);
      //2   //mediaController.setAnchorView(videoView);
            // Set video link (mp4 format )
            Uri video = Uri.parse("https://archive.org/download/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4");
            //videoView.setMediaController(mediaController);
            videoView.setVideoURI(video);
            MediaController vidControl = new MediaController(context);
            vidControl.setAnchorView(videoView);
            videoView.setMediaController(vidControl);
            videoView.start();
            /*videoView.setOnPreparedListener(new OnPreparedListener() {
                public void onPrepared(MediaPlayer mp) {
                    
                   videoView.start();
                }
            });*/

         }catch(Exception e){
             System.out.println("Video Play Error :"+e.getMessage());
         }
		
		final ArrayList<Catalogue> listToko = new ArrayList<Catalogue>(DataSingleton.getInstance().toko);
		final ArrayList<Catalogue> listKategori = new ArrayList<Catalogue>(DataSingleton.getInstance().kategori);
		/*Product p1 = new Product("Toko 1", 250000,R.drawable.image_12);
		Product p2 = new Product("Toko 2", 250000,R.drawable.image_1);
		Product p3 = new Product("Toko 3", 250000,R.drawable.image_14);
		Product p4 = new Product("Toko 4", 250000,R.drawable.image_3);
		Product p5 = new Product("Toko 5", 250000,R.drawable.image_9);
		Product p6 = new Product("Toko 6", 250000,R.drawable.image_5);
		
		Product p11 = new Product("Kategori 1", 250000,R.drawable.image_5);
		Product p21 = new Product("Kategori 2", 250000,R.drawable.image_9);
		Product p31 = new Product("Kategori 3", 250000,R.drawable.image_4);
		Product p41 = new Product("Kategori 4", 250000,R.drawable.image_8);
		Product p51 = new Product("Kategori 5", 250000,R.drawable.image_10);
		Product p61 = new Product("Kategori 6", 250000,R.drawable.image_12);
		
		listToko.add(p1);listToko.add(p2);listToko.add(p3);listToko.add(p4);listToko.add(p5);listToko.add(p6);
		listKategori.add(p11);listKategori.add(p21);listKategori.add(p31);listKategori.add(p41);listKategori.add(p51);listKategori.add(p61);*/
		
		ProductAdapterHorizontal adapterToko = new ProductAdapterHorizontal(context, listToko);
		ProductAdapterHorizontal adapterKategori = new ProductAdapterHorizontal(context, listKategori);
		TwoWayView kategoriList = (TwoWayView) rootView.findViewById(R.id.kategoriList);
		TwoWayView tokoList = (TwoWayView) rootView.findViewById(R.id.tokoList);
		kategoriList.setAdapter(adapterKategori);
		tokoList.setAdapter(adapterToko);
		
		kategoriList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
				// TODO Auto-generated method stub
				KategoriFragment fragment = new KategoriFragment().newInstance(listKategori.get(position));
				FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction()
						.replace(R.id.frame_container, fragment)
						.addToBackStack(null).commit();
				((MainActivity)getActivity()).warpToFragment(Constants.DRAWER_LIST_KATEGORI, position+1);
			}
			
		});
		
		tokoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
				// TODO Auto-generated method stub
				
				Catalogue selectedToko = listToko.get(position);
				
				if(null!=DataSingleton.getInstance().loggedInUser && DataSingleton.getInstance().loggedInUser.getLevel()==Constants.LEVEL_AGENT){
					((MainActivity)getActivity()).showToast(getResources().getString(R.string.access_denied, "toko"));
				}else if(null!=DataSingleton.getInstance().loggedInUser && DataSingleton.getInstance().loggedInUser.getLevel()==Constants.LEVEL_TOKO){
					if(DataSingleton.getInstance().loggedInUser.getBelongToCatalogue() == selectedToko.getId()){
						TokoFragment fragment = new TokoFragment().newInstance(listToko.get(position));
						FragmentManager fragmentManager = getFragmentManager();
						fragmentManager.beginTransaction()
								.replace(R.id.frame_container, fragment)
								.addToBackStack(null).commit();
						((MainActivity)getActivity()).warpToFragment(Constants.DRAWER_LIST_TOKO, position+1);
					}else{
						((MainActivity)getActivity()).showToast(getResources().getString(R.string.access_denied, "toko"));
					}
				}
				else{
					TokoFragment fragment = new TokoFragment().newInstance(listToko.get(position));
					FragmentManager fragmentManager = getFragmentManager();
					fragmentManager.beginTransaction()
							.replace(R.id.frame_container, fragment)
							.addToBackStack(null).commit();
					((MainActivity)getActivity()).warpToFragment(Constants.DRAWER_LIST_TOKO, position+1);
				}
				
			}
			
		});
		
		/*//get screen size info
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		//int width = size.x;
		//int height = size.y;
		DisplayMetrics metrics = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int height = metrics.heightPixels;
		int width = metrics.widthPixels;
		Log.e("width", ""+width);
		Log.e("height", ""+height);
		
		final ArrayList<Product> items = new ArrayList<Product>();
		Product p1 = new Product("Baju 1", 250000,R.drawable.image_12);
		Product p2 = new Product("Baju 2", 250000,R.drawable.image_1);
		Product p3 = new Product("Baju 3", 250000,R.drawable.image_14);
		Product p4 = new Product("Baju 4", 250000,R.drawable.image_3);
		Product p5 = new Product("Baju 5", 250000,R.drawable.image_9);
		Product p6 = new Product("Baju 6", 250000,R.drawable.image_5);
		items.add(p1);items.add(p2);items.add(p3);items.add(p4);items.add(p5);items.add(p6);
		
		for (int i = 0; i < items.size(); i++) {
			items.get(i).setDescription("Kebaya Kece\n-Bahan katun\n-Nyaman dipakai");			
			items.get(i).setGrosirPrice(50000 + ((int)(Math.random() * 10) * 15000));
			items.get(i).setAvailableStock((int)(Math.random() * 10) + 1);
			
			String[] colors = new String[]{"black", "white", "red", "blue"};

			ArrayList<String> colorList = new ArrayList<String>();			
			for(int j = 0; j < (int)(Math.random() * colors.length) + 1; j++){
				colorList.add(colors[j]);
			}	
			items.get(i).setAvailableColorList(colorList);
		}
		
		ProductAdapter adapter = new ProductAdapter(context, items);
		
		GridView gridview = (GridView) rootView.findViewById(R.id.gridview);
	    //gridview.setAdapter(new ImageAdapter(context));
		int screenSize = context.getResources().getConfiguration().screenLayout &
		        Configuration.SCREENLAYOUT_SIZE_MASK;
		String screen = "";
		switch(screenSize) {
			case Configuration.SCREENLAYOUT_SIZE_XLARGE:
		    	screen = "Xtra Large screen";
		    	gridview.setNumColumns(2);
		        break;
		    case Configuration.SCREENLAYOUT_SIZE_LARGE:
		    	screen = "Large screen";
		    	gridview.setNumColumns(2);
		        break;
		    case Configuration.SCREENLAYOUT_SIZE_NORMAL:
		    	screen = "Normal screen";
		    	gridview.setNumColumns(2);
		        break;
		    case Configuration.SCREENLAYOUT_SIZE_SMALL:
		    	screen = "Small screen";
		    	gridview.setNumColumns(2);
		        break;
		    default:
		    	screen = "Screen size is neither large, normal or small";
		}
		//Log.e("Screen Size", screen);
		gridview.setAdapter(adapter);

	    gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
//	            Toast.makeText(context, "" + position, Toast.LENGTH_SHORT).show();
//	        	ProductDetailFragment fragment = new ProductDetailFragment(items
//						.get(position));
	        	ProductDetailFragment fragment = ProductDetailFragment.newInstance(items.get(position));
				FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction()
						.replace(R.id.frame_container, fragment)
						.addToBackStack(null).commit();
	        }
	    });*/
		
		return rootView;
		
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.e(TAG, "OnStart");
		((MainActivity)getActivity()).setTitle(Constants.TITLE_HOME);
		((MainActivity)getActivity()).toggleIconDrawer(true);
		((MainActivity)getActivity()).warpToFragment(Constants.DRAWER_LIST_MAIN, -1);
	}

}
