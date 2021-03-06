package com.dios.shopper.fragments;

import java.io.Serializable;
import java.util.ArrayList;

import com.dios.model.Catalogue;
import com.dios.model.Product;
import com.dios.shopper.MainActivity;
import com.dios.shopper.R;
import com.dios.shopper.global.Constants;
import com.dios.shopper.listadapter.ProductAdapter;

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
import android.widget.ListView;

public class AgentFragment extends Fragment {
	
	private static final String TAG ="AgentFragment";
	private Activity context;
	View rootView;
	ListView productsListView;
	private static final String CATALOGUE_KEY = "catalogue_key";
	private Catalogue catalogue;
	
	//private ArrayList<Product> agentProducts = new ArrayList<Product>();
	
	public AgentFragment(){
		
	}
	
	public static final AgentFragment newInstance(Catalogue catalogue){
		AgentFragment fragment = new AgentFragment();
		
		final Bundle bundle = new Bundle();
		bundle.putSerializable(CATALOGUE_KEY, (Serializable) catalogue);
		fragment.setArguments(bundle);
		
		return fragment;
	}
	
	/*public AgentFragment(ArrayList<Product> thisProducts){
		this.agentProducts = thisProducts;
	}*/
	
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
		rootView = inflater.inflate(R.layout.fragment_agent, container, false);
		context = getActivity();
		catalogue = (Catalogue) getArguments().getSerializable(
		        CATALOGUE_KEY);
		final ArrayList<Product> items = new ArrayList<Product>(catalogue.getProducts());
		if(items.size()==0){
			//dummy
			Product p1 = new Product("Baju 1 "+catalogue.getName(), 400000,(long)(400000+(400000*0.3)),R.drawable.image_3);
			Product p2 = new Product("Baju 2 "+catalogue.getName(), 250000,(long)(250000+(250000*0.3)),R.drawable.image_5);
			Product p3 = new Product("Baju 3 "+catalogue.getName(), 300000,(long)(300000+(300000*0.3)),R.drawable.image_14);
			Product p4 = new Product("Baju 4 "+catalogue.getName(), 220000,(long)(220000+(220000*0.3)),R.drawable.image_12);
			Product p5 = new Product("Baju 5 "+catalogue.getName(), 4050000,(long)(4050000+(4050000*0.3)),R.drawable.image_1);
			Product p6 = new Product("Baju 6 "+catalogue.getName(), 2500000,(long)(2500000+(2500000*0.3)),R.drawable.image_9);
			items.add(p1);items.add(p2);items.add(p3);items.add(p4);items.add(p5);items.add(p6);
		}
		
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
		
		return rootView;
		
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.e(TAG, "OnStart");
		((MainActivity)getActivity()).setTitle(catalogue==null?Constants.TITLE_AGENT:catalogue.getName());
		((MainActivity)getActivity()).toggleIconDrawer(true);
	}
	
	
}
