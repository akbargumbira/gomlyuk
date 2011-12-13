/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package machinelearning;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import machinelearning.data.DataSet;
import machinelearning.learningalgo.ID3;
import machinelearning.learningalgo.KNN;
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
        dataSetFile.put("3-kingrookvskingpawn.txt",36);
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
          new ID3(ds.attributes, dataSetFile.get(filename)), 
          new KNN(ds.attributes, dataSetFile.get(filename),3)
        };
        

        //Create Crossfold Data
        //learnDataSet[0],learnDataSet[1], ..., learnDataSet[n], relate to testDataSet[0],testDataSet[0],...,testDataSet[n] respectively
        int numFold = 10;
        int totalData  = ds.data.size();
        int totalDataPerFold = totalData/numFold;
        
        ArrayList<List<Object[]>> learnDataSets = new ArrayList<List<Object[]>> ();
        ArrayList<List<Object[]>> testDataSets = new ArrayList<List<Object[]>> ();
        ArrayList<List<Object[]>> foldedDataSets = new ArrayList<List<Object[]>> ();
        
        //Shuffle dataset
        Collections.shuffle(ds.data);
        
        //Folding data :
        for (int i=0; i < numFold; ++i) {
            if (i != (numFold-1) )
                foldedDataSets.add(i, ds.data.subList(i*totalDataPerFold, (i*totalDataPerFold)+totalDataPerFold));
            else
                foldedDataSets.add(i, ds.data.subList(i*totalDataPerFold, totalData));
        }
          
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
        
        //Hitung akurasi semua algo dan print tabel akurasi
        ArrayList<ArrayList<Float>> akurasiAllAlgo = getAkurasiAllAlgo(algos, numFold, learnDataSets, testDataSets);
        printAkurasiAllAlgo(akurasiAllAlgo, algos, numFold);
        
        
        //Hitung delta dan print delta tiap algo
        for (int i = 0; i < algos.length; i++) {
            int j,k;
            if (i==0) {
                j = 1; k = 2;
            } else if (i==1) {
                j = 0; k = 2;
            } else {
                j = 0; k = 1;
            }
            ArrayList<Float> deltas = countDeltas(akurasiAllAlgo.get(j), akurasiAllAlgo.get(k) , numFold);
            System.out.println("");
            printDeltaTabel(deltas, algos[j].getName(), algos[k].getName());   
        }
    }
    
    
    /*
     * Menghitung akurasi semua algoritma
     */
    public static ArrayList<ArrayList<Float>> getAkurasiAllAlgo(LearningAlgo[] algos, int numFold, ArrayList<List<Object[]>> learnDataSet, ArrayList<List<Object[]>> testDataSet){
        ArrayList<ArrayList<Float>> result = new ArrayList<ArrayList<Float>>();
       
        for(LearningAlgo algo : algos) {
            ArrayList<Float> akurasiThisAlgo = new ArrayList<Float>();
            for (int i=0 ; i < numFold; ++i) {
                algo.learn(learnDataSet.get(i));
                float res = algo.test(testDataSet.get(i));
                akurasiThisAlgo.add(res);
            }
            result.add(akurasiThisAlgo);
        }
        
        return result;
    }
    
    /*
     * Print Akurasi semua algoritma
     */
    public static void printAkurasiAllAlgo(ArrayList<ArrayList<Float>> akurasiAllAlgo, LearningAlgo[] algos, int numFold) {
        System.out.println("------------------------------- TABEL AKURASI ALGORITMA ----------------------------------");
        System.out.print("\tk \t | ");
        for (LearningAlgo algo : algos) {
            if (algo.getName().equals("Naive Bayes"))
                System.out.print("\t"+algo.getName()+"\t |");
            else
                System.out.print("\t"+algo.getName()+"\t\t |");
        }
        System.out.println("");
        System.out.println("------------------------------------------------------------------------------------------");
        
        for (int i = 0; i < numFold; i++) {
            System.out.print("\t"+i+" \t |");
            for (int j = 0; j < algos.length; j++) {
                System.out.print("\t");
                System.out.format("%.6f",akurasiAllAlgo.get(j).get(i) );
                System.out.print("\t |");
            }
                
            System.out.println("");
        }
        System.out.println("------------------------------------------------------------------------------------------");
        System.out.print("\tMEAN \t | ");
        for (int j = 0; j < algos.length; j++) {
                System.out.print("\t");
                System.out.format("%.6f",mean(akurasiAllAlgo.get(j), numFold)) ;
                System.out.print("\t |");
        }
        System.out.println("");
        System.out.println("------------------------------------------------------------------------------------------");
    }
    
    
    
    /*
     * Menghitung delta antara 2 algoritma
     */
    public static ArrayList<Float> countDeltas(ArrayList<Float> accAlgo1, ArrayList<Float> accAlgo2, int numFold) {
        ArrayList<Float> result = new ArrayList<Float>();
        
        for (int i = 0; i < numFold; ++i) {      
            Float delta = Math.abs(accAlgo1.get(i) - accAlgo2.get(i)) ;
            result.add(delta);
        }
        
        return result;
    }
    
    /*
     * Print tabel delta, rataan delta, standar deviasi, dan T
     */
    public static void printDeltaTabel(ArrayList<Float> deltas, String algo1Name, String algo2Name) {
        System.out.println("------------- TABEL DELTA ----------------");
        if (algo1Name.equals("Naive Bayes"))
            System.out.println("-------- "+ algo1Name +" -- "+ algo2Name +" --------"+"\t |");
        else
            System.out.println("---------- "+ algo1Name +" -- "+ algo2Name +" ----------"+"\t |");
        
        System.out.print("\tk \t | ");
        System.out.print("\tDelta \t\t | ");
        System.out.println("");
        System.out.println("------------------------------------------");
        
        for (int i = 0; i < deltas.size(); i++) {
            System.out.print("\t"+i+" \t |");
            System.out.print("\t");
            System.out.format("%.6f",deltas.get(i) );
            System.out.print("\t |");
            
                
            System.out.println("");
        }
        System.out.println("------------------------------------------");
        
        Float deltamean = mean(deltas, deltas.size());
        Float standarDev = standarDev(deltamean, deltas);
        Float T = deltamean/standarDev;
        
        System.out.print("\tMean \t | ");
        System.out.print("\t");
        System.out.format("%.6f",deltamean);
        System.out.print("\t |");
        System.out.println("");
        
        System.out.print("\tSt Dev \t | ");
        System.out.print("\t");
        System.out.format("%.6f",standarDev);
        System.out.print("\t |");
        System.out.println("");
        
        System.out.print("\tT \t | ");
        System.out.print("\t");
        System.out.format("%.6f",T);
        System.out.print("\t |");
        
        System.out.println("");
        System.out.println("------------------------------------------");
    }
   
    
    /*
     * Menghitung mean dari deltas
     */
    public static Float mean(ArrayList<Float> deltas, int numFold) {
        Float deltamean = 0.0f;
        for (int i = 0; i < deltas.size(); ++i) {
            deltamean += deltas.get(i);
        }
        deltamean /= numFold;
        
        return deltamean;
    }
    
    /*
     * Menghitung standar deviasi dari deltas
     */
    public static Float standarDev(Float mean, ArrayList<Float> deltas) {
        Float sumKuadratSelisih = 0.0f;
        for (int i = 0; i < deltas.size(); ++i) {
            Float selisih = (deltas.get(i)-mean);
            Float temp = selisih * selisih;
            sumKuadratSelisih += temp;
        }
        
        int k = deltas.size();
        Float result = (Float)Math.sqrt(sumKuadratSelisih/(k*(k-1)));
        
        return result;
    }

}
