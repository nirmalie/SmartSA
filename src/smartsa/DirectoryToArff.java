/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smartsa;

import cmu.arktweetnlp.Tagger;
import cmu.arktweetnlp.Tagger.TaggedToken;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import edu.stanford.nlp.process.Morphology;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.TreeMap;

/**
 *
 * @author 0611397
 */
public class DirectoryToArff {
    
    
    public static void main (String argv[]){
        SWNAccess swn = new SWNAccess();
        Morphology morphology = new Morphology();
//        Inflector inflector = new Inflector();
        Tagger tagger = new Tagger();
        BufferedWriter ARFFWriter;
        BufferedWriter ARFFWriter2;
        BufferedWriter ARFFWriter3;
        ArrayList<String> trainTerms = new ArrayList<String>();
        ArrayList<String> testTerms = new ArrayList<String>();
        Map<String, ArrayList> theMap = new HashMap<String, ArrayList>();
        File sourceDir = new File("C:\\RGU\\sentenceDataset\\Technology");
        File[] docs = sourceDir.listFiles();
        int docNumber = 0;
        int numPos=0, numNeg=0, numTest=0;
        double totalPos=0.0, totalNeg=0.0;
        try{
            tagger.loadModel("C:\\mydata\\ark-tweet-nlp-0.3.2\\model.20120919");
            ARFFWriter = new BufferedWriter(new FileWriter("C:\\mydata\\ARFF\\technology\\trainNorm1.arff", true));
            ARFFWriter2 = new BufferedWriter(new FileWriter("C:\\mydata\\ARFF\\technology\\testNorm1.arff", true));
            ARFFWriter3 = new BufferedWriter(new FileWriter("C:\\mydata\\ARFF\\technology\\vocabulary.txt", true));
//        }catch(Exception e){System.err.println("Bad location to arktweetnlp model or arff file");}
        for(File doc: docs){
            docNumber++;
            try{
                BufferedReader docReader = new BufferedReader(new FileReader(doc));
                String s ="";
                StringBuilder docContent = new StringBuilder();
                while ((s = docReader.readLine()) != null) {
                    if (s.isEmpty())
                        continue;
                    docContent.append(s);
                }
                List<TaggedToken> taggedTerms = tagger.tokenizeAndTag(docContent.toString());
                for(TaggedToken taggedTerm: taggedTerms){
                    String lemma = morphology.lemma(taggedTerm.token, taggedTerm.tag, true);
                    lemma = lemma.replaceAll("~", "-");
                    String PoS = taggedTerm.tag;
                    SentiWord termScores = swn.getScore(lemma, PoS, -1);
                    StringBuilder aterm = new StringBuilder();
                  //  aterm.append("~"+PoS);
                    if(PoS.equalsIgnoreCase("e")){
                        lemma = lemma.replaceAll("\\s", "");
                        if(Modifier.getEmoticonVal(lemma) == 1){
                            aterm.append("posemo~E~");
                            aterm.append("1");
                        }
                        else if (Modifier.getEmoticonVal(lemma) == -1){
                            aterm.append("negemo~E~");
                            aterm.append("-1");
                        }
                        else {
                            aterm.append(lemma);
                            aterm.append("~");
                            aterm.append(PoS);
                            aterm.append("~51");
                        }
                    }
                    else if(termScores.getPol().equals("non")){
                        //51 is a sentinel
                        aterm.append(lemma);
                        aterm.append("~");
                        aterm.append(PoS);
                        aterm.append("~51");
                    }
                    else{
                        Double score = termScores.getPos()-termScores.getNeg();
                        aterm.append(lemma);
                        aterm.append("~");
                        aterm.append(PoS);
                        aterm.append("~");
                        aterm.append(score);
                     }
                    if(!theMap.containsKey(aterm.toString())){
                        ArrayList entries = new ArrayList();
                        //Arrays.fill(entries,23);
                        entries.add(docNumber);
                        theMap.put(aterm.toString(), entries);
                    }
                    else{
                        ArrayList entries = theMap.get(aterm.toString());
                        entries.add(docNumber);
                        theMap.remove(aterm.toString());
                        theMap.put(aterm.toString(), entries);
                    }
                }
                }catch(Exception e){System.err.println("Cannot read from "+ doc.getAbsolutePath());}
            }
                
        
        
        ARFFWriter.write("@RELATION lexicon_expansion");
        ARFFWriter.newLine();
        ARFFWriter.newLine();
        ARFFWriter.newLine();
        ARFFWriter.write("@ATTRIBUTE @@class@@ NORMINAL");
        ARFFWriter.newLine();
        //test
        ARFFWriter2.write("@RELATION lexicon_expansion");
        ARFFWriter2.newLine();
        ARFFWriter2.newLine();
        ARFFWriter2.newLine();
        ARFFWriter2.write("@ATTRIBUTE @@class@@ NORMINAL");
        ARFFWriter2.newLine();
        
        for(int i=1; i<docs.length+1; i++){
            ARFFWriter.write("@ATTRIBUTE "+i +" NUMERIC");
            ARFFWriter.newLine();
            
            //test
            ARFFWriter2.write("@ATTRIBUTE "+i +" NUMERIC");
            ARFFWriter2.newLine();
        }
        
        ARFFWriter.newLine();
        ARFFWriter.newLine();
        ARFFWriter.newLine();
        ARFFWriter.write("@DATA");
        
        //test
        ARFFWriter2.newLine();
        ARFFWriter2.newLine();
        ARFFWriter2.newLine();
        ARFFWriter2.write("@DATA");
        
        for(String term: theMap.keySet()){
            String[] termComponents = term.split("~");
            if(termComponents[2].isEmpty())
                continue;
            NumberFormat _format = NumberFormat.getInstance(Locale.US);
            Number number = null;
            double _double=0.0;
            try {
            number = _format.parse(termComponents[2].trim());
            _double = Double.parseDouble(number.toString());
//        System.err.println("Double Value is :"+_double);
            } catch (ParseException e) {System.err.println("problem with term: "+term); }
         //   Double tmp = Double.parseDouble(termComponents[2].trim());
//            tmp = Math.round(tmp*1000.0)/1000.0;
//            float score = _double.floatValue();
            if(_double < 50){
                ArrayList entries = theMap.get(term);
                TreeMap mappedEntries = getTermVector(entries);
//                get df
                double df_ti = mappedEntries.keySet().size();
                if(df_ti >=10 && Math.abs(_double) >= 0.5){
                    trainTerms.add(term);
                    ARFFWriter.newLine();
                    ARFFWriter.write("{0 "+getLabel(_double));
                    if(_double >0){
                    numPos++;
                    totalPos+=_double;}
                    else{
                    numNeg++;
                    totalNeg+=_double;}
                for(Object obj: mappedEntries.keySet()){
                    int doc = (Integer)obj;
                        ARFFWriter.write(",");
                        int freq = (Integer)mappedEntries.get(doc);
                        double tfidf_ti =freq*Math.log10(docs.length/df_ti);
                        tfidf_ti = Math.round(tfidf_ti*1000.0)/1000.0;
                        ARFFWriter.write(doc +" "+tfidf_ti);
                }
                ARFFWriter.write("}");
                }
            }
            //test
            if(_double > 50){
                ArrayList entries = theMap.get(term);
                TreeMap mappedEntries = getTermVector(entries);
//                get df
                double df_ti = mappedEntries.keySet().size();
                
                if(df_ti >=10){
                    numTest++;
                    testTerms.add(term);
                    ARFFWriter2.newLine();
                    ARFFWriter2.write("%"+term);
                    ARFFWriter2.newLine();
                    ARFFWriter2.write("{0 ?");
                    for(Object obj: mappedEntries.keySet()){
                        int doc = (Integer)obj;
                        ARFFWriter2.write(",");
                        int freq = (Integer)mappedEntries.get(doc);
                        double tfidf_ti =freq*Math.log10(docs.length/df_ti);
                        tfidf_ti = Math.round(tfidf_ti*1000.0)/1000.0;
                        ARFFWriter2.write(doc +" "+tfidf_ti);
                }
                ARFFWriter2.write("}");
                }
            }
        }
       //test
       for(String term: trainTerms){
           String[] comps = term.split("~");
           ARFFWriter3.write(comps[0]+"\t"+comps[1]+"\t"+comps[2]);
           ARFFWriter3.newLine();
       }
       for(String term: testTerms){
           String[] comps = term.split("~");
           ARFFWriter3.write(comps[0]+"\t"+comps[1]);
           ARFFWriter3.newLine();
       }
       ARFFWriter.close();
       ARFFWriter2.close();
       ARFFWriter3.close();
     }catch(Exception e){e.printStackTrace();}
        System.out.println(numPos);
        System.out.println(numNeg);
        System.out.println(numTest);
        System.out.println(totalPos);
        System.out.println(totalNeg);
    }
    
    private static TreeMap getTermVector(ArrayList entries){
        TreeMap<Integer,Integer> returnMap = new TreeMap<Integer,Integer>();
        for(Object obj: entries){
            int docNumber = (Integer)obj;
            if(!returnMap.containsKey(docNumber)){
                returnMap.put(docNumber, 1);
            }
            else{
                int freq = returnMap.get(docNumber);
                freq++;
                returnMap.remove(docNumber);
                returnMap.put(docNumber, freq);
            }
         }
        return returnMap;
    }
    
    private static String getLabel(double val){
        if (val>=0.75)
            return "plusPointSevenFive";
        if (val>=0.5)
            return "PlusPointFive";
        if (val>=0.25)
            return "PlusPointTwoFive";
        if (val<=-0.75)
            return "minusPointSevenFive";
        if (val<=-0.5)
            return "minusPointFive";
        if (val<=-0.25)
            return "minusPointTwoFive";
        return "zero";
    }
}
