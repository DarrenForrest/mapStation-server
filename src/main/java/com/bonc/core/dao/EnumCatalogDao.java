package com.bonc.core.dao;

import org.springframework.stereotype.Repository;

import com.bonc.db.BatisDao;

@Repository
public class EnumCatalogDao extends BatisDao {

	public EnumCatalogDao() {
		this.setNameSpace(F.NAMESPACE_CORE + ".EnumCatalog");
		
		//this.setWriteHis(true);
	}
}
