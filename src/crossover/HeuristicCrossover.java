package crossover;

import base.City;
import base.Tour;
import random.MersenneTwisterFast;

import java.util.*;

public class HeuristicCrossover implements ICrossover {
    public Tour doCrossover(Tour tour1, Tour tour2) {
        if (tour1.getSize() != tour2.getSize()) {
            System.out.println("Different chromosom length of parents");
            return null;
        }
        ArrayList<City> parent1 = (ArrayList<City>) tour1.getCities().clone();
        ArrayList<City> parent2 = (ArrayList<City>) tour2.getCities().clone();

        //generate two children
        Tour child = new Tour();

        //not necessary for coding, but essential for idea of evo. Allways create Dummy Child first.
        City dummyCity = new City(0, 0, 0);
        for (int i = 0; i < parent1.size(); i++) {
            child.addCity(dummyCity);
        }

        //get Random Start City
        MersenneTwisterFast randomGenerator = new MersenneTwisterFast();
        int positionParent1 = randomGenerator.nextInt(0, parent1.size() - 1);
        int positionParent2 = parent2.indexOf(parent1.get(positionParent1));
        ArrayList<Integer> alreadyUsedIndices = new ArrayList<>();

        //add first gene to child
        child.addCity(0, parent1.get(positionParent1));
        alreadyUsedIndices.add(positionParent1);

        //do the Magic, fill child from left starting with the second gene in the child
        for (int i = 1; i < child.getSize(); i++) {
            HashMap<City, Double> neighboursAndFitness = new HashMap<>();
            //gets all Neighbour Citys with responding Fitness. Saves them into those maps
            getNeighbourCitys(child, parent1, positionParent1, parent2, positionParent2, neighboursAndFitness);

            if (neighboursAndFitness.isEmpty()) {
                //if there are no more neighbours without building a cycle choose a random city
                while (alreadyUsedIndices.contains(positionParent1)) {
                    positionParent1 = randomGenerator.nextInt(0, parent1.size() - 1);
                }
                positionParent2 = parent2.indexOf(parent1.get(positionParent1));
                child.addCity(i, parent1.get(positionParent1));
                alreadyUsedIndices.add(positionParent1);
            } else {
                Map.Entry<City, Double> bestFitness = Collections.min(neighboursAndFitness.entrySet(), Comparator.comparingDouble(Map.Entry::getValue));
                City bestFitnessCity = bestFitness.getKey();

                positionParent1 = parent1.indexOf(bestFitnessCity);
                positionParent2 = parent2.indexOf(bestFitnessCity);
                child.addCity(i, parent1.get(positionParent1));
                alreadyUsedIndices.add(positionParent1);
            }
        }


        return child;
    }

    public String toString() {
        return getClass().getSimpleName();
    }

    private double getFitness(City city1, City city2) {
        double distance = 0.0;

        double x1 = city1.getX();
        double y1 = city1.getY();
        double x2 = city2.getX();
        double y2 = city2.getY();
        distance = distance + Tour.euclideanDistance(x1, y1, x2, y2);

        return distance;
    }

    private void getNeighbourCitys(Tour child, ArrayList<City> parent1, int positionParent1, ArrayList<City> parent2, int positionParent2, HashMap<City, Double> neighbourCitys) {
        //Check Citys in First Parent
        int tempPosParentLeft, tempPosParentRight;
        if (positionParent1 == 0) {
            tempPosParentLeft = parent1.size() - 1;
            tempPosParentRight = positionParent1 + 1;
        } else if (positionParent1 == parent1.size() - 1) {
            tempPosParentLeft = positionParent1 - 1;
            tempPosParentRight = 0;
        } else {
            tempPosParentLeft = positionParent1 - 1;
            tempPosParentRight = positionParent1 + 1;
        }

        if (!child.containsCity(parent1.get(tempPosParentLeft))) {
            neighbourCitys.put(parent1.get(tempPosParentLeft), getFitness(parent1.get(tempPosParentLeft), parent1.get(positionParent1)));
        }
        if (!child.containsCity(parent1.get(tempPosParentRight))) {
            neighbourCitys.put(parent1.get(tempPosParentRight), getFitness(parent1.get(tempPosParentRight), parent1.get(positionParent1)));
        }

        //Check Citys for second Parent
        if (positionParent2 == 0) {
            tempPosParentLeft = parent2.size() - 1;
            tempPosParentRight = positionParent2 + 1;
        } else if (positionParent2 == parent2.size() - 1) {
            tempPosParentLeft = positionParent2 - 1;
            tempPosParentRight = 0;
        } else {
            tempPosParentLeft = positionParent2 - 1;
            tempPosParentRight = positionParent2 + 1;
        }
        if (!child.containsCity(parent2.get(tempPosParentLeft))) {
            neighbourCitys.put(parent2.get(tempPosParentLeft), getFitness(parent2.get(tempPosParentLeft), parent1.get(positionParent1)));
        }
        if (!child.containsCity(parent2.get(tempPosParentRight))) {
            neighbourCitys.put(parent2.get(tempPosParentRight), getFitness(parent2.get(tempPosParentRight), parent1.get(positionParent1)));
        }
    }
}