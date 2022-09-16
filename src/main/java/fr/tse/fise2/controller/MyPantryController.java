package fr.tse.fise2.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;

import fr.tse.fise2.prinfo.ApiRequests;
import fr.tse.fise2.prinfo.DatabaseManager;
import fr.tse.fise2.prinfo.Ingredients;
import fr.tse.fise2.prinfo.RequestsParser;

/**
 * The sub controller class that handles the Pantry tab. Calls from this tab are redirected here by the instance of AppController
 *
 */
public class MyPantryController {
	
	//Attributs : le handle du controller principal
	private AppController MainController;
	
	// Le constructeur sera appele dans AppController en se passant lui-meme en reference
	/**
	 * Constructor : instance of AppController as this will be an aggregate of AppController
	 * @param mainController
	 */
	public MyPantryController(AppController mainController) {
		super();
		MainController = mainController;
	}

	// Toutes les methodes liees au actions du Pantry
	/**
	 * Ask the food API for ingredients that resemble to what the user wrote. Shows them in a ListView
	 */
	public void SearchButtonOnclick() {
		MainController.MP_AddPane_ingredientResult_list.getItems().clear();
		
		System.out.println("bouton search clique");      
		String userRequest = ApiRequests.FoodSearch(MainController.MP_AddPane_ingredient_input.getText(), 2);
		ArrayList<String> Parsedlist = RequestsParser.FoodSearchParser(userRequest);
		Parsedlist.forEach(
				el -> MainController.MP_AddPane_ingredientResult_list.getItems().add(el)
				);
	}
	
	/**
	 * Add the selected ingredient to the list and TableViex
	 */
	public void ConfirmSearchButtonOnClick() {
		String ingName = MainController.getMP_AddPane_ingredientResult_list().getSelectionModel().getSelectedItem();
		Float ingQty = Float.valueOf(MainController.getMP_AddPane_quantity_imput().getText());
		String ingUnit = MainController.getMP_AddPane_units_combo().getSelectionModel().getSelectedItem();
		LocalDate ingDate = MainController.getMP_AddPane_ingredient_exp_date().getValue();
		
		LocalDateTime dateNow = LocalDateTime.now();
		String date = null;
		String consumed;
		date = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(dateNow);
		
		System.out.println("bouton confirm cliquÃƒÂ©");
		
		     	LocalDate exp = LocalDate.parse(ingDate.toString());
		     	LocalDate now = LocalDate.parse(date);
		     	
		     	long diff = ChronoUnit.DAYS.between(now, exp);
		     	
		     	consumed="V";
		     	
		     	if (diff<=5) {
		     		consumed="~";
		     	}
		     	
		     	if (diff<=2) {
		     		consumed="X";
		     	}
		     	
		if (ingName!=null && ingQty!=null && ingUnit!=null && ingDate!=null) {
			int i = DatabaseManager.addIngredient(ingName, ingQty, ingUnit, ingDate, 1);
			Ingredients toAdd = new Ingredients(i, ingName, ingQty, ingUnit, null, ingDate, consumed);
			
			MainController.getMP_tableview().getItems().add(toAdd);
			MainController.getFrigoMain().getLists().add(toAdd);
		}
	
	}
	
	/**
	 * Delete the selected ingredient from the Pantry
	 */
	public void DeleteIngredientButtonOnClick() {
		// Remove from database
		int ingIDInDB = MainController.getMP_tableview().getSelectionModel().getSelectedItem().getId();
		DatabaseManager.deleteIngredient(ingIDInDB);
		MainController.getMP_tableview().getItems()
			.removeAll(MainController.getMP_tableview().getSelectionModel().getSelectedItem());
	}
	
	/**
	 * Validates the modification of an ingredient spec
	 */
	public void ConfirmModifyButtonOnClick() {
		String strQuantity = MainController.getMP_selected_quantity().getText();
		LocalDate exp = MainController.getMP_selected_date().getValue();
		String strUnit = MainController.getMP_selected_units_combo().getSelectionModel().getSelectedItem();
		
		if (strQuantity != null & !strQuantity.trim().isEmpty()) {
			int ingIDInDB = MainController.getMP_tableview().getSelectionModel()
					.getSelectedItem().getId();
			DatabaseManager.updateIngredientQuantity(ingIDInDB,Float.parseFloat(strQuantity));
			MainController.getMP_tableview().getSelectionModel()
			.getSelectedItem().setQuantity(Float.parseFloat(strQuantity));
		}
		
		if(exp != null) {
			int ingIDInDB = MainController.getMP_tableview().getSelectionModel()
					.getSelectedItem().getId();
			DatabaseManager.updateIngredientExpdate(ingIDInDB,exp);
			MainController.getMP_tableview().getSelectionModel()
			.getSelectedItem().setExpdate(exp);
		}
		
		if(strUnit != null) {
			int ingIDInDB = MainController.getMP_tableview().getSelectionModel()
					.getSelectedItem().getId();
			DatabaseManager.updateIngredientUnit(ingIDInDB,strUnit);
			MainController.getMP_tableview().getSelectionModel()
			.getSelectedItem().setUnits(strUnit);
		}
		
		
		MainController.getMP_tableview().refresh();
	}
	/*
	public void Consume() {
		
		LocalDateTime dateNow = LocalDateTime.now();
		String date = null;
		date = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(dateNow);
		for(int i = 0;i<MainController.getAllFoodLists().get(index).getLists().size();i++) {
		
		     	LocalDate exp = LocalDate.parse(MainController.getAllFoodLists().get(index).getLists().get(i).getExpdate().toString());
		     	LocalDate now = LocalDate.parse(date);
		     	
		     	long diff = ChronoUnit.DAYS.between(now, exp);

		     	MainController.getAllFoodLists().get(index).getLists().get(i).setConsume("V");
		     	System.out.println(MainController.getAllFoodLists().get(index).getLists().get(i).getConsume());
		     	
		     	if (diff<=5) {
		     		MainController.getAllFoodLists().get(index).getLists().get(i).setConsume("~");
		     	}
		     	
		     	if (diff<=2) {
		     		MainController.getAllFoodLists().get(index).getLists().get(i).setConsume("X");
		     	}
		}
		MainController.MI_Listview.refresh();
		
		
	}*/
	public void SavePantryButtonOnClick() {
		
	}
	
	public void ActualisePeremption() {
		for(int i=0; i<MainController.getFrigoMain().getLists().size();i++) {
			LocalDateTime dateNow = LocalDateTime.now();
			String date = null;
			date = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(dateNow);
			LocalDate exp = LocalDate.parse(MainController.getFrigoMain().getLists().get(i).getExpdate().toString());
			LocalDate now = LocalDate.parse(date);
			long diff = ChronoUnit.DAYS.between(now, exp);	
			if (diff<=2) {
				MainController.getMP_tableview().getItems().get(i).setConsume("X");
	     	}
			else if (diff<=5) {
				MainController.getMP_tableview().getItems().get(i).setConsume("~");
	     	}
			else {
				MainController.getMP_tableview().getItems().get(i).setConsume("V");
			}	     	 	
		}
	}
	
	/**
	 * Database method to initialize the whole pantry
	 */
	public void initiatePantry() {
		Connection conn = DatabaseManager.getConnection();

		String query = "SELECT ingID, ingName, quantity, units, expdate FROM Ingredients WHERE listID = 1";

		Statement st = null;
		try {

			// create the java statement
			st = conn.createStatement();

			// execute the query, and get a java resultset

			ResultSet rsIng = st.executeQuery(query);
			while (rsIng.next()) {
				int ingID = rsIng.getInt("ingID");
				String ingName = rsIng.getString("ingName");
				Float ingQty = rsIng.getFloat("quantity");
				String ingUnit = rsIng.getString("units");
				LocalDate ingDate = rsIng.getDate("expdate").toLocalDate();

				Ingredients toAdd = new Ingredients(ingID, ingName, ingQty, ingUnit, null, ingDate, null);
				MainController.getMP_tableview().getItems().add(toAdd);
				MainController.getFrigoMain().getLists().add(toAdd);
				

			}
			rsIng.close();
			
			// Close statement
			if (st != null) {
				st.close();
			}

			// Close connection
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
