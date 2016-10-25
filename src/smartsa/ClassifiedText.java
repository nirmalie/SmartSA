/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smartsa;

import java.util.ArrayList;

/**
 *
 * @author 0611397
 */
public class ClassifiedText {
    private int clas;
    private ArrayList staticOnly;
    private ArrayList domainOnly;
    private ArrayList overlapAggree;
    private ArrayList overlapDisaggree;

    public ClassifiedText(int clas, ArrayList staticOnly, ArrayList domainOnly, ArrayList overlapAggree, ArrayList overlapDisaggree) {
        this.clas = clas;
        this.staticOnly = staticOnly;
        this.domainOnly = domainOnly;
        this.overlapAggree = overlapAggree;
        this.overlapDisaggree = overlapDisaggree;
    }

   

    public void setClas(int clas) {
        this.clas = clas;
    }

    public void setStaticOnly(ArrayList staticOnly) {
        this.staticOnly = staticOnly;
    }

    public void setDomainOnly(ArrayList domainOnly) {
        this.domainOnly = domainOnly;
    }

    public void setOverlapAggree(ArrayList overlapAggree) {
        this.overlapAggree = overlapAggree;
    }

    public void setOverlapDisaggree(ArrayList overlapDisaggree) {
        this.overlapDisaggree = overlapDisaggree;
    }

    public int getClas() {
        return clas;
    }

    public ArrayList getStaticOnly() {
        return staticOnly;
    }

    public ArrayList getDomainOnly() {
        return domainOnly;
    }

    public ArrayList getOverlapAggree() {
        return overlapAggree;
    }

    public ArrayList getOverlapDisaggree() {
        return overlapDisaggree;
    }
    
    
    
}
