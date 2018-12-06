package com.bonc.microapp.dao;

import org.springframework.stereotype.Repository;

import com.bonc.db.BatisDao;
@Repository
public class MapPoiSearchRecordDao  extends BatisDao{

	public MapPoiSearchRecordDao() {
		this.setNameSpace(F.NAMESPACE_MICROAPP + ".MapPoiSearchRecord");
	}
	
}
