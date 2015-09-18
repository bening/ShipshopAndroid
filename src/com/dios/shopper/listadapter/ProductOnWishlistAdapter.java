package com.dios.shopper.listadapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dios.model.Product;
import com.dios.shopper.R;
import com.dios.shopper.custominterface.ProductDetailInterface;
import com.dios.shopper.util.Helper;

public class ProductOnWishlistAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Product> items = null;
    private int desiredWidth = -1;
    private int desiredHeight = -1;
    private ProductDetailInterface closeInterface = null;
    
    public ProductOnWishlistAdapter(Context c, ArrayList<Product> _items, ProductDetailInterface myCloseInterface) {
        mContext = c;
        items = _items;
        this.closeInterface = myCloseInterface;
    }
    
    public ProductOnWishlistAdapter(Context c, ArrayList<Product> _items, int width, int height, ProductDetailInterface myCloseInterface) {
        mContext = c;
        items = _items;
        desiredWidth = width;
        desiredHeight = height;
        this.closeInterface = myCloseInterface;
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
    	LayoutInflater mInflater = (LayoutInflater) mContext
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		convertView = mInflater.inflate(R.layout.product_on_wishlist, null); 
		
        final Product thisItem  = items.get(position);
        FrameLayout wrapper = (FrameLayout)convertView.findViewById(R.id.procuctWrapper);
        if(desiredWidth!=-1 && desiredHeight!=-1){
    		wrapper.setLayoutParams(new GridView.LayoutParams(Helper.getDPValue(mContext, desiredWidth), Helper.getDPValue(mContext, desiredHeight)));
    	}else{
    		int screenSize = mContext.getResources().getConfiguration().screenLayout &
    		        Configuration.SCREENLAYOUT_SIZE_MASK;
    		String screen = "";
    		switch(screenSize) {
    			case Configuration.SCREENLAYOUT_SIZE_XLARGE:
    		    	screen = "Xtra Large screen";
    		    	wrapper.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, Helper.getDPValue(mContext, 300)));
    		        break;
    		    case Configuration.SCREENLAYOUT_SIZE_LARGE:
    		    	screen = "Large screen";
    		    	wrapper.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, Helper.getDPValue(mContext, 300)));
    		        break;
    		    case Configuration.SCREENLAYOUT_SIZE_NORMAL:
    		    	screen = "Normal screen";
    		    	wrapper.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, Helper.getDPValue(mContext, 300)));
    		        break;
    		    case Configuration.SCREENLAYOUT_SIZE_SMALL:
    		    	screen = "Small screen";
    		    	wrapper.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, Helper.getDPValue(mContext, 225)));
    		        break;
    		    default:
    		    	screen = "Screen size is neither large, normal or small";
    		}
    		//Log.e("Screen Size", screen);
    	}
        //wrapper.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, Helper.getDPValue(mContext, 200)));
        
        ImageView itemImage = (ImageView)convertView.findViewById(R.id.itemImage);
        TextView itemName = (TextView)convertView.findViewById(R.id.itemName);
        TextView itemPriceInfo = (TextView)convertView.findViewById(R.id.itemPriceInfo);
        LinearLayout closeArea = (LinearLayout)convertView.findViewById(R.id.closeLayout);
        closeArea.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.e("click", "click close");
				if(null!=closeInterface){
					closeInterface.onCloseButtonClick(position, thisItem.getId());
				}
			}
		});
        
        LinearLayout addToBag = (LinearLayout)convertView.findViewById(R.id.addToBagLayout);
        addToBag.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.e("click", "click add to bag");
				if(null!=closeInterface){
					closeInterface.onAddToBagClick(position, thisItem.getId());
				}
			}
		});
        
        if(thisItem.getIcon()==-1 && null!=thisItem.getImage()){
        	itemImage.setImageBitmap(thisItem.getImage());
        }else if(thisItem.getIcon()!=-1){
        	itemImage.setImageResource(thisItem.getIcon());
        }
        
        if(thisItem.getName().length()>0){
        	itemName.setText(thisItem.getName());
        }
        
        itemPriceInfo.setText(Helper.formatCurrency(String.valueOf((int) thisItem.getPrice())));
        return convertView;
    }
}
