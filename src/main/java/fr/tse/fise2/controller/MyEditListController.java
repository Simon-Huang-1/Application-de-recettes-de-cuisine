
package fr.tse.fise2.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import fr.tse.fise2.prinfo.DatabaseManager;
import fr.tse.fise2.prinfo.FoodLists;
import fr.tse.fise2.prinfo.Ingredients;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * This class is also a controller class to manage the behavior of Instructions.fxml
 * This one is a bit different since it instanciates a new window/stage
 */
public class MyEditListController {

	// Holds this controller's Stage
    private Stage thisStage;
 // Will hold a reference to the first controller, allowing us to access the methods found there.
    private AppController MainController;
    
    public MyEditListController(AppController MainController) {
        // We received the first controller, now let's make it usable throughout this controller.
        this.MainController = MainController;

        // Create the new stage
        thisStage = new Stage();

        // Load the FXML file
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Edit.fxml"));

            // Set this class as the controller
            loader.setController(this);

            // Load the scene
            thisStage.setScene(new Scene(loader.load()));

            // Setup the window/stage
            thisStage.setTitle("Edit lists");
            
            //On met  jour le menu deroulant
            getME_list_combo().getItems().addAll(MainController.getAllFoodListsName());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Show the stage that was loaded in the constructor
     */
    public void showStage() {
        thisStage.showAndWait();
    }

    
    
	
	//-----Declaration des attributs de MyEditListController = les elements du FXML
	//Fenetre EditList 
	@FXML public AnchorPane editList;
	@FXML public AnchorPane ME_managementPane;
	@FXML public ComboBox<String> ME_list_combo;
	@FXML public Button ME_confirm_button;
	@FXML public TextField ME_managementPane_newname_input;
	@FXML public Button ME_managementPane_create_button;
	@FXML public Button ME_managementPane_delete_button;
	
	//Getters and setters
	public AnchorPane getME_managementPane() {
		return ME_managementPane;
	}
	public void setME_managementPane(AnchorPane mE_managementPane) {
		ME_managementPane = mE_managementPane;
	}
	public ComboBox<String> getME_list_combo() {
		return ME_list_combo;
	}
	public void setME_list_combo(ComboBox<String> mE_list_combo) {
		ME_list_combo = mE_list_combo;
	}
	public Button getME_confirm_button() {
		return ME_confirm_button;
	}
	public void setME_confirm_button(Button mE_confirm_button) {
		ME_confirm_button = mE_confirm_button;
	}
	public TextField getME_managementPane_newname_input() {
		return ME_managementPane_newname_input;
	}
	public void setME_managementPane_newname_input(TextField mE_managementPane_newname_input) {
		ME_managementPane_newname_input = mE_managementPane_newname_input;
	}
	public Button getME_managementPane_create_button() {
		return ME_managementPane_create_button;
	}
	public void setME_managementPane_create_button(Button mE_managementPane_create_button) {
		ME_managementPane_create_button = mE_managementPane_create_button;
	}

	public Button getME_managementPane_delete_button() {
		return ME_managementPane_delete_button;
	}
	public void setME_managementPane_delete_button(Button mE_managementPane_delete_button) {
		ME_managementPane_delete_button = mE_managementPane_delete_button;
	}
	
	
	
	//---Methodes de "Edit list"--------------------------------------------------------------------------------------------------------------------------------
	/**
	 * This method allows the user to confirm is he willing to use the currently selected list
	 */
	@FXML public void ME_Select_List_Confirm() {
		//1) On selectionne le champ de texte correspondant au nom de la liste, 
		//	 uniquement si elle est non vide
		
		String listName=getME_list_combo().getSelectionModel().getSelectedItem();
		//ça marche si on selectionne la liste+confirmer apres avoir cree la liste
		//On met  jour le champ listName de AppController
		MainController.setListName(listName);
		
		if (listName.isEmpty()==false) {
			// On affiche le nom de la liste au centre
			MainController.getMI_title().setText("Currents contents of: "+listName);
			
			//  2) On vide le tableau des ingredients au niveau de l'affichage
			MainController.getMI_Listview().getItems().clear();
			
			// Petit test
			System.out.println("clear ok"+listName);
			
			//  3) On recupere l'indice de liste d'ingredient qui a pour nom le champ de texte,
			//     dans la liste AllFoodLists (même index que pour la liste AllFoodListsName)
			int index=MainController.getAllFoodListsName().indexOf(listName);
			
			//On affiche la date et l'heure de la liste
			String date_hour=MainController.getAllFoodLists().get(index).getDate_hour();
			MainController.getMI_date_hour().setText("Created: "+date_hour);
			
			for(int i=0;i<MainController.getAllFoodLists().get(index).getLists().size();i++)
			{
//			 	4) On affiche dans le tableau les ingredients de la liste selectionnee (application)
				MainController.getMI_Listview().getItems().add(MainController.getAllFoodLists().get(index).getLists().get(i));
				System.out.println(MainController.getAllFoodLists().get(index).getLists().get(i).getName());
			}
			
		}		
		
	}
	
	//Appui du bouton "Create new list"
	/**
	 * This method allows the user to create a new list (Food list)
	 */
	@FXML public void ME_Create_List() {
		MainController.getMI_Listview().getItems().clear();
		//On recupere la valeur saisie dans le champ de texte 'Enter name'
		String listName = String.valueOf(getME_managementPane_newname_input().getText());
		//On met  jour le champ listName de AppController
		MainController.setListName(listName);
		
		if (listName.isEmpty()==false) //Si le champ de texte n'est pas vide, on peut creer une liste
		{
			// On affiche le nom de la liste au centre
			MainController.getMI_title().setText("Currents contents of: "+listName);
			//Dans 'Select a list', on remplace le texte par le nom de la liste creee
			MainController.MI_list_combo.setPromptText(listName);
			getME_list_combo().setPromptText(listName);
			
			//LocalDate date_hour=java.time.LocalDate.now(); //Ne renvoie que la date et non date+heure
        	
			//Initialisation de la liste
			
			//Date et heure courantes de la liste
			SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		    Date date = new Date();
			String date_hour=s.format(date);
			
			//On affiche la date et l'heure de la liste
			MainController.getMI_date_hour().setText("Created: "+date_hour);
			
			//Test date et heure
        	//System.out.println(date_hour);
        	
			//Ajouter la liste à la base donnée
			int i = DatabaseManager.addList(listName, date_hour);
			
			ArrayList<Ingredients> lists = new ArrayList<Ingredients>();
			FoodLists nouvelleListe = new FoodLists(i, listName, lists, date_hour);
			
			//On ajoute la liste d'ingredient a la liste des listes d'ingredients
			MainController.getAllFoodLists().add(nouvelleListe);
			
			//On ajoute le nom de la liste nouvellement creee a une liste des noms des listes
			//pour pouvoir l'ajouter au menu deroulant dans la selection des listes
			ArrayList<String> AllFoodListsName = new ArrayList<String>();
			AllFoodListsName.add(listName);	
			
			//Mise a jour de la liste entiere des noms
			MainController.getAllFoodListsName().add(listName);	
				
			//On ajoute la liste d'ingredients dans la selection de liste 
			//(Ainsi on pourra la recuperer, pour ajouter des ingredients et la gerer)
			MainController.getMI_list_combo().getItems().addAll(AllFoodListsName);
			getME_list_combo().getItems().addAll(AllFoodListsName);
			
			//Test : Pour afficher la liste contenant les noms des listes d'ingredients
			//System.out.println(MainController.getAllFoodListsName()); 

		}
		
	}
	
	//Appui du bouton "Rename selected list"
	@FXML public void ME_Rename_List() {
		
	}

	
	//Appui du bouton "Delete selected list"
	/**
	 * This method allows the user to delete a list previously created
	 */
	@FXML public void ME_Delete_List() {
		// On recupere les donnees de la liste a supprimer
				String listName = getME_list_combo().getSelectionModel().getSelectedItem();
				int index=MainController.getAllFoodListsName().indexOf(listName);
				
				// On supprime le nom de la liste au centre
				MainController.getMI_title().setText("Currents contents of: ");
				
				// On supprime tous les aliments de la liste qu'on supprime
				MainController.getAllFoodLists().get(index).getLists().clear();
				
				// On recupere l'indice de la liste a supprimer
				int id = MainController.getAllFoodLists().get(index).getId();
				
				// Petits tests
				System.out.println("Taille de la liste d'ingredients : " +MainController.getAllFoodLists().get(index).getLists().size());
				System.out.println("L'index de la liste que l'on supprime : "+ index);
				System.out.println("Le nom de la liste que l'on supprime : "+ MainController.getAllFoodListsName().get(index));
				System.out.println("La liste des listes presentes : "+ MainController.getAllFoodListsName());
				
				// On supprime la liste 
				MainController.getAllFoodListsName().remove(index);
				System.out.println(MainController.getAllFoodListsName());
				
				// On supprime la liste des choix possibles
				MainController.getMI_list_combo().getItems().remove(index);
				getME_list_combo().getItems().remove(index);
				MainController.getMI_date_hour().setText("");
				MainController.getMI_Listview().getItems().clear();
				
				// On modifie les index des listes pour que cela corresponde aux listes d'ingredients
				for (int i= 0;i<MainController.getAllFoodLists().size();i++) {
					if(i>index) {
						MainController.getAllFoodLists().get(i-1).getLists().addAll(MainController.getAllFoodLists().get(i).getLists());
						MainController.getAllFoodLists().get(i).getLists().clear();
					}
				}
				// remove from database
				DatabaseManager.deleteList(id);
	}

}