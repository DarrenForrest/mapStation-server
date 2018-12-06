package com.bonc.tools;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

public class JSONTools
{
  public static JSONObject getJSONObject(Object obj)
  {
    JsonConfig jsonConfig = new JsonConfig();
    jsonConfig.setExcludes(new String[] { "handler", "hibernateLazyInitializer" });
    return JSONObject.fromObject(obj, jsonConfig);
  }

  public static JSONArray getJSONArray(Object obj) {
    JsonConfig jsonConfig = new JsonConfig();
    jsonConfig.setExcludes(new String[] { "handler", "hibernateLazyInitializer" });
    return JSONArray.fromObject(obj, jsonConfig);
  }
}
