package fr.tse.fise2.prinfo;

import java.time.LocalDate;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A POJO class mainly designed to hold data.
 * It will most likely be used as an aggregate of FoodLists
 *
 */
public class Ingredients {
	
	//Attributes
	private final int id;
	private String name;
	private Float quantity;
	private String units;
	private String available;
	private LocalDate expdate;
	private String consume;
	
	

	//Constructor
	/**
	 * Constructor
	 * @param id
	 * @param name
	 * @param quantity
	 * @param units
	 * @param available
	 * @param expdate
	 * @param consume
	 */
	public Ingredients(int id, String name, Float quantity, String units, String available, LocalDate expdate, String consume) {
		super();
		this.name = name;
		this.quantity = quantity;
		this.units = units;
		this.available =available;
		this.expdate = expdate;
		this.consume = consume;	
		this.id = id;
	}
	
	//Getters-Setters
	public String getConsume() {
		return consume;
	}

	public void setConsume(String consume) {
		this.consume = consume;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Float getQuantity() {
		return quantity;
	}
	public void setQuantity(Float quantity) {
		this.quantity = quantity;
	}
	public String getUnits() {
		return units;
	}
	public void setUnits(String units) {
		this.units = units;
	}
	public LocalDate getExpdate() {
		return expdate;
	}
	public void setExpdate(LocalDate expdate) {
		this.expdate = expdate;
	}

	public String getAvailable() {
		return available;
	}

	public void setAvailable(String available) {
		this.available = available;
	}

	public int getId() {
		return id;
	}
	
	
}
