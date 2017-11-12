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

public class DBQueryer {
	private DBPool s;
	
	public DBQueryer(DBPool pool) {
		this.s = pool;
	}
	
	public static DBQueryer use(DBPool pool) {
		return new DBQueryer(pool);
	}
	
	public void closePool() throws SQLException {
		this.s.closePool();
	}
	
	public Object query(Object cond) throws ClassNotExistAnnotation {
		RowValueAndTable rowValueAndTable = Processor.TableToRV(cond);
		return Processor.RowSetToObject(ExecuteSQL.executeSQLAndReturn(s, 
				buildQueryerSQL(rowValueAndTable.getTableName(), rowValueAndTable.getRowValue(), DBType.And)), cond.getClass());
	}
	
	public Object query(Object cond, DBType type) throws ClassNotExistAnnotation {
		RowValueAndTable rowValueAndTable = Processor.TableToRV(cond);
		return Processor.RowSetToObject(ExecuteSQL.executeSQLAndReturn(s, 
				buildQueryerSQL(rowValueAndTable.getTableName(), rowValueAndTable.getRowValue(), type)), cond.getClass());
	}
	
	private String buildQueryerSQL(String table, RowValue rv, DBType type) {
		String sql = "select * from " + table;
		if (rv != null && rv.getRow().size() > 0) {
			List<TextValue> cond = rv.getRow();
			sql += " where ";
			for (int i = 0; i < cond.size(); i++) {
				sql += cond.get(i).getText() + " = '" + cond.get(i).getValue() + "'";
				if (i < cond.size() - 1) {
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
