package com.howtodoinjava.demo.lucene.file;
 
import java.io.IOException;
import java.nio.file.Path;
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
 
public class LuceneReadIndexFromFileExample
{
    //directory contains the lucene indexes
    private final String INDEX_DIR = "indexedFiles";
 
    public void ReadIndex(String filterName, String srch, JTextArea TextArea) throws Exception
    {
        //Create lucene searcher. It search over a single IndexReader.
        IndexSearcher searcher = createSearcher();
         
        //Search indexed contents using search term
        TopDocs foundDocs = searchInContent(srch, searcher);
         
        //Total found documents
        //System.out.println("Total Results :: " + foundDocs.totalHits);
         
        //Let's print out the path of files which have searched term
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
                    TextArea.append("\n	" + d.get("contents") + "\n");
                    //Here we have to increase the counter for when we count up to 10 prints (also on line 84)
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
                TextArea.append("\n	" + filterCat + " - " + d.get("contents") + "\n");
                //Here we have to increase the counter as well for when we count up to 10 prints (also on line 66)
            }
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
}