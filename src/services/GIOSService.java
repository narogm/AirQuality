package services;

import patterns.Data;
import patterns.Index;
import patterns.Sensor;
import patterns.Station;

import javax.json.Json;
import javax.json.stream.JsonParser;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GIOSService implements IService {

    /**
     * {@inheritDoc}
     * @return
     * @throws IOException
     */
    @Override
    public List<Station> manageStations() throws IOException {

        //String stations = loadRecords("test.txt");
        String stations = loadRecordsFromService("stations",0);
        stations = stations.replaceAll("\\[","").replaceAll("]","");
        stations = stations.replaceAll("},\\{","}|{");
        String[] splittedStations = stations.split("\\|");

        List<Station> list = new ArrayList<>();
        for(String string : splittedStations){
            JsonParser parser = Json.createParser(new StringReader(string));
            Station station = new Station();
            for(int i = 0; i<5; i++){
                JsonParser.Event event = parser.next();
                switch(event){
                    case VALUE_NUMBER:
                        station.setId(parser.getInt());
                        break;
                    case VALUE_STRING:
                        station.setName(parser.getString());
                        break;
                }
            }
            parser.close();
            list.add(station);
        }
        return list;
    }

    /**
     * {@inheritDoc}
     * @param stationID
     * @return
     * @throws IOException
     */
    @Override
    public Sensor manageSensors(int stationID) throws IOException {
        //String records = loadRecords("sensor_" + stationID + ".txt");
        String records = loadRecordsFromService("sensor",stationID);
        Sensor sensor = new Sensor(stationID);

        JsonParser parser = Json.createParser(new StringReader(records));
        String key = null;
        int number = 0;
        boolean id = false;
        boolean pollutionType = false;
        while(parser.hasNext()){
            JsonParser.Event event = parser.next();
            switch(event){
                case KEY_NAME:
                    if(parser.getString().equals("id"))
                        id = true;
                    if(parser.getString().equals("paramCode"))
                        pollutionType = true;
                    break;
                case VALUE_NUMBER:
                    if(id){
                        number=parser.getInt();
                        id = false;
                    }
                    break;
                case VALUE_STRING:
                    if(pollutionType){
                        sensor.add(parser.getString(),number);
                        pollutionType = false;
                    }
                    break;
            }
        }
        parser.close();

        return sensor;
    }

    /**
     * {@inheritDoc}
     * @param sensorID given sensor id
     * @return
     * @throws IOException
     */
    @Override
    public Data manageData(int sensorID) throws IOException {
        //String values = loadRecords("data_" + sensorID + ".txt");
        String values = loadRecordsFromService("data",sensorID);

        Data data = new Data(sensorID);

        JsonParser parser = Json.createParser(new StringReader(values));
        String date = null;
        boolean isDate = false;
        while(parser.hasNext()){
            JsonParser.Event event = parser.next();
            switch (event){
                case KEY_NAME:
                    if(parser.getString().equals("date"))
                        isDate = true;
                    break;
                case VALUE_STRING:
                    if(isDate){
                        date = parser.getString();
                        isDate=false;
                    }
                    break;

                case VALUE_NUMBER:
                    data.add(date,parser.getBigDecimal().floatValue());
                    break;

            }
        }
        return data;
    }

    /**
     * {@inheritDoc}
     * @param stationID given station id
     * @return
     * @throws IOException
     */
    @Override
    public Index manageIndex(int stationID) throws IOException {
        //String values = loadRecords("index_" + stationID + ".txt");
        String values = loadRecordsFromService("index",stationID);


        Index index = new Index();

        JsonParser parser = Json.createParser(new StringReader(values));
        String key = null;
        boolean value = false;
        while (parser.hasNext()) {
            JsonParser.Event event = parser.next();
            switch (event) {
                case KEY_NAME:
                    if(parser.getString().contains("IndexLevel"))
                        key = parser.getString();
                    if(parser.getString().contains("LevelName"))
                        value = true;
                    break;
                case VALUE_STRING:
                    if(value){
                        index.add(key,parser.getString());
                        value=false;
                    }
                    break;
            }
        }
        parser.close();
        return index;
    }

    /*
    public String loadRecords (String file_name) throws IOException {

        File file = new File(file_name);
        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;
        StringBuilder result = new StringBuilder();

        while((st = br.readLine()) != null)
            result.append(st).append("\n");

        return result.toString();
    }

     */

    /**
     * {@inheritDoc}
     * @param type type of records to download
     * @param id   id of station or sensor
     * @return
     * @throws IOException
     */
    public String loadRecordsFromService (String type,int id) throws IOException {

        String path = "http://api.gios.gov.pl/pjp-api/rest/";
        switch (type){
            case "stations":
                path += "station/findAll";
                break;
            case "sensor":
                path += "station/sensors/" + id;
                break;
            case "data":
                path += "data/getData/" + id;
                break;
            case "index":
                path += "aqindex/getIndex/" + id;
                break;
        }
        StringBuilder result = new StringBuilder();

        URL url = new URL(path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        if(connection != null){
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String input;
            while((input = br.readLine()) != null){
                result.append(input).append("\n");
            }
        }
        return result.toString();
    }
}
