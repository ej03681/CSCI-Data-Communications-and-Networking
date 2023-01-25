/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.georgiasouthern.csci5332;

/**
 *
 * @author EddyJ
 */
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Assignment4 {

    public static void main(String[] args) {
        String city = "Savannah";
        try {
//            URL url = new URL("https://www.georgiasouthern.edu");
            
            URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=6d25ad22e98c5d8496b49d9af505861b&units=metric");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (conn != null) {
                System.out.println(conn.getRequestMethod());
                System.out.println(conn.getResponseCode());
                System.out.println(conn.getResponseMessage());
                System.out.println(conn.getHeaderField(0));

                InputStream in = conn.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                Gson gson = new Gson();
                GeoInfo geoInfo = gson.fromJson(br, GeoInfo.class);
                
                
                System.out.println("name: " + geoInfo.name);
                System.out.println("lon:" + geoInfo.coord.lon);
                System.out.println("lat:" + geoInfo.coord.lat);
                System.out.println("main:" + geoInfo.weather[0].main);
                System.out.printf("Temperature: %3.1f (Fahrenheit)\n", geoInfo.main.temp);
                
                
                br.close();
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