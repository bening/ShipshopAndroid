package com.dios.shopper.fragments;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.dios.model.Catalogue;
import com.dios.model.Product;
import com.dios.shopper.MainActivity;
import com.dios.shopper.R;
import com.dios.shopper.customcomponent.ViewPagerDisable;
import com.dios.shopper.global.Constants;
import com.dios.shopper.global.DataSingleton;
import com.dios.shopper.listadapter.ProductSliderAdapter;
//import android.support.v4.widget.DrawerLayout;

public class ProductDetailFragment extends Fragment {
	
	private Product product;
	private static final String TAG = "ProductDetail";
	View rootView;
	//private ListView colorListView;
	//private ImageView productImage;
	private TextView productName;
	private TextView productPrice;
	private ImageView wishListButton;	 
	private ProductSliderAdapter adapter;
	private ViewPagerDisable viewPager;
	private Catalogue catalogue;
	
	private static final String EXTRA_ID = "product_id";
	private static final String EXTRA_PRODUCT_NAME = "name";
	private static final String EXTRA_PRODUCT_PRICE = "price";
	private static final String EXTRA_PRODUCT_TAG = "tag";
	private static final String EXTRA_PRODUCT_MARK = "mark";
	private static final String EXTRA_PRODUCT_ICON = "icon";
	private static final String EXTRA_PRODUCT_IMAGE = "image";
	private static final String EXTRA_PRODUCT_DESCRIPTION = "description";
	private static final String EXTRA_PRODUCT_AVAILABLE_STOCK = "available_stock";
	private static final String EXTRA_PRODUCT_GROSIR_PRICE = "grosir_price";
	private static final String EXTRA_PRODUCT_AVAILABLE_COLOR = "available_color";	
	private static final String EXTRA_CATALOGUE_INDEX = "index";
	
	public ProductDetailFragment() {
		// TODO Auto-generated constructor stub
	}

	public static final ProductDetailFragment newInstance(Catalogue catalogue, int index){
		Log.e(TAG, "newInstance");
		ProductDetailFragment fragment = new ProductDetailFragment();
		
		final Bundle bundle = new Bundle();
		bundle.putSerializable(Constants.CATALOGUE_KEY, (Serializable) catalogue);
		bundle.putInt(EXTRA_CATALOGUE_INDEX, index);
		fragment.setArguments(bundle);
		
		/*Product _product = catalogue.getProducts().get(index);
		final Bundle args = new Bundle();
		args.putInt(EXTRA_ID, _product.getId());
		args.putString(EXTRA_PRODUCT_NAME, _product.getName());
		args.putLong(EXTRA_PRODUCT_PRICE, _product.getPrice());
		args.putString(EXTRA_PRODUCT_TAG, _product.getTag());
		args.putString(EXTRA_PRODUCT_MARK, _product.getMark());
		args.putInt(EXTRA_PRODUCT_ICON, _product.getIcon());
		args.putString(EXTRA_PRODUCT_IMAGE, null);
		args.putString(EXTRA_PRODUCT_DESCRIPTION, _product.getDescription());
		args.putInt(EXTRA_PRODUCT_AVAILABLE_STOCK, _product.getAvailableStock());
		args.putLong(EXTRA_PRODUCT_GROSIR_PRICE, _product.getGrosirPrice());
		args.putStringArrayList(EXTRA_PRODUCT_AVAILABLE_COLOR, _product.getAvailableColor());
		fragment.setArguments(args);*/
		
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.e(TAG, "OnCreate");
		/*product = new Product();
		product.setId(getArguments().getInt(EXTRA_ID));				
		product.setName(getArguments().getString(EXTRA_PRODUCT_NAME));
		product.setPrice(getArguments().getLong(EXTRA_PRODUCT_PRICE));
		product.setTag(getArguments().getString(EXTRA_PRODUCT_TAG));
		product.setMark(getArguments().getString(EXTRA_PRODUCT_MARK));
		product.setIcon(getArguments().getInt(EXTRA_PRODUCT_ICON));
		product.setImage(null);
		product.setDescription(getArguments().getString(EXTRA_PRODUCT_DESCRIPTION));
		product.setAvailableStock(getArguments().getInt(EXTRA_PRODUCT_AVAILABLE_STOCK));
		product.setGrosirPrice(getArguments().getLong(EXTRA_PRODUCT_GROSIR_PRICE));		
		product.setAvailableColorList(getArguments().getStringArrayList(EXTRA_PRODUCT_AVAILABLE_COLOR));*/
		catalogue = (Catalogue) getArguments().getSerializable(
		        Constants.CATALOGUE_KEY);
		int index = getArguments().getInt(EXTRA_CATALOGUE_INDEX);
		product = catalogue.getProducts().get(index);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.e(TAG, "OnCreateView");
		/*((MainActivity)getActivity()).resetActionBar(true,
			      DrawerLayout.LOCK_MODE_LOCKED_CLOSED);*/
		int index = getArguments().getInt(EXTRA_CATALOGUE_INDEX);
		rootView = inflater.inflate(R.layout.product_detail, container, false);
		
		productName = (TextView) rootView.findViewById(R.id.productName);
		productPrice = (TextView) rootView.findViewById(R.id.prductPrice);
		ImageView shareButton = (ImageView) rootView.findViewById(R.id.shareButton);
		ImageView detailButton = (ImageView) rootView.findViewById(R.id.detailButton);
		ImageView sizeButton = (ImageView) rootView.findViewById(R.id.sizeButton);
		ImageView addButton = (ImageView) rootView.findViewById(R.id.addButton);
		//productImage = (ImageView) rootView.findViewById(R.id.productImg);
		wishListButton = (ImageView) rootView.findViewById(R.id.wishListButton);
		viewPager = (ViewPagerDisable) rootView.findViewById(R.id.pager);
		
		ArrayList<Product> products = new ArrayList<Product>();
		for (Product product : catalogue.getProducts()) {
			Product newProduct = new Product();
			newProduct.setId(product.getId());
			newProduct.setName(product.getName());
			newProduct.setPrice(product.getPrice());
			newProduct.setTag(product.getTag());
			newProduct.setMark(product.getMark());
			newProduct.setIcon(product.getIcon());
			newProduct.setImage(product.getImage());
			newProduct.setDescription(product.getDescription());
			newProduct.setAvailableStock(product.getAvailableStock());
			newProduct.setGrosirPrice(product.getGrosirPrice());
			newProduct.setAvailableColorList(product.getAvailableColor());
			newProduct.setImages(product.getImages());
			newProduct.setImageURL(product.getImageURL());
			newProduct.setImageURLs(product.getImageURLs());
			products.add(newProduct);
		}
		
		adapter = new ProductSliderAdapter(getActivity(), products);
		viewPager.setAdapter(adapter);
		viewPager.setCurrentItem(index);
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				product = catalogue.getProducts().get(position);
				productName.setText(product.getName());
				productPrice.setText(formatCurrency(String.valueOf(product.getPrice())));
				((MainActivity)getActivity()).setTitle(product.getName());
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		productName.setText(product.getName());
		productPrice.setText(formatCurrency(String.valueOf(product.getPrice())));
		
		shareButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		detailButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Fragment fragment = new ProductDescriptionFragment(product);
				FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction()
						.replace(R.id.frame_container, fragment)
						.addToBackStack(null).commit();	
			}
		});
		
		wishListButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				v.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.anim_image_click));
				DataSingleton.getInstance().wishList.add(product);
				((MainActivity)getActivity()).updateWishlistCounter();
			}
		});
		
		sizeButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		addButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DataSingleton.getInstance().bags.add(product);
				((MainActivity)getActivity()).updateBagCounter();
			}
		});
		
		/*colorListView = (ListView) rootView.findViewById(R.id.colorList);		
		
		colorListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				String color = product.getAvailableColor().get(position);
				int[] colors = {R.drawable.image_1, R.drawable.image_2, R.drawable.image_3, R.drawable.image_4};
				int index = 0;
				
				if(color.equalsIgnoreCase("blue")){
					index = 3;
				}
				else if(color.equalsIgnoreCase("white")){
					index = 1;
				}
				else if(color.equalsIgnoreCase("red")){
					index = 2;
				}
				productImage.setImageResource(colors[index]);
				//productImage.setBackgroundResource(colors[index]);
			}
		});
			
		if(product.getAvailableColor() != null){
			ArrayList<ProductColor> colors = new ArrayList<ProductColor>();
			for (String colorString : product.getAvailableColor()) {
				colors.add(new ProductColor(colorString, colorString));
			}
			initColor(colors);
		}		*/
		
		return rootView;
		
	}
	
	/*private void initColor(ArrayList<ProductColor> colorList){
		if(colorList != null && colorListView != null){
			ColorAdapter adapter = new ColorAdapter(getActivity(), colorList, null);
			colorListView.setAdapter(adapter);			
		}
	}	*/	
	
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
