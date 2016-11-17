package com.github.luohaha.LuoORM.Table;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.RowSet;

import com.github.luohaha.LuoORM.RowValue;
import com.github.luohaha.LuoORM.RowValueAndTable;

public class Processor {

	public static RowValueAndTable TableToRV(Object object) {
		Class c = object.getClass();
		if (c.isAnnotationPresent(Table.class)) {
			Table tname = (Table) c.getAnnotation(Table.class);
			RowValue rValue = RowValue.create();
			for (Field field : c.getDeclaredFields()) {
				try {
					field.setAccessible(true);
					if (field.get(object) != null)
						rValue.set(field.getName(), String.valueOf(field.get(object)));
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return new RowValueAndTable(rValue, tname.TableName());
		} else {
			return null;
		}
	}

	public static List<Object> RowSetToObject(RowSet rowset, Class c) {
		List<Object> ret = new ArrayList<>();
		try {
			while (rowset.next()) {
				Object object = c.newInstance();
				for (Field field : c.getDeclaredFields()) {
					field.setAccessible(true);
					if (rowset.getObject(field.getName()) != null) {
						if (field.getType().equals(Boolean.TYPE) || field.getType().equals(Boolean.class)) {
							// boolean
							field.set(object, Boolean.valueOf(String.valueOf(rowset.getObject(field.getName()))));
						} else if (field.getType().equals(Integer.TYPE) || field.getType().equals(Integer.class)) {
							// int
							field.set(object, Integer.valueOf(String.valueOf(rowset.getObject(field.getName()))));
						} else if (field.getType().equals(Byte.TYPE) || field.getType().equals(Byte.class)) {
							// byte
							field.set(object, Byte.valueOf(String.valueOf(rowset.getObject(field.getName()))));
						} else if (field.getType().equals(Short.TYPE) || field.getType().equals(Short.class)) {
							// short
							field.set(object, Short.valueOf(String.valueOf(rowset.getObject(field.getName()))));
						} else if (field.getType().equals(Long.TYPE) || field.getType().equals(Long.class)) {
							// long
							field.set(object, Long.valueOf(String.valueOf(rowset.getObject(field.getName()))));
						} else if (field.getType().equals(Float.TYPE) || field.getType().equals(Float.class)) {
							// float
							field.set(object, Float.valueOf(String.valueOf(rowset.getObject(field.getName()))));
						} else if (field.getType().equals(Double.TYPE) || field.getType().equals(Double.class)) {
							// double
							field.set(object, Double.valueOf(String.valueOf(rowset.getObject(field.getName()))));
						} else {
							field.set(object, String.valueOf(rowset.getObject(field.getName())));
						}
					} //if (rowset.getObject(field.getName()) != null)
				}
				ret.add(object);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
}
