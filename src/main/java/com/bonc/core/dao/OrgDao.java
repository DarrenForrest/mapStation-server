package com.bonc.core.dao;

import org.springframework.stereotype.Repository;

import com.bonc.db.BatisDao;

@Repository
public class OrgDao extends BatisDao {

	public OrgDao() {
		this.setNameSpace(F.NAMESPACE_CORE + ".Org");
	}

}
