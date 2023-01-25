/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.georgiasouthern.csci5332;

import java.io.*;
import java.net.*;
import java.net.Socket;
import java.net.ServerSocket;

/**
 *
 * @author EddyJ
 */
public class GameServer {

    private ServerSocket ss;
    private int numPlayers;
    private ServerSideConnection player1;
    private ServerSideConnection player2;
    private int turnsMade;
    private int maxTurns;
    private int[] values;
    private int player1ButtonNums;
    private int player2ButtonNums;

    public GameServer() {
        System.out.println("-----Game Server-----");
        numPlayers = 0;
        turnsMade = 0;
        maxTurns = 4;
        values = new int[4];

        for (int i = 0; i < values.length; i++) {
            values[i] = (int) Math.ceil(Math.random() * 100);
            System.out.println("Value #" + (i + 1) + " is " + values[i]);

        }
        try {
            ss = new ServerSocket(777);

        } catch (IOException ex) {
            System.out.println("IOExeption from GameServer Construct");

        }
    }

    public void acceptConnection() {
        try {
            System.out.println("Waiting for connection...");
            while (numPlayers < 2) {
                Socket s = ss.accept();
                numPlayers++;
                System.out.println("Player #" + numPlayers + " has connected.");
                ServerSideConnection ssc = new ServerSideConnection(s, numPlayers);
                if (numPlayers == 1) {
                    player1 = ssc;

                } else {
                    player2 = ssc;

                }
                Thread t = new Thread(ssc);
                t.start();
            }
            System.out.println("We now have 2 players. No longer accepting connections.");

        } catch (IOException ex) {
            System.out.println("IOException from acceptConnections()");
        }
    }

    private class ServerSideConnection implements Runnable {

        private Socket socket;
        private DataInputStream dataIn;
        private DataOutputStream dataOut;
        private int playerID;

        public ServerSideConnection(Socket s, int id) {
            socket = s;
            playerID = id;

            try {
                dataIn = new DataInputStream(socket.getInputStream());
                dataOut = new DataOutputStream(socket.getOutputStream());

            } catch (IOException ex) {
                System.out.println("IOException from run() SSC");
            }
        }

        public void run() {
            try {
                dataOut.writeInt(playerID);
                dataOut.writeInt(maxTurns);
                dataOut.writeInt(values[0]);
                dataOut.writeInt(values[1]);
                dataOut.writeInt(values[2]);
                dataOut.writeInt(values[3]);
                dataOut.flush();

                while (true) {
                    if (playerID == 1) {
                        player1ButtonNums = dataIn.readInt();
                        System.out.println("Player 1 clicked button #" + player1ButtonNums);
                        player2.sendButtonNum(player1ButtonNums);

                    } else {
                        player2ButtonNums = dataIn.readInt();
                        System.out.println("Player 2 clicked button #" + player2ButtonNums);
                        player1.sendButtonNum(player2ButtonNums);
                    }
                    turnsMade++;
                    if (turnsMade == maxTurns) {
                        System.out.println("Max turns has been reached.");
                        break;
                    }
                }
                player1.closeConnection();
                player2.closeConnection();
                
            } catch (IOException ex) {
                System.out.println("IOException from run() SSC");

            }
        }

        public void sendButtonNum(int n) {
            try {
                dataOut.writeInt(n);
                dataOut.flush();

            } catch (IOException ex) {
                System.out.println("IOException from sendButtonNum() CSC");
            }
        }

        public void closeConnection() {
            try {
                socket.close();
                System.out.println("Connection closed");

            } catch (IOException ex) {
                System.out.println("IOException on closeConnect() SSC");
            }
        }
    }
    public static void main(String[] args) {
        GameServer gs = new GameServer();
        gs.acceptConnection();
    }
}
