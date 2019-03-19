package services.esti;

import org.jsoup.HttpStatusException;
import util.DBUtil;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Controller {

    private Walker myWalker;

    public Controller(Walker myWalker) {
        this.myWalker = myWalker;

    }

    public Walker getMyWalker() {
        return myWalker;
    }

    public int StartWalker(String estiId) throws IOException, InterruptedException {
        int iter_num = 0;
        String walkTimesStr= DBUtil.select("estimate",new String[]{"walkTimes"},new String[]{"estiId"}, new String[]{estiId})[0][0];
        /*The frontend ensure the walktimes is a int str*/
        int walkTimes=Integer.parseInt(walkTimesStr);
        /*To show the RateBar*/
        double rate = (double) iter_num / walkTimes;
        DecimalFormat decimalFormat = new DecimalFormat("00.00%");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        String rateBar = decimalFormat.format(rate);//format 返回的是字符串
        DBUtil.update("estimate", new String[]{"rateBar"}, new String[]{rateBar}, new String[]{"estiId"}, new String[]{estiId});

        try {
            while (iter_num < walkTimes) {
                iter_num = iter_num + 1;
                /*Get a rand page link*/
                int totalPageNum = myWalker.getPageNum();
                if (totalPageNum == 0) {
                    int kwListSize = myWalker.getKwList().size();
                    String randKw = myWalker.getKwList().get(Utils.GetRandNum(kwListSize) - 1);
                    myWalker.setKeyword(randKw);
                }
                String QueryLink2 = myWalker.BuildQueryLink(myWalker.getKeyword(), Utils.GetRandNum(totalPageNum) + "");

                /*Get all links of this page*/
                ArrayList<String> docLinks = myWalker.getLinks(QueryLink2);

                /*Genr a rand docLink,and fetch the docStr*/
                String randDocStr = "";
                try {
                    randDocStr = myWalker.GetRandContent(docLinks);
                } catch (HttpStatusException ex) {
                    /* ignored */
                }
            /*
            Since the docStr may be null.
            The if condition make sure we get a not null randDocStr
            * by going into next loop with another kw*/
                if (randDocStr.equals("")) {
                    int kwListSize = myWalker.getKwList().size();
                 /*
                    After 3 times fetch,the rand_docStr is null;
                    We change to another kw.
                 */
                    /*
                     * if kwList is null.Go into next loop.
                     * This happen when the startKwWord return many null docs
                     * */
                    if (kwListSize != 0) {
                        /*I store last loop's docStr
                         * So I can change another word from the kwList of this.myWalker*/
                        String randKw = myWalker.getKwList().get(Utils.GetRandNum(kwListSize) - 1);
                        myWalker.setKeyword(randKw);
                    }

                } else {
                    //Normal arrival:
                    this.myWalker.getKwList().add(this.myWalker.getKeyword());
                    ArrayList<String> KwList = Utils.ExtrKwList(randDocStr);
                    this.myWalker.setKwList(KwList);
                    int docSize = KwList.size();
                    String newKw = KwList.get(Utils.GetRandNum(docSize) - 1);
                    System.out.println(estiId + " new keyword is " + newKw);
                    this.myWalker.setKeyword(newKw);
                    this.myWalker.getDocList().addDoc(new Doc(this.myWalker.getCurDocLink(), docSize, 0));
                }

                /*limit the time in order not to be too fast*/
                Thread.sleep(6000);
            }
        }catch (IndexOutOfBoundsException e){

        }
        return 0;
    }

}
