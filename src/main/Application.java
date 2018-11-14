package main;

import base.City;
import base.Population;
import base.Tour;
import bruteforce.BruteForce;
import crossover.*;
import data.HSQLDBManager;
import data.InstanceReader;
import data.TSPLIBReader;
import mutation.*;
import random.MersenneTwisterFast;
import selection.ISelection;
import selection.PopulationTooSmallException;
import selection.RouletteWheelSelection;
import selection.TournamentSelection;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

public class Application {
    private static ArrayList<City> availableCities;
    private double[][] distances;
    private MersenneTwisterFast mtwister;
    private double lastResult = 0;
    private ISelection selection;
    private ICrossover crossover;
    private IMutation mutation;

    public void startupHSQLDB() {
        HSQLDBManager.instance.startup();
        HSQLDBManager.instance.init();
    }

    public void shutdownHSQLDB() {
        HSQLDBManager.instance.shutdown();
    }

    public void printMatrix(double[][] matrix) {
        DecimalFormat decimalFormat = new DecimalFormat("000.00");

        for (int rowIndex = 0; rowIndex < matrix.length; rowIndex++) {
            for (int columnIndex = 0; columnIndex < matrix[0].length; columnIndex++)
                System.out.print(decimalFormat.format(matrix[rowIndex][columnIndex]) + "\t");
            System.out.println();
        }
    }

    public void loadData() {
        System.out.println("--- GeneticAlgorithm.loadData()");
        InstanceReader instanceReader = new InstanceReader(Configuration.instance.dataFilePath);
        instanceReader.open();
        TSPLIBReader tspLibReader = new TSPLIBReader(instanceReader);

        availableCities = tspLibReader.getCities();
        System.out.println("availableCities (size) : " + availableCities.size());

        distances = tspLibReader.getDistances();

        printMatrix(distances);

        instanceReader.close();

        System.out.println();
    }

    public Scenario[] initConfiguration() {
        System.out.println("--- GeneticAlgorithm.initConfiguration()");
        System.out.println();
        Scenario[] scenarios = null;
        try {
            scenarios = new XMLParser(new File("configuration/genetic_algorithm_tsp.xml")).getScenarios();

            if (scenarios == null) {
                throw new Exception();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Szenario-Datei nicht gefunden");
            System.exit(1);
        }
        System.out.println("Es wurden " + scenarios.length + " Szenarien geladen. ");
        return scenarios;
    }

    public void execute(Scenario[] scenarios) {
        int iterationsMax = Configuration.instance.iterationsMaximum;
        double bruteForceResult = 0;
        for (Scenario scenario : scenarios) {
            long populationStartSize = Long.parseUnsignedLong("100");
            Population population = createPopulation(availableCities, populationStartSize);
            switch (scenario.getSelection()) {
                case TOURNAMENT:
                    selection = new TournamentSelection();
                    break;
                case ROULETTE_WHEEL:
                    selection = new RouletteWheelSelection();
                    break;
            }
            switch (scenario.getCrossover()) {
                case CYCLE:
                    crossover = new CycleCrossover();
                    break;
                case ORDERED:
                    crossover = new OrderedCrossover();
                    break;
                case POSITION:
                    crossover = new PositionBasedCrossover();
                    break;
                case HEURISTIC:
                    crossover = new HeuristicCrossover();
                    break;
                case PARTIALLY_MATCHED:
                    crossover = new PartiallyMatchedCrossover();
                    break;
                case SUB_TOUR_EXCHANGE:
                    crossover = new SubTourExchangeCrossover();
                    break;
            }
            switch (scenario.getMutation()) {
                case HEURISTIC:
                    mutation = new HeuristicMutation();
                    break;
                case DISPLACEMENT:
                    mutation = new DisplacementMutation();
                    break;
                case EXCHANGE:
                    mutation = new ExchangeMutation();
                    break;
                case INSERTION:
                    mutation = new InsertionMutation();
                    break;
                case INVERSION:
                    mutation = new InversionMutation();
                    break;
            }

            System.out.println("-------------------");
            System.out.println(" Scenario: " + scenario.getId());
            System.out.println(" Crossover Type: " + scenario.getCrossover());
            System.out.println(" Crossover Ratio: " + scenario.getCrossoverRatio());
            System.out.println(" Mutation Type: " + scenario.getMutation());
            System.out.println(" Mutation Ratio: " + scenario.getMutationRatio());
            System.out.println(" Selection Type: " + scenario.getSelection());
            System.out.println("-------------------");
            long startTime = System.currentTimeMillis();
            double bestResult = 80000;
            int iterationsCounter = 1;
            if (scenario.getIsEvaluated()) {
                //  long permutationsNumber = scenario.getMaximumNumberOfEvaluations();
                long permutationsNumber = Long.parseUnsignedLong("100");
                bruteForceResult = bruteForceResult(permutationsNumber);
            }
            int sameResultCounter = 0;

            do {
                double result = 0;
                ArrayList<Tour> newPopulation;
                ArrayList<Tour> parents;

                newPopulation = population.getTours();
                try {
                    parents = selection.doSelection(population);
                    mtwister = (MersenneTwisterFast) Configuration.instance.random;
                    if (mtwister.nextBoolean(scenario.getCrossoverRatio())) {
                        for (int j = 1; j < 26; j = j + 2) {
                            Tour child1 = crossover.doCrossover(parents.get(j), parents.get(j - 1));
                            Tour child2 = crossover.doCrossover(parents.get(j), parents.get(j - 1));
                            newPopulation.add(child1);
                            newPopulation.add(child2);
                        }
                    }

                    newPopulation = mutation.doMutation(newPopulation, scenario.getMutationRatio());

                    population.setTours(newPopulation);
                    result = this.getBestResult(population);

                    if (result < bestResult) {
                        bestResult = result;
                    }

                    HSQLDBManager.instance.addFitnessToScenario(scenario.getId(), iterationsCounter, result);

                    if (lastResult == result) {
                        sameResultCounter++;
                    } else {
                        sameResultCounter = 0;
                    }
                    lastResult = result;

                    if (iterationsCounter == 1) {
                        System.out.println("Iteration: " + iterationsCounter + " Starting with value: " + Math.round(result));
                    }
                    if (iterationsCounter % (iterationsMax / 4) == 0) {
                        System.out.println("Iteration: " + iterationsCounter + " Best value: " + Math.round(bestResult) + " Population Size: " + newPopulation.size());
                    }
                    if (iterationsCounter == iterationsMax) {
                        HSQLDBManager.instance.writeCsv(scenario.getId(), iterationsCounter);
                    }

                } catch (PopulationTooSmallException e) {
                    e.printStackTrace();
                }
                if (iterationsCounter == iterationsMax) {
                    long endTime = System.currentTimeMillis();
                    System.out.println("Runtime " + (endTime - startTime) / 1000 + " Seconds");
                }
                iterationsCounter++;
                if (sameResultCounter >= Configuration.instance.abortScenarioNumber) {
                    System.out.println("The Fitness Value hasn't changed for the " + sameResultCounter + "th time, this Scenario has been stopped.");
                    HSQLDBManager.instance.writeCsv(scenario.getId(), iterationsCounter);
                }
            }
            while (!isSolutionQualityReached(bestResult) && iterationsCounter <= iterationsMax && sameResultCounter < Configuration.instance.abortScenarioNumber);
            if (scenario.isEvaluated()) {
                System.out.println("BruteForce Best Result : " + Math.round(bruteForceResult) + " Algorithm Best Result : " + Math.round(bestResult));
            }
        }
    }

    public double getBestResult(Population population) {
        double bestResult = 1000000;
        for (Tour tour : population.getTours()) {
            if (tour.getFitness() < bestResult) {
                bestResult = tour.getFitness();
            }
        }
        return bestResult;
    }

    private boolean isSolutionQualityReached(double quality) {
        return (quality * 0.95) <= 2579;
    }

    public double bruteForceResult(long permutationsNumber) {
        BruteForce bruteForce = new BruteForce();
        Map<Tour, Double> map = bruteForce.createPermutationEvaluate(permutationsNumber);
        return bruteForce.getFitnessAll(map);
    }

    public Population createPopulation(ArrayList<City> cityList, long permutationsNumber) {
        Population population = new Population();
        HashSet<Tour> tours = new HashSet<>();
        long counter = 0;
        while (counter < permutationsNumber) {
            Tour newTour = generateTour(cityList);
            if (tours.add(newTour)) {
                counter++;
            }
        }
        ArrayList<Tour> PopulationTours = new ArrayList<>(tours);

        population.setTours(PopulationTours);

        return population;
    }

    public Tour generateTour(ArrayList<City> cityArrayList) {

        Tour newTour = new Tour();

        int random;

        for (int i = 0; i < cityArrayList.size(); i++) {
            do {
                random = Configuration.instance.random.nextInt(cityArrayList.size());
            } while (newTour.containsCity(cityArrayList.get(random)));
            newTour.addCity(cityArrayList.get(random));
        }

        return newTour;
    }

    public static void main(String... args) {
        Application application = new Application();
        application.startupHSQLDB();
        application.loadData();
        Scenario[] scenarios = application.initConfiguration();
        application.execute(scenarios);
        application.shutdownHSQLDB();
    }
}
