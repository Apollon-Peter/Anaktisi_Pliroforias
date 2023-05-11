package com.howtodoinjava.demo.lucene.file;
 
import java.io.IOException;
import java.nio.file.Paths;
import javax.swing.*;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.queryparser.classic.QueryParser;
 
public class ReadIndex
{
    //directory contains the lucene indexes
    private final String INDEX_DIR = "indexedFiles";
    private int floor = 0;
    private int num_prints = 0;
    private int counter = 0;
    private int ceiling = 10;
 
    public void ReadIndex(String filterName, String srch, JTextArea TextArea, boolean sorted) throws Exception
    {
        floor = 0;
        num_prints = 0;
        counter = 0;
        ceiling = 10;
        
        Searcher(filterName, srch, TextArea, sorted);
        
        if (num_prints == 0) {
        	TextArea.append("	No results were found!");
        }
    }
    
    public void nextResults(String filterName, String srch, JTextArea TextArea, boolean sorted) throws Exception
    {
        floor += 10;
        num_prints = 0;
        counter = 0;
        ceiling += 10;
        
        Searcher(filterName ,srch, TextArea, sorted);
        
        if (num_prints == 0) {
        	TextArea.append("	No more results were found!");
        }
    }
    
    private void Searcher(String filterName, String srch, JTextArea TextArea, boolean sorted) throws Exception
    {
    	//Create lucene searcher. It search over a single IndexReader.

        IndexSearcher searcher = createSearcher();
        TopDocs foundDocs;
        
        if (sorted) {
            Sort sort = new Sort(new SortField("Year", SortField.Type.INT));
        	if (filterName != "No filter") {
        		Query query = new QueryParser(filterName, new StandardAnalyzer()).parse(srch);
                foundDocs = searcher.search(query, 10000, sort);
        	} else {
                Query query = new QueryParser("contents", new StandardAnalyzer()).parse(srch);        
                foundDocs = searcher.search(query, 10000, sort);
        	}
        	
        } else {
        	if (filterName != "No filter") {
        		Query query = new QueryParser(filterName, new StandardAnalyzer()).parse(srch);
                foundDocs = searcher.search(query, 10000);
        	} else {
                Query query = new QueryParser("contents", new StandardAnalyzer()).parse(srch);        
                foundDocs = searcher.search(query, 10000);
        	}
        }
    	
    	for (ScoreDoc sd: foundDocs.scoreDocs) {
        	Document d = searcher.doc(sd.doc);
        	if (sorted) {
        		if (d.get("Year") != null) {
        			if (counter < ceiling && counter >= floor) {
                		TextArea.append("\n	" + d.get("Artist") + " | " + d.get("Title") + " | " + d.get("Album") + " | " + d.get("Year") + "\n	" + d.get("Lyrics") + "\n");
                    	num_prints ++; //used for printing "no results"
                    }
                	counter ++; //counting the num of hits
        		}
        	}
        }
    }
 
    private IndexSearcher createSearcher() throws IOException
    {
        Directory dir = FSDirectory.open(Paths.get(INDEX_DIR));
         
        //It is an interface for accessing a point-in-time view of a lucene index
        IndexReader reader = DirectoryReader.open(dir);
         
        //Index searcher
        IndexSearcher searcher = new IndexSearcher(reader);
        return searcher;
    }
}