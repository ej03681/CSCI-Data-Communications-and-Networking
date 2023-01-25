/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.georgiasouthern.csci5332;

/**
 *
 * @author EddyJ
 */
public class Assignment8 {

    public static final double INF = Double.POSITIVE_INFINITY;

    // Dijkstra’s Algorithm
    public static int min(double dist[], Boolean b[]){
        double min = INF;
        int index = -1;
        for(int i = 0; i < dist.length; i++){
            if (b[i] == false && dist[i] <= min){
                min = dist[i];
                index = i;
            }
        }
        return index ;
    }
    public static double[] dijkstra(double[][] c, int u) {
        int n = c.length;
        double dist[] = new double[n];
        Boolean[] b = new Boolean[n];
        
        for(int i = 0; i < n; i++){
            dist[i] = INF;
            b[i] = false;
            
        }
        dist[u] = 0;
      
        for(int i = 0; i < n; i++){
           int s = min(dist, b);
           b[s] = true;
            for(int j = 0; j < n; j++){
                if(!b[j] && c[s][j] != 0 && dist[s] != INF && dist[s] + c[s][j] < dist[j]){
                    dist[j] = dist[s] + c[s][j];
                }
            }
        }
        
        return dist;
    }

    // a test program
    public static void main(String[] args) {

        double[][] c
                = {{0, 2, INF, 4},
                {2, 0, 1, 5},
                {INF, 1, 0, INF},
                {4, 5, INF, 0}};

        double[] d;
        d = dijkstra(c, 1);

        for (int i = 0; i < d.length; i++) {
            System.out.println(d[i]);
        }
    }
}
