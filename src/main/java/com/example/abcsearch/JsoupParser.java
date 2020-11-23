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

        String tittle = page.title();
        String body = page.text();
        System.out.println(body);
        Document document = new Document();
       /** final FieldType textIndexedType = new FieldType();
        textIndexedType.setStored(true);
        textIndexedType.setIndexOptions(IndexOptions.DOCS);
        textIndexedType.setTokenized(true); **/
        document.add(new Field("tittle", tittle, TextField.TYPE_STORED));
        document.add(new Field("body", body, TextField.TYPE_STORED));
        document.add(new Field("link", url, TextField.TYPE_STORED));
        return document;
        } catch (Exception e ){
            System.out.println("==========  oshibochka  ==============");
            System.out.println(e.toString());
            System.out.println("==========  oshibochka  ==============");
            return null;
        }
    }
}
