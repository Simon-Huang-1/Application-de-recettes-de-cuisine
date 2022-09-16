package fr.tse.fise2.prinfo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

import fr.tse.fise2.controller.MyIngredientsController;

public class DatabaseManager {
 	
	public static Connection getConnection() {
		Connection connection = null;
		
		
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:foodapp.db");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		return connection;
	}
	
	public static void initiateDatabase() {
		final String CREATE_LISTS_TABLE_SQL="CREATE TABLE IF NOT EXISTS Lists ("
                + "listID integer primary key,"
                + "listName VARCHAR(45) NOT NULL,"
                + "date_hour VARCHAR(45) NULL);";
		
		final String CREATE_INGREDIENTS_TABLE_SQL="CREATE TABLE IF NOT EXISTS Ingredients ("
                + "ingID integer primary key,"
                + "ingName VARCHAR(45),"
                + "quantity FLOAT,"
                + "units VARCHAR(45),"
                + "expdate DATE,"
                + "listID integer,"
                + "FOREIGN KEY (listID) REFERENCES Lists(listID));";
		
		final String INITIATE_PANTRY_SQL="INSERT OR REPLACE INTO Lists VALUES (1,'Frigo',null) ;";
		
		Connection conn = getConnection();
	    Statement stmtList = null;
	    Statement stmtIng = null;
		try {
			stmtList = conn.createStatement();
			stmtList.executeUpdate(CREATE_LISTS_TABLE_SQL);
		    
		    // Close statement
		    if (stmtList != null) {
		    	stmtList.close();
		    }
		    
		    stmtIng = conn.createStatement();
		    stmtIng.executeUpdate(CREATE_INGREDIENTS_TABLE_SQL);


			// Close statement
			if (stmtIng != null) {
				stmtIng.close();
			}

			Statement st = conn.createStatement();

			st.executeUpdate(INITIATE_PANTRY_SQL);

			// Close statement
			if (st != null) {
				st.close();
			}
			// Close connection
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static int addList(String listName, String date_hour) {
		
		Connection conn = getConnection();
		int autoIncKeyFromApi = 1;
		
		String query = "insert into Lists "
				+ " values (?, ?, ?);";

		// create the mysql insert preparedstatement
		PreparedStatement preparedStmt = null;
		Statement getGeneratedKeys = null;
		try {
			getGeneratedKeys = conn.createStatement();
			
			ResultSet rs = getGeneratedKeys.executeQuery("SELECT * FROM Lists ORDER BY listID DESC LIMIT 1;");
			
		    if (rs.next()) {
		        autoIncKeyFromApi = rs.getInt(1);
		    }
			preparedStmt = conn.prepareStatement(query);
			preparedStmt.setInt(1, autoIncKeyFromApi+1);
			preparedStmt.setString(2, listName);
			preparedStmt.setString(3, date_hour);
			
			preparedStmt.executeUpdate();
			
		    if (rs.next()) {
		        autoIncKeyFromApi = rs.getInt(1);
		    }
			// Close statement
			if (preparedStmt != null) {
				preparedStmt.close();
			}
			rs.close();
			
		    
		    // Close connection
		    if (conn != null) {
		    	conn.close();
		    }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return autoIncKeyFromApi + 1;
	}

	public static int addIngredient(String ingName, Float ingQty, String ingUnit, LocalDate ingDate, int listID) {
		Connection conn = getConnection();
		int autoIncKeyFromApi = 0;
		
		String query = "insert into Ingredients "
				+ " values (?, ?, ?, ?, ?, ?);";

		// create the mysql insert preparedstatement
		PreparedStatement preparedStmt = null;
		Statement getGeneratedKeys = null;
		try {
			getGeneratedKeys = conn.createStatement();
			
			ResultSet rs = getGeneratedKeys.executeQuery("SELECT * FROM Ingredients ORDER BY ingID DESC LIMIT 1;");
			
		    if (rs.next()) {
		        autoIncKeyFromApi = rs.getInt(1);
		    }
			preparedStmt = conn.prepareStatement(query);
			preparedStmt.setInt(1, autoIncKeyFromApi+1);
			preparedStmt.setString(2, ingName);
			preparedStmt.setFloat(3, ingQty);
			preparedStmt.setString(4, ingUnit);
			
			preparedStmt.setDate(5, java.sql.Date.valueOf(ingDate));
			preparedStmt.setInt(6, listID);
			preparedStmt.executeUpdate();
			
			// Close statement
			if (preparedStmt != null) {
				preparedStmt.close();
			}
			rs.close();
			// Close connection
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return autoIncKeyFromApi+1;
	}

	public static void updateIngredientQuantity(int id, float quantity) {
		Connection conn = getConnection();

		String query = "update Ingredients set quantity = ? where ingID = ?;";

		// create the mysql insert preparedstatement
		PreparedStatement preparedStmt = null;
		try {

			preparedStmt = conn.prepareStatement(query);
			preparedStmt.setFloat(1, quantity);
			preparedStmt.setInt(2, id);

			// execute the java preparedstatement
			preparedStmt.executeUpdate();

			// Close statement
			if (preparedStmt != null) {
				preparedStmt.close();
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

	public static void updateIngredientExpdate(int id, LocalDate expdate) {
		Connection conn = getConnection();

		String query = "update Ingredients set expdate = ? where ingID = ?;";

		// create the mysql insert preparedstatement
		PreparedStatement preparedStmt = null;
		try {

			preparedStmt = conn.prepareStatement(query);
			preparedStmt.setDate(1, java.sql.Date.valueOf(expdate));
			preparedStmt.setInt(2, id);

			// execute the java preparedstatement
			preparedStmt.executeUpdate();

			// Close statement
			if (preparedStmt != null) {
				preparedStmt.close();
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

	public static void updateIngredientUnit(int id, String strUnit) {
		Connection conn = getConnection();

		String query = "update Ingredients set units = ? where ingID = ?;";

		// create the mysql insert preparedstatement
		PreparedStatement preparedStmt = null;
		try {

			preparedStmt = conn.prepareStatement(query);
			preparedStmt.setString(1, strUnit);
			preparedStmt.setInt(2, id);

			// execute the java preparedstatement
			preparedStmt.executeUpdate();

			// Close statement
			if (preparedStmt != null) {
				preparedStmt.close();
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

	public static void deleteIngredient(int id) {
		Connection conn = getConnection();

		String query = "delete from Ingredients where ingID = ?;";

		// create the mysql insert preparedstatement
		PreparedStatement preparedStmt = null;
		try {

			preparedStmt = conn.prepareStatement(query);
			preparedStmt.setInt(1, id);
			
			// execute the java preparedstatement
			preparedStmt.executeUpdate();

			// Close statement
			if (preparedStmt != null) {
				preparedStmt.close();
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
	
	// Classe pour tester
	public static void displayTables() {
		Connection conn = getConnection();
		
		String queryL = "SELECT * FROM Lists;";
		String queryI = "SELECT * FROM Ingredients;";
		
		// create the mysql insert preparedstatement
		Statement listsStmt = null;
		Statement ingStmt = null;
		try {
			listsStmt = conn.createStatement();
			
			ResultSet rsL = listsStmt.executeQuery(queryL);
			
			while (rsL.next()) {
				int listID = rsL.getInt("listID");
				String listName = rsL.getString("listName");
				String date_hour = rsL.getString("date_hour");
				System.out.println("listID  " + listID);
				System.out.println("listName  " + listName);
				System.out.println("date_hour  " + date_hour);
			}
			
			ingStmt = conn.createStatement();
			
			ResultSet rsI = ingStmt.executeQuery(queryI);
			
			while (rsI.next()) {
				int ingID = rsI.getInt("ingID");
		    	String ingName = rsI.getString("ingName");
		    	Float ingQty = rsI.getFloat("quantity");
		    	String ingUnit = rsI.getString("units");
		    	LocalDate ingDate = rsI.getDate("expdate").toLocalDate();
		    	int listID2 = rsI.getInt("listID");
				System.out.println("ingID  " + ingID);
				System.out.println("ingName  " + ingName);
				System.out.println("quantity  " + ingQty);
				System.out.println("units  " + ingUnit);
				System.out.println("expdate  " + ingDate);
		    	System.out.println("listID  " + listID2);
			}
			
			// Close statement
			if (listsStmt != null) {
				listsStmt.close();
			}
			if (ingStmt != null) {
				ingStmt.close();
			}
			rsL.close();
			rsI.close();
			// Close connection
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void deleteList(int id) {

		Connection conn = getConnection();

		String selectQuery = "delete from Ingredients where Ingredients.listID = ?;";

		// create the mysql insert preparedstatement
		PreparedStatement preparedStmt = null;
		try {

			preparedStmt = conn.prepareStatement(selectQuery);
			preparedStmt.setInt(1, id);

			// execute the java preparedstatement
			preparedStmt.executeUpdate();

			// Close statement
			if (preparedStmt != null) {
				preparedStmt.close();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String query = "delete from Lists where Lists.listID = ?;";

		// create the mysql insert preparedstatement
		PreparedStatement preparedStmt2 = null;
		try {

			preparedStmt2 = conn.prepareStatement(query);
			preparedStmt2.setInt(1, id);

			// execute the java preparedstatement
			preparedStmt2.executeUpdate();

			// Close statement
			if (preparedStmt2 != null) {
				preparedStmt2.close();
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
