package com.bonc.microapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonc.db.IDao;
import com.bonc.microapp.entity.MapAreaInfo;
import com.bonc.microapp.entity.MapPoiSearchAk;
import com.bonc.microapp.entity.MapPoiSearchInfo;
import com.bonc.tools.ParamVo;

@Service
public class MapPoiSearchInfoService {

	@Autowired
	private IDao mapPoiSearchInfoDao;
	
	@Autowired
	private IDao mapPoiSearchTotalDao;

	@Autowired
	private IDao mapPoiSearchAkDao;
	
	@Autowired
    private IDao mapAreaInfoDao;
	
	@Autowired
	private IDao mapPoiSearchNoDataDao;
	
	@Autowired
	private IDao mapPoiSearchRecordDao;
	
	public MapPoiSearchInfo selectByBdUid(ParamVo vo) {
		// TODO Auto-generated method stub
		Object infoById = this.mapPoiSearchInfoDao.getInfoById(vo);
		 MapPoiSearchInfo mapPoiSearchInfo=(MapPoiSearchInfo) infoById;
		return mapPoiSearchInfo;
		
	}

	public void tnUpdateInfo(ParamVo vo) {
		// TODO Auto-generated method stub
		this.mapPoiSearchInfoDao.update(this.mapPoiSearchInfoDao.getNameSpace()+".updateById", vo.getParam());
		
	}

	public void tnSave(ParamVo vo) {
		// TODO Auto-generated method stub
		this.mapPoiSearchInfoDao.add(vo);
	}

	public List<MapPoiSearchInfo> findPOI(ParamVo vo) {
		vo.setMethod("findPoi");
		List<Object> selectList = this.mapPoiSearchInfoDao.selectList(vo);
		List<MapPoiSearchInfo> poiList=new ArrayList<MapPoiSearchInfo>();
		for (Object object : selectList) {
			MapPoiSearchInfo mapPoiSearchInfo=(MapPoiSearchInfo) object;
			poiList.add(mapPoiSearchInfo);
		}
		return poiList;
	}

	public List<Object> selectTotalList(ParamVo vo) {
		// TODO Auto-generated method stub
		List<Object> selectList = this.mapPoiSearchTotalDao.selectList(vo);
		return selectList;
	}

	public MapPoiSearchAk selectAk(ParamVo vo) {
		// TODO Auto-generated method stub
		 Object select = this.mapPoiSearchAkDao.select(vo);
		 MapPoiSearchAk mapPoiSearchAk=(MapPoiSearchAk) select;
		return mapPoiSearchAk;
	}

	public MapAreaInfo selectArea(ParamVo vo) {
		// TODO Auto-generated method stub
		Object select = this.mapAreaInfoDao.select(vo);
		MapAreaInfo mapAreaInfo=(MapAreaInfo) select;
		return mapAreaInfo;
	}

	public void tnMapPoiSearchNoData(ParamVo vo) {
		// TODO Auto-generated method stub
		this.mapPoiSearchNoDataDao.save(vo);
	}

	public List<Object> selectMapPoiSearchNoData(ParamVo vo) {
		// TODO Auto-generated method stub
		List<Object> selectList = this.mapPoiSearchNoDataDao.selectList(vo);
		return selectList;
	}

	public void tnUpdateMapPoiSearchTotal(Map<String, Object> map) {
		// TODO Auto-generated method stub
		this.mapPoiSearchTotalDao.update(this.mapPoiSearchTotalDao.getNameSpace()+".updateById", map);
	}

	public void tnSaveMapPoiSearchTotal(ParamVo vo) {
		// TODO Auto-generated method stub
		this.mapPoiSearchTotalDao.add(vo);
	}

	public Object selectMapPoiSearchTotal(ParamVo vo) {
		// TODO Auto-generated method stub
		Object select = this.mapPoiSearchTotalDao.select(vo);
		return select;
	}

	public List<Object> selectMapAreaInf(ParamVo vo) {
		List<Object> list = this.mapAreaInfoDao.selectList(vo);
		return list;
	}

	public void tnSaveMapPoiSearchRecord(ParamVo vo) {
		// TODO Auto-generated method stub
		this.mapPoiSearchRecordDao.save(vo);
	}

	public List<Object> selectMapPoiSearchRecordList(ParamVo vo) {
		// TODO Auto-generated method stub
		List<Object> selectList = this.mapPoiSearchRecordDao.selectList(vo);
		return selectList;
	}
	
}
