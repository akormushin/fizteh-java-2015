package ru.fizteh.fivt.students.nmakeenkov.twitterstream.library;

import com.beust.jcommander.ParameterException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import java.io.PrintStream;
import ru.fizteh.fivt.students.nmakeenkov.twitterstream.library.CommandLineParametersParser.Parameters;

@RunWith(JUnit4.class)
public class CommandLineParametersParserTest {
    private static CommandLineParametersParser commandLineParametersParser
            = new CommandLineParametersParser();

    @Mock
    PrintStream outStream;

    @Before
    public void setUp() throws Exception {
        outStream = mock(PrintStream.class);
        System.setOut(outStream);
    }

    @Test(expected = ParameterException.class)
    public void testFail() throws Exception {
        CommandLineParametersParser.parse(new String[]{"--ololo"});
    }


    @Test
    public void testHelp() {
        CommandLineParametersParser.parse(new String[]{"--help"});
        verify(outStream).println(anyString());
    }

    @Test
    public void testCase1() {
        Parameters params = CommandLineParametersParser.parse(new String[]{"-q", "Chloe",
                "Grace", "Moretz"});
        Assert.assertEquals("Chloe Grace Moretz", params.getQuery());
        Assert.assertEquals("nearby", params.getPlace());
        Assert.assertEquals(false, params.isHideRetweets());
    }

    @Test
    public void testCase2() {
        Parameters params = CommandLineParametersParser.parse(new String[]{"--limit",
                "228", "-p", "Moscow", "city", "-q", "java"});
        Assert.assertEquals("java", params.getQuery());
        Assert.assertEquals("Moscow city", params.getPlace());
        Assert.assertEquals(228, params.getLimit());
        Assert.assertEquals(false, params.isStream());
    }
}