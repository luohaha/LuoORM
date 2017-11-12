package com.github.luohaha.luoORM.dbExecuter;

import java.sql.SQLException;
import java.util.List;
import com.github.luohaha.luoORM.core.ExecuteSQL;
import com.github.luohaha.luoORM.dbPool.DBPool;
import com.github.luohaha.luoORM.table.Processor;


public class DBExecuter {
	private DBPool s;
	
	public DBExecuter(DBPool pool) {
		this.s = pool;
	}
	
	public void closePool() throws SQLException {
		this.s.closePool();
	}
	
	public static DBExecuter use(DBPool pool) {
		return new DBExecuter(pool);
	}

	public DBExecuter execute(String sql) {
		// TODO Auto-generated method stub
		ExecuteSQL.executeSingelSQL(s, sql);
		return this;
	}

	public DBExecuter executeBatch(List<String> sqls) {
		// TODO Auto-generated method stub
		ExecuteSQL.executeSQLBatch(s, sqls);
		return this;
	}

	public Object executeAndReturn(String sql, Class c) {
		return Processor.RowSetToObject(ExecuteSQL.executeSQLAndReturn(s, sql), c);
	}
	
}
