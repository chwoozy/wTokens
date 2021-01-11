package me.yungweezy.tokens.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class connectMysql {

	public static Connection con;
	public static Statement state;
	public static ResultSet rs;
	private static String pass;
	private static String user;
	
	public static Connection getConnection(){
		if (con != null){
			return con;
		}
		
		try {
			Properties cProps = new Properties();
			cProps.put("user", user);
			cProps.put("password", pass);
			cProps.put("autoReconnect", "true");
			cProps.put("maxReconnects", "4");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/prison1_tokens", cProps);
		} catch(Exception e) {
			System.out.println("[SBMysql] Something went wrong connecting!");
			System.out.println(e);
		}
			
		return con;
	}
	
	public static boolean attemptConnect(String username, String password){
		pass = password;
		user = username;
		try {
			Properties cProps = new Properties();
			cProps.put("user", username);
			cProps.put("password", password);
			cProps.put("autoReconnect", "true");
			cProps.put("maxReconnects", "4");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/prison1_tokens", cProps);
			
			// jdbc:mysql://host:port/database , username , password
			// jdbc:mysql://host:port/database , java.util.Properties
			
			state = con.createStatement();
			
			testDatabase();
		} catch(Exception e) {
			System.out.println(e);
			return false;
		}
		return true;
	}
	
	public static void testDatabase(){
		boolean exists = false;
		String quer = "SELECT * FROM PlayerData";
		try {
			rs = state.executeQuery(quer);
			if (rs.next()){
				System.out.println("[SBMysql] table exists");
				exists = true;
			} else {
				System.out.println("[SBMysql] table doesnt exist, creating it now!");
				exists = false;
			}
		} catch (Exception e){
			System.out.println("[SBMysql] Small error of table not existing, the following is fine!");
			System.out.println(e);
		}
		
		if (exists == false){
			String up = "CREATE TABLE PlayerData (uuid VARCHAR(255), TOKENS INTEGER not NULL, PRIMARY KEY (uuid))";
			try {
				state.executeUpdate(up);
			} catch (Exception e) {
				System.out.println("[SBMysql] Couldnt create table, disabling plugin");
				System.out.println(e);
				
				main.pl.getPluginLoader().disablePlugin(main.pl);
				return;
			}
			
			System.out.println("[SBMysql] Created table, adding dummy info");
			
			String addDummy = "insert into PlayerData (uuid, TOKENS) VALUES ('DummyUUID', '69')";
			try {
				PreparedStatement dummyState = getConnection().prepareStatement(addDummy);
				dummyState.executeUpdate();
				System.out.println("[SBMysql] Dummy added succesfully!");
			} catch (SQLException e1) {
				System.out.println("[SBMysql] Error while adding dummy");
				e1.printStackTrace();
			}
		}
	}
}
