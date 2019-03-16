package tests.unitTests;

import org.junit.jupiter.api.Test;
import patterns.Data;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class DataTest {

    private Data data = new Data(0);

    @Test
    void getAverageInPeriodTest() throws IOException, ParseException {
        float val1 = (float) 30.3018;
        float val2 = (float) 27.5946;
        float val3 = (float) 29.8635;
        float val4 = (float) 24.0947;
        data.add("2017-03-28 11:00:00", val1);
        data.add("2017-03-28 12:00:00", val2);
        data.add("2017-03-28 13:00:00", val3);
        data.add("2017-03-28 14:00:00", val4);

        float expected = (val1+val2+val3)/3;
        assertEquals(expected,data.getAverageInPeriod("2017-03-28 11:00:00","2017-03-28 13:00:00"));
    }

    @Test
    void getMaximalFluctuationTest() throws ParseException {
        float val1 = (float) 30.3018;
        float val2 = (float) 27.5946;
        float val3 = (float) 29.8635;
        float val4 = (float) 24.0947;
        data.add("2017-03-28 11:00:00", val1);
        data.add("2017-03-28 12:00:00", val2);
        data.add("2017-03-28 13:00:00", val3);
        data.add("2017-03-28 14:00:00", val4);

        assertEquals(Math.abs(val1-val4),data.getMaximalFluctuation("2017-03-28 11:00:00"));
    }

    @Test
    void getValuesInPeriodTest() throws ParseException {
        float val1 = (float) 30.3018;
        float val2 = (float) 27.5946;
        float val3 = (float) 29.8635;
        float val4 = (float) 24.0947;
        data.add("2017-03-28 11:00:00", val1);
        data.add("2017-03-28 12:00:00", val2);
        data.add("2017-03-28 13:00:00", val3);
        data.add("2017-03-28 14:00:00", val4);

        HashMap<String,Float> expected = new HashMap<>();
        expected.put("2017-03-28 12:00:00", val2);
        expected.put("2017-03-28 13:00:00", val3);

        assertEquals(expected,data.getValuesInPeriod("2017-03-28 12:00:00","2017-03-28 13:00:00"));
    }
}