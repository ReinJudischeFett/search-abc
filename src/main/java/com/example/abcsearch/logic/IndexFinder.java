package com.example.abcsearch.logic;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.print.Doc;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


public class IndexFinder {
    private static QueryParser parser;
    public static IndexSearcher indexSearcher;
    public static DirectoryReader directoryReader;

    static{
        try {
        Analyzer analyzer = new StandardAnalyzer();
        Path indexPath = Paths.get("/Users/antonminakov/Downloads/abcsearch/src/main/resources/lucene");//Files.createTempDirectory("tempIndex");
        Directory directory = FSDirectory.open(indexPath);
        directoryReader = DirectoryReader.open(directory);
        indexSearcher = new IndexSearcher(directoryReader);
        parser = new QueryParser("body", analyzer);
        } catch (IOException e) { e.printStackTrace(); }
    }

    public static Page<Document> find(String text,Pageable pageable) throws IOException, ParseException {
        DirectoryReader newReader = DirectoryReader.openIfChanged(directoryReader);
        if (newReader != null && newReader != directoryReader) {
            directoryReader.close();
            directoryReader = newReader;
            indexSearcher = new IndexSearcher(directoryReader);
        }
        Query query = parser.parse(text);

        ScoreDoc[] hits = indexSearcher.search(query,  99999999, Sort.RELEVANCE).scoreDocs;
        List<Document> list = new ArrayList<>();

        if(hits.length > 0) {
            for (int i = 0; i < hits.length; i++) {
                Document hitDoc = indexSearcher.doc(hits[i].doc);
                list.add(hitDoc);
            }
        }
        Page<Document>  page;
        if(!list.isEmpty()) {
            page = new PageImpl<>(list.subList((int) pageable.getOffset(), (int) pageable.getOffset() + pageable.getPageSize()), pageable, list.size());
        } else {
            page = new PageImpl<>(list);

        }
        return page;

    }
}
