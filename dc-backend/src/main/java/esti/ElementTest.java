package esti;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class ElementTest {

    public static String getContent(String link, String content_xpath) throws IOException, InterruptedException {
        Document doc = BaseUtils.GetDoc(link);
        Elements contents=doc.getElementsByClass("content");
        String passage=contents.text();
        return passage;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        String testURL="https://www.henan.gov.cn/2014/12-18/333250.html";
        String content= ElementTest.getContent(testURL,"content");
        System.out.println(content);
    }
}
