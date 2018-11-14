package base;

import java.util.ArrayList;

public class Population {
    private ArrayList<Tour> tours;

    public ArrayList<Tour> getTours() {
        return tours;
    }

    public Population() {
        this.tours = new ArrayList<>();
    }

    public Population addTour(Tour tour) {
        tours.add(tour);
        return this;
    }

    public void setTours(ArrayList<Tour> tours) {
        this.tours = tours;
    }

    public int getSize(){
        int size=0;
        for(Tour tour:tours){
            size++;
        }
        return size;
    }
}