package com.example.abcsearch.configs;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class LuceneConfig {
    private static final String PATH ="/Users/antonminakov/Downloads/abcsearch/src/main/resources/lucene";

    @Bean
    public IndexWriter indexWriter() throws IOException {
        Analyzer analyzer = new StandardAnalyzer();
        Path indexPath = Paths.get(PATH);
        Directory directory = FSDirectory.open(indexPath);
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        IndexWriter indexWriter = new IndexWriter(directory, config);
        indexWriter.forceMerge(20);
        return indexWriter;
    }

}
