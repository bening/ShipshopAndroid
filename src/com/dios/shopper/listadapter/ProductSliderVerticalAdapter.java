package com.dios.shopper.listadapter;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.view.DirectionalViewPager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;

import com.dios.model.Product;
import com.dios.model.ProductColor;
import com.dios.shopper.R;
import com.dios.shopper.customcomponent.VerticalViewPager;
import com.dios.shopper.customcomponent.ViewPagerDisable;
import com.polites.android.GestureImageView;

public class ProductSliderVerticalAdapter extends PagerAdapter{
	
	private Activity activity;
    private LayoutInflater inflater;
	private GestureImageView productImage;
	private ViewGroup container;
	private View viewLayout;
	private Product product;
    
 // constructor
    public ProductSliderVerticalAdapter(Activity _activity,
            Product _product) {
        this.activity = _activity;
        this.product = _product;
    }
    
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return product.getImages().length;
	}

	@Override
	public boolean isViewFromObject(View v, Object obj) {
		// TODO Auto-generated method stub
		return v == ((FrameLayout) obj);
	}
	
	@Override
    public Object instantiateItem(ViewGroup container, int position) {
        //final Product product = products.get(position);
        this.container = container;
        
        ViewLoader loader = new ViewLoader();
        loader.execute(position);
        View result = null;
        
        try {
			result = loader.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return result;
        
    }
	
	private class ViewLoader extends AsyncTask<Integer, Void, View>{
		Bitmap image;
		int position;
		
		@Override
		protected void onPreExecute() {
			inflater = (LayoutInflater) activity
	                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        viewLayout = inflater.inflate(R.layout.vp_image_vertical, container,
	                false);
		}

		@Override
		protected View doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			position = params[0];
			int[] images = product.getImages();
			if(images.length>position){
				image = BitmapFactory.decodeResource(activity.getResources(),
            			images[position]);
			}else{
				image = null;
			}
			return viewLayout;
		}
		
		@Override
        protected void onPostExecute(View result) {
			productImage = (GestureImageView) result.findViewById(R.id.productImg);
	        final GestureImageView object = (GestureImageView)productImage;
        	productImage.setImageBitmap(image); 
        	//product.setImage(image);
        	
        	productImage.setOnTouchListener(new View.OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					//Log.e("onTouch", ((GestureImageView)v).isZoomed()?"zoomed":"not zoomed");
					((VerticalViewPager)container).setPagingEnabled(!((GestureImageView)v).isZoomed());
					
					return true;
				}
			});
        	
	        //images.set(position, image);
        	((ViewPager) container).addView(result);
        }
		
	}
	
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((FrameLayout) object);
  
    }

}
