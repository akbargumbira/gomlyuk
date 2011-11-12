/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package machinelearning.data;

/**
 *
 * @author Ecky
 */
public class NominalDataAttribute extends DataAttribute {

    public String[] values;

    @Override
    public void parseDefinition(String def) {
        this.values = def.split(",");
    }

    @Override
    public Object parseValue(String value) {
        return value;
    }

    public int valueIndex(String value) {
        for(int i = 0; i < values.length; ++i) {
            if(value.equals(values[i])) return i;
        }

        return -1;
    }
}
