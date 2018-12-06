package com.bonc.microapp.job;

import com.bonc.common.CrontabTask;
import com.bonc.core.service.AuthorityService;
import com.bonc.db.TxException;
import com.bonc.microapp.entity.JobDefine;
import com.bonc.microapp.service.AmsService;
import com.bonc.tools.ParamVo;

public class AmsCreatePlanJob extends CommonJob {
	
	private AmsService amsService;

	@Override
	protected String ownerJob(JobDefine jobDefine, long triggerType, Long exeFlowId) throws TxException {
		System.out.println("AmsCreatePlanJob: execute...............");
		
		if(amsService == null) {
			amsService = (AmsService)CrontabTask.getBean("amsService");
		}
		
		ParamVo vo = new ParamVo();
		String ret = amsService.txCreateAmsPlan(vo);
		System.out.println("AmsCreatePlanJob ret = " + ret );
		
		return ret;
	}

}
