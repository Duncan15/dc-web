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
		String[] paramValue = {taskName, runningMode.name(), workPath.replace("\\","\\\\"), driver.getValue() + "", sdf.format(new Date()), Usable.none.getValue() + "", base.getValue() + "", siteURL};

		boolean websiteParam = DBUtil.insert("website", param, paramValue);
		if(!websiteParam) return false;//if can't insert, return directly
		String webId = DBUtil.getLastWebId()+"";

		if(runningMode == RunningMode.unstructed){
			flag = Initializer.initUnstructed(workPath, webId);
		}
		else if(runningMode == RunningMode.structed){
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
	private static void createNewfile(String workfile, String webId){

		//create work dir
		File workDir = Paths.get(workfile, webId).toFile();
		if(!workDir.exists()){
			workDir.mkdirs();
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
