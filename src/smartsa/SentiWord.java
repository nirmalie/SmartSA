/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package smartsa;

/**
 *
 * @author Aminu
 */
public class SentiWord {


    public String pol;
    public Double pos;
    public Double neg;


    public SentiWord(){

    }

     public SentiWord(String pol, Double pos, Double neg){
         this.pol=pol;
         this.pos=pos;
         this.neg=neg;
    }

    public void setNeg(Double neg) {
        this.neg = neg;
    }

    public void setPol(String pol) {
        this.pol = pol;
    }

    public void setPos(Double pos) {
        this.pos = pos;
    }

    public Double getNeg() {
        return neg;
    }

    public String getPol() {
        return pol;
    }

    public Double getPos() {
        return pos;
    }





}
