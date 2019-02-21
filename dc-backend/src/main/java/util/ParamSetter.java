package util;

import java.io.File;
import java.io.IOException;

public class ParamSetter {
	private int threadNum = 20;
	private int timeout = 10000;
	private String charset = "gbk";
	private static String workfile = "D:/crawler/";
	
	public static void initialByWebId(String webId)
	{
		String[] pn={"webId"};
		String[] pv={webId};
		DBUtil.delete("current", pn, pv);
		DBUtil.delete("status", pn, pv);
		//DBUtil.delete("pattern", pn, pv);
		String[] p1 = {"webId","round","type","fLinkNum","sLinkNum"};
		String[] pv1 = {webId,"0","info","0","0"};
		String[] pv2 = {webId,"0","query","0","0"};
		DBUtil.insert("status", p1, pv1);
		DBUtil.insert("status", p1, pv2);
		
		String[] cParams = {"webId","round","M1status","M2status","M3status","M4status","fQueryLink_sum","fInfoLink_sum","SampleData_sum"};
		String[] cParamValue = {webId,"0","inactive","inactive","inactive","inactive","0","0","0"};
		DBUtil.insert("current", cParams, cParamValue);
		String[] qParams = {"webId"};
		String[] qParamValue = {webId};
		DBUtil.insert("queryparam", qParams, qParamValue);
		DBUtil.insert("structedParam", qParams, qParamValue);
		//update pattern
		//createPattern(webId+"","fulltext","//body","fulltextIndex");
		//createPattern(webId+"","table","//table","tableIndex");
	}
	//5.1.1+5.1.2
	public static boolean setInterface(String[] param,String[] paramValue ){
		
		boolean websiteParam = DBUtil.insert("website", param, paramValue);
		if(!websiteParam)return false;//if can't insert, return directly
		String webId = DBUtil.getLastWebId()+"";
		String[] filePara = {"workFile"};
		workfile = DBUtil.select("website", filePara, Integer.parseInt(webId))[0][0];

		initialByWebId(webId);//initiate database
		createNewfile( workfile, webId);
		return websiteParam;
	}
	public static void createNewfile(String workfile,String webId){

		//create work dir
		File workFilff = new File(workfile);
		if(!workFilff.exists()){
			workFilff.mkdirs();
		}
		//subpage dir
		File subpage = new File(workfile+webId+"/subpage");
		//if(!subpage.exists())	
			subpage.mkdirs();
		File subpageMD5 = new File(workfile+webId+"/subpage/subpageMD5.txt");
		//if(!subpageMD5.exists())
		//{
			try {
				subpageMD5.createNewFile();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		//}
		
		//html dir
		File f = new File(workfile+webId+"/html");
		//if(!f.exists())
			f.mkdirs();
		//query dir
		f = new File(workfile+webId+"/query");
		//if(!f.exists())
			f.mkdir();
		File queries = new File(workfile+webId+"/query/queries.txt");
		
			try {
				queries.createNewFile();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
		File unqueries = new File(workfile+webId+"/query/unusedQueries.txt");
		
			try {
				unqueries.createNewFile();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
		File usedQueries = new File(workfile+webId+"/query/usedQueries.txt");
		
			try {
				usedQueries.createNewFile();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
		File logging = new File(workfile+webId+"/logging.txt");
		
			try {
				logging.createNewFile();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
		f = new File(workfile+webId+"/failedQueryLinks.txt");
		
			try {
				f.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		f = new File(workfile+webId+"/ParamValuelist.txt");
		
			try {
				f.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		f = new File(workfile+webId+"/dataMD5.txt");
		
			try {
				f.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
	public static void initialByWebId__unstructed(String webId)
	{
		String[] pn={"webId"};
		String[] pv={webId};
		DBUtil.delete("current", pn, pv);
		DBUtil.delete("status", pn, pv);
		DBUtil.delete("pattern", pn, pv);
		String[] p1 = {"webId","round","type","fLinkNum","sLinkNum"};
		String[] pv1 = {webId,"1-1","info","0","0"};
		String[] pv2 = {webId,"1-1","query","0","0"};
		DBUtil.insert("status", p1, pv1);
		DBUtil.insert("status", p1, pv2);

		String[] cParams = {"webId","round","M1status","M2status","M3status","M4status","fQueryLink_sum","fInfoLink_sum","SampleData_sum"};
		String[] cParamValue = {webId,"1-1","inactive","inactive","inactive","inactive","0","0","0"};
		DBUtil.insert("current", cParams, cParamValue);

		//update pattern
		createPattern(webId+"","fulltext","//body","fulltextIndex");
		createPattern(webId+"","table","//table","tableIndex");
	}
	//5.1.1+5.1.2
	public static boolean setInterface_unstructed(String[] param,String[] paramValue ){

		boolean websiteParam = DBUtil.insert("website", param, paramValue);
		if(!websiteParam)return false;//if can't insert, return directly
		String webId = DBUtil.getLastWebId()+"";
		String[] filePara = {"workFile"};
		workfile = DBUtil.select("website", filePara, Integer.parseInt(webId))[0][0];

		initialByWebId(webId);//initiate database

		//create work dir
		File workFilff = new File(workfile);
		if(!workFilff.exists()){
			workFilff.mkdirs();
		}
		//attachment dir
		File attachment = new File(workfile+webId+"/attachment");
		if(!attachment.exists())
			attachment.mkdirs();
		//html dir
		File f = new File(workfile+webId+"/html");
		if(!f.exists())
			f.mkdirs();
		//query dir
		f = new File(workfile+webId+"/query");
		if(!f.exists())
			f.mkdir();
		return websiteParam;
	}
	public static boolean createPattern(String webId,String patternName, String xpath,String indexPath){
		String[] params = {"webId","patternName","xpath","indexPath"};
		String[] params_value = {webId,patternName,xpath,indexPath};
		return DBUtil.insert("pattern", params, params_value);
	}

}
