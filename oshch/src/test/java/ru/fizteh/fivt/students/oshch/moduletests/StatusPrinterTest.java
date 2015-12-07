package ru.fizteh.fivt.students.oshch.moduletests;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import ru.fizteh.fivt.students.oshch.moduletests.library.StatusPrinter;
import twitter4j.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class StatusPrinterTest {
    private static final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    public static List<Status> statuses;

    @BeforeClass
    public static void loadSampleData() {
        statuses = Twitter4jTestUtils.tweetsFromJson("/statuses.json");
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void statusPrinterTest() throws Exception {
        StatusPrinter.setStream(true);
        StatusPrinter.printStatus(statuses.get(0));
        String expected =
                "@QuintetLive: #QuintetLaunch : welddon launched the MIPT DGAP (Human Solo).\n" +
                        "--------------------------------------------------------------------------------\n".trim();
        String actual = outContent.toString().trim();
        assertEquals(expected, actual);

    }
}