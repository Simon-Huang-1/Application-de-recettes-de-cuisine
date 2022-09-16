package fr.tse.fise2.controller;

import java.io.IOException;

import fr.tse.fise2.prinfo.ApiRequests;
import fr.tse.fise2.prinfo.RequestsParser;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;import javafx.stage.Stage;

/**
 * This particular sub controller class shows the instructions of a specified recipe
 * It is special since it is a bit more independent : it creates a new window
 */
public class MyInstructionsController {
	
	// Holds this controller's Stage
    private Stage thisStage;
 // Will hold a reference to the first controller, allowing us to access the methods found there.
    private AppController MainController;
    
    /**
     * Constructor of InstructionsController with an instance of AppController as this is an aggregate from the main controller
     * @param MainController
     */
    public MyInstructionsController(AppController MainController) {
        // We received the first controller, now let's make it usable throughout this controller.
        this.MainController = MainController;

        // Create the new stage
        thisStage = new Stage();

        // Load the FXML file
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Instructions.fxml"));

            // Set this class as the controller
            loader.setController(this);

            // Load the scene
            thisStage.setScene(new Scene(loader.load()));

            // Setup the window/stage
            thisStage.setTitle("Instructions ");
            
            //On affiche la liste d'instruction
        	displayInstructions();

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
    
    
  //-----Declaration des attributs de MyIinstructionsController = les elments du FXML
  	//Fenetre Instructions 
  	@FXML public TextArea instructions;

	public TextArea getInstructions() {
		return instructions;
	}
	public void setInstructions(TextArea instructions) {
		this.instructions = instructions;
	}
	
	/**
	 * Shows the instructions for the recipe. It is the only goal of this class anyway
	 */
	public void displayInstructions() {
		String recipeRequest = ApiRequests.RecipeGetbyID(MainController.selectedRecipeIndex.toString());
		
		//On recupere les instructions
		String instructionsText=RequestsParser.RecipeResultParser(recipeRequest, "instructions"); //Uniquement a cette ligne : "instructions" designe la propriete "instructions" dans le tableau format json de l'API
		System.out.println(instructionsText);
		
		int k=0;
		//On organise les instructions de facon a avoir une instruction par ligne
		String organizedInstructions="";

		while (k<instructionsText.length()) {
			//Saut de ligne des que l'on rencontre un '.'
			if (instructionsText.charAt(k)=='.')
			{
				organizedInstructions+="\n";
				k++;
			}
			//On enleve les balises html si elles sont presentes
			else if (instructionsText.charAt(k)=='<')
			{
				while(instructionsText.charAt(k)!='>')
				{
					k++;
				}
				//On met le code suivant pour enlever les '>'
				if (instructionsText.charAt(k)=='>') 
				{
					k++;
				}
			}
			//Dans les autres cas, on recupere les chaines de caracteres
			else
			{
				organizedInstructions+=instructionsText.charAt(k);
				k++;
			}
		}
		//On affecte le texte structure dans le TextArea (qui n'est pas modifiable par l'utilisateur)
		getInstructions().setText(organizedInstructions);
		}
	
}
