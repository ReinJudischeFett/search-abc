package com.example.abcsearch.logic;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.ConcurrentMergeScheduler;
import org.apache.lucene.index.IndexNotFoundException;
import org.apache.lucene.index.IndexWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.core.task.support.ExecutorServiceAdapter;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class JsoupParser {
    public static Document getPageForIndex(String url) throws IOException {
        org.jsoup.nodes.Document page = null;
        try {
             page = Jsoup.connect(url).get();
        } catch (Exception e ){
            System.out.println("==========  oshibochka  ==============");
            System.out.println(e.toString());
            System.out.println("==========  oshibochka  ==============");
            return null;
        }
        Document document = new Document();
        document.add(new Field("tittle", page.title(), TextField.TYPE_STORED));
        document.add(new Field("body", page.text(), TextField.TYPE_STORED));
        document.add(new Field("link", url, TextField.TYPE_STORED));
        return document;
    }
    public static void parseLinks(String uri) throws IOException, InterruptedException {
        org.jsoup.nodes.Document doc = Jsoup.connect(uri).get();
        Elements urls = doc.select("a");
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for(Element url : urls){
            if (!url.attr("href").equals("/") & !url.attr("href").contains("#")) {
                if (!url.attr("href").contains("://")) {
                   executorService.execute(new ParsingThread(uri.substring(0, uri.indexOf("/", 9))+ url.attr("href")));
                } else if (url.attr("href").contains("/")) {
                   executorService.execute(new ParsingThread(url.attr("href")));

                }
            }
        }


    }
}
