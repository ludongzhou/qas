package com.pku;

import edu.stanford.nlp.ling.TaggedWord;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

//import org.apdplat.qa.SharedQuestionAnsweringSystem;
/**
 * Created by ember on 12/8/14.
 */
public class Answerer {
    static private Logger logger;
    static private List<ScoredSnips> snips = new ArrayList<>();
    static private String ans_type[] = {"NR", "NR", "QP", "NN","NT", "NT"};
    static File answers;
    static FileOutputStream answerStream;
    static int order = 0;
    private static void setQes(Question q)
    {
        logger = Logger.getLogger("answerer");
        logger.setLevel(Level.ALL);
        answers = new File("/Users/ember/Downloads/answers.txt");
        try{
            answers.createNewFile();
            answerStream = new FileOutputStream(answers);
        }catch (IOException e){
            e.printStackTrace();
        }
        logger.info("接受问题，问题为:" + q.getSentence());
    }
    private static void segment(Question q)
    {
        logger.info("开始分词...");
        q.setWords(Segmenter.segment(q));
        logger.info("分词结束，分词结果为:" + q.getWords());
    }
    private static void classify(Question q)
    {
        logger.info("判断问题类型...");
        q.setType(QuestionClassifier.classify(q));
        logger.info("类型为:" + q.getType());
    }
    private static void search(Question q)
    {
        logger.info("开始搜索....");
        snips = Searcher.search(q.getSentence());
        //logger.info(snips.toString());
        logger.info("搜索完毕");
    }
    private static void findAns(Question q)
    {
        logger.info("正在确定答案...");
        //todo
        String ans = "未知";
        for(int i=0;i<snips.size();i++) {
            List<TaggedWord> words = Segmenter.segment(snips.get(i).snips);
            Iterator<TaggedWord> iter = words.iterator();
            while (iter.hasNext()) {
                TaggedWord taggedWord = iter.next();
                if (  taggedWord.tag().equals("PU")) {
                    continue;
                }
                if ( taggedWord.word().equals("-LSB-") || taggedWord.word().equals("-RSB-") ) {
                   continue;
                }
                if (taggedWord.tag().equals(ans_type[q.getType()])) {
                    ans = taggedWord.word().toString();
                    break;
                }
            }
            if(ans!="未知")
                break;
        }
        q.setAnswer(ans);
        logger.info("答案为:" + ans);
    }

    public static void answerFileOnline(String filename)
    {
        List<String> qlist = XmlHandler.parseQuestion(filename);
        for (String q:qlist)
        {
            answer(new Question(q));
        }
    }

    public static void answer(Question q)
    {
        order++;
        setQes(q);
        segment(q);
        classify(q);
        search(q);
//        findAns(q);
        findAns(q);
        try {
            answerStream.write((String.valueOf(order) + "\t" + q.getAnswer() + '\n').getBytes());
        }catch (IOException e){
            e.printStackTrace();
        }
    }
//    public static void answerOnline(Question q)
//    {
//         q.setAnswer(SharedQuestionAnsweringSystem.getInstance().answerQuestion(q.getSentence()).getAllCandidateAnswer().get(1).getAnswer());
//    }
}
