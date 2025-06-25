package org.example.predictechmq.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.predictechmq.model.Reading;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ApiCommunication {
    public void addMeasurements(Reading reading) throws IOException {
        String url = "https://localhost:8081/api/readings/";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(reading);
        System.out.println(json);

        try (OutputStream os = con.getOutputStream()) {
            os.write(json.getBytes());
            os.flush();
        }

        int responseCode = con.getResponseCode();
        if (responseCode >= 200 && responseCode < 300) {
            System.out.println("successful: " + responseCode);
        } else {
            try (InputStream errorStream = con.getErrorStream()) {
                if (errorStream != null) {
                    String errorMsg = new String(errorStream.readAllBytes());
                    System.err.println("failed: " + responseCode + " - " + errorMsg);
                } else {
                    System.err.println("failed: " + responseCode);
                }
            }
        }
    }
}
