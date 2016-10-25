/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package smartsa;

/**
 *
 * @author Aminu
 */

import cmu.arktweetnlp.Tagger.TaggedToken;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.*;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.pipeline.*;
import java.util.*;



public class SmartSA {
    
private Properties props;
private StanfordCoreNLP pipeline;
private SWNScores senti;
//private Wsd wsd;
private Modifier modifier;
Double posInstances =0.0;
Double negInstances =0.0;
int sentTerms =0;
boolean caps=false, repeat=false, emos=false, discourse=false;

ArrayList<Token> tokens = new ArrayList<Token>();


public SmartSA() {
    props = new Properties();
    props.put("annotators", "tokenize, ssplit, pos, lemma");
    pipeline = new StanfordCoreNLP(props);
    try{
    senti = new SWNScores("C:\\Aminu\\Aminu\\Documents\\Projects\\Ebuka\\SentiTweetsAnalysis\\SentiTweets\\SentiWordNet_3.0.0.txt");
    }catch(Exception e){e.printStackTrace();}
   // wsd = new Wsd();
    modifier = new Modifier();
    }//end constructor



   public ArrayList<Token> getTokens() {
        return tokens;
    }
   
   public Double[] getSentimentScores(String text){
       tokens.clear();
       Double[] docScores = new Double[]{0.0,0.0};
       posInstances =0.0;
       negInstances =0.0;
       try{
            //obtain input text
            String theText = text;
            
            //create an empty Annotation just with the given text
            Annotation document = new Annotation(theText);

            //run all Annotators on this text
            pipeline.annotate(document);

            //these are all the sentences in this document
            //a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
            List<CoreMap> sentences = document.get(SentencesAnnotation.class);

           //System.out.println(text);
           //Sentence Level
             Double[] senPosScore = new Double[sentences.size()];
             Double[] senNegScore = new Double[sentences.size()];
             Arrays.fill(senPosScore, 0.0); Arrays.fill(senNegScore, 0.0);

             sentTerms =0;
             //Get positive and negative scores foreach sentence
             for(int i=0; i<sentences.size(); i++){
                 Double[] senScore = getSenScore(sentences.get(i));
                 senPosScore[i] = senScore[0]; senNegScore[i] = senScore[1];
                 
                 //discourse modification
//                 Double[][] senScores = DiscourseMod(senPosScore, senNegScore, sentences.get(i));
             }
             
             
             
            Arrays.sort(senPosScore); 
            Arrays.sort(senNegScore);
            
            docScores[0] = Math.round(sum(senPosScore)*1000.0)/1000.0;
            docScores[1] = Math.round(sum(senNegScore)*1000.0)/1000.0;
            
//            Max aggeration strategy
//            docScores[0] =senPosScore[senPosScore.length-1];
//            docScores[1] =senNegScore[senNegScore.length-1];
              
//            Normalization
//            if(posInstances >0)
//                docScores[0] =Math.round((sum(senPosScore)/posInstances) *1000.0)/1000.0;
//            if(negInstances >0)
//                docScores[1] =Math.round((sum(senNegScore)/negInstances) *1000.0)/1000.0;
//            }

       }catch(Exception e){e.printStackTrace();}
    return docScores;
   }

             private Double[] getSenScore(CoreMap sentence){
//             System.out.println("\t" + sentence.get(TextAnnotation.class));
             Double[] tokenPosScore = new Double[sentence.get(TokensAnnotation.class).size()];
             Double[] tokenNegScore = new Double[sentence.get(TokensAnnotation.class).size()];
             Arrays.fill(tokenPosScore, 0.0); Arrays.fill(tokenNegScore, 0.0);

             int index=0;
             for(CoreLabel token: sentence.get(TokensAnnotation.class)){
//                 if (modifier.isIntensifier(token.lemma())|| modifier.isDowntoner(token.lemma()))
//                     continue;
                 Double[] tokenScore = getTokenScore(token, index, sentence);
                 tokenPosScore[index] = tokenScore[0]; tokenNegScore[index] = tokenScore[1];
                 index++;
                 //some display
                 
             }
             //optinal in case of max aggregation
             Arrays.sort(tokenPosScore); Arrays.sort(tokenNegScore);
             
            // Double[] retArray = new Double[] {tokenPosScore[tokenPosScore.length-1], tokenNegScore[tokenNegScore.length-1]};
           //  sum instead of max

             Double sentencePosScore = sum(tokenPosScore); // tokenPosScore[tokenPosScore.length-1];
             Double sentenceNegScore = sum(tokenNegScore); //tokenNegScore [tokenNegScore.length-1];
             Double[] retArray = new Double[] {sentencePosScore, sentenceNegScore};
             
             System.out.println(sentencePosScore +"\t" +sentenceNegScore);
             return retArray;
    }
             
             
             
          // modifier method   
          private Double[] modify(Double[] score, CoreLabel token,int index, CoreMap context){
//              System.out.println("old scores "+"\t"+score[0]+" "+score[1]);
              if(score[0]==0 && score[1]==0)
                      return score;
              Double[] returnScore = {score[0],score[1]};
              for(int i = Math.max(0, index-5); i<Math.min((context.get(TokensAnnotation.class).size()),index+6); i++){
//                  System.out.println(i+"---" +index+" "+(context.get(TokensAnnotation.class)).get(i).lemma());
                  if(i == index){
//                      System.out.println(i+" skipped");
                      continue;}
                  else{
                      String term=context.get(TokensAnnotation.class).get(i).lemma();
//                  System.out.println(term);
                  
                if(modifier.isNegator(term)){
//                  System.out.println(term+"\t is negator");
                  double temp = returnScore[0];
                  returnScore[0] = returnScore[1];
                  returnScore[1] = temp;
                  }
                
//                 if(modifier.isIntensifier(term)){
//                    double strength = modifier.getStrength((context.get(TokensAnnotation.class)).get(i).lemma());
//                    if (returnScore[0] >= returnScore[1])
//                        returnScore[0] = returnScore[0]+ (strength*50)/100*returnScore[0];
//                    else
//                        returnScore[1] = returnScore[1]+ (strength*50)/100*returnScore[1];
//                 }
//                 
//                  if(modifier.isDowntoner(term)){
//                    double strength = modifier.getStrength((context.get(TokensAnnotation.class)).get(i).lemma());
//                    if (returnScore[0] >= returnScore[1])
//                        returnScore[0] = returnScore[0]- (strength*50)/100*returnScore[0];
//                    else
//                        returnScore[1] = returnScore[1]- (strength*50)/100*returnScore[1];
//                  }
                  
                  }
                  }
              
//              Normalization
//               if(returnScore[0] > 1)
//                   returnScore[0]=1.0;
//               if(returnScore[0] < 0.0)
//                   returnScore[0]=0.0;
//               if(returnScore[1] > 1)
//                   returnScore[1]=1.0;
//               if(returnScore[0] < 0.0)
//                   returnScore[0]=0.0;
//              System.out.println("new scores "+"\t"+returnScore[0]+" "+returnScore[1]);
              return returnScore;
          }

          //method token score
          private Double[] getTokenScore(CoreLabel token,int index, CoreMap sentence){
//          Double[] ret = new Double[]{0.0,0.0};
          
//          System.out.println(token.originalText() +token.lemma() +token.tag()  + ":received.. ");
          //int sense = wsd.disambiguate(token, sentence);
          //Extract sentiment scores from SentiWordNet

          Double[] ret = senti.extract(token.lemma(), token.tag());
//          SentiWord sw = senti.getScore("very", "R", -1);
//          System.out.println(sw.pos + sw.neg);
//          ret[0]=sw.pos; ret[1]=sw.neg;z
          //modify score
          if(ret[0]<0)
              return new Double[]{0.0,0.0};
          
          if(ret[0] >0 || ret[1]>0){
          Double[] modifiedScore = modify(ret, token, index, sentence);
          System.out.println(token.lemma()+"  " +ret[0]+"  " + ret[1] +"  " + modifiedScore[0]+" "+modifiedScore[1]);
          ret[0]=modifiedScore[0];
          ret[1]=modifiedScore[1];
          }
//          System.out.println(token.lemma()+"  " +token.tag()+"  " + ret[0] +"  " + ret[1]);
          String st = token.lemma()+"_"+token.tag().charAt(0);
          tokens.add(new Token(st, ret[0], ret[1]));
          if(ret[0]>0)
              posInstances++;
          if(ret[1]>0)
              negInstances++;
          return ret;
     }
//           private boolean isSubjective(Double pos, double neg){
//               Double sum = pos+neg;
//               return sum >= 1-sum;
//           
//           }
//
          private Double sum(Double[] scores){
              Double sum =0.0;
              for(int i =0; i<scores.length; i++)
                  sum += scores[i];
              return sum;
          }
          
//          public Double[][] discourseMod(Double[] pos, Double[] neg, String sentence){
//              
//              //check for occurence of each marker
//              
//              
//              //get their scores
//              
//              //modify scores
//              
//              return
//          }
          
//          Main Method
          public static void main(String args[]){
          Double [] scores = new SmartSA().getSentimentScores("not stupid");
          System.out.println("pos = "+scores[0] +"\t"+ "neg = "+scores[1]);
          }
}

