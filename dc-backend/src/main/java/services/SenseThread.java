package services;
import util.DBUtil;
import util.WebCrawlerDemo;
import java.util.HashMap;
public class SenseThread extends Thread{
    private String status = "stop";
    private boolean isInterrupted = false;
    private WebCrawlerDemo webCrawlerDemo = new WebCrawlerDemo();
    public String getStatus(){
        return status;
    }
    public void interrupt() {
        isInterrupted = true;
        super.interrupt();
    }
    public void setStatus(String status) {
        if(status.equals("stop"))
        {
            webCrawlerDemo.crawlerStop();
        }
        this.status = status;

        DBUtil.update("sensestate",new String[]{"status"},new String[]{status},new String[]{"id"},new String[]{getName()});


    }

    public void run(){
        System.out.println("Running " + getName());
        setStatus("start");
        try{
            String [][]url = DBUtil.select("website",new String[]{"indexUrl"},new String[]{"webId"},new String[]{getName()});
            String link = url[0][0];

            webCrawlerDemo.setBaseLink1(link);
            webCrawlerDemo.setWebId(Integer.valueOf(getName()));
            webCrawlerDemo.myPrint(link);
            this.setStatus("stop");
        }catch (Exception e){
            System.out.println("侦测任务 " + getName() + " 正在停止...");
            this.setStatus("stop");
            this.interrupt();
        }


    }


}
