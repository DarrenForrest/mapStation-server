package com.bonc.core.dao;

import org.springframework.stereotype.Repository;

import com.bonc.db.BatisDao;

@Repository
public class RoleRuleDao extends BatisDao {

	public RoleRuleDao() {
		this.setNameSpace(F.NAMESPACE_CORE + ".RoleRule");
	}

}
