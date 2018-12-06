package com.bonc.microapp.job;

import org.springframework.beans.factory.annotation.Autowired;

import com.bonc.common.CrontabTask;
import com.bonc.db.TxException;
import com.bonc.microapp.entity.JobDefine;
import com.bonc.microapp.service.JobDefineService;

public class OracleProcedureJob extends CommonJob {
	
	@Autowired
	private JobDefineService jobDefineService;

	@Override
	protected String ownerJob(JobDefine jobDefine, long triggerType, Long exeFlowId) throws TxException {
		if(jobDefineService == null) {
			jobDefineService = (JobDefineService)CrontabTask.getBean("jobDefineService");
		}
		return this.jobDefineService.txOracleProcedureJob(jobDefine, triggerType, exeFlowId);
	}

}
