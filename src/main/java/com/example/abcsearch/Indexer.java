package com.example.abcsearch;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Indexer {
    private static Directory directory; // remove
    private static Analyzer analyzer; // remove


    static{
        analyzer = new StandardAnalyzer();//new RussianAnalyzer();
        try {
            Path indexPath = Paths.get("/Users/antonminakov/Downloads/abcsearch/src/main/resources/lucene");//Files.createTempDirectory("tempIndex");
            directory = FSDirectory.open(indexPath);

        } catch (Exception e){}
    }

    public synchronized static void index(String url) throws IOException, ParseException {
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        IndexWriter iwriter = new IndexWriter(directory, config);
        System.out.println("indexing => " + url);
        Document doc = JsoupParser.getPageForIndex(url);
        if(doc != null) {
            iwriter.addDocument(doc);
        }
        iwriter.close();

    }

    public static Set<Document> find(String text) throws IOException, ParseException {
        DirectoryReader ireader = DirectoryReader.open(directory); // try with resources
        IndexSearcher isearcher = new IndexSearcher(ireader);
        QueryParser parser = new QueryParser("body", analyzer);
        Query query = parser.parse(text);
        ScoreDoc[] hits = isearcher.search(query, 10).scoreDocs;

        Set<Document> set = new HashSet<>();
        if(hits.length > 0) {
            for (int i = 0; i < hits.length; i++) {
                Document hitDoc = isearcher.doc(hits[i].doc);
                set.add(hitDoc);
            }
        }
        ireader.close();
        return set;
    }

    public static void parseLinks(String uri) throws IOException, ParseException {
            org.jsoup.nodes.Document doc = Jsoup.connect(uri).get();
            String name = doc.title(); //извлекаем title страницы
            Elements urls = doc.select("a"); //парсим маяк "а"
          System.out.println("============= Links found = " + urls.size());
            for(Element url : urls){ //перебираем все ссылки
                    if (!url.attr("href").equals("/") & !url.attr("href").contains("#")) {
                        if (!url.attr("href").contains("://")) {

                            Thread thread = new ParsingThread(uri.substring(0, uri.indexOf("/", 9))+ url.attr("href"));
                            thread.start();
                        } else if (url.attr("href").contains("/")) {
                              Thread thread = new ParsingThread(url.attr("href")); // индексируем
                              thread.start();
                        }
                    }

            }

    }

}
