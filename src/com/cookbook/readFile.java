/**
*@author Giulio Muntoni
*@version 1.0
*/
package com.cookbook;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Vector;

/**
 * Parse a file from an input stream and generates a list of recipes.
 * Useful as an example on how to read files on android.
 * All the files should be in /res/raw/
 * @author Giulio
 *
 */
public class readFile  {

	InputStream fos;
	RecipeList list;
	
	public readFile(){
		
	}

	/**
	 * Read a file from an InputStream, parse it and generate the RecipeList
	 * @param fos the InputStream
	 * @return the RecipeList reference
	 */
	public RecipeList readRecipeData(InputStream fos){
		
		this.fos = fos;
		this.list = new RecipeList();
		InputStreamReader input = new InputStreamReader(fos);
		BufferedReader buffreader = new BufferedReader(input);
		int step = 0;
		String line;
		
		String mName = "";
		String mIngredients = "";
		String mPreparation = "";
		int identifier =0;
		String type= "Main" ;
		int cookingTime =0;
		String season = " ";
		String mRegion = "";
		float mRating =0;
		
		/**
		 * Read every line adding the data to the Recipe class,then add the class to the list
		 */
			try {
				while (( line = buffreader.readLine()) != null) {
					switch (step){
						case 0:
						{
							mName = new String(line);
							step++;
							break;
							
						}
						case 1:
						{
							mIngredients = new String(line);
							step++;
							break;
						}
						case 2:
						{
							mPreparation = new String(line);
							step++;
							break;
						}
						case 3:
						{
							 identifier = Integer.parseInt(line);
							 step++;
							 break;
						}
						case 4:
						{
							 type = new String(line);
							 step++;
							 break;
						}
						case 5:
						{
							 cookingTime = Integer.parseInt(line);
							 step++;
							 break;
						}
						case 6:
						{
							 season = new String(line);
							 step++;
							 break;
						}
						case 7:
						{
							mRegion = new String(line);
							step++;
							break;
						}
						case 8:
						{
							 mRating = Float.parseFloat(line);
							 list.addRecipe(new Recipe(mName,mIngredients,mPreparation,identifier,
				type,cookingTime,season,mRegion,mRating));
							 step++;
							 break;
						}
						case 9:
						{
							
							 step = 0;
							 break;
						}
					}		
				}
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			return this.list;
	
	}
	
	/**
	 * Read Ids from an inpuStream and return a Vector of them
	 * @param fos
	 * @return
	 */
	public Vector<Long> readIDs(FileInputStream fos){
	
	this.fos = fos;
	this.list = new RecipeList();
	InputStreamReader input = new InputStreamReader(fos);
	BufferedReader buffreader = new BufferedReader(input);
	String line;
	Vector<Long> ids = new Vector<Long>();
	
	try {
		while ((line = buffreader.readLine()) != null)
		{
			long i;
			try {
				i = Long.valueOf(line);
			} catch (NumberFormatException e) {
				return ids;
			}
			ids.add(i);
		}
	} catch (IOException e) {
		// TODO Auto-generated catch block
		return ids;
	}
	
	return ids;
	
	}
	
	
	 /**
     * Write the bookmarks ids in the raw file
     * @param list
     */
    public void writeIDs(RecipeList list,FileOutputStream ros)
    {
    	 try {
			OutputStreamWriter output = new OutputStreamWriter(ros);
			BufferedWriter wr = new BufferedWriter(output);
			for (int i =0; i< list.size();i++)
			{
				long j =  list.getRecipe(i).getIdentifier();
				String id = Long.toString(j);
				try {
					wr.write(id);
					wr.write("\n");
					wr.flush();
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			ros.close();
			
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}