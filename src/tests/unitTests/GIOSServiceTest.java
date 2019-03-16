package tests.unitTests;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import patterns.Data;
import patterns.Index;
import patterns.Sensor;
import patterns.Station;
import services.GIOSService;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class GIOSServiceTest {

    @Mock
    GIOSService gios = Mockito.mock(GIOSService.class);

    @Test
    void manageStations() throws IOException {
        File file = new File("test.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;
        StringBuilder result = new StringBuilder();

        while((st = br.readLine()) != null)
            result.append(st).append("\n");

        Station first = new Station();
        first.setName("Wrocław - Bartnicza"); first.setId(114);
        Station second = new Station();
        second.setName("Wrocław - Korzeniowskiego"); second.setId(117);
        List<Station> stationList = new ArrayList<>();
        stationList.add(first);
        stationList.add(second);

        when(gios.loadRecordsFromService("stations",0)).thenReturn(result.toString());
        when(gios.manageStations()).thenCallRealMethod();
        List<Station> stations = gios.manageStations();
        assertEquals(stationList.size(),stations.size());

    }

    @Test
    void manageSensors() throws IOException {
        File file = new File("sensor_114.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;
        StringBuilder result = new StringBuilder();

        while((st = br.readLine()) != null)
            result.append(st).append("\n");

        Sensor sensor = new Sensor(114);
        sensor.add("NO2",642);
        sensor.add("O3",644);

        when(gios.loadRecordsFromService("sensor",114)).thenReturn(result.toString());
        when(gios.manageSensors(114)).thenCallRealMethod();
        Sensor tmp = gios.manageSensors(114);
        assertTrue(sensor.equals(tmp));
    }

    @Test
    void manageData() throws IOException {
        File file = new File("data_0.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;
        StringBuilder result = new StringBuilder();

        while((st = br.readLine()) != null)
            result.append(st).append("\n");

        Data data = new Data(0);
        data.add("2017-03-28 11:00:00", (float) 30.3018);
        data.add("2017-03-28 12:00:00", (float) 27.5946);

        when(gios.loadRecordsFromService("data",0)).thenReturn(result.toString());
        when(gios.manageData(0)).thenCallRealMethod();
        Data tmp = gios.manageData(0);
        assertTrue(data.equals(tmp));
    }

    @Test
    void manageIndex() throws IOException {
        File file = new File("index_114.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;
        StringBuilder result = new StringBuilder();

        while((st = br.readLine()) != null)
            result.append(st).append("\n");

        Index index = new Index();
        index.add("stIndexLevel","Brak indeksu");
        index.add("no2IndexLevel","Bardzo dobry");
        index.add("o3IndexLevel","Bardzo dobry");

        when(gios.loadRecordsFromService("index",114)).thenReturn(result.toString());
        when(gios.manageIndex(114)).thenCallRealMethod();
        Index tmp = gios.manageIndex(114);
        assertTrue(index.equals(tmp));
    }
}