package com.bonc.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.StaxDriver;

public class XmlUtils {

	public static String toXML(Object obj) {
		XStream xstream = new XStream(new StaxDriver(new NoNameCoder()));
		xstream.processAnnotations(obj.getClass());
		return xstream.toXML(obj);
	}

	public static <T> T toBean(String xmlStr, Class<T> cls) {
		XStream xstream = new XStream(new StaxDriver(new NoNameCoder()));
		xstream.processAnnotations(cls);
		@SuppressWarnings("unchecked")
		T obj = (T) xstream.fromXML(xmlStr);
		return obj;
	}
	
	public static Map<String, Object> documentToMap(Document document) {
		Map<String, Object> res = new HashMap<String, Object>();
		res.put(document.getRootElement().getName(), elementToMap(document.getRootElement()));
		return res;
	}
	
	@SuppressWarnings("unchecked")
	public static Object elementToMap(Element element) {
		
		if(element == null) {
			return null;
		}
		
		List<?> elements = element.elements();
		if(elements == null || elements.size() == 0) {
			return element.getTextTrim();
		}
		
		Map<String, Object> res = new HashMap<String, Object>();
		for(Object obj : elements) {
			Element i = (Element)obj;
			if(res.containsKey(i.getName())) {
				Object value = res.get(i.getName());
				if(value instanceof List) {
					((ArrayList<Object>) value).add(elementToMap(i));
				} else {
					ArrayList<Object> list = new ArrayList<Object>();
					list.add(value);
					list.add(elementToMap(i));
					res.put(i.getName(), list);
				}
			} else {
				res.put(i.getName(), elementToMap(i));
			}
		}
		return res;
	}
	
}
