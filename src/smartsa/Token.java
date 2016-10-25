/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package smartsa;

/**
 *
 * @author Aminu
 */
public class Token {

    private int index;
    private String PoStag;
    private String token;
    private String lemma;
    private Double pos;
    private Double neg;
    private int freq;

    public Token(int index, String token, String lemma, Double pos, Double neg, int freq) {
        this.index = index;
        this.token = token;
        this.lemma = lemma;
        this.pos = pos;
        this.neg = neg;
        this.freq = freq;
    }


    public Token(String lemma, Double pos, Double neg) {
        this.lemma = lemma;
        this.pos = pos;
        this.neg = neg;
    }

    public String getPoStag() {
        return PoStag;
    }

    public void setPoStag(String PoStag) {
        this.PoStag = PoStag;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLemma() {
        return lemma;
    }

    public void setLemma(String lemma) {
        this.lemma = lemma;
    }

    public Double getNeg() {
        return neg;
    }

    public void setNeg(Double neg) {
        this.neg = neg;
    }

    public Double getPos() {
        return pos;
    }

    public void setPos(Double pos) {
        this.pos = pos;
    }

    public int getFreq() {
        return freq;
    }

    public void setFreq(int freq) {
        this.freq = freq;
    }



}
