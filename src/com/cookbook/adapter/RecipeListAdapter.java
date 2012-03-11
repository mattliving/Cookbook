package com.cookbook.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.cookbook.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class RecipeListAdapter extends BaseAdapter{


	        private LinkedList<Map<String, Object>> recipes;

	        private LayoutInflater mInflater;

	        
	        
	        public RecipeListAdapter(LinkedList<Map<String, Object>> recipes, Context context){
	        	
	            this.recipes = recipes;
	
	            mInflater = LayoutInflater.from(context);

	        }
	
	        public int getCount() {
	
	            // TODO Auto-generated method stub
	
	            return recipes.size();
	
	        }
	
	        public Object getItem(int position) {
	
	            // TODO Auto-generated method stub
	
	            return recipes.get(position);
	
	        }
	
	        public long getItemId(int position) {
	            // TODO Auto-generated method stub
	            return position;
	        }


public View getView(int position, View convertView, ViewGroup parent) {
	
// TODO Auto-generated method stub
// A ViewHolder keeps references to children views to avoid unneccessary calls	
// to findViewById() on each row.
	
	            ViewHolder holder;
	
	             
	
	            // When convertView is not null, we can reuse it directly, there is no need
	
	            // to reinflate it. We only inflate a new View when the convertView supplied
	
	            // by ListView is null
	
	             if (convertView == null) {

	                 convertView = mInflater.inflate(R.layout.list_complex, null);
	
	                 // Creates a ViewHolder and store references to the two children views
	
	                 // we want to bind data to.
	
	                 holder = new ViewHolder();
	
	                 holder.v = (TextView) convertView.findViewById(R.id.list_recipe_title);
	
	                 holder.v1 = (TextView) convertView.findViewById(R.id.list_recipe_caption);
	
	                 holder.icon = (ImageView) convertView.findViewById(R.id.avatar);
	
	                 holder.rating = (RatingBar)convertView.findViewById(R.id.small_ratingbar);
	
	                 convertView.setTag(holder);
	
	             }else {

	                 // Get the ViewHolder back to get fast access to the TextView
	
	                 // and the ImageView.
	
	                 holder = (ViewHolder) convertView.getTag();
	
	             }
	
	                // Bind the data with the holder.
	
	              
	
	                holder.v.setText((String) recipes.get(position).get("title"));
	
	                 

	                holder.v1.setText((String) recipes.get(position).get("caption"));
	
	                 
	
	                //holder.icon.setImageResource((Integer)recipes.get(position).get(IMGKEY));
	
	                 
	
	                holder.rating.setRating((Float)recipes.get(position).get("rating"));
	
	                 
	
	                return convertView;
	
	        }
	
	         
	
	        class ViewHolder {
	
	            TextView v;
	
	            TextView v1;
	
	            ImageView icon;
	
	            RatingBar rating;
	
	        }



			
	
	         
	
	    }
	
	 
	



