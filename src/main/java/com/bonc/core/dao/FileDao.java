package com.bonc.core.dao;

import org.springframework.stereotype.Repository;

import com.bonc.db.BatisDao;

@Repository
public class FileDao extends BatisDao {

	public FileDao() {
		this.setNameSpace(F.NAMESPACE_CORE + ".File");
		
		//需要实时备份历史表
		this.setWriteHis(true);
	}

}
