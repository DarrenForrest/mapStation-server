package com.bonc.microapp.dao;

import org.springframework.stereotype.Repository;

import com.bonc.db.BatisDao;

@Repository
public class MessageHintDao  extends BatisDao {

	public MessageHintDao() {
		this.setNameSpace(F.NAMESPACE_MICROAPP + ".MessageHint");
	}
}
