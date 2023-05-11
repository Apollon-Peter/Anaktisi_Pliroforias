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
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import java.io.BufferedReader;
import java.io.FileReader;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;


public class WriteIndex
{	
    public void WriteIndex()
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
             
            //analyzer with the default stop words 
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
    
    public static void stemmingLyrics(String fileName, String fieldName) throws IOException {
        Analyzer analyzer1 = new StandardAnalyzer();
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] fields = line.split(",");
            String text = fields[8]; // Assuming the first field contains the text to be analyzed
            TokenStream tokenStream = analyzer1.tokenStream(fieldName, text);
            tokenStream = new PorterStemFilter(tokenStream); // Add PorterStemFilter
            CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);

            tokenStream.reset();
            while (tokenStream.incrementToken()) {
                String term = charTermAttribute.toString();
                System.out.println(term);
            }
            tokenStream.end();
            tokenStream.close();
        }
        analyzer1.close();
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
            stemmingLyrics("Eminem.csv", "Lyrics");
        }
    }
    
    static void indexDoc(IndexWriter writer, Path file, long lastModified) throws IOException {
        String name = file.toString();
    	Scanner reader = null;
    	try {
        	reader = new Scanner(new FileInputStream(name));
        	String line = reader.nextLine();
            while (reader.hasNextLine()) {
                line = reader.nextLine();
                String[] sentences = line.split(",(?=(?:[^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)");
                Document doc = new Document();
                doc.add(new TextField("Artist", sentences[1], Field.Store.YES));
                doc.add(new TextField("Title", sentences[2], Field.Store.YES));
                doc.add(new TextField("Album", sentences[3], Field.Store.YES));
                if (!sentences[4].isEmpty()) {
                	if (!sentences[4].equals("nan")) {
                		try {
                			int year = Integer.parseInt(sentences[4]);
                        	doc.add(new NumericDocValuesField("Year", year));
                        	doc.add(new StoredField("Year", year));
                		}catch (NumberFormatException e) {
                			System.out.println(e);
                		}
                		
                	}
                }
                doc.add(new TextField("Date", sentences[5], Field.Store.YES));
                doc.add(new TextField("Lyrics", sentences[6], Field.Store.YES));
                doc.add(new TextField("contents", line, Field.Store.YES));
                writer.addDocument(doc);
                }
        }
    	catch(FileNotFoundException e){
        	System.out.println("File not found");
        	System.exit(0);
        }
    }
}