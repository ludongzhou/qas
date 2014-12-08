package com.pku;

import edu.stanford.nlp.ling.TaggedWord;

import java.util.List;

/**
 * Created by ember on 14/11/29.
 * 1、人名
 如：
 APDPlat的作者是谁？
 APDPlat的发起人是谁？
 谁死后布了七十二疑冢？
 习近平最爱的女人是谁？
 2、地名
 如：
 “海的女儿”是哪个城市的城徽？
 世界上流经国家最多的河流是哪一条？
 世界上最长的河流是什么？
 汉城是哪个国家的首都？
 3、机构团体名
 如：
 BMW是哪个汽车公司制造的？
 长城信用卡是哪家银行发行的？
 美国历史上第一所高等学府是哪个学校？
 前身是红色中华通讯社的是什么？
 4、数字
 如：
 全球表面积有多少平方公里？
 撒哈拉有多少平方公里？
 北京大学占地多少平方米？
 撒哈拉有多少平方公里？
 5、时间
 如：
 哪一年第一次提出“大跃进”的口号？
 大庆油田是哪一年发现的？
 澳门是在哪一年回归祖国怀抱的？
 邓小平在什么时候进行南巡讲话？
 */
public class QuestionClassifier {
    public static int classify(Question q)
    {
        String sentence = q.getSentence();
        if (sentence.contains("谁") || sentence.contains("什么人") || sentence.contains("哪个人"))
            return 0;
        if (sentence.contains("哪儿") || sentence.contains("什么地方") || sentence.contains("何处"))
            return 1;
        if (sentence.contains("多少") || sentence.contains("几"))
            return 2;
        return 5;
    }
}
