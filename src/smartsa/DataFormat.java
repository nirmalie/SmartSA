/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smartsa;

import cmu.arktweetnlp.Tagger;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author 0611397
 */
public class DataFormat {
    
    
    public static void main(String[] argv){
        ArrayList nt = new ArrayList();
    try{
        java.io.File posFiles[] = new java.io.File("C:\\SEMEVAL2013\\pos").listFiles();
        java.io.File negFiles[] = new java.io.File("C:\\SEMEVAL2013\\neg").listFiles();
        java.io.File neuFiles[] = new java.io.File("C:\\SEMEVAL2013\\neu").listFiles();
//        FileWriter fr =  new FileWriter("C:\\SEMEVAL2013\\traindata.txt", true);
        FileWriter fr =  new FileWriter("C:\\SEMEVAL2013\\unigrams.txt", true);
        int filecount=0;
        System.out.println("POSITIVE====================================================");
        for(int i=0; i<posFiles.length; i++){
        BufferedReader csv = new BufferedReader(new FileReader(posFiles[i]));
        String s="", data="";
            while ((s = csv.readLine()) != null){
				if (s.isEmpty()){
					continue;
				}
                                data = data+s;
                                ArrayList thisdocterms = getDocTerms(data);
                                for(int k=0; k<thisdocterms.size(); k++){
                                    String xx=(String)thisdocterms.get(k);
                                    if(!nt.contains(xx)) 
                                        nt.add(xx);
                                }
            }
//            filecount++;
//            fr.write(i+"\t"+data+"\n");
        }
            
        System.out.println("NEGATIVE====================================================");
        for(int j=0; j<negFiles.length; j++){
        BufferedReader csv = new BufferedReader(new FileReader(negFiles[j]));
        String s="", data="";
            while ((s = csv.readLine()) != null){
				if (s.isEmpty()){
					continue;
				}
                                data = data+s;
                                ArrayList thisdocterms = getDocTerms(data);
                                for(int k=0; k<thisdocterms.size(); k++){
                                    String xx=(String)thisdocterms.get(k);
                                    if(!nt.contains(xx)) 
                                        nt.add(xx);
                                }
            }
//            filecount++;
//            fr.write(filecount+"\t"+data+"\n");
    }
    System.out.println("NEUTRAL====================================================");
        for(int i=0; i<neuFiles.length; i++){
        BufferedReader csv = new BufferedReader(new FileReader(neuFiles[i]));
        String s="", data="";
            while ((s = csv.readLine()) != null){
				if (s.isEmpty()){
					continue;
				}
                                data = data+s;
                               ArrayList thisdocterms = getDocTerms(data);
                                for(int k=0; k<thisdocterms.size(); k++){
                                    String xx=(String)thisdocterms.get(k);
                                    if(!nt.contains(xx)) 
                                        nt.add(xx);
                                }
            }
//            filecount++;
//            fr.write(filecount+"\t"+data+"\n");
        }
        for(int j=0; j<nt.size(); j++)
            fr.write((String)nt.get(j)+"\n");
        fr.close();
    }catch(Exception e){e.printStackTrace();}
}
    public static ArrayList getDocTerms(String txt){
    Modifier mod = new Modifier();
    NormToken nt = new NormToken();
    Inflector inf = new Inflector();
    Tagger tg = new Tagger();
    ArrayList unigrams = new ArrayList();
    try{
    tg.loadModel("C:\\mydata\\ark-tweet-nlp-0.3.2\\model.20120919");
    if (!txt.isEmpty()){
       List<Tagger.TaggedToken> tokens = tg.tokenizeAndTag(txt);
                for(Tagger.TaggedToken tk: tokens){
                   String st =tk.token.toLowerCase();
                   if (tk.tag.equals("E")){
                      double val = mod.getEmoticonVal(st);
                      if(val ==1)
                        st = "posemo";
                      else if(val == -1)
                       st = "negemo";
                      }
                  else if(tk.tag.equals("U")){
                       st="url";
                      }
                 else if(tk.tag.equals("@")){
                          st="username";
                      }
                 else{
                      st = nt.norm(st);
                      st = inf.singularize(st);
                      }
                unigrams.add(st);
                }
    }
    }catch(Exception e){e.printStackTrace();}
//    ArrayList bigrams = getBigram(unigrams);
//    ArrayList trigrams = getTrigram(unigrams);
    
//    String[] returnArray=new String[unigrams.size()+bigrams.size()+trigrams.size()];
//    int x=0;
//    for(int i=0; i<unigrams.size(); i++){
//        returnArray[x]=(String)unigrams.get(i);
//        x++;
//    }
//    for(int i=0; i<bigrams.size(); i++){
//        returnArray[x]=(String)bigrams.get(i);
//        x++;
//    }
//    for(int i=0; i<trigrams.size(); i++){
//        returnArray[x]=(String)trigrams.get(i);
//        x++;
//    }
return unigrams;
}
}
