package com.bonc.microapp.dao;

import org.springframework.stereotype.Repository;

import com.bonc.db.BatisDao;

@Repository
public class MapResourceDao extends BatisDao{
	
	public MapResourceDao() {
		this.setNameSpace(F.NAMESPACE_MICROAPP + ".MapResource");
	}

}
