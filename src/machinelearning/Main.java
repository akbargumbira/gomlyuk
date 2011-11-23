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
        String filename = "e.txt";

        //variables
        DataSet ds = new DataSet();

        //read things
        InputStream is  = new FileInputStream(filename);
        ds.load(is);
        is.close();

        //Initialize learning algorithms
        LearningAlgo[] algos = new LearningAlgo[] {
          new NaiveBayes(ds.attributes, 17, 0.4f),
          //new ID3(ds.attributes, 0)
        };
        
        /*
         * Tes ID3
         */
//        List<Object[]> dataSet = ds.data.subList(0, ds.data.size());
//        algos[1].learn(dataSet);

        //Create Crossfold Data
        //learnDataSet[0],learnDataSet[1], ..., learnDataSet[n], relate to testDataSet[0],testDataSet[0],...,testDataSet[n] respectively
        int numFold = 2;
        int totalData  = ds.data.size();
        int totalDataPerFold = totalData/numFold;
        
        ArrayList<List<Object[]>> learnDataSets = new ArrayList<List<Object[]>> ();
        ArrayList<List<Object[]>> testDataSets = new ArrayList<List<Object[]>> ();
        ArrayList<List<Object[]>> foldedDataSets = new ArrayList<List<Object[]>> ();
        
        //Folding data :
        for (int i=0; i < numFold; ++i) {
            if (i != (numFold-1) )
                foldedDataSets.add(i, ds.data.subList(i*totalDataPerFold, (i*totalDataPerFold)+totalDataPerFold));
            else
                foldedDataSets.add(i, ds.data.subList(i*totalDataPerFold, totalData));
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

        
        for(LearningAlgo algo : algos) {
            float totalAccuracy = 0;
            for (int i=0 ; i < numFold; ++i) {
                System.out.println("Algoritma : "+algo.getName() + " - Fold: " + i);
                algo.learn(learnDataSets.get(i));
                float res = algo.test(testDataSets.get(i));
                totalAccuracy += res;
            }
            float averageAccuracy = totalAccuracy/(float)numFold;
            System.out.println("Average Akurasi Algoritma: "+algo.getName()+" = "+averageAccuracy);
        }
    }

}
