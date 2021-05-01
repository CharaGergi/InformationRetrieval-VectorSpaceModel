package myLuceneApp;

// tested for lucene 7.7.2 and jdk13f
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import txtparsing.*;

public class IndexerDemo {
    
    /**
     * Configures IndexWriter.
     * Creates a lucene's inverted index.
     *
     */
    public IndexerDemo() throws Exception{
        
        String txtfile =  "C://Users//Chara//Desktop//Ανάκτηση//CISI.ALL"; //txt file to be parsed and indexed, it contains one document per line
        String indexLocation = ("index"); //define were to store the index        
        
        Date start = new Date();
        try {
            System.out.println("Indexing to directory '" + indexLocation + "'...");
            
            Directory dir = FSDirectory.open(Paths.get(indexLocation));
            // define which analyzer to use for the normalization of documents
            Analyzer analyzer = new EnglishAnalyzer();
            // define retrieval model 
            Similarity similarity = new ClassicSimilarity();
            // configure IndexWriter
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            config.setSimilarity(similarity);

            // Create a new index in the directory, removing any
            // previously indexed documents:
            config.setOpenMode(OpenMode.CREATE);

            // create the IndexWriter with the configuration as above 
            IndexWriter indexWriter = new IndexWriter(dir, config);
            
            // parse txt document using TXT parser and index it
            List<MyDoc> docs = TXTParsing.parse(txtfile);
            for (MyDoc doc : docs){
                //System.out.println(doc.getAuthor());
                indexDoc(indexWriter, doc);
            }
            
            indexWriter.close();
            
            Date end = new Date();
            System.out.println(end.getTime() - start.getTime() + " total milliseconds");
            
        } catch (IOException e) {
            System.out.println(" caught a " + e.getClass() +
                    "\n with message: " + e.getMessage());
        }
    }
    
    /**
     * Creates a Document by adding Fields in it and 
     * indexes the Document with the IndexWriter
     *
     * @param indexWriter the indexWriter that will index Documents
     * @param mydoc the document to be indexed
     *
     */
    private void indexDoc(IndexWriter indexWriter, MyDoc mydoc){
        
        try {
            
            // make a new, empty document
            Document doc = new Document();

            // create the fields of the document and add them to the document
            StoredField id = new StoredField("id", mydoc.getId());
            doc.add(id);

            StoredField title = new StoredField("title", mydoc.getTitle());
            doc.add(title);
            //System.out.println("TITLE " + title);
            StoredField author = new StoredField("author", mydoc.getAuthor());
            //System.out.println("AUTHOR " + author);
            doc.add(author);
            StoredField body = new StoredField("body", mydoc.getBody());
            doc.add(body);
            //System.out.println("BODY " + body);
//            StoredField citation = new StoredField("citation",arrayToString(mydoc.getCitation()));
//            doc.add(citation);
//            System.out.println("CITATION  " + citation);
            String fullSearchableText = mydoc.getTitle() + " " + mydoc.getAuthor() + " " + mydoc.getBody();


            TextField contents = new TextField("contents", fullSearchableText, Field.Store.NO);
            doc.add(contents);
            //System.out.println(contents);

            if (indexWriter.getConfig().getOpenMode() == OpenMode.CREATE ) {
                // New index, so we just add the document (no old document can be there):
                //System.out.println("adding " + doc);
                indexWriter.addDocument(doc);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        
    }

//    public String arrayToString(ArrayList<String[]> array){
//        String stringifiedArray = "";
//        for(int i=1; i<array.size(); i++){
//            stringifiedArray  = stringifiedArray + array.get(i)[0];
//        }
//        return  stringifiedArray;
//    }
    
    /**
     * Initializes an IndexerDemo
     */
    public static void main(String[] args) {
        try {
            IndexerDemo indexerDemo = new IndexerDemo();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
}
