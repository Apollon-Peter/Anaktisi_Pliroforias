package com.howtodoinjava.demo.lucene.file;
 
import java.io.IOException;
import java.nio.file.Paths;
import javax.swing.*;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.NumericDocValuesField;
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
    
    // We use this function whenever we press the "Search" button
    public void ReadIndex(String filterName, String srch, JTextArea TextArea, boolean sorted) throws Exception
    {
    	// With the following parameters we guarantee we only get the first 10 results of our search and whether or not we had any results at all
        floor = 0;
        num_prints = 0;
        counter = 0;
        ceiling = 10;
        
        Searcher(filterName, srch, TextArea, sorted);
        
        if (num_prints == 0) {
        	TextArea.append("	No results were found!");
        }
    }
    
    // We use this function whenever we press the "Next 10 results" button
    public void nextResults(String filterName, String srch, JTextArea TextArea, boolean sorted) throws Exception
    {
    	// With the following parameters we guarantee we get the next 10 results of our search and whether or not we had any results at all
        floor += 10;
        num_prints = 0;
        counter = 0;
        ceiling += 10;
        
        Searcher(filterName ,srch, TextArea, sorted);
        
        if (num_prints == 0) {
        	TextArea.append("	No more results were found!");
        }
    }
    
    // Here we apply stemming on the user's input
    public String stemmingUser(String usersrch) throws IOException {
        Analyzer analyzer = new StandardAnalyzer();
        String term = "";
        String[] fields = usersrch.split(" -$&#@[,\\s!]+"); // Split the input based on these characters
        for(String text : fields) { // Stemming each word the user inputed 
        	TokenStream tokenStream = analyzer.tokenStream(null, text);
        	tokenStream = new PorterStemFilter(tokenStream); // Add PorterStemFilter
            CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
            tokenStream.reset();
            while (tokenStream.incrementToken()) {
                term += charTermAttribute.toString() + " "; // Save it in the String term
            }
            tokenStream.end();
            tokenStream.close();
            analyzer.close();
        }
        return term; // Return the stemmed input
    }

    
    // With this function we search our indexed files by also applying the necessary filters
    private void Searcher(String filterName, String srch, JTextArea TextArea, boolean sorted) throws Exception
    {
    	//Create lucene searcher. It search over a single IndexReader.

        IndexSearcher searcher = createSearcher();
        TopDocs foundDocs;
        
        if (sorted) { // If "Sort by Year" is On
            Sort sort = new Sort(new SortField("Year", SortField.Type.INT)); // We create the sort object that is going to sort the results 
        	if (filterName != "No filter") { // If there are filters
        		if(filterName != "Year") { // If the filter is not "Year"
        			Query query = new QueryParser(filterName, new StandardAnalyzer()).parse(srch);
                    foundDocs = searcher.search(query, 10000, sort);
        		}else { // If the filter is "Year"
        			String[] date = srch.split(" ");
        			srch = "";
        			for (String i : date) {
        				srch += i;
        			}
        			Query query = NumericDocValuesField.newSlowExactQuery("Year", Long.parseLong(srch));
        			foundDocs = searcher.search(query, 10000, sort);
        		}
        	} else { // If there are no filters
                Query query = new QueryParser("contents", new StandardAnalyzer()).parse(srch);        
                foundDocs = searcher.search(query, 10000, sort);
        	}
        	
        } else { // If "Sort by Year" is Off
        	if (filterName != "No filter") { // If there are filters
        		if(filterName != "Year") { // If the filter is not "Year"
        			Query query = new QueryParser(filterName, new StandardAnalyzer()).parse(srch);
                    foundDocs = searcher.search(query, 10000);
        		}else { // If the filter is "Year"
        			String[] date = srch.split(" ");
        			srch = "";
        			for (String i : date) {
        				srch += i;
        			}
        			Query query = NumericDocValuesField.newSlowExactQuery("Year", Long.parseLong(srch));
        			foundDocs = searcher.search(query, 10000);
        		}
        	} else { // If there are no filters
                Query query = new QueryParser("contents", new StandardAnalyzer()).parse(srch);        
                foundDocs = searcher.search(query, 10000);
        	}
        } 
        // In every case above, we create a query object in which we input the field we want to search in
    	// and we also create a sort object in some cases. The query and the sort objects are then used as parameters for our search method.
        
    	for (ScoreDoc sd: foundDocs.scoreDocs) { // For each hit
        	Document d = searcher.doc(sd.doc);
        	if (sorted) {
        		if (d.get("Year") != null) {
        			if (counter < ceiling && counter >= floor) {
                		TextArea.append("\n	" + d.get("Artist") + " | " + d.get("PTitle") + " | " + d.get("PAlbum") + " | " + d.get("Year") + "\n	" + d.get("PLyrics") + "\n");
                    	num_prints ++; //used for printing "no results"
                    }
                	counter ++; //counting the num of hits
        		}
        	}else {
        		if (counter < ceiling && counter >= floor) {
            		TextArea.append("\n	" + d.get("Artist") + " | " + d.get("PTitle") + " | " + d.get("PAlbum") + " | " + d.get("Year") + "\n	" + d.get("PLyrics") + "\n");
                	num_prints ++; //used for printing "no results"
                }
            	counter ++; //counting the num of hits
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