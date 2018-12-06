package com.bonc.microapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonc.common.CrontabTask;
import com.bonc.db.IDao;
import com.bonc.db.TxException;
import com.bonc.microapp.dao.F;
import com.bonc.microapp.entity.JobDefine;
import com.bonc.microapp.job.CommonJob;
import com.bonc.tools.ParamVo;

@Service
public class JobDefineService {

	@Autowired
	private IDao jobDefineDao;
	
	@Autowired
	private IDao jobLogDao;
	
	public Object getJobDefineList(ParamVo vo) {
		vo.setMethod("selectList");
		return this.jobDefineDao.selectList(vo);
	}
	
	public void txSaveJobLog(ParamVo vo) throws TxException {
		if(!this.jobLogDao.save(vo)) {
			throw new TxException("保存自动任务日志出错");
		}
	}
	
	public boolean start() {
		
		if(!CrontabTask.init()) {
			return false;
		}
		
		if(!CrontabTask.isRunning()) {
			ParamVo vo = new ParamVo();
			vo.setObjectClass(JobDefine.class);
			@SuppressWarnings("rawtypes")
			List list = (List)getJobDefineList(vo);
			for(Object obj : list) {
				JobDefine job = (JobDefine)obj;
				if(job.getStatus().intValue() != 1) {
					continue;
				}
				if(!CrontabTask.addToScheduler(job.getId().toString(), job.getJobClass(), job.getCronExpression(), job)) {
					System.out.println("定时器设置失败！" + obj.toString());
				}
			}
		}
		
		if(!CrontabTask.start()) {
			return false;
		}
		
		return true;
	}
	
	public boolean stop() {
		return CrontabTask.stop();
	}
	
	public boolean isRunning() {
		return CrontabTask.isRunning();
	}
	
	//JOB_DEFINE
	public void txInserJobDefine(ParamVo vo) throws TxException {
		if(!this.jobDefineDao.add(vo)) {
			throw new TxException("插入任务表出错！");
		}
	}
	public void txSaveJobDefine(ParamVo vo) throws TxException {
		if(vo.getObject() == null) {
			throw new TxException("保存的任务信息为空！");
		}
		
		if(!this.jobDefineDao.save(vo)) {
			throw new TxException("保存任务信息出错！");
		}
	}
	public Object getJobDefineInfo(ParamVo vo) {
		return this.jobDefineDao.getInfoById(vo);
	}
	public Object getJobDefinePage(ParamVo vo) {
		vo.setMethod("selectPage");
		return this.jobDefineDao.selectPage(vo);
	}
	public Object getJobLogPage(ParamVo vo) {
		vo.setMethod("selectPage");
		return this.jobLogDao.selectPage(vo);
	}
	
	public void manualDoJob(String jobId) throws TxException {
		
		ParamVo vo = new ParamVo();
		vo.put("id", jobId);
		vo.setObjectClass(JobDefine.class);
		JobDefine jobDefine = (JobDefine)this.jobDefineDao.getInfoById(vo);
		if(jobDefine == null) {
			throw new TxException("找不到定时任务，编号：" + jobId);
		}
		
		//创建一个
		CommonJob commonJob = null;
		try {
			commonJob = (CommonJob)Class.forName(jobDefine.getJobClass()).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			throw new TxException("创建CommonJob失败：" + e.getMessage());
		}
		if(commonJob != null) {
			String res = commonJob.doJob(jobDefine, 1L); //1：手工触发  0：系统时钟触发
			if(res != null) {
				throw new TxException(res);
			}
		} else {
			throw new TxException("找不到JOB类：" + jobDefine.getJobClass());
		}
	
	}
	
	public String txOracleProcedureJob(JobDefine jobDefine, long triggerType, Long exeFlowId) throws TxException {
		
		ParamVo vo = new ParamVo();
		
		vo.setMethod("callProParam1");
		vo.put("procedureName", jobDefine.getStoredProcedure());
		vo.put("param1", "" + exeFlowId); //调用存储过程时把EXE_FLOW_ID作为参数传进去
		vo.put("param2", "");
		vo.put("code", 0);
		vo.put("message", "");
		
		//执行存储过程select成功失败都返回null,只能判断过程的返回代码
		this.jobDefineDao.select(vo);
		Integer code = -1;
		if(vo.get("code") == null) {
			throw new TxException("code=,message=" + vo.get("message"));
		}
		code = (Integer)vo.get("code");
		if(code != 1) {
			throw new TxException("code=" + code + ",message=" + vo.get("message"));
		}
		
		return "code=" + code + ",message=" + vo.get("message");
	}
}
