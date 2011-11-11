/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package machinelearning.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 *
 * @author Ecky
 */
public class DataSet {

    public ArrayList<DataAttribute> attributes;
    public ArrayList<Object[]>      data;

    public DataSet() {
        attributes  = new ArrayList<DataAttribute>();
        data        = new ArrayList<Object[]>();
    }

    //Load from file
    public void load(InputStream is) throws IOException {
        //reset
        attributes.clear();
        data.clear();
        BufferedReader r = new BufferedReader(new InputStreamReader(is));

        //load attributes
        String dataRead;
        while(!(dataRead = r.readLine()).equals("@")) {
            ////parse current attribute
            String[] defs = dataRead.split(" ");

            //Nominal type
            if(defs[0].equals(DataAttribute.TYPE_NOMINAL)) {
                NominalDataAttribute da = new NominalDataAttribute();
                da.parseDefinition(defs[1]);
                attributes.add(da);
            }
            
            // .. other type ?
            else if(false) {

            }
        }

        //load data
        while(null != (dataRead = r.readLine())) {
            //split the values
            String[] values = dataRead.split(",");

            //create an entry for that line
            Object[] entry  = new Object[attributes.size()];
            for(int i = 0; i < attributes.size(); ++i) {
                //parse the read value based on the attribute
                entry[i] = attributes.get(i).parseValue(values[i]);
            }

            //push that entry into data
            data.add(entry);
        }
    }
}
