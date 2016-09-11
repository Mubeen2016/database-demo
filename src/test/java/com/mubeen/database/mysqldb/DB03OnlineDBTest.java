package com.mubeen.database.mysqldb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

import org.apache.commons.io.FileUtils;

import junit.framework.TestCase;

//https://www.freemysqlhosting.net/account/
//http://www.phpmyadmin.co/
public class DB03OnlineDBTest extends TestCase {
	
	private String db_hostname = "sql9.freemysqlhosting.net";
	private int port = 3306;
	private String database = "sql9135255";
	private String db_username = "sql9135255";
	private String db_password = "LJXiCrMCHP";
	
	public void testDatabase() throws Exception {
		Connection connection = null;
		String host = "jdbc:mysql://"+db_hostname+":"+port+"/"+database;
		connection = DriverManager.getConnection( host, db_username, db_password);
		
		Statement statement = connection.createStatement();
		statement.executeUpdate("CREATE DATABASE IF NOT EXISTS sql9135255");
		
		String sql = "CREATE TABLE IF NOT EXISTS CustomerInfo (" + "id INT(64) NOT NULL AUTO_INCREMENT,"
				+ "name VARCHAR(20)," + "phone VARCHAR(12)," + "PRIMARY KEY(id))";
		statement.executeUpdate(sql);
		
		// Delete all rows before data setup
		statement.executeUpdate("DELETE FROM CustomerInfo");

		PreparedStatement pstmt = connection
				.prepareStatement("INSERT INTO `CustomerInfo`(id,name,phone) VALUE (?,?,?)");

		// using absolute path. This is not recommended
		// String csvFile = "C:\\Users\\mubeen\\Google
		// Drive\\workspace\\database-demo\\mysqldb\\src\\test\\java\\com\\mubeen\\database\\mysqldb\\customer-data.csv";
		String userDir = System.getProperty("user.dir");
		String filePath = userDir + File.separator + "src" + File.separator + "test" + File.separator + "resources"
				+ File.separator;
		File f = FileUtils.getFile(filePath + "customer-data.csv");
		String csvFile = f.toString();

		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";

		try {

			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {

				// use comma as separator
				String[] data = line.split(cvsSplitBy);

				System.out.println("id = " + data[0] + " , name=" + data[1] + " phone= " + data[2] + "]");
				pstmt.setInt(1, Integer.parseInt(data[0]));
				pstmt.setString(2, data[1].trim());
				pstmt.setString(3, data[2].trim());
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
