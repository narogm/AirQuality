package patterns;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Index implements Serializable {
    private int stationID;
    private HashMap<String, String> indexValues;

    public int getStationID() {
        return stationID;
    }

    public void setStationID(int stationID) {
        this.stationID = stationID;
    }

    public Index(){
        indexValues = new HashMap<>();
    }

    public void add(String pollution, String value){
        indexValues.put(pollution,value);
    }

    @Override
    public String toString(){
        StringBuilder result = new StringBuilder();
        for(Map.Entry<String,String> entry : indexValues.entrySet()){
            result.append(entry.getKey()).append(" -> ").append(entry.getValue()).append("\n");
        }
        return result.toString();
    }

    public boolean equals(Index i){
        if(i.stationID != this.stationID)
            return false;
        return i.indexValues.equals(this.indexValues);
    }
}
