package util;

import enums.Base;
import enums.Driver;
import enums.RunningMode;
import enums.Usable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Initializer {

	public static boolean init(RunningMode runningMode, Driver driver, Base base, String taskName, String workPath, String siteURL){
		boolean flag=false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String[] param = {"webName", "runningMode", "workFile", "driver", "createtime", "usable", "base", "indexUrl"};
		String[] paramValue = {taskName, runningMode.name(), workPath, driver.getValue() + "", sdf.format(new Date()), Usable.none.getValue() + "", base.getValue() + "", siteURL};

		boolean websiteParam = DBUtil.insert("website", param, paramValue);
		if(!websiteParam) return false;//if can't insert, return directly
		String webId = DBUtil.getLastWebId()+"";

		if(runningMode == RunningMode.unstructed){
			flag = Initializer.initUnstructed(workPath, webId);
		}
		else if(runningMode == RunningMode.structed){
			DBUtil.insert("structedParam", new String[]{"webId"}, new String[]{"" + webId});

			flag = Initializer.initStructed(workPath, webId);
		}
		return flag;
	}
	
	//should be change!
	private static boolean initStructed(String workPath, String webId){

		//initialByWebId(webId);//initiate database
		createNewfile(workPath, webId);
		return true;
	}

	/**
	 * for structed
	 * @param workfile
	 * @param webId
	 */
	private static void createNewfile(String workfile,String webId){

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
	 * the initial options for a specified crawler, for example some makedir options
	 * for unstructed crawler
	 * notes: the current crawler implementation make sure that when initiating, only need to insert website table
	 * @return
	 */
	private static boolean initUnstructed(String workPath, String webId) {

		/**
		 * note: here the only mkdirs need is to create the work directory
		 */
		//create work dir
		File workDir = Paths.get(workPath, webId).toFile();
		if(!workDir.exists()){
			workDir.mkdirs();
		}
		return true;
	}

}
