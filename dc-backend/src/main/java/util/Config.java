package util;

import enums.Driver;
import enums.RunningMode;
import enums.Usable;

import java.text.SimpleDateFormat;
import java.util.Date;
public class Config {

	public static boolean setInterface(RunningMode runningMode, String taskName, String workPath, Driver driver){
		boolean flag=false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String[] param = {"webName","runningMode","workFile","driver","createtime","usable"};
		String[] paramValue = {taskName, runningMode.name(), workPath, driver.getValue() + "", sdf.format(new Date()), Usable.none.getValue() + ""};
		if(runningMode == RunningMode.unstructed){
			flag = ParamSetter.setInterfaceUnstructed(param, paramValue);
		}
		else if(runningMode == RunningMode.structed){
			flag = ParamSetter.setInterface(param, paramValue);
		}
		return flag;
	}
	public static boolean checkTask(String taskName){
		boolean flag=false;
		String[] getP = {"webName"};
		String[] p = {"webName"};
		String[] v = {taskName};
		if(DBUtil.select("website",getP,p,v).length != 0)
			flag=true;
		return flag;
	}

}
