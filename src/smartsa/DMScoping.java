/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smartsa;

import SentiStrength.SentiStrengthApp;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author 0611397
 */
public class DMScoping {
    
    
    public static void main(String argv[]){
        SentiStrengthApp st = new SentiStrengthApp();
        ArrayList markers = new ArrayList();
        markers.add("because");
        markers.add("but");
        markers.add("therefore");
        int[][] markerInfo = new int[markers.size()][14];
        //initialise
        for(int k=0; k<markerInfo.length; k++)
            Arrays.fill(markerInfo[k],0);
        ArrayList manyMarkerText =  new ArrayList();
        try{
        BufferedReader reader = new BufferedReader(new FileReader("C:\\mydata\\goTweets\\train.csv"));
        String theText="";
            String s="";
            String[] data;
            while((s=reader.readLine()) != null){
                 if (s.isEmpty()) continue;
                data =s.split(",");
                theText = data[5];
                 if (theText.trim().isEmpty()) continue;
                 int c= Integer.parseInt(data[0].replaceAll("\"", ""));
       
//        int c = 1;
        for(int i=0; i<markers.size();i++){
            int[] indexes = find(theText,(String)markers.get(i));
            if (indexes[0]>0){
                String prev = getPrev(theText,indexes[0]);
                String succ = getSucc(theText,indexes[1]);
                int classPrev = 0;
                if(!prev.trim().isEmpty())
                    classPrev=Integer.parseInt(st.polarityDetection(prev).split("\\s")[2]);
                int classSucc = 0;
                if(!succ.trim().isEmpty())
                    classSucc=Integer.parseInt(st.polarityDetection(succ).split("\\s")[2]);
                if(c==0){
                    if(classPrev>0 && classSucc>0)
                        markerInfo[i][0]++;
                    else if(classPrev>0 && classSucc<0)
                        markerInfo[i][1]++;
                    else if (classPrev<0 && classSucc>0)
                        markerInfo[i][2]++;
                    else
                        markerInfo[i][3]++;
                   
                //    markerInfo[i][6]++;
                }
                if(c==4){
                   if(classPrev<0 && classSucc<0)
                        markerInfo[i][4]++;
                    else if(classPrev<0 && classSucc>0)
                        markerInfo[i][5]++;
                    else if (classPrev>0 && classSucc<0)
                        markerInfo[i][6]++;
                   else
                        markerInfo[i][7]++;
                   
                }
              }
            }
        
        }
        }catch(Exception e){e.printStackTrace();}
        printArray(markerInfo[0]);
        printArray(markerInfo[1]);
        printArray(markerInfo[2]);
        
        
    }
    public static int[] find(String text, String marker){
        Pattern p =Pattern.compile(marker);
        Matcher m = p.matcher(text);
        if (m.find())
          return new int[]{m.start(),m.end()};
        return new int[]{-1,-1};
    }
    public static String getPrev(String text, int index){
        return text.substring(0, index);
    }
    public static String getSucc(String text, int index){
        return text.substring(index);
    }
    public static void printArray(int[] arr){
        for(int i=0; i<arr.length; i++){
            System.out.println(arr[i] +" ");
        }
    }
    
}
