package com.pku;
import com.pku.Segmenter;
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
        Object obj = Segmenter.segment("周鲁东出生于哪一年");
        Object obj2 = Segmenter.segment("钟高浩出生于哪一年");
        System.out.println(obj.toString());
        System.out.println(obj2.toString());
    }


}
