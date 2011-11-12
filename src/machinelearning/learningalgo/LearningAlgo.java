/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package machinelearning.learningalgo;

import java.util.List;
import machinelearning.data.DataAttribute;

/**
 *
 * @author Ecky
 */
public abstract class LearningAlgo {

    protected List<DataAttribute> _attributes;
    protected int _targetAttributeIdx;

    public LearningAlgo(List<DataAttribute> attributes, int targetAttributeIdx) {
        _attributes         = attributes;
        _targetAttributeIdx = targetAttributeIdx;
    }

    public abstract String getName();

    //learn from array of data
    public abstract void    learn(List<Object[]> data);

    //test array of data and return the precision
    public abstract float   test(List<Object[]> data);
}
