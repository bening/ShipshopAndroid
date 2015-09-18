package com.dios.shopper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.dios.model.Catalogue;
import com.dios.shopper.custominterface.SelectionInterface;
import com.dios.shopper.fragments.AgentFragment;
import com.dios.shopper.fragments.BagFragment;
import com.dios.shopper.fragments.HomeFragment;
import com.dios.shopper.fragments.KategoriFragment;
import com.dios.shopper.fragments.TokoFragment;
import com.dios.shopper.fragments.LoginFragment;
import com.dios.shopper.fragments.OtherFragment;
import com.dios.shopper.fragments.WishlistFragment;
import com.dios.shopper.global.Constants;
import com.dios.shopper.global.DataSingleton;
import com.dios.shopper.listadapter.NavDrawerListAdapter;
import com.dios.shopper.model.DrawerSelection;
import com.dios.shopper.model.NavDrawerItem;
import com.dios.shopper.R;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MainActivity extends Activity implements SelectionInterface {
	private static final String TAG = "MainActivity";
	static final String STATE_LIST_TYPE = "listType";
	static final String STATE_CURRENT_TITLE = "currentTitle";
	private static final int _HOME = 0;
	private static final int _TOKO = 1;
	private static final int _AGENT = 2;
	private static final int _KATEGORI = 3;
	private static final int _OTHER = 4;
	private static final int _LOGIN = 5;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	
	private TextView bagCounter = null;
	private TextView wishCounter = null;
	
	// nav drawer title
	private CharSequence mDrawerTitle;
	
	// used to store app title
	private CharSequence mTitle;
	
	// slide menu items
	private ArrayList<NavDrawerItem> navDrawerItems = new ArrayList<NavDrawerItem>();
	private NavDrawerListAdapter adapter;
	
	// Asyntask
	AsyncTask<Void, Void, Void> loaderTask;
	
	final Context context = this;
	int listType = Constants.DRAWER_LIST_MAIN;
	String[] mainList = {"Home","Toko","Agen","Kategori","Lainnya"};
	/*String[] tokoList = {"Toko 1","Toko 2","Toko 3","Toko 4","Toko 5"};
	String[] agenList = {"Agen A","Agen B","Agen X","Agen Y","Agen Z"};
	String[] kategoriList = {"Kategori 1","Kategori 2","Kategori 3","Kategori 4", "Kategori 5"};*/
	/*private String selectedKota = tokoList[0];
	private String selectedAgent = agenList[0];
	private String selectedKategori = kategoriList[0];*/
	
	private ArrayList<Catalogue> toko = new ArrayList<Catalogue>();
	private ArrayList<Catalogue> agen = new ArrayList<Catalogue>();
	private ArrayList<Catalogue> kategori = new ArrayList<Catalogue>();
	
	private Catalogue selectedToko = null;
	private Catalogue selectedAgen = null;
	private Catalogue selectedKategori = null;
	
	
	//ArrayList<Product> products = new ArrayList<Product>();
	
	HashMap<String, ArrayList<String>> kotaMenu = new HashMap<String, ArrayList<String>>();
	//HashMap<String, ArrayList<Product>> mapCityToAgent  = new HashMap<String, ArrayList<Product>>();
	
	String currentTitle = Constants.TITLE_HOME;
	private ArrayList<DrawerSelection> menuSelections = new ArrayList<DrawerSelection>();
	private DrawerSelection menu1;
	private DrawerSelection menu2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e(TAG, "on create");
		setContentView(R.layout.activity_main);
		mTitle = mDrawerTitle = currentTitle;
		
		initializeSelection();
		readCatalogue();
		//initializeProducts();
		//initializeAgentWithProducts();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
		
		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
		
		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems,this);
		mDrawerList.setAdapter(adapter);
		
		final ActionBar actionBar = getActionBar();
		//actionBar.setCustomView(R.layout.actionbar_custom_view_home);
		//actionBar.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
		actionBar.setDisplayShowTitleEnabled(true);
		//actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setTitle(currentTitle);
		// enabling action bar app icon and behaving it as toggle button
		actionBar.setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		//getActionBar().setHomeAsUpIndicator(R.drawable.button_back);
		getActionBar().setDisplayShowTitleEnabled(true);
		
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, // nav menu toggle icon
				R.string.app_name, // nav drawer open - description for
									// accessibility
				R.string.app_name // nav drawer close - description for
									// accessibility
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(currentTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(getString(R.string.app_name));
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		
		if(savedInstanceState == null){
			// load slide menu items
			listType = Constants.DRAWER_LIST_MAIN;
			currentTitle = Constants.TITLE_HOME; 
			displayView(_HOME,0);
		}else{
			listType = savedInstanceState.getInt(STATE_LIST_TYPE, Constants.DRAWER_LIST_MAIN);
			currentTitle = savedInstanceState.getString(STATE_CURRENT_TITLE, Constants.TITLE_HOME);
		}
		populateListDrawer();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		// Save the user's current game state
	    savedInstanceState.putString(STATE_CURRENT_TITLE, currentTitle);
	    savedInstanceState.putInt(STATE_LIST_TYPE, listType);
	    
	    // Always call the superclass so it can save the view hierarchy state
		super.onSaveInstanceState(savedInstanceState);
	}
	
	private void readCatalogue(){
		toko = DataSingleton.getInstance().toko;
		agen = DataSingleton.getInstance().agen;
		kategori = DataSingleton.getInstance().kategori;
	}
	
	/*private void initializeAgentWithProducts(){
		Random rand = new Random();

	    // nextInt is normally exclusive of the top value,
	    // so add 1 to make it inclusive
		for (int i = 0; i < agenList.length; i++) {
			int maxProductNumber = products.size()-1;
			int minProductNumber = 1;
		    int randomNum = rand.nextInt((maxProductNumber - minProductNumber) + 1) + minProductNumber;
			int productTotal = 5;
			
			ArrayList<Product> agentProducts = new ArrayList<Product>();
			long _seed = System.nanoTime();
			Collections.shuffle(products, new Random(_seed));
			for (int j = 0; j < randomNum; j++) {
				agentProducts.add(products.get(j));
			}
			mapCityToAgent.put(agenList[i], agentProducts);
		}
	}*/
	
	/*private void initializeProducts(){
		Product p1 = new Product("Kebaya Kece", 250000);
		Product p2 = new Product("Kebaya Kondangan", 250000);
		Product p3 = new Product("Gamis Kece", 250000);
		Product p4 = new Product("Gamis Segamis-gamisnya", 250000);
		Product p5 = new Product("Rompi Anti Peluru", 250000);
		Product p6 = new Product("Pelana Kuda", 250000);
		products.add(p1);products.add(p2);products.add(p3);products.add(p4);products.add(p5);products.add(p6);
	}*/
	
	private void initializeSelection(){
		menuSelections = new ArrayList<DrawerSelection>();
		menu1 = new DrawerSelection(Constants.CODE_MAN, "Pria");
		menu2 = new DrawerSelection(Constants.CODE_WOMAN, "Wanita");
		menuSelections.add(menu1);
		menuSelections.add(menu2);
	}
	
	private void populateListDrawer(){
		navDrawerItems.clear();
		
		switch (listType) {
		case Constants.DRAWER_LIST_MAIN:
			if(!menu1.isSelected() && !menu2.isSelected())
				menu1.setSelected(true);
			navDrawerItems.add(new NavDrawerItem("gender", null, false, "0", true, menu1, menu2));
			for (String title : mainList) {
				navDrawerItems.add(new NavDrawerItem(title, null, false, "0"));
			}
			navDrawerItems.add(new NavDrawerItem(DataSingleton.getInstance().loggedInUser==null?"Login":"Logout", null, false, "0"));
			break;
		case Constants.DRAWER_LIST_TOKO:
			navDrawerItems.add(new NavDrawerItem("Menu Utama", null, false, "0",true));
			for (Catalogue eachtoko : toko) {
				navDrawerItems.add(new NavDrawerItem(eachtoko, null, false, "0"));
			}
			/*for (String title : tokoList) {
				navDrawerItems.add(new NavDrawerItem(title, null, false, "0"));
			}*/
			break;
		case Constants.DRAWER_LIST_AGENT:
			navDrawerItems.add(new NavDrawerItem("Menu Utama", null, false, "0",true));
			for (Catalogue eachagen : agen) {
				navDrawerItems.add(new NavDrawerItem(eachagen, null, false, "0"));
			}
			/*for (String title : agenList) {
				navDrawerItems.add(new NavDrawerItem(title, null, false, "0"));
			}*/
			break;
		case Constants.DRAWER_LIST_KATEGORI:
			navDrawerItems.add(new NavDrawerItem("Menu Utama", null, false, "0",true));
			for (Catalogue eachkategori : kategori) {
				navDrawerItems.add(new NavDrawerItem(eachkategori, null, false, "0"));
			}
			/*for (String title : kategoriList) {
				navDrawerItems.add(new NavDrawerItem(title, null, false, "0"));
			}*/
			break;
		default:
			break;
		}
		adapter.notifyDataSetChanged();
	}
	
	private void displayView(int code, int position) {
		// update the main content by replacing fragments
		Fragment fragment = null;
		switch (code) {
		case _HOME:
			fragment = new HomeFragment();
			break;
		case _TOKO:
			fragment = new TokoFragment().newInstance(selectedToko);
			break;
		case _AGENT:
			/*ArrayList<String> givenAgent = kotaMenu.get(selectedKota);
			selectedAgent = givenAgent.get(position-1);*/
			/*ArrayList<Product> agentProducts = mapCityToAgent.get(selectedAgent);*/
			fragment = new AgentFragment().newInstance(selectedAgen);
			break;
		case _KATEGORI:
			fragment = new KategoriFragment().newInstance(selectedKategori);
			break;
		case _OTHER:
			fragment = new OtherFragment();
			break;
		case _LOGIN:
			fragment = new LoginFragment();
			break;	
		default:
			break;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).commit();
			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(currentTitle);
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			// error in creating fragment
			Log.e(TAG, "fragment is null");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		Log.e(TAG, "onCreateOptionsMenu");
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_activity_action, menu);
	    
	    final View bagWithCount = menu.findItem(R.id.action_bag).getActionView();
	    final View wishlistWithCount = menu.findItem(R.id.action_whishlist).getActionView();
	    bagCounter = (TextView) bagWithCount.findViewById(R.id.notif_count);
	    wishCounter = (TextView) wishlistWithCount.findViewById(R.id.notif_count);
	    updateBagCounter();
	    updateWishlistCounter();
	    bagWithCount.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				openBag();
			}
		});
	    wishlistWithCount.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				openWishlist();
			}
		});
	    
	    // Associate searchable configuration with the SearchView
	    SearchManager searchManager =
	            (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	    SearchView searchView =
	             (SearchView) menu.findItem(R.id.action_search).getActionView();
	    searchView.setSearchableInfo(
	             searchManager.getSearchableInfo(getComponentName()));
	    formatSearchView(searchView);
	    
	    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			
			@Override
			public boolean onQueryTextSubmit(String query) {
				// TODO Auto-generated method stub
				showToast("search submitted");
				return true;
			}
			
			@Override
			public boolean onQueryTextChange(String newText) {
				// TODO Auto-generated method stub
				return true;
			}
		});
	    
	    return super.onCreateOptionsMenu(menu);

	}
	
	public void formatSearchView(SearchView searchView){
		/*
	     * Search View Component ID:
	     *  SearchAutoComplete mQueryTextView = (SearchAutoComplete) findViewById(R.id.search_src_text);
			View mSearchEditFrame = findViewById(R.id.search_edit_frame);
			View mSearchPlate = findViewById(R.id.search_plate);
			View mSubmitArea = findViewById(R.id.submit_area);
			View mSubmitButton = findViewById(R.id.search_go_btn);
			ImageView mCloseButton = (ImageView) findViewById(R.id.search_close_btn);
			View mVoiceButton = findViewById(R.id.search_voice_btn);
			ImageView mSearchHintIcon = (ImageView) findViewById(R.id.search_mag_icon);
	     * */
	    
	    int searchAutoCompleteId = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
	    int searchEditFrameId = searchView.getContext().getResources().getIdentifier("android:id/search_edit_frame", null, null);
	    int searchPlateId = searchView.getContext().getResources().getIdentifier("android:id/search_plate", null, null);
	    int submitAreaId = searchView.getContext().getResources().getIdentifier("android:id/submit_area", null, null);
	    int submitButtoId = searchView.getContext().getResources().getIdentifier("android:id/search_go_btn", null, null);
	    int closeButtonId = searchView.getContext().getResources().getIdentifier("android:id/search_close_btn", null, null);
	    int voiceButtonId = searchView.getContext().getResources().getIdentifier("android:id/search_voice_btn", null, null);
	    int searchHintIconId = searchView.getContext().getResources().getIdentifier("android:id/search_mag_icon", null, null);
        
	    /*LinearLayout searchFrame = (LinearLayout) searchView.findViewById(searchEditFrameId);
        searchFrame.setBackgroundColor(Color.parseColor("#33FFFFFF"));*/
	    
	    View searchPlateView = searchView.findViewById(searchPlateId);
	    if (searchPlateView != null) {
	        searchPlateView.setBackgroundColor(Color.parseColor("#33FFFFFF"));
	        //searchPlateView.setBackgroundResource(R.drawable.search_custom);
	    }
        
	    EditText searchAutoComplete = (EditText) searchView.findViewById(searchAutoCompleteId);
        searchAutoComplete.setTextColor(getResources().getColor(R.color.White));
        searchAutoComplete.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        searchAutoComplete.setHintTextColor(getResources().getColor(R.color.DarkGray));
        searchView.setQueryHint(getResources().getString(R.string.search_hint));
        
        ImageView searchHintIcon = (ImageView) searchView.findViewById(searchHintIconId);
	    searchHintIcon.setImageResource(R.drawable.ic_menu_search);
	    //searchHintIcon.setVisibility(View.VISIBLE);
	    //set iconified to false to get search hint icon overridden
        searchView.setIconifiedByDefault(true);
	}
	
	private void modifySearchAutoComplete(SearchView searchView){
		// Accessing the SearchAutoComplete
		int queryTextViewId = getResources().getIdentifier("android:id/search_src_text", null, null);  
		View autoComplete = searchView.findViewById(queryTextViewId);

		Class<?> clazz;
		SpannableStringBuilder stopHint = new SpannableStringBuilder("   ");  
		stopHint.append(getString(R.string.search_hint));

		// Add the icon as an spannable
		Drawable searchIcon = getResources().getDrawable(R.drawable.ic_menu_search);  
		Method textSizeMethod;
		try {
			clazz = Class.forName("android.widget.SearchView$SearchAutoComplete");
			textSizeMethod = clazz.getMethod("getTextSize");
			Float rawTextSize = (Float)textSizeMethod.invoke(autoComplete);  
			int textSize = (int) (rawTextSize * 1.25);  
			searchIcon.setBounds(0, 0, textSize, textSize);  
			stopHint.setSpan(new ImageSpan(searchIcon), 1, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

			// Set the new hint text
			Method setHintMethod = clazz.getMethod("setHint", CharSequence.class);  
			setHintMethod.invoke(autoComplete, stopHint);
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		// Handle presses on the action bar items
		Log.e(TAG, "onOptionsItemSelected");
		if(!mDrawerToggle.isDrawerIndicatorEnabled()){
			toggleIconDrawer(true);
			onBackPressed();
			return true;
		}
		else if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}else{
			switch (item.getItemId()) {
		        case R.id.action_search:
		            //openSearch();
		            return true;
		        case R.id.action_bag:
		        	//showToast("Your Bag");
		        	openBag();
		            return true;
		        case R.id.action_whishlist:
		        	//showToast("Your whishlist");
		        	openWishlist();
		        	return true;
		        default:
		            return super.onOptionsItemSelected(item);
			}
		}
	}
	
	@Override
	public void onBackPressed() {
		Log.e(TAG, "onBackPressed");
		FragmentManager fragmentManager = getFragmentManager();
		//if fragment has no stack entry and the current fragment is not fragment home, redirect to fragment home
		//because when fragment has no stack entry and user tap back button, this activity will be destroyed right away
		//we don't want the app closed unless the user tap back button on home fragment
	    if (fragmentManager.getBackStackEntryCount() == 0 && !currentTitle.equalsIgnoreCase(Constants.TITLE_HOME) ) {
	        displayView(_HOME,0);
	    } else {
	        super.onBackPressed();
	    }
	}
	
	public void continueShopping(View v){
		onBackPressed();
	}
	
	public void openWishlist(){
		
		//if already on wishlist dont re open the fragment
		if(!currentTitle.equalsIgnoreCase(Constants.TITLE_WISHLIST)){
			Fragment fragment = new WishlistFragment();
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
			.replace(R.id.frame_container, fragment)
			.addToBackStack(null).commit();
		}
	}
	
	public void openBag(){
		
		//if already on bag dont re open the fragment
		if(!currentTitle.equalsIgnoreCase(Constants.TITLE_BAG)){
			Fragment fragment = new BagFragment();
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
			.replace(R.id.frame_container, fragment)
			.addToBackStack(null).commit();
		}
	}
	
	/* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		// boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		// menu.findItem(R.id.action_add).setVisible(false);
		return super.onPrepareOptionsMenu(menu);
	}
	
	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		currentTitle = String.valueOf(title);
		getActionBar().setTitle(currentTitle);
	}
	
	
	public CharSequence getActionBarTitle(){
		return getActionBar().getTitle();
	}
	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
	
	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item

			mDrawerList.setItemChecked(position, true);
			switch (listType) {
			case Constants.DRAWER_LIST_MAIN:
				switch (position) {
				case 1:
					//select Home
					displayView(_HOME,position);
					break;
				case 2:
					//select Toko
					mDrawerList.setItemChecked(position, false);
					listType = Constants.DRAWER_LIST_TOKO;
					populateListDrawer();
					break;
				case 3:
					//select Agen
					mDrawerList.setItemChecked(position, false);
					listType = Constants.DRAWER_LIST_AGENT;
					populateListDrawer();
					break;
				case 4:
					//select Kategori
					mDrawerList.setItemChecked(position, false);
					listType = Constants.DRAWER_LIST_KATEGORI;
					populateListDrawer();
					break;
				case 5:
					//select Lainnya
					displayView(_OTHER, position);
					break;
				case 6:
					//select Login
					if(null!=DataSingleton.getInstance().loggedInUser){
						DataSingleton.getInstance().loggedInUser = null;
						listType = Constants.DRAWER_LIST_MAIN;
						populateListDrawer();
						displayView(_HOME,position);
					}else{
						displayView(_LOGIN,position);
					}
					break;
				default:
					break;
				}
				break;
			case Constants.DRAWER_LIST_TOKO:
				switch (position) {
				case 0:
					//back to previous list of menu
					mDrawerList.setItemChecked(position, false);
					listType = Constants.DRAWER_LIST_MAIN;
					populateListDrawer();
					break;
				default:
					selectedToko = toko.get(position-1);
					if(null!=DataSingleton.getInstance().loggedInUser && DataSingleton.getInstance().loggedInUser.getLevel()==Constants.LEVEL_AGENT){
						showToast(getResources().getString(R.string.access_denied, "toko"));
					}else if(null!=DataSingleton.getInstance().loggedInUser && DataSingleton.getInstance().loggedInUser.getLevel()==Constants.LEVEL_TOKO){
						if(DataSingleton.getInstance().loggedInUser.getBelongToCatalogue() == selectedToko.getId()){
							displayView(_TOKO, position);
						}else{
							showToast(getResources().getString(R.string.access_denied, "toko"));
						}
					}
					else{
						displayView(_TOKO, position);
					}
					
					/*selectedKota = tokoList[position-1];
					currentTitle = selectedKota;
					if(kotaMenu.get(selectedKota).size()>0){
						setTitle(currentTitle);
						mDrawerList.setItemChecked(position, false);
						listType = Constants.DRAWER_LIST_AGENT;
						populateListDrawer();
					}else{
						displayView(_TOKO,position);
					}*/
					break;
				}
				break;
			case Constants.DRAWER_LIST_AGENT:
				switch (position) {
				case 0:
					mDrawerList.setItemChecked(position, false);
					listType = Constants.DRAWER_LIST_MAIN;
					populateListDrawer();
					break;
				default:
					selectedAgen = agen.get(position-1);
					if(null!=DataSingleton.getInstance().loggedInUser){
						if(DataSingleton.getInstance().loggedInUser.getLevel()==Constants.LEVEL_AGENT){
							if(DataSingleton.getInstance().loggedInUser.getBelongToCatalogue() == selectedAgen.getId()){
								displayView(_AGENT,position);
							}else{
								showToast(getResources().getString(R.string.access_denied, "agen"));
							}
							
						}else{
							showToast(getResources().getString(R.string.access_denied, "agen"));
						}
					}else{
						showToast(getResources().getString(R.string.access_denied, "agen"));
					}
					
					break;
				}
				break;
			case Constants.DRAWER_LIST_KATEGORI:
				switch (position) {
				case 0:
					mDrawerList.setItemChecked(position, false);
					listType = Constants.DRAWER_LIST_MAIN;
					populateListDrawer();
					break;
				default:
					selectedKategori = kategori.get(position-1);
					displayView(_KATEGORI, position);
					break;
				}
				break;
			default:
				break;
			}
		}
	}
	
	public String getSelectedAgent(){
		return "";//selectedAgent+" - "+selectedKota;
	}
	
	public void showToast(String message){
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}
	
	public void resetActionBar(boolean childAction, int drawerMode)
	{
	    if (childAction) {
	        // [Undocumented?] trick to get up button icon to show
	    	mDrawerToggle.setDrawerIndicatorEnabled(false);
	        getActionBar().setDisplayHomeAsUpEnabled(true);
	    } else {
	    	mDrawerToggle.setDrawerIndicatorEnabled(true);
	    }

	    mDrawerLayout.setDrawerLockMode(drawerMode);
	}
	
	public void updateBagCounter(){
		if (bagCounter == null) return;
		runOnUiThread(new Runnable() {
	        @Override
	        public void run() {
	            if (DataSingleton.getInstance().bags.size() == 0)
	            	bagCounter.setVisibility(View.INVISIBLE);
	            else {
	            	bagCounter.setVisibility(View.VISIBLE);
	            	bagCounter.setText(Integer.toString(DataSingleton.getInstance().bags.size()));
	            }
	        }
	    });
	}
	
	public void updateWishlistCounter(){
		if (wishCounter == null) return;
		runOnUiThread(new Runnable() {
	        @Override
	        public void run() {
	            if (DataSingleton.getInstance().wishList.size() == 0)
	            	wishCounter.setVisibility(View.INVISIBLE);
	            else {
	            	wishCounter.setVisibility(View.VISIBLE);
	            	wishCounter.setText(Integer.toString(DataSingleton.getInstance().wishList.size()));
	            }
	        }
	    });
	}
	
	public void toggleIconDrawer(boolean shouldIcon){
		mDrawerToggle.setDrawerIndicatorEnabled(shouldIcon);
		if(!shouldIcon){
			mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		}else{
			mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
		}
	}

	@Override
	public void onSelectionSelected(int code) {
		// TODO Auto-generated method stub
		if(code==menu1.getCode()){
			showToast(menu1.getName());
			menu1.setSelected(true);
			menu2.setSelected(false);
		}else{
			showToast(menu2.getName());
			menu1.setSelected(false);
			menu2.setSelected(true);
		}
		adapter.notifyDataSetChanged();
	}
	
	public void warpToFragment(int _listtype, int selectedPosition){
		listType = _listtype;
		populateListDrawer();
		mDrawerLayout.closeDrawers();
		if(selectedPosition>0){
			mDrawerList.setItemChecked(selectedPosition, true);
		}else{
			mDrawerList.setItemChecked(0, false);
		}
	}
	
	public void handleLogin(int loginCode){
		switch (loginCode) {
		case Constants.LOGIN_NONE:
			showToast(Constants.LOGIN_MESSAGE_EMPTY);
			break;
		case Constants.LOGIN_FAILED:
			showToast(Constants.LOGIN_MESSAGE_FAILED);
			break;
		case Constants.LOGIN_SUCCESS:
			listType = Constants.DRAWER_LIST_MAIN;
			populateListDrawer();
			displayView(_HOME, 0);
			break;
		default:
			break;
		}
	}
}
