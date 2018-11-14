package selection;

import base.Population;
import base.Tour;
import main.Configuration;

import java.util.ArrayList;

public class TournamentSelection implements ISelection {
    public ArrayList<Tour> doSelection(Population population) throws PopulationTooSmallException {

        int size = Configuration.instance.TOURNAMENT_SELECT_COUNT * 2;
        if (population.getTours().size() < size) {
            throw new PopulationTooSmallException();
        }

        ArrayList<Tour> result = new ArrayList<>();
        ArrayList<Tour> selected = new ArrayList<>();

        for (int i = 0; i < size; ) {
            Tour select = population.getTours().get(Configuration.instance.random
                    .nextInt(population.getTours().size()));

            if (!result.contains(select)) {
                selected.add(select);
                i++;
            }
        }

        for (int i = 0; i < selected.size(); i += 2) {
            if (selected.get(i).getFitness() < selected.get(i + 1).getFitness()) {
                result.add(selected.get(i));
            } else {
                result.add(selected.get(i + 1));
            }
        }

        return result;
    }

    public String toString() {
        return getClass().getSimpleName();
    }
}