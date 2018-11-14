package crossover;

import base.City;
import base.Tour;
import random.MersenneTwisterFast;

import java.util.ArrayList;

public class SubTourExchangeCrossover implements ICrossover {

    private MersenneTwisterFast rand = new MersenneTwisterFast();

    public Tour doCrossover(Tour tour01, Tour tour02) {

        if (tour01.getSize() != tour02.getSize())
            return null; //error!

        int left = rand.nextInt(tour01.getSize()-1);
        int selectionSize = rand.nextInt(Math.round(tour01.getSize()/35));

        return(Substring(left, selectionSize, tour01, tour02));
    }

    private Tour Substring(int left, int selectionSize, Tour tour01, Tour tour02){

        boolean found = false;
        int startValue02 = 0;
        int startLeft = left;

        while (!found) {

            ArrayList<City> selected01 = new ArrayList<>();
            ArrayList<City> selected02 = new ArrayList<>();

            for (int i=0; i<=selectionSize; i++)
            {
                int selectedIndex;
                if(left+i>=tour01.getSize()) selectedIndex = left+i-tour01.getSize();
                else selectedIndex = left+i;

                selected01.add(tour01.getCity(selectedIndex));

                if(startValue02+i>=tour01.getSize()) selectedIndex = startValue02+i-tour01.getSize();
                else selectedIndex = startValue02+i;

                selected02.add(tour02.getCity(selectedIndex));
            }

            Tour tourSelected01 = new Tour();
            Tour tourSelected02 = new Tour();

            tourSelected01.setCities(selected01);
            tourSelected02.setCities(selected02);

            found = isInTour02(selectionSize, tourSelected01, tourSelected02);

            if (!found)
            {
                startValue02++;
                if (startValue02 == tour01.getSize())
                {
                    left++;
                    startValue02 = 0;
                    if (left >= tour01.getSize()){ left = 0;}
                    if (left == startLeft)
                    {
                        selectionSize--;
                    }
                    if (selectionSize<1)
                    {
                        return theFitter(tour01, tour02);
                    }
                }
            }
        }

        ArrayList<City> listTour01 = new ArrayList<>(tour01.getCities());
        ArrayList<City> listTour02 = new ArrayList<>(tour02.getCities());
        ArrayList<City> cacheList = new ArrayList<>();
        int position, position02;

        for (int i=0; i<=selectionSize; i++)
        {
            if (i+left >= tour01.getSize()) position = i + left - tour01.getSize();
            else position = i + left;
            if (i+startValue02 >= tour02.getSize()) position02 = i + startValue02 - tour02.getSize();
            else position02 = i + startValue02;

            cacheList.add(listTour01.get(position));
            listTour01.remove(position);
            listTour01.add(position, listTour02.get(position02));
        }

        for (int i=startValue02; i<=selectionSize; i++)
        {
            if (i+startValue02 >= tour01.getSize()) position = i + startValue02 - tour01.getSize();
            else position = i + startValue02;

            listTour02.remove(position);
            listTour02.add(position, cacheList.get(i-startValue02));
        }

        tour01.setCities(listTour01);
        tour02.setCities(listTour02);

        return theFitter(tour01, tour02);
    }

    private boolean isInTour02(int selectionSize, Tour tourSelected01, Tour tourSelected02){

        for (int i=0; i<=selectionSize; i++)
        {
            if (!(tourSelected02.containsCity(tourSelected01.getCity(i))))
            {
                return false;
            }
        }
        return true;
    }

    private Tour theFitter(Tour tour01, Tour tour02)
    {
        int fit = tour01.compareTo(tour02);

        if (fit == 1)
        {
            return tour02;
        }
        else if (fit == -1)
        {
            return tour01;
        }
        else
        {
            int choose = rand.nextInt(2);
            switch (choose) {
                case 0:
                    return tour01;
                case 1:
                    return tour02;
            }
        }

        throw new IllegalArgumentException("Fehler in Crossover STEC");
    }

    public String toString() {
        return getClass().getSimpleName();
    }
}