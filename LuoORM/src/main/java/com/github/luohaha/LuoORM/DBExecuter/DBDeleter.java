package com.github.luohaha.LuoORM.DBExecuter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.github.luohaha.LuoORM.ExecuteSQL;
import com.github.luohaha.LuoORM.RowValue;
import com.github.luohaha.LuoORM.RowValue.DBType;
import com.github.luohaha.LuoORM.RowValueAndTable;
import com.github.luohaha.LuoORM.TextValue;
import com.github.luohaha.LuoORM.DBPool.DBPool;
import com.github.luohaha.LuoORM.Table.Processor;

public class DBDeleter {
	private DBPool s;
	
	public DBDeleter(DBPool pool) {
		this.s = pool;
	}
	
	public static DBDeleter create(DBPool pool) {
		return new DBDeleter(pool);
	}
	
	public void closePool() throws SQLException {
		this.s.closePool();
	}

	/**
	 * 删除
	 * @param cond
	 * 条件对象
	 * @return
	 * 可以接着使用
	 */
	public DBDeleter delete(Object cond) {
		RowValueAndTable rv = Processor.TableToRV(cond);
		ExecuteSQL.executeSingelSQL(s, buildDeleteSQL(rv.getTableName(), rv.getRowValue(), DBType.And));
		return this;
	}
	
	/**
	 * 删除
	 * @param cond
	 * 条件对象
	 * @param type
	 * 条件
	 * @return
	 * 可以接着使用
	 */
	public DBDeleter delete(Object cond, DBType type) {
		RowValueAndTable rv = Processor.TableToRV(cond);
		ExecuteSQL.executeSingelSQL(s, buildDeleteSQL(rv.getTableName(), rv.getRowValue(), type));
		return this;
	}

	private String buildDeleteSQL(String table, RowValue rv, DBType type) {
		String sql = "delete from " + table;
		if (rv != null && rv.getRow().size() > 0) {
			List<TextValue> conds = rv.getRow();
			sql += " where ";
			for (int i = 0; i < conds.size(); i++) {
				sql += conds.get(i).getText() + " = '" + conds.get(i).getValue() + "'";
				if (i < conds.size() - 1) {
					if (type == DBType.And) {
						sql += " and ";
					} else {
						sql += " or ";
					}
				}
			}
		}
		return sql;
	}
}
