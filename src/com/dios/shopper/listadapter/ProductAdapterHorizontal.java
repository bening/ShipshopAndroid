package com.dios.shopper.listadapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dios.model.Catalogue;
import com.dios.model.Product;
import com.dios.shopper.R;
import com.dios.shopper.util.Helper;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Picasso.Listener;

public class ProductAdapterHorizontal extends BaseAdapter {
    private Context mContext;
    private ArrayList<Catalogue> items = null;
    private int desiredWidth = -1;
    private int desiredHeight = -1;
    
	// our ViewHolder.
	// caches our TextView
	static class ViewHolderItem {
		FrameLayout wrapper;
		ProgressBar loadingBar;
		ImageView itemImage;
		TextView itemTitle;
        int position;
	}
    
    public ProductAdapterHorizontal(Context c, ArrayList<Catalogue> _items) {
        mContext = c;
        items = _items;
    }
    
    public ProductAdapterHorizontal(Context c, ArrayList<Catalogue> _items, int width, int height) {
        mContext = c;
        items = _items;
        desiredWidth = width;
        desiredHeight = height;
    }

    public int getCount() {
    	return items.size();
    }

    public Object getItem(int position) {
    	if(position>=0 && position<items.size())
    		return items.get(position);
    	else
    		return null;
    }

    public long getItemId(int position) {
    	if(position>=0 && position<items.size())
    		return (items.get(position)).getId();
    	else
    		return -1;
    }

    // create a new ImageView for each item referenced by the Adapter
    @SuppressLint("ViewHolder")
	@Override
    public View getView(final int position, View convertView, ViewGroup parent) {
    	ViewHolderItem viewHolder;
    	
    	if(convertView==null){
    		LayoutInflater mInflater = (LayoutInflater) mContext
    				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
    		convertView = mInflater.inflate(R.layout.product_horizontal, null);
    		viewHolder = new ViewHolderItem();
    		viewHolder.wrapper = (FrameLayout)convertView.findViewById(R.id.procuctWrapper);
    		viewHolder.loadingBar = (ProgressBar)convertView.findViewById(R.id.imageLoading);
    		viewHolder.itemImage = (ImageView)convertView.findViewById(R.id.itemImage);
    		viewHolder.itemTitle = (TextView)convertView.findViewById(R.id.tag);
    		// store the holder with the view.
            convertView.setTag(viewHolder);
    	}else{
    		// just use the viewHolder
            viewHolder = (ViewHolderItem) convertView.getTag();
    	}
    	
        final Catalogue thisItem  = items.get(position);
        viewHolder.itemTitle.setText(thisItem.getName());
        viewHolder.position = position;
        viewHolder.loadingBar.setVisibility(View.VISIBLE);
        
        final ViewHolderItem finalViewHolder = viewHolder;
        Picasso picasso = new Picasso.Builder(mContext).listener(
                new Listener() {

					@Override
					public void onImageLoadFailed(Picasso arg0, Uri arg1,
							Exception arg2) {
						// TODO Auto-generated method stub
						Log.e("Picasso Error", arg2.getMessage());
					}

                }).debugging(true).build();
        picasso
        .load(thisItem.getRepresentativeImageUrl())
        .into(viewHolder.itemImage, new Callback() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				if(finalViewHolder.loadingBar!=null){
					finalViewHolder.loadingBar.setVisibility(View.GONE);
				}
			}
			
			@Override
			public void onError() {
				// TODO Auto-generated method stub
				
			}
		});
        /*if(desiredWidth!=-1 && desiredHeight!=-1){
        	viewHolder.wrapper.setLayoutParams(new FrameLayout.LayoutParams(Helper.getDPValue(mContext, desiredWidth), Helper.getDPValue(mContext, desiredHeight)));
    	}else{
    		int screenSize = mContext.getResources().getConfiguration().screenLayout &
    		        Configuration.SCREENLAYOUT_SIZE_MASK;
    		String screen = "";
    		switch(screenSize) {
    			case Configuration.SCREENLAYOUT_SIZE_XLARGE:
    		    	screen = "Xtra Large screen";
    		    	viewHolder.wrapper.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Helper.getDPValue(mContext, 480)));
    		        break;
    		    case Configuration.SCREENLAYOUT_SIZE_LARGE:
    		    	screen = "Large screen";
    		    	viewHolder.wrapper.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Helper.getDPValue(mContext, 400)));
    		        break;
    		    case Configuration.SCREENLAYOUT_SIZE_NORMAL:
    		    	screen = "Normal screen";
    		    	viewHolder.wrapper.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Helper.getDPValue(mContext, 300)));
    		        break;
    		    case Configuration.SCREENLAYOUT_SIZE_SMALL:
    		    	screen = "Small screen";
    		    	viewHolder.wrapper.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Helper.getDPValue(mContext, 250)));
    		        break;
    		    default:
    		    	screen = "Screen size is neither large, normal or small";
    		}
    		//Log.e("Screen Size", screen);
    	}*/
        //wrapper.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, Helper.getDPValue(mContext, 200)));
        //wrapper.setOrientation(LinearLayout.VERTICAL);
        //wrapper.setBackgroundResource(R.color.White);
        
     // Using an AsyncTask to load the slow images in a background thread
        /*new AsyncTask<ViewHolderItem, Void, Bitmap>() {
            private ViewHolderItem v;

			@Override
            protected Bitmap doInBackground(ViewHolderItem... params) {
                v = params[0];
                //return mFakeImageLoader.getImage();
                String imageUrl = thisItem.getRepresentativeImageUrl();
                Picasso.with(mContext)
                .load(imageUrl)
                .into(v.itemImage, new Callback() {
					
					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						if(v.loadingBar!=null){
							v.loadingBar.setVisibility(View.GONE);
						}
					}
					
					@Override
					public void onError() {
						// TODO Auto-generated method stub
						
					}
				});
                if(thisItem.getRepresentativeIcon()==-1 && null!=thisItem.getRepresentativeImage()){
                	return thisItem.getRepresentativeImage();
                }else if(thisItem.getRepresentativeIcon()!=-1){
                	return BitmapFactory.decodeResource(mContext.getResources(),
                			thisItem.getRepresentativeIcon());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                super.onPostExecute(result);
                if (v.position == position) {
                    // If this item hasn't been recycled already, hide the
                    // progress and set and show the image
                    
                    v.itemImage.setImageBitmap(result);
                    //v.loadingBar.setVisibility(View.INVISIBLE);
                }
            }
        }.execute(viewHolder);*/
        
        return convertView;
    }
}
