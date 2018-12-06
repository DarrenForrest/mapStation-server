package com.bonc.microapp.dao;

import org.springframework.stereotype.Repository;

import com.bonc.db.BatisDao;

@Repository
public class StaffExtendDao extends BatisDao {

	public StaffExtendDao() {
		this.setNameSpace(F.NAMESPACE_MICROAPP + ".StaffExtend");
	}
	
}
