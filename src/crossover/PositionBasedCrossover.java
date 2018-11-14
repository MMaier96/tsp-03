package crossover;

import base.Tour;
import base.City;
import random.MersenneTwisterFast;
import java.util.ArrayList;


public class PositionBasedCrossover implements ICrossover {

    MersenneTwisterFast rand = new MersenneTwisterFast();

    public Tour doCrossover(Tour tour01,Tour tour02) {

        int rand1 = rand.nextInt(tour01.getSize());

        base.Tour child = new Tour();

        ArrayList<Integer> positions = new ArrayList<>();
        ArrayList<base.City> listTour02 = new ArrayList<>(tour02.getCities());

        for (int i=0; i<=rand1; i++)
        {
            positions.add(rand.nextInt(tour01.getSize()));
        }

        for (int i=0; i<positions.size(); i++)
        {
            for (int j=i+1; j<positions.size(); j++)
            {
                if (positions.get(i) == positions.get(j))
                {
                    positions.remove(j);
                    i = 0;
                    j = i+1;
                }

            }
        }

        child.setCities(tour01.getCities());

        for (int i=0; i<positions.size(); i++)
        {
            int pJ = 0;
            for (int j=0; j<listTour02.size(); j++) {
                if (listTour02.get(j).getId() == tour01.getCity(positions.get(i)).getId()) {
                    pJ = j;
                }
            }
            if (listTour02.get(pJ).getId() == tour01.getCity(positions.get(i)).getId()) {
                listTour02.remove(pJ);
            }
        }

        for (int i=0; i<tour01.getSize(); i++)
        {
            boolean inPosition = false;
            for (int j=0; j<positions.size(); j++)
            {
                if (i == positions.get(j))
                {
                    inPosition = true;
                }
            }
            if(!inPosition)
            {
                child.addCity(i, listTour02.get(0));
                listTour02.remove(0);
            }
        }

        return child;
    }

    public String toString() {
        return getClass().getSimpleName();
    }
}