package com.bonc.microapp.dao;

import org.springframework.stereotype.Repository;

import com.bonc.db.BatisDao;

@Repository
public class MapPoiSearchNoDataDao extends BatisDao{

	public MapPoiSearchNoDataDao() {
		this.setNameSpace(F.NAMESPACE_MICROAPP + ".MapPoiSearchNoData");
	}

}
