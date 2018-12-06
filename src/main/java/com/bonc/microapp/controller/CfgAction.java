package com.bonc.microapp.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
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

import com.bonc.common.Auth;
import com.bonc.core.entity.EnumCatalog;
import com.bonc.core.entity.EnumCfg;
import com.bonc.db.TxException;
import com.bonc.microapp.entity.Notice;
import com.bonc.microapp.service.CfgService;
import com.bonc.microapp.service.TemplateService;
import com.bonc.tools.DateUtils;
import com.bonc.tools.JsEngine;
import com.bonc.tools.JsonBody;
import com.bonc.tools.Page;
import com.bonc.tools.ParamVo;
import com.bonc.tools.Validator;

@Scope("prototype")
@Controller
@RequestMapping({ "/cfg" })
public class CfgAction {
	
	@Autowired
	private CfgService cfgService;
	
	@Autowired
	private TemplateService templateService;
	
	@RequestMapping(value = {"delEnumCfg.action", "/" }, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public Object delEnumCfg(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> form) {
		
		JsonBody res = new JsonBody();
		ParamVo vo = new ParamVo();
		vo.put("id", form.get("id"));
		
		try {
			this.cfgService.txDelEnumCfg(vo);						
			res.setCode(0);
			res.setMessage("删除成功");
		} catch (TxException e) {
			res.setCode(-1);
			res.setMessage(e.getMessage());
			return res;
		}		
		return res;
	}
	
	@RequestMapping(value = {"saveEnumCfg.action", "/" }, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public Object saveEnumCfg(HttpServletRequest request,HttpServletResponse response,@RequestParam Map<String, Object> form) {
		
		JsonBody res = new JsonBody();
		JsonBody jsonBody = new JsonBody(form);
	
		EnumCfg enumCfg = (EnumCfg)jsonBody.toJava("", EnumCfg.class);
		if(enumCfg.getId() == null || enumCfg.getId() <= 0) {
			enumCfg.setId(-1L);
			enumCfg.setUpdateDate(DateUtils.getDate());
		}else{
			enumCfg.setUpdateDate(DateUtils.getDate());
		}
	
		ParamVo vo = new ParamVo();
		vo.setParam(form);
		vo.setObject(enumCfg);
		try {
			this.cfgService.txSaveEnumCfg(vo);
			res.setCode(0);
			res.setMessage("保存成功");
		} catch (TxException e) {
			res.setCode(-1);
			res.setMessage(e.getMessage());
			return res;
		}
		return res;
	}
	
	@RequestMapping({"toEnumCfgEdit.action"})
	protected ModelAndView toEnumCfgEdit(HttpServletRequest request, @RequestParam Map<String, Object> form) {
		ModelAndView view = new ModelAndView("/cfg/enumCfgEdit");
		
		String id = (String)form.get("id");
		if(StringUtils.isNumeric(id)) {
			ParamVo vo = new ParamVo();
			vo.put("id", id);
			EnumCfg enumCfg = (EnumCfg)this.cfgService.getEnumCfg(vo);
			view.addObject("enumCfg", enumCfg);
		}
		
		view.addAllObjects(form);
		return view;
	}
	
	@RequestMapping(value = {"delEnumCatalog.action", "/" }, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public Object delEnumCatalog(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> form) {
		
		JsonBody res = new JsonBody();
		ParamVo vo = new ParamVo();
		vo.put("id", form.get("id"));
		try {
			this.cfgService.txDelEnumCatalog(vo);
			res.setCode(0);
			res.setMessage("删除成功");
		} catch (TxException e) {
			res.setCode(-1);
			res.setMessage(e.getMessage());
			return res;
		}		
		return res;
	}
	
	@RequestMapping(value = {"saveEnumCatalog.action", "/" }, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public Object saveEnumCatalog(HttpServletRequest request,HttpServletResponse response,@RequestParam Map<String, Object> form) {
		
		JsonBody res = new JsonBody();
		JsonBody jsonBody = new JsonBody(form);
	
		EnumCatalog enumCatalog = (EnumCatalog)jsonBody.toJava("", EnumCatalog.class);
		if(enumCatalog.getId() == null || enumCatalog.getId() <= 0) {
			enumCatalog.setId(-1L);
		}else{
			//
		}
	
		ParamVo vo = new ParamVo();
		vo.setParam(form);
		vo.setObject(enumCatalog);
		try {
			this.cfgService.txSaveEnumCatalog(vo);
			res.setCode(0);
			res.setMessage("保存成功");
			} catch (TxException e) {
				res.setCode(-1);
				res.setMessage(e.getMessage());
				return res;
			}
			return res;
		}
	 
		
	@RequestMapping({"toEnumCatalogEdit.action"})
	protected ModelAndView toEnumCatalogEdit(HttpServletRequest request, @RequestParam Map<String, Object> form) {
		ModelAndView view = new ModelAndView("/cfg/enumCatalogEdit");
		
		String id = (String)form.get("id");
		if(StringUtils.isNumeric(id)) {
			ParamVo vo = new ParamVo();
			vo.put("id", id);
			EnumCatalog enumCatalog = (EnumCatalog)this.cfgService.getEnumCatalog(vo);
			view.addObject("enumCatalog", enumCatalog);
		}
		
		view.addAllObjects(form);
		return view;
	}
	
	@RequestMapping(value = { "getEnumCfgPage.action", "/" }, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public Object getEnumCfgPage(HttpServletRequest request,	HttpServletResponse response,		@RequestParam Map<String, Object> form) {
		
		ParamVo vo = new ParamVo();
		vo.setParam(form);
		
		vo.put("pageSize", form.get("rows"));
		vo.put("currentPage", form.get("page"));
		vo.setOrderby((String)form.get("sort"), (String)form.get("order"));
		vo.put("orgId", (StringUtils.isBlank((String)form.get("orgId")) ? Auth.getAuth(request).getOrgId() : (String)form.get("orgId")));
		
		Page page = (Page)this.cfgService.getEnumCfgPage(vo);
		
		JsonBody res = new JsonBody();
		res.put("total", page.getTotalCnt());
		res.put("rows", page.getDataList());
		
		return res;
	}
	
	@RequestMapping(value = { "getEnumCatalogPage.action", "/" }, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public Object getEnumCatalogPage(HttpServletRequest request,	HttpServletResponse response,		@RequestParam Map<String, Object> form) {
		
		ParamVo vo = new ParamVo();
		vo.setParam(form);
		
		vo.put("pageSize", form.get("rows"));
		vo.put("currentPage", form.get("page"));
		vo.setOrderby((String)form.get("sort"), (String)form.get("order"));
		vo.put("orgId", (StringUtils.isBlank((String)form.get("orgId")) ? Auth.getAuth(request).getOrgId() : (String)form.get("orgId")));
		
		Page page = (Page)this.cfgService.getEnumCatalogPage(vo);
		
		JsonBody res = new JsonBody();
		res.put("total", page.getTotalCnt());
		res.put("rows", page.getDataList());
		
		return res;
	}
	
	@RequestMapping({"toEnumManage.action"})
	protected ModelAndView toEnumManage(HttpServletRequest request, @RequestParam Map<String, Object> form) {
		ModelAndView view = new ModelAndView("/cfg/enumManage");
		return view;
	}
	

	

	
	@RequestMapping({"toDispatchRuleManage.action"})
	protected ModelAndView toDispatchRuleManage(HttpServletRequest request, @RequestParam Map<String, Object> form) {
		ModelAndView view = new ModelAndView("/cfg/dispatchRuleManage");
		return view;
	}
	
	@RequestMapping(value = { "saveNoticeJson.action", "/" }, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public Object saveNoticeJson(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		
		JsonBody res = new JsonBody();
		JsonBody jsonBody = new JsonBody(form);
		Notice notice = (Notice)jsonBody.toJava("", Notice.class);
		if(notice.getId() == null || notice.getId() <= 0) {
			notice.setId(-1L);
			notice.setStaffId( Auth.getAuth(request).getStaffId() );
			notice.setState(1L);
			notice.setCreateDate(DateUtils.getDate());
			notice.setUpdateDate(DateUtils.getDate());
		}
		ParamVo vo = new ParamVo();
		vo.setObject(notice);
		try {
			this.cfgService.txSaveNotice(vo);
			res.setCode(0);
			res.setMessage("保存成功");
		} catch (TxException e) {
			res.setCode(-1);
			res.setMessage(e.getMessage());
			return res;
		}
		
		return res;
	}
	
	@RequestMapping(value = { "delNotice.action", "/" }, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public Object delNotice(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		
		JsonBody res = new JsonBody();
		ParamVo vo = new ParamVo();
		vo.put("id", form.get("id"));
		try {
			this.cfgService.txDelNotice(vo);
			res.setCode(0);
			res.setMessage("删除成功");
		} catch (TxException e) {
			res.setCode(-1);
			res.setMessage(e.getMessage());
			return res;
		}
		
		return res;
	}
	
	@RequestMapping({"noticeEdit.action"})
	protected ModelAndView noticeEdit(HttpServletRequest request, @RequestParam Map<String, Object> form)
	{
		ModelAndView view = new ModelAndView("/cfg/noticeEdit");			
		Auth auth = Auth.getAuth(request);
		if(auth == null)
		{
			view.setViewName("redirect:/login.action");
			return view;
		}
		
		String id = (String)form.get("id");
		if(StringUtils.isNumeric(id)) {
			ParamVo vo = new ParamVo();
			vo.put("id", id);
			Map notice = (Map)this.cfgService.getNotice(vo);
			view.addObject("notice", notice);
		}
		
		return view;
	}
	
	@RequestMapping({"noticeView.action"})
	protected ModelAndView noticeView(HttpServletRequest request, @RequestParam Map<String, Object> form)
	{
		ModelAndView view = new ModelAndView("/cfg/noticeView");			
		Auth auth = Auth.getAuth(request);
		if(auth == null)
		{
			view.setViewName("redirect:/login.action");
			return view;
		}
		
		String id = (String)form.get("id");
		if(StringUtils.isNumeric(id)) {
			ParamVo vo = new ParamVo();
			vo.put("id", id);
			Map notice = (Map)this.cfgService.getNotice(vo);
			view.addObject("notice", notice);
		}	
		return view;
	}
	
	@RequestMapping({"noticeList.action"})
	protected ModelAndView noticeList(HttpServletRequest request, @RequestParam Map<String, Object> param)
	{
		ModelAndView view = new ModelAndView("/cfg/noticeList");			
		Auth auth = Auth.getAuth(request);
		if(auth == null)
		{
			view.setViewName("redirect:/login.action");
			return view;
		}
		
		return view;
	}
	
	@RequestMapping({"noticeManage.action"})
	protected ModelAndView noticeManage(HttpServletRequest request, @RequestParam Map<String, Object> param)
	{
		ModelAndView view = new ModelAndView("/cfg/noticeManage");			
		Auth auth = Auth.getAuth(request);
		if(auth == null)
		{
			view.setViewName("redirect:/login.action");
			return view;
		}
		
		return view;
	}
	
	@RequestMapping({"noticeListData.action"})
	@ResponseBody
	public Object noticeListData(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam Map<String, Object> form)
	{
		Map mapData = new HashMap(); 
		
		String rows = (String)form.get("rows");
		String page = (String)form.get("page");
		String sort = (String)form.get("sort");
		String order = (String)form.get("order");
		
		ParamVo vo  = new ParamVo();
		vo.put("pageSize", Validator.getDefaultStr(rows, "10"));
		vo.put("currentPage", Validator.getDefaultStr(page, "1")); 
		vo.put("sort", sort);
		vo.put("order", order);
		vo.setOrderby(sort, order);
		
		Page resultPage = (Page) cfgService.queryNoticePage(request, vo);
		
		mapData.put("total", resultPage.getTotalCnt() );
		mapData.put("rows",  resultPage.getDataList() );		
		return 	mapData;	
	}
	
	@RequestMapping({"messageListData.action"})
	@ResponseBody
	public Object messageListData(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam Map<String, Object> form)
	{
		Map mapData = new HashMap(); 
		
		String rows = (String)form.get("rows");
		String page = (String)form.get("page");
		String sort = (String)form.get("sort");
		String order = (String)form.get("order");
		
		ParamVo vo  = new ParamVo();
		vo.put("pageSize", Validator.getDefaultStr(rows, "10"));
		vo.put("currentPage", Validator.getDefaultStr(page, "1")); 
		vo.put("sort", sort);
		vo.put("order", order);
		vo.setOrderby(sort, order);
		
		Page resultPage = (Page) cfgService.queryMessagePage(request, vo);
		
		List<Object> list = resultPage.getDataList();
		List<Object> returnList = new ArrayList<Object>();
		for(int i=0; i<list.size(); i++){
			Map<String, Object> temp = (Map<String, Object>)list.get(i);
			String express = temp.get("EXPRESS").toString();
			if(StringUtils.isNotEmpty(express)) {
				express = this.templateService.format(express, request);
				ScriptEngineManager m = new ScriptEngineManager();
				ScriptEngine s = m.getEngineByName("js");
				System.out.println(express);
				try {
					if(s.eval("function auto_foo() {" + express + "} auto_foo();").equals(Boolean.TRUE)) {
						ParamVo voTemplate  = new ParamVo();
						voTemplate.put("id", temp.get("TEMPLATE_ID"));
						Map templateMap = (Map)cfgService.queryMessageTemplate(request, voTemplate);
						
						ParamVo voSql = new ParamVo();
						String sql = templateMap.get("sql").toString();
						sql = this.templateService.format(sql, request);
						voSql.put("sql", sql);
						Map map = (Map)cfgService.querySql(voSql);
						templateMap.put("content", this.templateService.format(templateMap.get("content").toString(), request, map.get("COUNT")));
						templateMap.put("url", temp.get("URL"));
						returnList.add(templateMap);
					}
				} catch (ScriptException e) {
					e.printStackTrace();
				}
			}
		}
		mapData.put("total", returnList.size() );
		mapData.put("rows",  returnList );		
		return 	mapData;	
	}
	


}
