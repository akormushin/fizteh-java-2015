package ru.fizteh.fivt.students.cache_nez.TwitterStream;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

/**
 * Created by cache-nez on 9/28/15.
 */


public class IntegerValidator implements IParameterValidator {
    public void validate(String name, String value)
            throws ParameterException {
        int n = Integer.parseInt(value);
        if (n <= 0) {
            throw new ParameterException("parameter " + name + " should be positive (found " + value + ")");
        }
    }
}
