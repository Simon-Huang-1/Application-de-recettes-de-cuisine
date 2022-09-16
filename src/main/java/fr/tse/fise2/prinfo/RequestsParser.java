package fr.tse.fise2.prinfo;

import java.util.ArrayList;
import java.util.HashMap;
import org.json.*;

/**
 * This class is also populated with static methods. It works in addition to the ApiRequests class
 * The main goal of the class is to extract data from the API responses 
 *
 */
public class RequestsParser {

	/**
	 * Parse an API return into a list of ingredients
	 * @param apiReturned
	 * @return
	 */
	public static ArrayList<String> FoodSearchParser(String apiReturned) {
		
		ArrayList<String> resultsStringList = new ArrayList<String>();
		try {
			JSONObject rawJson = new JSONObject(apiReturned);
			JSONArray resultsArray = rawJson.getJSONArray("results");
			
			System.out.println(resultsArray.toString());
			
			for(int i=0; i< resultsArray.length();i++) {
				resultsStringList.add(resultsArray.getJSONObject(i).getString("name"));
			}
		} catch (JSONException e) {
			resultsStringList.add("JsonError");
			e.printStackTrace();
		}
		return resultsStringList;
	}
	
	/**
	 * Parse an API return into a map of recipe/id
	 * @param apiReturned
	 * @return
	 */
	public static HashMap<String, Integer> RecipeSearchParser(String apiReturned) {
		
		Integer value = null;
		String key = null;
		HashMap<String, Integer> ParsedResult = new HashMap<String, Integer>();
		
		try {
			JSONObject rawJson = new JSONObject(apiReturned);
			JSONArray resultsArray = rawJson.getJSONArray("results");
			for(int i=0; i< resultsArray.length();i++) {
				value = resultsArray.getJSONObject(i).getInt("id");
				key = resultsArray.getJSONObject(i).getString("title");
				ParsedResult.put(key, value);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return ParsedResult;
	
	}
	
	/**
	 * Parse an API return into a recipe informations
	 * @param apiReturned
	 * @param field
	 * @return
	 */
	public static String RecipeResultParser(String apiReturned, String field) {
		
		String resultsString;
		try {
			JSONObject rawJson = new JSONObject(apiReturned);
			resultsString = rawJson.getString(field);
	
		} catch (JSONException e) {
			resultsString = "JsonError";
			e.printStackTrace();
		}
		return resultsString;
	}
	
	/**
	 * Parse an API return to extract an specific information as Integer
	 * @param apiReturned
	 * @param field
	 * @return
	 */
	public static Integer RecipeResultParserInt(String apiReturned, String field) {
		
		Integer resultsString;
		try {
			JSONObject rawJson = new JSONObject(apiReturned);
			resultsString = rawJson.getInt(field);
	
		} catch (JSONException e) {
			resultsString = -1;
			e.printStackTrace();
		}
		return resultsString;
	}

	/**
	 * Parse an API return into a list of ingredients
	 * @param apiReturned
	 * @return
	 */
	public static ArrayList<Ingredients> RecipeResultParserIngredients(String apiReturned) {
		ArrayList<Ingredients> results = new ArrayList<Ingredients>();
		
		String ingName;
		Float ingQty;
		String ingUnit;
		
		try {
			JSONObject rawJson = new JSONObject(apiReturned);
			JSONArray resultsArray = rawJson.getJSONArray("extendedIngredients");
			
			for(int i=0; i< resultsArray.length();i++) {
				ingName = resultsArray.getJSONObject(i).getString("name");
				ingQty = (float)resultsArray.getJSONObject(i).getJSONObject("measures").getJSONObject("metric").getDouble("amount");
				ingUnit = resultsArray.getJSONObject(i).getJSONObject("measures").getJSONObject("metric").getString("unitShort");
				Ingredients toAdd = new Ingredients(0, ingName, ingQty, ingUnit, null, null,"");
				results.add(toAdd);
			}
	
		} catch (JSONException e) {
			Ingredients Error = new Ingredients(0, "Erreur", (float) 0.0, "ERR","ERR", null,"");
			results.add(Error);
			e.printStackTrace();
		}
		return results;
	}
	
	/**
	 * Parse an API return into a list of double, to extract nutrition informations
	 * @param apiReturned
	 * @return
	 */
	public static ArrayList<Double> RecipeResultParserNutrition(String apiReturned) {
		ArrayList<Double> nutrimentList = new ArrayList<Double>();
		
		try {
			JSONObject rawJson = new JSONObject(apiReturned);
			JSONObject resultsArray = rawJson.getJSONObject("nutrition");
			nutrimentList.add(resultsArray.getJSONArray("nutrients").getJSONObject(0).getDouble("amount")); //cal
			nutrimentList.add(resultsArray.getJSONArray("nutrients").getJSONObject(1).getDouble("amount")); //fat
			nutrimentList.add(resultsArray.getJSONArray("nutrients").getJSONObject(3).getDouble("amount")); //carbs
			nutrimentList.add(resultsArray.getJSONArray("nutrients").getJSONObject(9).getDouble("amount")); //prots
			}
		catch (JSONException e) {
			nutrimentList.add(-1.0);
			e.printStackTrace();
		}
		
		return nutrimentList;
		
	}
	
	/**
	 * Parse an API return into a Double, from a units conversion
	 * @param apiReturned
	 * @return
	 */
	public static Double AmountConverterParser(String apiReturned) {
		Double TargetAmount= 0.0;
		try {
			JSONObject rawJson = new JSONObject(apiReturned);
			TargetAmount = rawJson.getDouble("targetAmount");
		}
		catch (Exception e) {
			TargetAmount = 0.0;
			e.printStackTrace();
		}
		
		
		return TargetAmount;
	}
}
