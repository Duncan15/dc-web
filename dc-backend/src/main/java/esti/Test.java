package esti;

import org.jsoup.HttpStatusException;

import java.io.IOException;
import java.util.ArrayList;

public class Test {

    public static void main(String[] args) throws IOException, InterruptedException {
//
//        String linkPrefix = "http://www.cufe.edu.cn/cms/search/searchResults.jsp";
//        String queryParam = "query";
//        String pageParam = "offset";
//        String pageConf = "0,10";
//        String keyword = "书记";
//        String links_xpath = "con03";
//        String pages_info_id = "";
//        String content_class_name = "list05";
//        String pageBarXpath = "";
//        String otherParams = "rows,flg,siteID";
//        String otherValues = "10,1,4";
//        String resPerPage = "10";

//        String linkPrefix = "https://search1.henan.gov.cn/jrobot/search.do?";
//        String queryParam = "q";
//        String pageParam = "p";
//        String pageConf = "0,10";
//        String keyword = "国企改革";
//        String links_xpath = "ui-search-result-items";
//        String pages_info_id = "jsearch-info-box";
//        String content_class_name = "content";
//        String pageBarXpath = "";
//        String otherParams = "webid";
//        String otherValues = "450001";
//        String resPerPage = "12";

        String linkPrefix = "http://www.zhaoan.gov.cn/cms/siteresource/search.shtml?";
        String queryParam = "key";
        String pageParam = "page";
        String pageConf = "1,1";
        String keyword = "县委书记";
        String links_xpath = "wz-list";
        String pages_info_id = "";
        String content_class_name = "zw";
        String pageBarXpath = "pages";
        String otherParams = "searchSiteId,siteId,pageName";
        String otherValues = "60427348114130001,60427348114130001,quickSiteSearch";
        String resPerPage = "10";


        Walker walker = new Walker(
                linkPrefix, queryParam, keyword, pageParam,
                pageConf, otherParams, otherValues, pages_info_id, pageBarXpath,
                links_xpath, content_class_name, resPerPage);

        try {
            for (int i = 0; i < 50; i++) {
                /*Get a rand page link*/
                System.out.println("keyword " + walker.getKeyword());
                System.out.println("pageNum :" + walker.getPageNum());
                String QueryLink2 = walker.BuildQueryLink(walker.getKeyword(), Utils.GetRandNum(walker.getPageNum())+1 + "");
                System.out.println(QueryLink2);
                /*Get all links of this page*/
                ArrayList<String> docLinks = walker.getLinks(QueryLink2);

                /*Genr a rand docLink,and fetch the docStr*/
                String randDocStr = "";
                try {
                    randDocStr = walker.GetRandContent(docLinks);
                } catch (HttpStatusException ex) {
                    /* ignored */
                }
                System.out.println(randDocStr);

            /*
            Since the docStr may be null.
            The if condition make sure we get a not null randDocStr
            * by going into next loop with another kw*/
                if (randDocStr.equals("")) {
                    int kwListSize = walker.getKwList().size();
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
                         * So I can change another word from the kwList of walker*/
                        String randKw = walker.getKwList().get(Utils.GetRandNum(kwListSize) - 1);
                        walker.setKeyword(randKw);
                    }

                } else {
                    //Normal arrival:
                    walker.getKwList().add(walker.getKeyword());
                    ArrayList<String> KwList = Utils.ExtrKwList(randDocStr);
                    walker.setKwList(KwList);
                    int docSize = KwList.size();
                    String newKw = KwList.get(Utils.GetRandNum(docSize) - 1);
                    System.out.println("newKw is " + newKw);
                    walker.setKeyword(newKw);
                    walker.getDocList().addDoc(new Doc(walker.getCurDocLink(), docSize, 0));
                }

//            Thread.sleep(3000L);

                System.out.println("第" + i + "次循环");
            }
        }catch (Exception e){
            System.out.println("Exception happened");
            e.getMessage();
        }


        /*
        * Print the DocList.
        * */
        System.out.println(walker.getDocList().toString());
        walker.PrintEstiRes();
        System.out.println("The EstiRes is "+walker.GetEstiRes());
//        double rate = (double)34/10000;
//        System.out.println(rate);
//        DecimalFormat decimalFormat=new DecimalFormat("00.00%");//构造方法的字符格式这里如果小数不足2位,会以0补足.
//        String rateBar=decimalFormat.format(rate);//format 返回的是字符串
//        System.out.println("rateBar:"+rateBar);


    }
}

