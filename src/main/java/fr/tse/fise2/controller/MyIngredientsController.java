
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
import fr.tse.fise2.prinfo.ApiRequests;
import fr.tse.fise2.prinfo.DatabaseManager;
import fr.tse.fise2.prinfo.FoodLists;
import fr.tse.fise2.prinfo.Ingredients;
import fr.tse.fise2.prinfo.RequestsParser;

//Important
//Pour gerer plusieurs listes dans l'application il faut faire attention
//1)Saisir le nom de la 1ere liste puis appuyer sur create a list
//2)Ajouter les ingredients
//3)On peut creer d'autres listes et ajouter des ingredients.
//	Si on n'a pas selectionne une liste et confirmer, il n'y a pas de bug
//4) Si on selectionne une liste et qu'on confirme, il y a un bug:
//		En effet, le champ de texte dans "Select a list" ne change plus
//		On peut toujours ajouter des ingredients mais il faut selectionner la liste
//		puis appuyer sur confirmer
//		Pour toutes les nouvelles listes qu'on cree, desormais
/**
 * This class is the sub-controller designed to handle the IngredientsList Tab. All the calls received from actions on this tab lead to this class
 *
 */
public class MyIngredientsController {

	//Attributs : le handle du controller principal
	private AppController MainController;

	// Le constructeur sera appele dans AppController en se passant lui-meme en reference
	/**
	 * The contructor of this class takes an instance of AppController since this class is an aggregate from AppController
	 * @param mainController
	 */
	public MyIngredientsController(AppController mainController) {
		super();
		MainController = mainController;
	}
	
	// Toutes les methodes liees au actions de MyIngredients' lists
	
	
	public void RenameListButtonOnClick() {
		
	}
	
	/**
	 * This method allows to ask the food API about some ingredients and it fills a ListView with the search results
	 */
	public void SearchButtonOnClick() {
		System.out.println("bouton search clique");      
		String userRequest = ApiRequests.FoodSearch(MainController.MI_ingredientPane_type_input.getText(), 2);
		ArrayList<String> Parsedlist = RequestsParser.FoodSearchParser(userRequest);
		Parsedlist.forEach(
				el -> MainController.MI_ingredientPane_result_list.getItems().add(el)
				);
		
	}
	
	/**
	 * This method allows to add a certain ingredients to a list from parameters set by the user graphically
	 */
	public void ConfirmAddButtonOnClick() {
		
		//On recupere le nom de la liste selectionne
		String listName=MainController.getListName();

		
		int index=MainController.getAllFoodListsName().indexOf(listName);
		
		System.out.println("bouton confirm clique");
		String ingName = MainController.getMI_ingredientPane_result_list().getSelectionModel().getSelectedItem();
		Float ingQty = Float.valueOf(MainController.getMI_ingredientPane_quantity_input().getText());
		String ingUnit = MainController.getMI_ingredientPane_units_combo().getSelectionModel().getSelectedItem();
		LocalDate ingDate = MainController.getMI_ingredientPane_date().getValue();
		LocalDateTime dateNow = LocalDateTime.now();
		String date = null;
		String consumed;
		date = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(dateNow);
		
		System.out.println("bouton confirm clique");
		
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
			int indexInDB = MainController.getAllFoodLists().get(index).getId();
			int i = DatabaseManager.addIngredient(ingName, ingQty, ingUnit, ingDate, indexInDB);
			Ingredients toAdd = new Ingredients(i,ingName, ingQty, ingUnit, null, ingDate, consumed);
				
			//Ajout des ingredients selectionnes dans la liste selectionnee
			MainController.getAllFoodLists().get(index).getLists().add(toAdd);
			
			
			//Affichage des ingredients ajoutes dans la liste, dans le grand tableau (lorsque l'on lance l'application)
			
			MainController.getMI_Listview().getItems().add(toAdd);
		}
		
	}
		
	
	/**
	 * This method switch the list currently shown	
	 */
	public void ConfirmSelectionListButtonOnClick() {
		//1) On selectionne le champ de texte correspondant au nom de la liste, 
		//	 uniquement si elle est non vide
		
		//Des erreurs avec la ligne d'au-dessous
        //String listName = String.valueOf(MainController.getMI_list_combo().getPromptText());
		
		//Il y a des erreurs pour celui d'en bas aussi :/
		String listName=MainController.getMI_list_combo().getSelectionModel().getSelectedItem();
		//ca marche si on selectionne la liste+confirmer apres avoir cree la liste
		//On met a jour le champ listName de AppController
		MainController.setListName(listName);

		if (listName.isEmpty()==false) {
			// On affiche le nom de la liste au centre
			MainController.getMI_title().setText("Currents contents of: "+listName);

			//  2) On vide le tableau des ingredients au niveau de l'affichage
			MainController.getMI_Listview().getItems().clear();
			
			// Petit test
			System.out.println("clear ok"+listName);
			
			//  3) On recupere l'indice de liste d'ingredient qui a pour nom le champ de texte,
			//     dans la liste AllFoodLists (meme index que pour la liste AllFoodListsName)
			int index=MainController.getAllFoodListsName().indexOf(listName);
			
			//On affiche la date et l'heure de la liste
			String date_hour=MainController.getAllFoodLists().get(index).getDate_hour();
			MainController.getMI_date_hour().setText("Created: "+date_hour);
			
			for(int i=0;i<MainController.getAllFoodLists().get(index).getLists().size();i++)
			{
			//	4) On affiche dans le tableau les ingredients de la liste selectionnee (application)
				MainController.getMI_Listview().getItems().add(MainController.getAllFoodLists().get(index).getLists().get(i));
				System.out.println(MainController.getAllFoodLists().get(index).getLists().get(i).getName());
			}
			
		}
		
		//
		//Probleme: Avec String listName = String.valueOf(MainController.getMI_list_combo().getPromptText());
		//			1)Quand on cree une 2e liste, la 1ere liste redevient vide
		//			2)Quand on cree une 2e liste, ca clear, 
		//			mais quand on change une 3e fois de liste, ca ne clear pas l'affichage
		//			3) 3e liste ok. Le probleme c'est quand on revient sur une liste deja  creee. Le nom reste celui de la liste avant selection d'une ancienne liste
		//			4) Donc recuperer la selection?
		
		
	}
	/**
	 * This method delete a selected ingredient in the list
	 */
	public void DeleteIngButtonOnClick() {
		//On recupere le nom de la liste dans le champ de texte
		//String listName = String.valueOf(MainController.getMI_list_combo().getPromptText());
		String listName=MainController.getListName();
		
		//On recupere l'indice correspondance dans AllFoodListsName
		int index=MainController.getAllFoodListsName().indexOf(listName);
		
		int indiceLigneTableauIngredient=MainController.getMI_Listview().getSelectionModel().getSelectedIndex();
		//Remove from database
		int ingIDInDB = MainController.getAllFoodLists().get(index).getLists()
				.get(indiceLigneTableauIngredient).getId();
		DatabaseManager.deleteIngredient(ingIDInDB);
		//On enleve l'ingredient selectionne de la liste dans AllFoodLists
		MainController.getAllFoodLists().get(index).getLists().remove(indiceLigneTableauIngredient);
		
		//On supprime immediatement l'ingredient selectionne de l'affichage
		MainController.getMI_Listview().getItems()
		.removeAll(MainController.getMI_Listview().getSelectionModel().getSelectedItem());
	}
	
	/**
	 * This method validate the modifications of a an ingredient's spec
	 */
	public void ConfirmModifyButtonOnClick() {
		// On recupere le nom de la liste dans le champ de texte
		// String listName =
		// String.valueOf(MainController.getMI_list_combo().getPromptText());
		String listName = MainController.getListName();

		// On recupere l'indice correspondance dans AllFoodListsName
		int index = MainController.getAllFoodListsName().indexOf(listName);

		int indiceLigneTableauIngredient = MainController.getMI_Listview().getSelectionModel().getSelectedIndex();

		String strQuantity = MainController.getMI_selectedPane_quantity_input().getText();
		LocalDate exp = MainController.getMI_selectedPane_date().getValue();
		String strUnit = MainController.getMP_selectedPane_units_combo().getSelectionModel().getSelectedItem();

		if (strQuantity != null & !strQuantity.trim().isEmpty()) {
			int ingIDInDB = MainController.getAllFoodLists().get(index).getLists()
					.get(indiceLigneTableauIngredient).getId();
			System.out.println(index + "   index");
			System.out.println(indiceLigneTableauIngredient + "   indiceLigneTableauIngredient");
			System.out.println(ingIDInDB + "   ingIDInDB");
			DatabaseManager.updateIngredientQuantity(ingIDInDB,Float.parseFloat(strQuantity));
			MainController.getAllFoodLists().get(index).getLists().get(indiceLigneTableauIngredient)
					.setQuantity(Float.parseFloat(strQuantity));
			MainController.getMI_Listview().getSelectionModel().getSelectedItem()
					.setQuantity(Float.parseFloat(strQuantity));
		}

		if (exp != null) {
			int ingIDInDB = MainController.getAllFoodLists().get(index).getLists()
					.get(indiceLigneTableauIngredient).getId();
			DatabaseManager.updateIngredientExpdate(ingIDInDB,exp);
			MainController.getAllFoodLists().get(index).getLists().get(indiceLigneTableauIngredient)
					.setExpdate(exp);
			MainController.getMI_Listview().getSelectionModel().getSelectedItem()
					.setExpdate(exp);
		}
		
		if(strUnit != null) {
			int ingIDInDB = MainController.getAllFoodLists().get(index).getLists()
					.get(indiceLigneTableauIngredient).getId();
			DatabaseManager.updateIngredientUnit(ingIDInDB,strUnit);
			MainController.getAllFoodLists().get(index).getLists().get(indiceLigneTableauIngredient)
					.setUnits(strUnit);
			MainController.getMI_Listview().getSelectionModel().getSelectedItem()
					.setUnits(strUnit);
		}
		
		MainController.getMI_Listview().refresh();
		
	}
	/**
	 * Instanciates the EditListController
	 */
	public void EditListButtonClick() {
		
		//Des qu'on clique sur le bouton Edit Lists, on cree le controller associe
		MyEditListController myEditListController = new MyEditListController(this.MainController);

        // Show the new stage/window
		myEditListController.showStage();
	    
	}
	
	public void ActualisePeremption() {
		for(int j=0; j<MainController.getAllFoodLists().size();j++) {
			for(int i=0; i<MainController.getAllFoodLists().get(j).getLists().size();i++) {
				LocalDateTime dateNow = LocalDateTime.now();
				String date = null;
				date = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(dateNow);
				LocalDate exp = LocalDate.parse(MainController.getFrigoMain().getLists().get(i).getExpdate().toString());
				LocalDate now = LocalDate.parse(date);
				long diff = ChronoUnit.DAYS.between(now, exp);	
				if (diff<=2) {
					MainController.getAllFoodLists().get(j).getLists().get(i).setConsume("X");
		     	}
				else if (diff<=5) {
					MainController.getAllFoodLists().get(j).getLists().get(i).setConsume("~");
		     	}
				else {
					MainController.getAllFoodLists().get(j).getLists().get(i).setConsume("V");
				}	     	 	
			}
		}
	}
	
	/**
	 * Database method to initialize Lists
	 */
	public void initiateLists() {
		Connection conn = DatabaseManager.getConnection();

		String query = "SELECT * FROM Lists WHERE listID != 1 AND listName != 'Frigo'";

		Statement st = null;
		try {

			// create the java statement
			st = conn.createStatement();
			
			// execute the query, and get a java resultset
			ResultSet rs = st.executeQuery(query);
			
			// iterate through the java resultset
			while (rs.next()) {
				int listID = rs.getInt("listID");
				String listName = rs.getString("listName");
				String date_hour = rs.getString("date_hour");
				
				//Initiate an empty list
				ArrayList<Ingredients> lists = new ArrayList<Ingredients>();
				FoodLists nouvelleListe = new FoodLists(listID, listName, lists, date_hour);

				//On ajoute la liste d'ingrédient à la liste des listes d'ingrédients
				MainController.getAllFoodLists().add(nouvelleListe);

				//On ajoute le nom de la liste nouvellement créée à une liste des noms des listes
				//pour pouvoir l'ajouter au menu déroulant dans la sélection des listes
				ArrayList<String> AllFoodListsName = new ArrayList<String>();
				AllFoodListsName.add(listName);	

				//Mise à jour de la liste entière des noms
				MainController.getAllFoodListsName().add(listName);	
					
				//On ajoute la liste d'ingrédients dans la sélection de liste 
				//(Ainsi on pourra la récupérer, pour ajouter des ingrédients et la gérer)
				MainController.getMI_list_combo().getItems().addAll(AllFoodListsName);
				
				
				
				//Populate the list
				
				String selectQuery = "SELECT ingID, ingName, quantity, units, expdate FROM Ingredients WHERE Ingredients.listID = ?";
				// create the mysql insert preparedstatement
				PreparedStatement preparedStmt = null;
				try {

					preparedStmt = conn.prepareStatement(selectQuery);
					preparedStmt.setInt(1, listID);

					// execute the java preparedstatement
					ResultSet rsIng = preparedStmt.executeQuery();
				    while ( rsIng.next() )
				    {
				    	int ingID = rsIng.getInt("ingID");
				    	String ingName = rsIng.getString("ingName");
				    	Float ingQty = rsIng.getFloat("quantity");
				    	String ingUnit = rsIng.getString("units");
				    	LocalDate ingDate = rsIng.getDate("expdate").toLocalDate();

				    	Ingredients toAdd = new Ingredients(ingID, ingName, ingQty, ingUnit, null, ingDate,null);
				    	//Ajout des ingrÃƒÂ©dients selectionnÃƒÂ©s dans la liste selectionnÃƒÂ©e
				    	int index=MainController.getAllFoodListsName().indexOf(listName);
				    	MainController.getAllFoodLists().get(index).getLists().add(toAdd);

				    	//Test: ÃƒÂ§a affiche bien les ingrÃƒÂ©dients de la liste^^
				    	//for(int i=0;i<MainController.getAllFoodLists().get(index).getLists().size();i++)
				    	//{
				    	//System.out.println(MainController.getAllFoodLists().get(index).getLists().get(i).getName());
				    	//}
				    	//
				    	//Affichage des ingrÃƒÂ©dients ajoutÃƒÂ©s dans la liste, dans le grand tableau (lorsque l'on lance l'application)

				    }
				    rsIng.close();

					// Close statement
					if (preparedStmt != null) {
						preparedStmt.close();
					}
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			rs.close();

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

