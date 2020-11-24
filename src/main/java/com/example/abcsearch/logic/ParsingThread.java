package com.example.abcsearch.logic;


import lombok.SneakyThrows;
import org.apache.lucene.index.IndexWriter;


public class ParsingThread extends Thread{
    private String url;


    public ParsingThread(String url){
        this.url = url;
    }
    @SneakyThrows
    @Override
    public void run() {
        Indexer.index(url);
    }
}
