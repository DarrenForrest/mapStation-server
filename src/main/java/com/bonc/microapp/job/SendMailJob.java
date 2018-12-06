package com.bonc.microapp.job;

import com.bonc.common.CrontabTask;
import com.bonc.core.service.AuthorityService;
import com.bonc.db.TxException;
import com.bonc.microapp.entity.JobDefine;
import com.bonc.microapp.service.AmsService;
import com.bonc.microapp.service.ToolService;
import com.bonc.tools.ParamVo;

public class SendMailJob extends CommonJob {
	
	private ToolService toolService;

	@Override
	protected String ownerJob(JobDefine jobDefine, long triggerType, Long exeFlowId) throws TxException {
		System.out.println("SendMailJob: execute...............");
		
		if(toolService == null) {
			toolService = (ToolService)CrontabTask.getBean("toolService");
		}
		
		ParamVo vo = new ParamVo();
		String ret = toolService.txSendMail();
		System.out.println("SendMailJob ret = " + ret );
		
		return ret;
	}

}
