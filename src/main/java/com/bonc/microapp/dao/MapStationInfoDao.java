package com.bonc.microapp.dao;

import org.springframework.stereotype.Repository;

import com.bonc.db.BatisDao;

@Repository
public class MapStationInfoDao extends BatisDao{

	public MapStationInfoDao() {
		this.setNameSpace(F.NAMESPACE_MICROAPP + ".MapStationInfo");
	}
}
