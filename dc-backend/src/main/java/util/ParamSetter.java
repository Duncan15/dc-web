package util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class ParamSetter {
	private int threadNum = 20;
	private int timeout = 10000;
	private String charset = "gbk";
	private static String workfile = "D:/crawler/";
	
	//should be change!
	public static boolean setInterface(String[] param,String[] paramValue ){
		
		boolean websiteParam = DBUtil.insert("website", param, paramValue);
		if(!websiteParam)return false;//if can't insert, return directly
		String webId = DBUtil.getLastWebId()+"";
		String[] filePara = {"workFile"};
		workfile = DBUtil.select("website", filePara, Integer.parseInt(webId))[0][0];

		//initialByWebId(webId);//initiate database
		createNewfile(workfile, webId);
		return websiteParam;
	}

	public static void createNewfile(String workfile,String webId){

		//create work dir
		File workDir = new File(workfile);
		if(!workDir.exists()){
			workDir.mkdirs();
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


	/**
	 * the initial options for a specified crawler, for example some insert options and makedir options
	 * for unstructed crawler
	 * notes: the current crawler implementation make sure that when initiating, only need to insert website table
	 * @param param
	 * @param paramValue
	 * @return
	 */
	public static boolean setInterfaceUnstructed(String[] param, String[] paramValue) {

		boolean websiteParam = DBUtil.insert("website", param, paramValue);
		if(!websiteParam) return false;//if can't insert, return directly
		String webId = DBUtil.getLastWebId() + "";
		String[] filePara = {"workFile"};
		workfile = DBUtil.select("website", filePara, Integer.parseInt(webId))[0][0];

		/**
		 * note: here the only mkdirs need is to create the work directory
		 */
		//create work dir
		File workDir = Paths.get(workfile, webId).toFile();
		if(!workDir.exists()){
			workDir.mkdirs();
		}
		return websiteParam;
	}
	public static boolean createPattern(String webId,String patternName, String xpath,String indexPath){
		String[] params = {"webId","patternName","xpath","indexPath"};
		String[] params_value = {webId,patternName,xpath,indexPath};
		return DBUtil.insert("pattern", params, params_value);
	}

}
