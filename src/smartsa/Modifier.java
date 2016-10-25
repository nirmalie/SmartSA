/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package smartsa;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Aminu
 */
public class Modifier {
    private static String[] negators = {"not", "don't", "dont", "never", "nothing", "nobody","no","n't","nowhere","none","havent","haven't",
                                        "hasnt","hasn't","hadnt","hadn't","cant","can't","couldnt","couldn't","shouldnt","shouldn't","wont",
                                        "won't","wouldnt","wouldn't","doesnt","doesn't","didnt","didn't","isnt","isn't","arent","aren't","aint",
                                        "ain't", "'t", "t"};

                                           //brooke stanford
    private static String[] intensifiers ={"immediately", "quite", "perfectly", "consistently" ,"really","clearly","obviously" ,"certainly",
                                          "completely","definitely", "absolutely" ,"highly","very","truly","especially","particularly",
                                          "Significantly","noticeably", "distinctively","frequently","Awfully" ,"Totally" ,"largely", "fully",
                                          "damn","intensively", "Downright", "Entirely", "strongly", "Remarkably", "Majorly","amazingly",
                                          "strikingly","stunningly","quintessentially", "Unusually", "Dramatically", "Intensely", "Extremely",
                                          "so","Incredibly", "Terribly", "Hugely", "Immensely", "Such", "Unbelievably","Insanely", "Outrageously",
                                          "Radically","exceptionally", "Exceedingly","Way", "vastly", "Deeply", "super", "Profoundly","Universally",
                                          "Abundantly","Infinitely","Enormously","Thoroughly","Passionately","tremendously","Ridiculously",
                                          "Obscenely", "Extraordinarily", "Spectacularly", "Phenomenally","Monumentally",};

                                           //brooke stanford
    private static String[] downtoners =   {"the_least", "less","barely","hardly","almost","not_too","only","a_little","a_little_bit","slightly",
                                           "marginally","relatively","mildly","moderately","somewhat","partially","a_bit","to_some_extent",
                                           "to_a_certain_extent","sort_of","sorta","kind_of","kinda","fairly","pretty","rather","more","small",
                                           "smaller","smallest","minor","moderate","mild","slight","slightest","insignificant","inconsequential",
                                           "low","lower","lowest","few","fewer","fewest","a_few","a_couple","a_couple_of","at_all","a_certain_amount_of",
                                           "some","a_little_bit_of","a_bit_of","a_bit_of_a","difficult_to","hard_to","tough_to","nowhere_near",
                                           "not_all_that","not_that","out_of",};

    private static String[] neutralizers=  {"should","could","may","might"};


    private static String addr = "C:\\SmartSAresources\\";
//    private ArrayList<String> ints;
//    private ArrayList<String> dims;
    private ArrayList<String> negs;
    private ArrayList<String> stops;
    private Map<String, Double> ints;
    private Map<String, Double> dims;
//    private Map<String, Double> negs;
   
    
   public Modifier(){
       ints = new HashMap<String,Double>();
       dims = new HashMap<String,Double>();
       negs = new ArrayList<String>();
       stops = new ArrayList();

        BufferedReader csv = null;
        BufferedReader csv2 = null;
        BufferedReader csv3 = null;

       try{
            csv = new BufferedReader(new FileReader(addr+"BoosterWordList.txt"));
            csv2 = new BufferedReader(new FileReader(addr+"NegatingWordList.txt"));
            csv3 = new BufferedReader(new FileReader(addr+"stopList.txt"));
            String s = "";
           // String[] data = "";
            while ((s = csv.readLine()) != null) {
                if (s.isEmpty()) {
                    continue;
                }
                String [] data = s.split("\t");
                Double strength = Double.parseDouble(data[1]);
                if (strength>0)
                    ints.put(data[0].trim().toLowerCase(),strength);
                else if (strength<0)
                    dims.put(data[0].trim().toLowerCase(),Math.abs(strength));
            }
             while ((s = csv2.readLine()) != null) {
                if (s.isEmpty()) {
                  continue;
                }
               negs.add(s.trim().toLowerCase());
            }
             while ((s = csv3.readLine()) != null) {
                if (s.isEmpty()) {
                  continue;
                }
               stops.add(s.trim());
            }
             csv.close();
             csv2.close();
             csv3.close();
       } catch(Exception e){e.printStackTrace();}
   }
   
   public  boolean isIntensifier(String str){
            if (ints.containsKey(str.trim().toLowerCase()))
            return true;
       return  false;
    }

public Double getStrength(String word){
    if (ints.containsKey(word))
        return ints.get(word.trim().toLowerCase());
    return dims.get(word.trim().toLowerCase());
    }

    public boolean isDowntoner(String str){
            if (dims.containsKey(str.trim().toLowerCase())  )
            return true;
       return  false;
    }

    
   public boolean isNegator(String str){       
            if (negs.contains(str.trim().toLowerCase()))
            return true;
       return  false;
    }
   public boolean isStop(String str){
            if (stops.contains(str.trim().toLowerCase()))
            return true;
       return  false;
    }

    public  static Double getEmoticonVal(String str){
       try{
            BufferedReader csv = new BufferedReader(new FileReader("C:\\SmartSAresources\\EmoticonLookupTable.txt"));
		//for(int i=0; i<intensifiers.length; i++) {
          //  ArrayList<String> ints = new ArrayList<String>();
            Map<String,Double> emo = new HashMap<String,Double>();
             String s ="";
            while ((s = csv.readLine()) != null) {
                if (s.isEmpty()) {
                    continue;
                }
                    String[] temp = s.split("\t");
                    emo.put(temp[0].trim().toLowerCase(), Double.parseDouble(temp[1]));
            }
            if (emo.containsKey(str.trim().toLowerCase()))
            return (Double)emo.get(str.trim().toLowerCase());
		//}
	}catch (Exception e){e.printStackTrace();}
       return  0.0;
    }



}
