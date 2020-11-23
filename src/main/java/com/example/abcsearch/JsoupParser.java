package com.example.abcsearch;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.jsoup.Jsoup;

import java.io.IOException;

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
}
