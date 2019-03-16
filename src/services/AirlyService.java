package services;

import patterns.Data;
import patterns.Index;
import patterns.Sensor;
import patterns.Station;

import java.io.IOException;
import java.util.List;

public class AirlyService implements IService{

    @Override
    public List<Station> manageStations() {

        return null;
    }

    @Override
    public Sensor manageSensors(int stationID) {

        return null;
    }

    @Override
    public Data manageData(int sensorID) {
        return null;
    }

    @Override
    public Index manageIndex(int StationID) {
        return null;
    }

    @Override
    public String loadRecordsFromService(String type, int id) throws IOException {
        return null;
    }
}
