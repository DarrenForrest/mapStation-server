package com.bonc.microapp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.bonc.common.Auth;
import com.bonc.common.TokenInterceptor;
import com.bonc.common.TokenInterceptor.Token;
import com.bonc.core.entity.Org;
import com.bonc.core.service.AuthorityService;
import com.bonc.db.TxException;
import com.bonc.microapp.dao.F;
import com.bonc.microapp.service.ConfigDataService;
import com.bonc.tools.JSONTools;
import com.bonc.tools.JsonBody;
import com.bonc.tools.ParamVo;
import com.bonc.tools.StrUtil;

@Scope("prototype")
@Controller
@RequestMapping({ "/" })
public class WebAction {
	
	@Autowired
	private ConfigDataService configDataService;
	
	@Autowired
	private AuthorityService authorityService;
	
	@RequestMapping({ "selectOrg.action" })
	protected ModelAndView index(HttpServletRequest request) {
		ModelAndView view = new ModelAndView("/staff/selectOrg");
		
		Auth auth = Auth.getAuth(request);
		if(auth == null)
		{
			return null;
		}
		
		ParamVo vo = new ParamVo();
		vo.put("orgId", auth.getOrgId());
		List<Org> orgList = (List<Org>) this.authorityService.getOrgList(vo);
		
		String orgListJson = JSONTools.getJSONArray(orgList).toString() ;
		view.addObject("orgList", orgListJson );		
		return view;
	}
	

	/**
	 * 获取下拉框列表数据
	 * @param request
	 * @param response
	 * @param param{catalog:string, type:object}
	 * @return List
	 */
	@RequestMapping(value = { "getComboBoxData.action", "/" }, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public Object getComboBoxData(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam Map<String, Object> param) {
		
		Object obj = this.configDataService.getComboBoxData((String)param.get("catalog"), param.get("type"));
		
		return obj;
	}
	

}
