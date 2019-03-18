package esti;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.recognition.impl.StopRecognition;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseUtils {

    public static int getPagesInfo(String pagesStr) {
        Pattern pattern = Pattern.compile("[0-9]+ ?(项|条)");
        Matcher matcher = pattern.matcher(pagesStr);
        matcher.find();
        String info = matcher.group();
        pattern = Pattern.compile("[0-9]+");
        matcher = pattern.matcher(info);
        matcher.find();
        return Integer.parseInt(matcher.group());
    }


    public static Document GetDoc(String url) throws IOException, InterruptedException {
        Document doc;
        String[] ua = {
//                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36 OPR/26.0.1656.60",
//                "Opera/8.0 (Windows NT 5.1; U; en)",
//                "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:34.0) Gecko/20100101 Firefox/34.0",
//                "Mozilla/5.0 (X11; U; Linux x86_64; zh-CN; rv:1.9.2.10) Gecko/20100922 Ubuntu/10.10 (maverick) Firefox/3.6.10",
//                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534.57.2 (KHTML, like Gecko) Version/5.1.7 Safari/534.57.2",
//                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36",
////                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.101 Safari/537.36",
//                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.11 (KHTML, like Gecko) Chrome/20.0.1132.11 TaoBrowser/2.0 ",
//                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; QQDownload 732; .NET4.0C; .NET4.0E; LBBROWSER)",
//                "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E; QQBrowser/7.0.3698.400)",
//                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; QQDownload 732; .NET4.0C; .NET4.0E)",
//                "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.84 Safari/535.11 SE 2.X MetaSr 1.0",
//                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Maxthon/4.4.3.4000 Chrome/30.0.1599.101 Safari/537.36",
//                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 UBrowser/4.0.3214.0 Safari/537.36",
//                "Mozilla/5.0 (iPhone; U; CPU iPhone OS 4_3_3 like Mac OS X; en-us) AppleWebKit/533.17.9 (KHTML, like Gecko) Version/5.0.2 Mobile/8J2 Safari/6533.18.5",
//                "Mozilla/5.0 (iPod; U; CPU iPhone OS 4_3_3 like Mac OS X; en-us) AppleWebKit/533.17.9 (KHTML, like Gecko) Version/5.0.2 Mobile/8J2 Safari/6533.18.5",
//                "Mozilla/5.0 (iPad; U; CPU OS 4_2_1 like Mac OS X; zh-cn) AppleWebKit/533.17.9 (KHTML, like Gecko) Version/5.0.2 Mobile/8C148 Safari/6533.18.5",
//                "Mozilla/5.0 (Linux; U; Android 2.2.1; zh-cn; HTC_Wildfire_A3333 Build/FRG83D) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1",
//                "Mozilla/5.0 (Linux; U; Android 2.3.7; en-us; Nexus One Build/FRF91) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1",
//                "Opera/9.80 (Android 2.3.4; Linux; Opera Mobi/build-1107180945; U; en-GB) Presto/2.8.149 Version/11.10",
//                "Mozilla/5.0 (hp-tablet; Linux; hpwOS/3.0.0; U; en-US) AppleWebKit/534.6 (KHTML, like Gecko) wOSBrowser/233.70 Safari/534.6 TouchPad/1.0",
//                "Mozilla/5.0 (SymbianOS/9.4; Series60/5.0 NokiaN97-1/20.0.019; Profile/MIDP-2.1 Configuration/CLDC-1.1) AppleWebKit/525 (KHTML, like Gecko) BrowserNG/7.1.18124",
//                "Openwave/ UCWEB7.0.2.37/28/999",
//                "Mozilla/4.0 (compatible; MSIE 6.0; ) Opera/UCWEB7.0.2.37/28/999",
//                "Mozilla/5.0 (compatible; MSIE 9.0; Windows Phone OS 7.5; Trident/5.0; IEMobile/9.0; HTC; Titan)",
//                "Mozilla/5.0 (Windows NT 6.1; W…) Gecko/20100101 Firefox/60.0",

                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:57.0) Gecko/20100101 Firefox/57.0",
                "Mozilla/5.0(WindowsNT6.1;rv:31.0)Gecko/20100101Firefox/31.0",
                "Mozilla",
                "HTTP Banner Detection(security.ipip.net)"
        };

        String uaStr = "Mozilla/5.0 (Windows NT 6.1; W…) Gecko/20100101 Firefox/60.0";

        String uaRandStr = ua[(int) (ua.length * Math.random())];
//        System.out.println(uaRandStr + " ");
//        Thread.sleep(4000L);

        doc = Jsoup.connect(url).ignoreContentType(true).userAgent(uaRandStr).timeout(5000).get();
//        System.out.println(doc.title());
        return doc;
//			Connection conn=null;
//			conn = Jsoup.connect(url);
//			conn.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
//			conn.header("Accept-Encoding", "gzip, deflate, sdch");
//			conn.header("Accept-Language", "zh-CN,zh;q=0.8");
//			conn.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
//			doc=conn.get();
//        doc = Jsoup.connect(url).ignoreContentType(true).userAgent(
//                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31").get();
    }


    public static HashSet<String> Wash(String text) throws IOException {
        text = text.replaceAll("[\\p{P}+~$`^=|<>～｀＄＾＋＝｜＜＞￥×]", "");
        text = text.replaceAll("[^(\\u4e00-\\u9fa5)]", "");
        StopRecognition fitler = new StopRecognition();
        List<String> filterWords = new ArrayList<>();
        String filePath = "webapp\\WEB-INF\\stopwords.txt";
        File f = new File(filePath);
        FileInputStream fileInputStream = new FileInputStream(f);
        //读入停用词文件
        BufferedReader StopWordFileBr = new BufferedReader(new InputStreamReader(fileInputStream));
        String stopWord = null;

        for (; (stopWord = StopWordFileBr.readLine()) != null; ) {
            filterWords.add(stopWord.trim());
        }
        StopWordFileBr.close();

        fitler.insertStopWords(filterWords);

        Result result = ToAnalysis.parse(QueryParser.escape(text)).recognition(fitler); // 分词结果的一个封装，主要是一个List<Term>的terms
        List<org.ansj.domain.Term> terms = result.getTerms(); // 拿到terms
        Iterator<Term> iterator = terms.iterator();
        HashSet<String> hSet = new HashSet<>();
        while (iterator.hasNext()) {
            String term = iterator.next().getName();
            hSet.add(term.trim());
        }
        return hSet;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        for (int i = 0; i < 1000; i++) {
            Document aDoc = GetDoc("http://www.zhaoan.gov.cn/cms/siteresource/search.shtml?searchSiteId=60427348114130001&siteId=60427348114130001&pageName=quickSiteSearch&page=2&key=书记");
            Elements links = aDoc.getElementsByClass("wz-list");
            System.out.println(links.first().children().first().text());
            Thread.sleep(3000L);
        }

    }


}
