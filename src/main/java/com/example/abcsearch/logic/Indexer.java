package com.example.abcsearch.logic;

import lombok.SneakyThrows;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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

    // TODO: add executorService in separate method (?)
    public void parseLinks(String uri , int depth) throws IOException {
            if (depth <= 3) {
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
