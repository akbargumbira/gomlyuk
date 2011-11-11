/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package machinelearning.learningalgo;

import java.util.ArrayList;
import machinelearning.data.DataAttribute;

/**
 *
 * @author Ecky
 */
public abstract class LearningAlgo {

    protected ArrayList<DataAttribute> _attributes;

    public LearningAlgo(ArrayList<DataAttribute> attributes) {
        _attributes = attributes;
    }

    public abstract String getName();

    //learn from array of data
    public abstract void    learn(ArrayList<Object[]> data);

    //test array of data and return the precision
    public abstract float   test(ArrayList<Object[]> data);
}
