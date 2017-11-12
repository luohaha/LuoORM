package com.github.luohaha.luoORM.define;

import java.util.ArrayList;
import java.util.List;

public class RowValue {
	public enum DBType{And, Or};
	private List<TextValue> row = new ArrayList<>();
	
	public static RowValue create() {
		return new RowValue();
	}
	
	public RowValue set(String k, String v) {
		this.row.add(TextValue.create().setText(k).setValue(v));
		return this;
	}
	
	public RowValue set(String k, int v) {
		this.row.add(TextValue.create().setText(k).setValue(v));
		return this;
	}
	
	public RowValue set(String k, long v) {
		this.row.add(TextValue.create().setText(k).setValue(v));
		return this;
	}
	
	public RowValue set(String k, Double v) {
		this.row.add(TextValue.create().setText(k).setValue(v));
		return this;
	}
	
	public RowValue set(String k, Boolean v) {
		this.row.add(TextValue.create().setText(k).setValue(v));
		return this;
	}

	public List<TextValue> getRow() {
		return row;
	}

	@Override
	public String toString() {
		String ret = "";
		for (TextValue tValue : row) {
			ret += tValue.text + "=>" + tValue.value + "\n";
		}
		return "RowValue [row=" + ret + "]";
	}

	
	
}
