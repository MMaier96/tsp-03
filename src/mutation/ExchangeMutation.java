package mutation;

import base.City;
import base.Tour;
import main.Configuration;
import main.Scenario;
import random.MersenneTwisterFast;

import java.util.ArrayList;

public class ExchangeMutation implements IMutation {
    public ArrayList<Tour> doMutation(ArrayList<Tour> tours, double mutationRatio) {
        MersenneTwisterFast randomGenerator = (MersenneTwisterFast) Configuration.instance.random;

        for (Tour tour : tours) {
            if (randomGenerator.nextBoolean(mutationRatio)) { 
                int arrayLength = tour.getSize();
                int random1 = randomGenerator.nextInt(arrayLength);
                int random2 = randomGenerator.nextInt(arrayLength);
                City tempCity1 = tour.getCity(random1);
                City tempCity2 = tour.getCity(random2);
                tour.addCity(random1, tempCity2);
                tour.addCity(random2, tempCity1);
            }
        }

        return tours;
    }

    public String toString() {
        return getClass().getSimpleName();
    }
}