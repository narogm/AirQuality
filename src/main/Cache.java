package main;

import patterns.Index;
import services.AirlyService;
import services.GIOSService;
import services.IService;

import java.io.*;
import java.util.Date;

/**
 * Class that save records to files and read them when needed
 */
public class Cache {

    private String serviceName;
    private IService service;

    public Cache(String serviceName) throws IOException {
        switch(serviceName){
            case "GIOS":
                service = new GIOSService();
                break;
            case "Airly":
                service = new AirlyService();
                break;
            default:
                throw new IOException("Invalid service name");
        }
        this.serviceName = serviceName;
    }

    public Cache(String serviceName, IService service) throws IOException {
        this.service = service;
        this.serviceName = serviceName;
    }

    /**
     * Method that save record to files
     * @param key object type
     * @param id object id
     * @throws IOException
     */
    public void save(String key, int id) throws IOException {
        Object object = null;
        switch(key){
            case "stations":
                object = service.manageStations();
                break;
            case "sensor":
                object = service.manageSensors(id);
                break;
            case "data":
                object = service.manageData(id);
                break;
            case "index":
                object = service.manageIndex(id);
                //((Index) object).setStationID(id);
                break;
        }

        File directory = new File("Cache"+File.separator+serviceName+File.separator+key);
        if(!directory.exists())
           directory.mkdirs();
        File file = new File("Cache"+File.separator+serviceName+File.separator+key+File.separator+id+".txt");
        try(ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(file))){
            output.writeObject(object);
            output.flush();
            output.close();
        }
        catch (IOException ex){
            System.out.println("Problem IO: " + ex);
        }
    }

    /**
     * Method that read required records form file.
     * When they are older than 1h, then the update take place.
     * @param key object type
     * @param id object id
     * @return required records
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public Object read(String key,int id) throws IOException, ClassNotFoundException {
        Object result = null;
        File file = new File("Cache"+File.separator+serviceName+File.separator+key+File.separator+id+".txt");

        long fileModificationDate = file.lastModified();
        if(!file.exists() || needsUpdate(fileModificationDate))
            save(key, id);
        try(ObjectInputStream input = new ObjectInputStream(new FileInputStream("Cache"+File.separator+serviceName+File.separator+key+File.separator+id+".txt"))){
            result = input.readObject();
        }
        catch (IOException ex){
            System.out.println("Problem IO" + ex);
        }
        return result;
    }

    private boolean needsUpdate(long fileModificationDate){
        Date currentDate = new Date();
        long diff = currentDate.getTime() - fileModificationDate;
        long diffHours = diff / (60*60*1000);
        return diffHours > 0;
    }
}
