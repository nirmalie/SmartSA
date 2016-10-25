/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smartsa;

import cmu.arktweetnlp.Tagger;
import cmu.arktweetnlp.Tagger.TaggedToken;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;

/**
 *
 * @author 0611397
 */
public class TestSetGenerator {
    
    
    public static void main(String argv[]){
        try{
        Tagger tagger = new Tagger();
        tagger.loadModel("C:\\mydata\\ark-tweet-nlp-0.3.2\\model.20120919");
        File sourceDir = new File("C:\\RGU\\sentenceDataset\\Entertainment");
        File[] docs = sourceDir.listFiles();
        int posFileCount=1;
        int negFileCount=1;
        for(int i=0; i<docs.length; i++){
            if(posFileCount>200 && negFileCount>200)
                    break;
                BufferedReader docReader = new BufferedReader(new FileReader(docs[i]));
                String s ="";
                String docContent ="";
                while ((s = docReader.readLine()) != null) {
                    if (s.isEmpty())
                        continue;
                    docContent = docContent + s;
                }
                if(docContent.length() < 5)
                    continue;
                List <TaggedToken> tokens = tagger.tokenizeAndTag(docContent);
                for(TaggedToken term: tokens){
//                    if(Modifier.isEmoticon(term.token)){
                    String st= term.token.replaceAll("\\s","");
                        Double emovalue = Modifier.getEmoticonVal(st);
                        if (emovalue == 1 && posFileCount <= 200){
                            BufferedWriter wr = new BufferedWriter(new FileWriter("C:\\mydata\\testdata\\entertainment\\pos\\"+posFileCount+".txt"));
                            wr.write(docContent);
                            wr.close();
                            posFileCount++;
                        }
                        if (emovalue == -1 && negFileCount <= 200){
                            BufferedWriter wr = new BufferedWriter(new FileWriter("C:\\mydata\\testdata\\entertainment\\neg\\"+negFileCount+".txt"));
                            wr.write(docContent);
                            wr.close();
                            negFileCount++;
                        }
//                    }
                    
                }
        }
    
        }catch(Exception e){e.printStackTrace();}
    }
    
    
}
