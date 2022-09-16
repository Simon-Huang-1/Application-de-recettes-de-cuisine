package fr.tse.fise2.prinfo;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A POJO class which is basically a list of another POJO (Ingredients)
 * It comes with some methods to compare lists between them
 * Other than that, it is only designed to hold data
 *
 */
public class FoodLists {
	//Attributes
		private final int id;
		private String name;
		private ArrayList<Ingredients> lists = new ArrayList<Ingredients>();
		private String date_hour;
		
	//Constructor
		/**
		 * Constructor
		 * @param id
		 * @param name
		 * @param lists
		 * @param date_hour
		 */
		public FoodLists(int id, String name, ArrayList<Ingredients> lists, String date_hour) {
			super();
			this.name = name;
			this.lists = lists;
			this.date_hour = date_hour;
			this.id = id;
		}
	// Getters and setters
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public ArrayList<Ingredients> getLists() {
			return lists;
		}

		public void setLists(ArrayList<Ingredients> lists) {
			this.lists = lists;
		}

		public String getDate_hour() {
			return date_hour;
		}

		public void setDate_hour(String date_hour) {
			this.date_hour = date_hour;
		}
		
		public int getId() {
			return id;
		}
		
		/**
		 * Comparison between 2 ingredients quantity
		 * @param a
		 * @param b
		 * @return
		 */
		public static Integer QuantityCompare(Ingredients a, Ingredients b) {
			//Si ils ont la meme unite on compare juste les quantites
			if(a.getUnits().equals(b.getUnits()) || b.getUnits().equals("item") && a.getUnits().equals("")) {
				if(b.getQuantity() >= a.getQuantity()){
					return 2;
				}
				else {
					return 1;
				}
			}
			else{
				String IngName = a.getName();
				String SrcAmount = a.getQuantity().toString();
				String SrcUnit = a.getUnits();
				String TargetUnit = b.getUnits();
				Double NewQty = RequestsParser.AmountConverterParser(ApiRequests.AmountConverter(IngName, SrcAmount, SrcUnit, TargetUnit));
				if(b.getQuantity() >= NewQty.floatValue()){
					return 2;
				}
				else {
					return 1;
				}
			}
		}
		
		/**
		 * Comparison between 2 FoodLists. It returns an List of Integer of the size of the 'recette' parameter size
		 * @param recette
		 * @return
		 */
		public ArrayList<Integer> IngComparaison(ArrayList<Ingredients> recette) {
			ArrayList<Integer> result = new ArrayList<Integer>();
			for(int i=0; recette.size() > i; i++) {
				Ingredients oneIngofRecipe = recette.get(i);
				boolean isFound = false;
				for(int j=0; lists.size() > j; j++){
					Ingredients oneIngofFrigo = lists.get(j);
					System.out.println("On compare : [" + oneIngofFrigo.getName() + "] avec [" + oneIngofRecipe.getName() +"]");
					if(oneIngofFrigo.getName().equals(oneIngofRecipe.getName())) {
						System.out.println(oneIngofFrigo.getName()+" BON");
						Integer ok = QuantityCompare(oneIngofRecipe, oneIngofFrigo);
						result.add(ok);
						isFound = true;
						break;
					}
					System.out.println(oneIngofFrigo.getName());
				}
				if(!isFound) {
					System.out.println(oneIngofRecipe.getName() + " INEX");
					result.add(0);
				}
				
			}
			return result;
		}
			
	}
		
