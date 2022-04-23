package com.example.A_starAlgo;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;


public class A_star {

    HashMap<String, Branching> branchingHashMap = new HashMap<>();
    HashMap<String, City> cityHashMap = new HashMap<>();

    Branching[] branching;
    City[] city;
    PriorityQueue<Branching> pq;
    int counterSpaceComplexity=0;
    int counterTimeComplexity=0;


    public A_star(ArrayList<City> city, City source) {

        this.city = new City[city.size()];
        for (int i=0; i< city.size(); i++)
            this.city[i] = city.get(i);

        this.branching = new Branching[city.size()];
        this.pq = new PriorityQueue<>(city.size(), new CityComparator() {
        });
        initializeTableOfRoads(source);

    }

    //initialize the table of roads to calculate the distance and path through
    public void initializeTableOfRoads(City source) {

        for (int i = 0; i < branching.length; i++) {
            branching[i] = new Branching();
        }


        for (int i = 0; i < city.length; i++) {
            branching[i].branchName = city[i].cityName;
            branching[i].distance = Double.MAX_VALUE;
            branching[i].heuristic =Double.MAX_VALUE;
            branching[i].state = false;
            branching[i].previous = null;
        }

        for (int j = 0; j < city.length; j++) {
            branchingHashMap.put(city[j].cityName, branching[j]);
        }


        for (City value : city) {
            cityHashMap.put(value.cityName, value);
        }

        Branching temp = branchingHashMap.get(source.cityName);
        temp.distance = 0;
        pq.add(temp);
        counterSpaceComplexity++;

    }

    //Method for finding the shortest path from source to the goal
    public void A_Star_Search(City dest) {

        Branching d= branchingHashMap.get(dest.cityName);

        //stop when the queue is empty
        while(!pq.isEmpty()) {

            Branching v = pq.poll();

            //if the city is not visited yet -doesn't find the shortest path- for it
            if(!v.state) {
                v.state = true;//donates it as visited because is will be the shortest path

                if(d.state)
                    break;

                City branch = cityHashMap.get(v.branchName);
                //take the coordinates for the destination
                double x2 = dest.X;
                double y2 = dest.Y;

                //take the branches from current city
                for (int j=0; j<branch.branches.size(); j++){
                    Branching node = branchingHashMap.get(branch.branches.get(j).branchName);
                    double weight = branch.branches.get(j).weight;//the distance between the city and the current branch

                    City c = cityHashMap.get(node.branchName);
                    //take the coordinates for the current city
                    double x1 = c.X;
                    double y1 = c.Y;

                    /*
                     if is not visited yet will calculate the distance
                     from the source to the current city in addition to the
                     distance-weight- from the current city to the current road also with
                     the heuristic value from the current road to the goal then will put it in the
                     queue to compare it with other cities
                     */
                    if (!node.state) {
                        double heuristic= Math.sqrt(Math.pow(x2-x1,2)+Math.pow(y2-y1,2));

                        if ((v.distance + weight +heuristic)
                                < node.heuristic) {
                            node.distance = v.distance + weight ;
                            node.heuristic = v.distance + weight + heuristic;
                            node.previous = v;
                            pq.add(node);
                            counterSpaceComplexity++;
                            counterTimeComplexity++;
                        }
                    }


                }

            }

        }

    }

    String S="";
    double distance;
    //track the path via recursive function
    public void printPath(Branching dest) {
        if(branchingHashMap.get(dest.branchName).previous!=null) {
            printPath(branchingHashMap.get(dest.branchName).previous);
            S+=("\n");
        }
        distance = branchingHashMap.get(dest.branchName).distance;
        S+=dest.branchName;

    }


    public Branching convert(City destination) {
        return branchingHashMap.get(destination.cityName);
    }
}
