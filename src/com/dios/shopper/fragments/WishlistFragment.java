package com.dios.shopper.fragments;

import java.util.ArrayList;

import com.dios.model.Catalogue;
import com.dios.model.Product;
import com.dios.shopper.MainActivity;
import com.dios.shopper.R;
import com.dios.shopper.custominterface.ProductDetailInterface;
import com.dios.shopper.global.Constants;
import com.dios.shopper.global.DataSingleton;
import com.dios.shopper.listadapter.ProductOnBagAdapter;
import com.dios.shopper.listadapter.ProductOnWishlistAdapter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

public class WishlistFragment extends Fragment implements ProductDetailInterface{
	private static final String TAG ="WhishlistFragment";
	private Activity context;
	View rootView;
	GridView gridview;
	LinearLayout noProductLayout;
	ProductOnWishlistAdapter adapter;
	ArrayList<Product> items = new ArrayList<Product>();
	Catalogue catalogue = new Catalogue(Constants.CODE_WISHLIST, Constants.TITLE_WISHLIST, Constants.LEVEL_WISHLIST);
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.e(TAG, "OnCreate");
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.e(TAG, "OnStart");
		((MainActivity)getActivity()).setTitle(Constants.TITLE_WISHLIST);
		((MainActivity)getActivity()).toggleIconDrawer(false);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.e(TAG, "OnCreateView");
		rootView = inflater.inflate(R.layout.fragment_wishlist, container, false);
		context = getActivity();
		
		noProductLayout = (LinearLayout) rootView.findViewById(R.id.noProductLayout);
		gridview = (GridView) rootView.findViewById(R.id.gridview);
		items = DataSingleton.getInstance().wishList;
		catalogue.setProducts(items);
		/*new ArrayList<Product>();
		Product p1 = new Product("Baju 1", 400000,R.drawable.image_9);
		Product p2 = new Product("Baju 2", 250000,R.drawable.image_1);
		Product p3 = new Product("Baju 3", 300000,R.drawable.image_5);
		Product p4 = new Product("Baju 4", 220000,R.drawable.image_13);
		items.add(p1);items.add(p2);items.add(p3);items.add(p4);*/
		
		int screenSize = context.getResources().getConfiguration().screenLayout &
		        Configuration.SCREENLAYOUT_SIZE_MASK;
		int width = GridView.LayoutParams.MATCH_PARENT;
		int height = -1;
		String screen = "";
		switch(screenSize) {
			case Configuration.SCREENLAYOUT_SIZE_XLARGE:
		    	screen = "Xtra Large screen";
		    	height = 300;
		    	gridview.setNumColumns(2);
		        break;
		    case Configuration.SCREENLAYOUT_SIZE_LARGE:
		    	screen = "Large screen";
		    	gridview.setNumColumns(2);
		    	height = 300;
		        break;
		    case Configuration.SCREENLAYOUT_SIZE_NORMAL:
		    	screen = "Normal screen";
		    	gridview.setNumColumns(1);
		    	height = 300;
		        break;
		    case Configuration.SCREENLAYOUT_SIZE_SMALL:
		    	screen = "Small screen";
		    	gridview.setNumColumns(1);
		    	height = 225;
		        break;
		    default:
		    	screen = "Screen size is neither large, normal or small";
		}
		
		adapter = new ProductOnWishlistAdapter(context, items, width, height,this);
		
		gridview.setAdapter(adapter);

	    gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	            Fragment fragment = ProductDetailFragment.newInstance(catalogue,position);
				FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction()
						.replace(R.id.frame_container, fragment)
						.addToBackStack(null).commit();
	        }
	    });
	    checkProductNumber();
		return rootView;
	}
	
	public void checkProductNumber(){
		if(items.size()>0){
			noProductLayout.setVisibility(View.GONE);
		}else{
			noProductLayout.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onCloseButtonClick(int position, int productID) {
		// TODO Auto-generated method stub
		//((MainActivity)getActivity()).showToast("click close on "+position);
		items.remove(position);
		if(null!=adapter){
			adapter.notifyDataSetChanged();
		}
		checkProductNumber();

		((MainActivity)getActivity()).updateWishlistCounter();
	}

	@Override
	public void onAddToWishlistClick(int position, int productID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAddToBagClick(int position, int productID) {
		// TODO Auto-generated method stub
		DataSingleton.getInstance().bags.add(items.get(position));
		((MainActivity)getActivity()).updateBagCounter();
		this.onCloseButtonClick(position, productID);
	}

}
