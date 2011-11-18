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
    private Boolean _isLeaf;
    private int _classIdx; //if _isLeaf then _class diisi
    private Node[] _child;
    private Node _parent;
    private List<Object[]> _dataSetNode; //Dataset untuk node ini
    private double _entropyNode;

        
    public Node() {
        
    }

    public int getAttributeIndex() {
        return _attributeIndex;
    }

    public void setAttributeIndex(int _attributeIndex) {
        this._attributeIndex = _attributeIndex;
    }

    public Node[] getChild() {
        return _child;
    }

    public void setChild(Node[] _child) {
        this._child = _child;
    }

    public int getClassIdx() {
        return _classIdx;
    }

    public void setClassIdx(int _classIdx) {
        this._classIdx = _classIdx;
    }

    public List<Object[]> getDataSetNode() {
        return _dataSetNode;
    }

    public void setDataSetNode(List<Object[]> _dataSetNode) {
        this._dataSetNode = _dataSetNode;
    }

    public double getEntropyNode() {
        return _entropyNode;
    }

    public void setEntropyNode(double _entropyNode) {
        this._entropyNode = _entropyNode;
    }

    public Boolean getIsLeaf() {
        return _isLeaf;
    }

    public void setIsLeaf(Boolean _isLeaf) {
        this._isLeaf = _isLeaf;
    }

    public int getNumAttributeValue() {
        return _numAttributeValue;
    }

    public void setNumAttributeValue(int _numAttributeValue) {
        this._numAttributeValue = _numAttributeValue;
    }

    public Node getParent() {
        return _parent;
    }

    public void setParent(Node _parent) {
        this._parent = _parent;
    }
}
