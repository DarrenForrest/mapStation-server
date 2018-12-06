package com.bonc.core.dao;

import org.springframework.stereotype.Repository;

import com.bonc.db.BatisDao;

@Repository
public class BigDataDao extends BatisDao {

	public BigDataDao() {
		this.setNameSpace(F.NAMESPACE_CORE + ".BigData");
	}

}
