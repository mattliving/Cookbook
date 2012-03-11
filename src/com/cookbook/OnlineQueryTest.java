package com.cookbook;

import com.cookbook.adapter.OnlineQueryAdapter;

public class OnlineQueryTest {
	
	private OnlineQueryAdapter q = new OnlineQueryAdapter();
	private OnlineRecipeList l;
	private OnlineIngredientList i;

	public void testFetchRecipeIngredientsOneRecipe() {
		// this test should print no results
		System.out.println("testFetchRecipeIngredientsOneRecipe, xNbQg81Tc2GGUabc, 2");
		i = q.fetchRecipeIngredientsOneRecipe("xNbQg81Tc2GGUabc", 2);
		if (i != null)
			i.printAll();
		else
			System.out.println("No results");		
		
		// this test should print 2 ingredients
		System.out.println("testFetchRecipeIngredientsOneRecipe, xNbQg81Tc2GGU, 2");
		i = q.fetchRecipeIngredientsOneRecipe("xNbQg81Tc2GGU", 2);
		if (i != null)
			i.printAll();
		else
			System.out.println("No results");		
	}
	
	public void testFetchRecipesSpecificAuthors() {
		// this test should print one recipe
		System.out.println("testFetchRecipesSpecificAuthors, B8S9VN0s9q9sg");
		l = q.fetchRecipesSpecificAuthors("B8S9VN0s9q9sg");
		if (l != null)
			l.printAll();
		else
			System.out.println("No results");

		// this test should print no results
		System.out.println("testFetchRecipesSpecificAuthors, abc");
		l = q.fetchRecipesSpecificAuthors("abc");
		if (l != null)
			l.printAll();
		else
			System.out.println("No results");
	}
	
	public void testFetchAllRecipesWithRatings() {
		System.out.println("testFetchAllRecipesWithRatings");
		l = q.fetchAllRecipesWithRatings();
		if (l != null)
			l.printAll();
		else
			System.out.println("No results");
	}
	
	public void testFetchOneRecipe() {
		System.out.println("testFetchOneRecipe");
		l = q.fetchOneRecipe("1330374564", 2);
		if (l != null)
			l.printAll();
		else
			System.out.println("No results");
	}
	
	public void testFetchAllRecipes() {
		System.out.println("testFetchAllRecipes");
		l = q.fetchAllRecipes();
		if (l != null)
			l.printAll();
		else
			System.out.println("No results");
	}

	public void testAll() {
		//testFetchAllRecipes(); // working
		testFetchAllRecipesWithRatings(); // removed print statements, should after next upload
		testFetchRecipeIngredientsOneRecipe(); // doesn't seem to getting valid SQL result - should print error after next upload
		testFetchRecipesSpecificAuthors(); // doesn't seem to getting valid SQL result - should print error after next upload
		testFetchOneRecipe();
	}
}
