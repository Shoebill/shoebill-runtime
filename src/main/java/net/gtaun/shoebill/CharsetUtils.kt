package net.gtaun.shoebill

import java.nio.ByteBuffer
import java.nio.CharBuffer
import java.nio.charset.Charset
import java.util.*

object CharsetUtils {
    private val WINDOWS_CHARSETS = HashMap<Int, Charset>()

    init {
        WINDOWS_CHARSETS.put(862, Charset.forName("CP862"))
        WINDOWS_CHARSETS.put(874, Charset.forName("CP874"))
        WINDOWS_CHARSETS.put(932, Charset.forName("SHIFT_JIS"))
        WINDOWS_CHARSETS.put(936, Charset.forName("GBK"))
        WINDOWS_CHARSETS.put(949, Charset.forName("BIG5"))
        for (page in 1250..1258) WINDOWS_CHARSETS.put(page, Charset.forName("CP" + page))
    }

    fun getCharsetByCodepage(codepage: Int): Charset? = WINDOWS_CHARSETS[codepage]

    @JvmOverloads
    fun splitStringByCharsetLength(str: String, maxBytes: Int, charset: Charset, linePrefix: String = ""): List<String> {
        val strs = ArrayList<String>()
        val encoder = charset.newEncoder()

        val `in` = CharArray(1)
        val outBuf = ByteArray(2)

        val out = ByteBuffer.wrap(outBuf)
        val sb = StringBuilder(maxBytes)

        for (c in str.toCharArray()) {
            out.clear()
            encoder.encode(CharBuffer.wrap(`in`), out, false)

            if (sb.length + out.position() > maxBytes) {
                strs.add(sb.toString())
                sb.setLength(0)
                sb.append(linePrefix)
            }

            sb.append(c)
        }

        strs.add(sb.toString())
        return strs
    }
}
