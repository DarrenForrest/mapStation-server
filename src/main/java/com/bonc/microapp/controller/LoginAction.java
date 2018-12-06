package com.bonc.microapp.controller;

import java.util.HashMap;
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
import com.bonc.core.entity.Staff;
import com.bonc.core.service.AuthorityService;
import com.bonc.microapp.service.ConfigDataService;
import com.bonc.tools.ConfigUtil;
import com.bonc.tools.JSONTools;
import com.bonc.tools.MD5Utils;
import com.bonc.tools.ParamVo;
import com.bonc.tools.StrUtil;

@Scope("prototype")
@Controller
@RequestMapping({ "/" })
public class LoginAction {
	protected Log log=LogFactory.getLog(this.getClass());
	
	@Autowired
	private ConfigDataService configDataService;
	
	@Autowired
	private AuthorityService authorityService;
	
	@RequestMapping({ "login.action" })
	protected ModelAndView login(HttpServletRequest request) {
		ModelAndView view = new ModelAndView("login");
		return view;
	}
	
	@RequestMapping(value = { "loginJson.action", "/" })
	@ResponseBody
	public Object loginJson(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		
		String ctx = request.getContextPath();
		
		log.debug("hello this is loginJson");		
		log.debug("form = " + JSONTools.getJSONObject(form).toString() );
		
		Map map = new HashMap<String, Object>();
		
		String username = (String)form.get("username");
		String password = (String)form.get("password");
		
		if(StrUtil.isEmpty(username))
		{
			map.put("code", "500");
			map.put("message","用户名不能为空");
			return map;
		}
		
		String md5Flag = ConfigUtil.getInstance().getValueByProperty("config/microapp.properties", "md5Flag");		
		if(md5Flag.equals("T"))
		{
			password = MD5Utils.MD5(password).toUpperCase();
		}

		
		ParamVo vo = new ParamVo();
		vo.put("staffName", username);
		vo.put("password", password);
		
		Staff staff = null;
		try{
			staff = this.authorityService.queryStaffByNamePassword(vo);
		}catch(Exception e)
		{
			map.put("code", "-1");
			map.put("message", "查询数据出错");
			map.put("url", ctx + "/login.action");		
			return map;
		}
		
		if(staff == null)
		{
			map.put("code", "-1");
			map.put("message", "用户名或密码错误，请检查");
			map.put("url", ctx + "/login.action");		
			return map;
		}

/*		
		if(staff.getState().equals(0L))// 0 注销
		{
			map.put("code", "-1");
			map.put("message", "用户已被注销，不允许登录，请联系管理员");
			map.put("url", ctx + "/login.action");		
			return map;
		}
		
		if(staff.getState().equals(2L))//2 密码过期
		{
			map.put("code", "-1");
			map.put("message", "密码已过期，不允许登录，请联系管理员");
			map.put("url", ctx + "/login.action");		
			return map;
		}
*/		
		
		Long staffId = staff.getStaffId();
		vo.put("staffId",  staffId);
		String optionOrgId  = "";
		
		try{
			optionOrgId = this.authorityService.queryStaffOptionOrg(vo);
		}catch(Exception e){
			e.printStackTrace();
			map.put("code", "-1");
			map.put("message", "查询数据出错");
			map.put("url", ctx + "/login.action");		
			return map;
		}
		
		if(staffId.equals(-999L))//针对root用户做特殊处理
		{
			optionOrgId = "1";
		}
		
		Auth auth = new Auth();
		//auth.setOrgId(staff.getOrgId());
		if(StringUtils.isNotBlank(optionOrgId)) {
			if(optionOrgId.contains(staff.getOrgId().toString()))
				auth.setOrgId(staff.getOrgId());
			else
				auth.setOrgId(Long.valueOf(optionOrgId.split(",")[0]));
		}
		auth.setStaffId(staff.getStaffId());
		auth.setStaffName(staff.getStaffName());
		auth.setOptionOrgId(optionOrgId);
		
		request.getSession().removeValue("auth"); // 先清空一下
		request.getSession().putValue("auth", auth);
		
		map.put("code", "0");
		map.put("message", "ok");
		map.put("url", ctx + "/main.action");		
		return map;
	}
	
	
	@RequestMapping(value = { "logout.action", "/" })
	@ResponseBody
	public Object logout(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		
		Map map = new HashMap<String, Object>();
		String ctx = request.getContextPath();
		
		request.getSession().removeValue("auth");
		
		
		map.put("code", "0");
		map.put("message", "ok");
		map.put("url", ctx + "/login.action");		
		return map;
	}
	
	@RequestMapping({ "main.action" })
	protected ModelAndView index(HttpServletRequest request) {
		ModelAndView view = new ModelAndView("main");
		
		Auth auth = Auth.getAuth(request);
		
		if(auth == null)
		{
			return new ModelAndView("redirect:/login.action");
		}
		
		List list = (List)this.configDataService.getComboTreeData(request, "menu", auth.getStaffId());
		view.addObject("menuList", list);
		view.addObject("staffName", auth.getStaffName());
		
		return view;
	}
	
	@RequestMapping({ "todoList.action" })
	protected ModelAndView todoList(HttpServletRequest request) {
		ModelAndView view = new ModelAndView("todoList");
		return view;
	}
}
