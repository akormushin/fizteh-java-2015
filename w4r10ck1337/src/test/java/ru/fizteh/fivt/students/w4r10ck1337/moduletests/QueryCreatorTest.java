package ru.fizteh.fivt.students.w4r10ck1337.moduletests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ru.fizteh.fivt.students.w4r10ck1337.moduletests.library.GeoApi;
import ru.fizteh.fivt.students.w4r10ck1337.moduletests.library.QueryCreator;
import twitter4j.Query;

import static org.junit.Assert.assertEquals;
import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(GeoApi.class)
public class QueryCreatorTest {
    @Test
    public void testCreateQuery() {
        double [] location = new double[3];
        Query query = QueryCreator.createQuery("a", 300, location);
        assertEquals(query.getCount(), 100);

        query = QueryCreator.createQuery("a", 20, location);
        assertEquals(query.getCount(), 20);
    }

    @Test
    public void testGetLocation() throws Exception {
        double [] location = {1.0, 2.0, 3.0};
        mockStatic(GeoApi.class);
        when(GeoApi.getLocation(null, null)).thenThrow(new Exception()).thenThrow(new Exception()).thenReturn(location);

        assertEquals(location, QueryCreator.findLocation(null, null));
    }
}
