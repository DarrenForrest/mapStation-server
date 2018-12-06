package com.bonc.core.dao;

import org.springframework.stereotype.Repository;

import com.bonc.db.BatisDao;

@Repository
public class OrgExtendDao extends BatisDao {

	public OrgExtendDao() {
		this.setNameSpace(F.NAMESPACE_CORE + ".OrgExtend");
		
		//需要实时备份历史表
		this.setWriteHis(true);
	}

}
