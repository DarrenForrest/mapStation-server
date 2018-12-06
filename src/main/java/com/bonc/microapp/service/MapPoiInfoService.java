package com.bonc.microapp.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonc.db.IDao;
import com.bonc.db.TxException;
import com.bonc.microapp.entity.MapPoiInfo;
import com.bonc.microapp.entity.MapResource;
import com.bonc.tools.ParamVo;

@Service
public class MapPoiInfoService{

	@Autowired
	private IDao mapPoiInfoDao;

	@Autowired
	private IDao mapResourceDao;

	public void txSaveMapPoiInfo(ParamVo vo) throws TxException {
//		if(vo.getObject() == null) {
//			throw new TxException("保存的主机信息为空！");
//		}
//		
//		if(!this.mapPoiInfoDao.save(vo)) {
//			throw new TxException("保存主机信息出错！");
//		}
		this.mapPoiInfoDao.add(vo);
	}

	public MapPoiInfo selectByUid(ParamVo vo) {
		// TODO Auto-generated method stub
		vo.setObjectClass(MapPoiInfo.class);
		Object select = this.mapPoiInfoDao.getInfoById(vo);
		MapPoiInfo poi=(MapPoiInfo) select;
		return poi;
	}

	public void txUpdatePoi(ParamVo vo) {
		// TODO Auto-generated method stub
		this.mapPoiInfoDao.update(this.mapPoiInfoDao.getNameSpace()+".updateById", vo.getParam());
	}

	public List<MapPoiInfo> findPOI(ParamVo vo) {
		// TODO Auto-generated method stub
		vo.setMethod("findPoi");
		List<Object> selectList = this.mapPoiInfoDao.selectList(vo);
		List<MapPoiInfo> poiList=new ArrayList<MapPoiInfo>();
		for (Object object : selectList) {
			MapPoiInfo mapPoiInfo=(MapPoiInfo) object;
			poiList.add(mapPoiInfo);
		}
		return poiList;
	}

	
}
