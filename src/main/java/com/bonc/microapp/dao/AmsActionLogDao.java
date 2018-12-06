package com.bonc.microapp.dao;

import org.springframework.stereotype.Repository;

import com.bonc.db.BatisDao;

@Repository
public class AmsActionLogDao extends BatisDao {

	public AmsActionLogDao() {
		this.setNameSpace(F.NAMESPACE_MICROAPP + ".AmsActionLog");
		
		//this.setWriteHis(true);
	}
}
