package com.howtodoinjava.demo.lucene.file;
 
import java.io.IOException;
import java.nio.file.Paths;
import javax.swing.*;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
 
public class ReadIndex
{
    //directory contains the lucene indexes
    private final String INDEX_DIR = "indexedFiles";
    private int floor = 0;
    private int num_prints = 0;
    private int counter = 0;
    private int ceiling = 10;
 
    public void ReadIndex(String filterName, String srch, JTextArea TextArea) throws Exception
    {
        //Create lucene searcher. It search over a single IndexReader.
        IndexSearcher searcher = createSearcher();
         
        //Search indexed contents using search term
        TopDocs foundDocs = searchInContent(srch, searcher);
                  
        floor = 0;
        num_prints = 0;
        counter = 0;
        ceiling = 10;
        
        int filter = -1;
        if (filterName == "No filter") {
        	filter = 7;
        }else if (filterName == "Artist") {
        	filter = 2;
        }else if (filterName == "Title") {
        	filter = 3;
        }else if (filterName == "Album") {
        	filter = 4;
        }else if (filterName == "Year") {
        	filter = 5;
        }else if (filterName == "Date") {
        	filter = 6;
        }else if (filterName == "Lyrics") {
        	filter = 0;
        }
        
        boolean filters = true;
        if (filter == 7) {
        	filters = false;
        }        
        
        for (ScoreDoc sd: foundDocs.scoreDocs) {
        	Document d = searcher.doc(sd.doc);
        	int num = Integer.parseInt(d.get("number"));
            int temp = (num-(num/7)*7); //the category of the document we are checking
        	if (filters)
            {
                if (temp == filter) {
                	if (counter < ceiling && counter >= floor) {
                		TextArea.append("\n	" + d.get("artist") + ": " + d.get("song") + " \n	" + d.get("contents") + "\n");
                		num_prints ++; //used for printing "no results"
                	}
                    counter ++; //counting the num of hits
                }
            }else {
                String filterCat = "";
                if (temp == 0) {
                	filterCat = "Lyrics";
                }else if (temp == 2) {
                	filterCat = "Artist";
                }else if (temp == 3) {
                	filterCat = "Title";
                }else if (temp == 4) {
                	filterCat = "Album";
                }else if (temp == 5) {
                	filterCat = "Year";
                }else if (temp == 6) {
                	filterCat = "Date";
                }
                if (counter < ceiling && counter >= floor) {
            		TextArea.append("\n	" + d.get("artist") + ": " + d.get("song") + " \n	" + filterCat + " - " + d.get("contents") + "\n");
                	num_prints ++; //used for printing "no results"
                }
            	counter ++; //counting the num of hits
            }
        }
        
        if (num_prints == 0) {
        	TextArea.append("	No results were found!");
        }
    }
     
    private TopDocs searchInContent(String textToFind, IndexSearcher searcher) throws Exception
    {
        //Create search query
        QueryParser qp = new QueryParser("contents", new StandardAnalyzer());
        Query query = qp.parse(textToFind);
         
        //search the index
        TopDocs hits = searcher.search(query, 1000);
        return hits;
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
    
    public void nextResults(String filterName, String srch, JTextArea TextArea) throws Exception
    {
        //Create lucene searcher. It search over a single IndexReader.
        IndexSearcher searcher = createSearcher();
         
        //Search indexed contents using search term
        TopDocs foundDocs = searchInContent(srch, searcher);
                
        floor += 10;
        num_prints = 0;
        counter = 0;
        ceiling += 10;   
        
        int filter = -1;
        if (filterName == "No filter") {
        	filter = 7;
        }else if (filterName == "Artist") {
        	filter = 2;
        }else if (filterName == "Title") {
        	filter = 3;
        }else if (filterName == "Album") {
        	filter = 4;
        }else if (filterName == "Year") {
        	filter = 5;
        }else if (filterName == "Date") {
        	filter = 6;
        }else if (filterName == "Lyrics") {
        	filter = 0;
        }
        
        boolean filters = true;
        if (filter == 7) {
        	filters = false;
        }
                
        for (ScoreDoc sd: foundDocs.scoreDocs) {
        	Document d = searcher.doc(sd.doc);
        	int num = Integer.parseInt(d.get("number"));
            int temp = (num-(num/7)*7);
        	if (filters)
            {
                if (temp == filter) {
                	if (counter < ceiling && counter >= floor) {
                		TextArea.append("\n	" + d.get("artist") + ": " + d.get("song") + " \n	" + d.get("contents") + "\n");
                		num_prints ++;
                	}
                    counter ++;
                }
            }else {
                String filterCat = "";
                if (temp == 0) {
                	filterCat = "Lyrics";
                }else if (temp == 2) {
                	filterCat = "Artist";
                }else if (temp == 3) {
                	filterCat = "Title";
                }else if (temp == 4) {
                	filterCat = "Album";
                }else if (temp == 5) {
                	filterCat = "Year";
                }else if (temp == 6) {
                	filterCat = "Date";
                }
                if (counter < ceiling && counter >= floor) {
            		TextArea.append("\n	" + d.get("artist") + ": " + d.get("song") + " \n	" + filterCat + " - " + d.get("contents") + "\n");
                	num_prints ++;
                }
                counter ++;
            }
        }
        if (num_prints == 0) {
        	TextArea.append("	No more results were found!");
        }
    }
}