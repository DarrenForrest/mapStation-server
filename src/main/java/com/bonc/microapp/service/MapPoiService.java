package com.bonc.microapp.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bonc.BMapSearch;
import com.bonc.DownloadBD;
import com.bonc.db.IDao;
import com.bonc.microapp.entity.MapAreaInfo;
import com.bonc.microapp.entity.MapPoiSearchAk;
import com.bonc.microapp.entity.MapPoiSearchInfo;
import com.bonc.microapp.entity.MapPoiSearchNoData;
import com.bonc.microapp.entity.MapPoiSearchRecord;
import com.bonc.microapp.entity.MapPoiSearchTotal;
import com.bonc.microapp.entity.MapResource;
import com.bonc.microapp.entity.Range;
import com.bonc.tools.DateUtils;
import com.bonc.tools.ParamVo;
import com.bonc.tools.TheadUtil;

@Service
public class MapPoiService implements Runnable{
	@Autowired
    private MapResourceService mapResourceService;
	@Autowired
	private MapPoiSearchInfoService mapPoiSearchInfoService;
	@Autowired
    private IDao mapAreaInfoDao;

	private MapAreaInfo mapAreaInfo;
	public MapAreaInfo getMapAreaInfo() {
		return mapAreaInfo;
	}
	public void setMapAreaInfo(MapAreaInfo mapAreaInfo) {
		this.mapAreaInfo = mapAreaInfo;
	}
	private String cityCode;
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	
	/**
	 * @param cityCode
	 * @return
	 * poi数据获取
	 *  根据地市编码与后台返回的标签 调用 DownloadBD(poiType,cityCode);百度地图api获取出全部的poi,
	 *  通过id查询poi数据表 有则更新 无则插入
	 *  将标签分成一个礼拜的数量分开跑数
	 * @throws Exception 
	 */
	public String getPoiInfo(String cityCode,String ak) {
//		 List<Object> selectList=new ArrayList<Object>();
		 Map<String,Object> m=new HashMap<String,Object>(); 
		 ParamVo vo = new ParamVo();
		 m.put("parentAreaCode", cityCode);
		 vo.setParam(m);
		 vo.setObjectClass(MapAreaInfo.class);
		 vo.setMethod("selectByCityCode");
		 List<Object> selectList = mapAreaInfoDao.selectList(vo); 
		 int i=0;
		 for (Object object : selectList) {
		      MapAreaInfo res=(MapAreaInfo) object;
		      MapPoiService mapPoiService = new MapPoiService();
		      mapPoiService.setMapAreaInfo(res);
		      mapPoiService.setCityCode(cityCode);
		      Thread thread = new Thread(mapPoiService);
		      System.out.println("====="+i);
			  thread.start();
			  i++;
		}
	    return null;
	}
	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Map<String,Object> m=new HashMap<String,Object>(); 
	     ParamVo vo = new ParamVo();
	     m.put("regionName", getCityname(mapAreaInfo.getAreaName()));
	     vo.setParam(m);
	     vo.setObjectClass(MapPoiSearchTotal.class);
	     vo.setMethod("selectList");
	     if(mapPoiSearchInfoService == null){
	    	 mapPoiSearchInfoService=(MapPoiSearchInfoService)TheadUtil.getBean("mapPoiSearchInfoService");
	     }
	     System.out.println(mapAreaInfo);
	     List<Object> selectList = mapPoiSearchInfoService.selectTotalList(vo);  
	     
	     vo = new ParamVo();
	     m.put("cityCode",cityCode);
	     vo.setParam(m);
	     vo.setObjectClass(MapPoiSearchAk.class);
	     vo.setMethod("selectById");
	     MapPoiSearchAk searchAk = (MapPoiSearchAk) mapPoiSearchInfoService.selectAk(vo);
	     
	     for (Object object : selectList) {	 
	    	 MapPoiSearchTotal res=(MapPoiSearchTotal) object;
	    	 String areaCode = getAreaCityCode(res.getRegionName());
	    	 if(StringUtils.isBlank(areaCode)){
	    		 areaCode = cityCode;
	    	 }
	    	 List<MapPoiSearchRecord> listRecord = getMapPoiSearchRecordForNoAk(res, areaCode, "noAK");
	    	 if(listRecord.size() == 0){
	    		 DownloadBD dd = new DownloadBD(res.getKeyWord(), res.getWd2Name(),res.getRegionName(), res.getRegionCode());
		    	 try {
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
				    tnSavePoiSearchRecord(res,areaCode,"noAK",download.size());
				} catch (Exception e) {
					e.printStackTrace();
				}
	    	 }
	    	 if(Integer.valueOf(res.getTotal())>750 && StringUtils.isNotBlank(res.getBounds()) && 
						StringUtils.isNotBlank(res.getStepNum())){
					dealSearchPoiByBounds(res,searchAk,areaCode,"0");
			}
		}
	}
	
	
	 public void tnSavePoiSearchRecord(MapPoiSearchTotal res, String areaCode, String searchType, int resNos) {
		ParamVo vo = new ParamVo();
		MapPoiSearchRecord record = new MapPoiSearchRecord();
		record.setFirstPoi(res.getWd2Name());
		record.setSecondPoi(res.getKeyWord());
		record.setRegionName(res.getRegionName());
		record.setRegionCode(res.getRegionCode());
		record.setAreaCode(areaCode);
		record.setSearchType(searchType);
		record.setSearchResult(resNos+"");
		vo.setObject(record);
		vo.setObjectClass(MapPoiSearchRecord.class);
		if(mapPoiSearchInfoService == null){
	    	 mapPoiSearchInfoService=(MapPoiSearchInfoService)TheadUtil.getBean("mapPoiSearchInfoService");
	     }
		this.mapPoiSearchInfoService.tnSaveMapPoiSearchRecord(vo);
	}

	 public List<MapPoiSearchRecord> getMapPoiSearchRecordForNoAk(MapPoiSearchTotal res, String areaCode, String searchType){
		List<MapPoiSearchRecord> list =  new ArrayList<MapPoiSearchRecord>();
		ParamVo vo = new ParamVo();
		vo.put("firstPoi", res.getWd2Name());
		vo.put("secondPoi", res.getKeyWord());
		vo.put("regionName", res.getRegionName());
		vo.put("regionCode", res.getRegionCode());
		vo.put("searchType", searchType);
		vo.setMethod("selectById");
		vo.setObjectClass(MapPoiSearchRecord.class);
		if(mapPoiSearchInfoService == null){
	    	 mapPoiSearchInfoService=(MapPoiSearchInfoService)TheadUtil.getBean("mapPoiSearchInfoService");
	     }
		List<Object> listObjs = this.mapPoiSearchInfoService.selectMapPoiSearchRecordList(vo);
		if(listObjs != null){
			for(Object obj:listObjs){
				MapPoiSearchRecord record = (MapPoiSearchRecord) obj;
				list.add(record);
			}
		}
		return list;
	}
	 
	public List<String[]> getMapPoiSearchRecordForAk(MapPoiSearchTotal res, String areaCode, String searchType,String dealType){
		List<String[]> list =  new ArrayList<String[]>();
		ParamVo vo = new ParamVo();
		vo.put("firstPoi", res.getWd2Name());
		vo.put("secondPoi", res.getKeyWord());
		vo.put("regionName", res.getRegionName());
		vo.put("regionCode", res.getRegionCode());
		vo.put("searchType", searchType);
		vo.put("bounds",res.getBounds());
		vo.put("stepNum",res.getStepNum());
		vo.setMethod("selectById");
		vo.setObjectClass(MapPoiSearchRecord.class);
		if(mapPoiSearchInfoService == null){
	    	 mapPoiSearchInfoService=(MapPoiSearchInfoService)TheadUtil.getBean("mapPoiSearchInfoService");
	     }
		List<Object> listObjs = this.mapPoiSearchInfoService.selectMapPoiSearchRecordList(vo);
		if(listObjs != null && listObjs.size()>0 && "0".equals(dealType)){
			for(Object obj:listObjs){
				MapPoiSearchRecord record = (MapPoiSearchRecord) obj;
				String[]  str = new String[12];
				str[0]=record.getFirstPoi();
				str[1]=record.getSecondPoi();
				str[2]=record.getRegionName();
				str[3]=record.getRegionCode();
				str[4]=record.getAreaCode();
				str[5]=record.getBounds();
				str[6]=record.getStepNum();
				str[7]=record.getSmallBounds();
				str[8]=record.getPageNum();
				str[9]=record.getSearchResult();
				str[10]=record.getSearchType();
				str[11]=record.getCreateTime();
				list.add(str);
			}
		}else{
			String[]  str = new String[12];
			str[0]=res.getWd2Name();
			str[1]= res.getKeyWord();
			str[2]=res.getRegionName();
			str[3]=res.getRegionCode();
			str[4]=areaCode;
			str[5]=res.getBounds();
			str[6]=res.getStepNum();
			str[7]="";
			str[8]="";
			str[9]="";
			str[10]=searchType;
			str[11]="";
			list.add(str);
		}
		return list;
	}
	 
	public void dealSearchPoiByBounds(MapPoiSearchTotal res,MapPoiSearchAk searchAk,String areaCode,String dealType) {
		 BMapSearch bms = new BMapSearch();
		 ParamVo vo = new ParamVo();
		 try {
			 String ak="3k0KG0jADuc0GFziTgOWHamzH9KCSdLh";
			 int quota=2000;
			 if(searchAk != null && StringUtils.isNotBlank(searchAk.getAk()) && StringUtils.isNotBlank(searchAk.getQuotaNum())){
				 ak = searchAk.getAk();
				 quota = Integer.valueOf(searchAk.getQuotaNum());
			 }
	     List<String[]> listSearchRecord = getMapPoiSearchRecordForAk(res, areaCode,"AK",dealType);		 
	     List<String[]> listNoData = getPoiSearchNoData(res);
		 List<String> list = bms.searchBMapInfoByBoundsKeyWord(res.getBounds(),Integer.valueOf(res.getStepNum().split("\\*")[0]),Integer.valueOf(res.getStepNum().split("\\*")[1]),res.getKeyWord(),ak,res.getWd2Name(),listNoData,quota,listSearchRecord);
	     if(mapPoiSearchInfoService == null){
   	    	 mapPoiSearchInfoService=(MapPoiSearchInfoService)TheadUtil.getBean("mapPoiSearchInfoService");
   	     }
   		 for(String str:list){
   			 System.out.println(str);
   			 if(str.contains("矩形区域子区域未检索到POI数据:")){
   				 str=str.replace("矩形区域子区域未检索到POI数据:", "");
   				 String[] strs = str.split("\\|\\|");
   				 //(recti)+"||"+smallRect+"||"+(pageNum)
   				 //map_poisearch_nodata
   				 MapPoiSearchNoData nodata = new MapPoiSearchNoData();
   				 nodata.setKeyWord(res.getKeyWord());
   				 nodata.setSmallBounds(strs[1]);
   				 nodata.setPageNum(strs[2]);
   				 nodata.setStepNum(res.getStepNum());
   				 nodata.setRegionName(res.getRegionName());
   				 vo = new ParamVo();
				 vo.setObject(nodata);
				 vo.setObjectClass(MapPoiSearchNoData.class);
				 this.mapPoiSearchInfoService.tnMapPoiSearchNoData(vo);
   			 }else{
   				 JSONObject obj = JSON.parseObject(str);
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
				    mapPoiSearchInfo.setSearchType("AK");
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
				   if(StringUtils.isBlank(obj.getString("tag"))) {
				      mapPoiSearchInfo.setStdTag(res.getWd2Name()); 
					  map.put("stdTag",res.getWd2Name()); 
				    }else {
				      mapPoiSearchInfo.setStdTag(obj.getString("tag")); 
					  map.put("stdTag", obj.getString("tag")); 
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
				    map.put("searchType", "AK");
				    vo = new ParamVo();
				    vo.setParam(map);
				    vo.setObject(mapPoiSearchInfo);
				    vo.setObjectClass(MapPoiSearchInfo.class);
				    MapPoiSearchInfo selectByBdUid = this.mapPoiSearchInfoService.selectByBdUid(vo);
				    if(selectByBdUid != null) {
				    	mapPoiSearchInfo.setUpdateTime(DateUtils.getNow("yyyy-MM-dd HH:mm:ss"));
				    	map.put("updateTime",DateUtils.getNow("yyyy-MM-dd HH:mm:ss"));
				    	vo = new ParamVo();
					    vo.setParam(map);
					    vo.setObject(mapPoiSearchInfo);
					    vo.setObjectClass(MapPoiSearchInfo.class);
				    	this.mapPoiSearchInfoService.tnUpdateInfo(vo);
				    }else {
					    mapPoiSearchInfo.setCreateTime(DateUtils.getNow("yyyy-MM-dd HH:mm:ss")); 
						map.put("createTime",DateUtils.getNow("yyyy-MM-dd HH:mm:ss") );
						vo = new ParamVo();
						vo.setParam(map);
						vo.setObject(mapPoiSearchInfo);
						vo.setObjectClass(MapPoiSearchInfo.class);
						this.mapPoiSearchInfoService.tnSave(vo);
				    }
   			 }
			}
		 }catch(Exception ex){
   		 ex.printStackTrace();
   	 }
	}

	private List<String[]> getPoiSearchNoData(MapPoiSearchTotal res) {
		List<String[]> list = new ArrayList<String[]>();
		ParamVo vo = new ParamVo();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("regionName", res.getRegionName());
		map.put("keyWord", res.getKeyWord());
		map.put("stepNum", res.getStepNum());
		vo.setParam(map);
		vo.setObjectClass(MapPoiSearchNoData.class);
		vo.setMethod("findPoiSearchNodata");
		if(mapPoiSearchInfoService == null){
	    	 mapPoiSearchInfoService=(MapPoiSearchInfoService)TheadUtil.getBean("mapPoiSearchInfoService");
	    }
		List<Object> listObj = this.mapPoiSearchInfoService.selectMapPoiSearchNoData(vo);
		for(Object obj:listObj){
			MapPoiSearchNoData poi = (MapPoiSearchNoData) obj;
			String[] str = new String[4];
			str[0]=poi.getSmallBounds();
			str[1]=poi.getPageNum();
			str[2]=poi.getKeyWord();
			str[3]=poi.getStepNum();
			list.add(str);
		}
		return list; 
	}

	private String getAreaCityCode(String regionName) {
		Map<String,Object> m=new HashMap<String,Object>(); 
	     ParamVo vo = new ParamVo();
	     m.put("areaName", regionName);
	     vo.setParam(m);
	     vo.setObjectClass(MapAreaInfo.class);
	     vo.setMethod("selectList");
	     if(mapPoiSearchInfoService == null){
	    	 mapPoiSearchInfoService=(MapPoiSearchInfoService)TheadUtil.getBean("mapPoiSearchInfoService");
	     }
	     MapAreaInfo area = (MapAreaInfo) mapPoiSearchInfoService.selectArea(vo);
		if(area != null){
			return area.getAreaCode();
		}else{
	     return "";
		}
	}

	public static String getCityname(String cityCode){
	    	if(cityCode.equals("140100")){
	    		cityCode="太原";
	    	}else if(cityCode.equals("140200")){
	    		cityCode="大同";
	    	}else if(cityCode.equals("140300")){
	    		cityCode="阳泉";
	    	}else if(cityCode.equals("140400")){
	    		cityCode="长治";
	    	}else if(cityCode.equals("140500")){
	    		cityCode="晋城";
	    	}else if(cityCode.equals("140600")){
	    		cityCode="朔州";
	    	}else if(cityCode.equals("140700")){
	    		cityCode="晋中";
	    	}else if(cityCode.equals("140800")){
	    		cityCode="运城";
	    	}else if(cityCode.equals("140900")){
	    		cityCode="忻州";
	    	}else if(cityCode.equals("141000")){
	    		cityCode="临汾";
	    	}else if(cityCode.equals("141100")){
	    		cityCode="吕梁";
	    	}
			return cityCode;
				
		 }

	public String getPoiSearchTotal(){
		String ret="";
		try{
			List<MapAreaInfo> areaList = getAreaList();
			List<MapResource> poiList = getPoiList();
			for(MapResource poi:poiList){
				for(MapAreaInfo area:areaList){
					DownloadBD dd = new DownloadBD(poi.getSecondType(),poi.getFirstType(),area.getAreaName(),"");
			        try{
			        	String regioncode = dd.getCityCode(area.getAreaName());
			            String res = dd.getTotalPoiForKeyWord();
			            if(StringUtils.isNotBlank(res)&&StringUtils.isNotBlank(regioncode)){
			            	System.out.println(area+"  查询【"+poi.getFirstType()+"-"+poi.getSecondType()+"】数据返回总量："+res);
			            	if(checkPoiTotalIsExist(poi.getSecondType(),poi.getFirstType(),regioncode)){
			            		tnInsertBMapSearchPoiTotal(poi.getSecondType(),poi.getFirstType(),area,regioncode,res);
			            	}else{
			            		tnUpdateBMapSearchPoiTotal(poi.getSecondType(),poi.getFirstType(),area,regioncode,res);
			            	}
			            }
			        }catch (Exception e){
			            e.printStackTrace();
			        }
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return ret;
	}

	private void tnUpdateBMapSearchPoiTotal(String secondType,String firstType,MapAreaInfo area, String regioncode, String res) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("total", res);
		map.put("keyWord", secondType);
		map.put("wd2Name", firstType);
		map.put("regionCode", regioncode);
		if(Integer.valueOf(res)>700){
			map.put("bounds", area.getBounds());
			map.put("stepNum", area.getStepNum());
		}else{
			map.put("bounds", "");
			map.put("stepNum", "");
		}
		if(mapPoiSearchInfoService == null){
	    	 mapPoiSearchInfoService=(MapPoiSearchInfoService)TheadUtil.getBean("mapPoiSearchInfoService");
	    }
		this.mapPoiSearchInfoService.tnUpdateMapPoiSearchTotal(map);
	}

	private void tnInsertBMapSearchPoiTotal(String secondType,String firstType, MapAreaInfo area, String regioncode, String res) {
		ParamVo vo = new ParamVo();
		MapPoiSearchTotal poitotal = new MapPoiSearchTotal();
		poitotal.setKeyWord(secondType);
		poitotal.setRegionName(area.getAreaName());
		poitotal.setRegionCode(regioncode);
		poitotal.setTotal(res);
		poitotal.setStartTime("");
		poitotal.setEndTime("");
		if(Integer.valueOf(res)>700){
			poitotal.setBounds(area.getBounds());
			poitotal.setStepNum(area.getStepNum());
		}else{
			poitotal.setBounds("");
			poitotal.setStepNum("");
		}
		poitotal.setWd2Name(firstType);
		if(mapPoiSearchInfoService == null){
	    	 mapPoiSearchInfoService=(MapPoiSearchInfoService)TheadUtil.getBean("mapPoiSearchInfoService");
	    }
		this.mapPoiSearchInfoService.tnSaveMapPoiSearchTotal(vo);
	}

	private boolean checkPoiTotalIsExist(String secondType,String firstType,String regioncode) {
		boolean flag=true;
		ParamVo vo = new ParamVo();
		vo.put("keyWord", secondType);
		vo.put("regionCode", regioncode);
		vo.put("wd2Name", firstType);
		vo.setMethod("selectPoiSearchTotal");
		vo.setObjectClass(MapPoiSearchTotal.class);
		if(mapPoiSearchInfoService == null){
	    	 mapPoiSearchInfoService=(MapPoiSearchInfoService)TheadUtil.getBean("mapPoiSearchInfoService");
	    }
		MapResource resource=(MapResource) this.mapPoiSearchInfoService.selectMapPoiSearchTotal(vo);
		if(resource != null){
			flag=false;
		}
		return flag;
	}

	private List<MapResource> getPoiList() {
		List<MapResource> listResources = new ArrayList<MapResource>();
		ParamVo vo = new ParamVo();
		vo.setObjectClass(MapResource.class);
		vo.setMethod("selectAllPoiList");
		if(mapResourceService == null){
			mapResourceService=(MapResourceService)TheadUtil.getBean("mapResourceService");
	    }
		List<Object> list = this.mapResourceService.selectList(vo);
		if(list != null){
			for(Object obj:list){
				MapResource resource =(MapResource) obj;
				listResources.add(resource);
			}
		}
		return listResources;
	}

	private List<MapAreaInfo> getAreaList() {
		List<MapAreaInfo> listAreas = new ArrayList<MapAreaInfo>();
		ParamVo vo = new ParamVo();
		vo.setObjectClass(MapAreaInfo.class);
		vo.setMethod("selectAllAreaList");
		if(mapPoiSearchInfoService == null){
	    	 mapPoiSearchInfoService=(MapPoiSearchInfoService)TheadUtil.getBean("mapPoiSearchInfoService");
	    }
		List<Object> list = this.mapPoiSearchInfoService.selectMapAreaInf(vo);
		if(list != null){
			for(Object obj:list){
				MapAreaInfo mapArea =(MapAreaInfo) obj;
				listAreas.add(mapArea);
			}
		}
		return listAreas;
	}
	
}
