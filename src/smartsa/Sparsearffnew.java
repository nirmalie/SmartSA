/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smartsa;

/**
 *
 * @author 1211707
 */
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
    import weka.core.Attribute;
    import weka.core.FastVector;
    import weka.core.Instance;
    import weka.core.Instances;
    import weka.core.converters.ArffSaver;
    import weka.filters.Filter;
    import weka.filters.unsupervised.instance.NonSparseToSparse;

public class Sparsearffnew {
 public static void main(String[] args) throws Exception 
   {   
       FileInputStream fstream;
        FileInputStream fstream1;
        FileInputStream fstream2;
        FileInputStream fstream3;
        FileInputStream fstream4;
        FileInputStream fstream5;
        FileInputStream fstream6;
        FileInputStream fstream7;
        FileInputStream fstream8;
        FileInputStream fstream9;
        DataInputStream in;
        DataInputStream in1;
        DataInputStream in2;
        DataInputStream in3;
        DataInputStream in4;
        DataInputStream in5;
        DataInputStream in6;
        DataInputStream in7;
        DataInputStream in8;
        DataInputStream in9;
        BufferedReader br;
        BufferedReader br1;
        BufferedReader br2;
        BufferedReader br3;
        BufferedReader br4;
        BufferedReader br5;
        BufferedReader br6;
        BufferedReader br7;
        BufferedReader br8;
        BufferedReader br9;
        String strLine;
        String strLine1;
        String strLine2;
        String strLine3;
        String strLine4;
        String strLine5;
        String strLine6;
        String strLine7;
        
        int i = 0 ;
        int j = 0 ;
        int p = 0 ;
       FastVector attributes;
       Instances dataSet;
       double[] values;
       String[] Docs;
       String[] features;
       Docs = new String[8021];
       String[] Features;
       Features = new String[38557];
       String [] S;
       attributes = new FastVector();
      
       fstream = new FileInputStream("C:\\SEMEVAL2013\\feaPruned.txt");
            // Get the object of DataInputStream
       in = new DataInputStream(fstream);
       br = new BufferedReader(new InputStreamReader(in));
       
        fstream1 = new FileInputStream("C:\\SEMEVAL2013\\traindata.txt");
            // Get the object of DataInputStream
        in1 = new DataInputStream(fstream1);
        br1 = new BufferedReader(new InputStreamReader(in1));
        
        fstream2 = new FileInputStream("C:\\SEMEVAL2013\\traindatawithclass.txt");
            // Get the object of DataInputStream
        in2 = new DataInputStream(fstream2);
        br2 = new BufferedReader(new InputStreamReader(in2));
        
        fstream3 = new FileInputStream("C:\\SEMEVAL2013\\valuesTF.txt");
          in3 = new DataInputStream(fstream3);
        br3 = new BufferedReader(new InputStreamReader(in3));  
      
      
         while ((strLine = br.readLine())!= null)
         {   
             Features[i]= strLine;
             attributes.addElement(new Attribute(Features[i]));
             i++;
         }
          Features[Features.length -1] = "classlabel"; 
          attributes.addElement(new Attribute("classlabel"));
          
          while((strLine = br2.readLine())!= null)
          {   
              S = strLine.trim().split("\t");
              S[S.length-1] = S[S.length-1].trim();
              
              //Docs is the labels
              Docs[j] = S[(S.length)-1];
               //  System.out.println(Docs[j]);
                 j++;
          }
       
       dataSet = new Instances("5000allclass", attributes, 0);
       
       while ((strLine1 = br3.readLine())!= null && p < Docs.length ) {
                
                
                values = new double[dataSet.numAttributes()];
               
                  
                    
                     
                    if(!Features[0].equalsIgnoreCase("classlabel"))
                    {  
                       
                          features = strLine1.split("\t");
                          
                          for(i = 1 ; i < features.length; i++)
                          {   
                            
                              values[i] = Double.parseDouble(features[i]);
                          }
                         
                          
                         
                    }
                     
                 
                 
                  if(Docs[p].equalsIgnoreCase("positive"))
                         {
                            
                             values[attributes.size()-1] = 1.0;
                         }
                          
                   if(Docs[p].equalsIgnoreCase("negative"))
                         {   
                             values[attributes.size()-1] = 2.0; 
                         }
                   if(Docs[p].equalsIgnoreCase("neutral"))
                         {   
                             values[attributes.size()-1] = 3.0; 
                         }

                 dataSet.add(new Instance(1.0, values));
               
               p++;
              in.close();
               
            }
       
       NonSparseToSparse nonSparseToSparseInstance = new NonSparseToSparse(); 
       nonSparseToSparseInstance.setInputFormat(dataSet); 
       Instances sparseDataset = Filter.useFilter(dataSet, nonSparseToSparseInstance);
       
      // System.out.println(sparseDataset);
       
      ArffSaver arffSaverInstance = new ArffSaver(); 
       arffSaverInstance.setInstances(sparseDataset); 
       arffSaverInstance.setFile(new File("C:\\SEMEVAL2013\\ARFFfile.arff")); 
       arffSaverInstance.writeBatch();
    }    
}

