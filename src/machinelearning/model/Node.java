/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package machinelearning.model;

import java.util.List;

/**
 *
 * @author ACER 4741
 */
public class Node {
    private int _attributeIndex;
    private int _numAttributeValue;
    private Object[] _attributeValues; //Verteks
    private Node[] _child;
    private Node _parent;          
    
    public Node() {
        
    }
    
    public int getAttributeIndex() {
        return _attributeIndex;
    }

    public int getNumAttributeValue() {
        return _numAttributeValue;
    }
    
    public Object[] getAttributeValues() {
        return _attributeValues;
    }
    
    
    public Node[] getChild() {
        return _child;
    }

    public Node getParent() {
        return _parent;
    }    
    
}
