package com.bonc.microapp.dao;

import org.springframework.stereotype.Repository;

import com.bonc.db.BatisDao;


@Repository
public class HolidayDao extends BatisDao {

	public HolidayDao() {
		this.setNameSpace(F.NAMESPACE_MICROAPP + ".Holiday");
		
		//需要实时备份历史表
		//this.setWriteHis(true);
	}

}
