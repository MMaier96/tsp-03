package test.selection;

import java.util.Random;

public class PseudoRandom extends Random {
    private int[] intArr = {0};
    private double[] doubleArr = {0};

    private int intIndex = 0;
    private int doubleIndex = 0;

    public PseudoRandom(int[] intArr) {
        this.intArr = intArr;
    }

    public PseudoRandom(int[] intArr, double[] doubleArr) {
        this.intArr = intArr;
        this.doubleArr = doubleArr;
    }

    public PseudoRandom(double[] doubleArr) {
        this.doubleArr = doubleArr;
    }

    @Override
    public double nextDouble() {
        double val = doubleArr[doubleIndex];
        doubleIndex = (doubleIndex + 1) % doubleArr.length;
        return val;
    }

    @Override
    public int nextInt(int bound) {
        int val = intArr[intIndex];
        intIndex = (intIndex + 1) % intArr.length;
        return val;
    }
}
