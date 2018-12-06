package com.bonc.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JdbcPreviewTool {
	//HANA
	//private static final String DRIVER = "com.sap.db.jdbc.Driver";  //jdbc 4.0
	//private static final String URL = "jdbc:sap://192.168.0.199:30015?reconnect=true";
	//private static final String USER = "";
	//private static final String PASSWORD = "";
	
	//Oracle
	private static final String DRIVER = "oracle.jdbc.OracleDriver";  //jdbc 4.0
	private static final String URL = "jdbc:oracle:thin:@202.99.219.145:1521:orcl";
	private static final String USER = "remit";
	private static final String PASSWORD = "remit123";
	
	//MySql
//	private static final String DRIVER = "com.mysql.jdbc.Driver";  //jdbc 4.0
//	private static final String URL = "jdbc:mysql://202.99.219.145/test?characterEncoding=utf-8";
//	private static final String USER = "test";
//	private static final String PASSWORD = "test123";

	
	public JdbcPreviewTool() {
		
	}
	
	public static void main(String[] args) {
		JdbcPreviewTool demo = new JdbcPreviewTool();
		
		String sql = "select * from TB_RULE where 1=1";
		Map map = null;
		try {
			//demo.select(DRIVER,URL, USER, PASSWORD, sql);
			//demo.descTab(DRIVER,URL, USER, PASSWORD, sql);
			//String ret = demo.descTab(DRIVER,URL, USER, PASSWORD, sql);
			//System.out.println("\nret = \n" + ret);
			
			map = demo.selectList(DRIVER,URL, USER, PASSWORD, sql);
			
			System.out.println("list = " + map.toString());
		} catch (Exception e) {
			  e.printStackTrace();
		}
	}
	
	
	
	public Map selectList(String driver, String url, String user,String password, String sql)throws Exception{
		Map  map = null;
		try{
			Connection con = this.getConnection(driver, url, user, password);
			PreparedStatement pstmt = con.prepareStatement( sql );
			ResultSet rs = pstmt.executeQuery();
			map = this.processResult2List(rs);
			this.closeConnection(con, pstmt);
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
		return map;
	}
	
	public String descTab(String driver, String url, String user,String password, String sql) throws Exception{
		String ret = "";
		try{
			Connection con = this.getConnection(driver, url, user, password);
			PreparedStatement pstmt = con.prepareStatement( sql );
			ResultSet rs = pstmt.executeQuery();
			ret = this.processDesc(rs);
			this.closeConnection(con, pstmt);
		}catch(Exception e){
			e.printStackTrace();
			ret = e.toString();
		}

		
		return ret;
	}
	
	public String select(String driver, String url, String user,String password, String sql) throws Exception {
		String ret ="";
		try{
			Connection con = this.getConnection(driver, url, user, password);
			PreparedStatement pstmt = con.prepareStatement( sql );
			ResultSet rs = pstmt.executeQuery();		
			ret = this.processResult(rs);
			this.closeConnection(con, pstmt);
		}catch(Exception e){
			e.printStackTrace();
			
			ret = e.toString();
		}

		return ret;
	}
	
	
	private String processDesc(ResultSet rs) throws Exception {
		StringBuffer sb = new StringBuffer();
		if (rs.next()) {
			ResultSetMetaData rsmd = rs.getMetaData();
			int colNum = rsmd.getColumnCount();
			
			
			for(int i=1; i<= colNum; i++){
				String columnName = rsmd.getColumnName(i);
				//获取指定列的SQL类型对应于java.sql.Types类的字段
				int columnType = rsmd.getColumnType(i);
				//获取指定列的SQL类型
				String columnTypeName = rsmd.getColumnTypeName(i);
				//获取指定列SQL类型对应于Java的类型
				String className= rsmd.getColumnClassName(i);
				//获取指定列所在的表的名称
				String tableName = rsmd.getTableName(i);
				
				String metaData = "columnName = %20s\t" +
						   "columnTypeName = %10s\t" +
						   "columnType = %5s\t" +
						   "className = %20s\t" +
						   "tableName = %20s";
				metaData = metaData.format(metaData, columnName, columnTypeName,columnType ,className , tableName);
				
				sb.append(metaData+"\n");
				System.out.println(metaData );
			}
			System.out.println("\n");
			sb.append("\n");
			
			
			for (int i = 1; i <= colNum; i++){
				if (i == 1) {
					System.out.print(rsmd.getColumnName(i));
					sb.append(rsmd.getColumnName(i));
				} else {
					System.out.print("\t" + rsmd.getColumnName(i));
					sb.append("\t" + rsmd.getColumnName(i));
				}
		   }
			
		   System.out.print("\n");
		   System.out.println("———————–");
		   
		   sb.append("\n");
		   sb.append("———————–\n");
		   
		   int iCnt=0;
		   do {
			   for (int i = 1; i <= colNum; i++) {
				   if (i == 1) {
					   System.out.print(rs.getString(i));
					   sb.append(rs.getString(i));
				   } else {
					   System.out.print("\t" + (rs.getString(i) == null ? "null" : rs.getString(i).trim()));
					   sb.append("\t" + (rs.getString(i) == null ? "null" : rs.getString(i).trim()));
				   }
				}
		    	System.out.print("\n");
		    	sb.append("\n");
		    	iCnt = iCnt + 1;
		    	
		    	//此处最多显示5行
		    	if(iCnt > 5){
		    		break;
		    	}
		   } while (rs.next());
		   
		  } else {
			  System.out.println("query not result.");
			  sb.append("query not result.\n");
		  }
		
		return sb.toString();
	}

	private String processResult(ResultSet rs) throws Exception {
		StringBuffer sb= new StringBuffer();
		if (rs.next()) {
			ResultSetMetaData rsmd = rs.getMetaData();
			int colNum = rsmd.getColumnCount();		
			
			for (int i = 1; i <= colNum; i++){
				if (i == 1) {
					System.out.print(rsmd.getColumnName(i));
					sb.append(rsmd.getColumnName(i));
				} else {
					System.out.print("\t" + rsmd.getColumnName(i));
					sb.append("\t" + rsmd.getColumnName(i));
				}
		   }
			
		   System.out.print("\n");
		   System.out.println("———————–");
		   sb.append("\n———————–\n");
		   
		   do {
		    for (int i = 1; i <= colNum; i++) {
		     if (i == 1) {
		    	 System.out.print(rs.getString(i));
		    	 sb.append(rs.getString(i));
		     } else {
		    	 System.out.print("\t" + (rs.getString(i) == null ? "null" : rs.getString(i).trim()));
		    	 sb.append("\t" + (rs.getString(i) == null ? "null" : rs.getString(i).trim()));
		     }
		    }
		    System.out.print("\n");
		    sb.append("\n");
		   } while (rs.next());
		  } else {
			  System.out.println("query not result.");
			  sb.append("query not result.\n");
		  }
		return sb.toString();
	}
	
	private Map processResult2List(ResultSet rs) throws Exception {
		List<LinkedHashMap> dataList = new ArrayList<LinkedHashMap>();
		List<String> titleList = new ArrayList<String>();
		
		
		ResultSetMetaData rsmd = rs.getMetaData();
		int colNum = rsmd.getColumnCount();

		for(int i=1; i<= colNum; i++){
			titleList.add(rsmd.getColumnName(i));
		}
				
		//打印字段列头
		for (int i = 1; i <= colNum; i++){
			if (i == 1) {
				//System.out.print(rsmd.getColumnName(i));
			} else {
				//System.out.print("\t" + rsmd.getColumnName(i));
			}
		}
		//System.out.print("\n");
		//System.out.println("———————–");
		
		if (rs.next()) {
		   do {
			   LinkedHashMap map = new LinkedHashMap();
			   for (int i = 1; i <= colNum; i++) {
				   map.put(titleList.get(i-1), rs.getString(i) == null ? "" : rs.getString(i).trim() );
				   
				   if (i == 1) {
					   //System.out.print(rs.getString(i));
					} else {
						//System.out.print("\t" + (rs.getString(i) == null ? "null" : rs.getString(i).trim()));
					}
			   }
			   dataList.add(map);
		       //System.out.print("\n");
		   } while (rs.next());
		  } else {
			 // System.out.println("query not result.");
		  }
		
		Map<String, List> map = new HashMap<String, List>();
		
		//System.out.println("titleList=" + titleList.toString());
		
		map.put("titleList", titleList);
		map.put("dataList", dataList);
		return map;
	}
		
	private Connection getConnection(String driver, String url, String user, String password) throws Exception {
		Class.forName(driver);
		return DriverManager.getConnection(url, user, password);
	}
		
	private void closeConnection(Connection con, Statement stmt)throws Exception {
	  if (stmt != null) {
	   stmt.close();
	  }
	  if (con != null) {
	   con.close();
	  }
	}

}
