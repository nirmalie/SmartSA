/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smartsa;

//import twitter4j.http.AccessToken;

 

import java.io.*;
import twitter4j.*;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.auth.AccessToken;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.lang.System.*;

public class SearchByID {

    private final static String CONSUMER_KEY = "oF1A4VFV55crHmmnvqpGsoqye";
    private final static String CONSUMER_KEY_SECRET = "07wo8K5ZbbOZHRtiBkBDrhCHDX5xhugsYNJD0Zv7gqXECm4MB5";
    private final static String ACCESS_TOKEN = "792315775-bjRq713Wi37uDsXK7L7OTq1uNpZvjGHe3UMjNVsz";
    private final static String ACCESS_TOKEN_SECRET = "2RMTqfQWfrtgktbQvVsMmZU8bZHvrVjlCZjmUb7N0bwiW";

    public static void main(String[] args) {

        try {
            FileInputStream fstream = new FileInputStream("C:\\Python27\\input.txt");
            // Get the object of DataInputStream
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            PrintWriter out = new PrintWriter(new FileWriter("C:\\Python27\\output.txt", true));
            // String tweetID="143533032714940417";
            String strLine;
            String S[];
            String screenname;
            String statustext;
            int count = 0;
            //for access with proxy
            Properties systemSettings = System.getProperties();
            systemSettings.put("http.proxyHost", "proxy.rgu.ac.uk");
            systemSettings.put("http.proxyPort", "8080");

//        //Twitter twitter = new TwitterFactory().getInstance();
            Twitter twitter = new TwitterFactory().getSingleton();
            twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_KEY_SECRET);
            AccessToken oathAccessToken = new AccessToken(ACCESS_TOKEN, ACCESS_TOKEN_SECRET);
            twitter.setOAuthAccessToken(oathAccessToken);
//        
//       
//        //code to retrieve tweet    

            while ((strLine = br.readLine()) != null){
                //S = strLine.split("\t");
                //for (String word : S) {
                //System.out.println(S[0]+ "\t" + "Hi"+ "\t" + S[1]);
                //out.println(S[0]+ "\t" + "Hi"+ "\t" + S[1]);
                //count++;


                try {
                    // System.out.println(twitter.getTermsOfService());
                    Status status = twitter.showStatus(Long.parseLong(strLine));
                    if (status == null) {
                    } else {
                        count++;
                      //  System.out.println( count++ + "\t" + S[0]+ "\t"+ "@" + status.getUser().getScreenName()+ " - " + status.getText()+ "\t" + S[1]);
                        screenname = status.getUser().getScreenName();
                        statustext = status.getText();
//                        out.println(S[0] + "\t" + "@" + screenname + " - " + statustext + "\t" + S[1]);
                        out.write(strLine + "\t" + "@" + screenname + " - " + statustext + "\n");
                        System.out.println(count);
                        Thread.sleep(1000000);
                    }
                    // System.out.print(count);
                } catch (TwitterException e) {
                    System.err.print("Failed to search tweets: " + e.getMessage());
                }

            }

            in.close();
            out.close();
        } catch (Exception e) {//Catch exception if any
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace(out);
        }
    }
}
