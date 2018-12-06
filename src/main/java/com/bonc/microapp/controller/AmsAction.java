package com.bonc.microapp.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.bonc.common.Auth;
import com.bonc.db.TxException;
import com.bonc.microapp.entity.AmsStaff;
import com.bonc.microapp.service.AmsService;
import com.bonc.tools.AESpkcs7paddingUtil;
import com.bonc.tools.AmsTool;
import com.bonc.tools.DateUtils;
import com.bonc.tools.JsonBody;
import com.bonc.tools.Page;
import com.bonc.tools.ParamVo;

@Scope("prototype")
@Controller
@RequestMapping({ "/ams" })
public class AmsAction {
	private Log log = LogFactory.getLog(this.getClass());
	
	@Autowired
	private AmsService amsService;
	
	@RequestMapping({"toActionLogManage.action"})
	protected ModelAndView toActionLogManage(HttpServletRequest request, @RequestParam Map<String, Object> form)
	{
		ModelAndView view = new ModelAndView("/ams/actionLogManage");
		view.addAllObjects(form);
		
		String beginDate = DateUtils.getNow("yyyy-MM-dd");
		String endDate = DateUtils.getNow("yyyy-MM-dd");
		
		view.addObject("beginDate", beginDate);
		view.addObject("endDate", endDate);

		return view;
	}
	
	@RequestMapping(value = { "getActionLogPage.action", "/" }, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public Object getActionLogPage(HttpServletRequest request,	HttpServletResponse response, @RequestParam Map<String, Object> form) {
		
		ParamVo vo = new ParamVo();
		vo.setParam(form);
		
		vo.put("pageSize", form.get("rows"));
		vo.put("currentPage", form.get("page"));
		vo.setOrderby((String)form.get("sort"), (String)form.get("order"));		
		Page page = (Page)this.amsService.getActionLogPage(vo);
		
		List list = page.getDataList();
		
		for(int i=0; list != null && i < list.size(); i++){
			Map map = (Map)list.get(i);
			
			String encryptLoginName = map.get("LOGIN_NAME").toString();
			String encryptPassword = map.get("PASSWORD").toString();
			
			try {
				encryptLoginName = AESpkcs7paddingUtil.encrypt( encryptLoginName, AmsTool.C_Key);
				encryptPassword = AESpkcs7paddingUtil.encrypt(encryptPassword , AmsTool.C_Key);
			} catch (Exception e) {
				e.printStackTrace();
			}			
			
			map.put("ENCRYPT_LOGIN_NAME", encryptLoginName);
			map.put("ENCRYPT_PASSWORD",  encryptPassword);
			//map.put("PASSWORD", "");//隐藏密码
		}

		JsonBody res = new JsonBody();
		res.put("total", page.getTotalCnt());
		res.put("rows", page.getDataList());		
		return res;
	}

	
	
	@RequestMapping(value = {"queryWorkTime.action", "/" }, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public Object queryWorkTime(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> form) {		
		JsonBody res = new JsonBody();		
		String loginName = form.get("loginName").toString();
		String password = form.get("password").toString();
		AmsTool amsTool = new AmsTool();
		
		float wortTime = 0;
		try {
			wortTime = amsTool.queryWorkTime(loginName, password);
			
			res.setCode(0);
			res.setMessage("工作时长：" + wortTime + "小时");
			
		} catch (TxException e) {
			res.setCode(-1);
			res.setMessage(e.getMessage());
			return res;
		}		
		return res;
	}
	
	@RequestMapping(value = {"staffLoginout.action", "/" }, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public Object staffLoginout(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> form) {		
		JsonBody res = new JsonBody();		
		String loginName = form.get("loginName").toString();
		String password = form.get("password").toString();
		AmsTool amsTool = new AmsTool();
		
		boolean ret = false;
		try {
			ret = amsTool.workloginout(loginName, password);
			if(ret){
				res.setCode(0);
				res.setMessage("签出成功");
			}else{
				res.setCode(-1);
				res.setMessage("登录失败，请检查账号密码是否正确，或检查账号是否被锁定");
			}
		} catch (TxException e) {
			res.setCode(-1);
			res.setMessage(e.getMessage());
			return res;
		}		
		return res;
	}
	
	@RequestMapping(value = {"staffLogin.action", "/" }, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public Object staffLogin(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> form) {
		
		JsonBody res = new JsonBody();		
		String loginName = form.get("loginName").toString();
		String password = form.get("password").toString();
		AmsTool amsTool = new AmsTool();
		
		boolean ret = false;
		try {
			ret = amsTool.login(loginName, password);
			if(ret){
				res.setCode(0);
				res.setMessage("签入成功");
			}else{
				res.setCode(-1);
				res.setMessage("登录失败，请检查账号密码是否正确，或检查账号是否被锁定");
			}
		} catch (TxException e) {
			res.setCode(-1);
			res.setMessage(e.getMessage());
			return res;
		}		
		return res;
	}
	
	@RequestMapping(value = {"saveStaff.action", "/" }, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public Object saveStaff(HttpServletRequest request,HttpServletResponse response,@RequestParam Map<String, Object> form) {
		JsonBody res = new JsonBody();
		JsonBody jsonBody = new JsonBody(form);

		AmsStaff staff = (AmsStaff)jsonBody.toJava("", AmsStaff.class);
		if(staff.getId() == null || staff.getId() <= 0) {
			staff.setId(-1L);
			staff.setCreateDate(DateUtils.getDate());
			staff.setUpdateDate(DateUtils.getDate());
		}else{
			staff.setUpdateDate(DateUtils.getDate());
		}

		ParamVo vo = new ParamVo();
		vo.setParam(form);
		vo.setObject(staff);
		try {
			this.amsService.txSaveStaff(vo);
			res.setCode(0);
			res.setMessage("保存成功");
		} catch (TxException e) {
			res.setCode(-1);
			res.setMessage(e.getMessage());
			return res;
		}
		return res;
	}
	
	@RequestMapping({"toStaffEdit.action"})
	protected ModelAndView toStaffEdit(HttpServletRequest request, @RequestParam Map<String, Object> form)
	{
		ModelAndView view = new ModelAndView("/ams/staffEdit");
		
		String id = (String)form.get("id");
		if(StringUtils.isNumeric(id)) {
			ParamVo vo = new ParamVo();
			vo.put("id", Long.valueOf(id));
			Map staff = (Map)this.amsService.getStaffInfo(vo);
			view.addObject("staff", staff);
		} else {
			AmsStaff staff = new AmsStaff();
			view.addObject("staff", staff);
		}
		return view;
	}

	@RequestMapping({"toStaffManage.action"})
	protected ModelAndView toStaffManage(HttpServletRequest request, @RequestParam Map<String, Object> param)
	{
		ModelAndView view = new ModelAndView("/ams/staffManage");		
		return view;
	}
	
	@RequestMapping(value = { "getStaffPage.action", "/" }, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public Object getStaffPage(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		
		ParamVo vo = new ParamVo();
		vo.setParam(form);
		
		vo.put("pageSize", form.get("rows"));
		vo.put("currentPage", form.get("page"));
		vo.setOrderby((String)form.get("sort"), (String)form.get("order"));		
		Page page = (Page)this.amsService.getStaffPage(vo);
		
		List list = page.getDataList();
		
		for(int i=0; list != null && i < list.size(); i++){
			Map map = (Map)list.get(i);
			
			String encryptLoginName = map.get("LOGIN_NAME").toString();
			String encryptPassword = map.get("PASSWORD").toString();
			
			try {
				encryptLoginName = AESpkcs7paddingUtil.encrypt( encryptLoginName, AmsTool.C_Key);
				encryptPassword = AESpkcs7paddingUtil.encrypt(encryptPassword , AmsTool.C_Key);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			map.put("ENCRYPT_LOGIN_NAME", encryptLoginName);
			map.put("ENCRYPT_PASSWORD",  encryptPassword);
			//map.put("PASSWORD", "");//隐藏密码
		}

		JsonBody res = new JsonBody();
		res.put("total", page.getTotalCnt());
		res.put("rows", page.getDataList());		
		return res;
	}
}
