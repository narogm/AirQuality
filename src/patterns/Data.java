package patterns;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Data implements Serializable {
    private int sensorID;
    private LinkedHashMap<String, Float> values;

    public LinkedHashMap<String, Float> getValues() {
        return values;
    }

    public Data(int sensorID) {
        this.sensorID = sensorID;
        values = new LinkedHashMap<>();
    }

    public void add(String date, Float value) {
        values.put(date, value);
    }

    public float getValueForDate(String date) {
        return values.get(date);
    }

    /**
     * Method that return average value of given pollution in given period
     * @param startDate start of period
     * @param endDate end of period
     * @return average value
     * @throws ParseException
     * @throws IOException
     */
    public float getAverageInPeriod(String startDate, String endDate) throws ParseException, IOException {
        float sum = 0;
        int counter = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (sdf.parse(endDate).compareTo(sdf.parse(startDate)) <= 0) {
            throw new IOException("Wrong dates were given - end date is before start date");
        }

        HashMap<String, Float> valuesInPeriod = getValuesInPeriod(startDate, endDate);
        for (Map.Entry<String, Float> entry : valuesInPeriod.entrySet()) {
            sum += entry.getValue();
            counter += 1;
        }
        return sum / counter;
    }

    /**
     * Method that search for maximal fluctuation in given period
     * @param startDate start of the period
     * @return value of maximal fluctuation
     * @throws ParseException
     */
    public float getMaximalFluctuation(String startDate) throws ParseException {
        float reference = values.get(startDate);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse(startDate);
        float max = 0;
        for (Map.Entry<String, Float> entry : values.entrySet()) {
            if (date.compareTo(sdf.parse(entry.getKey())) < 0) {
                if (max < Math.abs(reference - entry.getValue()))
                    max = Math.abs(reference - entry.getValue());
            }
        }
        return max;
    }


    /**
     * Method that return only those values which are in given period
     * @param startDate start of the period
     * @param endDate end of the period
     * @return values in given period
     * @throws ParseException
     */
    public HashMap<String, Float> getValuesInPeriod(String startDate, String endDate) throws ParseException {
        HashMap<String, Float> result = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDate = startDate;
        while (sdf.parse(endDate).compareTo(sdf.parse(currentDate)) >= 0) {
            result.put(currentDate, values.get(currentDate));
            currentDate = getNextDate(currentDate);
        }
        return result;
    }


    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, Float> entry : values.entrySet())
            result.append(entry.getKey()).append(" -> ").append(entry.getValue()).append("\n");

        return result.toString();
    }

    private String getNextDate(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sdf.parse(date));
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        return sdf.format(calendar.getTime());
    }

    public boolean equals(Data d){
        if(d.sensorID != this.sensorID)
            return false;
        return d.values.equals(this.values);
    }
}
