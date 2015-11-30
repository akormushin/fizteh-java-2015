package ru.fizteh.fivt.students.w4r10ck1337.moduletests;

import com.beust.jcommander.ParameterException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import ru.fizteh.fivt.students.w4r10ck1337.moduletests.library.ArgsParser;

import java.io.PrintStream;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;

public class ArgsParserTest {
    @Mock
    PrintStream out;

    @Before
    public void setUp() {
        out = mock(PrintStream.class);
        System.setOut(out);
    }

    @Test
    public void testHelp () {
        String[] args = {"--help", "-s"};

        ArgsParser.parseArgs(args);
        verify(out).println(anyString());
    }

    @Test(expected = ParameterException.class)
    public void testWrongArgs () {
        String[] args = {"--help -s"};

        ArgsParser.parseArgs(args);
    }
}
