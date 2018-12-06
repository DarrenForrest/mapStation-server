package com.bonc.db;

import java.util.List;
import java.util.Map;

import com.bonc.tools.Page;
import com.bonc.tools.ParamVo;

public interface IDao {
	
	public final String WHERE_KEYSTRING    = "QHERE";
	public final String ORDER_BY_KEYSTRING = "Qrder-By";
	public final String GROUP_BY_KEYSTRING = "Qroup-By";
	
	public String getNameSpace();
	
	/**
	 * 根据ID获取单条记录
	 */
	public Object getInfoById(ParamVo vo);
	
	/**
	 * 增加新记录
	 */
	public boolean add(ParamVo vo);
	
	/**
	 * 编辑记录
	 */
	public boolean edit(ParamVo vo);
	
	/**
	 * 保存记录（没有则增加，有则修改）
	 */
	public boolean save(ParamVo vo);
	
	/**
	 * 删除记录
	 */
	public boolean remove(ParamVo vo);
	
	/**
	 * 获取历史ID
	 */
	public Long getHisId(ParamVo vo);
	
	public Object execute(String method, ParamVo param);
	public int update(String namespace, Object param);
	public int insert(String namespace, Object param);
	public int delete(String namespace, Object param);
	public int insertBatch(String namespace, Object param);
	public Object select(ParamVo param);
	public Object select(String namespace, Object param);
	public Object select(String namespace, Object param, Class<? extends Object> cls);
	public List<Object> selectList(ParamVo param);
	public List<Object> selectList(String namespace, Object param);
	public List<Object> selectList(String namespace, Object param, Class<? extends Object> cls);
	public Page selectPage(ParamVo param);
	public Page selectPage(String namespace, Map<String, Object> param);
	public Page selectPage(String namespace, Map<String, Object> param, Class<? extends Object> cls);
}
