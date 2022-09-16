package fr.tse.fise2.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import fr.tse.fise2.prinfo.ApiRequests;
import fr.tse.fise2.prinfo.FoodLists;
import fr.tse.fise2.prinfo.Ingredients;
import fr.tse.fise2.prinfo.RequestsParser;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 * Sub Controller of the Recipe tab. Every calls from this tab are redirected here by the instance of AppController
 *
 */
public class MyRecipesController {

	//Attributs : le handle du controller principal
	private AppController MainController;
	private ArrayList<Ingredients> currRecipe;
	private Integer currServings;
	private ArrayList<String> Liste = new ArrayList<String>();

	// Le constructeur sera appele dans AppController en se passant lui-meme en reference
	/**
	 * Constructor : instance of AppController as parameter since this controller is an aggregate from the AppController
	 * @param mainController
	 */
	public MyRecipesController(AppController mainController) {
		super();
		MainController = mainController;
	}
	
	// Toutes les methodes liees aux actions sur recipes
	/**
	 * Determines what happens on click of search recipe button
	 * Double action : it either does a search from a name or from a ingredient list
	 * Results are shown in a ListView, ready to be picked by the curious user
	 */
	public void SearchButtonOnClick() {
		MainController.MR_managerPane_searchresult_list.getItems().clear();
		if(MainController.MR_managerPane_radioNameSearch.isSelected()) {
			String userRequest = ApiRequests.RecipeSearchByName(MainController.MR_managerPane_search_input.getText(), 2);
			MainController.RecipesMap = RequestsParser.RecipeSearchParser(userRequest);
			MainController.RecipesMap.forEach(
					(k,v) -> MainController.MR_managerPane_searchresult_list.getItems().add(k)
					);
		}
		if(MainController.MR_managerPane_radioListSearch.isSelected()) {
			String listName = MainController.MR_managerPane_list_combo.getSelectionModel().getSelectedItem();
			FoodLists SelectedList = null;
			if (listName.equals("My Pantry")){
				SelectedList = MainController.getFrigoMain();
			}
			else {
				int index=MainController.getAllFoodListsName().indexOf(listName);
				SelectedList = MainController.getAllFoodLists().get(index);
			}
			String userRequest = ApiRequests.RecipeSearchByIngredients(SelectedList,2);
			userRequest = "{\"results\":" + userRequest + "}";
			MainController.RecipesMap = RequestsParser.RecipeSearchParser(userRequest);
			MainController.RecipesMap.forEach(
					(k,v) -> MainController.MR_managerPane_searchresult_list.getItems().add(k)
					);
		}
	}
	/**
	 * Display the recipe that was selected by the user in the ListView previously generated
	 */
	public void DisplayRecipeButtonOnClick() {
		
		//Set visibility to true (Display of recipe)
		MainController.MR_favoriteAdd_button.setVisible(true);
		MainController.MR_recipe_label.setVisible(true);
		MainController.MR_time_label.setVisible(true);
		MainController.MR_recipe_picture.setVisible(true);
		MainController.MR_ingredient_list.setVisible(true);
		MainController.MR_Compare_Button.setVisible(true);
		MainController.MR_compareCombobox.setVisible(true);
		MainController.MR_viewInstructions_button.setVisible(true);
		MainController.MR_serving_label.setVisible(true);
		MainController.MR_serving_number.setVisible(true);
		MainController.MR_EditServing_button.setVisible(true);
		MainController.MR_CaloriesText.setVisible(true);
		MainController.MR_CarbsText.setVisible(true);
		MainController.MR_FatsText.setVisible(true);
		MainController.MR_ProteinsText.setVisible(true);
		MainController.MR_Calories_amount.setVisible(true);
		MainController.MR_Carbs_amount.setVisible(true);
		MainController.MR_Fats_amount.setVisible(true);
		MainController.MR_Proteins_amount.setVisible(true);
		MainController.MR_AddToShoppingList.setVisible(false);
		MainController.MR_SaveShoppingList.setVisible(false);
		MainController.MR_Refresh_ShoppingList.setVisible(false);
		
		//DisplayRecipe
		MainController.MR_ingredient_list.getItems().clear();
		
		String SelectedOne = MainController.MR_managerPane_searchresult_list.getSelectionModel().getSelectedItem();
		MainController.selectedRecipeIndex=MainController.RecipesMap.get(SelectedOne);
		
		String recipeRequest = ApiRequests.RecipeGetbyID(MainController.selectedRecipeIndex.toString());
		String RecipeName = RequestsParser.RecipeResultParser(recipeRequest, "title");
		Integer RecipeTime = RequestsParser.RecipeResultParserInt(recipeRequest, "readyInMinutes");
		Integer RecipeServing = RequestsParser.RecipeResultParserInt(recipeRequest, "servings");
		String RecipePicture = RequestsParser.RecipeResultParser(recipeRequest, "image");
		ArrayList<Ingredients> RecipeIng = RequestsParser.RecipeResultParserIngredients(recipeRequest);
		currRecipe = RecipeIng;
		RecipeIng.forEach(
				el -> MainController.MR_ingredient_list.getItems().add(el)
				);
		
		MainController.MR_recipe_label.setText(RecipeName);
		
		currServings = RecipeServing;
		MainController.MR_serving_number.setText(RecipeServing.toString());
		MainController.MR_time_label.setText((RecipeTime).toString() + " min");
		
		Image recIm = new Image(RecipePicture, true);
		
		ArrayList<Double> Nutrients = RequestsParser.RecipeResultParserNutrition(recipeRequest);
		//Cette deuxieme liste permet de comparer avec les besoins de l'onglet profile
		ArrayList<Double> NutrientsPercent = new ArrayList<Double>();
		if(!(MainController.MN_CaloriesDayValue.getText().equals("0"))) {
			NutrientsPercent.add(Math.floor((Nutrients.get(0)/Double.parseDouble(MainController.MN_CaloriesDayValue.getText()))*10000)/100);
			NutrientsPercent.add(Math.floor((Nutrients.get(1)/Double.parseDouble(MainController.MN_ModerateCarbsFatsValue.getText()))*10000)/100);
			NutrientsPercent.add(Math.floor((Nutrients.get(2)/Double.parseDouble(MainController.MN_ModerateCarbsCarbsValue.getText()))*10000)/100);
			NutrientsPercent.add(Math.floor((Nutrients.get(3)/Double.parseDouble(MainController.MN_ModerateCarbsProtValue.getText()))*10000)/100);
		}
		else {
			NutrientsPercent.add(0.0);
			NutrientsPercent.add(0.0);
			NutrientsPercent.add(0.0);
			NutrientsPercent.add(0.0);
		}
		Tooltip tCal = new Tooltip("This represents " + NutrientsPercent.get(0).toString() + " % of your basic daily needs");
		tCal.setFont(Font.font("Verdana", FontPosture.REGULAR, 40));
		Tooltip tFat = new Tooltip("This represents " + NutrientsPercent.get(1).toString() + " % of your basic daily needs");
		tFat.setFont(Font.font("Verdana", FontPosture.REGULAR, 40));
		Tooltip tCarb = new Tooltip("This represents " + NutrientsPercent.get(2).toString() + " % of your basic daily needs");
		tCarb.setFont(Font.font("Verdana", FontPosture.REGULAR, 40));
		Tooltip tProt = new Tooltip("This represents " + NutrientsPercent.get(3).toString() + " % of your basic daily needs");
		tProt.setFont(Font.font("Verdana", FontPosture.REGULAR, 40));
		
		MainController.MR_Calories_amount.setText(Nutrients.get(0).toString());
		MainController.MR_Calories_amount.setTextFill(Color.web("#096BC8"));
		MainController.MR_Calories_amount.setTooltip(tCal);
		
		MainController.MR_Fats_amount.setText(Nutrients.get(1).toString());
		MainController.MR_Fats_amount.setTextFill(Color.web("#096BC8"));
		MainController.MR_Fats_amount.setTooltip(tFat);
		
		MainController.MR_Carbs_amount.setText(Nutrients.get(2).toString());
		MainController.MR_Carbs_amount.setTextFill(Color.web("#096BC8"));
		MainController.MR_Carbs_amount.setTooltip(tCarb);
		
		MainController.MR_Proteins_amount.setText(Nutrients.get(3).toString());
		MainController.MR_Proteins_amount.setTextFill(Color.web("#096BC8"));
		MainController.MR_Proteins_amount.setTooltip(tProt);
		
		
		MainController.MR_recipe_picture.setImage(recIm);
		if (MainController.FavoriteMap.containsKey(MainController.MR_recipe_label.getText())) {
			MainController.getMR_favoriteAdd_button().setText("-");
		}
		else {
			MainController.getMR_favoriteAdd_button().setText("+");
		}
		RefreshComboList();
		MainController.MR_AddToShoppingList.setDisable(false);
		MainController.MR_AddToShoppingList.setText("Add to shopping list");
	}
	/**
	 * Display a recipe that was previously registered as favorite
	 */
	public void DisplayFavoriteButtonOnClick() {
		MainController.MR_ingredient_list.getItems().clear();
		
		String SelectedOne = MainController.MR_managerPane_favorite_list.getSelectionModel().getSelectedItem();
		MainController.selectedRecipeIndex=MainController.FavoriteMap.get(SelectedOne);
		
		String recipeRequest = ApiRequests.RecipeGetbyID(MainController.selectedRecipeIndex.toString());
		String RecipeName = RequestsParser.RecipeResultParser(recipeRequest, "title");
		Integer RecipeTime = RequestsParser.RecipeResultParserInt(recipeRequest, "readyInMinutes");
		Integer RecipeServing = RequestsParser.RecipeResultParserInt(recipeRequest, "servings");
		String RecipePicture = RequestsParser.RecipeResultParser(recipeRequest, "image");
		ArrayList<Ingredients> RecipeIng = RequestsParser.RecipeResultParserIngredients(recipeRequest);
		currRecipe = RecipeIng;
		RecipeIng.forEach(
				el -> MainController.MR_ingredient_list.getItems().add(el)
				);
		
		MainController.MR_recipe_label.setText(RecipeName);
		
		currServings = RecipeServing;
		MainController.MR_serving_number.setText(RecipeServing.toString());
		MainController.MR_time_label.setText((RecipeTime).toString() + " min");
		
		Image recIm = new Image(RecipePicture, true);
		MainController.MR_recipe_picture.setImage(recIm);
		MainController.getMR_favoriteAdd_button().setText("-");
	}
	/**
	 * Allows the user to register a currently shown recipe to favorites, or remove it if already existent 
	 */
	public void AddtoFavsButtonOnClick() {
		System.out.println("CLICKK");
		if(MainController.getMR_favoriteAdd_button().getText().equals("+")) {
			MainController.MR_managerPane_favorite_list.getItems().add(MainController.MR_recipe_label.getText());
			MainController.FavoriteMap.put(MainController.MR_recipe_label.getText(), MainController.selectedRecipeIndex);
			MainController.getMR_favoriteAdd_button().setText("-");
		}
		else {
			//Supr de la lite
			//Suppr de la map
			MainController.MR_managerPane_favorite_list.getItems().remove(MainController.MR_recipe_label.getText());
			MainController.FavoriteMap.remove(MainController.MR_recipe_label.getText());
			MainController.getMR_favoriteAdd_button().setText("+");
		}
	}
	
	/**
	 * Compare the ingredients needed to craft the recipe with a specified FoodList
	 */
	public void ComparetoListButtonOnClick() {
		
		String listName = MainController.MR_compareCombobox.getSelectionModel().getSelectedItem();
		FoodLists SelectedList = null;
		if (listName.equals("My Pantry")){
			SelectedList = MainController.getFrigoMain();
		}
		else {
			int index=MainController.getAllFoodListsName().indexOf(listName);
			SelectedList = MainController.getAllFoodLists().get(index);
		}
		ArrayList<Integer> ResultComparasion = SelectedList.IngComparaison(currRecipe);
		String newName = "";
		
		System.out.println(ResultComparasion.toString());
		
		for (int i = 0; i < ResultComparasion.size(); i++) {
			
			switch (ResultComparasion.get(i).toString()) {
			case "0":
				newName = "X";
				System.out.println("cet ingredient" + newName);
				break;
			case "1":
				newName = "~";
				System.out.println("cet ingredient " + newName);
				break;
				
			case "2":
				newName = "V";
				System.out.println("cet ingredient " + newName);
				break;

			default:
				newName = "ERR";
				System.out.println("cet ingredient " + newName);
				break;
			}
			MainController.MR_ingredient_list.getItems().get(i).setAvailable(newName);
			MainController.MR_ingredient_list.refresh();
			MainController.MR_AddToShoppingList.setVisible(true);
			MainController.MR_SaveShoppingList.setVisible(true);
			MainController.MR_Refresh_ShoppingList.setVisible(true);
		}		
	}
	
	/**
	 * Actualizes the quantity needed to craft a recipe based on the number of servings specified by the user
	 */
	public void EditServingsAmountsOnClick() {
		if(!MainController.MR_serving_number.getText().trim().equals("")) {
			Integer enteredValue = Integer.valueOf(MainController.MR_serving_number.getText());
			Float coeff = (float) enteredValue / (float) currServings;
			MainController.MR_ingredient_list.getItems().forEach(
					el -> el.setQuantity(el.getQuantity() * coeff)
					);
			MainController.MR_ingredient_list.refresh();
			currRecipe.forEach(
					el -> el.setQuantity(el.getQuantity() * coeff)
					);
			currServings = enteredValue;
		}

	}
	/**
	 * Instanciates the InstructionController class. Refer to its particular JavaDoc for more infos.
	 */
	public void ViewInstructionsOnClick() {
		//Ds qu'on clique sur le bouton Edit Lists, on cre le controller associ
		MyInstructionsController myInstructionsController = new MyInstructionsController(this.MainController);

		// Show the new stage/window
		myInstructionsController.showStage();	
	}
	
	/**
	 * Describes the behavior of GUI component when clicking radio buttons
	 */
	public void RadioSwitchOnClick() {
		if(MainController.MR_managerPane_radioNameSearch.isSelected()) {
			MainController.MR_managerPane_search_input.setDisable(false);
			MainController.MR_managerPane_list_combo.setDisable(true);
		}
		if(MainController.MR_managerPane_radioListSearch.isSelected()) {
			MainController.MR_managerPane_search_input.setDisable(true);
			MainController.MR_managerPane_list_combo.setDisable(false);
		}
		RefreshComboList();
	}
	
	/**
	 * Refresh ComboList on the recipes tab
	 */
	public void RefreshComboList() {
		MainController.MR_managerPane_list_combo.getItems().clear();
		MainController.MR_managerPane_list_combo.getItems().add("My Pantry");
		MainController.MR_managerPane_list_combo.getItems().addAll(MainController.getAllFoodListsName());
		
		MainController.MR_compareCombobox.getItems().clear();
		MainController.MR_compareCombobox.getItems().add("My Pantry");
		MainController.MR_compareCombobox.getItems().addAll(MainController.getAllFoodListsName());
	}

	/**
	 * Add missing ingredients to a list for shopping later on
	 */
	public void AddToShoppingListOnClick() {
		
		String listName = MainController.MR_compareCombobox.getSelectionModel().getSelectedItem();
		FoodLists SelectedList = null;
		Double diff = 0.0;
		Double demande = 0.0;
		Double possession = 0.0;
		int indice =0 ;

		
		if (listName.equals("My Pantry")){
			SelectedList = MainController.getFrigoMain();
		}
		else {
			int index=MainController.getAllFoodListsName().indexOf(listName);
			SelectedList = MainController.getAllFoodLists().get(index);
		}
		System.out.println(MainController.MR_ingredient_list.getItems().get(4).getName());
		
		
		for(int i=0; i<MainController.MR_ingredient_list.getItems().size();i++) {
				
			for(int z =0;z<SelectedList.getLists().size();z++) {
				if (SelectedList.getLists().get(z).getName().toString().equals(MainController.MR_ingredient_list.getItems().get(i).getName().toString())) {
					indice = z;

				}
			}
			
			switch (MainController.MR_ingredient_list.getItems().get(i).getAvailable()) {
			
			
			case "V" :
				System.out.println(MainController.MR_ingredient_list.getItems().get(i).getAvailable());
				System.out.println("L'element est deja present dans la liste");
				System.out.println(MainController.MR_ingredient_list.getItems().get(i).getAvailable());
			break;
				
			case "X" :
				System.out.println(MainController.MR_ingredient_list.getItems().get(i).getAvailable());
				Liste.add(MainController.MR_ingredient_list.getItems().get(i).getName()+" "
						+MainController.MR_ingredient_list.getItems().get(i).getQuantity().toString()+" "
						+MainController.MR_ingredient_list.getItems().get(i).getUnits());
			break;
				
			case "~" :
				System.out.println(MainController.MR_ingredient_list.getItems().get(i).getAvailable());
				demande = (double) (MainController.MR_ingredient_list.getItems().get(i).getQuantity());
				possession = (double) (SelectedList.getLists().get(indice).getQuantity());
				diff = demande - possession;
				Liste.add(MainController.MR_ingredient_list.getItems().get(i).getName()+" "
					+diff+" "
					+MainController.MR_ingredient_list.getItems().get(i).getUnits());
				
			break ;
			
			}
		}
	}


	/**
	 * Initiates a SaveFileDialog to save the shopping list as a PDF file
	 */
	public void SavingPDFListOnClick() {
		try {
			String path = null;
			FileChooser fileChooser = new FileChooser();
		    fileChooser.setTitle("Save to PDF");
		    fileChooser.getExtensionFilters().addAll(new ExtensionFilter("PDF File", "*.pdf"));
		    fileChooser.setInitialFileName("MyShoppingList.pdf");
		    File file = fileChooser.showSaveDialog(null);
		    if (file != null) {
		    	path = file.getAbsolutePath();
		    	System.out.println(path);
		    	Document ShoppingList = new Document();
		    	PdfWriter.getInstance(ShoppingList, new FileOutputStream(path));
		    	ShoppingList.open();
				Paragraph paragraph = new Paragraph("My Shopping List");
				paragraph.setAlignment(Element.ALIGN_CENTER);
	            ShoppingList.add(paragraph);
	            paragraph = new Paragraph("-------------------------------------");
	            paragraph.setAlignment(Element.ALIGN_CENTER);
	            ShoppingList.add(paragraph);
	            for(String str:Liste)  {
	                System.out.println(str);  
	                ShoppingList.add(new Paragraph(" - "+ str));
	             } 
				ShoppingList.close();
		    }
		}
		catch(Exception e) {
			System.out.println("Une erreur s'est produite dans la sauvegarde du pdf");
		}
	}
	
	/**
	 * Clear the shopping list and reset GUI components
	 */
	public void RefreshListOnClick() {
		Liste.clear();
		MainController.MR_AddToShoppingList.setDisable(false);
		MainController.MR_AddToShoppingList.setText("Add to shopping list");
	}
}