package services;

import patterns.Data;
import patterns.Index;
import patterns.Sensor;
import patterns.Station;

import java.io.IOException;
import java.util.List;

/**
 * Class that connect with chosen service
 * and get required records.
 * Also parse those records to objects
 */
public interface IService {

    /**
     * Method that return list of stations from chosen service
     * @return list of stations
     * @throws IOException
     */
    public List<Station> manageStations() throws IOException;

    /**
     * Method that return sensors that are connected to given station id
     * @param StationID given station id
     * @return sensors from given station
     * @throws IOException
     */
    public Sensor manageSensors(int StationID) throws IOException;

    /**
     * Method that return values of pollution that is measured on the given sensor
     * @param sensorID given sensor id
     * @return pollution values
     * @throws IOException
     */
    public Data manageData(int sensorID) throws IOException;

    /**
     * Method that return index for given station
     * @param stationID given station id
     * @return index for given station
     * @throws IOException
     */
    public Index manageIndex(int stationID) throws IOException;


    /**
     * Method that connect with the service and download necessary records
     * @param type type of records to download
     * @param id id of station or sensor
     * @return downloaded records in json format
     * @throws IOException
     */
    public String loadRecordsFromService (String type,int id) throws IOException;
}
