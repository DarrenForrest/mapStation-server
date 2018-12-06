package com.bonc.microapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonc.db.IDao;
import com.bonc.db.TxException;
import com.bonc.tools.ParamVo;

@Service
public class MapResourceStateService {
	
	@Autowired
	private IDao mapResourceStateDao;

	public void txAdd(ParamVo vo) throws TxException {
		// TODO Auto-generated method stub
		if(vo.getObject() == null) {
			throw new TxException("空！");
		}
		
		if(!this.mapResourceStateDao.save(vo)) {
			throw new TxException("信息出错！");
		}
		
	}

}
