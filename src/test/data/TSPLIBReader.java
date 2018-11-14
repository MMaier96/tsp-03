package test.data;

import data.InstanceReader;
import main.Configuration;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TSPLIBReader {
    @Test
    public void test() {
        InstanceReader instanceReader = new InstanceReader(Configuration.instance.dataFilePath);
        instanceReader.open();
        data.TSPLIBReader tspLibReader = new data.TSPLIBReader(instanceReader);
        assertEquals(280, tspLibReader.getCities().size());
    }
}