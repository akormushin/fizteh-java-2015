package ru.fizteh.fivt.students.oshch.moduletests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import ru.fizteh.fivt.students.oshch.moduletests.library.PlaceApi;

import java.io.IOException;

import static org.junit.Assert.assertEquals;


@RunWith(MockitoJUnitRunner.class)
public class PlaceApiTest {
    @Test
    public void getMoscow() throws IOException {
        PlaceApi place = new PlaceApi("Moscow");
        place.getBounds();
        assertEquals(35, place.getRadius(), 0.2);
        assertEquals(0, (55.755826 - place.getLocation().lat), 0.2);
        assertEquals(0, (37.617300 - place.getLocation().lng), 0.2);
    }
}