package main;

import patterns.Data;
import patterns.Index;
import patterns.Sensor;
import patterns.Station;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * All available functionalities
 */
public class Results {

    private List<Station> stations;
    private Cache cache;

    public Results(String serviceName) throws IOException, ClassNotFoundException {
        cache = new Cache(serviceName);
        stations = (List<Station>) cache.read("stations",0);
    }

    public Results(String serviceName,Cache cache) throws IOException, ClassNotFoundException {
        this.cache = cache;
        stations = (List<Station>) this.cache.read("stations",0);
    }

    /**
     * Method that show index for given station name
     * @param stationName given station name
     * @throws IOException
     * @throws ClassNotFoundException
     */
    //1
    public String getIndexForStation(String stationName) throws IOException, ClassNotFoundException {
        int stationID = getStationID(stationName);
        Index index = (Index) cache.read("index",stationID);
        return "Index for " + stationName + ":\n" + index;
    }

    /**
     * Method that show value of given pollution type for given station
     * @param date given date
     * @param stationName given station name
     * @param pollutionName given pollution type
     * @throws IOException
     * @throws ClassNotFoundException
     */
    //2
    public String getValueOfPollutionForStation(String date, String stationName, String pollutionName) throws IOException, ClassNotFoundException {
        int stationID = getStationID(stationName);
        Sensor sensor = (Sensor) cache.read("sensor",stationID);
        int sensorID = sensor.getSensorIdByPollutionName(pollutionName);
        Data data = (Data) cache.read("data",sensorID);
        float value = data.getValueForDate(date);
        return "Value for given date of " + pollutionName + " was " + value;
    }

    /**
     * Method that show average value of given pollution type for given station in given period
     * @param startDate start of the given period
     * @param endDate end of the given period
     * @param stationName given station name
     * @param pollutionName given pollution type
     * @throws IOException
     * @throws ParseException
     * @throws ClassNotFoundException
     */
    //3
    public float getAverageOfPollutionForStation(String startDate, String endDate, String stationName, String pollutionName) throws IOException, ParseException, ClassNotFoundException {
        int stationID = getStationID(stationName);
        Sensor sensor = (Sensor) cache.read("sensor",stationID);
        int sensorID = sensor.getSensorIdByPollutionName(pollutionName);
        Data data = (Data) cache.read("data",sensorID);
        return data.getAverageInPeriod(startDate,endDate);
    }

    /**
     * Method that show maximal fluctuation for given station starting from given date
     * @param startDate given start date
     * @param stationName given station name
     * @throws IOException
     * @throws ParseException
     * @throws ClassNotFoundException
     */
    //4
    public String getMaximalFluctuationForStation(String startDate, String stationName) throws IOException, ParseException, ClassNotFoundException {
        int stationID = getStationID(stationName);
        Sensor sensor = (Sensor) cache.read("sensor",stationID);
        HashMap<String,Integer> pollutions = sensor.getPollutionTypes();
        float max = 0;
        String pollutionType = null;
        for(Map.Entry<String,Integer> entry : pollutions.entrySet()){
            Data data = (Data) cache.read("data",entry.getValue());
            float value = data.getMaximalFluctuation(startDate);
            if(max < value){
                max = value;
                pollutionType = entry.getKey();
            }
        }
        return "Maximal fluctuation:\n" + pollutionType + " -> " + max;
    }

    /**
     * Method that show minimal value of pollution types for given station at given date
     * @param date given date
     * @param stationName given station name
     * @throws IOException
     * @throws ClassNotFoundException
     */
    //5
    public String getMinimalValue(String date, String stationName) throws IOException, ClassNotFoundException {
        int stationID = getStationID(stationName);
        Sensor sensor = (Sensor) cache.read("sensor",stationID);
        HashMap<String,Integer> pollutions = sensor.getPollutionTypes();
        float minimal = Float.MAX_VALUE;
        String pollutionType = null;
        for(Map.Entry<String,Integer> entry : pollutions.entrySet()){
            Data data = (Data) cache.read("data",entry.getValue());
            float value = data.getValueForDate(date);
            if(value < minimal){
                minimal = value;
                pollutionType = entry.getKey();
            }
        }
        return "Minimal pollution value:\n" + pollutionType + " " + minimal;
    }

    /**
     * Method that show N sensors with pollution standards exceedance for given station
     * @param stationName given station name
     * @param amount given amount - N
     * @param date given date
     * @throws IOException
     * @throws ClassNotFoundException
     */
    //6
    public String getSensorsWithPollutionStandardsExceedance(String stationName, int amount, String date) throws IOException, ClassNotFoundException {
        HashMap<String,Float> pollutionStandards = new HashMap<>();
        pollutionStandards.put("C6H6", (float) 5);
        pollutionStandards.put("NO2",(float)200);
        pollutionStandards.put("SO2",(float)350);
        pollutionStandards.put("CO",(float)10000);
        pollutionStandards.put("PM10",(float)50);
        pollutionStandards.put("PM2.5",(float)25);
        //pollutionStandards.put("Pb",(float)0.5);

        int stationID = getStationID(stationName);
        Sensor sensor = (Sensor) cache.read("sensor",stationID);
        HashMap<String,Integer> stationPollutions = sensor.getPollutionTypes();
        HashMap<Float,String> exceededPollutions = new HashMap<>();
        for(Map.Entry<String,Integer> entry : stationPollutions.entrySet()){
            Data data = (Data) cache.read("data",entry.getValue());
            float value = data.getValueForDate(date);
            String type = entry.getKey();
            if(pollutionStandards.get(type) != null && value > pollutionStandards.get(type)){
                exceededPollutions.put(value-pollutionStandards.get(type),type);
            }
        }
        if(exceededPollutions.isEmpty()){
            return "There aren't any exceeded pollution types";
        }
        List<Float> sortedKeys = exceededPollutions.keySet().stream().sorted().collect(Collectors.toList());
        Collections.reverse(sortedKeys);

        StringBuilder result = new StringBuilder();

        result.append("Exceeded pollution types:");
        for(int i=0;i<amount;i++){
            float value = sortedKeys.get(i);
            result.append(exceededPollutions.get(value) + " with difference: " + value);
        }
        return result.toString();
    }

    /**
     * Method that show minimal and maximal values of given pollution type, as also their place and time
     * @param pollution given pollution type
     * @throws IOException
     * @throws ClassNotFoundException
     */
    //7
    public String getMaximalAndMinimalValueForPollution(String pollution) throws IOException, ClassNotFoundException {
        String maxStation = null;
        String maxDate = null;
        String minStation = null;
        String minDate = null;
        float max = 0;
        float min = Float.MAX_VALUE;
        for(Station station : stations){
                Sensor sensor = (Sensor) cache.read("sensor",station.getId());
                int sensorID = sensor.getSensorIdByPollutionName(pollution);

                Data data = (Data) cache.read("data",sensorID);
                LinkedHashMap<String,Float> values = data.getValues();
                for(Map.Entry<String,Float> entry : values.entrySet()){
                    if(entry.getValue() > max){
                        max = entry.getValue();
                        maxStation = station.getName();
                        maxDate = entry.getKey();
                    }
                    if(entry.getValue() < min){
                        min = entry.getValue();
                        minStation = station.getName();
                        minDate = entry.getKey();
                    }
                }
        }
        StringBuilder result = new StringBuilder();
        result.append("Maximal value:\n" + max + " -> " + maxStation + " at " + maxDate);
        result.append("---------------------");
        result.append("Minimal value:\n" + min+ " -> " + minStation + " at " + minDate);
        return result.toString();
    }

    /**
     * Method that show graph of changes in value of given pollution for given stations in given period
     * @param pollution given pollution type
     * @param stationsName given stations name
     * @param startDate start of the given period
     * @param endDate end of the given period
     * @throws IOException
     * @throws ParseException
     * @throws ClassNotFoundException
     */
    //8
    public String getGraph(String pollution, List<String> stationsName, String startDate, String endDate) throws IOException, ParseException, ClassNotFoundException {
        HashMap<String,Float> results = new HashMap<>();
        for(String stationName : stationsName){
            int stationID = getStationID(stationName);
            Sensor sensor = (Sensor) cache.read("sensor",stationID);
            Data data = (Data) cache.read("data",sensor.getSensorIdByPollutionName(pollution));
            HashMap<String,Float> tmpMap = data.getValuesInPeriod(startDate,endDate);
            for(Map.Entry<String,Float> entry : tmpMap.entrySet()){
                results.put(stationName + " " + entry.getKey(),entry.getValue());
            }
        }
        StringBuilder str = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDate = startDate;
        int cellSize = 30;
        while(sdf.parse(endDate).compareTo(sdf.parse(currentDate)) >= 0){
            for(String stationName : stationsName){
                String s = stationName + " " + currentDate;
                str.append(setCell(stationName,cellSize)).append("  ");
                str.append(currentDate).append(" ");
                float tmp = results.get(s);
                int value = (int) tmp;
                for(int i=0;i<value;i++){
                    str.append("*");
                }
                str.append(" ").append(tmp).append("\n");
            }
            currentDate = getNextDate(currentDate);
        }
        return str.toString();
    }

    /*
    private functions
     */

    private int getStationID(String stationName){
        for(Station station : stations){
            if(station.getName().equals(stationName))
                return station.getId();
        }
        //throw new exception
        return Integer.parseInt(null);
    }

    private String getNextDate(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sdf.parse(date));
        calendar.add(Calendar.HOUR_OF_DAY,1);
        return sdf.format(calendar.getTime());
    }

    private String setCell(String s,int len){
        return String.format("%1$" + (-len) + "s",s);
    }
}
