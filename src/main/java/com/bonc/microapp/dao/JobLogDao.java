package com.bonc.microapp.dao;

import org.springframework.stereotype.Repository;

import com.bonc.db.BatisDao;

@Repository
public class JobLogDao extends BatisDao {

	public JobLogDao() {
		this.setNameSpace(F.NAMESPACE_MICROAPP + ".JobLog");
	}

}
