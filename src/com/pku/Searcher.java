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
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import javax.print.Doc;
import java.io.*;
import java.util.ArrayList;
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
            //Directory directory = FSDirectory.open(new File("./index"));
            IndexWriterConfig config = new IndexWriterConfig(Version.LATEST, analyzer);
            IndexWriter iwriter = new IndexWriter(directory, config);
            //File test = new File("test.txt");
            System.out.println("正在解析XML文件");
            File dir = new File("/Users/ludongzhou/Desktop/new");
            String wikis[] = dir.list();
            int length = wikis.length;
            length = length>10?10:length;
            //System.out.println("******"+length);
            int rate = 0;
            for (int i = 0; i < length ; i++) {//
                if(rate!=i*100/length) {
                    rate = i * 100 / length;
                    System.out.println("解析完成:" + rate + "%");
                }
                List<Document> list = XmlHandler.parserXml("/Users/ludongzhou/Desktop/new/"+wikis[i]);
                for (Document doc : list)
                    iwriter.addDocument(doc);
            }

            System.out.println("解析XML完成");
            iwriter.close();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }


    }
    public static List<String> search(String key)
    {
        List<String> l = new ArrayList<String>();
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
            //System.out.println("the searched length is "+hits.length);
            for (int i = 0; i < hits.length && i < 10; i++) {
                Document hitDoc = isearcher.doc(hits[i].doc);
                //System.out.println(hitDoc.get("text"));
                TokenStream tokenStream = analyzer.tokenStream("", new StringReader(hitDoc.get("text")));
                String str = highlighter.getBestFragment(tokenStream, hitDoc.get("text"));
                //System.out.println(str);
                l.add(str);
                //System.out.println(l.toString()+"shit");
                //System.out.println("score:" + hits[i].score);
                //System.out.println("================================================");
            }
            ireader.close();
            //directory.close();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        System.out.println(l.size());
        return l;
    }
}
