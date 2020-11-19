package com.example.abcsearch;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.jsoup.Jsoup;

import java.io.IOException;

public class JsoupParser {
    public static Document getPageForIndex(String url) throws IOException {
        org.jsoup.nodes.Document page = Jsoup.connect(url).get();
        String tittle = page.title();
        String body = page.text();
        Document document = new Document();
        document.add(new Field("tittle", tittle, TextField.TYPE_STORED));
        document.add(new Field("body", body, TextField.TYPE_STORED));
        document.add(new Field("link", url, TextField.TYPE_STORED));
        return document;
    }
}
