package com.github.luohaha.luoORM.dbExecuter;

import java.sql.SQLException;
import java.util.List;

import com.github.luohaha.luoORM.exception.ClassNotExistAnnotation;
import com.github.luohaha.luoORM.core.ExecuteSQL;
import com.github.luohaha.luoORM.define.RowValue;
import com.github.luohaha.luoORM.define.RowValue.DBType;
import com.github.luohaha.luoORM.define.RowValueAndTable;
import com.github.luohaha.luoORM.define.TextValue;
import com.github.luohaha.luoORM.dbPool.DBPool;
import com.github.luohaha.luoORM.table.Processor;

public class DBDeleter {
	private DBPool s;
	
	public DBDeleter(DBPool pool) {
		this.s = pool;
	}
	
	public static DBDeleter use(DBPool pool) {
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
	public DBDeleter delete(Object cond) throws ClassNotExistAnnotation {
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
	public DBDeleter delete(Object cond, DBType type) throws ClassNotExistAnnotation {
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
