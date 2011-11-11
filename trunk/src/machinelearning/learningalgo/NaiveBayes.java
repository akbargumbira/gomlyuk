/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package machinelearning.learningalgo;

import java.util.ArrayList;
import machinelearning.data.DataAttribute;

/**
 * MILIK GUE!
 * @author Ecky
 */
public class NaiveBayes extends LearningAlgo {

    private static String NAME = "Naive Bayes";

    public NaiveBayes(ArrayList<DataAttribute> attributes) {
        super(attributes);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void learn(ArrayList<Object[]> data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public float test(ArrayList<Object[]> data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
