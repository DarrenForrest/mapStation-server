package com.bonc.microapp.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.bonc.core.entity.Org;
import com.bonc.core.entity.Staff;
import com.bonc.core.service.AuthorityService;
import com.bonc.db.IDao;
import com.bonc.db.TxException;
import com.bonc.microapp.service.ConfigDataService;
import com.bonc.tools.DateUtils;
import com.bonc.tools.JsonBody;
import com.bonc.tools.ParamVo;

@Scope("prototype")
@Controller
@RequestMapping({ "/" })
public class DemoAction {
	
	@Autowired
	private ConfigDataService configDataService;

	
	@Autowired
	private AuthorityService authorityService;
	
	@RequestMapping(value="/testView.action")
	public ModelAndView testView(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		
		System.out.println("testPage: " + form);
		
		String strView = (String)form.get("view");
		ModelAndView view = new ModelAndView(strView);
		
		view.addObject("debug", "DebugInfo");
		
		return view;
	}
	

	
	@RequestMapping(value = { "testGetJson.action", "/" })
	@ResponseBody
	public JsonBody testGetJson(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestBody JsonBody form) {
		
		System.out.println(form);
		
		JsonBody res = new JsonBody();
		res.setCode(-1);
		res.setMessage("unknown");
		
		Object obj = this.configDataService.getComboBoxData((String)form.get("catalog"), form.get("type"));
		res.setValue("data", obj);
		if(obj != null) {
			res.setCode(0);
			res.setMessage("ok");
		}
		
		return res;
	}
	
	@RequestMapping({ "testPage.action" })
	protected ModelAndView testPage(HttpServletRequest request, Map<String, Object> form) {
		ModelAndView view = new ModelAndView("/testPage");
		
		ParamVo vo = new ParamVo();
		vo.put("orgId", 1L);
		List<Org> list = (List<Org>) this.authorityService.getOrgList(vo);
		
		view.addObject("orgId", 1L);
		view.addObject("orgList", list );
		return view;
	}
	
	@RequestMapping(value = { "testSaveStaff.action", "/" }, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public Object testSaveStaff(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		
		ParamVo vo1 = new ParamVo();
		vo1.setParam(form);
		
		//AJAX传递LIST必须转为JSON串才可使用下列语句转换（先转出LIST，再转实体）
		List list = (List)new JsonBody(form).toJava("list", List.class);
		
		JsonBody res = new JsonBody();
		for(Object obj : list) {
			Map i = (Map)obj;
			JsonBody body = new JsonBody(i);
			Staff staff = (Staff)body.toJava("", Staff.class, true);
			
			if(staff.getStaffId() == null || staff.getStaffId() <= 0) {
				staff.setStaffId(-1L);
				staff.setState(1L);
				staff.setStateDate(DateUtils.getDate());
				staff.setCreateDate(DateUtils.getDate());
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
		}
		
		
		return res;
	}

}
