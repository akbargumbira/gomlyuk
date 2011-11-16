/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package machinelearning.learningalgo;

import java.util.ArrayList;
import java.util.List;
import machinelearning.data.DataAttribute;
import machinelearning.data.NominalDataAttribute;
import machinelearning.model.Node;

/**
 * @author ACER 4741
 */
public class ID3 extends LearningAlgo {

    private static String NAME = "ID3";
    private Node learningModel;
    NominalDataAttribute tgtAttr;
  
    public ID3(List<DataAttribute> attributes, int targetAttributeIdx) {
        super(attributes, targetAttributeIdx);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void learn(List<Object[]> data) {
         tgtAttr = (NominalDataAttribute) _attributes.get(_targetAttributeIdx);
         learningModel = new Node();
         
         //Tes ngitung entropy:
         //double tes = entropy(data);
         
         //Tes milih bestattribute:
         int tes = bestAttribute(data);
    }
    
    @Override
    public float test(List<Object[]> data) {
    
        return 0.4f;
    }

    
    public double entropy(List<Object[]> dataSet) {
        if (dataSet.size() == 0) 
            return 0;
       
        int[] totalCategory = totalPerCategory(dataSet, tgtAttr); 
        
        //CheckPoint
//        for (int i=0;i<totalCategory.length;++i) {
//            System.out.println("Jumlah kategori "+tgtAttr.values[i]+" = "+totalCategory[i]);
//        }
        
        double entropyRes = 0;
        for (int i = 0; i < totalCategory.length; ++i){
            double p = (float)totalCategory[i]/dataSet.size();
            double temp = 0;
            if (p != 0)
                temp = (-1*p) * log2(p);
            entropyRes += temp;
        }
//        System.out.println("Entropy:"+entropyRes);
        return entropyRes;
    }
    
    /*
     * Memilih subset dari dataset yang memiliki indeks atribut tertentu, dan value tertentu dari atribut ini
     */
    public List<Object[]> selectDataSet(List<Object[]> dataSet, int attributeIndex, Object attributeValue) {
        ArrayList<Object[]> result = new ArrayList<Object[]>();
        
        for (int i = 0; i < dataSet.size(); ++i) {
            Object[] thisObject = dataSet.get(i);
            if (thisObject[attributeIndex].equals(attributeValue)) {
                result.add(thisObject);
            }
        }
        return result;
    }
    
    /*
     * Memilah, mana data positif, negatif, etc (sesuai attribute kelas)
     * Return jumlah data tiap kategori ini
     */
    public int[] totalPerCategory(List<Object[]> dataSet, NominalDataAttribute tgtAttrib){
        int[] totalCategory = new int[tgtAttrib.values.length]; //Jumlah Kategori positif/negatif
        for (int i = 0; i < dataSet.size(); ++i) {
            for (int j = 0 ; j< tgtAttr.values.length; ++j) {
                if (dataSet.get(i)[_targetAttributeIdx].equals(tgtAttr.values[j])) {
                    totalCategory[j] +=1;
                }
            } 
        }
        return totalCategory;
    }
    
    /*
     * Choose best attribute from dataSet
     * What we should know : list attribute, list value dari tiap attribute
     * Return indeks attribute yg paling bagus
     */
    public int bestAttribute(List<Object[]> dataSet) {
        double initialEntropy = entropy(dataSet);
        
        int selectedAttribute = -1;
        double selectedGain = 0;
        
        int numTargetAttributes = _attributes.size();
        for (int i = 0; i < numTargetAttributes; ++i) {
            if (i != _targetAttributeIdx) {
                //Select dataset untuk masing2 attribut untuk tiap value:
                NominalDataAttribute thisAttrib = (NominalDataAttribute) _attributes.get(i);
                double totalEntropyReduction = 0;
                for (int j=0; j < thisAttrib.values.length ; ++j) {
                    List<Object[]> thisDataSet = selectDataSet(dataSet, i, thisAttrib.values[j]);
                    double entropyThisValue = entropy(thisDataSet);
                    double probThisValue = (double)thisDataSet.size() / dataSet.size();
                    totalEntropyReduction += probThisValue*entropyThisValue;
                    
                    /* CHECKPOINT
                     * int[] thisTotalCategory = totalPerCategory(thisDataSet, tgtAttr);
                    
                    System.out.println("Untuk Attribut = "+i+" Value = "+thisAttrib.values[j]);
                    for (int k = 0; k < thisTotalCategory.length; ++k) {
                      System.out.println("Jumlah kategori "+tgtAttr.values[k]+" = "+thisTotalCategory[k]);
                    } 
                    System.out.println(""); */
                }
                double gainThisAttribute = initialEntropy - totalEntropyReduction;
                if (gainThisAttribute > selectedGain) {
                    selectedAttribute = i;
                    selectedGain = gainThisAttribute;
                }
                System.out.println("Gain(S,"+i+") = "+gainThisAttribute);
                
            }
        }
        System.out.println("Best Gain: Attribute ke-"+selectedAttribute+", Gain = "+selectedGain);
        return selectedAttribute;
        
    }
    
    
    public int classify(Object[] data) {
        
        return 4;
    }
    
    public double log2(double a) {
        return (Math.log10(a)/Math.log10(2.0));
    }
    
    
    

}
