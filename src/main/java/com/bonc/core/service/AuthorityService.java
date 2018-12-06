package com.bonc.core.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonc.common.Auth;
import com.bonc.core.entity.BigData;
import com.bonc.core.entity.Org;
import com.bonc.core.entity.OrgExtend;
import com.bonc.core.entity.Role;
import com.bonc.core.entity.RoleRule;
import com.bonc.core.entity.Staff;
import com.bonc.core.entity.StaffOrg;
import com.bonc.core.entity.StaffRole;
import com.bonc.db.IDao;
import com.bonc.db.TxException;
import com.bonc.microapp.entity.StaffExtend;
import com.bonc.tools.ConfigUtil;
import com.bonc.tools.JsonBody;
import com.bonc.tools.ListToTree;
import com.bonc.tools.MD5Utils;
import com.bonc.tools.ParamVo;

@Service
public class AuthorityService {
	
	@Autowired
	private IDao fileDao;
	
	@Autowired
	private IDao roleDao;
	
	@Autowired
	private IDao roleRuleDao;

	@Autowired
	private IDao ruleDao;
	
	@Autowired
	private IDao staffDao;
	
	@Autowired
	private IDao staffRoleDao;
	
	@Autowired
	private IDao orgDao;
	
	@Autowired
	private IDao orgExtendDao;
	
	@Autowired 
	private IDao bigDataDao;
	
	@Autowired 
	private IDao staffOrgDao;
	
	@Autowired 
	private IDao staffExtendDao;
	
	public StaffOrg queryStaffOrg(ParamVo vo) throws TxException{
		vo.setObjectClass(StaffOrg.class);
		
		StaffOrg staffOrg = (StaffOrg)this.staffOrgDao.getInfoById(vo);
		return staffOrg;
	}
	
	public void txSaveStaffOrg(ParamVo vo) throws TxException {
		if(vo.getObject() == null) {
			throw new TxException("保存的员工管辖营业厅信息为空！");
		}
		
		if(!this.staffOrgDao.save(vo)) {
			throw new TxException("保存员工管辖营业厅信息出错！");
		}
	}
	
	public Object getStaffOrgPage(ParamVo vo) {
		vo.setMethod("selectPage");
		return this.staffOrgDao.selectPage(vo);
	}
	
	public Boolean txInsertBigData(ParamVo vo)throws TxException{
		
		if(!this.bigDataDao.add(vo)) {
			throw new TxException("插入big_data表出错！");
		}		
		return true;
	}
	
	public Boolean txUpdateBigData(ParamVo vo)throws TxException{
		
		if(!this.bigDataDao.edit(vo)) {
			throw new TxException("修改big_data表出错！");
		}		
		return true;
	}
	
	
	public BigData queryBigDataById(Long id){
		ParamVo vo = new ParamVo();
		vo.setMethod("selectById");
		vo.put("id", id);
		vo.setObjectClass(BigData.class);
		
		return (BigData)this.bigDataDao.getInfoById(vo);
	}
	
	public String queryStaffOptionOrg(ParamVo vo) throws TxException{
		StringBuffer sb = new StringBuffer();
		
		vo.setMethod("selectStaffOptionOrgList");		
		List<Object> list=this.staffRoleDao.selectList(vo);
		
		if( list == null)
		{
			return "0";
		}else if(list.size() < 1)
		{
			return "0";
		}
		
		for(int i=0; i<list.size(); i++ )
		{
			Long obj = (Long) list.get(i);
			
			if(i==0){
				sb.append( obj.toString() );
			}else
			{
				sb.append("," + obj.toString() );
			}
		}		
		return sb.toString();
	}
	
	public Staff queryStaffByNamePassword(ParamVo vo) throws TxException{
		
		String staffName = (String)vo.get("staffName");
		String password  = (String)vo.get("password");
		
		Map<String, String> mapPara = new HashMap<String,String>();
		mapPara.put("loginName", staffName);
		mapPara.put("password", password);
		
		Staff staff = (Staff)this.staffDao.select("selectStaffByNamePasswd", mapPara, Staff.class);
		return staff;
	}
	
	public StaffExtend queryStaffExtend(Long staffId){
		ParamVo vo = new ParamVo();
		vo.setObjectClass(StaffExtend.class);
		vo.put("staffId", staffId);
		StaffExtend staffExtend = (StaffExtend)this.staffExtendDao.getInfoById(vo);
		return staffExtend;
	}
	
	public void txModifyMobile(ParamVo vo) throws TxException {
		Long staffId = Long.valueOf(vo.get("staffId").toString());
		String mobile =  vo.get("mobile").toString();

		StaffExtend staffExtend = null;
		vo.setObjectClass(StaffExtend.class);
		staffExtend = (StaffExtend)this.staffExtendDao.getInfoById(vo);
		
		if(staffExtend == null){
			staffExtend = new StaffExtend();
			staffExtend.setStaffId(staffId);
			staffExtend.setMobile(mobile);
			staffExtend.setUpdateDate(new Date());
			staffExtend.setCreateDate(new Date());
		}else{
			staffExtend.setMobile(mobile);
		}
		
		vo = new ParamVo();
		vo.setObject(staffExtend);
		vo.setObjectClass(StaffExtend.class);
		if(!this.staffExtendDao.save(vo)){
			throw new TxException("保存员工扩展信息表出错");
		}
	}
	
	public void txModifyPassword(ParamVo vo) throws TxException {
		
		String staffId = (String)vo.get("staffId");
		String oldPassword = (String)vo.get("oldPassword");
		String newPassword = (String)vo.get("newPassword");
		String confirmPassword = (String)vo.get("confirmPassword");
		
		String md5Flag = ConfigUtil.getInstance().getValueByProperty("config/srjh.properties", "md5Flag");		
		if(md5Flag.equals("T"))
		{
			oldPassword = MD5Utils.MD5(oldPassword).toUpperCase();
			newPassword = MD5Utils.MD5(newPassword).toUpperCase();
			confirmPassword = MD5Utils.MD5(confirmPassword).toUpperCase();
		}
		
		if(
				StringUtils.isBlank(staffId) ||
				StringUtils.isBlank(oldPassword) ||
				StringUtils.isBlank(newPassword) ||
				StringUtils.isBlank(confirmPassword)
				) {
			throw new TxException("参数为空");
		}
		
		if(!newPassword.equals(confirmPassword)) {
			throw new TxException("两次输入的密码不相同");
		}
		
		vo.setObjectClass(Staff.class);
		Staff staff = (Staff)this.staffDao.getInfoById(vo);
		if(staff == null) {
			throw new TxException("找不到该员工");
		}
		if(!oldPassword.equals(staff.getPassword())) {
			throw new TxException("旧密码输入不正确");
		}
		
		staff.setPassword(newPassword);
		staff.setPasswordExpDate(DateUtils.addMonths(new Date(), 3));
		vo.setObject(staff);
		if(!this.staffDao.save(vo)) {
			throw new TxException("修改密码失败");
		}
	}
	
	public void txInserStaff(ParamVo vo) throws TxException {
		
		if(!this.staffDao.add(vo)) {
			throw new TxException("插入员工表出错！");
		}
		
	}
	
	public Object getStaffPage(ParamVo vo) {
		vo.setMethod("selectPage");
		return this.staffDao.selectPage(vo);
	}

	
	public Object getOrgList(ParamVo vo){
		vo.setMethod("selectList");
		vo.setObjectClass(Org.class);
		return this.orgDao.selectList(vo);
		
	}

	
	public Object getStaffInfo(ParamVo vo) {
		return this.staffDao.getInfoById(vo);
	}
	
	public void txSaveStaff(ParamVo vo) throws TxException {
		if(vo.getObject() == null) {
			throw new TxException("保存的员工信息为空！");
		}
		
		if(!this.staffDao.save(vo)) {
			throw new TxException("保存员工信息出错！");
		}
		
		//process role's rules
		Long staffId = ((Staff)vo.getObject()).getStaffId();
		if(this.staffRoleDao.delete(this.staffRoleDao.getNameSpace() + ".deleteByStaffId", vo.getObject()) < 0) {
			throw new TxException("删除原有员工角色关系出错！");
		}
		
		//以简单方式提交（只配员工角色）
		if(StringUtils.isNotBlank((String)vo.get("roles"))) {
			String[] roles = ((String)vo.get("roles")).split(",");
			for(String i : roles) {
				StaffRole t = new StaffRole();
				t.setId(-1L);
				t.setStaffId(staffId);
				t.setRoleId(Long.valueOf(i));
				vo.setObject(t);
				if(!this.staffRoleDao.add(vo)) {
					throw new TxException("插入员工角色关系出错！");
				}
			}
		}
		//以高级方式提交（可配置员工角色及管理范围）
		if(StringUtils.isNotBlank((String)vo.get("roleList"))) {
			//AJAX传递LIST必须转为JSON串才可使用下列语句转换（先转出LIST，再转实体）
			List roleList = (List)new JsonBody(vo.getParam()).toJava("roleList", List.class);
			for(Object obj : roleList) {
				Map i = (Map)obj;
				JsonBody body = new JsonBody(i);
				StaffRole staffRole = (StaffRole)body.toJava("", StaffRole.class, true);
				staffRole.setId(-1L);
				staffRole.setStaffId(staffId);
				vo.setObject(staffRole);
				if(!this.staffRoleDao.add(vo)) {
					throw new TxException("插入员工角色关系出错！");
				}
			}
		}
	}
	
	public void txDelStaff(ParamVo vo) throws TxException {
		if(vo.get("staffId") == null) {
			throw new TxException("要删除的员工编号为空！");
		}
		
		if(!this.staffDao.remove(vo)) {
			throw new TxException("删除员工信息出错！");
		}
	}
	
	public Boolean checkAuth(HttpServletRequest request) {
		
		Auth auth = Auth.getAuth(request);
		
		if(auth == null)
		{
			return Boolean.valueOf(false);
		}
		
		if(auth.getStaffId().equals(-999L))
		{
			return true;
		}
		
		ParamVo vo = new ParamVo();
		vo.setMethod("selectCheckAuth");
		vo.put("staffId", auth.getStaffId());
		vo.put("url", request.getRequestURI());
		
		List<Object> list = (List<Object>)this.staffDao.selectList(vo);
		if(list != null && list.size() > 0)
			return Boolean.valueOf(true);
		
		return Boolean.valueOf(false);
	}
	
	public Object getStaffRoleList(ParamVo vo) {
		//get role list
		ParamVo vo1 = new ParamVo();
		vo1.setMethod("selectList");
		vo1.put("staffId", vo.get("authStaffId"));
		List roleList = this.roleDao.selectList(vo1);
		
		//get staff_role list
		vo.setMethod("selectList");
		List checkedList = this.staffRoleDao.selectList(vo);
		
		return ListToTree.trans(roleList, "roleId", "roleName", "noParentId", checkedList);
	}
	
	public Object getStaffRolePage(ParamVo vo) {
		vo.setMethod("selectPage");
		return this.staffRoleDao.selectPage(vo);
	}
	
	public Object getGrantQueryPage(ParamVo vo) {
		vo.setMethod("selectGrantQueryByOrgPage");
		return this.staffRoleDao.selectPage(vo);
	}
	
	//RULE
	public void txInserRule(ParamVo vo) throws TxException {
		if(!this.ruleDao.add(vo)) {
			throw new TxException("插入权限表出错！");
		}
	}
	public void txSaveRule(ParamVo vo) throws TxException {
		if(vo.getObject() == null) {
			throw new TxException("保存的权限信息为空！");
		}
		
		if(!this.ruleDao.save(vo)) {
			throw new TxException("保存权限信息出错！");
		}
	}
	public void txDelRule(ParamVo vo) throws TxException {
		if(vo.get("ruleId") == null) {
			throw new TxException("要删除的权限编号为空！");
		}
		
		if(!this.ruleDao.remove(vo)) {
			throw new TxException("删除权限信息出错！");
		}
	}
	public Object getRuleInfo(ParamVo vo) {
		return this.ruleDao.getInfoById(vo);
	}
	public Object getRulePage(ParamVo vo) {
		vo.setMethod("selectPage");
		return this.ruleDao.selectPage(vo);
	}
	
	//ROLE
	public void txInserRole(ParamVo vo) throws TxException {
		if(!this.roleDao.add(vo)) {
			throw new TxException("插入角色表出错！");
		}
	}
	public void txSaveRole(ParamVo vo) throws TxException {
		if(vo.getObject() == null) {
			throw new TxException("保存的角色信息为空！");
		}
		
		if(!this.roleDao.save(vo)) {
			throw new TxException("保存角色信息出错！");
		}
		
		//process role's rules
		Long roleId = ((Role)vo.getObject()).getRoleId();
		if(this.roleRuleDao.delete(this.roleRuleDao.getNameSpace() + ".deleteByRoleId", vo.getParam()) < 0) {
			throw new TxException("删除原有角色权限关系出错！");
		}
		
		if(StringUtils.isNotBlank((String)vo.get("rules"))) {
			String[] rules = ((String)vo.get("rules")).split(",");
			for(String i : rules) {
				RoleRule t = new RoleRule();
				t.setId(-1L);
				t.setRoleId(roleId);
				t.setRuleId(Long.valueOf(i));
				vo.setObject(t);
				if(!this.roleRuleDao.add(vo)) {
					throw new TxException("插入角色权限关系出错！");
				}
			}
		}
	}
	public void txDelRole(ParamVo vo) throws TxException {
		if(vo.get("roleId") == null) {
			throw new TxException("要删除的角色编号为空！");
		}
		
		if(!this.roleDao.remove(vo)) {
			throw new TxException("删除角色信息出错！");
		}
	}
	public Object getRoleInfo(ParamVo vo) {
		return this.roleDao.getInfoById(vo);
	}
	public Object getRolePage(ParamVo vo) {
		vo.setMethod("selectPage");
		return this.roleDao.selectPage(vo);
	}
	public Object getRoleRuleList(ParamVo vo) {
		//get rule list
		vo.setMethod("selectList");
		List ruleList = this.ruleDao.selectList(vo);
		
		//get role_rule list
		vo.setMethod("selectList");
		List checkedList = this.roleRuleDao.selectList(vo);
		
		return ListToTree.trans(ruleList, "ruleId", "ruleName", "parentId", checkedList);
	}
	
	//ORG
	public void txInserOrg(ParamVo vo) throws TxException {
		if(!this.orgDao.add(vo)) {
			throw new TxException("插入部门表出错！");
		}
	}
	public void txSaveOrg(ParamVo vo) throws TxException {
		
		if(StringUtils.isNotBlank((String)vo.get("dataList"))) {
			//AJAX传递LIST必须转为JSON串才可使用下列语句转换（先转出LIST，再转实体）
			List dataList = (List)new JsonBody(vo.getParam()).toJava("dataList", List.class);
			for(Object obj : dataList) {
				Map i = (Map)obj;
				JsonBody body = new JsonBody(i);
				body.remove("state");
				body.remove("_parentId");
				OrgExtend orgExtend = (OrgExtend)body.toJava("", OrgExtend.class, true);
				
				System.out.println("orgExtend = " + orgExtend);
				
				if(orgExtend == null) {
					throw new TxException("要修改的部门扩展信息有误!");
				}
				vo = new ParamVo();
				vo.setObjectClass(OrgExtend.class);
				vo.put("orgId", orgExtend.getOrgId());
				OrgExtend oldOrgExtend = (OrgExtend)this.orgExtendDao.getInfoById(vo);
				if(oldOrgExtend == null) {
					//throw new TxException("找不到部门扩展信息!");
					oldOrgExtend = new OrgExtend();
					oldOrgExtend.setOrgId(orgExtend.getOrgId());
				}
				oldOrgExtend.setOrgId(orgExtend.getOrgId());
				oldOrgExtend.setOrgMark(orgExtend.getOrgMark());
				//-- cashSaveRule,cashZeroDate chequeSaveRule, chequeZeroDate, arrivedSpeed
				oldOrgExtend.setCashSaveRule(orgExtend.getCashSaveRule() );
				oldOrgExtend.setCashZeroDate( orgExtend.getCashZeroDate());
				oldOrgExtend.setChequeSaveRule( orgExtend.getChequeSaveRule() );
				oldOrgExtend.setChequeZeroDate( orgExtend.getChequeZeroDate());
				oldOrgExtend.setArrivedSpeed( orgExtend.getArrivedSpeed() );
				//--
				oldOrgExtend.setArrivedSpeed(orgExtend.getArrivedSpeed());
				oldOrgExtend.setNoAlarm(orgExtend.getNoAlarm());
				vo.setObject(oldOrgExtend);
				if(!this.orgExtendDao.save(vo)) {
					throw new TxException("保存部门扩展信息出错！");
				}
			}
		}
	}
	public void txDelOrg(ParamVo vo) throws TxException {
		if(vo.get("orgId") == null) {
			throw new TxException("要删除的部门扩展编号为空！");
		}
		
		if(!this.orgExtendDao.remove(vo)) {
			throw new TxException("删除部门扩展信息出错！");
		}
	}
	public Object getOrgInfo(ParamVo vo) {
		return this.orgDao.getInfoById(vo);
	}
	public Object getOrgPage(ParamVo vo) {
		vo.setMethod("selectPage");
		return this.orgDao.selectPage(vo);
	}
	
	//FILE
	public void txInserFile(ParamVo vo) throws TxException {
		if(!this.fileDao.add(vo)) {
			throw new TxException("插入文件表出错！");
		}
	}
	public void txSaveFile(ParamVo vo) throws TxException {
		if(vo.getObject() == null) {
			throw new TxException("保存的文件信息为空！");
		}
		
		if(!this.fileDao.save(vo)) {
			throw new TxException("保存文件信息出错！");
		}
	}
	public void txDelFile(ParamVo vo) throws TxException {
		if(vo.get("id") == null) {
			throw new TxException("要删除的文件编号为空！");
		}
		
		if(!this.fileDao.remove(vo)) {
			throw new TxException("删除文件信息出错！");
		}
	}
	public Object getFileInfo(ParamVo vo) {
		return this.fileDao.getInfoById(vo);
	}
	public Object getFilePage(ParamVo vo) {
		vo.setMethod("selectPage");
		return this.fileDao.selectPage(vo);
	}
}
