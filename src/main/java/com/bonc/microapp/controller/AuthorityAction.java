package com.bonc.microapp.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.bonc.common.Auth;
import com.bonc.common.TokenInterceptor;
import com.bonc.common.TokenInterceptor.Token;
import com.bonc.core.entity.File;
import com.bonc.core.entity.Role;
import com.bonc.core.entity.Rule;
import com.bonc.core.entity.Staff;
import com.bonc.core.entity.StaffOrg;
import com.bonc.core.service.AuthorityService;
import com.bonc.db.TxException;
import com.bonc.microapp.dao.F;
import com.bonc.microapp.entity.StaffExtend;
import com.bonc.microapp.service.ConfigDataService;
import com.bonc.tools.ConfigUtil;
import com.bonc.tools.DateUtils;
import com.bonc.tools.JsonBody;
import com.bonc.tools.MD5Utils;
import com.bonc.tools.OsUtils;
import com.bonc.tools.Page;
import com.bonc.tools.ParamVo;
import com.bonc.tools.StrUtil;

@Scope("prototype")
@Controller
@RequestMapping({ "/auth" })
public class AuthorityAction {
	
	@Autowired
	private AuthorityService authorityService;
	
	@Autowired
	private ConfigDataService configDataService;
	

	private Log log = LogFactory.getLog(this.getClass());
	
	@RequestMapping({"toStaffOrgEdit.action"})
	protected ModelAndView toStaffOrgEdit(HttpServletRequest request, @RequestParam Map<String, Object> form) {
		ModelAndView view = new ModelAndView("/staff/staffOrgEdit");
		
		String staffId = (String)form.get("staffId");
		if(StringUtils.isNumeric(staffId)) {
			ParamVo vo = new ParamVo();
			vo.put("staffId", staffId);
			vo.put("type", form.get("type"));
			Map staff = (Map)this.authorityService.getStaffInfo(vo);
			view.addObject("staff", staff);
			
			
			StaffOrg staffOrg = null;
			vo = new ParamVo();
			vo.setParam(form);
			try {
				staffOrg = this.authorityService.queryStaffOrg(vo);
			} catch (TxException e) {
				e.printStackTrace();
			}
			
			view.addObject("staffOrg", staffOrg);
			view.addObject("type", staffOrg.getType());
			
		} else {
			Staff staff = new Staff();
			staff.setOrgId(Auth.getAuth(request).getOrgId());
			view.addObject("staff", staff);
		}
		
		view.addObject("orgId", Auth.getAuth(request).getOrgId());
		return view;
	}
	
	@RequestMapping(value = { "saveStaffOrg.action", "/" }, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public Object saveStaffOrg(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		
		JsonBody res = new JsonBody();
		JsonBody jsonBody = new JsonBody(form);
		StaffOrg staffOrg = (StaffOrg)jsonBody.toJava("", StaffOrg.class);
	
		
		ParamVo vo = new ParamVo();
		vo.setObject(staffOrg);
		vo.setParam(form);
		try {
			this.authorityService.txSaveStaffOrg(vo);
			res.setCode(0);
			res.setMessage("保存成功");
		} catch (TxException e) {
			res.setCode(-1);
			res.setMessage(e.getMessage());
			return res;
		}
		
		return res;
	}
	
	@RequestMapping({"toStaffOrgManage.action"})
	protected ModelAndView toStaffOrgManage(HttpServletRequest request, @RequestParam Map<String, Object> form) {
		ModelAndView view = new ModelAndView("/staff/staffOrgManage");
		view.addObject("orgId", Auth.getAuth(request).getOrgId());
		return view;
	}
	
	@RequestMapping(value = { "getStaffOrgPage.action", "/" }, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public Object getStaffOrgPage(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		
		ParamVo vo = new ParamVo();
		vo.setParam(form);
		
		vo.put("pageSize", form.get("rows"));
		vo.put("currentPage", form.get("page"));
		vo.setOrderby((String)form.get("sort"), (String)form.get("order"));
		vo.put("orgId", (StringUtils.isBlank((String)form.get("orgId")) ? Auth.getAuth(request).getOrgId() : (String)form.get("orgId")));
		
		Page page = (Page)this.authorityService.getStaffOrgPage(vo);
		
		JsonBody res = new JsonBody();
		res.put("total", page.getTotalCnt());
		res.put("rows", page.getDataList());
		
		return res;
	}
	
	/////////////////////////////////////////////
	//getTreeDate 为combotree控件准备数据
	@RequestMapping(value = { "getTreeData.action", "/" }, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public Object getTreeData(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		
		Auth auth = Auth.getAuth(request);
		
		if(auth == null)
		{
			return null;
		}
		
		Long orgId = auth.getOrgId();
		List list = this.configDataService.getComboTreeData(request, (String)form.get("catalog"), form.get("type"), form);
		
		return list;
	}
	
	/////////////////////////////////////////////
	//getTreeGridDate 为treegrid控件准备数据
	@RequestMapping(value = { "getTreeGridData.action", "/" }, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public Object getTreeGridData(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		
		Auth auth = Auth.getAuth(request);
		
		if(auth == null)
		{
			return null;
		}
		
		Long orgId = auth.getOrgId();
		List list = this.configDataService.getTreeGridData(request, (String)form.get("catalog"), form.get("type"), form);
		
		return list;
	}
	
	/////////////////////////////////////////////
	//modify password
	@Token(save=true)
	@RequestMapping({"toModifyPassword.action"})
	protected ModelAndView toChangePassword(HttpServletRequest request, @RequestParam Map<String, Object> form) {
		ModelAndView view = new ModelAndView("/staff/modifyPassword");
		view.addObject("orgId", Auth.getAuth(request).getOrgId());
		
		StaffExtend staffExtend = this.authorityService.queryStaffExtend(Auth.getAuth(request).getStaffId());		
		String mobile = staffExtend == null ? "" :staffExtend.getMobile();
		
		view.addObject("mobile",mobile);
		
		return view;
	}
	
	@Token(remove=true)
	@RequestMapping(value = { "modifyPassword.action", "/" }, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public Object modifyPassword(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		
		JsonBody res = new JsonBody();
		JsonBody jsonBody = new JsonBody(form);
		
		Auth auth = Auth.getAuth(request);
		if(auth == null) {
			res.setCode(-1);
			res.setMessage("会话失效，请重新登录！");
			return res;
		}
		
		ParamVo vo = new ParamVo();
		vo.setParam(form);
		vo.put("staffId", auth.getStaffId().toString());
		try {
			this.authorityService.txModifyPassword(vo);
			res.setCode(0);
			res.setMessage("修改成功");
		} catch (TxException e) {
			res.setCode(-1);
			res.setMessage(e.getMessage());
			TokenInterceptor.restoreToken(request);
			return res;
		}
		
		return res;
	}
	
	@RequestMapping(value = { "modifyMobile.action", "/" }, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public Object modifyMobile(HttpServletRequest request,HttpServletResponse response,	@RequestParam Map<String, Object> form) {
		
		JsonBody res = new JsonBody();
		JsonBody jsonBody = new JsonBody(form);
		
		Auth auth = Auth.getAuth(request);
		if(auth == null) {
			res.setCode(-1);
			res.setMessage("会话失效，请重新登录！");
			return res;
		}
		
		ParamVo vo = new ParamVo();
		vo.setParam(form);
		vo.put("staffId", auth.getStaffId());
		try {
			this.authorityService.txModifyMobile(vo);
			res.setCode(0);
			res.setMessage("修改成功");
		} catch (TxException e) {
			res.setCode(-1);
			res.setMessage(e.getMessage());
			TokenInterceptor.restoreToken(request);
			return res;
		}
		
		return res;
	}
	
	/////////////////////////////////////////////
	//STAFF
	@RequestMapping({"toStaffManage.action"})
	protected ModelAndView toStaffManage(HttpServletRequest request, @RequestParam Map<String, Object> form) {
		ModelAndView view = new ModelAndView("/staff/staffManage");
		view.addObject("orgId", Auth.getAuth(request).getOrgId());
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
		vo.put("orgId", (StringUtils.isBlank((String)form.get("orgId")) ? Auth.getAuth(request).getOrgId() : (String)form.get("orgId")));
		
		Page page = (Page)this.authorityService.getStaffPage(vo);
		
		JsonBody res = new JsonBody();
		res.put("total", page.getTotalCnt());
		res.put("rows", page.getDataList());
		
		return res;
	}
	
	@RequestMapping({"toStaffEdit.action"})
	protected ModelAndView toStaffEdit(HttpServletRequest request, @RequestParam Map<String, Object> form) {
		ModelAndView view = new ModelAndView("/staff/staffEdit");
		
		String staffId = (String)form.get("staffId");
		if(StringUtils.isNumeric(staffId)) {
			ParamVo vo = new ParamVo();
			vo.put("staffId", staffId);
			Map staff = (Map)this.authorityService.getStaffInfo(vo);
			view.addObject("staff", staff);
		} else {
			Staff staff = new Staff();
			staff.setOrgId(Auth.getAuth(request).getOrgId());
			view.addObject("staff", staff);
		}
		
		view.addObject("orgId", Auth.getAuth(request).getOrgId());
		return view;
	}
	
	@RequestMapping(value = { "saveStaff.action", "/" }, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public Object saveStaff(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		
		JsonBody res = new JsonBody();
		JsonBody jsonBody = new JsonBody(form);
		Staff staff = (Staff)jsonBody.toJava("", Staff.class);
		if(staff.getStaffId() == null || staff.getStaffId() <= 0) {
			staff.setStaffId(-1L);
			staff.setState(1L);
			staff.setStateDate(DateUtils.getDate());
			staff.setCreateDate(DateUtils.getDate());
		} else {
			Auth auth = Auth.getAuth(request);
			if(auth != null) {
				if(staff.getStaffId().equals(auth.getStaffId())) {
					res.setCode(-1);
					res.setMessage("管理员不能修改自己的信息，可由上级管理员修改！");
					return res;
				}
			}
		}
		
		
		if( !StrUtil.isEmpty(staff.getPassword()) )
		{
			String password = staff.getPassword();
			String md5Flag = ConfigUtil.getInstance().getValueByProperty("config/microapp.properties", "md5Flag");		
			if(md5Flag.equals("T"))
			{
				password = MD5Utils.MD5(password).toUpperCase();
				staff.setPassword(password);
			}
		}
		
		ParamVo vo = new ParamVo();
		vo.setObject(staff);
		vo.setParam(form);
		try {
			this.authorityService.txSaveStaff(vo);
			res.setCode(0);
			res.setMessage("保存成功");
		} catch (TxException e) {
			res.setCode(-1);
			res.setMessage(e.getMessage());
			return res;
		}
		
		return res;
	}
	
	@RequestMapping(value = { "delStaff.action", "/" }, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public Object delStaff(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		
		JsonBody res = new JsonBody();
		ParamVo vo = new ParamVo();
		vo.put("staffId", form.get("staffId"));
		try {
			this.authorityService.txDelStaff(vo);
			res.setCode(0);
			res.setMessage("删除成功");
		} catch (TxException e) {
			res.setCode(-1);
			res.setMessage(e.getMessage());
			return res;
		}
		
		return res;
	}
	
	@RequestMapping(value = { "getStaffRolePage.action", "/" }, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public Object getStaffRolePage(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		
		ParamVo vo = new ParamVo();
		vo.setParam(form);
		
		vo.put("pageSize", form.get("rows"));
		vo.put("currentPage", form.get("page"));
		vo.setOrderby((String)form.get("sort"), (String)form.get("order"));
		vo.put("orgId", (StringUtils.isBlank((String)form.get("orgId")) ? Auth.getAuth(request).getOrgId() : (String)form.get("orgId")));
		
		Page page = (Page)this.authorityService.getStaffRolePage(vo);
		
		JsonBody res = new JsonBody();
		res.put("total", page.getTotalCnt());
		res.put("rows", page.getDataList());
		
		return res;
	}
	
	@RequestMapping(value = { "getRoleByStaffId.action", "/" }, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public Object getRoleByStaffId(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		
		ParamVo vo = new ParamVo();
		vo.put("staffId", form.get("staffId") == null ? 0 : form.get("staffId"));
		vo.put("authStaffId", Auth.getAuth(request).getStaffId());
		return this.authorityService.getStaffRoleList(vo);
	}
	
	@RequestMapping({"toGrantQuery.action"})
	protected ModelAndView toGrantQuery(HttpServletRequest request, @RequestParam Map<String, Object> form) {
		ModelAndView view = new ModelAndView("/staff/grantQuery");
		view.addObject("orgId", Auth.getAuth(request).getOrgId());
		return view;
	}
	
	@RequestMapping(value = { "getGrantQueryPage.action", "/" }, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public Object getGrantQueryPage(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		
		ParamVo vo = new ParamVo();
		vo.setParam(form);
		
		vo.put("pageSize", form.get("rows"));
		vo.put("currentPage", form.get("page"));
		vo.setOrderby((String)form.get("sort"), (String)form.get("order"));
		
		Page page = (Page)this.authorityService.getGrantQueryPage(vo);
		
		JsonBody res = new JsonBody();
		res.put("total", page.getTotalCnt());
		res.put("rows", page.getDataList());
		
		return res;
	}
	
	/////////////////////////////////////////////
	//RULE
	@RequestMapping({"toRuleManage.action"})
	protected ModelAndView toRuleManage(HttpServletRequest request, @RequestParam Map<String, Object> form) {
		ModelAndView view = new ModelAndView("/staff/ruleManage");
		view.addObject("orgId", Auth.getAuth(request).getOrgId());
		return view;
	}
	
	@RequestMapping(value = { "getRulePage.action", "/" }, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public Object getRulePage(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		
		ParamVo vo = new ParamVo();
		vo.setParam(form);
		
		vo.put("pageSize", "10000");//form.get("rows"));
		vo.put("currentPage", form.get("page"));
		vo.setOrderby((String)form.get("sort"), (String)form.get("order"));
		
		Page page = (Page)this.authorityService.getRulePage(vo);
		{ //easyui-treegrid需要特定字段_parentId来组织上下级关系
			for(Object obj : page.getDataList()) {
				Map i = (Map)obj;
				i.put("_parentId", i.get("PARENT_ID"));
			}
		}
		
		JsonBody res = new JsonBody();
		res.put("total", page.getTotalCnt());
		res.put("rows", page.getDataList());
		
		return res;
	}
	
	@RequestMapping({"toRuleEdit.action"})
	protected ModelAndView toRuleEdit(HttpServletRequest request, @RequestParam Map<String, Object> form) {
		ModelAndView view = new ModelAndView("/staff/ruleEdit");
		
		String ruleId = (String)form.get("ruleId");
		if(StringUtils.isNumeric(ruleId)) {
			ParamVo vo = new ParamVo();
			vo.put("ruleId", ruleId);
			Map rule = (Map)this.authorityService.getRuleInfo(vo);
			view.addObject("rule", rule);
		}
		
		view.addObject("orgId", Auth.getAuth(request).getOrgId());
		return view;
	}
	
	@RequestMapping(value = { "saveRule.action", "/" }, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public Object saveRule(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		
		JsonBody res = new JsonBody();
		JsonBody jsonBody = new JsonBody(form);
		Rule rule = (Rule)jsonBody.toJava("", Rule.class);
		if(rule.getRuleId() == null || rule.getRuleId() <= 0) {
			rule.setRuleId(-1L);
			rule.setState(1L);
			rule.setStateDate(DateUtils.getDate());
			rule.setCreateDate(DateUtils.getDate());
		}
		ParamVo vo = new ParamVo();
		vo.setObject(rule);
		try {
			this.authorityService.txSaveRule(vo);
			res.setCode(0);
			res.setMessage("保存成功");
		} catch (TxException e) {
			res.setCode(-1);
			res.setMessage(e.getMessage());
			return res;
		}
		
		return res;
	}
	
	@RequestMapping(value = { "delRule.action", "/" }, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public Object delRule(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		
		JsonBody res = new JsonBody();
		ParamVo vo = new ParamVo();
		vo.put("ruleId", form.get("ruleId"));
		try {
			this.authorityService.txDelRule(vo);
			res.setCode(0);
			res.setMessage("删除成功");
		} catch (TxException e) {
			res.setCode(-1);
			res.setMessage(e.getMessage());
			return res;
		}
		
		return res;
	}
	
	/////////////////////////////////////////////
	//ROLE
	@RequestMapping({"toRoleManage.action"})
	protected ModelAndView toRoleManage(HttpServletRequest request, @RequestParam Map<String, Object> form) {
		ModelAndView view = new ModelAndView("/staff/roleManage");
		view.addObject("orgId", Auth.getAuth(request).getOrgId());
		return view;
	}
	
	@RequestMapping(value = { "getRolePage.action", "/" }, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public Object getRolePage(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		
		ParamVo vo = new ParamVo();
		vo.setParam(form);
		
		vo.put("pageSize", form.get("rows"));
		vo.put("currentPage", form.get("page"));
		vo.setOrderby((String)form.get("sort"), (String)form.get("order"));
		
		Page page = (Page)this.authorityService.getRolePage(vo);
		
		JsonBody res = new JsonBody();
		res.put("total", page.getTotalCnt());
		res.put("rows", page.getDataList());
		
		return res;
	}
	
	@RequestMapping({"toRoleEdit.action"})
	protected ModelAndView toRoleEdit(HttpServletRequest request, @RequestParam Map<String, Object> form) {
		ModelAndView view = new ModelAndView("/staff/roleEdit");
		
		String roleId = (String)form.get("roleId");
		if(StringUtils.isNumeric(roleId)) {
			ParamVo vo = new ParamVo();
			vo.put("roleId", roleId);
			Map role = (Map)this.authorityService.getRoleInfo(vo);
			view.addObject("role", role);
		}
		
		view.addObject("orgId", Auth.getAuth(request).getOrgId());
		return view;
	}
	
	@RequestMapping(value = { "saveRole.action", "/" }, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public Object saveRole(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		
		JsonBody res = new JsonBody();
		JsonBody jsonBody = new JsonBody(form);
		Role role = (Role)jsonBody.toJava("", Role.class);
		if(role.getRoleId() == null || role.getRoleId() <= 0) {
			role.setRoleId(-1L);
			role.setState(1L);
			role.setStateDate(DateUtils.getDate());
			role.setCreateDate(DateUtils.getDate());
		}
		ParamVo vo = new ParamVo();
		vo.setObject(role);
		vo.setParam(form);
		try {
			this.authorityService.txSaveRole(vo);
			res.setCode(0);
			res.setMessage("保存成功");
		} catch (TxException e) {
			res.setCode(-1);
			res.setMessage(e.getMessage());
			return res;
		}
		
		return res;
	}
	
	@RequestMapping(value = { "delRole.action", "/" }, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public Object delRole(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		
		JsonBody res = new JsonBody();
		ParamVo vo = new ParamVo();
		vo.put("roleId", form.get("roleId"));
		try {
			this.authorityService.txDelRole(vo);
			res.setCode(0);
			res.setMessage("删除成功");
		} catch (TxException e) {
			res.setCode(-1);
			res.setMessage(e.getMessage());
			return res;
		}
		
		return res;
	}
	
	@RequestMapping(value = { "getRuleByRoleId.action", "/" }, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public Object getRuleByRoleId(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		
		ParamVo vo = new ParamVo();
		vo.put("roleId", form.get("roleId") == null ? 0 : form.get("roleId"));
		return this.authorityService.getRoleRuleList(vo);
	}
	
	/////////////////////////////////////////////
	//ORG
	@RequestMapping({"toOrgManage.action"})
	protected ModelAndView toOrgManage(HttpServletRequest request, @RequestParam Map<String, Object> form) {
		ModelAndView view = new ModelAndView("/staff/orgManage");
		view.addObject("orgId", Auth.getAuth(request).getOrgId());
		return view;
	}
	
	@RequestMapping(value = { "getOrgPage.action", "/" }, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public Object getOrgPage(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		
		ParamVo vo = new ParamVo();
		vo.setParam(form);
		
		vo.put("pageSize", "10000");//form.get("rows"));
		vo.put("currentPage", form.get("page"));
		vo.setOrderby((String)form.get("sort"), (String)form.get("order"));
		
		Page page = (Page)this.authorityService.getOrgPage(vo);
		{ //easyui-treegrid需要特定字段_parentId来组织上下级关系
			for(Object obj : page.getDataList()) {
				Map i = (Map)obj;
				i.put("_parentId", i.get("PARENT_ID"));
			}
		}
		
		JsonBody res = new JsonBody();
		res.put("total", page.getTotalCnt());
		res.put("rows", page.getDataList());
		
		return res;
	}
	
	@RequestMapping(value = { "saveOrg.action", "/" }, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public Object saveOrg(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		
		JsonBody res = new JsonBody();
		ParamVo vo = new ParamVo();
		vo.setParam(form);
		try {
			this.authorityService.txSaveOrg(vo);
			res.setCode(0);
			res.setMessage("保存成功");
		} catch (TxException e) {
			res.setCode(-1);
			res.setMessage(e.getMessage());
			return res;
		}
		
		return res;
	}
	
	@RequestMapping(value = { "delOrg.action", "/" }, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public Object delOrg(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		
		JsonBody res = new JsonBody();
		ParamVo vo = new ParamVo();
		vo.put("orgId", form.get("orgId"));
		try {
			this.authorityService.txDelOrg(vo);
			res.setCode(0);
			res.setMessage("删除成功");
		} catch (TxException e) {
			res.setCode(-1);
			res.setMessage(e.getMessage());
			return res;
		}
		
		return res;
	}
	
	/////////////////////////////////////////////
	//File
	@RequestMapping({"toFileManage.action"})
	protected ModelAndView toFileManage(HttpServletRequest request, @RequestParam Map<String, Object> form) {
		ModelAndView view = new ModelAndView("/file/fileManage");
		view.addObject("orgId", Auth.getAuth(request).getOrgId());
		return view;
	}
	
	@RequestMapping(value = { "getFilePage.action", "/" }, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public Object getFilePage(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		
		ParamVo vo = new ParamVo();
		
		vo.put("pageSize", form.get("rows"));
		vo.put("currentPage", form.get("page"));
		vo.setOrderby((String)form.get("sort"), (String)form.get("order"));
		
		Page page = (Page)this.authorityService.getFilePage(vo);
		
		JsonBody res = new JsonBody();
		res.put("total", page.getTotalCnt());
		res.put("rows", page.getDataList());
		
		return res;
	}
	
	@RequestMapping({"toFileEdit.action"})
	protected ModelAndView toFileEdit(HttpServletRequest request, @RequestParam Map<String, Object> form) {
		ModelAndView view = new ModelAndView("/file/fileEdit");
		
		String id = (String)form.get("id");
		if(StringUtils.isNumeric(id)) {
			ParamVo vo = new ParamVo();
			vo.put("id", id);
			Map file = (Map)this.authorityService.getFileInfo(vo);
			view.addObject("file", file);
		}
		view.addObject("id", id);
		view.addObject("type", form.get("type"));
		
		view.addObject("orgId", Auth.getAuth(request).getOrgId());
		return view;
	}
	
	
	
	@RequestMapping({"toFileUploadAndImport.action"})
	protected ModelAndView toFileUploadAndImport(HttpServletRequest request, @RequestParam Map<String, Object> form) {
		ModelAndView view = new ModelAndView("/file/fileUploadAndImport");
		
		String id = (String)form.get("id");
		if(StringUtils.isNumeric(id)) {
			ParamVo vo = new ParamVo();
			vo.put("id", id);
			Map file = (Map)this.authorityService.getFileInfo(vo);
			view.addObject("file", file);
		}
		view.addAllObjects(form);
		
		view.addObject("orgId", Auth.getAuth(request).getOrgId());
		return view;
	}
	
	

	
	@RequestMapping(value = { "saveFile.action", "/" }, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public Object saveFile(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		
		JsonBody res = new JsonBody();
		JsonBody jsonBody = new JsonBody(form);
		File file = (File)jsonBody.toJava("", File.class);
		
		if(file.getId() == null || file.getId() <= 0) {
			file.setId(-1L);
			file.setStaffId(Auth.getAuth(request).getStaffId());
			file.setState(1L);
			file.setStateDate(DateUtils.getDate());
			file.setCreateDate(DateUtils.getDate());
		}
		ParamVo vo = new ParamVo();
		vo.setObject(file);
		try {
			this.authorityService.txSaveFile(vo);
			res.setCode(0);
			res.setMessage("保存成功");
		} catch (TxException e) {
			res.setCode(-1);
			res.setMessage(e.getMessage());
			return res;
		}
		
		return res;
	}
	
	@RequestMapping(value = { "delFile.action", "/" }, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public Object delFile(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		
		JsonBody res = new JsonBody();
		ParamVo vo = new ParamVo();
		vo.put("id", form.get("id"));
		try {
			this.authorityService.txDelFile(vo);
			res.setCode(0);
			res.setMessage("删除成功");
		} catch (TxException e) {
			res.setCode(-1);
			res.setMessage(e.getMessage());
			return res;
		}
		
		return res;
	}
	
	public static void copyInputStreamToFile(InputStream ins, java.io.File file) {
		try {
			OutputStream os = new FileOutputStream(file);
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
			os.close();
			ins.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
