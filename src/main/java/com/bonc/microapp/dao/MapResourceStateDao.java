package com.bonc.microapp.dao;

import org.springframework.stereotype.Repository;

import com.bonc.db.BatisDao;

@Repository
public class MapResourceStateDao extends BatisDao{

	public MapResourceStateDao() {
		this.setNameSpace(F.NAMESPACE_MICROAPP + ".MapResourceState");
	}

}
