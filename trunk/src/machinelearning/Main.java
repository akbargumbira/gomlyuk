/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package machinelearning;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import machinelearning.data.DataSet;
import machinelearning.learningalgo.ID3;
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
        Hashtable<String, Integer> dataSetFile = new Hashtable<String, Integer> ();
        dataSetFile.put("1-balancescale.txt",0);
        dataSetFile.put("2-carevaluation.txt",6);
        dataSetFile.put("3-nursery.txt",8);
        dataSetFile.put("4-tictactoe.txt",9);
        dataSetFile.put("5-zoo.txt",17);
                
        
        //parameters
        String filename = "2-carevaluation.txt";

        //variables
        DataSet ds = new DataSet();

        //read things
        InputStream is  = new FileInputStream(filename);
        ds.load(is);
        is.close();

        //Initialize learning algorithms
        LearningAlgo[] algos = new LearningAlgo[] {
          new NaiveBayes(ds.attributes, dataSetFile.get(filename), 0.4f),
          new ID3(ds.attributes, dataSetFile.get(filename))
        };
        

        //Create Crossfold Data
        //learnDataSet[0],learnDataSet[1], ..., learnDataSet[n], relate to testDataSet[0],testDataSet[0],...,testDataSet[n] respectively
        int numFold = 10;
        int totalData  = ds.data.size();
        int totalDataPerFold = totalData/numFold;
        
        ArrayList<List<Object[]>> learnDataSets = new ArrayList<List<Object[]>> ();
        ArrayList<List<Object[]>> testDataSets = new ArrayList<List<Object[]>> ();
        ArrayList<List<Object[]>> foldedDataSets = new ArrayList<List<Object[]>> ();
        
        Collections.shuffle(ds.data);
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
        
        ArrayList<Float> deltas = countDeltas(algos[0], algos[1], numFold, learnDataSets, testDataSets);
        for (int i = 0; i < deltas.size(); ++i) {
            System.out.println("delta ke-"+i+" = "+deltas.get(i)*100+"%");
        }
        
        
    }
    
    //Ngitung delta performa algo1-algo2
    public static ArrayList<Float> countDeltas(LearningAlgo algo1, LearningAlgo algo2, int numFold, ArrayList<List<Object[]>> learnDataSet, ArrayList<List<Object[]>> testDataSet) {
        ArrayList<Float> result = new ArrayList<Float>();
        
        for (int i = 0; i < numFold; ++i) {
            algo1.learn(learnDataSet.get(i));
            float resAlgo1 = algo1.test(testDataSet.get(i));
            System.out.println("Acc algo1 ke-"+i+" = "+resAlgo1);
            
            algo2.learn(learnDataSet.get(i));
            float resAlgo2 = algo2.test(testDataSet.get(i));
            System.out.println("Acc algo2 ke-"+i+" = "+resAlgo2);
            
            Float delta = resAlgo1-resAlgo2;
            result.add(delta);
            System.out.println("");
        }
        
        return result;
    }

}
