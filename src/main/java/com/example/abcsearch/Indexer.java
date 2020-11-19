package com.example.abcsearch;

import ch.qos.logback.core.encoder.EchoEncoder;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class Indexer {
    private static Directory directory;
    private static Analyzer analyzer;
    static{
        analyzer = new EnglishAnalyzer();
        try {
            Path indexPath = Files.createTempDirectory("tempIndex");
            directory = FSDirectory.open(indexPath);
        } catch (Exception e){}
    }
    // мб перенести все читалки-писалки в константы

    public static void index(String url) throws IOException, ParseException {
      //  analyzer = new StandardAnalyzer();
       // Path indexPath = Files.createTempDirectory("tempIndex");
      //  directory = FSDirectory.open(indexPath);
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter iwriter = new IndexWriter(directory, config);
        Document doc = JsoupParser.getPageForIndex(url);
       // String text = "This is the text to be indexed.";
       // doc.add(new Field("fieldname", text, TextField.TYPE_STORED));
        iwriter.addDocument(doc);
        iwriter.close();
    }

    public static Set<Document> find(String text) throws IOException, ParseException {
// Now search the index:
        DirectoryReader ireader = DirectoryReader.open(directory); // try with resources
        IndexSearcher isearcher = new IndexSearcher(ireader);
        // Parse a simple query that searches for "text":
        QueryParser parser = new QueryParser("body", analyzer);
        Query query = parser.parse(text);
        ScoreDoc[] hits = isearcher.search(query, 10).scoreDocs;

        // Iterate through the results:
        Set<Document> set = new HashSet<>();
        if(hits.length > 0) {
            for (int i = 0; i < hits.length; i++) {
                Document hitDoc = isearcher.doc(hits[i].doc);
                set.add(hitDoc);
            }
        }
        ireader.close();
        //directory.close();

        return set;


    }

}
