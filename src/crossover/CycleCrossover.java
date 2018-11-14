package crossover;

import base.City;
import base.Tour;
import random.MersenneTwisterFast;

public class CycleCrossover implements ICrossover {

    public Tour doCrossover(Tour tour1, Tour tour2) {

        if (tour1.getSize() != tour2.getSize())
            return null;    //error!

        MersenneTwisterFast generator = new MersenneTwisterFast();
        //setup parents and children
        Tour parent1 = new Tour();
        Tour parent2 = new Tour();
        Tour child1 = new Tour();
        Tour child2 = new Tour();
        City dummy = new City(0, 0, 0);

        for (City city : tour1.getCities()) {
            parent1.addCity(city);
            child1.addCity(dummy);
        }

        for (City city : tour2.getCities()) {
            parent2.addCity(city);
            child2.addCity(dummy);
        }

        //generate start number
        int start = generator.nextInt(0, parent1.getSize() - 1);

        //use start as index
        int index = start;

        //take first city as current
        City currentCity;

        //start algorithm
        do {
            //set city of p1 to ch1
            currentCity = parent1.getCity(index);
            child1.getCities().set(index, currentCity);

            //set city of p2 to ch1
            currentCity = parent2.getCity(index);
            child2.getCities().set(index, currentCity);

            //search new index
            index = parent1.getCities().indexOf(currentCity);

        } while (index != start);

        //reset index
        index = 0;

        //fullfill the rest of children
        while (!parent1.getCities().isEmpty()) {
            if (!child1.getCity(index).equals(dummy)) {
                //remove index from both parents
                parent1.getCities().remove(0);
                parent2.getCities().remove(0);
                index++;
                continue;
            }
            //set p2 to ch1
            child1.getCities().set(index, parent2.getCity(0));
            //set p1 to ch2
            child2.getCities().set(index, parent1.getCity(0));

            //remove first index from both parents
            parent1.getCities().remove(0);
            parent2.getCities().remove(0);

            //incremenet index
            index++;
        }

        //finished!
        return child1.getFitness() < child2.getFitness() ? child1 : child2;
    }

    public String toString() {
        return getClass().getSimpleName();
    }
}