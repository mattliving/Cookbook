package com.cookbook.adapter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

import com.cookbook.OnlineIngredientList;
import com.cookbook.OnlineRecipeList;

public class OnlineQueryAdapter {
	
	private static final String phpFolder =
		"http://mattliving.com/cookbook/";
	private static final String uname = "CBUSER101";
	private static final String pass = "NJI(bhu8";
	
	/** Run script given by scriptName and return result as a String.
	 * @param scriptName - filename of script
	 * @param args - provides username, password and dynamic query values
	 * */ 
	private String getOnlineData(String scriptName, ArrayList<NameValuePair> args) {
		InputStream inStream = null;
		String result = null;

		// run script and get an InputStream containing the results
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(phpFolder + scriptName);
			httppost.setEntity(new UrlEncodedFormEntity(args));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity recipe = response.getEntity();
			inStream = recipe.getContent();
		}
		catch(Exception e){
			Log.e("log_tag", "Error running php script" + e.toString());
		}

		// convert InputStream to String
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(inStream,"iso-8859-1"),8);
			StringBuilder sBuilder = new StringBuilder();
			sBuilder.append(reader.readLine() + "\n");

			String line="0";
			while ((line = reader.readLine()) != null) {
				sBuilder.append(line + "\n");
			}
			inStream.close();
			result = sBuilder.toString();
			System.out.println(result);
		}
		catch(Exception e){
			Log.e("log_tag", "Error converting php result to string" + e.toString());
		}
		System.out.println(result);

		return result;
	}

	/** create OnlineRecipeList containing given results
	 * 
	 * @param result
	 * @return
	 */
	private OnlineRecipeList createOnlineRecipeList(String result, boolean withRating) {
		if (result.trim().equals("null"))
			return null;
		else {
			OnlineRecipeList onlineRecipeList = new OnlineRecipeList(result, withRating);
			return onlineRecipeList;
		}
	}

	/** create OnlineIngredientList containing given results
	 * 
	 * @param result
	 * @return
	 */
	private OnlineIngredientList createOnlineIngredientList(String result) {
		if (result.trim().equals("null"))
			return null;
		else {
			OnlineIngredientList onlineIngredientList = new OnlineIngredientList(result);
			return onlineIngredientList;
		}
	}
	
	/** Fetch all recipes from online db */
	public OnlineRecipeList fetchAllRecipes() {		
		String result = null;
		
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("uname", uname));
		nameValuePairs.add(new BasicNameValuePair("pass", pass));
		
		result = getOnlineData("getAllRecipes.php", nameValuePairs);
		
		return createOnlineRecipeList(result, false);
	}
	
	/** Fetch recipes from online db for specified author_facebook_ids
	 * @param facebookIds each id must be enclosed in single quotes and separated
	 * by a comma e.g. "'12345678', '56789012', '11112222'"
	 * */
	public OnlineRecipeList fetchRecipesSpecificAuthors(String facebookIds) {		
		String result = null;
		
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("uname", uname));
		nameValuePairs.add(new BasicNameValuePair("pass", pass));
		nameValuePairs.add(new BasicNameValuePair("authorIdList", facebookIds));
		
		result = getOnlineData("getRecipes_SpecificAuthors.php", nameValuePairs);
		
		return createOnlineRecipeList(result, false);
	}
	
	/** Fetch a recipe from online db uniquely specified by author_facebook_id and recipe_id
	*/
	public OnlineRecipeList fetchOneRecipe(String facebookId, long recipeId) {		
		String result = null;
		
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("uname", uname));
		nameValuePairs.add(new BasicNameValuePair("pass", pass));
		nameValuePairs.add(new BasicNameValuePair("authorId", facebookId));
		nameValuePairs.add(new BasicNameValuePair("recipeId", recipeId + ""));
		
		result = getOnlineData("getOneRecipe.php", nameValuePairs);
		
		return createOnlineRecipeList(result, false);
	}

	/** Fetch ingredients for specified recipe */
	public OnlineIngredientList fetchRecipeIngredientsOneRecipe(
			String authorFacebookId, long recipeId) {		
		String result = null;
		
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("uname", uname));
		nameValuePairs.add(new BasicNameValuePair("pass", pass));
		nameValuePairs.add(new BasicNameValuePair("authorId", authorFacebookId));
		nameValuePairs.add(new BasicNameValuePair("recipe_id", "" + recipeId));
		
		result = getOnlineData("getRecipeIngredients_OneRecipe.php", nameValuePairs);

		return createOnlineIngredientList(result);
	}

	/** Fetch all recipes from online db including average rating and 
	 * rating count */
	public OnlineRecipeList fetchAllRecipesWithRatings() {		
		String result = null;
		
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("uname", uname));
		nameValuePairs.add(new BasicNameValuePair("pass", pass));
		
		result = getOnlineData("getAllRecipes_AvgRating.php", nameValuePairs);
		
		return createOnlineRecipeList(result, true);
	}
}
