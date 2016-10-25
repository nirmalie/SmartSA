/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smartsa;

import SentiStrength.SentiStrengthApp;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import smartsa.Aligner;

/**
 *
 * @author 0611397
 */
public class MultiFileClassifier {
    
     public static void main(String[] vars){
         //                 alpha, beta, accuracy
         double[] result = {0.0,0.0,0.0,0.0,0.0,0.0,0.0};
  
        try{
        smartsa.Hybrid clf = new smartsa.Hybrid();
        SentiStrengthApp st = new SentiStrengthApp();
        java.io.File testFiles[] = new java.io.File("C:\\mydata\\TestAll").listFiles();
        double alpha=0.0, beta=0.0; //modify accordingly when mixing
        
        for(int i=0; i<testFiles.length; i++){
            double TP=0.0, FP=0.0, TN=0.0, FN=0.0;
            int neu=0;
            BufferedReader reader = new BufferedReader(new FileReader(testFiles[i]));
            String s="";
            String[] data;
            while((s=reader.readLine()) != null){
                 if (s.isEmpty()) continue;
                 
                 data=s.split("\\t");
                 if (data[2].trim().equalsIgnoreCase("neutral")){ 
                     neu++;
                     continue;
                 }
                 int cl = Integer.parseInt(st.polarityDetection(data[1]).split("\\s")[2]);
//                 System.out.println(data[1]+st.polarityDetection("what happy thing is going on here"));
                 
//                 ClassifiedText tx =clf.classify(data[1], 0.5, alpha, 0);
//                 if(tx.getClas()>0 && data[2].trim().equalsIgnoreCase("positive"))
//                    TP++;
//                 else if(tx.getClas()>0 && data[2].trim().equalsIgnoreCase("negative"))
//                    FP++;
//                 else if(tx.getClas()==0 && data[2].trim().equalsIgnoreCase("negative"))
//                    TN++;
//                 else if(tx.getClas()==0 && data[2].trim().equalsIgnoreCase("positive"))
//                    FN++;
                 
                  if(cl>0 && data[2].trim().equalsIgnoreCase("positive"))
                    TP++;
                 else if(cl>0 && data[2].trim().equalsIgnoreCase("negative"))
                    FP++;
                 else if(cl<0 && data[2].trim().equalsIgnoreCase("negative"))
                    TN++;
                 else if(cl<0 && data[2].trim().equalsIgnoreCase("positive"))
                    FN++;
            }
        
        
   
            Double pprec = (Math.round((TP/(TP+FP)) * 1000.0 ) / 1000.0)*100;
            Double prec = (Math.round((TP/(TP+FN)) * 1000.0 ) / 1000.0)*100;
            Double nprec= (Math.round((TN/(TN+FN)) * 1000.0 ) / 1000.0)*100;
            Double nrec = (Math.round((TN/(TN+FP)) * 1000.0 ) / 1000.0)*100;
            
            Double pF1 = (Math.round( ((2*pprec*prec)/(pprec+prec)) * 1000.0 ) / 1000.0);
            Double nF1 = (Math.round( ((2*nprec*nrec)/(nprec+nrec)) * 1000.0 ) / 1000.0);
            Double avrF1 = (Math.round(((pF1+nF1)/2) * 1000.0 ) / 1000.0);
   
            System.out.println(testFiles[i].getName());
            System.out.println("--------------------");
            System.out.println(TP +"\t"+FP); 
            System.out.println(FN +"\t"+TN); 
            System.out.println("neutral" +"\t"+neu); 
            System.out.println(pprec+"\t"+prec+"\t"+pF1+"\t"+nprec+"\t"+nrec+"\t"+nF1+"\t"+avrF1);
            
//            System.out.println("alpha = "+alpha +"\t beta = "+beta +"\t avrF1 = "+avrF1+"\t Pp = "+pprec+"\t Pr = "+prec+"\t Np = "+nprec+"\t Nr = "+nrec);
//            System.out.println(result[3]+"&"+result[4]+"&"+result[5]+"&"+result[6]+"&"+result[2]);
    
        }
            
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
     
}
