package ru.stwtforever.schedule.util;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

public class JSONParser {

    public static JSONObject parse(String url) {
        BufferedReader reader;
        HttpURLConnection connection;
        StringBuilder builder;
        JSONObject result = null;
        try {
            connection = (HttpURLConnection) new java.net.URL(url).openConnection();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            builder = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            connection.disconnect();
            reader.close();


            result = new JSONObject(builder.toString().trim());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}


