package com.bonc.core.dao;

import org.springframework.stereotype.Repository;

import com.bonc.db.BatisDao;

@Repository
public class RuleDao extends BatisDao {

	public RuleDao() {
		this.setNameSpace(F.NAMESPACE_CORE + ".Rule");
	}

}
