package com.bonc.tools;

import java.util.List;
import javax.script.*;

//import com.sun.script.javascript.RhinoScriptEngineFactory;
//import com.sun.phobos.script.javascript.RhinoScriptEngineFactory;

public class JsEngine {
	
	public static void main(String[] args) {
/*		
		ScriptEngineManager manager=null;		
        manager = new ScriptEngineManager();        
       // manager.registerEngineName("js", RhinoWrapFactory);                
        if ( manager!=null ) { System.out.println( manager);}        
        List<ScriptEngineFactory> list = manager.getEngineFactories();        
        manager.registerEngineName("MyJs", new RhinoScriptEngineFactory());        
        System.out.println("list.size = " + list.size());        
        for (ScriptEngineFactory available : manager.getEngineFactories()) {
            System.out.println( "EngineName = " +available.getEngineName());
        }
*/		
		
		
		String strExpr = "return 1*12+250.79;";
		Double value = JsEngine.calc(strExpr);
		System.out.println(" value = " + value);
		
		
		String strFun = "if (2==1)  return 10;  else return 20;";
		Double valueFun = JsEngine.calc(strFun);
		System.out.println("valueFun = " + valueFun);
		
		//String strFunStr = "return 'A20151224'.substr(0,1);";
		//String strFunStr = "return \"A20151224\".substr(1,8);";
		String strFunStr = "return \"\" ;";
		String valueFunStr = JsEngine.str_calc(strFunStr);
		System.out.println(" valueFunStr = " + valueFunStr);
		
		
	}
	
	public static String str_calc(String expression){
		
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine se = manager.getEngineByName("js");
       
        
        // str= " function foo(){  if (2==1)  return 10;  else return 20; } foo();";

        String sJsFunction = " function foo(){  ${Expresion} } foo(); ";
        sJsFunction = sJsFunction.replace("${Expresion}", expression);

        
        String result="";
		try {
			result = (String)se.eval(sJsFunction);
		} catch (ScriptException e1) {
			e1.printStackTrace();
		}		
		return result;
	}
	
	public static Double calc(String expression){
		
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine se = manager.getEngineByName("js");
       
        
        // str= " function foo(){  if (2==1)  return 10;  else return 20; } foo();";

        String sJsFunction = " function foo(){  ${Expresion} } foo(); ";
        sJsFunction = sJsFunction.replace("${Expresion}", expression);

        
        Double result=0d;
		try {
			result = (Double)se.eval(sJsFunction);
		} catch (ScriptException e1) {
			e1.printStackTrace();
		}		
		return result;
	}
}
