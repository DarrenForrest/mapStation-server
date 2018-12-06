package com.bonc.microapp.dao;

import org.springframework.stereotype.Repository;

import com.bonc.db.BatisDao;

@Repository
public class JobDefineDao extends BatisDao {

	public JobDefineDao() {
		this.setNameSpace(F.NAMESPACE_MICROAPP + ".JobDefine");
	}

}
