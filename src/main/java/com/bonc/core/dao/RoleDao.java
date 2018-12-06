package com.bonc.core.dao;

import org.springframework.stereotype.Repository;

import com.bonc.db.BatisDao;

@Repository
public class RoleDao extends BatisDao {

	public RoleDao() {
		this.setNameSpace(F.NAMESPACE_CORE + ".Role");
	}

}
