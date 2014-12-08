package com.pku;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by ember on 12/8/14.
 */
public class Answerer {
    static private Logger logger;
    static private boolean indexed = false;
    private static void setQes(Question q)
    {
        logger = Logger.getLogger("answerer");
        logger.setLevel(Level.ALL);
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
    private static void createIndex()
    {
        logger.info("创建索引.....");
        if (!indexed) {
            Searcher.createIndex();
            indexed = true;
        }
        logger.info("索引创建完成");
    }
    private static void search(Question q)
    {
        logger.info("开始搜索....");
        Searcher.search(q.getSentence());
        logger.info("搜索完毕");
    }
    private static void findAns(Question q)
    {
        logger.info("正在确定答案...");
        //todo
        String ans = "";
        q.setAnswer(ans);
        logger.info("答案为:" + ans);
    }
    public static void answer(Question q)
    {
        setQes(q);
        createIndex();
        segment(q);
        classify(q);
        search(q);
        findAns(q);
    }
}
