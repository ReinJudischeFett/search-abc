package com.example.abcsearch.logic;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JsoupParser {
    public static Document getPageForIndex(String url)  {
        org.jsoup.nodes.Document page = null;
        try {
             page = Jsoup.connect(url).get();
        } catch (Exception e ){
            return null;
        }
        Document document = new Document();
        document.add(new StringField("link", url, Field.Store.YES));
        document.add(new Field("tittle", page.title(), TextField.TYPE_STORED));
        document.add(new Field("body", page.text(), TextField.TYPE_STORED));
        return document;
    }
    public static void parseLinks(String uri , int depth) throws IOException {
        if(depth <= 3) {
            depth++;
            org.jsoup.nodes.Document doc = Jsoup.connect(uri).get();
            Elements urls = doc.select("a");
            ExecutorService executorService = Executors.newFixedThreadPool(10);
            for (Element url : urls) {
                if (!url.attr("href").equals("/") & !url.attr("href").contains("#")) {
                    String correctUrl = null;
                    if (!url.attr("href").contains("://")) {
                         correctUrl = uri.substring(0, uri.indexOf("/", 9)) + url.attr("href");
                        executorService.execute(new ParsingThread(correctUrl));
                    } else if (url.attr("href").contains("/")) {
                        correctUrl = url.attr("href");
                        executorService.execute(new ParsingThread(correctUrl));
                    }
                    parseLinks(correctUrl, depth);
                }
            }
        }
    }
}
