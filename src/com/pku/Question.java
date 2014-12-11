package com.pku;

import edu.stanford.nlp.ling.TaggedWord;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by ember on 14/11/29.
 */
public class Question {
    private List<TaggedWord> words;
    private String sentence;
    private int type;
    private String answer;

    public Question(String str)
    {
        super();
        sentence = str;
    }
    public String getWords()
    {
        return words.toString();
    }
    public void cleanWords()
    {
        Iterator<TaggedWord> iter = words.iterator();
        while (iter.hasNext())
        {
            TaggedWord taggedWord = iter.next();
            if (  taggedWord.tag().equals("PU"))
                iter.remove();
            if ( taggedWord.word().equals("-LSB-") || taggedWord.word().equals("-RSB-") )
                iter.remove();
        }
    }
    public void setWords(List _words)
    {
        words = _words;
        this.cleanWords();
    }
    public String getSentence()
    {
        return sentence;
    }
    public void setSentence(String _sentence)
    {
        this.sentence = _sentence;
    }
    public int getType()
    {
        return type;
    }
    public void setType(int _type)
    {
        this.type = _type;
    }
    public String getAnswer()
    {
        return answer;
    }
    public void setAnswer(String _answer)
    {
        this.answer = _answer;
    }
}
