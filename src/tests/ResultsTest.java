package tests;

import main.Cache;
import main.Results;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import patterns.Data;
import patterns.Index;



import java.io.IOException;
import java.text.ParseException;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ResultsTest {

    @Mock
    Cache cache;
    {
        try {
            cache = Mockito.spy(new Cache("GIOS"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getIndexForStationTest() throws IOException, ClassNotFoundException {
        Index index = new Index();
        index.add("stIndexLevel","Brak indeksu");
        index.add("no2IndexLevel","Bardzo dobry");
        index.add("o3IndexLevel","Bardzo dobry");

        String expected = "Index for Wrocław - Bartnicza:\n" +
                "stIndexLevel -> Brak indeksu\n" +
                "no2IndexLevel -> Bardzo dobry\n" +
                "o3IndexLevel -> Bardzo dobry\n";

        Results results = new Results("GIOS",cache);
        when(cache.read("index",114)).thenReturn(index);
        when(results.getIndexForStation("Wrocław - Bartnicza")).thenCallRealMethod();
        String result = results.getIndexForStation("Wrocław - Bartnicza");
        assertEquals(expected,result);
    }

    @Test
    void getValueOfPollutionForStationTest() throws IOException, ClassNotFoundException {
        Data data = new Data(642);
        data.add("2019-01-07 17:00:00", (float) 30.6717);
        data.add("209-01-07 18:00:00", (float) 27.5946);

        String expected = "Value for given date of O3 was 30.6717";

        Results results = new Results("GIOS",cache);
        when(cache.read("data",644)).thenReturn(data);
        when(results.getValueOfPollutionForStation("2019-01-07 17:00:00","Wrocław - Bartnicza","O3")).thenCallRealMethod();
        String result = results.getValueOfPollutionForStation("2019-01-07 17:00:00","Wrocław - Bartnicza","O3");
        assertEquals(expected,result);
    }


    //TODO do poprawy
    @Test
    void getAverageOfPollutionForStationTest() throws IOException, ClassNotFoundException, ParseException {
        Data data = new Data(642);
        data.add("2019-01-07 17:00:00", (float) 30.6717);
        data.add("209-01-07 18:00:00", (float) 35.6339);
        data.add("209-01-07 19:00:00", (float) 30.1489);
        data.add("209-01-07 20:00:00", (float) 26.3783);

        float expected = (float) 32.1515;

        Results results = new Results("GIOS",cache);
        when(cache.read("data",644)).thenReturn(data);
        when(results.getAverageOfPollutionForStation("2019-01-07 17:00:00","2019-01-07 19:00:00","Wrocław - Bartnicza","O3")).thenCallRealMethod();
        Float result = results.getAverageOfPollutionForStation("2019-01-07 17:00:00","2019-01-07 19:00:00","Wrocław - Bartnicza","O3");
        assertEquals(expected,result);
    }

    @Test
    void getMaximalFluctuationForStationTest() {
    }

    @Test
    void getMinimalValueTest() {
    }

    @Test
    void getSensorsWithPollutionStandardsExceedanceTest() {
    }

    @Test
    void getMaximalAndMinimalValueForPollutionTest() {
    }

    @Test
    void getGraphTest() {
    }
}