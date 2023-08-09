package com.example.couphoneserver.utils;

import com.example.couphoneserver.common.datatype.Coordinate;
import com.example.couphoneserver.common.exception.StoreException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static com.example.couphoneserver.common.response.status.BaseExceptionResponseStatus.COORDINATE_NOT_FOUND;

@Slf4j
@Component
@RequiredArgsConstructor
public class CoordinateConverter {
    //실제 환경에서는 실행이 되는데, 테스트 환경에서는 값이 주입되지 않는 상황
    private String apikey = "F224F68F-0102-3D4D-9467-84B239E77413";

    private String searchType = "road";

    private static final String GETCOORD = "epsg:5181";
    private static final String GETADDRESS = "epsg:4326";
    /*
    도로명 주소 필수!
     */
    public Coordinate getCoordinate(String address){

        String searchAddr = address;

        StringBuilder sb = getURL("getcoord",searchAddr);

        BufferedReader reader;

        try {
            URL url = new URL(sb.toString());
            reader = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));

            JSONParser jspa = new JSONParser();
            JSONObject jsob = (JSONObject) jspa.parse(reader);
            JSONObject jsrs = (JSONObject) jsob.get("response");

            JSONObject jsResult = (JSONObject) jsrs.get("result");
            JSONObject jspoitn = (JSONObject) jsResult.get("point");

            String x = jspoitn.get("x").toString();
            String y = jspoitn.get("y").toString();

            return new Coordinate(Double.parseDouble(x),Double.parseDouble(y));
        } catch (NullPointerException | ParseException | IOException e) {
            throw new StoreException(COORDINATE_NOT_FOUND);
        }
    }


    public String getAddress(Double x, Double y){

        String coordinate = x.toString()+","+y.toString();

        StringBuilder sb = getURL("getAddress",coordinate);

        BufferedReader reader;

        try {
            URL url = new URL(sb.toString());
            reader = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));

            JSONParser jspa = new JSONParser();
            JSONObject jsob = (JSONObject) jspa.parse(reader);
            JSONObject jsrs = (JSONObject) jsob.get("response");

            JSONArray jsonArray = (JSONArray) jsrs.get("result");
            JSONObject jsonfor = new JSONObject();

            String address = "";

            for (int i = 0; i< jsonArray.size(); i++){
                jsonfor = (JSONObject) jsonArray.get(i);
                address = (String) jsonfor.get("text");
                log.info(address);
            }

            return address;
        } catch (NullPointerException | ParseException | IOException e) {
            throw new StoreException(COORDINATE_NOT_FOUND);
        }
    }

    private StringBuilder getURL(String request, String query) {
        StringBuilder sb = new StringBuilder("https://api.vworld.kr/req/address");
        sb.append("?service=address");
        sb.append("&request="+request);
        sb.append("&format=json");
        sb.append("&key=" + apikey);
        sb.append("&type=" + searchType);
        sb.append("&simple=true");
        if(request.equals("getcoord")){
            sb.append("&crs=" + GETCOORD);
            sb.append("&address=" + URLEncoder.encode(query, StandardCharsets.UTF_8));
            return sb;
        }
        sb.append("&crs=" + GETADDRESS);
        sb.append("&point="+URLEncoder.encode(query, StandardCharsets.UTF_8));
        return sb;
    }
}
