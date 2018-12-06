package com.bonc.core.dao;

import org.springframework.stereotype.Repository;

import com.bonc.db.BatisDao;

@Repository
public class EnumCfgDao extends BatisDao {

	public EnumCfgDao() {
		this.setNameSpace(F.NAMESPACE_CORE + ".EnumCfg");
	}

}
