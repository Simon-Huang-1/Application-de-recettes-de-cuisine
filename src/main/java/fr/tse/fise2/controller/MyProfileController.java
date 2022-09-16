package fr.tse.fise2.controller;

import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;

/**
 * Sub controller of the Profile Tab. Calls from this tab are redirected here by the AppController instance
 *
 */
public class MyProfileController {
	//Attributs : le handle du controller principal
	private AppController MainController;
	private double BMI;
	private double size;
	private double weight;
	private double age;
	private String gender;
	private String activity;
	// Le constructeur sera appele dans AppController en se passant lui-meme en reference
	/**
	 * Constructor : instance of AppController as parameter as it is an aggregate from it
	 * @param mainController
	 */
	public MyProfileController(AppController mainController) {
		super();
		MainController = mainController;
	}
	/**
	 * The only method of this class. It is called Calculate_Calories but calculates everything
	 * Generates all nutrition informations by reading the parameters set by the user
	 */
	public void Calculate_Calories() {
		//Calcul du BMI (IMC)
		//BMI = poids (kg) / taille² (m²)
		//MN_ProfilePane_age_input.getText() 
		//MN_ProfilePane_size_input
		//MN_ProfilePane_weight_input
		//MN_ProfilePane_BMI_Value
	
		if (MainController.MN_ProfilePane_size_input.getText() != null && !(MainController.MN_ProfilePane_size_input.getText().isEmpty()) &&MainController.MN_ProfilePane_weight_input.getText() != null && !(MainController.MN_ProfilePane_weight_input.getText().isEmpty())) {
			
			size=Double.parseDouble(MainController.MN_ProfilePane_size_input.getText());
			weight=Double.parseDouble(MainController.MN_ProfilePane_weight_input.getText());
			age=Double.parseDouble(MainController.MN_ProfilePane_age_input.getText());
			gender= MainController.MN_ProfilePane_gender_combo.getSelectionModel().getSelectedItem();
			activity = MainController.MN_ProfilePane_activity_combo.getSelectionModel().getSelectedItem();
			
			if (size>=0 && weight >=0) { //BMI>=0
				BMI= weight/(Math.pow ((size*Math.pow ( 10,-2 )), 2)) ; //On n'oublie pas de convertir les cm en m et Java n'a pas d'operateur exposant
				BMI= Math.round(BMI * 100.0) / 100.0; //Arrondi a 2 chiffres apres la virgule				
				MainController.MN_ProfilePane_BMI_Value.setText(Double.toString(BMI));
				if(BMI < 18.5) {
					MainController.MN_ProfilePane_BMI_Value.setTextFill(Color.web("#1164D3"));
					MainController.MN_ProfilePane_BMI_Value.setTooltip(new Tooltip("You are currently underweight"));
				}
				else if(BMI >= 18.5 && BMI < 25) {
					MainController.MN_ProfilePane_BMI_Value.setTextFill(Color.web("#15AC23"));
					MainController.MN_ProfilePane_BMI_Value.setTooltip(new Tooltip("Your BMI is considered normal"));
				}
				else if(BMI >= 25 && BMI < 30) {
					MainController.MN_ProfilePane_BMI_Value.setTextFill(Color.web("#B8B806"));
					MainController.MN_ProfilePane_BMI_Value.setTooltip(new Tooltip("You are currently overweight"));
				}
				else if(BMI >= 30 && BMI < 35) {
					MainController.MN_ProfilePane_BMI_Value.setTextFill(Color.web("#E9991E"));
					MainController.MN_ProfilePane_BMI_Value.setTooltip(new Tooltip("Your BMI indicates that you are currently obese"));
				}
				else {
					MainController.MN_ProfilePane_BMI_Value.setTextFill(Color.web("#F0391D"));
					MainController.MN_ProfilePane_BMI_Value.setTooltip(new Tooltip("Your BMI indicates that you are currently severely obese"));
				}
				
				Integer chosenRegime;
				Integer basalCalPerDays = (int) Math.floor((10*weight) + (6.25 * size) - (5*age));
				if(gender.equals("F")) {
					basalCalPerDays -= 161;
				}
				else {
					basalCalPerDays += 5;
				}
				Integer sedentaryCalPerDays = (int) (basalCalPerDays*1.2);
				Integer lightCalPerDays = (int) (basalCalPerDays*1.375);
				Integer moderateCalPerDays = (int) (basalCalPerDays*1.55);
				Integer heavyCalPerDays = (int) (basalCalPerDays*1.725);
				Integer athleteCalPerDays = (int) (basalCalPerDays*1.9);
				switch(activity) {
					case "Basal Metabollic Rate":
						chosenRegime=basalCalPerDays;
						break;
					case "Sedentary":
						chosenRegime=sedentaryCalPerDays;
						break;
					case "Light exercise":
						chosenRegime=lightCalPerDays;
						break;
					case "Moderate exercise":
						chosenRegime=moderateCalPerDays;
						break;
					case "Heavy exercise":
						chosenRegime=heavyCalPerDays;
						break;
					case "Athlete":
						chosenRegime=athleteCalPerDays;
						break;
					default:
						chosenRegime=basalCalPerDays;
				}
				Integer calPerWeek = 7*chosenRegime;
				Integer moderateProt = (int) ((chosenRegime*0.3)/4);
				Integer moderateFat = (int) ((chosenRegime*0.35)/9);
				Integer moderateCarbs = (int) ((chosenRegime*0.35)/4);
				Integer lowerProt = (int) ((chosenRegime*0.4)/4);
				Integer lowerFat = (int) ((chosenRegime*0.4)/9);
				Integer lowerCarbs = (int) ((chosenRegime*0.2)/4);
				Integer higherProt = (int) ((chosenRegime*0.3)/4);
				Integer higherFat = (int) ((chosenRegime*0.2)/9);
				Integer higherCarbs = (int) ((chosenRegime*0.5)/4);
				
				MainController.MN_CaloriesDayValue.setText(chosenRegime.toString());
				MainController.MN_CaloriesWeekValue.setText(calPerWeek.toString());
				
				MainController.MN_Calories_amount1.setText(basalCalPerDays.toString());
				MainController.MN_Calories_amount2.setText(sedentaryCalPerDays.toString());
				MainController.MN_Calories_amount3.setText(lightCalPerDays.toString());
				MainController.MN_Calories_amount4.setText(moderateCalPerDays.toString());
				MainController.MN_Calories_amount5.setText(heavyCalPerDays.toString());
				MainController.MN_Calories_amount6.setText(athleteCalPerDays.toString());
				
				MainController.MN_ModerateCarbsProtValue.setText(moderateProt.toString());
				MainController.MN_ModerateCarbsFatsValue.setText(moderateFat.toString());
				MainController.MN_ModerateCarbsCarbsValue.setText(moderateCarbs.toString());
				
				MainController.MN_LowerCarbsProtValue.setText(lowerProt.toString());
				MainController.MN_LowerCarbsFatsValue.setText(lowerFat.toString());
				MainController.MN_LowerCarbsCarbsValue.setText(lowerCarbs.toString());
				
				MainController.MN_HigherCarbsProtValue.setText(higherProt.toString());
				MainController.MN_HigherCarbsFatsValue.setText(higherFat.toString());
				MainController.MN_HigherCarbsCarbsValue.setText(higherCarbs.toString());
			}
			
		}
		
	}
}
