package util;

public class Verifier {
    /**
     * 验证该任务名是否可用
     * @param taskName
     * @return
     */
    public static boolean varifyTaskName(String taskName){
        boolean flag=false;
        String[] getP = {"webName"};
        String[] p = {"webName"};
        String[] v = {taskName};
        if(DBUtil.select("website",getP,p,v).length != 0)
            flag=true;
        return flag;
    }
    public static boolean verifyExist(int webID, String table) {
        boolean flag = false;
        if (DBUtil.select(table, new String[]{"id"}, webID).length != 0) {
            flag = true;
        }
        return flag;
    }
    public static boolean verifyUsable(int webID) {
        if (!verifyExist(webID, "extraConf")) {
            return false;
        }

        String[] p = new String[] {"indexUrl"};
        String[][] ans = DBUtil.select("website", p, webID);
        if (ans.length != 0 && (!"".equals(ans[0][0]))) {
            p = new String[] {"charset"};
            ans = DBUtil.select("extraConf", p, webID);
            if (ans.length != 0 && (!"".equals(ans[0][0]))) {
                return true;
            }
        }
        return false;
    }
    public static Integer verifyInt(String webIDStr) {
        if(webIDStr == null) return null;
        int webID = 0;
        try {
            webID = Integer.parseInt(webIDStr);
            return webID;
        } catch (NumberFormatException ex) {
            //ignored
        }
        return null;
    }
}
