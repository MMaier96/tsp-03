package selection;

import base.Population;
import base.Tour;

import java.util.ArrayList;

public interface ISelection {
    ArrayList<Tour> doSelection(Population population) throws PopulationTooSmallException;
}