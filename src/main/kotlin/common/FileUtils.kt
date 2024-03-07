package common

import java.io.File

object FileUtils {

    fun deleteFolder(folder: File) {
        val files = folder.listFiles()
        if (files != null) {
            for (file in files) {
                if (file.isDirectory) {
                    deleteFolder(file)
                } else {
                    file.delete()
                }
            }
        }
        folder.delete()
    }
}
