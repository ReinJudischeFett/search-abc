package com.example.abcsearch.logic;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;


import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Indexer {
    private static Directory directory;
    private static Analyzer analyzer;
    private static IndexWriter indexWriter;
    private static final String path ="/Users/antonminakov/Downloads/abcsearch/src/main/resources/lucene";
    static {
        try {
            analyzer = new StandardAnalyzer();
            Path indexPath = Paths.get(path);
            directory = FSDirectory.open(indexPath);
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
            indexWriter = new IndexWriter(directory, config);
            indexWriter.forceMerge(20);
        } catch (Exception e) {}
    }

    public static void index(String url) throws IOException{
        System.out.println("indexing => " + url);
        Document doc = JsoupParser.getPageForIndex(url);
        if(doc != null) {
            Term term = new Term("link", url);
            indexWriter.updateDocument(term, doc);
            indexWriter.commit();

        }
    }



}
