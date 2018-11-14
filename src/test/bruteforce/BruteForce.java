package test.bruteforce;

import base.City;
import base.Tour;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class BruteForce {
    private bruteforce.BruteForce bruteForce;

    @Before
    public void init(){
        bruteForce=new bruteforce.BruteForce();
    }
    @Test
    public void createPermutationsEvaluateNotNull(){
        assertNotNull(bruteForce.createPermutationEvaluate(100));
    }
}
