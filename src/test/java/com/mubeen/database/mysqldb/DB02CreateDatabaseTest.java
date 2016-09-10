package com.mubeen.database.mysqldb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.io.FileUtils;

import junit.framework.TestCase;

public class DB02CreateDatabaseTest extends TestCase {

	public void test01CreateNewDatabase() throws SQLException {
		Connection Conn = DriverManager.getConnection("jdbc:mysql://localhost/?user=root&password=root");
		Statement s = Conn.createStatement();
		s.executeUpdate("CREATE DATABASE IF NOT EXISTS CUSTOMER");
	}

	public void test02CreateNewTable() throws Exception {
		Connection connection = null;
		connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/customer", "root", "root");
		Statement statement = connection.createStatement();
		// This line has the issue
		String sql = "CREATE TABLE IF NOT EXISTS CustomerInfo (" + "id INT(64) NOT NULL AUTO_INCREMENT,"
				+ "name VARCHAR(20)," + "phone VARCHAR(12)," + "PRIMARY KEY(id))";
		statement.executeUpdate(sql);
		System.out.println("Table Created");
	}

	public void test03CreateData() throws Exception {
		Connection connection = null;
		connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/customer", "root", "root");

		// Delete all rows before data setup
		PreparedStatement delstmt = connection.prepareStatement("DELETE FROM CustomerInfo");
		delstmt.executeUpdate();

		PreparedStatement pstmt = connection
				.prepareStatement("INSERT INTO `CustomerInfo`(id,name,phone) VALUE (?,?,?)");
		pstmt.setInt(1, 1001);
		pstmt.setString(2, "John Doe");
		pstmt.setString(3, "123-456-7890");
		pstmt.executeUpdate();

		pstmt.setInt(1, 1002);
		pstmt.setString(2, "Jane Doe");
		pstmt.setString(3, "123-456-0000");
		pstmt.executeUpdate();

		pstmt.setInt(1, 1003);
		pstmt.setString(2, "Johnny Doe");
		pstmt.setString(3, "123-456-1111");
		pstmt.executeUpdate();
	}

	public void test04CreateDataFromCSV() throws Exception {
		 Connection connection = null;
		 connection =
		 DriverManager.getConnection("jdbc:mysql://localhost:3306/customer",
		 "root", "root");
		
		 //Delete all rows before data setup
		 PreparedStatement delstmt = connection.prepareStatement("DELETE FROM CustomerInfo");
		 delstmt.executeUpdate();
		
		 PreparedStatement pstmt = connection.prepareStatement("INSERT INTO `CustomerInfo`(id,name,phone) VALUE (?,?,?)");


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
