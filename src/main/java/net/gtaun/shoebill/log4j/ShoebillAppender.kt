/**
 * Copyright (C) 2012 MK124

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.gtaun.shoebill.log4j

import org.apache.log4j.FileAppender
import org.apache.log4j.Layout
import org.apache.log4j.spi.LoggingEvent
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author MK124
 * @author Marvin Haschker
 */
internal class ShoebillAppender : FileAppender {

    private var path: File? = null
    private var fileExtension = "log"

    private var rollOverTimestamp: Long = 0

    @Throws(IOException::class)
    constructor(layout: Layout, filename: String) : super(layout, filename, true) {
        activateOptions()
    }

    fun setPath(path: String) {
        this.path = File(path)
    }

    fun setFileExtension(fileExtension: String) {
        this.fileExtension = fileExtension
    }

    override fun activateOptions() {
        if (!path!!.exists()) path!!.mkdirs()

        rollOverTimestamp = System.currentTimeMillis()
        try {
            rollOver()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        super.activateOptions()
    }

    override fun subAppend(event: LoggingEvent) {
        if (System.currentTimeMillis() >= rollOverTimestamp)
            try {
                rollOver()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        super.subAppend(event)
    }

    private fun generateRollOverDate() {
        val calendar = Calendar.getInstance()

        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        calendar.add(Calendar.DATE, 1)
        rollOverTimestamp = calendar.timeInMillis
    }

    @Throws(IOException::class)
    private fun rollOver() {
        closeFile()

        val date = Date(rollOverTimestamp)

        val destDir = File(path, DATEFORMAT_DIR.format(date))
        if (!destDir.exists()) destDir.mkdirs()

        val destFile = File(destDir, DATEFORMAT_FILE.format(date) + "." + fileExtension)
        setFile(destFile.path, true, bufferedIO, bufferSize)

        generateRollOverDate()
    }

    companion object {
        private val DATEFORMAT_FILE = SimpleDateFormat("yyyy-MM-dd")
        private val DATEFORMAT_DIR = SimpleDateFormat("yyyy-MM")
    }
}
