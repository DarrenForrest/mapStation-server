package com.bonc.microapp.dao;

import org.springframework.stereotype.Repository;

import com.bonc.db.BatisDao;
@Repository
public class MapPoiSearchAkDao  extends BatisDao{

	public MapPoiSearchAkDao() {
		this.setNameSpace(F.NAMESPACE_MICROAPP + ".MapPoiSearchAk");
	}
	
}
