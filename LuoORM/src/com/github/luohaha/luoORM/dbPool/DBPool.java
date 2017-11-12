package com.github.luohaha.luoORM.dbPool;
import java.sql.Connection;
import java.sql.SQLException;



public interface DBPool {
	public Connection getConnection() throws SQLException;
	public void closeConnection(Connection con) throws SQLException;
	public void initSource();
	public void closePool() throws SQLException;
}
