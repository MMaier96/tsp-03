package test.mutation;

import base.City;
import base.Tour;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.Assert.*;

public class InsertionMutation {
    private mutation.InsertionMutation mutation;
    private Tour tour;
    private ArrayList<Tour> tours;
    private double mutationRatio;

    @Before
    public void testSetup() {
        mutation = new mutation.InsertionMutation();
        tour = new Tour();
        for (int i = 1; i <= 280; i++) {
            tour.addCity(new City(i, i, i));
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
        HashSet<City> resultSet = new HashSet<>(result.remove(0).getCities());
        assertEquals(280, resultSet.size());
    }
}