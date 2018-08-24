package util;

import java.text.SimpleDateFormat;
import java.util.Date;
public class Config {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	public static boolean setInterface(String runningMode,String taskName, String workPath,String driver){
		boolean flag=false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String[] param = {"webName","runningMode","workFile","driver","createtime","usable"};
		String[] paramValue = {taskName,runningMode,workPath,driver,sdf.format(new Date()),"0"};
		if("unstructed".equals(runningMode)){
			flag=ParamSetter.setInterface_unstructed(param, paramValue);
		}
		else if("structed".equals(runningMode)){
			flag=ParamSetter.setInterface(param, paramValue);
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
