
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author EddyJ
 */
public class UDPServer {
    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket(5332);
        byte[] buf = new byte[256];
        
        DatagramPacket packet = new DatagramPacket(buf, buf.length);        
        while(true){
            socket.receive(packet);
            InetAddress ip = packet.getAddress();
            int port = packet.getPort();
            System.out.println("ip addr: " + ip);
            System.out.println("port: " + port);
            String msg = new String(buf, StandardCharsets.UTF_8);
            System.out.println("mag: " + msg);
            byte[] sbuf = ("Server: " + msg).getBytes();
            DatagramPacket spacket = new DatagramPacket(sbuf, sbuf.length, ip, port);
            socket.send(spacket);
                    
        }
    }
}
