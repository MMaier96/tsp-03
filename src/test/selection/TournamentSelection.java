package test.selection;

import base.City;
import base.Population;
import base.Tour;
import main.Configuration;
import org.junit.Assert;
import org.junit.Test;
import selection.PopulationTooSmallException;

import java.util.ArrayList;

public class TournamentSelection {

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

        int[] pseudoInts = {0, 1, 2, 3};
        Configuration.instance.random = new PseudoRandom(pseudoInts);

        selection.TournamentSelection ts = new selection.TournamentSelection();

        Configuration.instance.TOURNAMENT_SELECT_COUNT = 2;
        ArrayList<Tour> expected = new ArrayList<>();
        expected.add(tour[0]);
        expected.add(tour[2]);

        Assert.assertEquals(expected, ts.doSelection(pop));
    }
}