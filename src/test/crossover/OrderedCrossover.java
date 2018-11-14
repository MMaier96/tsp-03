package test.crossover;

import base.City;
import base.Tour;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class OrderedCrossover {

    crossover.OrderedCrossover oc;
    Tour tour1, tour2, child;

    @Before
    public void init() {

        oc = new crossover.OrderedCrossover();
        tour1 = new Tour();
        tour2 = new Tour();

        for (int i = 1; i < 281; i++) {
            tour1.addCity(new City(i, i, i));
        }
        for (int i = 279; i >= 0; i--) {
            tour2.addCity(tour1.getCity(i));
        }

        child = oc.doCrossover(tour1, tour2);
    }

    @Test
    public void testNotNull() {
        assertNotNull(child);
    }

    @Test
    public void testLength() {
        assertEquals(280, child.getSize());
    }

    //this is the only Test needed for Elements, checks for Duplicates, and all if all genes from Parents are existing
    @Test
    public void doCrossoverCheckElements() {
        assertTrue(child.getCities().containsAll(tour1.getCities()) && child.getSize() == tour1.getSize());
    }
}