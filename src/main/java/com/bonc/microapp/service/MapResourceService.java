package com.bonc.microapp.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonc.db.IDao;
import com.bonc.microapp.entity.MapResource;
import com.bonc.tools.ParamVo;

@Service
public class MapResourceService{
	
	@Autowired
	private IDao mapResourceDao;

	public List<MapResource> selectAll(ParamVo vo) {
		List<MapResource> mapResList=new ArrayList<MapResource>();
		vo.setMethod("selectAll");
		List<Object> selectList = mapResourceDao.selectList(vo);
		for (Object object : selectList) {
			MapResource  mapRes=(MapResource) object;
			mapResList.add(mapRes);
		}
		return mapResList;
	}

	public List<Object> selectList(ParamVo vo) {
		// TODO Auto-generated method stub
		List<Object> selectList = this.mapResourceDao.selectList(vo);
		return selectList;
	}
}
