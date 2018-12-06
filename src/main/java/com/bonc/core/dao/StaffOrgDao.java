package com.bonc.core.dao;

import org.springframework.stereotype.Repository;

import com.bonc.db.BatisDao;

@Repository
public class StaffOrgDao extends BatisDao {

	public StaffOrgDao() {
		this.setNameSpace(F.NAMESPACE_CORE + ".StaffOrg");
	}

}
