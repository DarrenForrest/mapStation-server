package com.bonc.microapp.dao;

import org.springframework.stereotype.Repository;

import com.bonc.db.BatisDao;

@Repository
public class NoticeDao extends BatisDao{
	
	public NoticeDao() {
		this.setNameSpace(F.NAMESPACE_MICROAPP + ".Notice");
		
	}
}
