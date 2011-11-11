/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package machinelearning.data;

/**
 *
 * @author Ecky
 */
public abstract class DataAttribute {
    public static final String TYPE_NOMINAL = "nom";

    private String _type;

    public String   getType()           { return _type; }
    public boolean  isType(String type) { return _type.equals(type); }

    public abstract void    parseDefinition(String definition);
    public abstract Object  parseValue(String value);
}
