package mutation;

import base.City;
import base.Tour;
import main.Configuration;
import main.Scenario;
import random.MersenneTwisterFast;

import java.util.ArrayList;

public class InsertionMutation implements IMutation {
    @Override
    public ArrayList<Tour> doMutation(ArrayList<Tour> tours, double mutationRatio) {
        MersenneTwisterFast randomGenerator = (MersenneTwisterFast) Configuration.instance.random;

        for (Tour tour : tours) {
            if (randomGenerator.nextBoolean(mutationRatio)) {
                int selectionPoint = randomGenerator.nextInt(tour.getSize());
                ArrayList<City> cities = tour.getCities();
                City target = cities.remove(selectionPoint);
                int insertionPoint;
                do {
                    insertionPoint = randomGenerator.nextInt(cities.size());
                } while (insertionPoint == selectionPoint);
                cities.add(insertionPoint, target);

                tour.setCities(cities);
            }
        }

        return tours;
    }

    public String toString() {
        return getClass().getSimpleName();
    }
}