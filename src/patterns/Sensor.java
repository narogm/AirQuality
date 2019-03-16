package patterns;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Sensor implements Serializable {
    private int stationID;
    private HashMap<String,Integer> pollutionTypes;

    public HashMap<String, Integer> getPollutionTypes() {
        return pollutionTypes;
    }

    public Sensor(int stationID) {
        this.stationID = stationID;
        pollutionTypes = new HashMap<>();
    }

    public void add(String type,int sensorID){
        pollutionTypes.put(type,sensorID);
    }

    public int getSensorIdByPollutionName(String pollutionName){
        // throw exeption ?
        return pollutionTypes.get(pollutionName);
    }


    @Override
    public String toString(){
        StringBuilder result = new StringBuilder();
        for(Map.Entry<String,Integer> entry : pollutionTypes.entrySet()){
            result.append(entry.getKey()).append(" -> ").append(entry.getValue()).append("\n");
        }
        return result.toString();
    }

    public boolean equals(Sensor s){
        if(s.stationID != this.stationID)
            return false;
        return s.pollutionTypes.equals(this.pollutionTypes);
    }
}
