package com.pku;


import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apdplat.qa.SharedQuestionAnsweringSystem;
import org.apdplat.qa.model.CandidateAnswer;
import org.apdplat.qa.system.QuestionAnsweringSystem;
import org.apdplat.qa.datasource.BaiduDataSource;
import org.apdplat.qa.datasource.GoogleDataSource;
import org.apdplat.qa.system.CommonQuestionAnsweringSystem;

import java.io.File;
import java.io.IOException;
/**
 * Created by ember on 12/8/14.
 */
public class Answerer {
    static private Logger logger;
    private static final QuestionAnsweringSystem baiduqas = new CommonQuestionAnsweringSystem();
    static File ansFile;
    static File errFile;
    static File answers;
    static FileOutputStream ansStream, errStream, answerStream;
    static int order = 0;
    static void init()
    {
        logger = Logger.getLogger("answerer");
        logger.setLevel(Level.ALL);
        baiduqas.setDataSource(new BaiduDataSource());
        ansFile = new File("/Users/ember/Downloads/ans.txt");
        errFile = new File("/Users/ember/Downloads/err.txt");
        answers = new File("/Users/ember/Downloads/answers.txt");
        try{
            ansFile.createNewFile();
            errFile.createNewFile();
            answers.createNewFile();
            ansStream = new FileOutputStream(ansFile);
            errStream = new FileOutputStream(errFile);
            answerStream = new FileOutputStream(answers);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public static String answerOnline(String q)
    {
        String baidures;
        List<CandidateAnswer>ca = baiduqas.answerQuestion(q).getAllCandidateAnswer();
        if (ca != null && ca.size() != 0)
            baidures = ca.get(0).getAnswer();
        else
            baidures=" ";
        try {
            errStream.write((q + "\t" + baidures + '\n').getBytes());
            answerStream.write((String.valueOf(order) + "\t" + baidures + '\n').getBytes());
        }catch (IOException e){
            e.printStackTrace();
        }
        return baidures;
    }

    public static void answerFileOnline(String filename)
    {
        List<String> qlist = XmlHandler.parseQuestion(filename);
        for (String q:qlist)
        {
            answer(q);
        }
    }
    public static String answer(String q)
    {
        order++;
//        if (order < 12776)
//            return " ";
        q = q.replace('？', ' ');
        q = q.replace('?', ' ');
        q = q.replace('/', ' ');
        logger.info(q);
        List<ScoredSnips> snips = Searcher.search(q);
        if (snips.size() == 0 || snips.get(0).score < 2) {
            if(snips.size()!=0) {
                logger.info(snips.get(0).snips);
                logger.info(String.valueOf(snips.get(0).score));
            }
                return answerOnline(q);
        }
        logger.info(snips.get(0).snips);
        logger.info(String.valueOf(snips.get(0).score));
        String snip = snips.get(0).snips;
        String ans = snip.substring(snip.indexOf("?")+1);
        try {
            ansStream.write((q + "\t" + ans + "\n").getBytes());
            answerStream.write((String.valueOf(order) + "\t" + ans + "\n").getBytes());
        }catch (IOException e){
            e.printStackTrace();
        }
        return ans;
    }
}
