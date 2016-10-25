/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package smartsa;

import java.io.FileInputStream;
import java.util.ArrayList;
import net.didion.jwnl.JWNL;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;

/**
 *
 * @author Aminu
 */
public class SWNAccess {

private static final String PROPERTIES="C:\\SmartSAresources\\jwnl14-rc2\\config\\file_properties.xml";
//private Properties props;
//private StanfordCoreNLP pipeline;
private net.didion.jwnl.dictionary.Dictionary wordnet;
private SentiWordNet senti;
//private Wsd wsd;



 public SWNAccess() {
    try {
          JWNL.initialize(new FileInputStream(PROPERTIES));
        } catch(Exception e) {
          e.printStackTrace();
          System.exit(1);
        }

    wordnet = net.didion.jwnl.dictionary.Dictionary.getInstance();
    senti = new SentiWordNet();

   }//end constructor




   public SentiWord getScore(String lemma, String wPOS, int sense){

       String pol ="neu";
//       Double pos =0.0;
//       Double neg =0.0;
     
//        ArrayList<Double[]> returnArray= new ArrayList<Double[]>();
        
        Double[] score = new Double[] {0.0,0.0};
        try{
            if (!JWNL.isInitialized())
                 JWNL.initialize(new FileInputStream(PROPERTIES));

        IndexWord indexWord = null;

        if (wPOS.toUpperCase().startsWith("N"))
            indexWord = wordnet.getIndexWord(POS.NOUN, lemma);
        else if (wPOS.toUpperCase().startsWith("V"))
            indexWord = wordnet.getIndexWord(POS.VERB, lemma);
        else if (wPOS.toUpperCase().startsWith("J") || wPOS.toUpperCase().startsWith("A"))
            indexWord = wordnet.getIndexWord(POS.ADJECTIVE, lemma);
        else if (wPOS.toUpperCase().startsWith("R"))
            indexWord = wordnet.getIndexWord(POS.ADVERB, lemma);
        
        if(indexWord == null)
            return new SentiWord("non",score[0],score[1]);

        //first sense
        if (indexWord !=null && sense == -2){
           // for (int i=1; i<=indexWord.getSenseCount(); i++){
                Double[] senseScore = new Double[2];
                senseScore = senti.getScores((Long)(indexWord.getSense(1).getKey()));
                score[0] = senseScore[0]; score[1] = senseScore[1];
           //    returnArray.add(senseScore);
           //    }
        }

        //WSD
        if (indexWord !=null && sense>0){
           // for (int i=1; i<=indexWord.getSenseCount(); i++){
                Double[] senseScore = new Double[2];
                senseScore = senti.getScores((Long)(indexWord.getSense(sense).getKey()));
                score[0] = senseScore[0]; score[1] = senseScore[1];
           //    returnArray.add(senseScore);
           //    }
        }

       //Average
        if (indexWord !=null && sense == -1){
            int posCount =0;
            int negCount =0;
//            int neuCount =0;
            for (int i=1; i<=indexWord.getSenseCount(); i++){
                Double[] senseScore = new Double[2];
                senseScore = senti.getScores((Long)(indexWord.getSense(i).getKey()));
                if(senseScore[0] >0 && senseScore[1] <=0){
                    posCount++;
                    score[0] = score[0]+senseScore[0]; }

                if(senseScore[1] >0 && senseScore[0] <=0){
                    negCount++;
                    score[1] = score[1]+senseScore[1]; }
              }

            if(posCount >0)
            score[0] = Math.round((score[0]/posCount)*1000.0)/1000.0;
            if(negCount >0)
            score[1] = Math.round(score[1]/negCount*1000.0)/1000.0;
        }

        } catch(Exception e) {
          e.printStackTrace();
          System.exit(1);
        }

 return new SentiWord(pol,score[0],score[1]);

    }



   public static void main(String[] argv){

       String a="excite"; String b="a";
       SentiWord scores = new SWNAccess().getScore(a, b,-1);
       System.out.println(scores.pos+"\t"+scores.neg);
//       System.out.println("sentiment scores of " +a );
//       for(int i=0; i<scores.size(); i++){
//        System.out.println("sense " +(i+1) +" pos = "+ scores.get(i)[0] + " neg = "+ scores.get(i)[1] );
//       }


   }

}
