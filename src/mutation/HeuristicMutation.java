package mutation;

import base.City;
import base.Tour;
import main.Configuration;
import main.Scenario;
import random.MersenneTwisterFast;

import java.util.ArrayList;

public class HeuristicMutation implements IMutation {
    public ArrayList<Tour> doMutation(ArrayList<Tour> tours, double mutationRatio) {
        MersenneTwisterFast randomGenerator = (MersenneTwisterFast) Configuration.instance.random;

        for (int index = 0; index < tours.size(); index++) {
            if (randomGenerator.nextBoolean(mutationRatio)) {
                ArrayList<City> cities = tours.get(index).getCities();
                int lambda = randomGenerator.nextInt(2,8);               //TODO check if n = 8 is an appropriate value
                int numberOfPermutations = 1;
                for(int i = 1; i <= lambda; i++)
                    numberOfPermutations *= i;
                ArrayList<Integer> allNumbers = new ArrayList<>();
                allNumbers.ensureCapacity(cities.size());
                for (int i = 0; i < cities.size(); i++) {
                    allNumbers.add(i);
                }
                ArrayList<Integer> positions = new ArrayList<>();
                for (int i = 0; i < lambda; i++) {
                    positions.add(allNumbers.remove(randomGenerator.nextInt(allNumbers.size())));
                }
                ArrayList<City> targets = new ArrayList<>();
                for (Integer position : positions) {
                    targets.add(cities.get(position));
                }
                ArrayList<ArrayList<City>> permutations = permutation(targets);

                ArrayList<Tour> possibleTours = new ArrayList<>(numberOfPermutations);

                permutations.parallelStream().forEach(onePermutation ->{
                    ArrayList<City> citiesPermutation = new ArrayList<>();
                    citiesPermutation.addAll(cities);
                    for (int i = 0; i < lambda; i++) {
                        citiesPermutation.set(positions.get(i), onePermutation.remove(0));
                    }
                    Tour oneTour = new Tour();
                    oneTour.setCities(citiesPermutation);
                    possibleTours.add(oneTour);
                });

                double minimumDistance = tours.get(index).getFitness();
                for (Tour eachTour : possibleTours) {
                    if (eachTour.getFitness() <= minimumDistance) {
                        minimumDistance = eachTour.getFitness();
                        tours.get(index).setCities(eachTour.getCities());
                    }
                }
            }
        }

        return tours;
    }

    public ArrayList<ArrayList<City>> permutation(ArrayList<City> nums) {
        ArrayList<ArrayList<City>> accum = new ArrayList<>();
        permutation(accum, new ArrayList<>(), nums);
        return accum;
    }

    private static void permutation(ArrayList<ArrayList<City>> accum, ArrayList<City> prefix, ArrayList<City> nums) {
        int n = nums.size();
        if (n == 0) {
            accum.add(prefix);
        } else {
            for (int i = 0; i < n; ++i) {
                ArrayList<City> newPrefix = new ArrayList<>(prefix);
                newPrefix.add(nums.get(i));
                ArrayList<City> numsLeft = new ArrayList<>(nums);
                numsLeft.remove(i);
                permutation(accum, newPrefix, numsLeft);
            }
        }
    }

    public String toString() {
        return getClass().getSimpleName();
    }
}