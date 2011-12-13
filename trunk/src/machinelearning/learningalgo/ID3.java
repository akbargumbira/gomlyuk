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
    private static int DEFAULT_CLASS = 0;
    private Node learningModel = new Node();
    NominalDataAttribute tgtAttr;
  
    public ID3(List<DataAttribute> attributes, int targetAttributeIdx) {
        super(attributes, targetAttributeIdx);
        learningModel.setParent(null);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void learn(List<Object[]> data) {
         tgtAttr = (NominalDataAttribute) _attributes.get(_targetAttributeIdx);
         
         learningModel = new Node();
         learningModel.setDataSetNode(data);
         learningModel.setParent(null);
         if (!learningModel.getDataSetNode().isEmpty())
             learningModel.setIsLeaf(Boolean.FALSE);
         else 
             learningModel.setIsLeaf(Boolean.TRUE);
         ArrayList<Integer> notAllowedChosenIdx = new ArrayList<Integer>();
         makeTree(learningModel, notAllowedChosenIdx);
    }
    
    @Override
    /*
     * Test dataTes = data
     */
    public float test(List<Object[]> data) {
        NominalDataAttribute tgtAttrib = (NominalDataAttribute)_attributes.get(_targetAttributeIdx);
        float   correctRes  = 0;
        for(Object[] o : data) {
            correctRes += classify(o) == tgtAttrib.valueIndex((String)o[_targetAttributeIdx]) ? 1 : 0;
        }

        return correctRes / data.size();
    }

    public int mostClassifiedClass(List<Object[]> data) {
        int[] totalCategory = totalPerCategory(data, tgtAttr);
        int selectedIdx = 0;
        int selectedTotal = 0;
        for (int i = 0; i < totalCategory.length; ++i){
            if (totalCategory[i] > selectedTotal)
                selectedIdx = i;
        }
        return selectedIdx;
    }
    
    public double entropy(List<Object[]> dataSet) {
        if (dataSet.isEmpty()) 
            return 0;
       
        int[] totalCategory = totalPerCategory(dataSet, tgtAttr); 
        
        double entropyRes = 0;
        for (int i = 0; i < totalCategory.length; ++i){
            double p = (float)totalCategory[i]/dataSet.size();
            double temp = 0;
            if (p != 0)
                temp = (-1*p) * log2(p);
            entropyRes += temp;
        }
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
     * Make Tree, buat model learning
     */
    public void makeTree(Node a_node, ArrayList<Integer> notAllowedChosenIdx) {
        int bestAttribute = bestAttribute(a_node.getDataSetNode(), notAllowedChosenIdx);
        
        a_node.setAttributeIndex(bestAttribute);
        
        //Perbaharui notAllowedChosenIdx:
        ArrayList<Integer> notAllowedChosenIdxNew = new ArrayList<Integer>();
        notAllowedChosenIdxNew.addAll(notAllowedChosenIdx);
        notAllowedChosenIdxNew.add(bestAttribute);
        
        //Bikin Anak dari best Attribute ini sebanyak value dari attribute ini:
        NominalDataAttribute thisAttribute = (NominalDataAttribute) _attributes.get(bestAttribute);
        Node[] childs = new Node[thisAttribute.values.length];
        
        for (int i = 0; i < thisAttribute.values.length; ++i) {
            //Pilih atribut terbaik buat jadi Anak:
            List<Object[]> thisDataSet = selectDataSet(a_node.getDataSetNode(), bestAttribute, thisAttribute.values[i]);
            double entropyThisDataSet = entropy(thisDataSet);
            
            Node thisNode = new Node();
            
            if (entropyThisDataSet == 0.0) { //all clasified
                thisNode.setAttributeIndex(-1);
                thisNode.setChild(null);
                thisNode.setParent(a_node);
                thisNode.setEntropyNode(0);
                thisNode.setIsLeaf(Boolean.TRUE);
                if (!thisDataSet.isEmpty())
                    thisNode.setClassIdx(tgtAttr.valueIndex((String)thisDataSet.get(0)[_targetAttributeIdx]));
                else {//("Class GEJE karena attribute ini gede Gainnya tapi value ini gak ada data trainingnya");
                    thisNode.setClassIdx(DEFAULT_CLASS);
                }
                childs[i] = thisNode;
            } else {
                if (notAllowedChosenIdxNew.size() != (_attributes.size()-1)) { //Kalo udah sama berarti atribut udah kepilih semua
                    int bestAttrib = bestAttribute(thisDataSet, notAllowedChosenIdx);
                    thisNode.setAttributeIndex(bestAttrib);
                    thisNode.setParent(a_node);
                    thisNode.setDataSetNode(thisDataSet);
                    thisNode.setIsLeaf(Boolean.TRUE);
                    thisNode.setEntropyNode(entropyThisDataSet);
                    childs[i] = thisNode;
                } else {
                    thisNode.setParent(a_node);
                    thisNode.setDataSetNode(thisDataSet);
                    thisNode.setIsLeaf(Boolean.TRUE);
                    thisNode.setEntropyNode(entropyThisDataSet);
                    thisNode.setClassIdx(mostClassifiedClass(thisDataSet));
                    System.out.println("hai");
                    childs[i] = thisNode;
                }
            }
        }
        a_node.setChild(childs);
        
        for (int i = 0; i < a_node.getChild().length; ++i) {
            if ((notAllowedChosenIdxNew.size() < _attributes.size()-1) && !a_node.getChild()[i].getIsLeaf())
                makeTree(a_node.getChild()[i], notAllowedChosenIdxNew);
        }
    }
    
    public void printTree(Node root) {
        if (!root.getIsLeaf()) {
            System.out.println("Anak dari atribut-"+root.getAttributeIndex()+" : ");
            Node[] childs = root.getChild();
            for (int i=0; i < childs.length; ++i) {
                System.out.println(childs[i].getAttributeIndex());
            }
            for (int i=0; i < childs.length; ++i) {
                printTree(childs[i]);
            }
        }
    }
    
    /*
     * Choose best attribute from dataSet, tapi tidak boleh ada di notAllowedChosenIdx
     * What we should know : list attribute, list value dari tiap attribute
     * Return indeks atribut terbaik
     */
    public int bestAttribute(List<Object[]> dataSet, ArrayList<Integer> notAllowedChosenIdx) {
        double initialEntropy = entropy(dataSet);
        
        int selectedAttribute = -1;
        double selectedGain = 0;
        
        int numTargetAttributes = _attributes.size();
        for (int i = 0; i < numTargetAttributes; ++i) {
            if (i != _targetAttributeIdx && (!notAllowedChosenIdx.contains(i))) {
                //Select dataset untuk masing2 attribut untuk tiap value:
                NominalDataAttribute thisAttrib = (NominalDataAttribute) _attributes.get(i);
                double totalEntropyReduction = 0;
                
                for (int j=0; j < thisAttrib.values.length ; ++j) {
                    List<Object[]> thisDataSet = selectDataSet(dataSet, i, thisAttrib.values[j]);
                    double entropyThisValue = entropy(thisDataSet);
                    double probThisValue = (double)thisDataSet.size() / dataSet.size();
                    totalEntropyReduction += probThisValue*entropyThisValue;       
                }
                double gainThisAttribute = initialEntropy - totalEntropyReduction;
                if (gainThisAttribute >= selectedGain) {
                    selectedAttribute = i;
                    selectedGain = gainThisAttribute;
                }
            }
        }
        
        return selectedAttribute;
    }
    
    
    /* 
     * Klasifikasi satu data
     * Return targetIdx
     */
    public int classify(Object[] data) {
        Node node = learningModel;
        while (!node.getIsLeaf()) {
            NominalDataAttribute thisAttr = (NominalDataAttribute)_attributes.get(node.getAttributeIndex());
            Object valueAttributeThisData = data[node.getAttributeIndex()];
            int idxValue = thisAttr.valueIndex((String)valueAttributeThisData);
            node = node.getChild()[idxValue];
        }
        return node.getClassIdx();
    }
    
    public double log2(double a) {
        return (Math.log10(a)/Math.log10(2.0));
    }
    
}