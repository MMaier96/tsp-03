package mutation;

import base.Tour;

import java.util.ArrayList;

public interface IMutation {
    ArrayList<Tour> doMutation(ArrayList<Tour> tours, double mutationRatio);
}