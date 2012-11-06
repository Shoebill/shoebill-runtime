/**
 * Copyright (C) 2011 MK124
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

package net.gtaun.shoebill.util.log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.slf4j.Logger;

/**
 * 
 * 
 * @author MK124
 */
public class LoggerOutputStream extends ByteArrayOutputStream
{
	private static final String SEPARATOR = System.getProperty("line.separator");
	
	
	private Logger logger;
	private LogLevel level;
	
	
	public LoggerOutputStream(Logger logger, LogLevel level)
	{
		this.logger = logger;
		this.level = level;
	}
	
	@Override
	public void flush() throws IOException
	{
		super.flush();
		String message = this.toString();
		
		if (message.contains(SEPARATOR))
		{
			super.reset();
			
			String messages[] = message.split(SEPARATOR);
			for (String msg : messages)
				level.log(logger, msg);
		}
	}
}
