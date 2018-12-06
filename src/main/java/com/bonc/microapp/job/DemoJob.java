package com.bonc.microapp.job;

import com.bonc.common.CrontabTask;
import com.bonc.core.service.AuthorityService;
import com.bonc.db.TxException;
import com.bonc.microapp.entity.JobDefine;
import com.bonc.tools.ParamVo;

public class DemoJob extends CommonJob {
	
	private AuthorityService authorityService;

	@Override
	protected String ownerJob(JobDefine jobDefine, long triggerType, Long exeFlowId) throws TxException {
		System.out.println("InnerJob: execute...............");
		
		if(authorityService == null) {
			authorityService = (AuthorityService)CrontabTask.getBean("authorityService");
		}
		
		ParamVo vo = new ParamVo();
		vo.put("staffId", -999);
		Object obj = authorityService.getStaffInfo(vo);
		System.out.println("自动任务取回数据：" + obj);
		
		return obj == null ? "null" : obj.toString();
	}

}
