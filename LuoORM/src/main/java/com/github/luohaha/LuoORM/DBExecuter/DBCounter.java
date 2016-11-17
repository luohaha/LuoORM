package com.github.luohaha.LuoORM.DBExecuter;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.github.luohaha.LuoORM.ExecuteSQL;
import com.github.luohaha.LuoORM.RowValue;
import com.github.luohaha.LuoORM.RowValue.DBType;
import com.github.luohaha.LuoORM.RowValueAndTable;
import com.github.luohaha.LuoORM.TextValue;
import com.github.luohaha.LuoORM.DBPool.DBPool;
import com.github.luohaha.LuoORM.Table.Processor;

/*
 * 获取表中的数目
 * */
public class DBCounter {
	private DBPool s;

	public DBCounter(DBPool pool) {
		this.s = pool;
	}

	public static DBCounter create(DBPool pool) {
		return new DBCounter(pool);
	}

	public void closePool() throws SQLException {
		this.s.closePool();
	}
	
	/**
	 * 获取个数
	 * @param cond
	 * 与表对应的对象
	 * @return
	 * 返回个数
	 */
	public int getCountOf(Object cond) {
		RowValueAndTable rowValueAndTable = Processor.TableToRV(cond);
		return ExecuteSQL.executeSQLSingleAndReturnCount(s, buildCounterSQL(rowValueAndTable.getTableName(),
				rowValueAndTable.getRowValue(), DBType.And));
	}
	
	public int getCountOf(Object cond, DBType type) {
		RowValueAndTable rowValueAndTable = Processor.TableToRV(cond);
		return ExecuteSQL.executeSQLSingleAndReturnCount(s, buildCounterSQL(rowValueAndTable.getTableName(),
				rowValueAndTable.getRowValue(), type));
	}

	/**
	 * 创建counter的sql语句
	 * 
	 * @param table
	 * 表名称
	 * @param tv
	 * rv
	 * @param type
	 * 类型
	 * @return
	 * 返回sql
	 */
	private String buildCounterSQL(String table, RowValue rv, DBType type) {
		String sql = "select count(*) from " + table;
		if (rv != null && rv.getRow().size() > 0) {
			List<TextValue> tv = rv.getRow();
			sql += " where ";
			for (int i = 0; i < tv.size(); i++) {
				sql += tv.get(i).getText() + " = '" + tv.get(i).getValue() + "'";
				if (i < tv.size() - 1) {
					if (type == DBType.And) {
						sql += " and ";
					} else {
						sql += " or ";
					}
				}
			}
		} else {
			
		}
		return sql;
	}
}
