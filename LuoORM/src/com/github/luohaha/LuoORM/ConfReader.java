package com.github.luohaha.LuoORM;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class ConfReader {
	private String filename;
	private Map<String, String> conf = new HashMap<>();
	
	public ConfReader(String name) {
		this.filename = name;
	}
	
	public static ConfReader create(String filename) {
		return new ConfReader(filename);
	}
	
	public Map<String, String> getResult() {
		initReader(this.filename);
		return this.conf;
	}
	
	private void initReader(String filename) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String line;
			while ((line = reader.readLine()) != null) {
				parse(line, conf); // 添加
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void parse(String line, Map<String, String> conf) {
		int i = line.indexOf('#');
		if (i >= 0)
			line = line.substring(0, i);
		line = line.trim();
		if (line.matches(".+=.+")) {
			i = line.indexOf('=');
			String name = line.substring(0, i);
			String value = line.substring(i + 1);
			if (!name.trim().equals("") && !value.trim().equals(""))
				conf.put(name.trim(), value.trim());
		}
	}
}
