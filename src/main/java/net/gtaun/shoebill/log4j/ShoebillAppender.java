/**
 * Copyright (C) 2012 MK124
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.gtaun.shoebill.log4j;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;

/**
 *
 *
 * @author MK124
 */
public final class ShoebillAppender extends FileAppender
{
	private static final DateFormat DATEFORMAT_FILE = new SimpleDateFormat("yyyy-MM-dd");
	private static final DateFormat DATEFORMAT_DIR = new SimpleDateFormat("yyyy-MM");


	private File path;
	private String fileExtension = "log";

	private long rollOverTimestamp;


	public ShoebillAppender()
	{

	}

	public ShoebillAppender(Layout layout, String filename) throws IOException
	{
		super(layout, filename, true);
		activateOptions();
	}

	public void setPath(String path)
	{
		this.path = new File(path);
	}

	public void setFileExtension(String fileExtension)
	{
		this.fileExtension = fileExtension;
	}

	@Override
	public void activateOptions()
	{
		if (!path.exists()) path.mkdirs();

		rollOverTimestamp = System.currentTimeMillis();
		try
		{
			rollOver();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		super.activateOptions();
	}

	@Override
	protected void subAppend(LoggingEvent event)
	{
		if (System.currentTimeMillis() >= rollOverTimestamp) try
		{
			rollOver();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		super.subAppend(event);
	}

	void generateRollOverDate()
	{
		Calendar calendar = Calendar.getInstance();

		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.add(Calendar.DATE, 1);
		rollOverTimestamp = calendar.getTimeInMillis();
	}

	void rollOver() throws IOException
	{
		closeFile();

		Date date = new Date(rollOverTimestamp);

		File destDir = new File(path, DATEFORMAT_DIR.format(date));
		if (!destDir.exists()) destDir.mkdirs();

		File destFile = new File(destDir, DATEFORMAT_FILE.format(date) + "." + fileExtension);
		setFile(destFile.getPath(), true, bufferedIO, bufferSize);

		generateRollOverDate();
	}
}
