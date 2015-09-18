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
import com.dios.shopper.util.Helper;

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
import android.widget.TextView;

public class BagFragment extends Fragment implements ProductDetailInterface{
	private static final String TAG ="BagFragment";
	private Activity context;
	private int totalPayment;
	View rootView;
	GridView gridview;
	LinearLayout noProductLayout;
	TextView price;
	ProductOnBagAdapter adapter;
	ArrayList<Product> items = new ArrayList<Product>();
	Catalogue catalogue = new Catalogue(Constants.CODE_BAG, Constants.TITLE_BAG, Constants.LEVEL_BAG);
	
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
		((MainActivity)getActivity()).setTitle(Constants.TITLE_BAG);
		((MainActivity)getActivity()).toggleIconDrawer(false);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.e(TAG, "OnCreateView");
		rootView = inflater.inflate(R.layout.fragment_bag, container, false);
		context = getActivity();
		items = DataSingleton.getInstance().bags;
		catalogue.setProducts(items);
		/*new ArrayList<Product>();
		Product p1 = new Product("Baju 1", 400000,R.drawable.image_12);
		Product p2 = new Product("Baju 2", 250000,R.drawable.image_14);
		Product p3 = new Product("Baju 3", 300000,R.drawable.image_5);
		Product p4 = new Product("Baju 4", 220000,R.drawable.image_3);
		items.add(p1);items.add(p2);items.add(p3);items.add(p4);*/
		noProductLayout = (LinearLayout) rootView.findViewById(R.id.noProductLayout);
		gridview = (GridView) rootView.findViewById(R.id.gridview);
		price = (TextView)rootView.findViewById(R.id.price);
		totalPayment = 0;
		for (Product product : items) {
			totalPayment += product.getPrice();
		}
		price.setText(Helper.formatCurrency(""+totalPayment));
		
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
		
		adapter = new ProductOnBagAdapter(context, items, width, height,this);
		
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
	    
	    LinearLayout checkoutBtn = (LinearLayout)rootView.findViewById(R.id.checkoutBtn);
	    checkoutBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				checkout();
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
	
	public void checkout(){
		((MainActivity)getActivity()).showToast("checkout");
	}

	@Override
	public void onCloseButtonClick(int position, int productID) {
		// TODO Auto-generated method stub
		//((MainActivity)getActivity()).showToast("click close on "+position);
		int deletedProductPrice = (int)items.get(position).getPrice();
		items.remove(position);
		if(null!=adapter){
			adapter.notifyDataSetChanged();
		}
		//recalculate total payment
		totalPayment -= deletedProductPrice;
		
		price.setText(Helper.formatCurrency(""+totalPayment));
		checkProductNumber();

		((MainActivity)getActivity()).updateBagCounter();
	}

	@Override
	public void onAddToWishlistClick(int position, int productID) {
		// TODO Auto-generated method stub
		DataSingleton.getInstance().wishList.add(items.get(position));

		((MainActivity)getActivity()).updateWishlistCounter();
		this.onCloseButtonClick(position, productID);
	}

	@Override
	public void onAddToBagClick(int position, int productID) {
		// TODO Auto-generated method stub
		
	}

}
