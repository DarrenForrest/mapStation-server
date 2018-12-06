package com.bonc.microapp.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bonc.DownloadBD;
import com.bonc.db.IDao;
import com.bonc.db.TxException;
import com.bonc.microapp.entity.MapBean;
import com.bonc.microapp.entity.MapPoiInfo;
import com.bonc.microapp.entity.MapPoiSearchAk;
import com.bonc.microapp.entity.MapPoiSearchInfo;
import com.bonc.microapp.entity.MapPoiSearchRecord;
import com.bonc.microapp.entity.MapPoiSearchTotal;
import com.bonc.microapp.entity.MapResource;
import com.bonc.microapp.entity.MapResourceState;
import com.bonc.microapp.entity.Range;
import com.bonc.microapp.service.MapPoiInfoService;
import com.bonc.microapp.service.MapPoiSearchInfoService;
import com.bonc.microapp.service.MapPoiService;
import com.bonc.microapp.service.MapResourceService;
import com.bonc.microapp.service.MapResourceStateService;
import com.bonc.microapp.service.MapStationInfoService;
import com.bonc.tools.AESpkcs7paddingUtil;
import com.bonc.tools.AmsTool;
import com.bonc.tools.DateUtils;
import com.bonc.tools.JsonBody;
import com.bonc.tools.Page;
import com.bonc.tools.ParamVo;
import com.bonc.tools.TheadUtil;

/**
 * @author liudong
 *
 */
@Scope("prototype")
@Controller
@RequestMapping({ "/mapPoi" })
public class MapPoiInfoAction  implements Runnable{


	@Autowired
	private  MapPoiInfoService mapPoiInfoService;

	@Autowired
	private  MapResourceService mapResourceService;
	
	@Autowired
	private  MapResourceStateService mapResourceStateService;
	
	@Autowired
	private  MapStationInfoService mapStationInfoService;
	
	@Autowired
	private MapPoiSearchInfoService mapPoiSearchInfoService;
	
	@Autowired
	private MapPoiService mapPoiService;
	
	@Autowired
	private IDao mapPoiSearchTotalDao;
	
	private String name;
	private String val;
	private String state;
	private String cityCode;
	public void setState(String state) {
		this.state = state;
	}
	public void setVal(String val) {
		this.val = val;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	
	//跳转到POI与基站关联的页面
	@RequestMapping(value = {"getMpoi.action", "/" }, produces = { "application/json;charset=UTF-8" })
	public ModelAndView getMpoi(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> form) throws InterruptedException {		
		ModelAndView view = new ModelAndView("/map/mapPoi");
		return view;
	}
	
	//跳转到POI与基站关联的页面
	@RequestMapping(value = {"searchPoi.action", "/" }, produces = { "application/json;charset=UTF-8" })
	public ModelAndView searchPoi(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> form) throws InterruptedException {		
		ModelAndView view = new ModelAndView("/map/mapSearchPoi");
		return view;
	}

	/**
	 * 	@RequestMapping(value = { "addMapStation.action", "/" }, produces = { "application/json;charset=UTF-8" })
	 * @param request
	 * @param response
	 * @param form
	 * @return
	 * @throws TxException
	 * 
	 * 多线程  11个地市线程 将兴趣点与基站关联
	 * name : 兴趣点         state : 0表示获取半径范围内的基站  1表示获取前 top N 个基站 
	 */
	@RequestMapping(value = {"addMapStation.action", "/" }, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public Object getResource(HttpServletRequest request,	HttpServletResponse response, @RequestParam Map<String, Object> form) throws TxException {
		JsonBody res = new JsonBody();		
		JsonBody jsonBody = new JsonBody(form);
		String name = jsonBody.getValue("name").toString();
		String state = jsonBody.getValue("state").toString();
		String val = jsonBody.getValue("val").toString();
		 for (int a = 140100; a <=141100; a=a+100) {
			 try {
			 MapPoiInfoAction mapResourceAction = new MapPoiInfoAction();
			    mapResourceAction.setState(state);
			    mapResourceAction.setCityCode(String.valueOf(a));
			    mapResourceAction.setVal(val);
			    mapResourceAction.setName(name);
				Thread thread = new Thread(mapResourceAction);
				thread.start();
			} catch (Exception e) {
				// TODO Auto-generated catch block
			    e.printStackTrace();
			}
		}
		
		return "";
	}

	@Override
	public void run() {
	    try{ 
	    	ParamVo vo=new ParamVo();
	    	if(mapPoiSearchInfoService == null){
	    		mapPoiSearchInfoService=(MapPoiSearchInfoService)TheadUtil.getBean("mapPoiSearchInfoService");
	    	}
	    	vo.put("keyWord", name);
	    	vo.put("cityCode", cityCode);
	    	vo.setObjectClass(MapPoiSearchInfo.class);
			List<MapPoiSearchInfo> list=mapPoiSearchInfoService.findPOI(vo);
			if(list.size()!=0){
			for (MapPoiSearchInfo mapPoiSearchInfo : list) {
				 String name=mapPoiSearchInfo.getPoiName();
				 String city=mapPoiSearchInfo.getCityCode();
				 MapBean mapBean=new MapBean();
				  mapBean.setAddress(mapPoiSearchInfo.getAddress());
	    		  mapBean.setCityCode(mapPoiSearchInfo.getCityCode());
	    		  mapBean.setFirstType(mapPoiSearchInfo.getStdTag());
	    		  mapBean.setLat(Double.valueOf(mapPoiSearchInfo.getLat()));
	    		  mapBean.setLng(Double.valueOf(mapPoiSearchInfo.getLng()));
	    		  mapBean.setMapName(name);
	    		  mapBean.setSecondType(mapPoiSearchInfo.getKeyWord());
	    		  mapBean.setRange(mapPoiSearchInfo.getRange());
	    		  if(mapStationInfoService == null){
	    			  mapStationInfoService=(MapStationInfoService)TheadUtil.getBean("mapStationInfoService");
			    	}
	    		  if(state.equals("0")) {
	    			  List<MapBean> findStation = mapStationInfoService.txFindStation(mapBean,val);
	    		  }else {
	    			  List<MapBean> station = mapStationInfoService.txGetTopNStation(mapBean, val); 
	    		  }
	
//			      mapPoiInfoService.updateSid(mapPoiInfo);    
			}
       }
	}catch (Exception e) {
		// TODO: handle exception
		e.printStackTrace();
	}
		
	}
	
	/**
	 * 	@RequestMapping(value = { "searchPoiSave.action", "/" }, produces = { "application/json;charset=UTF-8" })
	 * @param request
	 * @param response
	 * @param form
	 * @return
	 * @throws TxException
	 * 
	 * 多线程  11个地市线程 将兴趣点与基站关联
	 * name : 兴趣点         state : 0表示获取半径范围内的基站  1表示获取前 top N 个基站 
	 * @throws Exception 
	 */
	@RequestMapping(value = {"searchPoiSave.action", "/" }, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public Object searchPoiSave(HttpServletRequest request,	HttpServletResponse response, @RequestParam Map<String, Object> form) throws TxException, Exception {
		JsonBody res = new JsonBody();		
		JsonBody jsonBody = new JsonBody(form);
		String cityCode = jsonBody.getValue("cityCode").toString();
		String areaCode = jsonBody.getValue("areaCode").toString();
		String firstPoi = new String(jsonBody.getValue("firstPoi").toString().getBytes("ISO-8859-1"),"UTF-8");
		String secondPoi = new String(jsonBody.getValue("secondPoi").toString().getBytes("ISO-8859-1"),"UTF-8");
		String dealType =  jsonBody.getValue("dealType").toString();
		try{
			ParamVo vo = new ParamVo();
			vo.put("cityCode", cityCode);
			vo.put("areaCode", areaCode);
			vo.put("firstPoi", firstPoi);
			vo.put("secondPoi", secondPoi);
			vo.setMethod("selectPoiSearchTotalForAlone");
			vo.setObjectClass(MapPoiSearchTotal.class);
			List<Object> totals = this.mapPoiSearchTotalDao.selectList(vo);
			if(totals != null && totals.size()>0){
				saveSearchPoi(totals,areaCode,dealType,cityCode);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		ModelAndView view = new ModelAndView("/map/mapSearchPoi");
		return view;
	}
	
	public void saveSearchPoi(List<Object> totals,String areaCode,String dealType,String cityCode) throws Exception{
		for(Object resobj:totals){
			ParamVo vo = new ParamVo();
			MapPoiSearchTotal res=(MapPoiSearchTotal) resobj;
			boolean flag = true;
			if("0".equals(dealType)){
				List<MapPoiSearchRecord> listRecord = this.mapPoiService.getMapPoiSearchRecordForNoAk(res, areaCode, "noAK");
		    	 if(listRecord.size() != 0){
		    		 flag=false;
		    	 }
			}
			if(flag){
				DownloadBD dd = new DownloadBD(res.getKeyWord(), res.getWd2Name(),res.getRegionName(), res.getRegionCode());
				List<String> download = dd.download();
				for (String string : download){
						System.out.println(string);
				        JSONObject obj = JSON.parseObject(string);
					    MapPoiSearchInfo mapPoiSearchInfo=new MapPoiSearchInfo();
					    Map<String,Object> map=new HashMap<String,Object>(); 
					    StringBuffer sb=new StringBuffer();
					    mapPoiSearchInfo.setAddress(obj.getString("addr"));
					    mapPoiSearchInfo.setCityCode(areaCode);
					    mapPoiSearchInfo.setBdUid(obj.getString("uid"));
					    mapPoiSearchInfo.setKeyWord(res.getKeyWord());
					    mapPoiSearchInfo.setLat(obj.getString("lat"));
					    mapPoiSearchInfo.setLng(obj.getString("lng"));
					    mapPoiSearchInfo.setPoiName(obj.getString("name"));
					    mapPoiSearchInfo.setRegionCode(res.getRegionCode());
					    mapPoiSearchInfo.setRegionName(res.getRegionName());
					    mapPoiSearchInfo.setSearchType("noAK");
					    if(obj.containsKey("range")){
					    	JSONArray parseArray2 = JSONArray.parseArray(obj.getString("range"));
						    for (int a = 0; a < parseArray2.size(); a++) {
						    	JSONObject job = parseArray2.getJSONObject(a);
						    	Range range2 = new Range();
						    	range2.setLat(Double.valueOf(job.getString("lat")));
						    	range2.setLng(Double.valueOf(job.getString("lng")));
						    	sb.append(range2.getLng()+","+range2.getLat()+",");
						    }
						    String range=sb.toString();
						    range=range.substring(0,range.length()-1);
						    mapPoiSearchInfo.setRange(range);
						    map.put("range", range);
					   }
					   if(obj.getString("stdTag")=="" || obj.getString("stdTag").isEmpty()) {
					      mapPoiSearchInfo.setStdTag(obj.getString("diTag")); 
						  map.put("stdTag", obj.getString("diTag")); 
					    }else {
					      mapPoiSearchInfo.setStdTag(obj.getString("stdTag")); 
						  map.put("stdTag", obj.getString("stdTag")); 
					    } 
					    map.put("address", obj.getString("addr"));
					    map.put("cityCode",areaCode);
					    map.put("bdUid", obj.getString("uid"));
					    map.put("keyWord", res.getKeyWord());
					    map.put("lat", obj.getString("lat"));
					    map.put("lng", obj.getString("lng"));
					    map.put("poiName", obj.getString("name"));
					    map.put("regionCode", res.getRegionCode());
					    map.put("regionName", res.getRegionName());
					    map.put("searchType", "noAK");
					    vo = new ParamVo();
					    vo.setParam(map);
					    vo.setObject(mapPoiSearchInfo);
					    vo.setObjectClass(MapPoiSearchInfo.class);
					    MapPoiSearchInfo selectByBdUid = this.mapPoiSearchInfoService.selectByBdUid(vo);
					    if(selectByBdUid != null){
					    	mapPoiSearchInfo.setUpdateTime(DateUtils.getNow("yyyy-MM-dd HH:mm:ss"));
					    	map.put("updateTime",DateUtils.getNow("yyyy-MM-dd HH:mm:ss"));
					    	vo = new ParamVo();
						    vo.setParam(map);
						    vo.setObject(mapPoiSearchInfo);
						    vo.setObjectClass(MapPoiSearchInfo.class);
					    	this.mapPoiSearchInfoService.tnUpdateInfo(vo);
					    }else {
						    mapPoiSearchInfo.setCreateTime(DateUtils.getNow("yyyy-MM-dd HH:mm:ss")); 
							map.put("createTime",DateUtils.getNow("yyyy-MM-dd HH:mm:ss"));
							vo = new ParamVo();
							vo.setParam(map);
							vo.setObject(mapPoiSearchInfo);
							vo.setObjectClass(MapPoiSearchInfo.class);
							this.mapPoiSearchInfoService.tnSave(vo);
					    }	    		 
					}	
				//保存结果记录
			    this.mapPoiService.tnSavePoiSearchRecord(res,areaCode,"noAK",download.size());
			}
		
			if(Integer.valueOf(res.getTotal())>750 && StringUtils.isNotBlank(res.getBounds()) && 
					StringUtils.isNotBlank(res.getStepNum())){
				vo = new ParamVo();
				Map<String,Object> m=new HashMap<String,Object>(); 
			     m.put("cityCode",cityCode);
			     vo.setParam(m);
			     vo.setObjectClass(MapPoiSearchAk.class);
			     vo.setMethod("selectById");
			     MapPoiSearchAk searchAk = (MapPoiSearchAk) mapPoiSearchInfoService.selectAk(vo);
			     if(StringUtils.isBlank(areaCode)){
			    	 areaCode = res.getAreaCode();
			     }
				this.mapPoiService.dealSearchPoiByBounds(res,searchAk,areaCode,dealType);
			}
		}
	}
	
}
