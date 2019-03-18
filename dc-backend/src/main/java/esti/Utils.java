package esti;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.File;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

public class Utils {

    /*To get the content of links table for the purpose of comparing two page*/
    public static  String getLinksContent(String link,String content_xpath) throws IOException, InterruptedException {

        System.out.println("Current linksTable is "+link);
        Document doc = BaseUtils.GetDoc(link);
        Elements elements=doc.getElementsByClass(content_xpath);
        if(elements.isEmpty()){
            return "";
        }
        Element firElement=elements.first();
        Iterator<Element> iterator=firElement.children().iterator();
        String total_text="";
        while (iterator.hasNext()){
            total_text+=iterator.next().text();
        }
        return total_text;
    }


    public static String getContent(String link, String content_xpath) throws IOException, InterruptedException {
        Document doc = BaseUtils.GetDoc(link);
        Elements contents=doc.getElementsByClass(content_xpath);
        if (contents.text().isEmpty()) {
            return "";
        }else {
            return contents.text();
        }


    }
    public static int GetRandNum(int totalNum){
        return (int) (Math.random() * totalNum)+1;
    }

    public static int getDocSize(String docStr) throws IOException {
        HashSet<String> hSet = BaseUtils.Wash(docStr);
        return hSet.size();
    }

    public static ArrayList<String> ExtrKwList(String docStr) {
        HashSet<String> hSet = null;
        try {
            hSet = BaseUtils.Wash(docStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert hSet != null;
        ArrayList<String> keywordList1 = new ArrayList<String>(hSet);
        return keywordList1;
    }


    public static String BuildQueryLink(HashMap<String, String> confMap) {
        String QueryLink = null;
        String linkPrefix = confMap.get("linkPrefix");
        String queryParam = confMap.get("queryParam");
        String pageParam = confMap.get("pageParam");
        String pageConf = confMap.get("pageConf");
        String otherParams = confMap.get("otherParams");
        String otherValues = confMap.get("otherValues");
        String keyword = confMap.get("startKeyword");
        String[] pageConfArray = pageConf.split(",");
        String startPage = pageConfArray[0];
        String pageInterval = pageConfArray[1];

        String curPageNum;

        try {
            keyword = URLEncoder.encode(keyword, "utf-8");
        } catch (UnsupportedEncodingException ex) {
            //ignored
        }
        if (!linkPrefix.endsWith("?")) {
            QueryLink = linkPrefix + "?";
        }else {
            QueryLink=linkPrefix;
        }

        List<String> paramList = new ArrayList<>();
        paramList.add(queryParam + "=" + keyword);
        String[] otherParamsArray = otherParams.split(",");
        String[] otherValuesArray = otherValues.split(",");
        for (int i = 0; i < otherParamsArray.length; i++) {
            paramList.add(otherParamsArray[i] + "=" + otherValuesArray[i]);
        }
        if (confMap.containsKey("curPageNum")) {
            curPageNum = confMap.get("curPageNum");
            int startNum = Integer.parseInt(startPage);//the start number of pageNum，maybe 1 or 0
            int numInterval = Integer.parseInt(pageInterval);//the interval number of different pageNum corresponding to the neighbour query link
            int pgV = (Integer.parseInt(curPageNum) - 1) * numInterval + startNum;//the final value occur in the query link
            paramList.add(pageParam + "=" + pgV);
        }

        QueryLink += StringUtils.join(paramList, "&");
        return QueryLink;
    }


    public void getPath() throws IOException {
        File directory = new File("");
        System.out.println(directory.getCanonicalPath());
        System.out.println(this.getClass()+"\\webapp\\WEB-INF\\stopwords.txt");
    }
    public static void main(String[] args) throws IOException {
        new Utils().getPath();
    }
}
