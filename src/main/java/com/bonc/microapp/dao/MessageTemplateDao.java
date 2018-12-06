package com.bonc.microapp.dao;

import org.springframework.stereotype.Repository;

import com.bonc.db.BatisDao;

@Repository
public class MessageTemplateDao  extends BatisDao {

	public MessageTemplateDao() {
		this.setNameSpace(F.NAMESPACE_MICROAPP + ".MessageTemplate");
	}
}
