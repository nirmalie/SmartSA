/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smartsa;

/**
 *
 * @author 0611397
 */
public class Term {
    
    private double freq;
    private double pdf;
    private double ndf;
    private int docCount;

    public Term() {
       
    }

    public Term(double freq, double pdf, double ndf, int docCount) {
        this.freq = freq;
        this.pdf = pdf;
        this.ndf = ndf;
        this.docCount = docCount;
    }
    
    

    public double getFreq() {
        return freq;
    }

    public double getPdf() {
        return pdf;
    }

    public void setFreq(double freq) {
        this.freq = freq;
    }

    public void setPdf(double pdf) {
        this.pdf = pdf;
    }

    public double getNdf() {
        return ndf;
    }

    public void setNdf(double ndf) {
        this.ndf = ndf;
    }

    public int getDocCount() {
        return docCount;
    }

    public void setDocCount(int docCount) {
        this.docCount = docCount;
    }
    
    
    
}
