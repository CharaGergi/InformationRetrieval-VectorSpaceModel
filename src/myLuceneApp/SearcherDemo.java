package myLuceneApp;

// tested for lucene 7.7.2 and jdk13
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.store.FSDirectory;
import txtparsing.QueryParsing;


public class SearcherDemo {

    String queryFile = "C://Users//Chara//Desktop//Ανάκτηση//QRY.txt";

    public SearcherDemo(){
        try{
            String indexLocation = ("index"); //define where the index is stored
            String field = "contents"; //define which field will be searched

            //Access the index using indexReaderFSDirectory.open(Paths.get(index))
            IndexReader indexReader = DirectoryReader.open(FSDirectory.open(Paths.get(indexLocation))); //IndexReader is an abstract class, providing an interface for accessing an index.
            IndexSearcher indexSearcher = new IndexSearcher(indexReader); //Creates a searcher searching the provided index, Implements search over a single IndexReader.
            indexSearcher.setSimilarity(new ClassicSimilarity());
            
            //Search the index using indexSearcher
            search(indexSearcher, field);
            
            //Close indexReader
            indexReader.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    
    /**
     * Searches the index given a specific user query.
     */
    private void search(IndexSearcher indexSearcher, String field){
        try{
            // define which analyzer to use for the normalization of user's query
            Analyzer analyzer = new EnglishAnalyzer();
            
            // create a query parser on the field "contents"
            QueryParser parser = new QueryParser(field, analyzer);
            String star ="*";
            String apostrophe = "\'";
            String dots = ":";
            String questionMark ="?";

            parser.escape(star);
            parser.escape(apostrophe);
            parser.escape(dots);
            parser.escape(questionMark);

            //Read the file that contains the queries
            List<String[]> queries = QueryParsing.parse(queryFile);
            int i=0;
            //
            while(i<queries.size()){

                // parse the query according to QueryParser
                Query query = parser.parse(queries.get(i)[1]);

                System.out.println("Searching for: " + query.toString(field));
                i++;
                
                // search the index using the indexSearcher
                TopDocs results = indexSearcher.search(query, 4);
                ScoreDoc[] hits = results.scoreDocs;
                long numTotalHits = results.totalHits;
                System.out.println(numTotalHits + " total matching documents");

                //display results
                for(int k=0; k<hits.length; k++){
                    Document hitDoc = indexSearcher.doc(hits[k].doc);
                    //System.out.println("\tScore "+hits[k].score +"\ttitle:"+hitDoc.get("title")+"\tauthor:"+hitDoc.get("author")+"\tbody:"+hitDoc.get("body"));
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Initialize a SearcherDemo
     */
    public static void main(String[] args){
        SearcherDemo searcherDemo = new SearcherDemo();
    }
}
