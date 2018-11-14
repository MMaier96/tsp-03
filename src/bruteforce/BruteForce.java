package bruteforce;

import base.City;
import base.Population;
import base.Tour;
import main.Configuration;
import random.MersenneTwisterFast;

import java.util.*;
import java.util.Map.Entry;

public class BruteForce {


    private MersenneTwisterFast mtwister = (MersenneTwisterFast) Configuration.instance.random;

    public Map<Tour, Double> splitMap(Map<Tour, Double> tours, long lowerLimit, long upperLimit){
        Map<Tour, Double> splittedMap = new HashMap<Tour,Double>();
        long counter=0;
        for (Tour tour:tours.keySet()){
            if(counter>=lowerLimit && counter<upperLimit){
                splittedMap.put(tour, tour.getFitness());
            }
            counter++;
        }
        return splittedMap;
    }

    public double getFitnessAll(Map<Tour, Double> tours){
        Map<Tour, Double> sortedTours = sortByComparator(tours);
        Map.Entry<Tour, Double> entry = sortedTours.entrySet().iterator().next();
        Double value=entry.getValue();
        return value;
    }

    public double getFitnessTop25(Map<Tour, Double> tours){
        long quarterSize = tours.size()/4;
        Map <Tour, Double> newMap = splitMap(tours, 0, quarterSize);
        Map<Tour, Double> sortedTours = sortByComparator(newMap);
        Map.Entry<Tour, Double> entry = sortedTours.entrySet().iterator().next();
        Double value=entry.getValue();
        return value;
    }

    public double getFitnessMid50(Map<Tour, Double> tours){
        long quarterSize = tours.size()/4;
        Map <Tour, Double> newMap = splitMap(tours, quarterSize, quarterSize*3);
        Map<Tour, Double> sortedTours = sortByComparator(newMap);
        Map.Entry<Tour, Double> entry = sortedTours.entrySet().iterator().next();
        Double value=entry.getValue();
        return value;
    }

    public double getFitnessLast25(Map<Tour, Double> tours){
        long quarterSize = tours.size()/4;
        Map <Tour, Double> newMap = splitMap(tours, quarterSize*3, tours.size());
        Map<Tour, Double> sortedTours = sortByComparator(newMap);
        Map.Entry<Tour, Double> entry = sortedTours.entrySet().iterator().next();
        Double value=entry.getValue();
        return value;
    }

    public Map<Tour, Double> createPermutationEvaluate(long permutationsNumber) {
        Map<Tour, Double> tours = new LinkedHashMap<>();
        long counter = 0;
        do {
            Tour newTour = generateTourPermutation();
            double fitnessValue;
            fitnessValue = newTour.getFitness();
            tours.put(newTour, fitnessValue);
            counter++;
        } while (counter < permutationsNumber);
        return tours;
    }

    public Tour generateTourPermutation() {
        int random;
        Tour newTour = new Tour();
        HashSet<City> cityArrayList = new HashSet<>();
        ArrayList<City> tempList = new ArrayList<>();
        City tempCity;
        for (int i = 0; i < 280; i++) {
            do {
                double x = mtwister.nextInt(0, 279);
                double y = mtwister.nextInt(0, 279);
                tempCity = new City(i, x, y);
            } while (!cityArrayList.add(tempCity));
        }
        for (City city:cityArrayList){
            tempList.add(city);
        }

        do{
            random = mtwister.nextInt(tempList.size());
            if(random>Math.round(tempList.size()/2)){
                newTour.addCity(tempList.get(random));
                tempList.remove(random);
            }
            if(tempList.size()==2){
                newTour.addCity(tempList.get(1));
                newTour.addCity(tempList.get(0));
            }
        }
        while(newTour.getSize()<280);
        return newTour;
    }

    private static Map<Tour, Double> sortByComparator(Map<Tour, Double> unsortMap){
        List<Entry<Tour, Double>> list = new LinkedList<Entry<Tour, Double>>(unsortMap.entrySet());
        list.sort(Comparator.comparing(Entry::getValue));

        Map<Tour, Double> sortedMap = new LinkedHashMap<Tour, Double>();
        for (Entry<Tour, Double> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }



    public void evaluateFitness(long permutationNumber){
        Scanner scan = new Scanner(System.in);
        System.out.println("Choose your evaluation option:      ");
        System.out.println("1. Get Best of  Top 25% Percentile  Permutations");
        System.out.println("2. Get Best of  Mid-50% Percentile Permutations");
        System.out.println("3. Get Best of  Last 25% Percentile  Permutations");
        System.out.println("4. Get Best of  All                 Permutations");
        System.out.println("exit : Exit loop");
        String option = scan.nextLine();
        Map<Tour, Double> tours = createPermutationEvaluate(permutationNumber);
        boolean exit=false;
        while (!exit)
            switch (option){
                case "1":
                    System.out.println("Best of Top 25% Percentile Fitness: data this Permutations "+getFitnessTop25(tours));
                    option = scan.nextLine();
                    break;
                case "2":
                    System.out.println("Best of Mid-50% Percentile Fitness data this Permutations "+getFitnessMid50(tours));
                    option = scan.nextLine();
                    break;
                case "3":
                    System.out.println("Best of Last 25% Percentile Fitness data this Permutations "+getFitnessLast25(tours));
                    option = scan.nextLine();
                    break;
                case "4":
                    System.out.println("Best of All Fitness data this Permutations : "+ getFitnessAll(tours));
                    option = scan.nextLine();
                    break;
                case "exit":
                    exit=true;
                    break;
                default:
                    System.out.println("Wrong Option");
                    option = scan.nextLine();
            }
    }

    public static void main (String args[]){
        BruteForce bruteForce=new BruteForce();
        bruteForce.evaluateFitness(100);
    }
}
