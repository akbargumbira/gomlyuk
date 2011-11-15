/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package machinelearning;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import machinelearning.data.DataSet;
import machinelearning.learningalgo.LearningAlgo;
import machinelearning.learningalgo.NaiveBayes;

/**
 *
 * @author Ecky
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        //parameters
        String filename = "a.txt";

        //variables
        DataSet ds = new DataSet();

        //read things
        InputStream is  = new FileInputStream(filename);
        ds.load(is);
        is.close();

        //Initialize learning algorithms
        LearningAlgo[] algos = new LearningAlgo[] {
          new NaiveBayes(ds.attributes, 0, 0),
        };

        //Create Crossfold Data
        //learnDataSet[0],learnDataSet[1], ..., learnDataSet[n], relate to testDataSet[0],testDataSet[0],...,testDataSet[n] respectively
        int numFold = 5;
        int totalData  = ds.data.size();
        int totalDataPerFold = totalData/numFold;
        
        ArrayList<List<Object[]>> learnDataSets = new ArrayList<List<Object[]>> ();
        ArrayList<List<Object[]>> testDataSets = new ArrayList<List<Object[]>> ();
        ArrayList<List<Object[]>> foldedDataSets = new ArrayList<List<Object[]>> ();
        
        //Folding data :
        for (int i=0; i < numFold; ++i) {
            if (i != (numFold-1) )
                foldedDataSets.add(i, ds.data.subList(i*totalDataPerFold, (i*totalDataPerFold)+totalDataPerFold-1));
            else
                foldedDataSets.add(i, ds.data.subList(i*totalDataPerFold, ds.data.size()));
        }
        
        for (int i=0; i< numFold; ++i)
            System.out.println("Size folded data set ke-"+i+" = "+foldedDataSets.get(i).size());
        
        //Fill learnDataSet and testDataSet
        for (int i=0; i < numFold; ++i) {
            testDataSets.add(i, foldedDataSets.get(i));
            
            ArrayList<Object[]> temp = new ArrayList<Object[]>();
            for (int j=0; j < numFold; ++j) {
              if (j!=i)
                  temp.addAll(foldedDataSets.get(j));
            } 
            learnDataSets.add(i, temp);
        }

        
        float totalAccuracy = 0;
        for(LearningAlgo algo : algos) {
            for (int i=0 ; i < numFold; ++i) {
                System.out.println("Learn Data Set Size: "+learnDataSets.get(i).size());
                System.out.println("Test Data Set Size: "+testDataSets.get(i).size());
                algo.learn(learnDataSets.get(i));
                float res = algo.test(testDataSets.get(i));
                totalAccuracy += res;
                System.out.println("Akurasi :"+res+"\n");
            }
        }
        
        float averageAccuracy = totalAccuracy/(float)numFold;
        System.out.println("Akurasi rata-rata : "+averageAccuracy);
    }

}
