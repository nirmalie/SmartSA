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
public class NormToken {

    public NormToken() {
    }
    
    
    public String normalize(String str){
    String regexAppostroph=("\\w{1,20}['][s]|\\w{1,20}[']");
    String regex = ("(.)\\1{3,}");
    String text = str;
    
    Pattern checkRegex = Pattern.compile(regex);
    Matcher regexMatcher = checkRegex.matcher(text);
    String newToken=str;
    while(regexMatcher.find()){
        String[] stArray = str.split("\'");
        newToken = "yes";
        System.out.println(regexMatcher.group());
    }
    return newToken;
    }
    
  public String norm(CharSequence str) {
 // str = str.replaceAll("\'t", "not");
  Pattern patt = Pattern.compile("(.|..)\\1{3,}");
  Matcher m = patt.matcher(str);
  StringBuffer sb = new StringBuffer(str.length());
  while (m.find()) {
    String text = m.group(1)+m.group(1)+m.group(1);
    // ... possibly process 'text' ...
    m.appendReplacement(sb, Matcher.quoteReplacement(text));
  }
  m.appendTail(sb);
  String newStr = sb.toString();
  
  newStr = newStr.replaceAll("\'t\\z|\'nt\\z", "not");
  newStr = newStr.replaceAll("\'s\\z|\'\\z", "");
  return newStr;
}
}
