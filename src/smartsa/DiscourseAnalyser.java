/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smartsa;

import discourse.DiscourseStats;
import java.util.ArrayList;

/**
 *
 * @author 0611397
 */
public class DiscourseAnalyser {
    
    //discourse markers order
    //0-Assesment, 1-Authority, 2-Causation, 3-Conditionals, 4-Contrast, 5-Difficulty,
    //6-Doubt, 7-Emphasis, 8-Generalisation, 9-Inconsistency, 10-InYourShoes,
    //11-Necessity, 12-Possibility, 13-Priority, 14-RhetoricalQ, 15-Structure, 16-Wants
    double[] scoreb4 = new double[]{100,100,150,50,100,100,50,150,150,50,100,100,50,150,100,100,150};
    double[] scoreAfter = new double[]{150,150,150,50,150,100,50,150,150,50,150,150,50,150,150,100,150};
    
    private static String addr = "C:\\mydata\\discmarkers\\";
    ArrayList[] markers;

    public DiscourseAnalyser() {
        markers = DiscourseStats.getDiscourseList();
    }
    
    
    public boolean isAssessment(String text){
        for(int i=0; i<markers[0].size(); i++)
            
        return false;
    }
    
}

