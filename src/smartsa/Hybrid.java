/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smartsa;

/**
 *
 * @author 0611397
 * 
 */

import cmu.arktweetnlp.Tagger;
import cmu.arktweetnlp.Tagger.TaggedToken;
import edu.stanford.nlp.ling.WordTag;
import edu.stanford.nlp.process.Morphology;
import java.util.ArrayList;
public class Hybrid {
//    private SWNAccess swn;
    private WeightAccess wt;
    private Modifier modifier;
    private Inflector inf;
    private NormToken nt;
    private Morphology morp;
    private ArrayList staticOnly;
    private ArrayList domainOnly;
    private ArrayList overlapAggree;
    private ArrayList overlapDisaggree;
    private SWNScores sw;
    
    public Hybrid(){
//        swn = new SWNAccess();
        wt = new WeightAccess();
        modifier = new Modifier();
        inf=new Inflector();
        nt = new NormToken();
        morp = new Morphology();
        staticOnly = new ArrayList();
        domainOnly = new ArrayList();
        overlapAggree = new ArrayList();
        overlapDisaggree = new ArrayList();
        try{
        sw = new SWNScores("C:\\Aminu\\Aminu\\Documents\\Projects\\Ebuka\\SentiTweetsAnalysis\\SentiTweets\\SentiWordNet_3.0.0.txt");
        }catch(Exception e){e.printStackTrace();}

    }
    
    public ClassifiedText classify(String txt,double th, double alpha, double beta){
         String text =txt;
         Double pos=0.0, neg=0.0, neu=0.0;
         int npos=0;
         int nneg=0;
         String st ="";
         boolean containPosEmo=false, containNegEmo=false;
         
//         System.out.println(text+"\n ============================================== \n");
         
         try{
         Tagger tg = new Tagger();
         tg.loadModel("C:\\mydata\\ark-tweet-nlp-0.3.2\\model.20120919");
         Object[] tokens = tg.tokenizeAndTag(text).toArray();
         for (int i=0; i<tokens.length; i++){
            TaggedToken tk = (TaggedToken)tokens[i];
            String[] granular = tk.token.split("/");
            String partOfSpeech =tk.tag;
            st=tk.token;
            if (tk.tag.equals("E")){
               double val = modifier.getEmoticonVal(st);
               if(val ==1.0){
                 st = "posemo";
                 containPosEmo=true;
               }
               else if(val == -1.0){
                st = "negemo";
                containNegEmo=true;
               }
           }
           else if(tk.tag.equals("U")){
             st="url";
           }
           else if(tk.tag.equals("@")){
            st="username";
           }
           else{
             st = nt.norm(st);
             st = inf.singularize(st).trim().toLowerCase();
           }
           String lemma = st;
           //modifier.isStop(st) || 
//           if (modifier.isIntensifier(st) || modifier.isDowntoner(st))
//               continue;
//            if (modifier.isNegator(st))
//               continue;
           Double[] score1 = sw.extract(lemma, partOfSpeech);
           Double[] w = wt.getWeights(lemma+"_"+partOfSpeech.substring(0, 1).toUpperCase());
//           double score2=w[1]-w[2];
////                Double[] modScore=modify(new Double[]{w[1],w[2]},i,tokens);
//           double score_t = score2;
          
//                if(w[0] != 50.0 && !(score1==-10)){
//                     score_t =alpha*score1+(1-alpha)*score2;
////                     if((score1>=0 && score2>=0) || (score1<0 && score2<0))
////                         overlapAggree.add(lemma+"_"+tk.tag);
////                     else
////                         overlapDisaggree.add(lemma+"_"+tk.tag);
//                }
//                else if(w[0] == 50.0 && !(score1==-10)){
//                     score_t = score1;
////                     staticOnly.add(lemma+"_"+tk.tag);
//                 }
//                 else if(w[0] != 50.0 && score1==-10){
//                     score_t =score2;
////                     domainOnly.add(lemma+"_"+tk.tag);
//                 }
          // static only
//                    if(score1[0]!=-10){
//                     pos= pos + modify(score1,i,tokens)[0];
//                     neg= neg + modify(score1,i,tokens)[1];
//                    }
          // domain only         
                    if(w[0]!=50){
                     pos= pos + w[1]; //modify(score1,i,tokens)[0];
                     neg= neg + w[2]; //modify(score1,i,tokens)[1];
//                     System.out.println(lemma+"_"+partOfSpeech.substring(0, 1).toUpperCase()+"\t"+w[1]+"\t"+w[2]);
                    }
                 
         }
                 //normalization
//                 if(neu>0){
//                     pos=pos/neu;
//                     neg=neg/neu;
//                 }
////                 if(neg>0)
//                     neg=neg/nneg;
//               System.out.println("\t\t\t\t\t"+pos+"\t"+neg+"\n\n");
               
               
         
         }catch(Exception e){e.printStackTrace();}
         int clas =0;
         if(pos>=neg)
            clas = 1;
//         if(containPosEmo && !containNegEmo)
//             return new ClassifiedText(1, staticOnly, domainOnly, overlapAggree, overlapDisaggree);
//         if(!containPosEmo && containNegEmo)
//             return new ClassifiedText(0, staticOnly, domainOnly, overlapAggree, overlapDisaggree);
         
//         System.out.println("...........................................");
         return new ClassifiedText(clas, staticOnly, domainOnly, overlapAggree, overlapDisaggree);
       
    }
    
//    private SentiWord getAveragePoS(String term){
//        double sumPos=0.0,sumNeg=0.0, count=0.0;
//        String[] pos={"N","V","J","R"};
//        for(String s:pos){
//        SentiWord score=swn.getScore(term, s, -1);
//        if(!score.pol.equals("non")){
//            sumPos+=score.getPos();
//            sumNeg+=score.getNeg();
//            count+=1;
//        }
//        }
//        if(count>0.0)
//            return new SentiWord("neu",sumPos/count, sumNeg/count);
//        return new SentiWord("non",sumPos, sumNeg);
//    }
    
    private Double[] modify(Double[] score ,int index, Object[] context){
        if(score[0]==0 && score[1]==1)
            return score;
        Double[] returnScore = score;
        for(int i = Math.max(0, index-3); i<Math.min((context.length-1),index+4); i++){
            String term=((TaggedToken)context[i]).token.trim().toLowerCase();
            if(i == index || punctuationReached(i, index, context) || term==null)
                continue;
            else{
//                if(modifier.isNegator(term)){
////                  System.out.println(term+"\t is negator");
////                    if(returnScore[0]>=returnScore[1])
////                        returnScore[0]=0.0;
////                    else
////                         returnScore[1]=0.0;
//                  double temp = returnScore[0];
//                  returnScore[0] = returnScore[1];
//                  returnScore[1] = temp;
//                  }
                if(modifier.isIntensifier(term)){
                    double strength = modifier.getStrength(term);
                    if (returnScore[0] >= returnScore[1])
                        returnScore[0] = returnScore[0]+ (strength*50)/100*returnScore[0];
                    else
                        returnScore[1] = returnScore[1]+ (strength*50)/100*returnScore[1];
                 }
               
                if(modifier.isDowntoner(term)){
                    double strength = modifier.getStrength(term);
                    if (returnScore[0] >= returnScore[1])
                        returnScore[0] = returnScore[0]- (strength*50)/100*returnScore[0];
                    else
                        returnScore[1] = returnScore[1]- (strength*50)/100*returnScore[1];
                 }
             }
         }
              
//      Normalization
//       if(returnScore[0] > 1)
//           returnScore[0]=1.0;
//       if(returnScore[0] < 0.0)
//           returnScore[0]=0.0;
//       if(returnScore[1] > 1)
//           returnScore[1]=1.0;
//       if(returnScore[0] < 0.0)
//           returnScore[0]=0.0;
//      System.out.println("new scores "+"\t"+returnScore[0]+" "+returnScore[1]);
              return returnScore;
          }
    
    private boolean punctuationReached(int contextTermIndex, int modifyTermIndex, Object[] context){
        if(modifyTermIndex>contextTermIndex)
            for(int i=contextTermIndex; i<modifyTermIndex; i++)
                if(((TaggedToken)context[i]).token.equals(".") || ((TaggedToken)context[i]).token.equals(","))
                    return true;
        else
            for(int j=contextTermIndex; j>modifyTermIndex; j--)
                if(((TaggedToken)context[i]).token.equals(".") || ((TaggedToken)context[i]).token.equals(","))
                    return true;
                    
        return false;
    }

  
    
}
