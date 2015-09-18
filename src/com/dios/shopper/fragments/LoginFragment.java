package com.dios.shopper.fragments;

import com.dios.model.User;
import com.dios.shopper.MainActivity;
import com.dios.shopper.R;
import com.dios.shopper.global.Constants;
import com.dios.shopper.global.DataSingleton;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

public class LoginFragment extends Fragment {
	private static final String TAG ="LoginFragment";
	private Activity context;
	private String password;
	private String username;
	View rootView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.e(TAG, "OnCreate");
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.e(TAG, "OnStart");
		((MainActivity)getActivity()).setTitle(Constants.TITLE_LOGIN);
		((MainActivity)getActivity()).toggleIconDrawer(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.e(TAG, "OnCreateView");
		rootView = inflater.inflate(R.layout.fragment_login, container, false);
		context = getActivity();
		final EditText usernameBox = (EditText)rootView.findViewById(R.id.username);
		
		final EditText passwordBox = (EditText)rootView.findViewById(R.id.password);
		
		Button loginBtn = (Button)rootView.findViewById(R.id.loginBtn);
		loginBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AsyncTask<Void, Integer, Integer>(){

					private String _username;
					private String _password;
					
					@Override
					protected void onPreExecute() {
						// TODO Auto-generated method stub
						_username = usernameBox.getText().toString();
						_password = passwordBox.getText().toString();
						dismissKeyboard(passwordBox);
					}

					@Override
					protected Integer doInBackground(Void... params) {
						// TODO Auto-generated method stub
						if(_username.length()>0 && _password.length()>0){
							if(DataSingleton.getInstance().account.containsKey(_username)){
								User _user = DataSingleton.getInstance().account.get(_username);
								if(_user.getPassword().equals(_password)){
									//login success
									DataSingleton.getInstance().loggedInUser = _user;
									return Constants.LOGIN_SUCCESS;
								}else{
									//login failed
									return Constants.LOGIN_FAILED;
								}
							}else{
								//account not exists
								return Constants.LOGIN_FAILED;
							}
						}else{
							//data empty
							return Constants.LOGIN_NONE;
						}
						
					}

					@Override
					protected void onPostExecute(Integer result) {
						// TODO Auto-generated method stub
						((MainActivity)getActivity()).handleLogin(result);
					}
					
					
					
				}.execute();
				/*username = usernameBox.getText().toString();
				password = passwordBox.getText().toString();
				dismissKeyboard(passwordBox);
				if(username.length()>0 && password.length()>0){
					if(DataSingleton.getInstance().account.containsKey(username)){
						User _user = DataSingleton.getInstance().account.get(username);
						if(_user.getPassword().equals(password)){
							//login success
							DataSingleton.getInstance().loggedInUser = _user;
							((MainActivity)getActivity()).handleLogin(Constants.LOGIN_SUCCESS);
						}else{
							//login failed
							((MainActivity)getActivity()).handleLogin(Constants.LOGIN_FAILED);
						}
					}else{
						//account not exists
						((MainActivity)getActivity()).handleLogin(Constants.LOGIN_FAILED);
					}
				}else{
					//data empty
					((MainActivity)getActivity()).handleLogin(Constants.LOGIN_NONE);
				}*/
			}
		});
		return rootView;
	}
	
	private void dismissKeyboard(View v){
		InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(((EditText)v).getWindowToken(), 0);
	}
}
