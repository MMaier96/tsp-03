package crossover;

import base.City;
import base.Tour;
import random.MersenneTwisterFast;

import java.util.ArrayList;

public class OrderedCrossover implements ICrossover {
    public Tour doCrossover(Tour tour1, Tour tour2) {
        if (tour1.getSize() != tour2.getSize()) {
            System.out.println("Different chromosom length of parents");
            return null;
        }
        ArrayList<City> parent1 = (ArrayList<City>) tour1.getCities().clone();
        ArrayList<City> parent2 = (ArrayList<City>) tour2.getCities().clone();

        //generate gene Seperator
        MersenneTwisterFast randomGenerator = new MersenneTwisterFast();
        int minBorder;
        int maxBorder;
        do {
            minBorder = randomGenerator.nextInt(0, parent1.size() - 1);
            maxBorder = randomGenerator.nextInt(0, parent1.size() - 1);
            if (minBorder > maxBorder) {
                int temp = minBorder;
                minBorder = maxBorder;
                maxBorder = temp;
            }
        } while (maxBorder - minBorder >= parent1.size() - 1);

        //generate two children
        Tour child1 = new Tour();
        Tour child2 = new Tour();

        City dummyCity = new City(0, 0, 0);
        for (int i = 0; i < parent1.size(); i++) {
            child1.addCity(dummyCity);
            child2.addCity(dummyCity);
        }

        for (int i = minBorder; i <= maxBorder; i++) {
            child1.getCities().set(i, parent1.get(i));
            child2.getCities().set(i, parent2.get(i));
        }

        finishChilden(child1, parent2, minBorder, maxBorder);
        finishChilden(child2, parent1, minBorder, maxBorder);

        //return child with better fitness
        if (child1.getFitness() <= child2.getFitness()) {
            return child1;
        }
        return child2;
    }

    public String toString() {
        return getClass().getSimpleName();
    }

    private void finishChilden(Tour child, ArrayList<City> parent, int minBorder, int maxBorder) {
        for (int i = 0; i < child.getSize(); i++) {
            if (i == minBorder) {
                i += (maxBorder + 1 - minBorder);
            }
                while (parent.size() > 0 && child.containsCity(parent.get(0))) {
                    parent.remove(0);
                }
            if (parent.size()>0) {
            child.addCity(i, parent.get(0));
            parent.remove(0);
            }
/*            if (parent.size() == 0){
                for (int p = 0; p < child.getSize(); p++) {
                    if (child.getCity(p).getId() == 0)
                        System.err.println("Child is not Finished! Ordered Crossover");
                }
            }*/
        }
    }
}