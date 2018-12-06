package com.bonc.microapp.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonc.db.IDao;
import com.bonc.db.TxException;
import com.bonc.microapp.entity.MailList;
import com.bonc.tools.ParamVo;


@Service
public class MailService {
	Log log = LogFactory.getLog(this.getClass());
	
	@Autowired
	private IDao mailListDao;
	
	public void tnInsert(MailList mailList) throws TxException {
		ParamVo vo = new ParamVo();
		vo.setObject(mailList);
		if(!this.mailListDao.add(vo)) {
			log.error("tnInsert 添加mailList出错");
			throw new TxException("tnInsert 添加mailList出错");
		}
		log.info("tnInsert");
	}
	
	public void tnUpdate(MailList mailList) throws TxException {
		ParamVo vo = new ParamVo();
		vo.setObject(mailList);
		if(!this.mailListDao.edit(vo)) {
			log.info("tnUpdate 修改mailList出错");
			throw new TxException("tnUpdate 更新mailList出错");
		}
		log.info("tnUpdate");
	}
	
	public void txUpdate(MailList mailList) throws TxException {
		ParamVo vo = new ParamVo();
		vo.setObject(mailList);
		if(!this.mailListDao.edit(vo)) {
			log.info("tnUpdate 修改mailList出错");
			throw new TxException("tnUpdate 更新mailList出错");
		}
		log.info("tnUpdate");
	}
}
