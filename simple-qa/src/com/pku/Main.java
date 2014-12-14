package com.pku;

import java.util.Scanner;

/** This is a very simple demo of calling the Chinese Word Segmenter
 *  programmatically.  It assumes an input file in UTF8.
 *  <p/>
 *  <code>
 *  Usage: java -mx1g -cp seg.jar SegDemo fileName
 *  </code>
 *  This will run correctly in the distribution home directory.  To
 *  run in general, the properties for where to find dictionaries or
 *  normalizations have to be set.
 *
 *  @author Christopher Manning
 */

public class Main {
    public static void main(String[] args) throws Exception {
        Segmenter.prepare();
        Searcher.createIndex();
        Answerer.answerFileOnline("/Users/ember/Downloads/testset.xml");
        System.out.println("请输入要查询的问题,退出输入bye");
        Scanner sca = new Scanner(System.in);
        String s = sca.next();
        while (!s.equals("bye")) {
            Question q = new Question(s);
            Answerer.answer(q);
            System.out.println(q.getAnswer());
            System.out.println("请输入下一个问题，退出输入bye");
            s = sca.next();
        }
    }


}
