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

/*
 * 获取表中的数目
 * */
public class DBCounter {
	private DBPool s;

	public DBCounter(DBPool pool) {
		this.s = pool;
	}

	public static DBCounter use(DBPool pool) {
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
	public int getCountOf(Object cond) throws ClassNotExistAnnotation {
		RowValueAndTable rowValueAndTable = Processor.TableToRV(cond);
		return ExecuteSQL.executeSQLSingleAndReturnCount(s, buildCounterSQL(rowValueAndTable.getTableName(),
				rowValueAndTable.getRowValue(), DBType.And));
	}
	
	public int getCountOf(Object cond, DBType type) throws ClassNotExistAnnotation {
		RowValueAndTable rowValueAndTable = Processor.TableToRV(cond);
		return ExecuteSQL.executeSQLSingleAndReturnCount(s, buildCounterSQL(rowValueAndTable.getTableName(),
				rowValueAndTable.getRowValue(), type));
	}

	/**
	 * 创建counter的sql语句
	 * 
	 * @param table
	 * 表名称
	 * @param rv
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
		}
		return sql;
	}
}
