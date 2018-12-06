package com.bonc.microapp.dao;

import org.springframework.stereotype.Repository;

import com.bonc.db.BatisDao;

@Repository
public class MapPoiInfoDao extends BatisDao{
	
	public MapPoiInfoDao() {
		this.setNameSpace(F.NAMESPACE_MICROAPP + ".MapPoiInfo");
	}

}
