package i5b5.daniel.serszen.pz.controller;

import com.google.gson.Gson;
import i5b5.daniel.serszen.pz.controller.dto.TimeApiResponse;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.parser.JSONParser;
import jdk.nashorn.internal.runtime.Context;
import org.springframework.stereotype.Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

@Controller
public class WebController {
    public String getTimeAsString() throws IOException {
        URL url = new URL("http://api.timezonedb.com/v2/get-time-zone?key=0WUTEYPRFXTD&format=json&by=zone&zone=Europe/Warsaw");
        URLConnection connection = url.openConnection();

        BufferedReader in = new BufferedReader(new InputStreamReader(
                connection.getInputStream()));
        String inputLine;
        inputLine = in.readLine();
        in.close();

        Gson gson = new Gson();
        TimeApiResponse timeApiResponse = gson.fromJson(inputLine, TimeApiResponse.class);

        return timeApiResponse.getFormatted().substring(11,19);
    }
}
