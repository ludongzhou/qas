package com.pku;

import java.io.*;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;

import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;


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

    private static final String basedir = System.getProperty("SegDemo", "stanford_segmenter/data");

    public static void main(String[] args) throws Exception {
        System.setOut(new PrintStream(System.out, true, "utf-8"));

        Properties props = new Properties();
        props.setProperty("sighanCorporaDict", basedir);
        // props.setProperty("NormalizationTable", "data/norm.simp.utf8");
        // props.setProperty("normTableEncoding", "UTF-8");
        // below is needed because CTBSegDocumentIteratorFactory accesses it
        props.setProperty("serDictionary", basedir + "/dict-chris6.ser.gz");
        if (args.length > 0) {
            props.setProperty("testFile", args[0]);
        }
        props.setProperty("inputEncoding", "UTF-8");
        props.setProperty("sighanPostProcessing", "true");

        CRFClassifier<CoreLabel> segmenter = new CRFClassifier<CoreLabel>(props);
        segmenter.loadClassifierNoExceptions(basedir + "/ctb.gz", props);
        for (String filename : args) {
            segmenter.classifyAndWriteAnswers(filename);
        }

        String sample = "北京大学成立于哪一年";
        List<String> segmented = segmenter.segmentString(sample);
        System.out.println(segmented);


        //////////
        MaxentTagger tagger = new MaxentTagger("stanford_postagger/models/chinese-distsim.tagger");
        List<List<HasWord>> sentences = MaxentTagger.tokenizeText(new BufferedReader(new StringReader(segmented.toString())));
        for (List<HasWord> sentence : sentences) {
            List<TaggedWord> tSentence = tagger.tagSentence(sentence);
            System.out.println(Sentence.listToString(tSentence, false));
        }

    }

}
