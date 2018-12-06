package com.bonc.tools;

import java.lang.reflect.Field;

public class BaseEntity {

	public String toString(){
		String res = "toString: ";
		Field[] fs = this.getClass().getDeclaredFields();
		for(Field i : fs) {
			try {
				i.setAccessible(true);
				res += i.getName() + "=" + i.get(this) + " ";
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return res;
	}

}
