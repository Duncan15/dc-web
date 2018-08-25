package util;

public class DBService 
{
	public static String getWorkFile(String webId)
	{
		String[] filePara = {"workFile"};
		String workFile = DBUtil.select("website", filePara, Integer.parseInt(webId))[0][0];
		return workFile;
	}
	public static String getParamQuery(String webId)
	{
		String[] para = {"paramQuery"};
		String paramQuery = DBUtil.select("website", para, Integer.parseInt(webId))[0][0];
		return paramQuery;
	}
	public static String getCharset(String webId)
	{
		String[] para = {"charset"};
		String charset = DBUtil.select("website", para, Integer.parseInt(webId))[0][0];
		return charset;
	}
	public static int getTimeout(String webId)
	{
		String para[] = {"timeout"};
		int timeout = Integer.parseInt(DBUtil.select("website", para, Integer.parseInt(webId))[0][0]);
		return timeout;
	}
	
	/*
	 * update status in {current} table
	 * @param webId,statusId(from 0 to 3,and -1 represent initial),flag(true represent enter and false represent leave)
	 */
	public static void updateStatus(String webId,int statusId,boolean flag)
	{
		String status="";
		if(flag)status="active";
		else status="done";
		String[] tmpParams={"M1status","M2status","M3status","M4status"};
		String[] tmpParamValue={"inactive","inactive","inactive","inactive"};
		String[] params=tmpParams;
		String[] paramValue=tmpParamValue;
		if(statusId>=0&&statusId<4)
		{
			params=new String[1];
			paramValue=new String[1];
			params[0]=tmpParams[statusId];
			paramValue[0]=status;
		}
		DBUtil.update("current",params, paramValue, Integer.parseInt(webId));
	}
	
	/*
	 * read pattern information from DB to 'patterns'
	 * @param webId
	 * @return patterns[] {"patternName", "xpath","indexPath"}
	 */
	public static String[][] getPatterns(int webId)
	{
		String[][] patterns = null;
		String table = "pattern";
		String params[] = {"patternName", "xpath","indexPath" };
		String[] con = {"webId"};
		String[] conV = {webId+""};
		patterns = DBUtil.select(table, params, con,conV);
		return patterns;
	}
}
