package com.bonc.microapp.job;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.bonc.common.CrontabTask;
import com.bonc.db.TxException;
import com.bonc.microapp.dao.F;
import com.bonc.microapp.entity.JobDefine;
import com.bonc.microapp.entity.JobLog;
import com.bonc.microapp.service.JobDefineService;
import com.bonc.tools.ParamVo;

public abstract class CommonJob implements Job {
	
	private Log log = LogFactory.getLog(this.getClass());

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		
		JobDefine jobDefine = (JobDefine)context.getJobDetail().getJobDataMap().get("data");
		if(jobDefine == null) {
			log.error("获取定时任务信息出错！");
			return;
		}
		System.out.println(jobDefine);
		
		//调用执行JOB（系统自动）（此处还是使用和数据字典一样的枚举）
		//doJob(jobDefine, F.TriggerType.automatic); //1：手工触发  2：系统时钟触发
		doJob(jobDefine, 0L); //1：手工触发  0：系统时钟触发
		
	}
	
	//手动执行任务时调用此函数（成功返回null，失败返回错误信息）
	public String doJob(JobDefine jobDefine, long triggerType) {
		int res = 0;
		JobDefineService jobDefineService = (JobDefineService)CrontabTask.getBean("jobDefineService");
		if(jobDefineService == null) {
			log.error("获取定时器服务代码出错！");
			return "获取定时器服务代码出错！";
		}
		
		ParamVo vo = new ParamVo();
		JobLog jobLog = new JobLog();
		vo.setObject(jobLog);
		try {
			jobLog.setExeFlowId(-1L);
			jobLog.setJobId(jobDefine.getId());
			jobLog.setJobName(jobDefine.getJobName());
			jobLog.setTriggerType(triggerType);
			jobLog.setStartDate(new Date());
			jobLog.setState(0L); //0：失败     1：成功
			jobDefineService.txSaveJobLog(vo);
		} catch (TxException e) {
			e.printStackTrace();
			log.error("写入日志表出错，还未执行任务: " + e.getMessage());
		}
		
		try {
			//执行任务
			String retMsg = ownerJob(jobDefine, triggerType, jobLog.getExeFlowId());
			jobLog.setRetMsg(retMsg);
			jobLog.setState(1L); //执行成功
		} catch( Exception e) {
			jobLog.setState(0L); //执行失败 
			jobLog.setRetMsg("执行任务时发生异常: " + e.getMessage());
			return jobLog.getRetMsg();
		} finally {
			try {
				jobLog.setEndDate(new Date());
				int maxLength = 128; //和数据库保持一致
				if(jobLog.getRetMsg() != null && jobLog.getRetMsg().length() > maxLength) {
					jobLog.setRetMsg(jobLog.getRetMsg().substring(0, maxLength));
				}
				jobDefineService.txSaveJobLog(vo);
			} catch (TxException e) {
				e.printStackTrace();
				log.error("执行任务成功，但写入日志表出错: " + e.getMessage());
			}
		}
		
		return null;
	}
	
	//任务执行失败抛出TxException异常，执行成功return执行结果（即保存回日志表的摘要）
	protected abstract String ownerJob(JobDefine jobDefine, long triggerType, Long exeFlowId) throws TxException;

}
