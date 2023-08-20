package common;

import common.exceptions.MissingSystemPropertyException;

public class SystemPropertyReader {
    public static String readSystemProperty(String propertyName) throws MissingSystemPropertyException {
        String propertyValue = System.getProperty(propertyName);

        if (propertyValue == null) {
            throw new MissingSystemPropertyException("System property '" + propertyName + "' is not defined.");
        }

        return propertyValue;
    }
}

