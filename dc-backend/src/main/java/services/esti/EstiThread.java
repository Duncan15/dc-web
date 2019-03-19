package services.esti;

import util.DBUtil;

import java.util.HashMap;

public class EstiThread extends Thread {
    private boolean isInterrupted = false;
    private String status="stop";

    public void interrupt() {
        isInterrupted = true;
        super.interrupt();
    }

    public String getStatus(){
       return status;
    }

    public void setStatus(String status) {
        this.status = status;
        DBUtil.update("estimate", new String[]{"status"}, new String[]{status}, new String[]{"estiId"}, new String[]{getName()});

    }

    public void clearRes(){
        DBUtil.update("estimate", new String[]{"result"}, new String[]{" "}, new String[]{"estiId"}, new String[]{getName()});

    }

    public void run() {
        /*If you have inner loops,
         * you need to add the volatile to the loop condition.
         * */
        System.out.println("Running " + getName());
        setStatus("start");
        clearRes();

        /*
         * initialize the thread.
         * You need to select all the params from DBU using estiId.
         * The params come from two tables,website and estimate.
         * */

        String estiIdStr = getName();

        String[] params1= new String[]{
                "pagesInfoId", "linksXpath", "contentXpath","startWord","walkTimes"
        };
        String []confInfo1= DBUtil.select(
                "estimate", params1,
                new String[]{"estiId"}, new String[]{estiIdStr})[0];

        String[] params2= new String[]{
                "prefix", "paramQuery","paramPage","startPageNum",
                "paramList","paramValueList"
        };
        String []confInfo2= DBUtil.select(
                "website",params2,
                new String[]{"webId"}, new String[]{estiIdStr})[0];

        HashMap<String,String>WalkPairs=new HashMap<>();
        for(int i=0;i<params1.length;i++){
            WalkPairs.put(params1[i],confInfo1[i]);
        }
        for(int i=0;i<params2.length;i++){
            WalkPairs.put(params2[i],confInfo2[i]);
        }

        Walker walker=new Walker(WalkPairs);

        /*
         * Start the thread.
         * */
        Controller aController = new Controller(walker);
        while (!this.isInterrupted) {
            try {
                int feedback=aController.StartWalker(getName());
                if (feedback==0){
                    break;
                }
            } catch (Exception e) {
                System.out.println("估测任务 " + getName() + " 正在停止...");
                String result=aController.getMyWalker().GetEstiRes();
                DBUtil.update("estimate",new String[]{"result"},new String[]{result},new String[]{"estiId"},new String[]{estiIdStr});
                this.setStatus("stop");
                this.interrupt();

            }
        }

        /*Write the res into  db*/
        aController.getMyWalker().PrintEstiRes();
        String result=aController.getMyWalker().GetEstiRes();
        DBUtil.update("estimate",new String[]{"result"},new String[]{result},new String[]{"estiId"},new String[]{estiIdStr});
        this.setStatus("stop");


    }


    public static void main(String args[]) {
//        HashMap<String, EstiThread> threadMap = new HashMap<>();
//        EstiThread estiThread1 = new EstiThread("estiTask1");
//        EstiThread estiThread2 = new EstiThread("estiTask2");
//        EstiThread estiThread3 = new EstiThread("estiTask3");
//        EstiThread estiThread4 = new EstiThread("estiTask4");
//        threadMap.put("estiThread1", estiThread1);
//        threadMap.put("estiThread2", estiThread2);
//        threadMap.put("estiThread3", estiThread3);
//        threadMap.put("estiThread4", estiThread4);
//        estiThread1.start();
//        estiThread2.start();
//        estiThread3.start();
//        estiThread4.start();
//        try {
//            Thread.sleep(10000L);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        threadMap.get("estiThread4").stop();


    }


}

