package common

import java.io.*

class ResourceFileReader private constructor() {
    @Throws(IOException::class)
    fun readContentOfResource(resourceName: String): String {
        return extractContent(locateResource(File.separator + resourceName))
    }

    @Throws(FileNotFoundException::class)
    private fun locateResource(resourcePath: String): InputStream {
        val classLoader = javaClass.classLoader
        val inputStream = classLoader.getResourceAsStream(resourcePath)
                ?: throw FileNotFoundException("Could not find resource: $resourcePath")

        return inputStream
    }

    companion object {

        var instance: ResourceFileReader? = null
            get() {
                if (field == null) {
                    field = ResourceFileReader()
                }
                return field
            }
            private set

        @Throws(IOException::class)
        private fun extractContent(stream: InputStream): String {
            val result = StringBuilder()
            BufferedReader(InputStreamReader(stream)).use { br ->
                var line = br.readLine()
                while (line != null) {
                    result.append(line)
                    line = br.readLine()
                }
            }
            return result.toString()
        }
    }
}
