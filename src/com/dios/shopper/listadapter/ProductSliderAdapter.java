package com.dios.shopper.listadapter;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.dios.model.Product;
import com.dios.model.ProductColor;
import com.dios.shopper.R;
import com.dios.shopper.customcomponent.VerticalPager;
import com.dios.shopper.customcomponent.VerticalViewPager;
import com.dios.shopper.customcomponent.ViewPagerDisable;
import com.polites.android.GestureImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Picasso.Listener;

public class ProductSliderAdapter extends PagerAdapter{
	
	private Activity activity;
    private ArrayList<Product> products;
    private LayoutInflater inflater;
	private ListView colorListView;
	//private GestureImageView productImage;
	private ViewGroup container;
	private View viewLayout;
	private VerticalPager viewPagerVertical;
	//private ProductSliderVerticalAdapter adapter;
    
 // constructor
    public ProductSliderAdapter(Activity activity,
            ArrayList<Product> _products) {
        this.activity = activity;
        this.products = _products;
    }
    
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return products.size();
	}

	@Override
	public boolean isViewFromObject(View v, Object obj) {
		// TODO Auto-generated method stub
		return v == ((RelativeLayout) obj);
	}
	
	
	
	@Override
    public Object instantiateItem(ViewGroup container, int position) {
        final Product product = products.get(position);
        Log.e("Slider", "instantiate item at: "+position);
        this.container = container;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        viewLayout = inflater.inflate(R.layout.vp_image_horizontal, container,
                false);
        colorListView = (ListView) viewLayout.findViewById(R.id.colorList);
        viewPagerVertical = (VerticalPager) viewLayout.findViewById(R.id.pager);
        for (int i = 0; i < product.getImageURLs().length; i++) {
        	LayoutParams imageParam = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        	
        	FrameLayout wrapper = new FrameLayout(activity);
        	wrapper.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        	
        	FrameLayout.LayoutParams loadingBarParams = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        	loadingBarParams.gravity = Gravity.CENTER;
        	ProgressBar loadingBar = new ProgressBar(activity, null, android.R.attr.progressBarStyleLarge);
        	loadingBar.setLayoutParams(loadingBarParams);
        	loadingBar.setIndeterminate(true);
        	loadingBar.setVisibility(View.VISIBLE);
        	
        	final ProgressBar _loadingBar = loadingBar;
        	
			GestureImageView _image = new GestureImageView(activity);
			_image.setLayoutParams(imageParam);
			_image.setMinScale(0.1f);
			_image.setMaxScale(10.0f);
			_image.setScaleType(ScaleType.CENTER_CROP);
			_image.setStrict(false);
			
			Picasso picasso = new Picasso.Builder(activity).listener(
	                new Listener() {

						@Override
						public void onImageLoadFailed(Picasso arg0, Uri arg1,
								Exception arg2) {
							// TODO Auto-generated method stub
							Log.e("Picasso Error", arg2.getMessage());
						}

	                }).debugging(true).build();
	        picasso
	        .load(product.getImageURLs()[i])
	        .into(_image, new Callback() {
				
				@Override
				public void onSuccess() {
					// TODO Auto-generated method stub
					if(_loadingBar!=null){
						_loadingBar.setVisibility(View.GONE);
					}
				}
				
				@Override
				public void onError() {
					// TODO Auto-generated method stub
					
				}
			});
			
			wrapper.addView(_image);
			wrapper.addView(loadingBar);
			//viewPagerVertical.addView(wrapper);
		}
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
				//object.setImageResource(colors[index]);
				//product.setImage(((BitmapDrawable)object.getDrawable()).getBitmap());
				//productImage.setBackgroundResource(colors[index]);
			}
		});
        
        if(product.getAvailableColor() != null){
			ArrayList<ProductColor> colors = new ArrayList<ProductColor>();
			for (String colorString : product.getAvailableColor()) {
				colors.add(new ProductColor(colorString, colorString));
			}
			initColor(colors);
		}
        products.set(position, product);
    	((ViewPager) container).addView(viewLayout);
    	
        return viewLayout;
        /*ViewLoader loader = new ViewLoader();
        loader.execute(position);
        View result = null;
        
        try {
			result = loader.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			Log.e("Slider", "InterruptedException");
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			Log.e("Slider", "ExecutionException");
			e.printStackTrace();
		}
        
        return result;*/
        
    }
	
	private class ViewLoader extends AsyncTask<Integer, Void, View>{
		Product product;
		Bitmap image;
		int position;
		ArrayList<Bitmap> productImages = null;
		
		@Override
		protected void onPreExecute() {
			inflater = (LayoutInflater) activity
	                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        viewLayout = inflater.inflate(R.layout.vp_image_horizontal, container,
	                false);
		}

		@Override
		protected View doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			position = params[0];
			Log.e("Slider", "process item at: "+position);
			product = products.get(position);
			if(productImages==null)
				productImages = new ArrayList<Bitmap>();
			productImages.clear();
			if(null!=product.getImage()){
            	image = product.getImage();
            	//Log.e("product image exist", "true");
            }else{
            	int[] images = product.getImages();
            	for (int i = 0; i < images.length; i++) {
            		
					productImages.add(BitmapFactory.decodeResource(activity.getResources(),
                			images[i]));
				}
    			/*if(images.length>0){
    				image = BitmapFactory.decodeResource(activity.getResources(),
                			images[0]);
    			}else{
    				image = null;
    			}*/
            }
			return viewLayout;
		}
		
		@Override
        protected void onPostExecute(View result) {
			//productImage = (GestureImageView) result.findViewById(R.id.productImg);
	        colorListView = (ListView) result.findViewById(R.id.colorList);
	        viewPagerVertical = (VerticalPager) result.findViewById(R.id.pager);
	        for (Bitmap eachImage : productImages) {
	        	LayoutParams imageParam = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				GestureImageView _image = new GestureImageView(activity);
				_image.setLayoutParams(imageParam);
				_image.setMinScale(0.1f);
				_image.setMaxScale(10.0f);
				_image.setScaleType(ScaleType.CENTER_CROP);
				_image.setStrict(false);
				_image.setImageBitmap(eachImage);
				viewPagerVertical.addView(_image);
			}
	        //adapter = new ProductSliderVerticalAdapter(activity, product);
	        //viewPagerVertical.setAdapter(adapter);
	        //viewPagerVertical.setCurrentItem(0);

        	//Log.e("product image is null?("+position+") ", productImage==null?"yes":"no");
	        //final GestureImageView object = (GestureImageView)productImage;
	        //productImage.setImageBitmap(image); 
        	//product.setImage(image);
        	Log.e("is image null?("+position+") ", image==null?"yes":"no");
        	/*productImage.setOnTouchListener(new View.OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					//Log.e("onTouch", ((GestureImageView)v).isZoomed()?"zoomed":"not zoomed");
					((ViewPagerDisable)container).setPagingEnabled(!((GestureImageView)v).isZoomed());
					
					return true;
				}
			});*/
        	
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
					//object.setImageResource(colors[index]);
					//product.setImage(((BitmapDrawable)object.getDrawable()).getBitmap());
					//productImage.setBackgroundResource(colors[index]);
				}
			});
	        
	        if(product.getAvailableColor() != null){
				ArrayList<ProductColor> colors = new ArrayList<ProductColor>();
				for (String colorString : product.getAvailableColor()) {
					colors.add(new ProductColor(colorString, colorString));
				}
				initColor(colors);
			}
	        products.set(position, product);
        	((ViewPager) container).addView(result);
        	notifyDataSetChanged();
        }
		
	}
	
	private void initColor(ArrayList<ProductColor> colorList){
		if(colorList != null && colorListView != null){
			ColorAdapter adapter = new ColorAdapter(activity, colorList, null);
			colorListView.setAdapter(adapter);			
		}
	}
	
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);
  
    }

}
