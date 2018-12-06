package com.bonc.microapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonc.db.IDao;
import com.bonc.db.TxException;
import com.bonc.microapp.entity.AmsActionLog;
import com.bonc.tools.ParamVo;

@Service
public class AmsLogService {
	@Autowired
	private IDao amsStaffDao;
	
	@Autowired
	private IDao amsActionLogDao;
	
	
	public void tnSaveAmsActionLog(ParamVo vo)throws TxException {
		if(vo.getObject() == null) {
			throw new TxException("更新AmsActionLog的内容为空");
		}
		vo.setObjectClass(AmsActionLog.class);
		if(!this.amsActionLogDao.edit(vo) ){
			throw new TxException("更新AmsActionLog出错");
		}
	}

}
