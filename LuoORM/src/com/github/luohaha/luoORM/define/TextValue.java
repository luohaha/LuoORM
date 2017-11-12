package com.github.luohaha.luoORM.define;

public class TextValue {
	String text;
	String value;
	public static TextValue create() {
		return new TextValue();
	}
	
	public TextValue setText(String text) {
		this.text = text;
		return this;
	}
	
	public TextValue setValue(String value) {
		if (value == null) {
			this.value = "null";
		} else {
			this.value = value;
		}
		return this;
	}
	
	public TextValue setValue(int value) {
		this.value = String.valueOf(value);
		return this;
	}
	
	public TextValue setValue(long value) {
		this.value = String.valueOf(value);
		return this;
	}
	
	public TextValue setValue(Double value) {
		this.value = String.valueOf(value);
		return this;
	}
	
	public TextValue setValue(Boolean value) {
		this.value = String.valueOf(value);
		return this;
	}

	public String getText() {
		return text;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "TextValue [text=" + text + ", value=" + value + "]";
	}
	
	
}
