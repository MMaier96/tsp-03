package mutation;

import base.City;
import base.Tour;
import main.Configuration;
import main.Scenario;
import random.MersenneTwisterFast;

import java.util.ArrayList;

public class DisplacementMutation implements IMutation {
    @Override
    public ArrayList<Tour> doMutation(ArrayList<Tour> tours, double mutationRatio) {
        MersenneTwisterFast randomGenerator = (MersenneTwisterFast) Configuration.instance.random;

        for (Tour tour : tours) {
            if (randomGenerator.nextBoolean(mutationRatio)) {
                int startPoint = randomGenerator.nextInt(tour.getSize());
                int endPoint = randomGenerator.nextInt(tour.getSize());
                if (startPoint > endPoint) {
                    int temp = startPoint;
                    startPoint = endPoint;
                    endPoint = temp;
                }
                ArrayList<City> cities = tour.getCities();
                ArrayList<City> displaced = new ArrayList<>();
                for (int i = startPoint; i <= endPoint; i++) {
                    displaced.add(cities.remove(startPoint));
                }
                int insertionPoint;
                if (cities.size() <= 1) {
                    insertionPoint = 0;
                } else {
                    do {
                        insertionPoint = randomGenerator.nextInt(cities.size());
                    } while (insertionPoint == startPoint);
                }
                cities.addAll(insertionPoint, displaced);

                tour.setCities(cities);
            }
        }

        return tours;
    }

    public String toString() {
        return getClass().getSimpleName();
    }
}