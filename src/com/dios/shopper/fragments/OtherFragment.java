package com.dios.shopper.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.dios.shopper.MainActivity;
import com.dios.shopper.R;
import com.dios.shopper.global.Constants;

public class OtherFragment extends Fragment{
	
	private static final String TAG ="OtherFragment";
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
		rootView = inflater.inflate(R.layout.fragment_other, container, false);
		context = getActivity();
		
		
		return rootView;
		
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.e(TAG, "OnStart");
		((MainActivity)getActivity()).setTitle(Constants.TITLE_OTHER);
		((MainActivity)getActivity()).toggleIconDrawer(true);
	}

}
