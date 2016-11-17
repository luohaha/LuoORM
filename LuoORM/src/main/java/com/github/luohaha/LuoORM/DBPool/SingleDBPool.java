package com.github.luohaha.LuoORM.DBPool;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

import com.github.luohaha.LuoORM.ConfReader;



public class SingleDBPool implements DBPool {
	
	private String user;
	private String password;
	private String url;
	private String driver;
	
	public SingleDBPool() throws IOException, ClassNotFoundException{
		this("./conf/db.conf");
	}
	
	public SingleDBPool(String file) throws IOException , ClassNotFoundException {
		
		Map<String, String> conf = ConfReader.create(file).getResult();
		user = conf.get("user");
		password = conf.get("password");
		url = conf.get("url");
		driver = conf.get("driver");
		Class.forName(driver);
	}
	
	public SingleDBPool(String driver, String url, String user, String password) throws IOException , ClassNotFoundException {
		this.driver = driver;
		this.url = url;
		this.user = user;
		this.password = password;
		Class.forName(this.driver);
	}

	@Override
	public synchronized Connection getConnection() throws SQLException {

		return DriverManager.getConnection(url, user, password);
	}

	@Override
	public synchronized void closeConnection(Connection con) throws SQLException {

		con.close();
	}

	@Override
	public void initSource() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closePool() throws SQLException {
		// TODO Auto-generated method stub
		
	}

	
}
