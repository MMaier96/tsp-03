package crossover;

import base.City;
import base.Tour;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import random.MersenneTwisterFast;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PartiallyMatchedCrossover implements ICrossover{

    private  MersenneTwisterFast rand = new MersenneTwisterFast();

    public Tour doCrossover(Tour tour01, Tour tour02){

        int border1 = rand.nextInt(tour01.getSize()); //Grenzen der SubTouren mit MersenneTwister erstellen
        int border2 = rand.nextInt(tour02.getSize());

        if(border1 <= border2){
            return(Substring(border1, border2, tour01, tour02));
        }
        else if (border1 > border2){
            return(Substring(border2, border1, tour01, tour02));
        }
        else{return null;}
    }

    private Tour Substring(int left, int right, Tour tour01, Tour tour02){

        base.Tour sTour01 = new Tour();
        base.Tour sTour02 = new Tour();

        ArrayList<base.City> listTour01 = new ArrayList<>();
        ArrayList<base.City> listTour02 = new ArrayList<>();

        ArrayList<base.City> subListTour01 = new ArrayList<>();
        ArrayList<base.City> subListTour02 = new ArrayList<>();

        for (int i=0; i<tour01.getSize();i++){      //Touren in ArrayList laden
            listTour01.add(tour01.getCity(i));
            listTour02.add(tour02.getCity(i));
        }

        for (int i=left; i<=right; i++){            //SubTouren erstellen
            subListTour01.add(tour01.getCity(i));
            subListTour02.add(tour02.getCity(i));
        }

        for (int i=left; i<=right; i++){            //SubTouren aus den Touren entfernen
            listTour01.remove(left);
            listTour02.remove(left);
        }

        tour01.setCities(listTour01);               //TourObjekte mit den gekürzten ArrayListen beladen
        tour02.setCities(listTour02);

        sTour01.setCities(subListTour01);           //SubTourObjekte aus ArrayListen erstellen
        sTour02.setCities(subListTour02);

        City currentCity;
        City swapCity;
        int swapIndex;

        for (int i = 0; i < tour01.getSize(); i++){
            currentCity = tour01.getCity(i);
            if (sTour02.containsCity(currentCity)){
                swapIndex = subListTour02.indexOf(currentCity);
                swapCity = sTour01.getCity(swapIndex);
                while (sTour02.containsCity(swapCity)){
                    swapIndex = subListTour02.indexOf(swapCity);
                    swapCity = sTour01.getCity(swapIndex);
                }
                listTour01.set(i, swapCity);
            }
            currentCity = tour02.getCity(i);
            if (sTour01.containsCity(currentCity)){
                swapIndex = subListTour01.indexOf(currentCity);
                swapCity = sTour02.getCity(swapIndex);
                while (sTour01.containsCity(swapCity)){
                    swapIndex = subListTour01.indexOf(swapCity);
                    swapCity = sTour02.getCity(swapIndex);
                }
                listTour02.set(i, swapCity);
            }
            tour01.setCities(listTour01);               //in Tour Objekt wandeln für fitness
            tour02.setCities(listTour02);
        }

        for (int i=left; i<=right; i++) {
            int j = i-left;
            listTour01.add(i, subListTour02.get(j));
            listTour02.add(i, subListTour01.get(j));
        }

        tour01.setCities(listTour01);
        tour02.setCities(listTour02);

        int fit = tour01.compareTo(tour02);

        if (fit == -1) {
            return tour01;
        }
        else if (fit == 1) {
            return tour02;
        }
        else {
            int choose = rand.nextInt(2);
            switch (choose) {
                case 0:
                    return tour01;
                case 1:
                    return tour02;
            }
        }
        throw new IllegalArgumentException("Fehler in Crossover PMC");
    }

    public String toString() {
        return getClass().getSimpleName();
    }
}
