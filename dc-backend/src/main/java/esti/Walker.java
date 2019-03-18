package esti;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Walker {
    private String linkPrefix;
    private String queryParam;
    private String keyword;
    private String pageParam;
    private String pageConf;
    private String otherParams;
    private String otherValues;
    private String pageInfoXpath;
    private String linksTableXpath;
    private String contentStrXpath;
    private String curPageNum = "";
    private String resPerPage;
    private ArrayList<String> kwList = new ArrayList<>();
    private DocList docList;
    private String curDocLink;
    private long startTime;

    Walker() {
    }

    public Walker(String linkPrefix, String queryParam, String keyword, String pageParam, String pageConf, String otherParams, String otherValues, String pageInfoXpath, String pageBarXpath,
                  String linksTableXpath, String contentStrXpath, String resPerPage) {
        this.linkPrefix = linkPrefix;
        this.queryParam = queryParam;
        this.keyword = keyword;
        this.pageParam = pageParam;
        this.pageConf = pageConf;
        this.otherParams = otherParams;
        this.otherValues = otherValues;
        this.pageInfoXpath = pageInfoXpath;
        this.linksTableXpath = linksTableXpath;
        this.contentStrXpath = contentStrXpath;
        this.resPerPage = resPerPage;
        this.kwList = new ArrayList<String>();
        docList = new DocList();
        this.startTime = System.currentTimeMillis();
    }

    public Walker(HashMap<String,String>walkPairs) {
        this.linkPrefix = walkPairs.get("prefix");
        this.queryParam = walkPairs.get("paramQuery");
        this.keyword =  walkPairs.get("startWord");
        this.pageParam = walkPairs.get("paramPage");
        this.pageConf = walkPairs.get("startPageNum");
        this.otherParams = walkPairs.get("paramList");
        this.otherValues = walkPairs.get("paramValueList");
        this.pageInfoXpath = walkPairs.get("pageInfoId");
        this.linksTableXpath = walkPairs.get("linksXpath");
        this.contentStrXpath =  walkPairs.get("contentXpath");
        this.kwList = new ArrayList<String>();
        docList = new DocList();
        this.startTime = System.currentTimeMillis();
    }

    public String getLinkPrefix() {
        return linkPrefix;
    }

    public void setLinkPrefix(String linkPrefix) {
        this.linkPrefix = linkPrefix;
    }

    public String getQueryParam() {
        return queryParam;
    }

    public void setQueryParam(String queryParam) {
        this.queryParam = queryParam;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getPageParam() {
        return pageParam;
    }

    public void setPageParam(String pageParam) {
        this.pageParam = pageParam;
    }

    public String getPageConf() {
        return pageConf;
    }

    public void setPageConf(String pageConf) {
        this.pageConf = pageConf;
    }

    public String getOtherParams() {
        return otherParams;
    }

    public void setOtherParams(String otherParams) {
        this.otherParams = otherParams;
    }

    public String getOtherValues() {
        return otherValues;
    }

    public void setOtherValues(String otherValues) {
        this.otherValues = otherValues;
    }

    public String getPageInfoXpath() {
        if (this.pageInfoXpath == null) return "";
        else return pageInfoXpath;
    }

    public void setPageInfoXpath(String pageInfoXpath) {
        this.pageInfoXpath = pageInfoXpath;
    }

    public String getLinksTableXpath() {
        return linksTableXpath;
    }

    public void setLinksTableXpath(String linksTableXpath) {
        this.linksTableXpath = linksTableXpath;
    }

    public String getContentStrXpath() {
        return contentStrXpath;
    }

    public void setContentStrXpath(String contentStrXpath) {
        this.contentStrXpath = contentStrXpath;
    }


    public String getCurPageNum() {
        return curPageNum;
    }

    public void setCurPageNum(String curPageNum) {
        this.curPageNum = curPageNum;
    }

    public String getResPerPage() {
        return resPerPage;
    }

    public void setResPerPage(String resPerPage) {
        this.resPerPage = resPerPage;
    }

    public String BuildQueryLink(String keyword, String curPageNum) {
        String QueryLink = null;
        String linkPrefix = this.getLinkPrefix();
        String queryParam = this.getQueryParam();
        String pageParam = this.pageParam;
        String pageConf = this.pageConf;
        String otherParams = this.otherParams;
        String otherValues = this.otherValues;
        String[] pageConfArray = pageConf.split(",");
        String startPage = pageConfArray[0];
        String pageInterval = pageConfArray[1];
        try {
            keyword = URLEncoder.encode(keyword, "utf-8");
        } catch (UnsupportedEncodingException ex) {
            //ignored
        }
        if (!linkPrefix.endsWith("?")) {
            QueryLink = linkPrefix + "?";
        } else {
            QueryLink = linkPrefix;
        }

        List<String> paramList = new ArrayList<>();
        paramList.add(queryParam + "=" + keyword);
        String[] otherParamsArray = otherParams.split(",");
        String[] otherValuesArray = otherValues.split(",");
        for (int i = 0; i < otherParamsArray.length; i++) {
            paramList.add(otherParamsArray[i] + "=" + otherValuesArray[i]);
        }
        if (!curPageNum.equals("")) {
            int startNum = Integer.parseInt(startPage);//the start number of pageNum，maybe 1 or 0
            int numInterval = Integer.parseInt(pageInterval);//the interval number of different pageNum corresponding to the neighbour query link
            int pgV = (Integer.parseInt(curPageNum) - 1) * numInterval + startNum;//the final value occur in the query link
            paramList.add(pageParam + "=" + pgV);
        }

        QueryLink += StringUtils.join(paramList, "&");
        return QueryLink;
    }

    /**
     * @return the total pages num of this query.
     * @throws IOException
     */
    public int getPageNum() throws IOException, InterruptedException {
        if (this.getPageInfoXpath().equals("")) {
            Service aService = new Service(this);
            return aService.getTotalPageNum(this.keyword);
        } else {
            String aQueryLink = this.BuildQueryLink();
//        System.out.println("build query link is \n"+aQueryLink);
            Document doc = BaseUtils.GetDoc(aQueryLink);
            System.out.println(doc.getElementsByClass(this.pageInfoXpath).get(0).text());
            if (doc.getElementsByClass(this.pageInfoXpath).hasText()) {
                String page_info_str = doc.getElementsByClass(this.pageInfoXpath).text();
                System.out.println("page_info_str"+page_info_str);
                int total_links = BaseUtils.getPagesInfo(page_info_str);
                int resPerPage = Integer.parseInt(this.getResPerPage());
                int total_page = total_links / resPerPage;
                if (total_links % resPerPage != 0) {
                    total_page += 1;
                }
                return total_page;
            } else {
                return 0;
            }
        }
    }

    public String BuildQueryLink() {
        String QueryLink = null;
        String linkPrefix = this.getLinkPrefix();
        String queryParam = this.getQueryParam();
        String keyword = this.keyword;
        String otherParams = this.otherParams;
        String otherValues = this.otherValues;
        try {
            keyword = URLEncoder.encode(keyword, "utf-8");
        } catch (UnsupportedEncodingException ex) {
            //ignored
        }
        if (!linkPrefix.endsWith("?")) {
            QueryLink = linkPrefix + "?";
        } else {
            QueryLink = linkPrefix;
        }

        List<String> paramList = new ArrayList<>();
        paramList.add(queryParam + "=" + keyword);
        String[] otherParamsArray = otherParams.split(",");
        String[] otherValuesArray = otherValues.split(",");
        for (int i = 0; i < otherParamsArray.length; i++) {
            paramList.add(otherParamsArray[i] + "=" + otherValuesArray[i]);
        }

        QueryLink += StringUtils.join(paramList, "&");
        return QueryLink;
    }

    public ArrayList<String> getLinks(String link) throws IOException {
        Document doc = Jsoup.connect(link).userAgent(
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31")
                .get();
        Elements contents = doc.getElementsByClass(this.linksTableXpath);
        HashSet<String> aList = new HashSet<>();
        for (Element content : contents) {
            Elements links_1 = content.select("a[href*=htm]");
            Elements links_2 = content.select("a[href*=html]");
            for (Element link_1 : links_1) {
                aList.add(link_1.attr("abs:href"));
            }
            for (Element link_1 : links_2) {
                aList.add(link_1.attr("abs:href"));
            }
        }
        // 从文档的主词数组里面随机取词:
        return new ArrayList<String>(aList);
    }

    public String GetRandContent(ArrayList<String> linksList) throws IOException, InterruptedException {
        //get a rand_docLink for links_list.
        String rand_docStr = "";
        String rand_docLink = "";
        int i = 0;
        if (linksList.size() != 0) {
            do {
                rand_docLink = linksList.get(Utils.GetRandNum(linksList.size()) - 1);
                System.out.println(rand_docLink);
                rand_docStr = Utils.getContent(rand_docLink, this.contentStrXpath);
                i++;
            } while (rand_docStr.equals("") && i < 3);
        }
        if (!rand_docLink.equals("")) {
            this.setCurDocLink(rand_docLink);
        }
        return rand_docStr;


    }

    public void PrintEstiRes() {
        /*
         * compute the total num
         * */
        long endTime = System.currentTimeMillis(); // 获取结束时间
        System.out.println("================================================================");
        System.out.println("程序运行时间： " + ((endTime - this.startTime) / 1000f) / 60f + "min");
        System.out.println("num of documents:" + this.getDocList().getSize());
        System.out.println("the C now is " + this.getDocList().getC());
        long sumDoc = this.getDocList().getSum();
        System.out.println("the estimated population is [" + sumDoc + "]");
        System.out.println("================================================================");
    }

    public String GetEstiRes() {
        return this.getDocList().getSum() + "";
    }

    public ArrayList<String> getKwList() {
        return kwList;
    }

    public void setKwList(ArrayList<String> kwList) {
        this.kwList = kwList;
    }

    public String getCurDocLink() {
        return curDocLink;
    }

    public void setCurDocLink(String curDocLink) {
        this.curDocLink = curDocLink;
    }

    public DocList getDocList() {
        return docList;
    }

    public void setDocList(DocList docList) {
        this.docList = docList;
    }
}

