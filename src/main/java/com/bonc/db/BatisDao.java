package com.bonc.db;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.bonc.tools.Page;
import com.bonc.tools.ParamVo;

public abstract class BatisDao implements IDao {
	
	//for mysql count(*)
	private interface InnerMapper {
		@Select("select FOUND_ROWS();")
		long selectMysqlFoundRows();
	}
	
	@Autowired
	private SqlSessionFactory sqlSessionFactory;
	
	@Autowired
	private DataSource dataSource;
	
	private SqlSession session;
	private String databaseType;
	private String pageColumns;
	private String nameSpace;
	private boolean writeHis;
	
	public SqlSessionFactory getSqlSessionFactory() {
		return sqlSessionFactory;
	}

	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}
	
	public String getNameSpace() {
		return nameSpace;
	}
	
	public void setNameSpace(String nameSpace) {
		this.nameSpace = nameSpace;
	}
	
	public String getDatabaseType() {
		return this.databaseType;
	}
	
	public void setDatabaseType(String databaseType) {
		if(databaseType == null) {
			this.databaseType = "unknown";
			pageColumns = "";
		}
		else if(databaseType.contains("oracle")) {
			this.databaseType = "oracle";
			pageColumns = " count(*) over() as totalCnt, ";
		} else if(databaseType.contains("mysql")) {
			this.databaseType = "mysql";
			pageColumns = " SQL_CALC_FOUND_ROWS ";
			
			sqlSessionFactory.getConfiguration().addMapper(InnerMapper.class);
		}
	}
	
	public boolean getWriteHis() {
		return this.writeHis;
	}
	
	public void setWriteHis(boolean writeHis) {
		this.writeHis = writeHis;
	}

	public BatisDao() {
		databaseType = null;
		pageColumns = "";
	}
	
	@Override
	public Object getInfoById(ParamVo vo) {
		if(vo == null) {
			return null;
		}
		String nameSpace = vo.getNameSpace() == null ? this.getNameSpace() : vo.getNameSpace();
		if(nameSpace != null) {
			return this.select(nameSpace + ".selectById", vo.getObject() == null ? vo.getParam() : vo.getObject(), vo.getObjectClass());
		}
		return null;
	}
	
	@Override
	public Long getHisId(ParamVo vo) {
		if(vo == null) {
			return null;
		}
		String nameSpace = vo.getNameSpace() == null ? this.getNameSpace() : vo.getNameSpace();
		if(nameSpace != null) {
			return (Long)this.select(nameSpace + ".selectHisId", vo.getObject() == null ? vo.getParam() : vo.getObject(), vo.getObjectClass());
		}
		return null;
	}
	
	@Override
	public boolean add(ParamVo vo) {
		if(vo == null) {
			return false;
		}
		String nameSpace = vo.getNameSpace() == null ? this.getNameSpace() : vo.getNameSpace();
		Object obj = vo.getObject() == null ? vo.getParam() : vo.getObject();
		if(obj != null && nameSpace != null) {
			int res = this.insert(nameSpace + ".insertById", obj);
			if(res > 0)
			{
				if(writeHis) {
					if(this.insert(nameSpace + ".insertHis", obj) <= 0) {
						System.out.println("write to his is error: " + nameSpace);
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean edit(ParamVo vo) {
		if(vo == null) {
			return false;
		}
		String nameSpace = vo.getNameSpace() == null ? this.getNameSpace() : vo.getNameSpace();
		Object obj = vo.getObject() == null ? vo.getParam() : vo.getObject();
		if(obj != null && nameSpace != null) {
			int res = this.update(nameSpace + ".updateById", obj);
			if(res > 0)
			{
				if(writeHis) {
					if(this.insert(nameSpace + ".insertHis", obj) <= 0) {
						System.out.println("write to his is error: " + nameSpace);
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean save(ParamVo vo) {
		if(vo == null) {
			return false;
		}
		String nameSpace = vo.getNameSpace() == null ? this.getNameSpace() : vo.getNameSpace();
		Object obj = vo.getObject() == null ? vo.getParam() : vo.getObject();
		if(obj != null && nameSpace != null) {
			int res = this.update(nameSpace + ".updateById", obj);
			if(res > 0) {
				if(writeHis) {
					if(this.insert(nameSpace + ".insertHis", obj) <= 0) {
						System.out.println("write to his is error: " + nameSpace);
						return false;
					}
				}
				return true;
			} else if(res == 0){
				res = this.insert(nameSpace + ".insertById", obj);
				if(res > 0) {
					if(writeHis) {
						if(this.insert(nameSpace + ".insertHis", obj) <= 0) {
							System.out.println("write to his is error: " + nameSpace);
							return false;
						}
					}
					return true;
				}
			} else {
				return false;
			}
		}
		return false;
	}
	
	@Override
	public boolean remove(ParamVo vo) {
		if(vo == null) {
			return false;
		}
		String nameSpace = vo.getNameSpace() == null ? this.getNameSpace() : vo.getNameSpace();
		Object obj = vo.getObject() == null ? vo.getParam() : vo.getObject();
		if(obj != null && nameSpace != null) {
			if(writeHis) {
				if(this.insert(nameSpace + ".insertHis", obj) <= 0) {
					System.out.println("write to his is error: " + nameSpace);
					return false;
				}
			}
			int res = this.delete(nameSpace + ".deleteById", obj);
			if(res > 0)
				return true;
		}
		return false;
	}
	
	@Override
	public Object execute(String method, ParamVo param) {
		
		try {
			Method instance = this.getClass().getMethod(method, ParamVo.class);
			return instance.invoke(this, param);
		} catch (Exception e) {
			System.out.println(this.getClass().getName() + ".execute: Error " + e);
		}
		return null;
	}
	
	@Override
	public int update(String namespace, Object param){
		//if(!(param instanceof Map)) {
		//	return -1;
		//}
		int n = 0;
		try {
			SqlSession session = this.getSqlSession();
			n = session.update(namespace, param);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			this.closeSqlSession(session);
		}
		
		return n;
	}
	
	@Override
	public int insert(String namespace, Object param) {
		int n = 0;
		try {
			SqlSession session = this.getSqlSession();
			n = session.insert(namespace, param);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			this.closeSqlSession(session);
		}
		return n;
	}
	
	@Override
	public int delete(String namespace, Object param) {
		int n = 0;
		try {
			SqlSession session = this.getSqlSession();
			n = session.delete(namespace, param);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			this.closeSqlSession(session);
		}
		return n;
	}
	
	@Override
	public int insertBatch(String namespace, Object param) {
		int n = 0;
		try {
			SqlSession session = this.getSqlSession();
			n = session.insert(namespace, param);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			this.closeSqlSession(session);
		}
		
		return n;
	}
	
	@Override
	public Object select(ParamVo param) {
		if(param.getMethod() == null) {
			//return null;
			param.setMethod("Error_SqlIsBlank");
		}
		
		return select((param.getNameSpace() == null ? this.getNameSpace() : param.getNameSpace()) + "." + param.getMethod(), param.getParam(), param.getObjectClass());
	}
	
	@Override
	public Object select(String namespace, Object param) {
		return select(namespace, param, null);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Object select(String namespace, Object param, Class<? extends Object> cls) {

		try {
			SqlSession session = this.getSqlSession();
			Object obj = session.selectOne(namespace, param);
			if(obj instanceof Map && cls != null) {
				return Page.transMap2Bean((Map<String, Object>)obj, cls);
			} else {
				return obj;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.closeSqlSession(session);
		}

		return null;
	}
	
	/**
	 * selectList
	 * @return null 发生异常     List[] 查询结果记录数为0条
	 */
	@Override
	public List<Object> selectList(ParamVo param) {
		if(param.getMethod() == null) {
			//return null;
			param.setMethod("Error_SqlIsBlank");
		}
		
		return selectList((param.getNameSpace() == null ? this.getNameSpace() : param.getNameSpace()) + "." + param.getMethod(), param.getParam(), param.getObjectClass());
	}
	
	@Override
	public List<Object> selectList(String namespace, Object param) {
		return selectList(namespace, param, null);
	}
	
	@Override
	public List<Object> selectList(String namespace, Object param, Class<? extends Object> cls) {
		
		try {
			SqlSession session = this.getSqlSession();
			List<Object> list = session.selectList(namespace, param);
			return Page.toBeanList(list, cls);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.closeSqlSession(session);
		}
		
		return null;
	}
	
	@Override
	public Page selectPage(ParamVo param) {
		if(param.getMethod() == null)
			return null;
		
		if(param.getOrderby() != null)
			param.put(ORDER_BY_KEYSTRING, param.getOrderby());
		if(param.getGroupby() != null)
			param.put(GROUP_BY_KEYSTRING, param.getGroupby());
		
		return selectPage((param.getNameSpace() == null ? this.getNameSpace() : param.getNameSpace()) + "." + param.getMethod(), param.getParam(), param.getObjectClass());
	}
	
	@Override
	public Page selectPage(String namespace, Map<String, Object> param) {
		return selectPage(namespace, param, null);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Page selectPage(String namespace, Map<String, Object> param, Class<? extends Object> cls) {
		
		//param中应该带入"pageSize"和"currentPage"这两个参数，否则取默认值(10, 1)
		Page page = new Page(param);
		
		RowBounds rowBounds = new RowBounds((page.getCurrentPage() - 1) * page.getPageSize(), page.getCurrentPage() * page.getPageSize());
		
		//按分页查询时应该在原语句中带上TotalCnt字段（移入interceptor处理）
		//Map<String, Object> map = (Map<String, Object>)param;
		//map.put("pageColumns", this.pageColumns);
		
		try {
			SqlSession session = this.getSqlSession();
			List<Object> list = session.selectList(namespace, param, rowBounds);
			
			//处理总记录数（为分页计算）
			long totalCnt = 0;
			if("mysql".equals(this.databaseType)) {
				InnerMapper mapper = session.getMapper(InnerMapper.class);
				totalCnt = mapper.selectMysqlFoundRows();
			} else if("oracle".equals(this.databaseType)) {
				if(list != null && list.size() > 0) {
					if(!(list.get(0) instanceof Map)) {
						throw new Exception("check resultMap type should be[type=\"java.util.Map\"]");
					}
					Map<String, Object> firstMap = (Map<String, Object>)list.get(0);
					Object cnt = firstMap.get("autoTotalCnt") != null ? firstMap.get("autoTotalCnt") : firstMap.get("AUTO_TOTAL_CNT");
					if(cnt instanceof BigDecimal) {
						//根据测试不管ResultMap中配置类型是什么，此处都返回BigDecimal。
						totalCnt = ((BigDecimal)cnt).longValue();
					} else {
						//如果为空或别的数据类型是否应该抛出异常
						throw new Exception("selectPage need pageColumns as [" + this.pageColumns + "]");
					}
				}
			}
			page.setDataList(list, cls, totalCnt);
			return page;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.closeSqlSession(session);
		}
		
		return null;
	}
	
	private SqlSession getSqlSession(){
		if(this.getDatabaseType() == null) {
			try {
				Method instance = dataSource.getClass().getMethod("getDriverClassName");
				this.setDatabaseType((String)instance.invoke(dataSource));
			} catch(Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return this.sqlSessionFactory.openSession();
	}
	
	private void closeSqlSession(SqlSession session){
		if (session != null) {
			session.close();
		}
		session = null;
	}
	
}
