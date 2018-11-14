package test.main;

import base.City;
import data.InstanceReader;
import data.TSPLIBReader;
import main.Configuration;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class Application {
    private main.Application application;
    private static ArrayList<City> availableCities;

    @Before
    public void init(){
        InstanceReader instanceReader = new InstanceReader(Configuration.instance.dataFilePath);
        instanceReader.open();
        TSPLIBReader tspLibReader = new TSPLIBReader(instanceReader);

        availableCities = tspLibReader.getCities();
        application = new main.Application();
    }

    @Test
    public void loadInitConfiguration(){
        assertNotNull(application.initConfiguration());
    }

    @Test
    public void createPopulation(){
        assertNotNull(application.createPopulation(availableCities,10));
    }
}
