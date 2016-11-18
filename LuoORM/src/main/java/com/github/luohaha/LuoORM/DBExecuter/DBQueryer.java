package com.github.luohaha.LuoORM.DBExecuter;

import java.sql.SQLException;
import java.util.List;
import com.github.luohaha.LuoORM.ExecuteSQL;
import com.github.luohaha.LuoORM.RowValue;
import com.github.luohaha.LuoORM.RowValue.DBType;
import com.github.luohaha.LuoORM.RowValueAndTable;
import com.github.luohaha.LuoORM.TextValue;
import com.github.luohaha.LuoORM.DBPool.DBPool;
import com.github.luohaha.LuoORM.Table.Processor;

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
	
	public Object query(Object cond) {
		RowValueAndTable rowValueAndTable = Processor.TableToRV(cond);
		return Processor.RowSetToObject(ExecuteSQL.executeSQLAndReturn(s, 
				buildQueryerSQL(rowValueAndTable.getTableName(), rowValueAndTable.getRowValue(), DBType.And)), cond.getClass());
	}
	
	public Object query(Object cond, DBType type) {
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
