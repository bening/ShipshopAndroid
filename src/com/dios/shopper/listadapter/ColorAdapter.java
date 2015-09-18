package com.dios.shopper.listadapter;

import java.util.ArrayList;

import com.dios.model.ProductColor;
import com.dios.shopper.R;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ColorAdapter extends BaseAdapter{

	private Activity context;
	private ArrayList<ProductColor> colorList;
	private LayoutInflater mInflater;	
	
	public ColorAdapter(Activity context, ArrayList<ProductColor> colorList,
			LayoutInflater mInflater) {
		super();
		this.context = context;
		this.colorList = colorList;
		this.mInflater = mInflater;
		mInflater = this.context.getLayoutInflater();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return colorList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return colorList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			LayoutInflater mInflater = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(
					R.layout.color_item, null);
		}		
		
		LinearLayout color = (LinearLayout) convertView.findViewById(R.id.color);
		TextView colorName = (TextView) convertView.findViewById(R.id.colorName);
		String colorString = colorList.get(position) != null ? colorList.get(position).getColorString() : null;
		if(colorString != null){
			color.setBackgroundColor(Color.parseColor(colorString));
		}
		
		return convertView;
	}
	
}
