
package machinelearning.learningalgo;

import com.sun.org.glassfish.external.statistics.Statistic;
import com.sun.org.glassfish.external.statistics.impl.StatisticImpl;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import machinelearning.data.DataAttribute;
import machinelearning.data.NominalDataAttribute;
import sun.security.util.BitArray;

/**
 * @author vaio
 */
public class KNN extends LearningAlgo {

    private static String NAME = "KNN";
    private int K = 5;
    private int[][] _learningData;
    private int[][] _testData;
    private int _limit;
    
    NominalDataAttribute tgtAttr;
 
    public KNN(List<DataAttribute> attributes, int targetAttributeIdx, int K) {
        super(attributes, targetAttributeIdx);
        this.K = K;
        System.out.println(K);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void learn(List<Object[]> data) {
        _learningData = new int[data.size()][_attributes.size()];
        if (K > data.size())
            K = data.size();
        for (int i = 0; i < data.size(); ++i) {
            int k = 0;
            for (int j = 0; j < _attributes.size(); ++j) {
                if (j == _targetAttributeIdx)
                    continue;
                _learningData[i][k] = ((NominalDataAttribute) _attributes.get(j)).valueIndex((String) data.get(i)[j]);
                ++k;
            }
            _learningData[i][k] = ((NominalDataAttribute) _attributes.get(_targetAttributeIdx)).valueIndex((String) data.get(i)[_targetAttributeIdx]);
        }
        _limit = ((NominalDataAttribute) _attributes.get(_targetAttributeIdx)).values.length - 1;
    }
    
    @Override
    /*
     * Test dataTes = data
     */
    public float test(List<Object[]> data) {
        int sum = 0;
        
        System.out.println("Copying data");
        _testData = new int[data.size()][_attributes.size()];
        for (int i = 0; i < data.size(); ++i) {
            int k = 0;
            for (int j = 0; j < _attributes.size(); ++j) {
                if (j == _targetAttributeIdx)
                    continue;
                _testData[i][k] = ((NominalDataAttribute) _attributes.get(j)).valueIndex((String) data.get(i)[j]);
                ++k;
            }
            _testData[i][k] = ((NominalDataAttribute) _attributes.get(_targetAttributeIdx)).valueIndex((String) data.get(i)[_targetAttributeIdx]);
        }
        System.out.println("Testing data");
        for(int k = 0; k < _testData.length; ++k) {
            if (getClassification(_testData[k]) == _testData[k][_limit])
                ++sum;
        }
        
        return (float) sum/_testData.length;
    }
    
    private int getClassification(int[] test) {
        int[] distances = new int[_learningData.length];
        for (int i = 0; i < _learningData.length; ++i) {
            distances[i] = getDistance(_learningData[i], test);
        }
        int minIndex[] = indexBubbleSort(distances);
        
        
        int candidates[] = new int[K];
        for (int j = 0; j < K; ++j) {
            candidates[j] = _learningData[minIndex[j]][_limit];
        }
        
        return modus(candidates);
    }
    
    private int getDistance (int[] data, int[] test) {
        int out = 0;
            for (int i = 0; i < _limit; ++i) {
                if (data[i] != test[i]) ++out;
            }
        return out;
    }
    
    private int modus(int[] in) {
        int val [] = new int[((NominalDataAttribute)_attributes.get(_targetAttributeIdx)).values.length];
        Arrays.fill(val, 0);
        
        for (int i = 0; i < K; ++i) {
            ++val[in[i]];
        }
        
        return indexMaximal(val);
    }
    
    
    private int indexMaximal(int[] in) {
        int out = 0;
        int min = in[0];
        for (int i = 1; i < in.length; ++i) {
            if (in[i] > min)
            {
                min = in[i];
                out = i;
            }
        }
        return out;
    }
    
    private int[] indexBubbleSort(int[] x) {
        int out[] = new int[x.length];
        for (int i = 0; i < out.length; ++i) {
            out[i] = i;
        }
        
        int n = x.length;
        for (int pass = 1; pass < n; pass++) {  // count how many times
            // This next loop becomes shorter and shorter
            for (int i = 0; i < n - pass; i++) {
                if (x[i] < x[i + 1]) {
                    // exchange elements
                    int temp = x[i];
                    x[i] = x[i + 1];
                    x[i + 1] = temp;
                    
                    temp = out[i];
                    out[i] = out[i + 1];
                    out[i+1] = temp;
                }
            }
        }
        return out;
    }
}
