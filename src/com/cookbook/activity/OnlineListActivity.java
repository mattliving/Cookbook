package com.cookbook.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.support.v4.app.*;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.cookbook.*;
import com.cookbook.adapter.CookbookDBAdapter;
import com.cookbook.adapter.MainImageAdapter;
import com.cookbook.adapter.OnlineQueryAdapter;
import com.cookbook.adapter.SeparatedListAdapter;
import com.cookbook.facebook.BaseRequestListener;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.android.SessionEvents;
import com.facebook.android.SessionEvents.AuthListener;
import com.facebook.android.SessionEvents.LogoutListener;
import com.facebook.android.SessionStore;

public class OnlineListActivity extends BasicListActivity {
	

	protected OnlineRecipeList onlinelist = new OnlineRecipeList();

	
	
	/** ==================================== MEHODS =======================================*/
	
	 /**
	  * This methods gets all the recipes from the DB using the paramters which the class 
	  * got from the sender Intent
	  * Override it in child classes if you need.
	  */
	 @Override
	 public void getItems()
	 {
		    System.out.printf("Inside get items\n");
		 
		    OnlineQueryAdapter q = new OnlineQueryAdapter();
		    System.out.printf("before function call\n");
		    list = onlinelist.MakeRecipeListFromAllOnline(q);
		    if(list == null) System.out.printf("NULL!!!!!!");

		//list.fetchfilterRecipesSorted(mDbHelper, mealType, cookingDuration, season, country, rating,sortby); 

	 }

	 
}
