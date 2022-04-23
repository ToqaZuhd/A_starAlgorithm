package com.example.A_starAlgo;

import java.util.Comparator;

//order in priority queue depends on the least value from the city to the goal (distance+weight+heuristic)
public class CityComparator implements Comparator<Branching> {
    @Override
    public int compare(Branching c1, Branching c2) {
        return (int) (c1.heuristic - c2.heuristic);
    }
}
