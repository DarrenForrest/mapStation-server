package com.bonc.microapp.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.bonc.common.CrontabTask;
import com.bonc.db.TxException;
import com.bonc.microapp.entity.JobDefine;
import com.bonc.microapp.service.JobDefineService;
import com.bonc.tools.JsonBody;
import com.bonc.tools.Page;
import com.bonc.tools.ParamVo;


@Scope("prototype")
@Controller
@RequestMapping({ "/task" })
public class JobDefineAction {
	
	@Autowired
	private JobDefineService jobDefineService;
	
	
	
	@RequestMapping(value = { "startAutoTask.action", "/" }, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public Object startAutoTask(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		
		JsonBody res = new JsonBody();
		
		if(this.jobDefineService.isRunning()) {
			res.setCode(-1);
			res.setMessage("启动定时器失败: 定时器已经在运行中");
			return res;
		}
		if(!this.jobDefineService.start()) {
			res.setCode(-1);
			res.setMessage("启动定时器失败");
			return res;
		}
		
		res.setCode(0);
		res.setMessage("启动定时器成功");
		return res;
	}
	
	@RequestMapping(value = { "stopAutoTask.action", "/" }, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public Object stopAutoTask(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		
		JsonBody res = new JsonBody();
		
		if(!this.jobDefineService.isRunning()) {
			res.setCode(-1);
			res.setMessage("停止定时器失败：没有启动的定时器");
			return res;
		}
		if(!this.jobDefineService.stop()) {
			res.setCode(-1);
			res.setMessage("停止定时器失败");
			return res;
		}
		
		res.setCode(0);
		res.setMessage("停止定时器成功");
		return res;
	}
	
	@RequestMapping(value = { "getSchedulerState.action", "/" }, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public Object getSchedulerState(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		
		JsonBody res = new JsonBody();
		
		if(this.jobDefineService.isRunning()) {
			res.put("schedulerState", 1);
			res.put("schedulerStateName", "已启动");
		} else {
			res.put("schedulerState", 0);
			res.put("schedulerStateName", "未启动");
		}
		
		res.setCode(0);
		res.setMessage("操作成功");
		return res;
	}
	
	@RequestMapping(value = { "doJob.action", "/" }, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public Object doJob(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		
		JsonBody res = new JsonBody();
		
		String jobId = (String)form.get("jobId");
		
		try {
			this.jobDefineService.manualDoJob(jobId);
		} catch(TxException e) {
			res.setCode(-1);
			res.setMessage(e.getMessage());
			return res;
		}
		
		res.setCode(0);
		res.setMessage("操作成功");
		return res;
	}
	
	@RequestMapping({"toJobDefineManage.action"})
	protected ModelAndView toJobDefineManage(HttpServletRequest request, @RequestParam Map<String, Object> form) {
		ModelAndView view = new ModelAndView("/task/jobDefineManage");	
		return view;
	}
	
	@RequestMapping(value = { "getJobDefinePage.action", "/" }, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public Object getJobDefinePage(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		
		ParamVo vo = new ParamVo();
		vo.setParam(form);
		
		vo.put("pageSize", form.get("rows"));
		vo.put("currentPage", form.get("page"));
		vo.setOrderby((String)form.get("sort"), (String)form.get("order"));
		
		Page page = (Page)this.jobDefineService.getJobDefinePage(vo);
		
		for(Object obj : page.getDataList()) {
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>)obj;
			map.put("TRIGGER_STATE", CrontabTask.getJobState(map.get("ID").toString()));
		}
		
		JsonBody res = new JsonBody();
		res.put("total", page.getTotalCnt());
		res.put("rows", page.getDataList());
		
		return res;
	}
	
	@RequestMapping({"toJobDefineEdit.action"})
	protected ModelAndView toJobDefineEdit(HttpServletRequest request, @RequestParam Map<String, Object> form) {
		ModelAndView view = new ModelAndView("/task/jobDefineEdit");
		
		String id = (String)form.get("id");
		if(StringUtils.isNumeric(id)) {
			ParamVo vo = new ParamVo();
			vo.put("id", id);
			@SuppressWarnings("rawtypes")
			Map jobDefine = (Map)this.jobDefineService.getJobDefineInfo(vo);
			view.addObject("jobDefine", jobDefine);
		}
		
		return view;
	}
	
	@RequestMapping(value = { "saveJobDefine.action", "/" }, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public Object saveJobDefine(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		
		JsonBody res = new JsonBody();
		JsonBody jsonBody = new JsonBody(form);
		JobDefine jobDefine = (JobDefine)jsonBody.toJava("", JobDefine.class);
		if(jobDefine.getId() == null || jobDefine.getId() <= 0) {
			jobDefine.setId(-1L);
			jobDefine.setStatus(1L); //0：无效  1：有效
		}
		ParamVo vo = new ParamVo();
		vo.setObject(jobDefine);
		try {
			this.jobDefineService.txSaveJobDefine(vo);
			res.setCode(0);
			res.setMessage("保存成功");
		} catch (TxException e) {
			res.setCode(-1);
			res.setMessage(e.getMessage());
			return res;
		}
		
		return res;
	}
	
	@RequestMapping({"toJobLogManage.action"})
	protected ModelAndView toJobLogManage(HttpServletRequest request, @RequestParam Map<String, Object> form) {
		ModelAndView view = new ModelAndView("/task/jobLogManage");
		view.addObject("jobId", form.get("jobId"));
		return view;
	}
	
	@RequestMapping(value = { "getJobLogPage.action", "/" }, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public Object getJobLogPage(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		
		ParamVo vo = new ParamVo();
		vo.setParam(form);
		
		vo.put("pageSize", form.get("rows"));
		vo.put("currentPage", form.get("page"));
		vo.setOrderby((String)form.get("sort"), (String)form.get("order"));
		
		Page page = (Page)this.jobDefineService.getJobLogPage(vo);
		
		JsonBody res = new JsonBody();
		res.put("total", page.getTotalCnt());
		res.put("rows", page.getDataList());
		
		return res;
	}

}
