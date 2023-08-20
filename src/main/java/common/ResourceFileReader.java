package common;

import java.io.*;

public class ResourceFileReader {

    private static ResourceFileReader instance;

    public static ResourceFileReader getInstance() {
        if (instance == null) {
            instance = new ResourceFileReader();
        }
        return instance;
    }

    public String readContentOfResource(String resourceName) throws IOException
    {
        return extractContent(locateResource(File.separator + resourceName));
    }

    private InputStream locateResource(String resourcePath) throws FileNotFoundException {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(resourcePath);

        if (inputStream == null) {
            throw new FileNotFoundException("Could not find resource: " + resourcePath);
        }
        return inputStream;
    }

    private static String extractContent(InputStream stream) throws IOException
    {
        StringBuilder result = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(stream)))
        {
            String line = br.readLine();
            while (line != null)
            {
                result.append(line);
                line = br.readLine();
            }
        }
        return result.toString();
    }

    private ResourceFileReader()
    {
        // Hiding the public constructor.
    }
}
