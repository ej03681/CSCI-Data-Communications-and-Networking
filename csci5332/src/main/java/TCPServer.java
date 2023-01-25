
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Date;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author EddyJ
 */
public class TCPServer {

    public static void main(String[] args) throws Exception {
        ServerSocket server = new ServerSocket(532);
        while (true) {
            Socket socket = server.accept();
            Handler handler = new Handler(socket);
            handler.start();
        }
    }
}

class Handler extends Thread {

    Socket socket;

    public Handler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {

            InputStream is = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String city = br.readLine();
            
            URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=6d25ad22e98c5d8496b49d9af505861b");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (conn != null) {
                System.out.println(conn.getRequestMethod());
                System.out.println(conn.getResponseCode());
                System.out.println(conn.getResponseMessage());
                System.out.println(conn.getHeaderField(0));

                InputStream in = conn.getInputStream();
                BufferedReader web = new BufferedReader(new InputStreamReader(in));

                OutputStream os = socket.getOutputStream();

                PrintWriter pw = new PrintWriter(os);
                Gson gson = new Gson();
                GeoInfo geoInfo = gson.fromJson(web, GeoInfo.class);

                pw.printf("Temperature: %3.1f (Fahrenheit)\n", geoInfo.main.temp);
                
            
                pw.flush();
                pw.close();
                br.close();
                socket.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

class GeoInfo {

    Coord coord;
    Weather[] weather;
    Main main;
    String name;
}

class Coord {

    double lon;
    double lat;
}

class Weather {

    int id;
    String main;
    String description;
}

class Main {

    double temp;
}
