package common

import common.exceptions.MissingSystemPropertyException

object SystemPropertyReader {
    @Throws(MissingSystemPropertyException::class)
    fun readSystemProperty(propertyName: String): String {
        val propertyValue = System.getProperty(propertyName)
                ?: throw MissingSystemPropertyException("System property '$propertyName' is not defined.")

        return propertyValue
    }
}

