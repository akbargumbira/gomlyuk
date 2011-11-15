/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package machinelearning.learningalgo;

import java.util.List;
import machinelearning.data.DataAttribute;
import machinelearning.data.NominalDataAttribute;

/**
 * @author ACER 4741
 */
public class ID3 extends LearningAlgo {

    private static String NAME = "ID3";
    private float[][][] _learningResult; //class, attributes, values
    private float[]     _tgtValProbability;

    private float _m;

  
    public ID3(List<DataAttribute> attributes, int targetAttributeIdx, float m) {
        super(attributes, targetAttributeIdx);
        _m = m;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void learn(List<Object[]> data) {
        
    }

    @Override
    public float test(List<Object[]> data) {
    
        return 0.4f;
    }

    public int classify(Object[] data) {
        
        return 4;
    }

}
