package tests.unitTests;

import main.Cache;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import patterns.Station;
import services.IService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

//@ExtendWith(MockitoExtension.class)
public class CacheTest {

    @Mock
    //Cache cache = Mockito.mock(Cache.class);
    IService service = Mockito.mock(IService.class);

    @Test
    void readAndSaveTest() throws IOException, ClassNotFoundException {
        Station first = new Station();
        first.setName("jeden"); first.setId(1);
        Station second = new Station();
        first.setName("dwa"); first.setId(2);
        Station third = new Station();
        first.setName("trzy"); first.setId(3);
        List<Station> stationList = new ArrayList<>();
        stationList.add(first);
        stationList.add(second);
        stationList.add(third);

        Cache cache = new Cache("GIOS",service);

        when(service.manageStations()).thenReturn(stationList);
        cache.save("stations",999);
        assertEquals(stationList.size(),((List<Station>) cache.read("stations",999)).size());
    }

}