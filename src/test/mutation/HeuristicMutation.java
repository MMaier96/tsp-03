package test.mutation;

import base.City;
import base.Tour;
import main.Configuration;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import static org.junit.Assert.*;

public class HeuristicMutation {
    private mutation.HeuristicMutation mutation;
    private Tour tour;
    private ArrayList<Tour> tours;
    private double mutationRatio;
    private Random random = Configuration.instance.random;

    @Before
    public void testSetup() {
        mutation = new mutation.HeuristicMutation();
        tour = new Tour();
        for (int i = 1; i <= 280; i++) {
            tour.addCity(new City(i, random.nextInt(281), random.nextInt(281)));
        }
        tours = new ArrayList<>();
        tours.add(tour);
        mutationRatio = 1.0;
    }

    @Test
    public void testResultNotNull() {
        ArrayList<Tour> result = mutation.doMutation(tours,mutationRatio);
        assertNotNull(result);
    }

    @Test
    public void testResultLength() {
        ArrayList<Tour> result = mutation.doMutation(tours,mutationRatio);
        assertEquals(280, result.remove(0).getSize());
    }

    @Test
    public void checkElement0NotExists() {
        ArrayList<Tour> result = mutation.doMutation(tours,mutationRatio);
        assertFalse(result.remove(0).containsCity(new City(0, 0, 0)));
    }

    @Test
    public void checkElement281NotExists() {
        ArrayList<Tour> result = mutation.doMutation(tours,mutationRatio);
        assertFalse(result.remove(0).containsCity(new City(281, 281, 281)));
    }

    @Test
    public void checkElements() {
        ArrayList<Tour> result = mutation.doMutation(tours,mutationRatio);
        assertTrue(result.get(0).getCities().containsAll(tour.getCities()) && tour.getCities().containsAll(result.remove(0).getCities()));
    }

    @Test
    public void checkForDuplicates() {
        ArrayList<Tour> result = mutation.doMutation(tours,mutationRatio);
        ArrayList<City> cities = result.get(0).getCities();
        HashSet<City> resultSet = new HashSet<>(cities);
        assertEquals(280, resultSet.size());
    }

    @Test
    public void checkIfSomethingChanged() {
        double fitnessBeforeMutation = tour.getFitness();
        ArrayList<Tour> result = mutation.doMutation(tours, mutationRatio);
        ArrayList<City> cities = result.get(0).getCities();
        assertNotEquals(fitnessBeforeMutation, result.remove(0).getFitness());
    }
}