/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smartsa;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author 0611397
 */
public class EmoticonsRecogniser {
    private static String negEmoticon = "(\\Q:@\\E|\\Q:-@\\E|\\Q>:o\\E|\\Q>:0\\E|\\Qd:<\\E|\\Qd:\\E|\\Qd8\\E|\\Qd;\\E|\\Qd=\\E|\\Qdx\\E|\\Q\\>.<\\E|\\Q>_<\\E )"
            + "|(\\Qd:<\\E|\\Qd:\\E|\\Qd8\\E|\\Qd;\\E|\\Qd=\\E|\\Qdx\\E|\\Q>.<\\E|\\Q>_<\\E|\\Qv.v\\E )"
            + "|(\\Qd:/\\E|\\Q:\\\E|\\Q=/\\E|\\Q=\\\E|\\Q>:/\\E|\\Q>:\\\E|\\Q:-/\\E|\\Q:-\\\E)"
            + "|(\\Q:(\\E|\\Q):\\E|\\Q:'(\\E|\\Q:c\\E|\\Q:-(\\E|\\Q</3\\\E|\\Q:[\\E|\\Q:{\\E|\\Qv.v\\E|\\Qt.t\\E)";
    
    private static String posEmoticon = "(\\Q:)\\E|\\Q(:\\E|\\Q:-)\\E|\\Q:3\\E|\\Q:d\\E|\\Qxd\\E|\\Q:')\\E|\\Q^_^\\E|\\Q^.^\\E|\\Q:]\\E|\\Q:}\\E|\\Q:p\\E|\\Q:b\\E\\Q=p\\E|\\Q=b\\E|\\Q:-p\\E|\\Q:-b\\E|\\Q=)\\E )"
            + "|(\\Q<3\\E|\\Q:p\\E|\\Q:b\\E|\\Q=p\\E|\\Q=b\\E|\\Q:-p\\E|\\Q:-b\\E)";
    
    public static int getEmoticonValue(String emoticon){
        Pattern pattern = Pattern.compile(posEmoticon);
        Matcher matcher = pattern.matcher(emoticon);
        if(matcher.find())
            return 1;
        
        pattern = Pattern.compile(negEmoticon);
        matcher = pattern.matcher(emoticon);
        if(matcher.find())
            return -1;
        
        return 0;
    }
    public static void main(String argv[]){
        StringBuilder sb = new StringBuilder("<--___51");
//        sb.append("term");
//        sb.append("~");
//        sb.append("pos");
//        sb.append("~");
//        sb.append("score");
        
       // String s ="terkm~pos~score";
        String[] st = sb.toString().split("_");
        
        System.out.println(st[2]);
    }
    
}
