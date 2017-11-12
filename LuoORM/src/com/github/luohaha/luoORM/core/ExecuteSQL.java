package com.github.luohaha.luoORM.core;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Logger;

import javax.sql.RowSet;
import javax.sql.rowset.CachedRowSet;

import com.github.luohaha.luoORM.dbPool.DBPool;
import com.sun.rowset.CachedRowSetImpl;
/**
 * 数据库执行操作
 * @author luoyixin
 *
 */
public class ExecuteSQL {

	/**
	 * 执行单条sql语句
	 * 
	 * @param s
	 * 使用的线程池
	 * @param sql
	 * 要执行的sql语句
	 */
	public static void executeSingelSQL(DBPool s, String sql) {
		Statement stmt = null;
		Connection conn = null;
		try {
			conn = s.getConnection();
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);

		} catch (Exception e) {
			Logger.getLogger("logger").severe(e.toString());
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					Logger.getLogger("logger").severe(e.toString());
				}
			}
			if (conn != null) {
				try {
					s.closeConnection(conn);
				} catch (SQLException e) {
					Logger.getLogger("logger").severe(e.toString());
				}
			}
		}
		System.err.println(sql);
	}

	public static void executeSQLBatch(DBPool s, List<String> sqls) {
		Statement stmt = null;
		Savepoint savepoint = null;
		Connection conn = null;
		try {
			conn = s.getConnection();
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			savepoint = conn.setSavepoint(); // 設定save point
			/*
			 * 循环添加批次
			 */
			for (String each : sqls) {
				stmt.addBatch(each);
			}
			stmt.executeBatch();
			/*
			 * 如果没有出错
			 */
			conn.commit();
			conn.releaseSavepoint(savepoint);
		} catch (SQLException e) {
			Logger.getLogger("logger").warning(e.toString());
			try {
				conn.rollback(savepoint);
			} catch (SQLException e1) {
				Logger.getLogger("logger").severe(e1.toString());
			}
		} catch (Exception e) {
			Logger.getLogger("logger").severe(e.toString());
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					Logger.getLogger("logger").severe(e.toString());
				}
			}
			if (conn != null) {
				try {
					s.closeConnection(conn);
				} catch (SQLException e) {
					Logger.getLogger("logger").severe(e.toString());
				}
			}
		}
		System.err.println("update count : " + sqls.size());
	}
	
	
	/**
	 * 执行单条sql语句，并返回得到的值
	 * @param s
	 * 线程池
	 * @param sql
	 * 要执行的sql语句
	 * @return
	 * 得到的个数
	 */
	public static int executeSQLSingleAndReturnCount(DBPool s, String sql) {
		Statement stmt = null;
		Connection conn = null;
		ResultSet resultSet = null;
		int res = 0;
		try {
			conn = s.getConnection();
			stmt = conn.createStatement();
			resultSet = stmt.executeQuery(sql);
			while (resultSet.next()) {
				res = resultSet.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					Logger.getLogger("logger").severe(e.toString());
				}
			}
			if (conn != null) {
				try {
					s.closeConnection(conn);
				} catch (SQLException e) {
					Logger.getLogger("logger").severe(e.toString());
				}
			}
		}
		System.err.println(sql);
		return res;
	}
	
	/**
	 * 执行sql并返回结果
	 * @param s
	 * 使用的线程池
	 * @param sql
	 * 执行的sql语句
	 * @return
	 * 返回结果
	 */
	public static RowSet executeSQLAndReturn(DBPool s, String sql) {
		Statement stmt = null;
		Connection conn = null;
		ResultSet resultSet = null;
		CachedRowSet rowset = null;
		try {
			conn = s.getConnection();
			stmt = conn.createStatement();
			resultSet = stmt.executeQuery(sql);
			rowset = new CachedRowSetImpl();
			rowset.populate(resultSet);
			resultSet.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					Logger.getLogger("logger").severe(e.toString());
				}
			}
			if (conn != null) {
				try {
					s.closeConnection(conn);
				} catch (SQLException e) {
					Logger.getLogger("logger").severe(e.toString());
				}
			}
		}
		System.err.println(sql);
		return rowset;
	}
	
}
