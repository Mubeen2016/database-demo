package com.mubeen.database.mysqldb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.io.FileUtils;

import junit.framework.TestCase;

//https://www.freemysqlhosting.net/account/
//http://www.phpmyadmin.co/
public class DB04OnlineDBTest extends TestCase {
	
	private String db_hostname = "sql9.freemysqlhosting.net";
	private int port = 3306;
	private String database = "sql9135255";
	private String db_username = "sql9135255";
	private String db_password = "LJXiCrMCHP";

    
    Statement m_Statement = null;
    ResultSet m_ResultSet = null;
	
	public void testReadingDatabase() throws Exception {
		setupDB();
		doWork();
	}
	 private void doWork() throws SQLException {
         String query = "";
         Connection connection = null;
 		String host = "jdbc:mysql://"+db_hostname+":"+port+"/"+database;
 		connection = DriverManager.getConnection( host, db_username, db_password);
 		
 		Statement m_Statement = connection.createStatement();
         try {
             //Create connection object
        	 
      
             //Create Statement object
             m_Statement = connection.createStatement();
             query = "SELECT * FROM FlightInfo";
//Execute the query
             m_ResultSet = m_Statement.executeQuery(query);
//Loop through the results
             while (m_ResultSet.next()) {
                 System.out.print(m_ResultSet.getString(1) + ", ");
                 System.out.print(m_ResultSet.getString(2) + ", ");
                 System.out.print(m_ResultSet.getString(3) + ", ");
                 System.out.print(m_ResultSet.getString(4) + ", ");
                 System.out.print(m_ResultSet.getString(5));
                 System.out.print("\n"); //new line

             }
         } catch (SQLException ex) {
             ex.printStackTrace();
             System.out.println(query);
         } finally {
             try {
                 if (m_ResultSet != null) {
                     m_ResultSet.close();
                 }
                 if (m_Statement != null) {
                     m_Statement.close();
                 }
                 if (connection != null) {
                     connection.close();
                 }
             } catch (SQLException ex) {
                 ex.printStackTrace();
             }
         }
     }

	

	private void setupDB() throws SQLException {
		Connection connection = null;
		String host = "jdbc:mysql://"+db_hostname+":"+port+"/"+database;
		connection = DriverManager.getConnection( host, db_username, db_password);
		
		Statement statement = connection.createStatement();
		statement.executeUpdate("CREATE DATABASE IF NOT EXISTS sql9135255");
		
		//this deletes table 
		//statement.executeUpdate("DROP TABLE FlightInfo");
		
		String sql = "CREATE TABLE IF NOT EXISTS FlightInfo (" +"flight VARCHAR(12) NOT NULL," + 
		"depart_location VARCHAR(12)," +"arrive_location VARCHAR(12)," + "depart_time VARCHAR(12)," + "arrive_time VARCHAR(12)," +"PRIMARY KEY(flight))";
		statement.executeUpdate(sql);
		
		// Delete all rows before data setup
		statement.executeUpdate("DELETE FROM FlightInfo");

		PreparedStatement pstmt = connection
				.prepareStatement("INSERT INTO `FlightInfo`(flight,depart_location,arrive_location,depart_time,arrive_time) VALUE (?,?,?,?,?)");

		// using absolute path. This is not recommended
		// String csvFile = "C:\\Users\\mubeen\\Google
		// Drive\\workspace\\database-demo\\mysqldb\\src\\test\\java\\com\\mubeen\\database\\mysqldb\\customer-data.csv";
		String userDir = System.getProperty("user.dir");
		String filePath = userDir + File.separator + "src" + File.separator + "test" + File.separator + "resources"
				+ File.separator;
		File f = FileUtils.getFile(filePath + "flight-data.csv");
		String csvFile = f.toString();

		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";

		try {

			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {

				// use comma as separator
				String[] data = line.split(cvsSplitBy);
				System.out.println("flight = " + data[0] + " , depart_location=" + data[1] + " arrive_location= " + data[2] + ", depart_time=" +data[3]+ ", arrive_time= " + data[4]);
				pstmt.setString(1, data[0].trim());
				pstmt.setString(2, data[1].trim());
				pstmt.setString(3, data[2].trim());
				pstmt.setString(4, data[3].trim());
				pstmt.setString(5, data[4].trim());
				pstmt.executeUpdate();

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
