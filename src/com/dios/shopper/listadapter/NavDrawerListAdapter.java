package com.dios.shopper.listadapter;

import java.util.ArrayList;

import com.dios.shopper.custominterface.SelectionInterface;
import com.dios.shopper.model.DrawerSelection;
import com.dios.shopper.model.NavDrawerItem;
import com.dios.shopper.util.Helper;
import com.dios.shopper.MainActivity;
import com.dios.shopper.R;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;


public class NavDrawerListAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<NavDrawerItem> navDrawerItems;
	private SelectionInterface selectionInterface;

	public NavDrawerListAdapter(Context context,
			ArrayList<NavDrawerItem> navDrawerItems, SelectionInterface selectInterface) {
		this.context = context;
		this.navDrawerItems = navDrawerItems;
		this.selectionInterface = selectInterface;
	}

	@Override
	public int getCount() {
		return navDrawerItems.size();
	}

	@Override
	public Object getItem(int position) {
		return navDrawerItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final NavDrawerItem thisDrawerItem = navDrawerItems.get(position);
		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		
		
		//int height = Helper.getDPValue(context, 50);
		//convertView.setLayoutParams(new ListView.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height));
		if(!thisDrawerItem.isAsTabCell()){
			//normal cell
			convertView = mInflater.inflate(R.layout.list_drawer_menu, null);
			ImageView backBtn = (ImageView)convertView.findViewById(R.id.backBtn);
			if(thisDrawerItem.isAsBackNavigation()){
				backBtn.setVisibility(View.VISIBLE);
			}else{
				backBtn.setVisibility(View.GONE);
			}
			TextView textView = (TextView) convertView
					.findViewById(R.id.slidingmenu_section_title);
			textView.setText(thisDrawerItem.getTitle());
		}else{
			//tab cell
			convertView = mInflater.inflate(R.layout.list_drawer_menu_tab, null);
			final Button menu1 = (Button) convertView.findViewById(R.id.menu1);
			final Button menu2 = (Button) convertView.findViewById(R.id.menu2);
			menu1.setText(thisDrawerItem.getSelection1().getName());
			menu1.setTag(thisDrawerItem.getSelection1().getCode());
			
			menu2.setText(thisDrawerItem.getSelection2().getName());
			menu2.setTag(thisDrawerItem.getSelection2().getCode());
			
			if(thisDrawerItem.getSelection1().isSelected()){
				//menu 1 selected
				menu1.setPressed(true);
				menu2.setPressed(false);
			}else{
				//menu 2 selected
				menu1.setPressed(false);
				menu2.setPressed(true);
			}
			
			menu1.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					menu2.setBackgroundResource(R.drawable.drawer_selection_default);
					menu1.setBackgroundResource(R.drawable.drawer_selection_selected);
					/*
					menu2.setPressed(false);
					thisDrawerItem.getSelection1().setSelected(!thisDrawerItem.getSelection1().isSelected());*/
					if(!thisDrawerItem.getSelection1().isSelected()){
						//trigger only when menu 1 is not selected currently
						if(null!=selectionInterface){
							selectionInterface.onSelectionSelected(thisDrawerItem.getSelection1().getCode());
						}
					}
					
				}
			});
			
			menu2.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					menu1.setBackgroundResource(R.drawable.drawer_selection_default);
					menu2.setBackgroundResource(R.drawable.drawer_selection_selected);
					/*menu1.setPressed(false);
					menu2.setPressed(true);
					thisDrawerItem.getSelection2().setSelected(!thisDrawerItem.getSelection2().isSelected());*/
					if(!thisDrawerItem.getSelection2().isSelected()){
						//trigger only when menu 2 is not selected currently
						if(null!=selectionInterface){
							selectionInterface.onSelectionSelected(thisDrawerItem.getSelection2().getCode());
						}
					}
				}
			});
			
			/*TabHost tabHost = (TabHost)convertView.findViewById(android.R.id.tabhost);
			tabHost.setup();
			TabHost.TabSpec spec=tabHost.newTabSpec("tag1");

			spec.setContent(R.id.tab1);//here you define which tab you want to setup
			spec.setIndicator("Pria");//here you choose the text showed in the tab
			tabHost.addTab(spec);

			spec=tabHost.newTabSpec("tag2");
			spec.setContent(R.id.tab2);
			spec.setIndicator("Wanita");
			tabHost.addTab(spec);*/
			//ArrayList<TabHost.TabSpec> tabColl = new ArrayList<TabHost.TabSpec>();
			/*ArrayList<String> tabNames = thisDrawerItem.getTabCellNameCollection();
			for (String tabName : tabNames) {
				TabSpec tab = tabHost.newTabSpec(tabName);
				tab.setIndicator(tabName);
				tab.setContent(new Intent(context,MainActivity.class));
				//tabColl.add(tab);
				tabHost.addTab(tab);
			}*/
			
			
		}
		

		return convertView;
	}

	

}
