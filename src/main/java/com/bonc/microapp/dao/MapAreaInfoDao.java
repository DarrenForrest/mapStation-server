package com.bonc.microapp.dao;

import org.springframework.stereotype.Repository;

import com.bonc.db.BatisDao;
@Repository
public class MapAreaInfoDao  extends BatisDao{

	public MapAreaInfoDao() {
		this.setNameSpace(F.NAMESPACE_MICROAPP + ".MapAreaInfo");
	}
	
}
