package com.example.abcsearch.logic;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
@Scope("singleton")
public class IndexFinder {
    private QueryParser parser;
    private IndexSearcher indexSearcher;
    private DirectoryReader directoryReader;

    @Autowired
    public IndexFinder(IndexSearcher indexSearcher, DirectoryReader directoryReader, QueryParser parser) {
        this.indexSearcher = indexSearcher;
        this.directoryReader = directoryReader;
        this.parser = parser;
    }


    public List<Document> find(String text) throws IOException, ParseException {
        updateDirectoryReaderIfNeed();
        Query query = parser.parse(text);
        ScoreDoc[] hits = indexSearcher.search(query,  99999999, Sort.RELEVANCE).scoreDocs;
        List<Document> list = new ArrayList<>();
        if(hits.length > 0) {
            for (int i = 0; i < hits.length; i++) {
                Document hitDoc = indexSearcher.doc(hits[i].doc);
                list.add(hitDoc);
            }
        }
        return list;
    }

    public DirectoryReader updateDirectoryReaderIfNeed() throws IOException {
        DirectoryReader newReader = DirectoryReader.openIfChanged(directoryReader);
        if (newReader != null && newReader != directoryReader) {
            directoryReader.close();
            directoryReader = newReader;
            indexSearcher = new IndexSearcher(directoryReader);
        }
        return newReader;
    }
}
