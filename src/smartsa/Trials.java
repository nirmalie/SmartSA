/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smartsa;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import smartsa.Aligner;

/**
 *
 * @author 0611397
 */
public class Trials {
    
     public static void main(String[] vars){
        try{
        java.io.File posFiles[] = new java.io.File("C:\\mydata\\test\\pos").listFiles();
        java.io.File negFiles[] = new java.io.File("C:\\mydata\\test\\neg").listFiles();
        SentiWordNetDemoCode swn = new SentiWordNetDemoCode("C:\\Aminu\\Aminu\\Documents\\Projects\\Ebuka\\SentiTweetsAnalysis\\SentiTweets\\SentiWordNet_3.0.0.txt");
        double sixtyPercentPos=Math.round(0.6*posFiles.length);
        double sixtyPercentNeg=Math.round(0.6*negFiles.length);
        
        Random randomGenerator = new Random();
        List optPos=new ArrayList();
        for (int idx = 1; idx <= sixtyPercentPos; ++idx){
            optPos.add(randomGenerator.nextInt(posFiles.length));
        }
        List testPos=new ArrayList();
        for(int k=0; k<posFiles.length; k++){
            if(!optPos.contains(k))
                testPos.add(k);
        }
        Random randomGen = new Random();
        List optNeg=new ArrayList();
        for (int idx = 1; idx <= sixtyPercentNeg; ++idx){
            optNeg.add(randomGen.nextInt(negFiles.length));
        }
        List testNeg=new ArrayList();
        for(int k=0; k<negFiles.length; k++){
            if(!optNeg.contains(k))
                testNeg.add(k);
        }
        
        double[] accuracies=new double[25];
        double[] pp=new double[25];
        double[] pr=new double[25];
        double[] np=new double[25];
        double[] nr=new double[25];
        for(int trial =0; trial<25; trial++){
          //                 alpha, beta, accuracy
         double[] result = {0.0,0.0,0.0,0.0,0.0,0.0,0.0};
         smartsa.Hybrid clf = new smartsa.Hybrid();
         for(float alpha=0; alpha<=1.0; alpha+=0.1){
//             for(float beta=0; beta<=1.0; beta+=0.1){
             double TP=0.0, FP=0.0, TN=0.0, FN=0.0;
        
        for(int i=0; i<optPos.size(); i++){
        BufferedReader csv = new BufferedReader(new FileReader(posFiles[(int)optPos.get(i)]));
        String s="", data="";
            while ((s = csv.readLine()) != null) {
				if (s.isEmpty()) {
					continue;
				}
                                data = data+s;
            }
            ClassifiedText tx =clf.classify(data, 0.5, alpha, 0);
            int a = tx.getClas();
            if(a==1)
               TP++; 
            else
               FP++;
        }
            
        
//        System.out.println("NEGATIVE====================================================");
        for(int j=0; j<optNeg.size(); j++){
        BufferedReader csv = new BufferedReader(new FileReader(negFiles[(int)optNeg.get(j)]));
        String str="", text="";
            while ((str = csv.readLine()) != null) {
				if (str.isEmpty()) { 
					continue;
				}
                                text = text+str;
            }
            
            ClassifiedText tx =clf.classify(text, 0.5, alpha, 0);
            int a = tx.getClas();//            System.out.println(text);
            if(a==0)
              TN++; 
            else
              FN++;
            
        }
            
            
            Double pprec = (Math.round((TP/(TP+FP)) * 1000.0 ) / 1000.0)*100;
            Double prec = (Math.round((TP/(TP+FN)) * 1000.0 ) / 1000.0)*100;
            Double nprec= (Math.round((TN/(TN+FN)) * 1000.0 ) / 1000.0)*100;
            Double nrec = (Math.round((TN/(TN+FP)) * 1000.0 ) / 1000.0)*100;
            
            Double pF1 = (Math.round( ((2*pprec*prec)/(pprec+prec)) * 1000.0 ) / 1000.0);
            Double nF1 = (Math.round( ((2*nprec*nrec)/(nprec+nrec)) * 1000.0 ) / 1000.0);
            Double avrF1 = (Math.round(((pF1+nF1)/2) * 1000.0 ) / 1000.0);
            
            if(avrF1>result[2]){
                result[0]=alpha;
                result[1] =0;
                result[2] = avrF1;
                result[3] = pprec;
                result[4] = prec;
                result[5] = nprec;
                result[6] = nrec;
            }
//          }
         }
         
         //after optimsation
        double TP=0.0, FP=0.0, TN=0.0, FN=0.0; 
        for(int i=0; i<testPos.size(); i++){
        BufferedReader csv = new BufferedReader(new FileReader(posFiles[(int)testPos.get(i)]));
        String s="", data="";
            while ((s = csv.readLine()) != null) {
				if (s.isEmpty()) {
					continue;
				}
                                data = data+s;
            }
            ClassifiedText tx =clf.classify(data, 0.5, result[0], result[1]);
            int a = tx.getClas();
            if(a==1)
               TP++; 
            else
               FP++;
        }
        for(int j=0; j<testNeg.size(); j++){
        BufferedReader csv = new BufferedReader(new FileReader(negFiles[(int)testNeg.get(j)]));
        String str="", text="";
            while ((str = csv.readLine()) != null) {
				if (str.isEmpty()) { 
					continue;
				}
                                text = text+str;
            }
            
            ClassifiedText tx =clf.classify(text, 0.5, result[0], result[1]);
            int a = tx.getClas();//            System.out.println(text);
            if(a==0)
              TN++; 
            else
              FN++;
        }
         pp[trial] = (Math.round((TP/(TP+FP)) * 1000.0 ) / 1000.0)*100;
         pr[trial] = (Math.round((TP/(TP+FN)) * 1000.0 ) / 1000.0)*100;
         np[trial]= (Math.round((TN/(TN+FN)) * 1000.0 ) / 1000.0)*100;
         nr[trial] = (Math.round((TN/(TN+FP)) * 1000.0 ) / 1000.0)*100;
//         pF1[trial] = (Math.round( ((2*pprec*prec)/(pprec+prec)) * 1000.0 ) / 1000.0);
//         nF1[trial] = (Math.round( ((2*nprec*nrec)/(nprec+nrec)) * 1000.0 ) / 1000.0);
//         Double avrF1 = (Math.round(((pF1+nF1)/2) * 1000.0 ) / 1000.0);
//         accuracies[trial]=avrF1;
         System.out.println(trial+"trial");
        }
        double posp=sum(pp)/25;
        double posr=sum(pr)/25;
        double negp=sum(np)/25;
        double negr=sum(nr)/25;
        double pF1 = (Math.round( ((2*posp*posr)/(posp+posr)) * 1000.0 ) / 1000.0);
        double nF1 = (Math.round( ((2*negp*negr)/(negp+negr)) * 1000.0 ) / 1000.0);
        double avrF1 = (Math.round(((pF1+nF1)/2) * 1000.0 ) / 1000.0);
        System.out.println(posp+"&"+posr+"&"+negp+"&"+negr+"&"+avrF1);
         
         
        
     }catch(Exception e){e.printStackTrace();}
}

    
     
     private static void updateList(ArrayList localList, ArrayList globalList){
         for(Object o: localList){
             String s = (String)o;
             if(!globalList.contains(s))
                 globalList.add(s);
         }
     }
     
     private static void printList(ArrayList list){
         for(Object o:list)
             System.out.println((String)o);
     }
     private static double sum(double[] scores){
              double sum =0.0;
              for(int i =0; i<scores.length; i++)
                  sum += scores[i];
              return sum;
          }
}
