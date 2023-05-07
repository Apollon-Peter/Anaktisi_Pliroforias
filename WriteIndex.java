package com.howtodoinjava.demo.lucene.file;

import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.util.Scanner;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class WriteIndex
{
    public static void WriteIndex()
    {
        //Input folder
        String docsPath = "inputFiles";
         
        //Output folder
        String indexPath = "indexedFiles";
 
        //Input Path Variable
        final Path docDir = Paths.get(docsPath);
 
        try
        {
            //org.apache.lucene.store.Directory instance
            Directory dir = FSDirectory.open( Paths.get(indexPath) );
             
            //analyzer with the default stop words && stemming
            CharArraySet stopWords = EnglishAnalyzer.getDefaultStopSet();
            StandardAnalyzer analyzer = new StandardAnalyzer(stopWords);
               
            //IndexWriter Configuration
            IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
            iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
             
            //IndexWriter writes new index files to the directory
            IndexWriter writer = new IndexWriter(dir, iwc);
             
            //Its recursive method to iterate all files and directories
            indexDocs(writer, docDir);
 
            writer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
     
    static void indexDocs(final IndexWriter writer, Path path) throws IOException
    {
        //Directory?
        if (Files.isDirectory(path))
        {
            //Iterate directory
            Files.walkFileTree(path, new SimpleFileVisitor<Path>()
            {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
                {
                    try
                    {
                        //Index this file
                        indexDoc(writer, file, attrs.lastModifiedTime().toMillis());
                    }
                    catch (IOException ioe)
                    {
                        ioe.printStackTrace();
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        }
        else
        {
            //Index this file
            indexDoc(writer, path, Files.getLastModifiedTime(path).toMillis());
        }
    }
    
    static void indexDoc(IndexWriter writer, Path file, long lastModified) throws IOException {
        String name = file.toString();
    	Scanner reader = null;
    	try {
        	reader = new Scanner(new FileInputStream(name));
            int sentenceCount = 1;
            while (reader.hasNextLine()) {
            	int pos = 1;
                String line = reader.nextLine();
                String[] sentences = line.split(",(?=(?:[^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)");
                for (String sentence : sentences) {
                	String strSentence = Integer.toString(sentenceCount);
                    //Create lucene Document
                    Document doc = new Document();
                    doc.add(new TextField("contents", sentence, Store.YES));
                    doc.add(new StringField("number", strSentence, Field.Store.YES));
                    if (sentenceCount > 7) {
                    	if (pos == 2) {
                        	doc.add(new TextField("song", sentences[pos], Store.YES));
                        	doc.add(new TextField("artist", "", Store.YES));
                        }else if (pos == 3) {
                        	doc.add(new TextField("song", "", Store.YES));
                        	doc.add(new TextField("artist", sentences[pos-2], Store.YES));
                        }else if (pos == 4) {
                        	doc.add(new TextField("song", "", Store.YES));
                        	doc.add(new TextField("artist", sentences[pos-3], Store.YES));
                        }else if (pos == 5) {
                        	doc.add(new TextField("song", sentences[pos-3], Store.YES));
                        	doc.add(new TextField("artist", sentences[pos-4], Store.YES));
                        }else if (pos == 6) {
                        	doc.add(new TextField("song", sentences[pos-4], Store.YES));
                        	doc.add(new TextField("artist", sentences[pos-5], Store.YES));
                        }else if (pos == 7) {
                        	doc.add(new TextField("song", sentences[pos-5], Store.YES));
                        	doc.add(new TextField("artist", sentences[pos-6], Store.YES));
                        }
                    }
                    writer.addDocument(doc);
                    sentenceCount++;
                    pos ++;
                }
            }
        }
    	catch(FileNotFoundException e){
        	System.out.println("File not found");
        	System.exit(0);
        }
    }
}