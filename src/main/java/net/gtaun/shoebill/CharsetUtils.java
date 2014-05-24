package net.gtaun.shoebill;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CharsetUtils
{
	private static final Map<Integer, Charset> WINDOWS_CHARSETS = new HashMap<>();
	static
	{
		WINDOWS_CHARSETS.put(862, Charset.forName("CP862"));
		WINDOWS_CHARSETS.put(874, Charset.forName("CP874"));
		WINDOWS_CHARSETS.put(932, Charset.forName("SHIFT_JIS"));
		WINDOWS_CHARSETS.put(936, Charset.forName("GBK"));
		WINDOWS_CHARSETS.put(949, Charset.forName("BIG5"));
		for (int page = 1250; page <= 1258; page++) WINDOWS_CHARSETS.put(page, Charset.forName("CP" + page));
	}


	public static Charset getCharsetByCodepage(int codepage)
	{
		return WINDOWS_CHARSETS.get(codepage);
	}

	public static List<String> splitStringByCharsetLength(String str, int maxBytes, Charset charset)
	{
		List<String> strs = new ArrayList<>();
		CharsetEncoder encoder = charset.newEncoder();

		char[] in = new char[1];
		byte[] outBuf = new byte[2];

		ByteBuffer out = ByteBuffer.wrap(outBuf);
		StringBuilder sb = new StringBuilder(maxBytes);

		for (char c : str.toCharArray())
		{
			out.clear();
			encoder.encode(CharBuffer.wrap(in), out, false);

			if (sb.length() + out.position() > maxBytes)
			{
				strs.add(sb.toString());
				sb.setLength(0);
			}

			sb.append(c);
		}

		strs.add(sb.toString());
		return strs;
	}

	private CharsetUtils()
	{

	}
}
