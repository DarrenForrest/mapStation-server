package com.bonc.microapp.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonc.common.Auth;
import com.bonc.core.entity.Org;
import com.bonc.core.entity.Staff;
import com.bonc.core.entity.StaffRole;
import com.bonc.db.IDao;
import com.bonc.tools.ParamVo;
import com.bonc.tools.Template;

@Service
public class TemplateService {
	
	@Autowired
	private IDao staffDao;
	
	@Autowired
	private IDao staffRoleDao;
	
	@Autowired
	private IDao orgDao;
	
	@Autowired
	private Template template; //模板类必须自动注入，否则数据库连接无法释放（原因待分析）
	
	public String format(String template, Object...args) {
		return Template.format(template, args);
	}
	
	public TemplateService() {
		RegistAllVariables();
	}
	
	public void RegistAllVariables() {
		
		//注册模板变量
		Template.regist("T1",              this, "getT1",      "测试变量");
		Template.regist("roleIds",         this, "getRoleIds", "权限ID列表");
		Template.regist("staff",           this, "getStaff",   "员工信息");
		Template.regist("org",             this, "getOrg",     "营业厅信息");
		Template.regist("count",           this, "getCount",   "数量");
		Template.regist("optionOrgId",     this, "getOptionOrgId",     "营业厅信息");
		
		//显示注册成功的变量
		System.out.println(Template.listVariables());
	}
	
	public Object getT1(Object...args) {
		return "T1为测试变量";
	}
	
	public Object getCount(Object...args) {
		//调用format格式化的时候args第一个传request，第二个传count
		return args[1];
	}
	
	public Object getRoleIds(Object...args) {
		Auth auth = Auth.getAuth((HttpServletRequest)args[0]);
		String roleIds = "";
		ParamVo vo = new ParamVo();
		vo.put("staffId", auth.getStaffId());
		vo.setMethod("selectList");
		vo.setObjectClass(StaffRole.class);
		List<Object> list = this.staffRoleDao.selectList(vo);
		for(Object obj : list) {
			StaffRole i = (StaffRole)obj;
			if(roleIds.length() > 0) {
				roleIds += ",";
			}
			roleIds += i.getRoleId();
		}
		return roleIds;
	}
	
	public Object getStaff(Object...args) {
		Auth auth = Auth.getAuth((HttpServletRequest)args[0]);
		ParamVo vo = new ParamVo();
		vo.setObjectClass(Staff.class);
		vo.put("staffId", auth.getStaffId());
		return this.staffDao.getInfoById(vo);
	}
	
	public Object getOrg(Object...args) {
		Auth auth = Auth.getAuth((HttpServletRequest)args[0]);
		ParamVo vo = new ParamVo();
		vo.setObjectClass(Org.class);
		vo.put("orgId", auth.getOrgId());
		return this.orgDao.getInfoById(vo);
	}
	
	public Object getOptionOrgId(Object...args) {
		Auth auth = Auth.getAuth((HttpServletRequest)args[0]);
		return auth.getOptionOrgId();
	}
}
