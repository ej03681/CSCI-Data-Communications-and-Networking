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

/**
 *
 * @author hzhang
 */
public class HttpDemo {
    public static void main(String[] args) {
        try {
//            URL url = new URL("https://www.georgiasouthern.edu");
            URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=Savannah&APPID=aaf83e46bfe2cab5a0b75f411d58915a&u&units=metric");
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            if (conn != null) {
                System.out.println(conn.getRequestMethod());
                System.out.println(conn.getResponseCode());
                System.out.println(conn.getResponseMessage());
                System.out.println(conn.getHeaderField(0));

                InputStream in = conn.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                
                String line;
                //while ((line=br.readLine()) != null) {
                  //  System.out.println(line);
                //}
                
                Gson gson = new Gson();
                OpenWeather openWeather = gson.fromJson(br, OpenWeather.class);
                
                System.out.println("name: " + openWeather.name);
                System.out.println("lon:" + openWeather.coord.lon);
                System.out.println("lat:" + openWeather.coord.lat);
                System.out.println("main:" + openWeather.weather[0].main);
                System.out.printf("Temperature: %3.1f (Fahrenheit)\n", openWeather.main.temp);
                
                


                
                br.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

class OpenWeather{
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