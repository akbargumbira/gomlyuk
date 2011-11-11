/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package machinelearning;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
          new NaiveBayes(ds.attributes),
        };

        //AKBAR: do cross fold - langsung folding, learn dan tes di sini aja deh
        //Karena sepertinya urutan learning dan ngetes sangat tergantung dengan si
        //crossfoldnya

        //do training and test
        //AKBAR : atur di sini urutan training dan testnya
        for(LearningAlgo algo : algos) {
            ArrayList<Object[]> learnDataSet    = null;
            ArrayList<Object[]> testDataSet     = null;
            algo.learn(learnDataSet);
            algo.test(testDataSet);
        }
    }

}
