package com.bonc.microapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonc.core.entity.EnumCatalog;
import com.bonc.core.entity.EnumCfg;
import com.bonc.db.IDao;
import com.bonc.db.TxException;
import com.bonc.tools.JsonBody;
import com.bonc.tools.ParamVo;

@Service
public class CfgService {

	@Autowired
	private IDao noticeDao;
	
	@Autowired
	private IDao messageHintDao;
	
	@Autowired
	private IDao messageTemplateDao;	
	
	@Autowired
	private IDao enumCatalogDao;
	
	@Autowired
	private IDao enumCfgDao;
	
	public void txDelEnumCfg(ParamVo vo)  throws TxException {	
		vo.setMethod("updateById");
		vo.put("state", 0);
		if(!this.enumCfgDao.edit(vo)) {
			throw new TxException("删除参数值出错！");
		}
	}
	
	public void txSaveEnumCfg(ParamVo vo)  throws TxException {	
		if(vo.getObject() == null) {
			throw new TxException("保存的参数类型数据为空！");
		}
		if(!this.enumCfgDao.save(vo)) {
			throw new TxException("保存参数值表出错！");
		}
	}
	
	public EnumCfg getEnumCfg(ParamVo vo) {
		vo.setObjectClass(EnumCfg.class);
		return (EnumCfg)this.enumCfgDao.getInfoById(vo);
	}
	
	public List<EnumCfg> getEnumCfgList(ParamVo vo) {
		vo.setObjectClass(EnumCfg.class);
		vo.setMethod("selectList");
		List<Object> list = this.enumCfgDao.selectList(vo);
		
		List<EnumCfg> enumList = new ArrayList<EnumCfg>();
		for(int i=0; i<list.size(); i++){
			enumList.add((EnumCfg)list.get(i));
		}
		
		return enumList;
	}
	
	public List<EnumCatalog> getEnumCatalogList(ParamVo vo) {
		vo.setObjectClass(EnumCatalog.class);
		vo.setMethod("selectList");
		List<Object> list = this.enumCatalogDao.selectList(vo);
		
		List<EnumCatalog> enumCatalogList = new ArrayList<EnumCatalog>();
		for(int i=0; i<list.size(); i++){
			enumCatalogList.add((EnumCatalog)list.get(i));
		}
		
		return enumCatalogList;
	}
	
	public void txDelEnumCatalog(ParamVo vo)  throws TxException {	
		vo.setMethod("updateById");
		vo.put("state", 0);
		if(!this.enumCatalogDao.edit(vo)) {
			throw new TxException("删除参数类型出错！");
		}
	}
	
	public void txSaveEnumCatalog(ParamVo vo)  throws TxException {	
		if(vo.getObject() == null) {
			throw new TxException("保存的参数类型数据为空！");
		}
		if(!this.enumCatalogDao.save(vo)) {
			throw new TxException("保存参数类型表出错！");
		}
	}
	
	public EnumCatalog getEnumCatalog(ParamVo vo) {
		vo.setObjectClass(EnumCatalog.class);
		return (EnumCatalog)this.enumCatalogDao.getInfoById(vo);
	}
	
	public Object getEnumCfgPage(ParamVo vo) {
		vo.setMethod("selectPage");
		return this.enumCfgDao.selectPage(vo);
	}
	
	public List<Object> getEnumList(ParamVo vo) {
		vo.setMethod("selectPage");
		return this.enumCfgDao.selectList(vo);
	}
	
	public Object getEnumCatalogPage(ParamVo vo) {
		vo.setMethod("selectPage");
		return this.enumCatalogDao.selectPage(vo);
	}
	
	//系统通知
	public void txAddNotice(ParamVo vo)  throws TxException {		
		if(!this.noticeDao.add(vo)) {
			throw new TxException("插入通知表出错！");
		}
	}
	
	public void txSaveNotice(ParamVo vo)  throws TxException {	
		if(vo.getObject() == null) {
			throw new TxException("保存的通知表数据为空！");
		}
		if(!this.noticeDao.save(vo)) {
			throw new TxException("保存通知表出错！");
		}
	}
	
	public void txDelNotice(ParamVo vo)  throws TxException {	
		this.noticeDao.update("updateStateById", vo.getParam());
	}
	
	public Object queryNoticePage(HttpServletRequest request, ParamVo vo)
	{
		vo.setMethod("selectNoticePage");
		return this.noticeDao.selectPage(vo);
	}
	
	public Object queryMessagePage(HttpServletRequest request, ParamVo vo){
		vo.setMethod("selectPage");
		return this.messageHintDao.selectPage(vo);
	}
	
	public Object queryMessageTemplate(HttpServletRequest request, ParamVo vo){
		return this.messageTemplateDao.getInfoById(vo);
	}
	
	public Object getNotice(ParamVo vo) {
		return this.noticeDao.getInfoById(vo);
	}
	


	public Object querySql(ParamVo vo){
		vo.setMethod("selectBySql");
		return this.messageTemplateDao.select(vo);
	}
	
}
