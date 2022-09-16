package fr.tse.fise2.prinfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * A independent class constituted only with static methods
 * Each methods represent a different request from the food API
 * The only attribute is the API key
 * Every Methods returns a String in a JSON format
 */
public class ApiRequests {
	
	//Attributs : changer l'apiKey par la votre !
	private static String myapiKey = "0eea2b3d912c49a9a0d605ae36ad1e40";

	/**
	 * Ask the API for ingredients that look like 'name' and return 'howMuch' results.
	 * @param name
	 * @param howMuch
	 * @return
	 */
	public static String FoodSearch(String name, int howMuch) {
			
		String URLingredient = "https://api.spoonacular.com/food/ingredients/search?query=" + name + "&number=" + howMuch + "&sortDirection=desc&apiKey=" + myapiKey;
		System.out.println(URLingredient);
		URL obj;
		StringBuffer response = new StringBuffer();
		
		try {
			obj = new URL(URLingredient);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
	        String inputLine;

	        while ((inputLine = in.readLine()) != null) {
	            response.append(inputLine);
	        }
	        
	        in.close();
	        
	        System.out.println(response.toString());
		} catch (MalformedURLException e) {
			response.append("0 food matched your request");
			e.printStackTrace();
			
		} catch (IOException e) {
			response.append("0 food matched your request");
			e.printStackTrace();
		}       
		
		return response.toString();
	}
	
	/**
	 * Ask the API for recipes that look like 'name' and return 'howMuch' results.
	 * @param name
	 * @param howMuch
	 * @return
	 */
	public static String RecipeSearchByName(String name, int howMuch) {
		
		String URLingredient = "https://api.spoonacular.com/recipes/complexSearch?query=" + name + "&number=" + howMuch + "&sortDirection=desc&apiKey=" + myapiKey;
		URL obj;
		StringBuffer response = new StringBuffer();
		
		try {
			obj = new URL(URLingredient);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
	        String inputLine;

	        while ((inputLine = in.readLine()) != null) {
	            response.append(inputLine);
	        }
	        
	        in.close();
	        
	        System.out.println(response.toString());
		} catch (MalformedURLException e) {
			response.append("0 recipe matched your request");
			e.printStackTrace();
			
		} catch (IOException e) {
			response.append("0 recipe matched your request");
			e.printStackTrace();
		}       
		
		return response.toString();
	}
	
	/**
	 * Ask the API for recipes that fits with 'Fridge' and returns 'howMuch' results
	 * @param Fridge
	 * @param howMuch
	 * @return
	 */
	public static String RecipeSearchByIngredients(FoodLists Fridge, int howMuch) {
		
		StringBuffer FridgeContent = new StringBuffer();
		for(int i=0; i<Fridge.getLists().size(); i++) {
			FridgeContent.append( Fridge.getLists().get(i).getName() + ",+");
		}
		FridgeContent.delete(FridgeContent.length()-2, FridgeContent.length());
		
		String URLingredient = "https://api.spoonacular.com/recipes/findByIngredients?ingredients=" + FridgeContent.toString() + "&number=" + howMuch + "&sortDirection=desc&apiKey=" + myapiKey;
		URL obj;
		StringBuffer response = new StringBuffer();
		
		try {
			obj = new URL(URLingredient);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
	        String inputLine;

	        while ((inputLine = in.readLine()) != null) {
	            response.append(inputLine);
	        }
	        
	        in.close();
	        
	        System.out.println(response.toString());
		} catch (MalformedURLException e) {
			response.append("0 recipe matched your request");
			e.printStackTrace();
			
		} catch (IOException e) {
			response.append("0 recipe matched your request");
			e.printStackTrace();
		}       
		
		return response.toString();
	}
	
	/**
	 * Ask the API for detailed informations on a recipe regarding its 'ID'
	 * @param ID
	 * @return
	 */
	public static String RecipeGetbyID(String ID) {
		
		String URLingredient = "https://api.spoonacular.com/recipes/" + ID + "/information?includeNutrition=true&apiKey=" + myapiKey;
		URL obj;
		StringBuffer response = new StringBuffer();
		
		try {
			obj = new URL(URLingredient);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
	        String inputLine;

	        while ((inputLine = in.readLine()) != null) {
	            response.append(inputLine);
	        }
	        
	        in.close();
	        
	        System.out.println(response.toString());
		} catch (MalformedURLException e) {
			response.append("0 recipe matched your request");
			e.printStackTrace();
			
		} catch (IOException e) {
			response.append("0 recipe matched your request");
			e.printStackTrace();
		}       
		
		return response.toString();
	}
	
	/**
	 * Ask the API for a quantity/units conversion
	 * @param name
	 * @param srcAm
	 * @param srcUn
	 * @param tarUn
	 * @return
	 */
	public static String AmountConverter(String name, String srcAm, String srcUn, String tarUn) {
		String URLtoConv = "https://api.spoonacular.com/recipes/convert?ingredientName=" + name + "&sourceAmount=" + srcAm + "&sourceUnit=" + srcUn + "&targetUnit=" + tarUn + "&apiKey=" + myapiKey;
		URL obj;
		StringBuffer response = new StringBuffer();
		
		try {
			obj = new URL(URLtoConv);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
	        String inputLine;

	        while ((inputLine = in.readLine()) != null) {
	            response.append(inputLine);
	        }
	        
	        in.close();
	        
	        System.out.println(response.toString());
		} catch (MalformedURLException e) {
			response.append("\"targetAmount\": 312.5");
			e.printStackTrace();
			
		} catch (IOException e) {
			response.append("\"targetAmount\": 312.5");
			e.printStackTrace();
		}       
		
		return response.toString();
	}
}
