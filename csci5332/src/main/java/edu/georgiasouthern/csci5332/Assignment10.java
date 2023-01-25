/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.georgiasouthern.csci5332;

/**
 *
 * @author EddyJ
 */
public class Assignment10 {

    public static int[][] hadamard(int k) {
        int n = (int) Math.pow(2, k);
        int[][] hadamard = new int[n][n];
        hadamard[0][0] = 1;
        
        for(int i = 1; i < n; i += i){
            for(int j = 0; j < i; j++){
                for(int h = 0; h < i; h++){
                    hadamard[j + i][h] = hadamard[j][h];
                    hadamard[j][h + i] = hadamard[j][h];
                    hadamard[j + i][h + i] = -hadamard[j][h];
                }
            }
        }
        return hadamard;
    }

    public static void main(String[] args) {
        int[][] h = hadamard(3);
        for (int i = 0; i < h.length; i++) {
            for (int j = 0; j < h[i].length; j++) {
                System.out.printf("%3d", h[i][j]);
            }
            System.out.println();
        }
    }
}
