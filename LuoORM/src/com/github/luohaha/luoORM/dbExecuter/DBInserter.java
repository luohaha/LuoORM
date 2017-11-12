package com.github.luohaha.luoORM.dbExecuter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.github.luohaha.luoORM.exception.BuildSqlException;
import com.github.luohaha.luoORM.exception.ClassNotExistAnnotation;
import com.github.luohaha.luoORM.core.ExecuteSQL;
import com.github.luohaha.luoORM.define.RowValue;
import com.github.luohaha.luoORM.define.RowValueAndTable;
import com.github.luohaha.luoORM.define.TextValue;
import com.github.luohaha.luoORM.dbPool.DBPool;
import com.github.luohaha.luoORM.table.Processor;

public class DBInserter {
	private DBPool s;
	
	public DBInserter(DBPool pool) {
		this.s = pool;
	}
	
	public static DBInserter use(DBPool pool) {
		return new DBInserter(pool);
	}
	
	public void closePool() throws SQLException {
		this.s.closePool();
	}
	
	/**
	 * 插入注解类
	 * @param object
	 * 插入的对象
	 * @return
	 * 接着使用
	 */
	public DBInserter insert(Object object) throws BuildSqlException, ClassNotExistAnnotation {
		RowValueAndTable rowValueAndTable = Processor.TableToRV(object);
		if (rowValueAndTable != null) {
			ExecuteSQL.executeSingelSQL(s, buildInserterSQL(rowValueAndTable.getRowValue(), rowValueAndTable.getTableName()));
		}
		return this;
	}
	
	/**
	 * 插入多个对象
	 * @param objects
	 * 插入的对象们
	 * @return
	 * 接着使用
	 */
	public DBInserter insert(Collection<Object> objects) throws BuildSqlException, ClassNotExistAnnotation {
		for (Object object : objects) {
			RowValueAndTable rowValueAndTable = Processor.TableToRV(object);
			if (rowValueAndTable != null) {
				ExecuteSQL.executeSingelSQL(s, buildInserterSQL(rowValueAndTable.getRowValue(), rowValueAndTable.getTableName()));
			}
		}
		return this;
	}
	
	/**
	 * 批量插入注解类
	 * @param objects
	 * 按顺序插入的对象
	 * @return
	 * 接着使用
	 */
	public DBInserter insertBatch(List<Object> objects) throws BuildSqlException, ClassNotExistAnnotation {
		List<String> sqls = new ArrayList<>();
		for (Object object : objects) {
			RowValueAndTable rowValueAndTable = Processor.TableToRV(object);
			if (rowValueAndTable != null)
				sqls.add(buildInserterSQL(rowValueAndTable.getRowValue(), rowValueAndTable.getTableName()));
		}
		ExecuteSQL.executeSQLBatch(s, sqls);
		return this;
	}
	
	private String buildInserterSQL(RowValue rv, String table) throws BuildSqlException {
		if (rv == null || rv.getRow().isEmpty())
			throw new BuildSqlException();
		List<TextValue> values = rv.getRow();
		String sql = "insert IGNORE into " + table;
		String sqlset = " (";
		String sqlvalues = "values(";
		int count = values.size();
		for (int i = 0; i < count; i++) {
			sqlset += values.get(i).getText();
			sqlvalues += "'" + values.get(i).getValue() + "'";
			if (i < count - 1) {
				sqlset += ",";
				sqlvalues += ",";
			}
		}
		sqlset += ") ";
		sqlvalues += ");";
		sql = sql + sqlset + sqlvalues;
		return sql;
	}

}
