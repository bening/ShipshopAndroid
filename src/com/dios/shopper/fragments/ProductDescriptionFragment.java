package com.dios.shopper.fragments;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

import com.dios.model.Product;
import com.dios.shopper.MainActivity;
import com.dios.shopper.R;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ProductDescriptionFragment extends Fragment{
	private Product product;
	private View rootView;
	private static final String TAG = "ProductDescription";
	
	public ProductDescriptionFragment(Product product) {
		super();
		this.product = product;
	}

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
		rootView = inflater.inflate(R.layout.product_description, container, false);
		
		TextView productName = (TextView) rootView.findViewById(R.id.productName);
		TextView productPrice = (TextView) rootView.findViewById(R.id.productPrice);
		TextView grosirPrice = (TextView) rootView.findViewById(R.id.grosirPrice);
		TextView availableStock = (TextView) rootView.findViewById(R.id.availableStock);
		TextView productDescription = (TextView) rootView.findViewById(R.id.productDescription);
		
		productName.setText(product.getName());
		productPrice.setText(formatCurrency(String.valueOf(product.getPrice())));
		grosirPrice.setText(formatCurrency(String.valueOf(product.getGrosirPrice())));
		availableStock.setText("" + product.getAvailableStock());
		productDescription.setText(product.getDescription());
		
		return rootView;
	}
		
	String formatCurrency(String amount) {
        final NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("in", "ID"));
        return formatter.format(new BigDecimal(amount));
    }
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.e(TAG, "OnStart");
		((MainActivity)getActivity()).setTitle(product.getName());
		((MainActivity)getActivity()).toggleIconDrawer(false);
	}
}
