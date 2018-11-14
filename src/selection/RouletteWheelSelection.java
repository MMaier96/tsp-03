package selection;

import base.Population;
import base.Tour;
import main.Configuration;

import java.util.ArrayList;
import java.util.SortedMap;
import java.util.TreeMap;

public class RouletteWheelSelection implements ISelection {

    private SortedMap<Double, Tour> probabilityTourMap;

    public ArrayList<Tour> doSelection(Population population) throws PopulationTooSmallException {

        if (population.getTours().size() < Configuration.instance.ROULETTE_WHEEL_SELECT_COUNT) {
            throw new PopulationTooSmallException();
        }

        probabilityTourMap = calculateProbabilityTourMap(population);
        ArrayList<Tour> result = new ArrayList<>();

        for (int i = 0; i < Configuration.instance.ROULETTE_WHEEL_SELECT_COUNT; ) {
            Tour select = doSingleSelection();

            if (!result.contains(select)) {
                result.add(select);
                i++;
            }
        }

        return result;
    }

    public Tour doSingleSelection() {
        double chance = Configuration.instance.random.nextDouble();

        SortedMap<Double, Tour> probabilityMap = probabilityTourMap;
        for (double key : probabilityMap.keySet()) {
            if (chance <= key) {
                return probabilityMap.get(key);
            }
        }
        return probabilityMap.get(probabilityMap.lastKey());
    }

    public String toString() {
        return getClass().getSimpleName();
    }

    private static double getFitnessSum(Population population) {
        double fitnessSum = 0;

        for (Tour tour : population.getTours()) {
            fitnessSum += tour.getFitness();
        }

        return fitnessSum;
    }

    private SortedMap<Double, Tour> calculateProbabilityTourMap(Population population) {
        final double fitnessSum = getFitnessSum(population);
        SortedMap<Double, Tour> tourMap = new TreeMap<>();

        double probability = 0;
        for (Tour tour : population.getTours()) {
            probability += tour.getFitness() / fitnessSum;
            tourMap.put(probability, tour);
        }

        return tourMap;
    }
}