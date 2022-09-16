package fr.tse.fise2.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import fr.tse.fise2.prinfo.Ingredients;
import fr.tse.fise2.prinfo.DatabaseManager;
import fr.tse.fise2.prinfo.FoodLists;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

/**
 * This class is the principal Controller of the FXML file. It contains all the elements from the GUI
 * The class also have the Model role, since it contains attributes that store important data (Food lists, Pantry...)
 */

public class AppController
	{
		//Declaration d'attributs commun atoute l'interface
		private FoodLists FrigoMain = new FoodLists(1, "Frigo", new ArrayList<Ingredients>(), null); //le contenu  la liste principale : frigo
		private ArrayList<Integer> FavsRecipesID = new ArrayList<Integer>();	//Pour les favoris : chaque resultat est associe a 1 ID dans ce tableau
		private ArrayList<FoodLists> AllFoodLists = new ArrayList<FoodLists>();	//Toutes les listes crees dans l'onglet Lists seront stockees ici
	    private ArrayList<String> AllFoodListsName = new ArrayList<String>(); //Une liste des noms des listes pour la comboBox
	    public Integer selectedRecipeIndex;
	    public HashMap<String, Integer> FavoriteMap = new HashMap<String, Integer>();
	    public HashMap<String, Integer> RecipesMap = new HashMap<String, Integer>();
		
		//Declaration des sub-controllers
		private MyPantryController PantryTabController = new MyPantryController(this);
		private MyIngredientsController IngredientsTabController = new MyIngredientsController(this);
		private MyRecipesController RecipesTabController = new MyRecipesController(this);
		private MyProfileController ProfileTabController = new MyProfileController(this);

		//Nom de la liste selectionnee pour regler les soucis lies a la selection box
		private String listName;
		
		//-----Declaration des attributs de AppController = les elements du FXML
		//Premier Onglet : my pantry

	    @FXML public Tab my_pantry_TAB;
	    @FXML public TableView<Ingredients> MP_tableview;
			@FXML public TableColumn<Ingredients, String> MP_tableview_NAME;
			@FXML public TableColumn<Ingredients, Float> MP_tableview_QUANTITY;
			@FXML public TableColumn<Ingredients, String> MP_tableview_UNITS;
			@FXML public TableColumn<Ingredients, LocalDate> MP_tableview_EXPDATE;
			@FXML public TableColumn<Ingredients, LocalDate> MP_tableview_CONSUME;
		@FXML public TitledPane MP_selected_titlePane;
			@FXML public Button MP_selected_Delete;
			@FXML public TextField MP_selected_quantity;
			@FXML public ComboBox<String> MP_selected_units_combo;
			@FXML public DatePicker MP_selected_date;
			@FXML public Button MP_selected_confirm;
		@FXML public TitledPane MP_AddPane;
			@FXML public Button MP_AddPane_searchButton;
			@FXML public TextField MP_AddPane_ingredient_input;
			@FXML public ListView<String> MP_AddPane_ingredientResult_list;
			@FXML public TextField MP_AddPane_quantity_imput;
			@FXML public ComboBox<String> MP_AddPane_units_combo;
			@FXML public DatePicker MP_AddPane_ingredient_exp_date;
			@FXML public Button MP_AddPane_confirm_button;
			@FXML public Button MP_savePantry_button;
	    
	    //Deuxieme Onglet : My lists
			
		@FXML public Tab my_ingredients_TAB;
		@FXML public TableView<Ingredients> MI_Listview;
			@FXML public TableColumn<Ingredients, String> MI_Listview_NAME;
			@FXML public TableColumn<Ingredients, Float> MI_Listview_QUANTITY;
			@FXML public TableColumn<Ingredients, String> MI_Listview_UNITS;
			@FXML public TableColumn<Ingredients, LocalDate> MI_Listview_EXPDATE;
			@FXML public TableColumn<Ingredients, LocalDate> MI_Listview_CONSUME;
		@FXML public TitledPane MI_selectedPane;
			@FXML public Button MI_selectedPane_delete_button;
			@FXML public TextField MI_selectedPane_quantity_input;
			@FXML public ComboBox<String> MP_selectedPane_units_combo;
			@FXML DatePicker MI_selectedPane_date;
			@FXML public Button MI_selectedPane_confirm_button;
		@FXML public TitledPane MI_ingredientPane;
			@FXML public Button MI_ingredientPane_search_button;
			@FXML public TextField MI_ingredientPane_type_input;
			@FXML public ListView<String> MI_ingredientPane_result_list;
			@FXML public TextField MI_ingredientPane_quantity_input;
			@FXML public ComboBox<String> MI_ingredientPane_units_combo;
			@FXML public DatePicker MI_ingredientPane_date;
			@FXML public Button MI_ingredientPane_confirm_button;
			@FXML public Label MI_title;
			@FXML public Label MI_date_hour;
		@FXML public ComboBox<String> MI_list_combo;
		@FXML public Button MI_confirm_button;
		@FXML public TitledPane MI_managementPane;
			@FXML public TextField MI_managementPane_newname_input;
			@FXML public Button MI_managementPane_create_button;
			@FXML public TextField MI_managementPane_rename_input;
			@FXML public Button MI_managementPane_rename_button;
			@FXML public ToggleButton MI_managementPane_security_toggle;
			@FXML public Button MI_managementPane_delete_button;
			@FXML public Button MI_Edit_Button;
			
		//Troisieme onglet : my recipes
			
			@FXML public Tab my_recipes_TAB;
			@FXML public TitledPane MR_managerPane;
				@FXML public TextField MR_managerPane_search_input;
				@FXML public Button MR_managerPane_namesearch_button;
				@FXML public ListView<String> MR_managerPane_searchresult_list;
				@FXML public Button MR_managerPane_displaysearch_button;
				@FXML public ListView<String> MR_managerPane_favorite_list;
				@FXML public Button MR_managerPane_favoritedisplay_button;
				@FXML public ComboBox<String> MR_managerPane_list_combo;
				@FXML public Button MR_managerPane_listsearch_button;
			@FXML public Button MR_favoriteAdd_button;
			@FXML public Button MR_managerPane_removeFav_button;
			@FXML public Label MR_recipe_label;
			@FXML public Label MR_time_label;
			@FXML public ImageView MR_recipe_picture;
			@FXML public TableView<Ingredients> MR_ingredient_list;
				@FXML public TableColumn<Ingredients, String> MR_ingredient_list_NAME;
				@FXML public TableColumn<Ingredients, Float>MR_ingredient_list_QUANTITY;
				@FXML public TableColumn<Ingredients, String> MR_ingredient_list_UNITS;
				@FXML public TableColumn<Ingredients, String> MR_ingredient_list_AVAILABILITY;
			@FXML public Button MR_Compare_Button;
			@FXML public ComboBox<String> MR_compareCombobox;
			@FXML public Button MR_AddToShoppingList;
			@FXML public Button MR_SaveShoppingList;
			@FXML public Button MR_Refresh_ShoppingList;
			@FXML public TitledPane MR_FavsPane;
			@FXML public Label MR_Favs_text;
			@FXML public Button MR_viewInstructions_button;
			@FXML public Label MR_serving_label;
			@FXML public TextField MR_serving_number;
			@FXML public Button MR_EditServing_button;
			@FXML public Label MR_CaloriesText;
			@FXML public Label MR_CarbsText;
			@FXML public Label MR_FatsText;
			@FXML public Label MR_ProteinsText;
			@FXML public Label MR_Calories_amount;
			@FXML public Label MR_Carbs_amount;
			@FXML public Label MR_Fats_amount;
			@FXML public Label MR_Proteins_amount;
			@FXML public RadioButton MR_managerPane_radioNameSearch;
			@FXML public RadioButton MR_managerPane_radioListSearch;
				
		//Quatrieme onglet : profil
			//MN_ProfilPane
			@FXML public ImageView MN_ProfilePane_profile_picture;
			@FXML public TextField MN_ProfilePane_name_input;
			@FXML public TextField MN_ProfilePane_age_input;
			@FXML public TextField MN_ProfilePane_size_input;
			@FXML public TextField MN_ProfilePane_weight_input;
			@FXML public ComboBox<String> MN_ProfilePane_gender_combo;
			@FXML public ComboBox<String> MN_ProfilePane_activity_combo;
			@FXML public Label MN_ProfilePane_BMI_Value;
			@FXML public Button MN_ProfilePane_Button_Calculate;
			
			//MN_caloriesDay_Pane
			@FXML public Label MN_CaloriesDayValue;
			
			//MN_caloriesWeek_Pane
			@FXML public Label MN_CaloriesWeekValue;
			
			//MN_Nutrients_Pane
				//MN_ModerateCarbs
			@FXML public Label MN_ModerateCarbsProtValue;
			@FXML public Label MN_ModerateCarbsFatsValue;
			@FXML public Label MN_ModerateCarbsCarbsValue;
				//MN_LowerCarbs
			@FXML public Label MN_LowerCarbsProtValue;
			@FXML public Label MN_LowerCarbsFatsValue;
			@FXML public Label MN_LowerCarbsCarbsValue;
				
				//MN_HigherCarbs
			@FXML public Label MN_HigherCarbsProtValue;
			@FXML public Label MN_HigherCarbsFatsValue;
			@FXML public Label MN_HigherCarbsCarbsValue;
				
			
			//MN_Calories_per_activity_Pane
			@FXML public Label MN_Calories_amount1;
			@FXML public Label MN_Calories_amount2;
			@FXML public Label MN_Calories_amount3;
			@FXML public Label MN_Calories_amount4;
			@FXML public Label MN_Calories_amount5;
			@FXML public Label MN_Calories_amount6;

				
				
		//Les getters pour avoir les Handles de certains objets de la GUI et des attributs de AppController

			public FoodLists getFrigoMain() {
				return FrigoMain;
			}
		
			public ArrayList<Integer> getFavsRecipesID() {
				return FavsRecipesID;
			}
			
			public void setFavsRecipesID(ArrayList<Integer> a) {
				FavsRecipesID = a;
			}

			public ArrayList<FoodLists> getAllFoodLists() {
				return AllFoodLists;
			}

			public ArrayList<String> getAllFoodListsName() {
				return AllFoodListsName;
			}


			public MyPantryController getPantryTabController() {
				return PantryTabController;
			}

			public MyIngredientsController getIngredientsTabController() {
				return IngredientsTabController;
			}

			public MyRecipesController getRecipesTabController() {
				return RecipesTabController;
			}

			public String getListName() {
				return listName;
			}

			public void setListName(String listName) {
				this.listName = listName;
			}


			public Tab getMy_pantry_TAB() {
				return my_pantry_TAB;
			}

			public TableView<Ingredients> getMP_tableview() {
				return MP_tableview;
			}

			public TableColumn<Ingredients, String> getMP_tableview_NAME() {
				return MP_tableview_NAME;
			}

			public TableColumn<Ingredients, Float> getMP_tableview_QUANTITY() {
				return MP_tableview_QUANTITY;
			}

			public TableColumn<Ingredients, String> getMP_tableview_UNITS() {
				return MP_tableview_UNITS;
			}

			public TableColumn<Ingredients, LocalDate> getMP_tableview_EXPDATE() {
				return MP_tableview_EXPDATE;
			}

			public TitledPane getMP_selected_titlePane() {
				return MP_selected_titlePane;
			}

			public Button getMP_selected_Delete() {
				return MP_selected_Delete;
			}

			public TextField getMP_selected_quantity() {
				return MP_selected_quantity;
			}
			
			public ComboBox<String> getMP_selected_units_combo() {
				return MP_selected_units_combo;
			}

			public DatePicker getMP_selected_date() {
				return MP_selected_date;
			}

			public Button getMP_selected_confirm() {
				return MP_selected_confirm;
			}

			public TitledPane getMP_AddPane() {
				return MP_AddPane;
			}

			public Button getMP_AddPane_searchButton() {
				return MP_AddPane_searchButton;
			}

			public TextField getMP_AddPane_ingredient_input() {
				return MP_AddPane_ingredient_input;
			}

			public ListView<String> getMP_AddPane_ingredientResult_list() {
				return MP_AddPane_ingredientResult_list;
			}

			public TextField getMP_AddPane_quantity_imput() {
				return MP_AddPane_quantity_imput;
			}

			public ComboBox<String> getMP_AddPane_units_combo() {
				return MP_AddPane_units_combo;
			}

			public DatePicker getMP_AddPane_ingredient_exp_date() {
				return MP_AddPane_ingredient_exp_date;
			}

			public Button getMP_AddPane_confirm_button() {
				return MP_AddPane_confirm_button;
			}

			public Button getMP_savePantry_button() {
				return MP_savePantry_button;
			}

			public Tab getMy_ingredients_TAB() {
				return my_ingredients_TAB;
			}

			public TableView<Ingredients> getMI_Listview() {
				return MI_Listview;
			}

			public TableColumn<Ingredients, String> getMI_Listview_NAME() {
				return MI_Listview_NAME;
			}

			public TableColumn<Ingredients, Float> getMI_Listview_QUANTITY() {
				return MI_Listview_QUANTITY;
			}

			public TableColumn<Ingredients, String> getMI_Listview_UNITS() {
				return MI_Listview_UNITS;
			}
			
			public TableColumn<Ingredients, LocalDate> getMI_tableview_CONSUME() {
				return MI_Listview_CONSUME;
			}


			public TableColumn<Ingredients, LocalDate> getMI_Listview_EXPDATE() {
				return MI_Listview_EXPDATE;
			}


			public TitledPane getMI_selectedPane() {
				return MI_selectedPane;
			}

			public TextField getMI_selectedPane_quantity_input() {
				return MI_selectedPane_quantity_input;
			}
			
			public ComboBox<String> getMP_selectedPane_units_combo() {
				return MP_selectedPane_units_combo;
			}

			public DatePicker getMI_selectedPane_date() {
				return MI_selectedPane_date;
			}

			public Button getMI_selectedPane_confirm_button() {
				return MI_selectedPane_confirm_button;
			}

			public TitledPane getMI_ingredientPane() {
				return MI_ingredientPane;
			}

			public Button getMI_ingredientPane_search_button() {
				return MI_ingredientPane_search_button;
			}

			public TextField getMI_ingredientPane_type_input() {
				return MI_ingredientPane_type_input;
			}

			public ListView<String> getMI_ingredientPane_result_list() {
				return MI_ingredientPane_result_list;
			}

			public TextField getMI_ingredientPane_quantity_input() {
				return MI_ingredientPane_quantity_input;
			}

			public ComboBox<String> getMI_ingredientPane_units_combo() {
				return MI_ingredientPane_units_combo;
			}

			public DatePicker getMI_ingredientPane_date() {
				return MI_ingredientPane_date;
			}

			public Button getMI_ingredientPane_confirm_button() {
				return MI_ingredientPane_confirm_button;
			}

			public Label getMI_title() {
				return MI_title;
			}

			public void setMI_title(Label mI_title) {
				MI_title = mI_title;
			}

			public Label getMI_date_hour() {
				return MI_date_hour;
			}

			public ComboBox<String> getMI_list_combo() {
				return MI_list_combo;
			}

			public Button getMI_confirm_button() {
				return MI_confirm_button;
			}

			public TitledPane getMI_managementPane() {
				return MI_managementPane;
			}

			public TextField getMI_managementPane_newname_input() {
				return MI_managementPane_newname_input;
			}

			public Button getMI_managementPane_create_button() {
				return MI_managementPane_create_button;
			}

			public TextField getMI_managementPane_rename_input() {
				return MI_managementPane_rename_input;
			}

			public Button getMI_managementPane_rename_button() {
				return MI_managementPane_rename_button;
			}

			public ToggleButton getMI_managementPane_security_toggle() {
				return MI_managementPane_security_toggle;
			}

			public Button getMI_managementPane_delete_button() {
				return MI_managementPane_delete_button;
			}
			

			public Button getMI_Edit_Button() {
				return MI_Edit_Button;
			}

			public Tab getMy_recipes_TAB() {
				return my_recipes_TAB;
			}

			public TitledPane getMR_managerPane() {
				return MR_managerPane;
			}

			public TextField getMR_managerPane_search_input() {
				return MR_managerPane_search_input;
			}

			public Button getMR_managerPane_namesearch_button() {
				return MR_managerPane_namesearch_button;
			}

			public ListView<String> getMR_managerPane_searchresult_list() {
				return MR_managerPane_searchresult_list;
			}

			public Button getMR_managerPane_displaysearch_button() {
				return MR_managerPane_displaysearch_button;
			}

			public ListView<String> getMR_managerPane_favorite_list() {
				return MR_managerPane_favorite_list;
			}

			public Button getMR_managerPane_favoritedisplay_button() {
				return MR_managerPane_favoritedisplay_button;
			}

			public ComboBox<String> getMR_managerPane_list_combo() {
				return MR_managerPane_list_combo;
			}

			public Button getMR_managerPane_listsearch_button() {
				return MR_managerPane_listsearch_button;
			}

			public Button getMR_favoriteAdd_button() {
				return MR_favoriteAdd_button;
			}

			public Button getMR_managerPane_removeFav_button() {
				return MR_managerPane_removeFav_button;
			}

			public Label getMR_recipe_label() {
				return MR_recipe_label;
			}

			public Label getMR_time_label() {
				return MR_time_label;
			}

			public ImageView getMR_recipe_picture() {
				return MR_recipe_picture;
			}

			public TableView<Ingredients> getMR_ingredient_list() {
				return MR_ingredient_list;
			}

			public TableColumn<Ingredients, String> getMR_ingredient_list_NAME() {
				return MR_ingredient_list_NAME;
			}

			public TableColumn<Ingredients, Float> getMR_ingredient_list_QUANTITY() {
				return MR_ingredient_list_QUANTITY;
			}

			public TableColumn<Ingredients, String> getMR_ingredient_list_UNITS() {
				return MR_ingredient_list_UNITS;
			}
			
			public Label getMR_serving_label() {
				return MR_serving_label;
			}
			
			public TextField getMR_serving_number() {
				return MR_serving_number;
			}

			public Button getMR_Compare_Button() {
				return MR_Compare_Button;
			}

			public ComboBox<String> getMR_compareCombobox() {
				return MR_compareCombobox;
			}

			public ImageView getMN_ProfilePane_profile_picture() {
				return MN_ProfilePane_profile_picture;
			}

			public TextField getMN_ProfilePane_name_input() {
				return MN_ProfilePane_name_input;
			}

			public TextField getMN_ProfilePane_age_input() {
				return MN_ProfilePane_age_input;
			}

			public TextField getMN_ProfilePane_size_input() {
				return MN_ProfilePane_size_input;
			}

			public TextField getMN_ProfilePane_weight_input() {
				return MN_ProfilePane_weight_input;
			}

			public ComboBox<String> getMN_ProfilePane_gender_combo() {
				return MN_ProfilePane_gender_combo;
			}

			public ComboBox<String> getMN_ProfilePane_activity_combo() {
				return MN_ProfilePane_activity_combo;
			}

			public Label getMN_ProfilePane_BMI_Value() {
				return MN_ProfilePane_BMI_Value;
			}

			public Button getMN_ProfilePane_Button_Calculate() {
				return MN_ProfilePane_Button_Calculate;
			}

			public Label getMN_CaloriesDayValue() {
				return MN_CaloriesDayValue;
			}

			public Label getMN_CaloriesWeekValue() {
				return MN_CaloriesWeekValue;
			}

			public Label getMN_ModerateCarbsProtValue() {
				return MN_ModerateCarbsProtValue;
			}

			public Label getMN_ModerateCarbsFatsValue() {
				return MN_ModerateCarbsFatsValue;
			}

			public Label getMN_ModerateCarbsCarbsValue() {
				return MN_ModerateCarbsCarbsValue;
			}

			public Label getMN_LowerCarbsProtValue() {
				return MN_LowerCarbsProtValue;
			}

			public Label getMN_LowerCarbsFatsValue() {
				return MN_LowerCarbsFatsValue;
			}

			public Label getMN_LowerCarbsCarbsValue() {
				return MN_LowerCarbsCarbsValue;
			}

			public Label getMN_HigherCarbsProtValue() {
				return MN_HigherCarbsProtValue;
			}

			public Label getMN_HigherCarbsFatsValue() {
				return MN_HigherCarbsFatsValue;
			}

			public Label getMN_HigherCarbsCarbsValue() {
				return MN_HigherCarbsCarbsValue;
			}

			public Label getMN_Calories_amount1() {
				return MN_Calories_amount1;
			}

			public Label getMN_Calories_amount2() {
				return MN_Calories_amount2;
			}

			public Label getMN_Calories_amount3() {
				return MN_Calories_amount3;
			}

			public Label getMN_Calories_amount4() {
				return MN_Calories_amount4;
			}

			public Label getMN_Calories_amount5() {
				return MN_Calories_amount5;
			}

			public Label getMN_Calories_amount6() {
				return MN_Calories_amount6;
			}


		//Methode d'initialisation
		/**
		 * This method is called at the start of the application.
		 * It initializes the structure of all the TableView and ComboBox
		 * It also loads the database
		 * Finally, it renders some GUI elements to non-visible
		 */
		@FXML
		public void initialize() {
			DatabaseManager.initiateDatabase();		
			String[] unitSelectorList = {"item", "g", "l"};
			MP_AddPane_units_combo.getItems().addAll(unitSelectorList);
			MI_ingredientPane_units_combo.getItems().addAll(unitSelectorList);
			MP_selected_units_combo.getItems().addAll(unitSelectorList);
			MP_selectedPane_units_combo.getItems().addAll(unitSelectorList);
			MR_serving_number.setText("");
			
			//TableView de l'onglet Pantry
			MP_tableview_NAME.setCellValueFactory(new PropertyValueFactory<Ingredients, String>("name"));
			MP_tableview_QUANTITY.setCellValueFactory(new PropertyValueFactory<Ingredients, Float>("quantity"));
			MP_tableview_UNITS.setCellValueFactory(new PropertyValueFactory<Ingredients, String>("units"));
			MP_tableview_EXPDATE.setCellValueFactory(new PropertyValueFactory<Ingredients, LocalDate>("expdate"));
			MP_tableview_CONSUME.setCellValueFactory(new PropertyValueFactory<Ingredients, LocalDate>("consume"));
			
			//TableView de l'onglet Ingredients List
			MI_Listview_NAME.setCellValueFactory(new PropertyValueFactory<Ingredients, String>("name"));
			MI_Listview_QUANTITY.setCellValueFactory(new PropertyValueFactory<Ingredients, Float>("quantity"));
			MI_Listview_UNITS.setCellValueFactory(new PropertyValueFactory<Ingredients, String>("units"));
			MI_Listview_EXPDATE.setCellValueFactory(new PropertyValueFactory<Ingredients, LocalDate>("expdate"));
			MI_Listview_CONSUME.setCellValueFactory(new PropertyValueFactory<Ingredients, LocalDate>("consume"));
			
			//TableView de l'onglet Recipe
			MR_ingredient_list_NAME.setCellValueFactory(new PropertyValueFactory<Ingredients, String>("name"));
			MR_ingredient_list_QUANTITY.setCellValueFactory(new PropertyValueFactory<Ingredients, Float>("quantity"));
			MR_ingredient_list_UNITS.setCellValueFactory(new PropertyValueFactory<Ingredients, String>("units"));
			MR_ingredient_list_AVAILABILITY.setCellValueFactory(new PropertyValueFactory<Ingredients, String>("available"));
			
			//Set visibility to false (Display of recipe)
			MR_favoriteAdd_button.setVisible(false);
			MR_recipe_label.setVisible(false);
			MR_time_label.setVisible(false);
			MR_recipe_picture.setVisible(false);
			MR_ingredient_list.setVisible(false);
			MR_Compare_Button.setVisible(false);
			MR_compareCombobox.setVisible(false);
			MR_viewInstructions_button.setVisible(false);
			MR_serving_label.setVisible(false);
			MR_serving_number.setVisible(false);
			MR_EditServing_button.setVisible(false);
			MR_CaloriesText.setVisible(false);
			MR_CarbsText.setVisible(false);
			MR_FatsText.setVisible(false);
			MR_ProteinsText.setVisible(false);
			MR_Calories_amount.setVisible(false);
			MR_Carbs_amount.setVisible(false);
			MR_Fats_amount.setVisible(false);
			MR_Proteins_amount.setVisible(false);
			MR_AddToShoppingList.setVisible(false);
			MR_SaveShoppingList.setVisible(false);
			MR_Refresh_ShoppingList.setVisible(false);
			
			//TableView de l'onglet Profile
			//MN_ProfilePane
			//ComboBox Gender
			String[] genderSelectorList = {"M", "F"};
			MN_ProfilePane_gender_combo.getItems().addAll(genderSelectorList);
			//ComboBox Activity
			String[] activitySelectorList = {"Basal Metabollic Rate", "Sedentary", "Light exercise","Moderate exercise", "Heavy exercise", "Athlete" };
			MN_ProfilePane_activity_combo.getItems().addAll(activitySelectorList);
			
			PantryTabController.initiatePantry();
			IngredientsTabController.initiateLists();
			PantryTabController.ActualisePeremption();
			IngredientsTabController.ActualisePeremption();
			
		}

		//------Methodes liees au evenements------ elles redirigent vers les sub-controllers
		
		//---Methodes de "My Pantry"-------------------------------------------------------------------------------------------------------------------------------------------------
		
		//Appui du bouton "Confirm"(gauche) qui ajoute l'ingredient au pantry apres avoir rempli le formulaire
		/**
		 * Called from a button in "My Pantry" tab. It is a redirection to a method in the PantryTabController class
		 */
		@FXML public void MP_Add_Confirm() {
			PantryTabController.ConfirmSearchButtonOnClick();
		}
		
		//Appui du bouton "Search" qui va rechercher les ingredients correspondants au champ de texte
		/**
		 * Called from a button in "My Pantry" tab. It is a redirection to a method in the PantryTabController class
		 */
		@FXML public void MP_Add_Search() {	
			PantryTabController.SearchButtonOnclick();
		}
		
		//Appui du bouton "Delete" qui va supprimer l'ingredient selectionne du pantry
		/**
		 * Called from a button in "My Pantry" tab. It is a redirection to a method in the PantryTabController class
		 */
		@FXML public void MP_Delete_element() {
			PantryTabController.DeleteIngredientButtonOnClick();
		}

		//Appui du bouton "Confirm"(en bas) qui va modifier l'ingredient du pantry selon le formulaire de modification
		/**
		 * Called from a button in "My Pantry" tab. It is a redirection to a method in the PantryTabController class
		 */
		@FXML public void MP_Modify_confirm() {
			PantryTabController.ConfirmModifyButtonOnClick();
		}

		//Appui du bouton "Save current pantry as a list" qui doit enregister le pantry en tant que liste retrouvable dans l'onglet "my Lists"
		/**
		 * Called from a button in "My Pantry" tab. It is a redirection to a method in the PantryTabController class
		 */
		@FXML public void MP_Save_Pantry() {
			PantryTabController.SavePantryButtonOnClick();
		}

		//---Methodes de "My Ingredients lists"--------------------------------------------------------------------------------------------------------------------------------
		
		//Appui du bouton "Delete" dans le panneau "With selected item"
		/**
		 * Called from a button in "My Ingredients lists" tab. It is a redirection to a method in the IngredientsTabController class
		 */
		@FXML public void MI_Delete_Ing() {
			IngredientsTabController.DeleteIngButtonOnClick();
		}

		//Appui du bouton "Confirm" dans le panneau "With selected item"
		/**
		 * Called from a button in "My Ingredients lists" tab. It is a redirection to a method in the IngredientsTabController class
		 */
		@FXML public void MI_Modify_Ing() {
			IngredientsTabController.ConfirmModifyButtonOnClick();
		}

		//Appui du bouton "Search"
		/**
		 * Called from a button in "My Ingredients lists" tab. It is a redirection to a method in the IngredientsTabController class
		 */
		@FXML public void MI_Search_Ing() {
			IngredientsTabController.SearchButtonOnClick();
		}

		//Appui du bouton "Confirm" du panneau "Add an ingredient"
		/**
		 * Called from a button in "My Ingredients lists" tab. It is a redirection to a method in the IngredientsTabController class
		 */
		@FXML public void MI_Confirm_Search() {
			IngredientsTabController.ConfirmAddButtonOnClick();
		}
		
		//Appui du bouton "Confirm" dans la selection de liste en haut adroite
		/**
		 * Called from a button in "My Ingredients lists" tab. It is a redirection to a method in the IngredientsTabController class
		 */
		@FXML public void MI_Select_List_Confirm() {
			IngredientsTabController.ConfirmSelectionListButtonOnClick();
		}

		//Appui du bouton "Edit list"
		/**
		 * Called from a button in "My Ingredients lists" tab. It is a redirection to a method in the IngredientsTabController class
		 */
		@FXML public void MI_Edit_List() {
			IngredientsTabController.EditListButtonClick();
		}
		
		//---Methodes de "My recipes"----------------------------------------------------------------------------------------------------------------------------------------
				
		//Appui du bouton "Search" pour chercher une recette en indiquant son nom
		/**
		 * Called from a button in "My Recipes" tab. It is a redirection to a method in the RecipesTabController class
		 */
		@FXML public void MR_Recipe_Search() {
			RecipesTabController.SearchButtonOnClick();
		}

		//Appui du bouton "Display selected recipe" associe la recherche de recette
		/**
		 * Called from a button in "My Recipes" tab. It is a redirection to a method in the RecipesTabController class
		 */
		@FXML public void MR_Recipe_SearchbyDisplay() {
			RecipesTabController.DisplayRecipeButtonOnClick();
		}

		//Appui du bouton "+ favorites" qui ajoute la recette affiche aux favoris
		/**
		 * Called from a button in "My Recipes" tab. It is a redirection to a method in the RecipesTabController class
		 */
		@FXML public void MR_AddtoFavs() {	
			RecipesTabController.AddtoFavsButtonOnClick();		
		}
		
		//Appui du bouton "Display selected recipe" associe aux favoris
		/**
		 * Called from a button in "My Recipes" tab. It is a redirection to a method in the RecipesTabController class
		 */
		@FXML public void MR_Recipe_FavoriteDisplay() {
			RecipesTabController.DisplayFavoriteButtonOnClick();
		}

		//Appui du bouton "Compare"
		/**
		 * Called from a button in "My Recipes" tab. It is a redirection to a method in the RecipesTabController class
		 */
		@FXML public void MR_ComparetoList() {
			RecipesTabController.ComparetoListButtonOnClick();
		}
		
		/**
		 * Called from a button in "My Recipes" tab. It is a redirection to a method in the RecipesTabController class
		 */
		@FXML public void MR_EditAmounts() {
			RecipesTabController.EditServingsAmountsOnClick();
		}
		
		/**
		 * Called from a button in "My Recipes" tab. It is a redirection to a method in the RecipesTabController class
		 */
		@FXML public void MR_ViewInstructions() {
			RecipesTabController.ViewInstructionsOnClick();
		}

		/**
		 * Called from a button in "My Recipes" tab. It is a redirection to a method in the RecipesTabController class
		 */
		@FXML public void MR_SwitchtoName() {
			RecipesTabController.RadioSwitchOnClick();
		}
		
		/**
		 * Called from a button in "My Recipes" tab. It is a redirection to a method in the RecipesTabController class
		 */
		@FXML public void MR_AddShoppinglist() {
			RecipesTabController.AddToShoppingListOnClick();
		}
		
		/**
		 * Called from a button in "My Recipes" tab. It is a redirection to a method in the RecipesTabController class
		 */
		@FXML public void MR_SavingPDF() {
			RecipesTabController.SavingPDFListOnClick();
		}
		
		/**
		 * Called from a button in "My Recipes" tab. It is a redirection to a method in the RecipesTabController class
		 */
		@FXML public void MR_RefreshList() {
			RecipesTabController.RefreshListOnClick();
		}
		
		
		//Methode onglet profile
		/**
		 * Called from a button in "My Profile" tab. It is a redirection to a method in the ProfileTabController class
		 */
		@FXML public void MN_Calculate_Calories(){
			ProfileTabController.Calculate_Calories();
		}
			
}
