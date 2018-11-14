package test.selection;

import base.City;
import base.Population;
import base.Tour;
import main.Configuration;
import org.junit.Assert;
import org.junit.Test;
import selection.PopulationTooSmallException;

import java.util.ArrayList;

public class RouletteWheelSelection {

    @Test
    public void testDoSelection() throws PopulationTooSmallException {
        Tour[] tour = {new Tour(), new Tour(), new Tour(), new Tour()};

        City[] cities = {new City(1, 10, 10), new City(2, 20, 20),
                new City(3, 30, 30), new City(4, 40, 40)};

        tour[0].addCity(cities[0]);
        tour[0].addCity(cities[1]);
        tour[0].addCity(cities[2]);
        tour[0].addCity(cities[3]);

        tour[1].addCity(cities[1]);
        tour[1].addCity(cities[3]);
        tour[1].addCity(cities[0]);
        tour[1].addCity(cities[2]);

        tour[2].addCity(cities[1]);
        tour[2].addCity(cities[2]);
        tour[2].addCity(cities[3]);
        tour[2].addCity(cities[0]);

        tour[3].addCity(cities[3]);
        tour[3].addCity(cities[0]);
        tour[3].addCity(cities[2]);
        tour[3].addCity(cities[1]);

        Population pop = new Population();

        pop.addTour(tour[0])
                .addTour(tour[1])
                .addTour(tour[2])
                .addTour(tour[3]);

        double[] pseudoDoubles = {0.25, 0.5, 0.75, 1};
        Configuration.instance.random = new PseudoRandom(pseudoDoubles);

        selection.RouletteWheelSelection rs = new selection.RouletteWheelSelection();

        Configuration.instance.ROULETTE_WHEEL_SELECT_COUNT = 1;
        ArrayList<Tour> expected = new ArrayList<>();
        expected.add(tour[1]);

        Assert.assertEquals(expected, rs.doSelection(pop));
    }
}