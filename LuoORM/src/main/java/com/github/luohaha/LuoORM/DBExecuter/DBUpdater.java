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

public class DBUpdater {
	private DBPool s;

	public DBUpdater(DBPool pool) {
		this.s = pool;
	}

	public static DBUpdater create(DBPool pool) {
		return new DBUpdater(pool);
	}

	public void closePool() throws SQLException {
		this.s.closePool();
	}

	/**
	 * 更新，使用注解
	 * 
	 * @param update
	 * 要更新的对象
	 * @param cond
	 * 条件对象
	 * @return
	 * 接着使用
	 */
	public DBUpdater update(Object update, Object cond) {
		RowValueAndTable u = Processor.TableToRV(update);
		RowValueAndTable c = Processor.TableToRV(cond);
		ExecuteSQL.executeSingelSQL(s, buildUpdaterSQL(u.getTableName(), u.getRowValue(), c.getRowValue(), DBType.And));
		return this;
	}

	public DBUpdater update(Object update, Object cond, DBType type) {
		RowValueAndTable u = Processor.TableToRV(update);
		RowValueAndTable c = Processor.TableToRV(cond);
		ExecuteSQL.executeSingelSQL(s, buildUpdaterSQL(u.getTableName(), u.getRowValue(), c.getRowValue(), type));
		return this;
	}

	/**
	 * 批量更新,默认类型
	 * @param update
	 * 批量更新的对象
	 * @param cond
	 * 条件对象
	 * @return
	 * 接着使用
	 */
	public DBUpdater updateBatch(List<Object> update, List<Object> cond) {
		// 构造sql
		List<String> sqls = new ArrayList<>();
		for (int i = 0; i < update.size(); i++) {
			RowValueAndTable u = Processor.TableToRV(update.get(i));
			RowValueAndTable c = Processor.TableToRV(cond.get(i));
			if (i < cond.size()) {
				String sql = buildUpdaterSQL(u.getTableName(), u.getRowValue(), c.getRowValue(), DBType.And);
				sqls.add(sql);
			} else {
				// 没有条件
				String sql = buildUpdaterSQL(u.getTableName(), u.getRowValue(), null, DBType.And);
				sqls.add(sql);
			}
		}
		ExecuteSQL.executeSQLBatch(s, sqls);
		return this;
	}
	
	/**
	 * 批量更新，自定义类型
	 * @param update
	 * 对象
	 * @param cond
	 * 条件
	 * @param type
	 * 类型
	 * @return
	 * 接着使用
	 */
	public DBUpdater updateBatch(List<Object> update, List<Object> cond, DBType type) {
		// 构造sql
		List<String> sqls = new ArrayList<>();
		for (int i = 0; i < update.size(); i++) {
			RowValueAndTable u = Processor.TableToRV(update.get(i));
			RowValueAndTable c = Processor.TableToRV(cond.get(i));
			if (i < cond.size()) {
				String sql = buildUpdaterSQL(u.getTableName(), u.getRowValue(), c.getRowValue(), type);
				sqls.add(sql);
			} else {
				// 没有条件
				String sql = buildUpdaterSQL(u.getTableName(), u.getRowValue(), null, type);
				sqls.add(sql);
			}
		}
		ExecuteSQL.executeSQLBatch(s, sqls);
		return this;
	}

	private String buildUpdaterSQL(String table, RowValue rv, RowValue rvCond, DBType type) {
		List<TextValue> update = rv.getRow();
		String sql = "update " + table + " set ";
		for (int i = 0; i < update.size(); i++) {
			sql += update.get(i).getText() + " = '" + update.get(i).getValue() + "'";
			if (i < update.size() - 1) {
				sql += ", ";
			}
		}
		if (rvCond != null && rvCond.getRow().size() > 0) {
			List<TextValue> cond = rvCond.getRow();
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
