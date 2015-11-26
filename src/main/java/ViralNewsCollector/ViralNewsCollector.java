package ViralNewsCollector;

import ViralNewsCollector.viralsources.BuzzFeedBuzz;
import ViralNewsCollector.viralsources.ViralHours;

import java.util.ArrayList;

/**
 * Created by apatterson on 19/11/2015.
 */
public class ViralNewsCollector {

    ArrayList<Thread> viralCrawlers = new ArrayList<Thread>();


    public ViralNewsCollector(){
        //viralCrawlers
        viralCrawlers.add(new Thread(new BuzzFeedBuzz()));
        viralCrawlers.add(new Thread(new ViralHours()));
    }
    public void start(){
        for(Thread t: viralCrawlers)
            t.start();
    }
    public static void main(String[] args){

        ViralNewsCollector vnc = new ViralNewsCollector();
        vnc.start();
    }
}
