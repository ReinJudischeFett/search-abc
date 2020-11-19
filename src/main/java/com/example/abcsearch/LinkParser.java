package com.example.abcsearch;

import org.apache.lucene.queryparser.classic.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import static com.example.abcsearch.Indexer.index;

public class LinkParser extends Thread{
    private String url;
    private int depth;

    public LinkParser(String url){
       // this.depth = depth;
        this.url = url;
    }
    @Override
    public void run() {
        Document doc;
        try {
            doc = Jsoup.connect(url).get();
            String name = doc.title(); //извлекаем title страницы

            Elements urls = doc.select("a"); //парсим маяк "а"
            for(Element url : urls){ //перебираем все ссылки
                //... и вытаскиваем их название...

                if(!url.attr("href").contains("://")){
                    System.out.println(url + url.attr("href"));
                    index(url + url.attr("href")); // индексируем
                    //   linkParser(link + url.attr("href"), depth );  // отправляем на парсинг
                }
                else {
                    System.out.println(url.attr("href"));
                    index(url.attr("href")); // индексируем
                    //     linkParser(url.attr("href") , depth); // отправляем на парсинг
                }
            }
        } catch (IOException | ParseException e) {
        }
    }
}
