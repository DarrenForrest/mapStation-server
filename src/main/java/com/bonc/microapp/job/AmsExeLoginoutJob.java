package com.bonc.microapp.job;

import com.bonc.common.CrontabTask;
import com.bonc.core.service.AuthorityService;
import com.bonc.db.TxException;
import com.bonc.microapp.entity.JobDefine;
import com.bonc.microapp.service.AmsService;
import com.bonc.tools.ParamVo;

public class AmsExeLoginoutJob extends CommonJob {
	
	private AmsService amsService;

	@Override
	protected String ownerJob(JobDefine jobDefine, long triggerType, Long exeFlowId) throws TxException {
		System.out.println("AmsExeLoginoutJob: execute...............");
		
		if(amsService == null) {
			amsService = (AmsService)CrontabTask.getBean("amsService");
		}
		
		ParamVo vo = new ParamVo();
		String ret = amsService.txExeLoginoutPlan(vo);
		System.out.println("自动执行签出" + ret );		
		return ret ;
	}

}
