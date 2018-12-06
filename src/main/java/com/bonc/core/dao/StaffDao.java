package com.bonc.core.dao;

import org.springframework.stereotype.Repository;

import com.bonc.db.BatisDao;

@Repository
public class StaffDao extends BatisDao {

	public StaffDao() {
		this.setNameSpace(F.NAMESPACE_CORE + ".Staff");
	}

}
