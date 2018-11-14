package main;

import random.MersenneTwisterFast;

import java.util.Random;

public enum Configuration {
    instance;

    public String fileSeparator = System.getProperty("file.separator");
    public String userDirectory = System.getProperty("user.dir");

    public String statisticsDirectory = userDirectory + fileSeparator + "src" + fileSeparator + "statistics" + fileSeparator;

    public String dataDirectory = userDirectory + fileSeparator + "data" + fileSeparator;
    public String dataFilePath = dataDirectory + "TSP280.txt";

    public String databaseFile = dataDirectory + "datastore.db";

    public int ROULETTE_WHEEL_SELECT_COUNT = 26;

    public int TOURNAMENT_SELECT_COUNT = 26;

    public int iterationsMaximum = 10000;

    public int abortScenarioNumber = 1000;

    public Random random = new MersenneTwisterFast();
}