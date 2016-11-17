package com.github.luohaha.LuoORM;

public class RowValueAndTable {
	private RowValue rowValue;
	private String tableName;
	public RowValueAndTable(RowValue rowValue, String tableName) {
		super();
		this.rowValue = rowValue;
		this.tableName = tableName;
	}
	public RowValue getRowValue() {
		return rowValue;
	}
	public void setRowValue(RowValue rowValue) {
		this.rowValue = rowValue;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	@Override
	public String toString() {
		return "RowValueAndTable [rowValue=" + rowValue + ", tableName=" + tableName + "]";
	}
	
}
