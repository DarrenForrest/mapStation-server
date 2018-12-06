package com.bonc.microapp.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonc.db.IDao;
import com.bonc.microapp.entity.CoBaseStationInfo;
import com.bonc.microapp.entity.MapBean;
import com.bonc.microapp.entity.MapInfo;
import com.bonc.microapp.entity.MapStationInfo;
import com.bonc.tools.ParamVo;



@Service
public class MapStationInfoService {
	
	@Autowired
	private IDao coBaseStationInfoDao;

	@Autowired
	private IDao mapStationInfoDao;
	
	/**
	 * 
	 * @param mapBean
	 * @param val
	 * @return
	 * 
	 * 半径
	 */
	
	public List<MapBean> txFindStation(MapBean mapBean, String val) {
		// TODO Auto-generated method stub
		List<MapInfo> mapInfoList=new ArrayList<MapInfo>();
		ParamVo vo=new ParamVo();
		vo.put("cityCode", mapBean.getCityCode());
		vo.put("val", val);
		vo.put("lat", mapBean.getLat());
		vo.put("lng", mapBean.getLng());
		vo.put("maxLat", Double.valueOf(mapBean.getLat())+0.01);
		vo.put("minLat", Double.valueOf(mapBean.getLat())-0.01);
		vo.put("maxLng", Double.valueOf(mapBean.getLng())+0.01);
		vo.put("minLng", Double.valueOf(mapBean.getLat())-0.01);
		vo.setObjectClass(CoBaseStationInfo.class);
		vo.setMethod("findStation");
		List<Object> selectList = coBaseStationInfoDao.selectList(vo);
		for (Object object : selectList) {
			CoBaseStationInfo coBaseStationInfo=(CoBaseStationInfo) object;
			Map<String,Object> map=new HashMap<String,Object>();
			MapStationInfo mapStationInfo = new MapStationInfo();
			mapStationInfo.setCellCi(coBaseStationInfo.getCellCi());
			mapStationInfo.setCelllac(coBaseStationInfo.getCellLac());
			mapStationInfo.setCityCode(mapBean.getCityCode());
			mapStationInfo.setLacCode(coBaseStationInfo.getLacCode());
			mapStationInfo.setLacLat(mapBean.getLat());
			mapStationInfo.setLacLong(mapBean.getLng());
			mapStationInfo.setLacName(coBaseStationInfo.getLacName());
			mapStationInfo.setTitle(mapBean.getMapName());
			mapStationInfo.setAddress(mapBean.getAddress());
			mapStationInfo.setLat(mapBean.getLat());
			mapStationInfo.setLng(mapBean.getLng());
			mapStationInfo.setFirstType(mapBean.getFirstType());
			mapStationInfo.setSecondType(mapBean.getSecondType());
			mapStationInfo.setRange(mapBean.getRange());
			mapStationInfo.setNetType(coBaseStationInfo.getNetType());
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
			mapStationInfo.setCreateTime(formatter.format(new Date()));
			map.put("cellCi", coBaseStationInfo.getCellCi());
			map.put("celllac", coBaseStationInfo.getCellLac());
			map.put("cityCode", mapBean.getCityCode());
			map.put("lacCode", coBaseStationInfo.getLacCode());
			map.put("lacLat", coBaseStationInfo.getLacLat());
			map.put("lacLong", coBaseStationInfo.getLacLong());
			map.put("lacName", coBaseStationInfo.getLacName());
			map.put("title", mapBean.getMapName());
			map.put("address", mapBean.getAddress());
			map.put("lat", mapBean.getLat());
			map.put("lng", mapBean.getLng());
			map.put("firstType", mapBean.getFirstType());
			map.put("range", mapBean.getRange());
			map.put("netType",coBaseStationInfo.getNetType());
			map.put("createTime", formatter.format(new Date()));
			vo.setParam(map);
			vo.setObject(mapStationInfo);
			this.mapStationInfoDao.add(vo);
		}
		return null;
	}

    /**
     *  
     * @param mapBean
     * @param val
     * @return
     *   
     * 		TOP N
     */
	public List<MapBean> txGetTopNStation(MapBean mapBean, String val) {
		List<MapInfo> mapInfoList=new ArrayList<MapInfo>();
		ParamVo vo=new ParamVo();
		vo.put("cityCode", mapBean.getCityCode());
		vo.setObjectClass(CoBaseStationInfo.class);
		vo.setMethod("getTopNStation");
		List<Object> selectList = coBaseStationInfoDao.selectList(vo);
		CoBaseStationInfo c=(CoBaseStationInfo) selectList.get(0);
		for (Object object : selectList) {
			CoBaseStationInfo coBaseStationInfo=(CoBaseStationInfo) object;
			MapInfo mapInfo = new MapInfo();
			mapInfo.setCellCi(coBaseStationInfo.getCellCi());
			mapInfo.setCelllac(coBaseStationInfo.getCellLac());
			mapInfo.setCityCode(coBaseStationInfo.getCityCode());
			mapInfo.setLacCode(coBaseStationInfo.getLacCode());
			mapInfo.setLaclLat(Double.valueOf(coBaseStationInfo.getLacLat()));
			mapInfo.setLacLong(Double.valueOf(coBaseStationInfo.getLacLong()));
			mapInfo.setLacName(coBaseStationInfo.getLacName());
			mapInfo.setNetType(coBaseStationInfo.getNetType());
			double distance = GetDistance(Double.valueOf(coBaseStationInfo.getLacLat()),Double.valueOf(coBaseStationInfo.getLacLong()),mapBean.getLat(),mapBean.getLng());
			mapInfo.setDistance(distance);
			mapInfoList.add(mapInfo);
		}
		Collections.sort(mapInfoList);
		for (MapInfo mapInfo : mapInfoList) {	
			if(mapInfo.getDistance()<=mapInfoList.get(Integer.valueOf(val)).getDistance()) {
				Map<String,Object> map=new HashMap<String,Object>();
				MapStationInfo mapStationInfo = new MapStationInfo();
				mapStationInfo.setCellCi(mapInfo.getCellCi());
				mapStationInfo.setCelllac(mapInfo.getCelllac());
				mapStationInfo.setCityCode(mapBean.getCityCode());
				mapStationInfo.setLacCode(mapInfo.getLacCode());
				mapStationInfo.setLacLat(mapInfo.getLaclLat());
				mapStationInfo.setLacLong(mapInfo.getLacLong());
				mapStationInfo.setLacName(mapInfo.getLacName());
				mapStationInfo.setTitle(mapBean.getMapName());
				mapStationInfo.setAddress(mapBean.getAddress());
				mapStationInfo.setLat(mapBean.getLat());
				mapStationInfo.setLng(mapBean.getLng());
				mapStationInfo.setFirstType(mapBean.getFirstType());
				mapStationInfo.setSecondType(mapBean.getSecondType());
				mapStationInfo.setRange(mapBean.getRange());
				mapStationInfo.setNetType(mapInfo.getNetType());
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
				mapStationInfo.setCreateTime(formatter.format(new Date()));
				map.put("cellCi", mapInfo.getCellCi());
				map.put("celllac", mapInfo.getCelllac());
				map.put("cityCode", mapBean.getCityCode());
				map.put("lacCode", mapInfo.getLacCode());
				map.put("lacLat", mapInfo.getLaclLat());
				map.put("lacLong", mapInfo.getLacLong());
				map.put("lacName", mapInfo.getLacName());
				map.put("title", mapBean.getMapName());
				map.put("address", mapBean.getAddress());
				map.put("lat", mapBean.getLat());
				map.put("lng", mapBean.getLng());
				map.put("firstType", mapBean.getFirstType());
				map.put("range", mapBean.getRange());
				map.put("netType",mapInfo.getNetType());
				map.put("createTime", formatter.format(new Date()));
				vo.setParam(map);
				vo.setObject(mapStationInfo);
				this.mapStationInfoDao.add(vo);
			}
		}
		
		return null;
	}
	
	
	 private static double EARTH_RADIUS = 6371.393;  
	    private static double rad(double d)  
	    {  
	       return d * Math.PI / 180.0;  
	    }  
	    public static double GetDistance(double lat1, double lng1, double lat2, double lng2)  
	    {  
	 	   double radLat1 = rad(lat1);
		   double radLat2 = rad(lat2);
		   double a = radLat1 - radLat2;	
		   double b = rad(lng1) - rad(lng2);
		   double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) + 
		   Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
		   s = s * EARTH_RADIUS;
		   s = Math.round(s * 1000);
		   return s; 
	    }  


}
