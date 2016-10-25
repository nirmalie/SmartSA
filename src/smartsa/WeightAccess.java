/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smartsa;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

/**
 *
 * @author 0611397
 */
public class WeightAccess {
    
    
//    private String location = "C:\\mydata\\general\\sentenceLevel\\GenLexiconTF-DFNormByDF2New.txt";
    private String location = "C:\\mydata\\train20k\\pPMIlex.txt";
//    private String location = "C:\\mydata\\digg\\train\\sentenceLevel\\data\\newlexAll.txt";
//    private String location = "C:\\Aminu\\EM-Sentiment Lexicons\\EM-Sentiment Lexicons\\Lexicon-nopos\\Lexicon-1.txt";
//   
    private HashMap<String, Double[]> dict;

	public WeightAccess() {

		dict = new HashMap<String, Double[]>();
		try {
			BufferedReader csv = new BufferedReader(new FileReader(location));
			String line = "";

			while ((line = csv.readLine()) != null) {
				if (line.isEmpty()) {
					continue;
				}
				String[] data = line.split("\t");
                                Double[] scores = new Double[3];
                                //IG
                                scores[0] = Double.parseDouble(data[1]);
                                //pos
                                scores[1] = Double.parseDouble(data[2]);
                                //neg
                                scores[2] = Double.parseDouble(data[3]);
//                              if(scores[0]>0)
                                dict.put(data[0], scores);
                      }
                  } catch (Exception e) {
			e.printStackTrace();}
            }
        
        
        
        
        public Double[] getWeights(String term){
            Double[] returnValue = dict.get(term);
            if(!(returnValue == null))
            return returnValue;
         return new Double[]{50.0,0.0,0.0};
        }
    
}
