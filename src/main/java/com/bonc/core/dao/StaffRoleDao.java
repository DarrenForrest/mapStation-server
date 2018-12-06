package com.bonc.core.dao;

import org.springframework.stereotype.Repository;

import com.bonc.db.BatisDao;

@Repository
public class StaffRoleDao extends BatisDao {

	public StaffRoleDao() {
		this.setNameSpace(F.NAMESPACE_CORE + ".StaffRole");
	}

}
