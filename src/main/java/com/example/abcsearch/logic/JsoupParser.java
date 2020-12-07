package com.example.abcsearch.logic;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;


@Component
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
}
