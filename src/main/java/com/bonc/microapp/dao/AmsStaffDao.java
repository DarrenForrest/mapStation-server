package com.bonc.microapp.dao;

import org.springframework.stereotype.Repository;

import com.bonc.db.BatisDao;

@Repository
public class AmsStaffDao extends BatisDao {

	public AmsStaffDao() {
		this.setNameSpace(F.NAMESPACE_MICROAPP + ".AmsStaff");
		
		//this.setWriteHis(true);
	}
}
