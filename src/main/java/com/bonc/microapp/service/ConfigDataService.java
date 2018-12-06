package com.bonc.microapp.service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonc.common.Auth;
import com.bonc.db.IDao;
import com.bonc.db.TxException;
import com.bonc.tools.ListToTree;
import com.bonc.tools.ParamVo;
import com.bonc.tools.StrUtil;

@Service
public class ConfigDataService {
	
	@Autowired
	private IDao enumCfgDao;
	
	@Autowired
	private IDao orgDao;
	
	@Autowired
	private IDao roleDao;
	
	@Autowired
	private IDao ruleDao;
	
	@Autowired
	private IDao staffDao;
	
	@Autowired
	private IDao staffRoleDao;

	@Autowired
	private IDao mapAreaInfoDao;
	
	@Autowired
	private IDao mapResourceDao;
	
	public Object getComboBoxData(String catalog, Object type) {
		ParamVo vo = new ParamVo();
		vo.setMethod("selectList");
		if("bank".equalsIgnoreCase(catalog)) {		
			return null;
		}else {
			return null;
		}
	}
	
	public List getComboTreeData(HttpServletRequest request, String catalog, Object type) {
		return getComboTreeData(request, catalog, type, null);
	}
	public List getComboTreeData(HttpServletRequest request, String catalog, Object type, Map<String, Object> param) {
		ParamVo vo = new ParamVo();
		vo.setMethod("selectList");
		if("org".equalsIgnoreCase(catalog)) {
			if(true){
				System.out.println("ORG太大，不支持全部查回");
				return null; //ORG太大，不支持全部查回
			}
			vo.put("orgId", Auth.getAuth(request).getOrgId());
			return ListToTree.trans(this.orgDao.selectList(vo), "orgId", "orgName", "parentId", null, false, null);
		} else if("orgAsync".equalsIgnoreCase(catalog)) {
			if(StringUtils.isBlank((String)type))
				type = 1L; //
			//easyui-combotree展开节点默认字段"id"
			if(param == null || (type == null && param.get("id") == null))
				return null;
			vo.setMethod("selectByParentId");
			vo.put("orgId", Auth.getAuth(request).getOptionOrgId());
			vo.put("rootId", type);
			vo.put("parentId", param.get("id"));
			
			return ListToTree.trans(this.orgDao.selectList(vo), "ORG_ID", "ORG_NAME", "PARENT_ID", "CHILD", false, null);
		}else if("orgAsyncAll".equalsIgnoreCase(catalog)) {
			if(StringUtils.isBlank((String)type))
				type = 1L; //
			//easyui-combotree展开节点默认字段"id"
			if(param == null || (type == null && param.get("id") == null))
				return null;
			vo.setMethod("selectByParentId");
			vo.put("orgId", 1L);//显示全部的机构
			vo.put("rootId", type);
			vo.put("parentId", param.get("id"));			
			return ListToTree.trans(this.orgDao.selectList(vo), "ORG_ID", "ORG_NAME", "PARENT_ID", "CHILD", false, null);
		} else if("orgAll".equalsIgnoreCase(catalog)) {
			if(StringUtils.isBlank((String)type))
				type = 1L; //
			//easyui-combotree展开节点默认字段"id"
			if(param == null || (type == null && param.get("id") == null))
				return null;
			vo.setMethod("selectAll");
			vo.put("orgId", 1L);//显示全部的机构
			vo.put("rootId", type);
			vo.put("parentId", param.get("id"));	
			
			List checkedList = null;
			String staffId = (String)param.get("staffId");
			String sType = (String)param.get("type");
			if( !StrUtil.isEmpty(staffId) && !StrUtil.isEmpty(sType)){
				ParamVo voOrgs = new ParamVo();
				voOrgs.setMethod("selectStaffOrgs");
				voOrgs.put("staffId",  staffId  );
				voOrgs.put("type",  sType  );
				checkedList = this.orgDao.selectList(voOrgs);
			}
			
			return ListToTree.trans(this.orgDao.selectList(vo), "ORG_ID", "ORG_NAME", "PARENT_ID", "CHILD", false, checkedList);
		}else if("enumCfg".equalsIgnoreCase(catalog)) {
			vo.setMethod("selectListByCatalogCode");
			vo.put("catalogCode", type);
			return ListToTree.trans(this.enumCfgDao.selectList(vo), "ENUM_KEY", "ENUM_VALUE", "PID");
		} else if("menu".equalsIgnoreCase(catalog)) {
			vo.setMethod("selectMenuList");
			if( type.equals(-999L)) {
				vo.setMethod("selectAdminMenuList");
			}
			vo.put("staffId", Auth.getAuth(request).getStaffId());
			return ListToTree.trans(this.staffDao.selectList(vo), "RULE_ID", "RULE_NAME", "PARENT_ID");
		} else if("role".equalsIgnoreCase(catalog)) {
			vo.setMethod("selectList");
			vo.put("staffId", Auth.getAuth(request).getStaffId());
			return ListToTree.trans(this.roleDao.selectList(vo), "roleId", "roleName", "null");
		} else if("rule".equalsIgnoreCase(catalog)) {
			vo.setMethod("selectList");
			vo.put("ruleId", type);
			return ListToTree.trans(this.ruleDao.selectList(vo), "ruleId", "ruleName", "parentId");
		} else if("staffRole".equalsIgnoreCase(catalog)) {
			vo.setMethod("selectList");
			vo.put("staffId", type);
			return ListToTree.trans(this.staffRoleDao.selectList(vo), "roleId", "roleName", "null");
		} else if("allCityCode".equalsIgnoreCase(catalog)) {
			vo.setMethod("selectAllCityCodeList");
			return ListToTree.trans(this.mapAreaInfoDao.selectList(vo), "areaCode", "areaName", "null");
		} else if("getAreaCode".equalsIgnoreCase(catalog)) {
			vo.setMethod("selectAllAreaCodeList");
			vo.put("cityCode", param.get("cityCode"));
			return ListToTree.trans(this.mapAreaInfoDao.selectList(vo), "areaCode", "areaName", "null");
		} else if("allFirstPoi".equalsIgnoreCase(catalog)) {
			vo.setMethod("selectAllFirstPoiList");
			return ListToTree.trans(this.mapResourceDao.selectList(vo), "firstType", "firstType", "null");
		} else if("getSecondPoi".equalsIgnoreCase(catalog)) {
			vo.setMethod("selectAllSecondPoiList");
			try {
				vo.put("firstPoi",new String(param.get("firstPoi").toString().getBytes("ISO-8859-1"),"UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return ListToTree.trans(this.mapResourceDao.selectList(vo), "secondType", "secondType", "null");
		} else {
			return null;
		}
	}
	
	public List getTreeGridData(HttpServletRequest request, String catalog, Object type, Map<String, Object> param) {
		ParamVo vo = new ParamVo();
		vo.setMethod("selectList");
		if("orgAsyncForTreeGrid".equalsIgnoreCase(catalog)) {
			if(StringUtils.isBlank((String)type))
				type = 1L; //
			//easyui-combotree展开节点默认字段"id"
			if(param == null || (type == null && param.get("id") == null))
				return null;
			vo.setMethod("selectByParentId");
			vo.put("orgId", Auth.getAuth(request).getOptionOrgId());
			vo.put("rootId", type);
			vo.put("parentId", param.get("id"));
			
			List list = ListToTree.trans(this.orgDao.selectList(vo), "ORG_ID", "ORG_NAME", "PARENT_ID", "CHILD", true, null);
			for(Object obj : list) {
				Map i = (Map)obj;
				i.putAll((Map)i.get("data"));
			}
			return list;
		} else {
			return null;
		}
	}
	
	

}
