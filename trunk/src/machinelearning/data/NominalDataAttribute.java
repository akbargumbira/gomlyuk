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

}
