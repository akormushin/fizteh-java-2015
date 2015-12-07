package ru.fizteh.fivt.students.zakharovas.twitterstream.library;

import org.apache.commons.io.IOUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import twitter4j.JSONArray;
import twitter4j.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by alexander on 04.11.15.
 */
public class RussianDeclensionTest {

    private static String[] Endings;
    private static List<DeclensionTestData> tests;

    @BeforeClass
    public static void setUp() throws Exception {
        try (InputStream inputStream = RussianDeclensionTest.class.getResourceAsStream("/DeclensionTestData.json")) {
            JSONObject jsonTest = new JSONObject(IOUtils.toString(inputStream));
            JSONArray jsonEndings = jsonTest.getJSONArray("endings");
            Endings = new String[3];
            for (int i = 0; i < 3; ++i) {
                Endings[i] = jsonEndings.getString(i);
            }
            JSONArray jsonTests = jsonTest.getJSONArray("tests");
            tests = new ArrayList<>();
            for (int i = 0; i < jsonTests.length(); ++i) {
                JSONObject test = jsonTests.getJSONObject(i);
                tests.add(new DeclensionTestData(test.getInt("number"), test.getString("answer")));
            }
        }
    }

    @Test
    public void testDeclension() {
        for (DeclensionTestData test : tests) {
            assertThat(RussianDeclension.declensionWithNumber(test.number, Endings), is(test.answer));
        }
    }
}

class DeclensionTestData {
    int number;
    String answer;

    public DeclensionTestData(int number, String answer) {
        this.answer = answer;
        this.number = number;
    }
}
