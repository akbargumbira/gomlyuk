/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package machinelearning;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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

        //AKBAR: do cross fold - langsung folding, learn dan tes di sini aja deh
        //Karena sepertinya urutan learning dan ngetes sangat tergantung dengan si
        //crossfoldnya

        //do training and test
        //AKBAR : atur di sini urutan training dan testnya
        for(LearningAlgo algo : algos) {
            List<Object[]> learnDataSet    = ds.data.subList(0, ds.data.size() - 100);
            List<Object[]> testDataSet     = ds.data.subList(ds.data.size() - 100, ds.data.size());
            algo.learn(learnDataSet);
            float res = algo.test(learnDataSet);
            int i = 0; //just dummy line so that I can put a breakpoint here to check res
        }
    }

}
