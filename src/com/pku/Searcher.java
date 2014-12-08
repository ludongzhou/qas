package com.pku;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import javax.print.Doc;
import java.io.*;
import java.util.List;

/**
 * Created by ember on 12/8/14.
 */
public class Searcher {
    static Analyzer analyzer;
    static Directory directory;
    public static void createIndex()
    {
        try {
            analyzer = new StandardAnalyzer(Version.LATEST);
            // Store the index in memory:
            directory = new RAMDirectory();
            // To store an index on disk, use this instead:
            //Directory directory = FSDirectory.open(new File("./1002"));
            IndexWriterConfig config = new IndexWriterConfig(Version.LATEST, analyzer);
            IndexWriter iwriter = new IndexWriter(directory, config);
            //File test = new File("test.txt");
            List<Document> list = XmlHandler.parserXml("/Users/ember/Downloads/test.xml");
            for (Document doc : list)
                iwriter.addDocument(doc);
            iwriter.close();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }


    }
    public static void search(String key)
    {
        try {
            // Now search the index:
            DirectoryReader ireader = DirectoryReader.open(directory);
            IndexSearcher isearcher = new IndexSearcher(ireader);
            // Parse a simple query that searches for "text":
            QueryParser parser = new QueryParser(Version.LATEST, "text", analyzer);
            Query query = parser.parse(key);
            ScoreDoc[] hits = isearcher.search(query, null, 1000).scoreDocs;
            // Iterate through the results:
            SimpleHTMLFormatter simpleHtmlFormatter = new SimpleHTMLFormatter("", "");//设定高亮显示的格式，也就是对高亮显示的词组加上前缀后缀
            Highlighter highlighter = new Highlighter(simpleHtmlFormatter, new QueryScorer(query));
            highlighter.setTextFragmenter(new SimpleFragmenter(100));
            for (int i = 0; i < hits.length; i++) {
                Document hitDoc = isearcher.doc(hits[i].doc);
                //System.out.println(hitDoc.get("text"));
                TokenStream tokenStream = analyzer.tokenStream("", new StringReader(hitDoc.get("text")));
                String str = highlighter.getBestFragment(tokenStream, hitDoc.get("text"));
                System.out.println(str);
                System.out.println("score:" + hits[i].score);
                System.out.println("================================================");
            }
            ireader.close();
            directory.close();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}
