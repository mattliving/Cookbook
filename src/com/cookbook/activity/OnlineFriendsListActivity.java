package com.cookbook.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.cookbook.OnlineRecipeList;
import com.cookbook.*;
import com.cookbook.Recipe;
import com.cookbook.adapter.OnlineQueryAdapter;

public class OnlineFriendsListActivity extends BasicListActivity {
	
	protected OnlineRecipeList onlinelist = new OnlineRecipeList();

	@Override
	public void getItems()
	{ 
		OnlineQueryAdapter q = new OnlineQueryAdapter();
		//System.out.printf("before function call\n");
		list = onlinelist.MakeRecipeListFromFriendOnline(friendId, q);
	} 
	
	/**
	  * 
	  * Finalize the listview initialization and set the onItemClickListener.
	  * Override it in child classes if you need.
	  */
	 public void setOnItemClick()
	 {
			lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (!isEmpty())
				{
					Intent viewIntent = new Intent(view.getContext(), ViewRecipeActivity.class);
	
					String recName = ((TextView)view.findViewById(R.id.list_recipe_title)).getText().toString();
					
					Recipe rec = list.get(recName);
					viewIntent.putExtra("recipeID", rec.getIdentifier());
					viewIntent.putExtra("online", true);
					viewIntent.putExtra("recipeName", rec.getName());
					viewIntent.putExtra("method", rec.getPreparation());
					viewIntent.putExtra("mealType", rec.getType());
					viewIntent.putExtra("duration", rec.getCookingTime());
					startActivity(viewIntent);
				}	    	
			}
		}); 
	 }
}
