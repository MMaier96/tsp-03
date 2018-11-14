package crossover;

import base.Tour;

public interface ICrossover {
    Tour doCrossover(Tour tour01, Tour tour02);
}