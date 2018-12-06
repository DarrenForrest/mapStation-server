package com.bonc.microapp.dao;

import org.springframework.stereotype.Repository;

import com.bonc.db.BatisDao;
@Repository
public class MapPoiSearchInfoDao extends BatisDao{

	public MapPoiSearchInfoDao() {
		this.setNameSpace(F.NAMESPACE_MICROAPP + ".MapPoiSearchInfo");
	}
}
