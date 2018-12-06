package com.bonc.microapp.dao;

import org.springframework.stereotype.Repository;

import com.bonc.db.BatisDao;


@Repository
public class MailListDao extends BatisDao {

	public MailListDao() {
		this.setNameSpace(F.NAMESPACE_MICROAPP + ".MailList");
	}
}
