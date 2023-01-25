
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author EddyJ
 */
public class TCPClient {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost", 5332);
        PrintWriter pw = new PrintWriter(socket.getOutputStream());
        pw.println("Savannah");
        pw.flush();
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String line = br.readLine();
        System.out.println(line);
        br.close();
        pw.close();
        socket.close();
    }
}
