/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.georgiasouthern.csci5332;

import java.awt.*;
import java.awt.event.*;
import java.net.Socket;
import javax.swing.*;
import java.io.*;
import javax.swing.JTextArea;

/**
 *
 * @author EddyJ
 */

public class Player extends JFrame {

    private int width;
    private int height;
    private Container contentPane;
    private JTextArea message;
    private JButton b1;
    private JButton b2;
    private JButton b3;
    private JButton b4;
    private ClientSideConnection csc;
    private int playerID;
    private int otherPlayer;
    private int[] values;
    private int maxTurns;
    private int turnsMade;
    private int myPoints;
    private int enemyPoints;
    private boolean buttonsEnabled;

    public Player(int w, int h) {
        width = w;
        height = h;
        contentPane = this.getContentPane();
        message = new JTextArea();
        b1 = new JButton("1");
        b2 = new JButton("2");
        b3 = new JButton("3");
        b4 = new JButton("4");
        values = new int[4];
        turnsMade = 0;
        myPoints = 0;
        enemyPoints = 0;
    }

    public void setUpGUI() {
        this.setSize(width, height);
        this.setTitle("Turn-based Game");
        this.setTitle("Player #: " + playerID);
        contentPane.setLayout(new GridLayout(1, 5));
        contentPane.add(message);

        message.setWrapStyleWord(true);
        message.setLineWrap(true);
        message.setEditable(false);
        contentPane.add(b1);
        contentPane.add(b2);
        contentPane.add(b3);
        contentPane.add(b4);

        if (playerID == 1) {
            message.setText("You are player #1. You go first.");
            otherPlayer = 2;
            buttonsEnabled = true;
        } else {
            message.setText("You are player #2. Wait for your turn.");
            otherPlayer = 1;
            buttonsEnabled = false;
            
            Thread t = new Thread(new Runnable() {
                public void run(){
                    updateTurn();
                }
            });
            t.start();
        }
        toggleButtons();
        
        this.setVisible(true);

    }

    public void connectToServer() {
        csc = new ClientSideConnection();
    }

    public void setUpButtons() {
        ActionListener a1 = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JButton b = (JButton) ae.getSource();
                int bNum = Integer.parseInt(b.getText());

                message.setText("You clicked button #" + bNum + " Now wait for player #" + otherPlayer);
                turnsMade++;
                System.out.println("Turns made: " + turnsMade);

                buttonsEnabled = false;
                toggleButtons();
                
                myPoints += values[bNum - 1];
                System.out.println("My points: " + myPoints);
                csc.sendButtonNum(bNum);
                
                if(playerID == 2 && turnsMade == maxTurns){
                    checkWinner();
                } else {
                    
                Thread t = new Thread(new Runnable() {
                    public void run(){
                        updateTurn();
                    }
                });
                t.start();
                }
            }
        };
        b1.addActionListener(a1);
        b2.addActionListener(a1);
        b3.addActionListener(a1);
        b4.addActionListener(a1);
    }

    public void toggleButtons() {
        b1.setEnabled(buttonsEnabled);
        b2.setEnabled(buttonsEnabled);
        b3.setEnabled(buttonsEnabled);
        b4.setEnabled(buttonsEnabled);

    }
    public void updateTurn(){
        int n = csc.recieveButtonNum();
        message.setText("Your enemy clidked button #" + n + ". Your turn.");
        enemyPoints += values[n - 1];
        //System.out.println("Your enemy has " + enemyPoints + " points.");
        buttonsEnabled = true;
        if(playerID == 1 && turnsMade == maxTurns){
            checkWinner();
        } else {
            buttonsEnabled = true;            
        }
        toggleButtons();
    }
    private void checkWinner(){
        buttonsEnabled = false;
        if(myPoints > enemyPoints){
            message.setText("You WON!\n" + "YOU: " + myPoints + "\n" + "ENEMY: " + enemyPoints);
            csc.closeConnection();
            
        } else if (myPoints < enemyPoints){
            message.setText("You LOST!\n" + "YOU: " + myPoints + "\n" + "ENEMY: " + enemyPoints);
            csc.closeConnection();
        } else {
            message.setText("It's a tie! You both got " + myPoints + " points.");
            csc.closeConnection();
        }
    }
    private class ClientSideConnection {

        private Socket socket;
        private DataInputStream dataIn;
        private DataOutputStream dataOut;

        public ClientSideConnection() {
            System.out.println("---Client---");
            try {
                socket = new Socket("localhost", 777);
                dataIn = new DataInputStream(socket.getInputStream());
                dataOut = new DataOutputStream(socket.getOutputStream());
                playerID = dataIn.readInt();
                System.out.println("Connected to server as Player #" + playerID + ".");
                maxTurns = dataIn.readInt() / 2;
                values[0] = dataIn.readInt();
                values[1] = dataIn.readInt();
                values[2] = dataIn.readInt();
                values[3] = dataIn.readInt();
                System.out.println("maxTurns: " + maxTurns);
                System.out.println("Value # 1 is " + values[0]);
                System.out.println("Value # 2 is " + values[1]);
                System.out.println("Value # 3 is " + values[2]);
                System.out.println("Value # 4 is " + values[3]);

            } catch (IOException ex) { 
                System.out.println("IOException from CSC constructor");
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
        
        public int recieveButtonNum(){
            int n = -1;
            try {
                n = dataIn.readInt();
                System.out.println("Player #" + otherPlayer + " clicked butoton #" + n);
                
            }catch (IOException ex){
                System.out.println("IOException from receiveButtonNum() CSC");
            }
            return n;
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
        Player p = new Player(500, 100);
        p.connectToServer();
        p.setUpGUI();
        p.setUpButtons();
        
    }
}
