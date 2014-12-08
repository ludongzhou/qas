package com.pku;

import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.*;
import com.pku.Question;

/**
 * Created by ember on 14/11/29.
 */
public class Segmenter {
    private static final String basedir = System.getProperty("SegDemo", "stanford_segmenter/data");
    private static CRFClassifier<CoreLabel> ssegmenter;
    public static void prepare()
    {
        Properties props = new Properties();
        props.setProperty("sighanCorporaDict", basedir);
//        props.setProperty("NormalizationTable", "data/norm.simp.utf8");
//        props.setProperty("normTableEncoding", "UTF-8");
//        below is needed because CTBSegDocumentIteratorFactory accesses it
        props.setProperty("serDictionary", basedir + "/dict-chris6.ser.gz");
//        if (args.length > 0) {
//            props.setProperty("testFile", args[0]);
//        }
        props.setProperty("inputEncoding", "UTF-8");
        props.setProperty("sighanPostProcessing", "true");
        if (ssegmenter == null) {
            ssegmenter = new CRFClassifier<CoreLabel>(props);
            ssegmenter.loadClassifierNoExceptions(basedir + "/ctb.gz", props);
        }
//        for (String filename : args) {
//            Segmenter.classifyAndWriteAnswers(filename);
//        }
    }
    public static List segment(Question q)
    {
        //System.setOut(new PrintStream(System.out, true, "utf-8"));
        List<String> segmented = ssegmenter.segmentString(q.getSentence());
//        System.out.println(segmented);

        MaxentTagger tagger = new MaxentTagger("stanford_postagger/models/chinese-distsim.tagger");
        List<List<HasWord>> sentences = MaxentTagger.tokenizeText(new BufferedReader(new StringReader(segmented.toString())));
        List<List<TaggedWord>> ans = new ArrayList<List<TaggedWord>>();
        for (List<HasWord> sentence : sentences) {
            List<TaggedWord> tSentence = tagger.tagSentence(sentence);
            ans.add(tSentence);
        }
        return ans.get(0);
    }
}
