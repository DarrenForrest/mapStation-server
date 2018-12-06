package com.bonc.microapp.dao;

import org.springframework.stereotype.Repository;

import com.bonc.db.BatisDao;

@Repository
public class MapPoiSearchTotalDao extends BatisDao{

	public MapPoiSearchTotalDao() {
		this.setNameSpace(F.NAMESPACE_MICROAPP + ".MapPoiSearchTotal");
	}

}
