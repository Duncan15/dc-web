package util;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

/**
 * 数据库操作类
 */
public class DBUtil
{
	private static String dbDriver = "com.mysql.cj.jdbc.Driver";
//	private static String dbUrl = "jdbc:mysql://127.0.0.1:3306/webcrawler?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT";
//	private static String dbUser = "root";
//	private static String dbPass = "2333";

	private static String dbUrl;
	private static String dbUser;
	private static String dbPass;
	public static boolean config(String mysqlURL, String mysqlUserName, String mysqlPassword) {
		try {
			Class.forName(dbDriver);
		}catch (ClassNotFoundException e){
			System.err.println(e);
			return false;
		}

		dbUrl=mysqlURL;
		dbUser=mysqlUserName;
		dbPass=mysqlPassword;
		System.out.println("URL:"+dbUrl);
		System.out.println("UserName:"+dbUser);
		System.out.println("Password:"+dbPass);
		try{
			Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
			conn.close();
		}catch (SQLException e){
			System.err.println(e);
			dbUrl=null;
			dbPass=null;
			dbUser=null;
			return false;
		}
		return true;
	}
	public static Connection getConn()
	{
		Connection conn;
		if(dbUrl==null||dbUser==null||dbPass==null){
			Properties prop=new Properties();
			try {
				prop.load(DBUtil.class.getResourceAsStream("/webapp/WEB-INF/application.properties"));
			}catch (IOException e){
				System.err.println(e);
				return null;
			}
			dbUrl=prop.getProperty("mysqlURL");
			dbUser=prop.getProperty("mysqlUserName");
			dbPass=prop.getProperty("mysqlPassword");
		}
		try{
			conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
		}catch (SQLException e){
			System.err.println(e);
			return null;
		}
		return conn;
	}


	public static void release(Connection conn, Statement st, ResultSet rs)
	{
		if (rs != null)
		{
			try {
				rs.close();
			}catch (SQLException e){
				e.printStackTrace();
			}

		}
		if (st != null)
		{
			try {
				st.close();
			}catch (SQLException e){
				e.printStackTrace();
			}
		}
		if (conn != null)
		{
			try {
				conn.close();
			}catch (SQLException e){
				e.printStackTrace();
			}
		}
	}
	/*
	 * @param tableName,the target parameters,the parameter values
	 * @return flag(true->success)
	 */
	public static boolean insert(String table, String[] params, String[] params_value)
	{
		boolean flag = true;
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		String sql = "insert into " + table + " (";
		for (int i = 0; i < params.length - 1; i++)
		{
			sql += params[i] + ",";
		}
		sql += params[params.length - 1] + ") values (";
		for (int j = 0; j < params_value.length - 1; j++)
		{
			sql += "'" + params_value[j] + "' ,";
		}
		sql += "'" + params_value[params_value.length - 1] + "');";
		try
		{
			conn = getConn();

			try
			{
				st = (PreparedStatement) conn.prepareStatement(sql);
			} catch (SQLException e)
			{
				flag = false;
				e.printStackTrace();
			}
			try
			{
				st.executeUpdate();
			} catch (SQLException e)
			{
				flag = false;
				e.printStackTrace();
			}
		} finally {
			release(conn, st, rs);
		}
		return flag;
	}

	/*
	 * @param tableName,the target parameters,the parameter values,webId
	 */
	public static boolean update(String table, String[] params, String[] params_value, int webId)
	{
		boolean flag = true;
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		String sql = "update " + table + " SET ";
		for (int i = 0; i < params.length - 1; i++)
		{
			sql += params[i] + "=\'" + params_value[i] + "\',";
		}
		sql += params[params.length - 1] + "=\'" + params_value[params_value.length - 1] + "\' where webId=\'" + webId + "\';";
		try
		{
			conn = getConn();
			try
			{
				st = conn.prepareStatement(sql);
			} catch (SQLException e)
			{
				e.printStackTrace();
				flag = false;
			}
			try
			{
				st.executeUpdate();
			} catch (SQLException e)
			{
				e.printStackTrace();
				flag = false;
			}

		}finally {
			release(conn, st, rs);
		}
		return flag;
	}

	/*
	 * not use
	 */
	public static boolean update(String table, String params[], String params_value[],String cond_par[],String cond_par_val[]) {
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		boolean flag = true;
		String sql = "update " + table + " SET ";
		for (int i = 0; i < params.length - 1; i++) {
			sql += params[i] + "=" +"'"+ params_value[i] +"'"+ ",";
		}
		sql += params[params.length - 1] + "=" + "'"+params_value[params_value.length - 1] +"'"+ " where ";
		for (int j = 0; j < cond_par.length - 1; j++) {
			sql += cond_par[j] + "=" + "'"+cond_par_val[j] +"'"+ " and ";
		}
		sql +=cond_par[cond_par.length-1]+"="+"'"+cond_par_val[cond_par.length-1]+"'"+";";
		//System.out.println(sql);
		try {
			conn = getConn();

			try {
				st = (PreparedStatement) conn.prepareStatement(sql);
			} catch (SQLException e) {
				flag = false;
				e.printStackTrace();
			}
			try {
				st.executeUpdate();
			} catch (SQLException e) {
				flag = false;
				e.printStackTrace();
			}
		}finally {
			release(conn, st, rs);
		}
		return flag;
	}

	/*
	 * select {params} from {table} where {cond_params}={cond_par_val}
	 */
	public static String[][] select(String table, String[] params, String[] cond_params,String[] cond_par_val)
	{
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		String[][] result = null;
		String sql = "select ";
		for (int i = 0; i < params.length - 1; i++)
		{
			sql += params[i] + ",";
		}
		sql += params[params.length - 1] + " from " + table + " where ";
		for(int j=0;j<cond_params.length-1;j++)
		{
			sql+=cond_params[j]+"=\'"+cond_par_val[j]+"\' and ";
		}
		sql+=cond_params[cond_params.length-1]+"=\'"+cond_par_val[cond_params.length-1]+"\'";
		try
		{
			conn = getConn();
			try
			{
				st = (PreparedStatement) conn.prepareStatement(sql);
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
			try
			{
				rs = st.executeQuery();
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
			ResultSetMetaData mm = null;
			try
			{
				mm = rs.getMetaData();
			} catch (SQLException e1)
			{
				e1.printStackTrace();
			}
			int columns = 0;
			try
			{
				columns = mm.getColumnCount();
			} catch (SQLException e1)
			{
				e1.printStackTrace();
			}
			int rowCount = 0;
			try {
				rs.last();
				rowCount = rs.getRow();
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			try
			{
				rs.beforeFirst();
			} catch (SQLException e1)
			{
				e1.printStackTrace();
			}
			int k = 0;
			try
			{
				result = new String[rowCount][columns];
				while (rs.next())
				{
					for (int i = 1; i <= columns; i++)
					{
						try
						{
							result[k][i - 1] = rs.getString(i);
						} catch (SQLException e)
						{
							e.printStackTrace();
						}
					}
					k++;
				}
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		} finally {
			release(conn, st, rs);
		}
		return result;

	}

	/*
	 * select {params} from table
	 */
	public static String[][] select(String table, String[] params)
	{
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		String[][] result = null;
		String sql = "select ";
		for (int i = 0; i < params.length - 1; i++)
		{
			sql += params[i] + ",";
		}
		sql += params[params.length - 1] + " from " + table ;
		try
		{
			conn = getConn();
			try
			{
				st = (PreparedStatement) conn.prepareStatement(sql);
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
			try
			{
				rs = st.executeQuery();
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
			ResultSetMetaData mm = null;
			try
			{
				mm = rs.getMetaData();
			} catch (SQLException e1)
			{
				e1.printStackTrace();
			}
			int columns = 0;
			try
			{
				columns = mm.getColumnCount();
			} catch (SQLException e1)
			{
				e1.printStackTrace();
			}
			int rowCount = 0;
			try
			{
				rs.last();
				rowCount = rs.getRow();
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			try
			{
				rs.beforeFirst();
			} catch (SQLException e1)
			{
				e1.printStackTrace();
			}
			int k = 0;
			try
			{
				result = new String[rowCount][columns];
				while (rs.next())
				{
					for (int i = 1; i <= columns; i++)
					{
						try
						{
							result[k][i - 1] = rs.getString(i);
						} catch (SQLException e)
						{
							e.printStackTrace();
						}
					}
					k++;
				}
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
			return result;

		} finally {
			release(conn, st, rs);
		}

	}

	public static String[][] selectByWebId(String table,int webId)
	{
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		String sql="select ";
		String[][] result = null;
		sql+="* from "+table+" where webId= "+webId+" ;";
		try
		{
			conn = getConn();
			try
			{
				st = (PreparedStatement) conn.prepareStatement(sql);
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
			try
			{
				rs = st.executeQuery();
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
			ResultSetMetaData mm = null;
			try
			{
				mm = rs.getMetaData();
			} catch (SQLException e1)
			{
				e1.printStackTrace();
			}
			int columns = 0;
			try
			{
				columns = mm.getColumnCount();
			} catch (SQLException e1)
			{
				e1.printStackTrace();
			}
			int rowCount = 0;
			try
			{
				rs.last();
				rowCount = rs.getRow();
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			try
			{
				rs.beforeFirst();
			} catch (SQLException e1)
			{
				e1.printStackTrace();
			}
			try
			{
				result = new String[rowCount+1][columns];
				for(int i=0;i<columns;i++)
				{
					result[0][i]=mm.getColumnName(i+1);
				}
				int k = 1;
				while (rs.next())
				{
					for (int i = 1; i <= columns; i++)
					{
						try
						{
							result[k][i - 1] = rs.getString(i);
						} catch (SQLException e)
						{
							e.printStackTrace();
						}
					}
					k++;
				}
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		} finally {
			release(conn, st, rs);
		}
		return result;
	}

	/*
	 * 在table里面通过列params2查询params1，关键词params2value（lp write）
	 */
	public static String getparams1Byparams2(String table,String params1,String params2,String params2Value) {
		String []params= {params2,params1};
		String [][] date=select(table,params);
		String backParams1=null;
		for(int i=0;i<date.length;i++) {
			if(date[i][0].equals(params2Value))
			{backParams1=date[i][1];
				break;}
		}
		return backParams1;
	}

	/*
	 * 通过table  name获取列名（lc write）
	 */
	public static String[] selectColnameBytbname(String tbName){
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		String sql="select*from ";
		sql+=tbName;
		String result[]=null;
		System.out.println(sql);
		try {
			conn = getConn();
			try {
				st = (PreparedStatement) conn.prepareStatement(sql);
			} catch (SQLException e) {
				System.out.println("预锟斤拷锟斤拷锟斤拷锟?");
				e.printStackTrace();
			}

			try {
				rs = st.executeQuery();
			} catch (SQLException e) {
				System.out.println("select锟斤拷锟斤拷");
				e.printStackTrace();
			}

			ResultSetMetaData mm = null;
			try {
				mm = rs.getMetaData();
			} catch (SQLException e1) {
				System.out.println("rs锟结构锟斤拷取锟斤拷锟斤拷");
				e1.printStackTrace();
			}

			int columns = 0;
			try {
				columns = mm.getColumnCount();
				String[]colname=new String[columns];
				for(int i=0;i<columns;i++) {
					colname[i]=mm.getColumnName(i);
					result=colname;
				}
			} catch (SQLException e1) {
				System.out.println("锟斤拷取锟斤拷锟斤拷锟斤拷锟?");
				e1.printStackTrace();
			}

		} finally {
			release(conn, st, rs);
		}
		return result;

	}

	/*
	 * select {params} from {table} where webId={webId}
	 */
	public static String[][] select(String table,String[] params,int webId)
	{
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		String sql="select ";
		String[][] result = null;
		for(int i=0;i<params.length-1;i++)
		{
			sql+=params[i]+",";
		}
		sql+=params[params.length-1]+" from "+table+" where webId="+webId+";";
		try
		{
			conn = getConn();
			try
			{
				st = (PreparedStatement) conn.prepareStatement(sql);
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
			try
			{
				rs = st.executeQuery();
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
			ResultSetMetaData mm = null;
			try
			{
				mm = rs.getMetaData();
			} catch (SQLException e1)
			{
				e1.printStackTrace();
			}
			int columns = 0;
			try
			{
				columns = mm.getColumnCount();
			} catch (SQLException e1)
			{
				e1.printStackTrace();
			}
			int rowCount = 0;
			try
			{
				rs.last();
				rowCount = rs.getRow();
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			try
			{
				rs.beforeFirst();
			} catch (SQLException e1)
			{
				e1.printStackTrace();
			}
			int k = 0;
			try
			{
				result = new String[rowCount][columns];
				while (rs.next())
				{
					for (int i = 1; i <= columns; i++)
					{
						try
						{
							result[k][i - 1] = rs.getString(i);
						} catch (SQLException e)
						{
							e.printStackTrace();
						}
					}
					k++;
				}
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		} finally {
			release(conn, st, rs);
		}
		return result;
	}

	/*
	 * get the first record's webId from sql "select * from website"
	 */
	public static int getLasttaskID()
	{
		int max = 0;
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		String sql = "select * from requesttable";
		try
		{
			conn = getConn();
			try
			{
				st = (PreparedStatement) conn.prepareStatement(sql);
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
			try
			{
				rs = st.executeQuery();
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
			try
			{
				rs.last();
				max =Integer.parseInt( rs.getString("requestID"));
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		} finally {
			release(conn, st, rs);
		}
		return max;
	}


	/*
	 * get the first record's webId from sql "select * from website"
	 */
	public static int getLastWebId()
	{
		int max = 0;
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		String sql = "select * from website";
		try
		{
			conn = getConn();
			try
			{
				st = (PreparedStatement) conn.prepareStatement(sql);
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
			try
			{
				rs = st.executeQuery();
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
			try
			{
				rs.last();
				max =Integer.parseInt( rs.getString("webId"));
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		} finally {
			release(conn, st, rs);
		}
		return max;
	}

	/*
	 * get the first record's webId from sql "select * from website"
	 */
	public static int getLastRequestID()
	{
		int max = 0;
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		String sql = "select * from requestTable";
		try
		{
			conn = getConn();
			try
			{
				st = (PreparedStatement) conn.prepareStatement(sql);
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
			try
			{
				rs = st.executeQuery();
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
			try
			{
				rs.last();
				max =Integer.parseInt( rs.getString("requestID"));
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		} finally {
			release(conn, st, rs);
		}
		return max;
	}
	/*
	 * @param webId
	 * @return the latest 10 infolinks, if not enough,return the most can satisfy
	 */
	public static ArrayList<Long> getLastInfoLinks(String webId)
	{
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		String[] param = {"round"};
		String round = DBUtil.select("current", param,Integer.parseInt(webId))[0][0];
		String r = round.split("-")[0]+"-%";
		String sql = "select * from status "
				+ "where webId='"+webId+"' and "
				+ "type='info' and "
				+ "round LIKE '"+r+"' "
				+ "order by statusId desc";
		ArrayList<Long> links=new ArrayList<Long>();
		try
		{
			conn = getConn();
			try
			{
				st = (PreparedStatement) conn.prepareStatement(sql);
			} catch (SQLException e)
			{
				System.out.println("get statement error");
				e.printStackTrace();
			}
			try
			{
				rs = st.executeQuery();
			} catch (SQLException e)
			{
				System.out.println("select operation error");
				e.printStackTrace();
			}
			try
			{
				for(int i=0;i<10;i++)
				{
					if(rs.next())
					{
						long rs1=rs.getLong("sLinkNum");
						links.add(rs1);
						System.out.println("the last time's success info link number is "+rs1);
					}
					else break;
				}
			} catch (SQLException e)
			{
				System.out.println("error in move point ");
				e.printStackTrace();
			}
		} finally {
			release(conn, st, rs);
		}
		return links;
	}

	/*
	 * get total number of a kind of links in current round(not use)
	 */
	public static int getLinksNum(String webId, int type){
		//type:
		//1 sinfo successful info links in total
		//2 finfo failed info links in total
		//3 squery successful query links in total
		//4 fquery successful query links in total
		String sf = "";
		String iq = "";
		if(type==1){
			sf = "sLinkNum";
			iq = "info";
		}
		else if(type == 2){
			sf = "fLinkNum";
			iq = "info";
		}
		else if(type == 3){
			sf = "sLinkNum";
			iq = "query";
		}
		else if(type == 4){
			sf = "fLinkNum";
			iq = "query";
		}
		else{
			return 0;
		}
		int sum = 0;
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		String[] param = {"round"};
		String round = DBUtil.select("current", param,Integer.parseInt(webId))[0][0];
		String r = round.split("-")[0]+"-%";
		String sql = "select * from status where webId=\'"+webId+"\' and type=\'"+iq+"\' and round LIKE \'"+r+"\';" ;
		try {
			conn = getConn();
			try {
				st = (PreparedStatement) conn.prepareStatement(sql);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				rs = st.executeQuery();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			try {
				while(rs.next()){
					String sn = rs.getString(sf);
					sum += Integer.parseInt(sn);

				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		} finally {
			release(conn, st, rs);
		}
		return sum;
	}

	/*
	 * not useS
	 */
	public static int getTotalSinfoLinks(String webId)
	{
		int sum = 0;
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		String[] param = {"round"};
		String round = DBUtil.select("current", param,Integer.parseInt(webId))[0][0];
		String r = round.split("-")[0]+"-%";
		String sql = "select * from status where webId=\'"+webId+"\' and type=\'info\' and round LIKE \'"+r+"\';" ;
		try
		{
			conn = getConn();
			try
			{
				st = (PreparedStatement) conn.prepareStatement(sql);
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
			try
			{
				rs = st.executeQuery();
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
			try
			{
				while(rs.next())
				{
					String sn = rs.getString("sLinkNum");
					sum += Integer.parseInt(sn);
				}
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		} finally {
			release(conn, st, rs);
		}
		return sum;
	}

	public static int delete(String table,String[] pn,String[] pv)
	{
		Connection conn = null;
		PreparedStatement st = null;
		int rs = 0;
		String sql="delete from "+table;
		String cond=" where ";
		for(int i=0;i< pn.length;i++)
		{
			cond+=pn[i]+" = "+pv[i]+" ,";
		}
		if(pn!=null&&pn.length!=0)
		{
			sql+=cond.substring(0, cond.length()-2)+" ;";
		}
		else
			sql+=" ;";
		try
		{
			conn=getConn();
			st = (PreparedStatement) conn.prepareStatement(sql);
			rs=st.executeUpdate();
		}
		catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			release(conn, st, null);
		}
		return rs;
	}

	public static void main(String[] args){
		String[] params_value = {"aaaa","prefix"};
		String[] params= {"requestName","requestDesc"};
		boolean flag = DBUtil.insert("requesttable", params, params_value);
		System.out.println("insert or not ?"+flag);

	}

}
