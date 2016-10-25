/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smartsa;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author 1211707
 */
public class LexiconbasedFeatures {
     public static void main(String[] args) {
        
        FileInputStream fstream;
        FileInputStream fstream1;
        FileInputStream fstream2;
        FileInputStream fstream3;
        FileInputStream fstream4;
        FileInputStream fstream5;
        DataInputStream in;
        DataInputStream in1;
        DataInputStream in2;
        DataInputStream in3;
        DataInputStream in4;
        DataInputStream in5;
        BufferedReader br;
        BufferedReader br1;
        BufferedReader br2;
        BufferedReader br3;
        BufferedReader br4;
        BufferedReader br5;
        PrintWriter out;
        
        try {
            // Open the file that is the first 
            // command line parameter
            fstream = new FileInputStream("C:\\SEMEVAL2013\\traindatawithclass.txt");
            // Get the object of DataInputStream
            in = new DataInputStream(fstream);
            br = new BufferedReader(new InputStreamReader(in));
            
            fstream1 = new FileInputStream("C:\\Aminu\\EM-Sentiment Lexicons\\EM-Sentiment Lexicons\\Lexicon-nopos\\Lexicon-1.txt");
            // Get the object of DataInputStream
            in1 = new DataInputStream(fstream1);
            br1 = new BufferedReader(new InputStreamReader(in1));
            
      
            FileWriter fr =  new FileWriter("C:\\SEMEVAL2013\\countconcepts.txt", true);
           
            out = new PrintWriter(fr);
//           
            //output to the file a line


            //close the file (VERY IMPORTANT)

            int count = 0;
            int i = 0 ;
            int j = 0;
            int k = 0;
            int countPos = 0;
			int countNeg = 0;
			int countNeutral = 0;
          Double [] scoreSurp = new Double [24390];
            Double[] scores = new Double[7];
           // String[] termswithscores = new String[9124];
           // String[] tweets = new String[2815];
            String strLine;
            String strLine1;
            String[] S;
            String[] S1;
            String[] tmp1;
            String[] tmp;
            int counter = 0;
            String [] vectors = new String[8000];
            double average;
            double comedy = 0.0;
            double less_comedy = 0.0;
            String word = null;
            Set<String> s1 = new HashSet<>();
             Set<String> s2 = new HashSet<>();
              Map<String, String> termswithscores = new HashMap<String, String>();
              Map<String, String> termweights = new HashMap<String, String>();
              
            boolean p;
            
            for(j = 0 ; j < vectors.length; j++)
            {
                vectors[j] = null;
            }
            //Read File Line By Line
             while ((strLine = br1.readLine())!= null ) {
                 
                 S1 = strLine.split("\t");
                 termswithscores.put(S1[0], S1[1] + "\t" + S1[2] + "\t" + S1[3] );
                 i++;
             }

            while ((strLine = br.readLine())!= null ) {
                // out.println(strLine);
                 S1 = strLine.split("\t");
                 
                 
                System.out.println("For document " + k);
                 S = S1[1].trim().split(" ");
                 
                 for(j = 0 ; j < S.length; j++)
                 { 
               
                    
                    if(termswithscores.containsKey(S[j]))
                    {   
                       
                        
                        tmp = termswithscores.get(S[j]).split("\t");
                       
                           if(Double.parseDouble(tmp[0]) > Double.parseDouble(tmp[1]) && Double.parseDouble(tmp[0]) > Double.parseDouble(tmp[2])  )
                           {      
                               
                                 
                                   countPos++;
                           
                           }
                           if(Double.parseDouble(tmp[1]) > Double.parseDouble(tmp[0]) && Double.parseDouble(tmp[1]) > Double.parseDouble(tmp[2])    )
                           {   
                               
                                   countNeg++;
                             
                           }
                           if(Double.parseDouble(tmp[2]) > Double.parseDouble(tmp[0]) && Double.parseDouble(tmp[2]) > Double.parseDouble(tmp[1])    )
                           { 
                                
                                   countNeutral++;
                              
                               
                           }
                           
                       
              }
                 
                 }
                 
//                 
                 
                  out.println(S1[0] + "\t" + countPos + "\t" + countNeg + "\t" + countNeutral );
                  countPos =0;
                  countNeg = 0;
                  countNeutral = 0;				  
                 
                 
                // out.println();
             }
            
         
         
            in.close();
           out.flush();
          
           out.close();
        } catch (Exception e) {//Catch exception if any
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {

        }
    }  
}
