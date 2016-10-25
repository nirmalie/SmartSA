/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smartsa;

import cmu.arktweetnlp.Tagger;
import edu.stanford.nlp.process.Morphology;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 0611397
 */
public class LexiconGenerator {
    
   
       
    public static void main(String argv[]){
        try {
            Tagger tg=new Tagger();
            tg.loadModel("C:\\mydata\\ark-tweet-nlp-0.3.2\\model.20120919");
            java.io.File[] posDir = new java.io.File("C:\\mydata\\digg\\train\\sentenceLevel\\data\\pos").listFiles();
            java.io.File[] negDir = new java.io.File("C:\\mydata\\digg\\train\\sentenceLevel\\data\\neg").listFiles();
            Map<String,Term> posMap = new HashMap<String, Term>();
            Map<String,Term> negMap = new HashMap<String, Term>();
//            LBClassifier lbc = new LBClassifier();
            Inflector inf = new Inflector();
//            NormToken nt = new NormToken();
            Modifier mod = new Modifier();
            Morphology morp = new Morphology();
            
            BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\mydata\\digg\\train\\sentenceLevel\\data\\lexAll.txt"));
            
            //positive directory
            int currentDoc =0;
            for(java.io.File f : posDir){
//                String filename = f.getName().substring(0,1);
//                if(Integer.parseInt(filename)>=4749)
//                    continue;
                BufferedReader csv = new BufferedReader(new FileReader(f));
                String s = "";
                String data ="";
                while ((s = csv.readLine()) != null) {
                    if (s.isEmpty()) 
			continue;
                    data = data + s;
                }
                if (data.isEmpty()) 
                    continue;
                currentDoc++;
//                data = lbc.standardize(data);
                List<Tagger.TaggedToken> tokens = tg.tokenizeAndTag(data);
                
                for(Tagger.TaggedToken tk: tokens){
                    String[] granular = tk.token.split("/");
                    
                    //word-level
                    for(String st :granular){
                        
                      
                      if (tk.tag.equals("E")){
                          double val = Modifier.getEmoticonVal(st);
                          if(val ==1)
                              st = "posemo";
                          else if(val == -1)
                              st = "negemo";
                      }
                      st = morp.lemma(st, tk.tag, true);
                      st = st+"_"+tk.tag;
                          
//                    st = nt.normalize(st);
                    if(posMap.containsKey(st)){
                        Term t = posMap.get(st);
                        Double freq = t.getFreq();
                        freq++;
                        t.setFreq(freq);
                        int thisTermCurrentDoc = t.getDocCount();
                        double df= t.getPdf();
                        if(thisTermCurrentDoc < currentDoc){
                           df++;
                           thisTermCurrentDoc = currentDoc;
                        }
                        t.setPdf(df);
                        t.setDocCount(thisTermCurrentDoc);
                        posMap.put(st, t);
                    }
                    else{ 
                         Term t = new Term(1.0,1.0,0.0,currentDoc);
                         posMap.put(st, t);}
                }
                }
                csv.close();
            }
            
            //Negative Directory
            int negCurrentDoc =0;
            for(java.io.File f : negDir){
//                String filename = f.getName().substring(0,1);
//                if(Integer.parseInt(filename)>=4750)
//                    continue;
                BufferedReader csv = new BufferedReader(new FileReader(f));
                String s = "";
                String data ="";
                while ((s = csv.readLine()) != null) {
                    if (s.isEmpty()) 
			continue;
                    data = data + s;
                }
                if (data.isEmpty()) 
                    continue;
                negCurrentDoc++;
//                data = lbc.standardize(data);
                List<Tagger.TaggedToken> tokens = tg.tokenizeAndTag(data);
                for(Tagger.TaggedToken tk: tokens){
                     String[] granular = tk.token.split("/");
                    for(String st :granular){
                        if (tk.tag.equals("E")){
                          double val = Modifier.getEmoticonVal(st);
                          if(val ==1)
                              st = "posemo";
                          else if(val == -1)
                              st = "negemo";
                      }
                      st = morp.lemma(st, tk.tag, true);
                      st = st+"_"+tk.tag;
//                    st = inf.singularize(st);
//                    st = nt.normalize(st);
                    if(negMap.containsKey(st)){
                        Term t = negMap.get(st);
                        Double freq = t.getFreq();
                        freq++;
                        t.setFreq(freq);
                        int thisTermCurrentDoc = t.getDocCount();
                        double df= t.getPdf();
                        if(thisTermCurrentDoc < negCurrentDoc){
                           df++;
                           thisTermCurrentDoc = negCurrentDoc;
                        }
                        t.setPdf(df);
                        t.setDocCount(thisTermCurrentDoc);
                        negMap.put(st, t);
                    }
                    else{
                         Term t = new Term(1.0,1.0,0.0,negCurrentDoc);
                         negMap.put(st, t);}
                }
                }
                csv.close();
            }
            
            
            //calculate tfidf
            
            for(String s: posMap.keySet()){
                Term t = posMap.get(s);
                Double posFreq = t.getFreq();
                Double pdf = t.getPdf();
//                Double negFreq =0.0;
                Term tn;
                Double negTfIdf=0.0;
                
                if(negMap.containsKey(s)){
                    tn = negMap.get(s);
                    double negFreq = tn.getFreq();
                    double ndf=tn.getPdf();
                    negTfIdf = negFreq;
//                    negTfIdf = (negFreq*Math.pow((Math.log(negCurrentDoc/ndf)/Math.log(2)),-1.0));///negCurrentDoc;
//                    negTfIdf = negFreq*(Math.log(negCurrentDoc/ndf)/Math.log(2));
                    negMap.remove(s);
                }
//                double posScore = posFreq/(posFreq+negFreq);
//                double negScore = 1-posScore;
                double posTfIdf = posFreq;
//              double posTfIdf = (posFreq*Math.pow((Math.log(currentDoc/pdf)/Math.log(2)),-1.0));///currentDoc;
//              double posTfIdf = posFreq*(Math.log(currentDoc/pdf)/Math.log(2));
             
                posTfIdf = Math.round( posTfIdf * 100000.0 ) / 100000.0;
                negTfIdf = Math.round( negTfIdf * 100000.0 ) / 100000.0;
//                writer.write(s +"\t" + posFreq + "\t" + negFreq + "\t" + posScore + "\t" + negScore + "\n");
                writer.write(s +"\t" + posTfIdf + "\t" + negTfIdf + "\n");
            }
             for(String s: negMap.keySet()){
                Term tn = negMap.get(s);
                Double negFreq = tn.getFreq();
                Double ndf = tn.getPdf();
                double negTfIdf = negFreq;
//                double negTfIdf = (negFreq*(Math.log(negCurrentDoc/ndf)/Math.log(2)));///ndf;
//                double negTfIdf = (negFreq* Math.pow((Math.log(negCurrentDoc/ndf)/Math.log(2)),-1.0)  );// /negCurrentDoc;
//                Double posFreq =0.0;
//                double posScore = posFreq/(posFreq+negFreq);
//                double negScore = 1-posScore;
//                posScore = Math.round( posScore * 100.0 ) / 100.0;
                negTfIdf = Math.round( negTfIdf * 100000.0 ) / 100000.0;
                writer.write(s +"\t" + 0.0 + "\t" + negTfIdf + "\n");
            }
            writer.flush();
            writer.close();
            
        } catch (IOException ex) {
            Logger.getLogger(LexiconGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
}
