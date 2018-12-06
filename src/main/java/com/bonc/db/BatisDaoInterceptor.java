package com.bonc.db;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

@Intercepts({@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class }) })
public class BatisDaoInterceptor implements Interceptor {
	
	private Properties properties;

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		
		Object[] queryArgs = invocation.getArgs();
		MappedStatement ms = (MappedStatement)queryArgs[0];
		Object parameter = queryArgs[1];
		RowBounds rowBounds = (RowBounds)queryArgs[2];
		int offset = rowBounds.getOffset();
		int limit = rowBounds.getLimit();
		
		if((offset != 0) || ((limit > 0) && (limit != Integer.MAX_VALUE))) {
			BoundSql boundSql = ms.getBoundSql(parameter);
			String sql = boundSql.getSql().trim();
			String where = "";
			if ((parameter != null) && ((parameter instanceof HashMap))) {
				where = (String)((Map)parameter).get(IDao.WHERE_KEYSTRING);
				where = (where == null ? "" : " where " + where);
			}
			String orderby = "";
			if ((parameter != null) && ((parameter instanceof HashMap))) {
				orderby = (String)((Map)parameter).get(IDao.ORDER_BY_KEYSTRING);
			}
			String groupby[] = null;
			if ((parameter != null) && ((parameter instanceof HashMap))) {
				groupby = (String[])((Map)parameter).get(IDao.GROUP_BY_KEYSTRING);
			}
			
			sql = getLimitString(sql, offset, limit, where, orderby, groupby);
			
			//System.out.println("selectPage rebuild sql: " + sql);
			
			BoundSql newBoundSql = new BoundSql(ms.getConfiguration(), sql, boundSql.getParameterMappings(), 
			         boundSql.getParameterObject());
			queryArgs[0] = this.copyFromMappedStatement(ms, new BoundSqlSqlSource(newBoundSql));
			queryArgs[2] = new RowBounds(0, Integer.MAX_VALUE); //必须这样重新赋值，理由还未分析
		}
		//System.out.println("[Debug Info]: " + ((MappedStatement)queryArgs[0]).getBoundSql(invocation.getArgs()[1]).getSql().trim());
		//System.out.println("[Debug Info]: " + ((MappedStatement)queryArgs[0]).getBoundSql(invocation.getArgs()[1]).getParameterObject());
		return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
		this.properties = properties;
	}
	
	//以下代码为分页处理（只实现ORACLE，并且未考虑通用性，有时间再补充其他类型数据库）
	public String getLimitString(String sql, int startRow, int endRow, String where, String orderby, String[] groupby) {
		if (sql == null)
			return "";
		sql = trim(sql);
		sql = decorateSql(sql, where, orderby, groupby);
		StringBuffer sb = new StringBuffer(sql.length() + 20);
		sb.append("SELECT * FROM (SELECT A.*, ROWNUM RN FROM (");
		sb.append(sql);
		sb.append(" )A WHERE ROWNUM <= ");
		sb.append(endRow);
		sb.append(")WHERE RN > ");
		if (startRow >= 0) {
			sb.append(startRow);
		}
		return sb.toString();
	}

	private String decorateSql(String sql, String where, String orderby, String groupby[]) {
		if (StringUtils.isEmpty(sql)) {
			return sql;
		}
		
		StringBuffer strBuf = new StringBuffer();
		String oracleCountStr = " count(*) over() AUTO_TOTAL_CNT, ";
		strBuf.append("select ").append(oracleCountStr);
		if (groupby == null) {
			strBuf.append(" Expr1.* from (").append(sql).append(") Expr1 ").append(where);
		} else {
			if(StringUtils.isBlank(groupby[1]))
				strBuf.append(groupby[0]).append(" from (").append(sql).append(") Expr1 ").append(where);
			else
				strBuf.append(groupby[0]).append(", ").append(groupby[1]).append(" from (").append(sql).append(") Expr1 ").append(where).append(" group by ").append(groupby[1]);
		}
		
		if (StringUtils.isNotBlank(orderby)) {
			strBuf.append(" order by ").append(orderby);
		}
		return strBuf.toString();
	}

	private String trim(String sql) {
		if (StringUtils.isEmpty(sql)) {
			return sql;
		}
		sql = sql.trim();
		if (sql.endsWith(";")) {
			sql = sql.substring(0, sql.length() - ";".length());
		}
		return sql;
	}
	
	private MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
		MappedStatement.Builder builder = new MappedStatement.Builder(
				ms.getConfiguration(), ms.getId(), newSqlSource,
				ms.getSqlCommandType());
		builder.resource(ms.getResource());
		builder.fetchSize(ms.getFetchSize());
		builder.statementType(ms.getStatementType());
		builder.keyGenerator(ms.getKeyGenerator());

		builder.timeout(ms.getTimeout());

		builder.parameterMap(ms.getParameterMap());

		builder.resultMaps(ms.getResultMaps());
		builder.resultSetType(ms.getResultSetType());

		builder.cache(ms.getCache());
		builder.flushCacheRequired(ms.isFlushCacheRequired());
		builder.useCache(ms.isUseCache());

		return builder.build();
	}
	
	public static class BoundSqlSqlSource implements SqlSource {
		BoundSql boundSql;

		public BoundSqlSqlSource(BoundSql boundSql) {
			this.boundSql = boundSql;
		}

		public BoundSql getBoundSql(Object parameterObject) {
			return this.boundSql;
		}
	}
}
