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
    public ArrayList<Tour> doMutation(ArrayList<Tour> tours) {
        MersenneTwisterFast randomGenerator = (MersenneTwisterFast) Configuration.instance.random;
        Scenario scenario = new Scenario();                     //TODO is this correct?

        for (Tour tour : tours) {
            if (randomGenerator.nextBoolean(scenario.getMutationRatio())) {
                int arrayLength = tour.getSize();
                int random1 = randomGenerator.nextInt(0, arrayLength);
                int random2 = randomGenerator.nextInt(0, arrayLength);
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
                System.out.println(tour.getCities().toString());
            }
        }

        return tours;
    }

    public String toString() {
        return getClass().getSimpleName();
    }
}