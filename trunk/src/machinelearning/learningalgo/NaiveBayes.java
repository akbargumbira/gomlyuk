/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package machinelearning.learningalgo;

import java.util.List;
import machinelearning.data.DataAttribute;
import machinelearning.data.NominalDataAttribute;

/**
 * MILIK GUE!
 * @author Ecky
 */
public class NaiveBayes extends LearningAlgo {

    private static String NAME = "Naive Bayes";
    private float[][][] _learningResult; //class, attributes, values
    private float[]     _tgtValProbability;

    private float _m;

    /**
     *
     * @param attributes
     * @param targetAttributeIdx
     * @param m refer to (nc + (m * p)) / (n + m) .. textbook p179
     */
    public NaiveBayes(List<DataAttribute> attributes, int targetAttributeIdx, float m) {
        super(attributes, targetAttributeIdx);
        _m = m;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void learn(List<Object[]> data) {
        //debug
        System.out.println("=======================================");
        System.out.println(NAME + " started learning with m = " + _m);
        System.out.println("=======================================");

        //create learning result array
        NominalDataAttribute tgtAttr = (NominalDataAttribute) _attributes.get(_targetAttributeIdx);
        _learningResult = new float[tgtAttr.values.length][_attributes.size()][];
        for(int i = 0; i < tgtAttr.values.length; ++i) {
            for(int j = 0; j < _attributes.size(); ++j) {
                if(j == _targetAttributeIdx) continue;
                NominalDataAttribute nomm = (NominalDataAttribute) _attributes.get(j);
                _learningResult[i][j] = new float[nomm.values.length];
                for(int k = 0; k < nomm.values.length; ++k) {
                    _learningResult[i][j][k] = 0;
                }
            }
        }

        //create temporary buffer for storing count of each target attr's value
         _tgtValProbability = new float[tgtAttr.values.length];
        for(int i = 0; i < tgtAttr.values.length; ++i) _tgtValProbability[i] = 0;

        //process training data, count the values
        for(Object[] o : data) {
            //count the target attr's value
            int tgtValIdx   = tgtAttr.valueIndex((String)o[_targetAttributeIdx]);
            _tgtValProbability[tgtValIdx] += 1;
            
            //count other attr's values
            for(int i = 0; i < _attributes.size(); ++i) {
                if(i == _targetAttributeIdx) continue;
                NominalDataAttribute nomm   = (NominalDataAttribute)_attributes.get(i);
                int valIdx      = nomm.valueIndex((String)o[i]);
                if(valIdx < 0) System.out.println(o[i]);
                _learningResult[tgtValIdx][i][valIdx] += 1;
            }
        }

        //Learning result debug
        System.out.println("-- Target Attribute's values count --");
        for(int i = 0; i < tgtAttr.values.length; ++i) {
            System.out.println(tgtAttr.values[i] + " : " + _tgtValProbability[i]);
        }
        System.out.println("----");

        //learning result debug
        for(int i = 0; i < _attributes.size(); ++i) {
            if(i == _targetAttributeIdx) continue;
            System.out.println("-- Attribute " + i + "'s values count --");

            NominalDataAttribute nomm   = (NominalDataAttribute)_attributes.get(i);
            for(int j = 0; j < nomm.values.length; ++j) {
                System.out.print(nomm.values[j] + " : ");
                for(int k = 0; k < tgtAttr.values.length; ++k) {
                    System.out.print(tgtAttr.values[k] + "=" + _learningResult[k][i][j] + " ");
                }
                System.out.println("");
            }
            System.out.println("----");
        }

        //pre process
        for(int i = 0; i < tgtAttr.values.length; ++i) {
            for(int j = 0; j < _attributes.size(); ++j) {
                if(j == _targetAttributeIdx) continue;
                NominalDataAttribute nomm = (NominalDataAttribute) _attributes.get(j);
                for(int k = 0; k < nomm.values.length; ++k) {
                    _learningResult[i][j][k] = (_learningResult[i][j][k] + _m * 1/nomm.values.length) / (_tgtValProbability[i] + _m);
                }
            }
        }
        float sum = 0;
        for(int i = 0; i < tgtAttr.values.length; ++i) sum += _tgtValProbability[i];
        for(int i = 0; i < tgtAttr.values.length; ++i) _tgtValProbability[i] = _tgtValProbability[i]/sum;

        //Learning result debug
        System.out.println("-- Target Attribute's values probability --");
        for(int i = 0; i < tgtAttr.values.length; ++i) {
            System.out.println(tgtAttr.values[i] + " : " + _tgtValProbability[i]);
        }
        System.out.println("----");

        //learning result debug
        for(int i = 0; i < _attributes.size(); ++i) {
            if(i == _targetAttributeIdx) continue;
            System.out.println("-- Attribute " + i + "'s values count --");

            NominalDataAttribute nomm   = (NominalDataAttribute)_attributes.get(i);
            for(int j = 0; j < nomm.values.length; ++j) {
                System.out.print(nomm.values[j] + " : ");
                for(int k = 0; k < tgtAttr.values.length; ++k) {
                    System.out.print(tgtAttr.values[k] + "=" + _learningResult[k][i][j] + " ");
                }
                System.out.println("");
            }
            System.out.println("----");
        }
    }

    @Override
    public float test(List<Object[]> data) {
        NominalDataAttribute tgtAttr = (NominalDataAttribute)_attributes.get(_targetAttributeIdx);
        float   correctRes  = 0;
        for(Object[] o : data) {
            //check whether the classification result is equal to actual classification
            correctRes += classify(o) == tgtAttr.valueIndex((String)o[_targetAttributeIdx]) ? 1 : 0;
        }

        return correctRes / data.size();
    }

    public int classify(Object[] data) {
        NominalDataAttribute tgtAttr = (NominalDataAttribute)_attributes.get(_targetAttributeIdx);
        float[] res = new float[tgtAttr.values.length];

        for(int j = 0; j < res.length; ++j) {
            res[j] = _tgtValProbability[j];
            for(int i = 0; i < _attributes.size(); ++i) {
                if(i == _targetAttributeIdx) continue;
                NominalDataAttribute nomm   = (NominalDataAttribute)_attributes.get(i);
                int valIdx      = nomm.valueIndex((String)data[i]);
                res[j] *= _learningResult[j][i][valIdx];
            }
        }

        //find the maximum value to determine which class this data is classified
        int maxIdx = 0;
        for(int i = 0; i < res.length; ++i) if(res[maxIdx] < res[i]) maxIdx = i;

        //return the class idx
        return maxIdx;
    }

}
