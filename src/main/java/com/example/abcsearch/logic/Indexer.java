package com.example.abcsearch.logic;

import lombok.SneakyThrows;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Scope("singleton")
public class Indexer {
    private IndexWriter indexWriter;

    @Autowired
    public Indexer(IndexWriter indexWriter) {
        this.indexWriter = indexWriter;
    }

    public void indexAndParse(String url) throws IOException {
        index(url);
        parseLinks(url, 1);
    }

    public void parseLinks(String uri , int depth) throws IOException {
        if (depth <= 3) {
            depth++;
            org.jsoup.nodes.Document doc = Jsoup.connect(uri).get();
            Elements urls = doc.select("a");
            ExecutorService executorService = Executors.newFixedThreadPool(10);
            for (Element url : urls) {
                String href = url.attr("href");
            if (!href.equals("/") & !href.contains("#") & !href.isEmpty()) { // TODO: to boolean method isHrefValid(href)
                    String correctUrl = null;
                    try {
                        if (!href.contains("://")) {
                            correctUrl = uri.substring(0, uri.indexOf("/", 9)) + href;
                        } else if (href.contains("/")) {
                            correctUrl = href;
                        }
                    } catch (Exception e){
                        System.out.println(e.getMessage() + href);
                    }
                    if(correctUrl != null) {
                        executorService.execute(new ParsingThread(correctUrl));
                        parseLinks(correctUrl, depth);
                    }
                }
            }
        }
    }

    public void index(String url) throws IOException{
        System.out.println("indexing => " + url);
        Document doc = JsoupParser.getPageForIndex(url);
        if(doc != null) {
            Term term = new Term("link", url);
            indexWriter.updateDocument(term, doc);
            indexWriter.commit();
        }
    }

    public class ParsingThread extends Thread{
        private String url;
        public ParsingThread(String url){
            this.url = url;
        }
        @SneakyThrows
        public void run(){
            index(url);
        }
    }

}
