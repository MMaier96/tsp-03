package mutation;

import base.City;
import base.Tour;
import main.Configuration;
import main.Scenario;
import random.MersenneTwisterFast;

import java.util.ArrayList;
import java.util.Collections;

public class InversionMutation implements IMutation {
    @Override
    public ArrayList<Tour> doMutation(ArrayList<Tour> tours, double mutationRatio) {
        MersenneTwisterFast randomGenerator = (MersenneTwisterFast) Configuration.instance.random;

        for (Tour tour : tours) {
            if (randomGenerator.nextBoolean(mutationRatio)) {
                int arrayLength = tour.getSize();
                int random1 = randomGenerator.nextInt(arrayLength);
                int random2 = randomGenerator.nextInt(arrayLength);
                int small = Math.min(random1, random2);
                int large = Math.max(random1, random2);
                ArrayList<City> tourTemp = new ArrayList<>();
                int temp = 0;
                for (int i = small; i <= large; i++) {
                    tourTemp.add(temp, tour.getCity(i));
                    temp++;
                }

                Collections.reverse(tourTemp);
                int start = small;
                for (City aTourTemp : tourTemp) {
                    tour.addCity(start, aTourTemp);
                    start++;
                }
            }
        }

        return tours;
    }

    public String toString() {
        return getClass().getSimpleName();
    }
}
