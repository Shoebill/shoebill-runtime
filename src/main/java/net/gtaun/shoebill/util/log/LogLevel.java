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

package net.gtaun.shoebill.util.log;

import org.slf4j.Logger;

/**
 * 
 * 
 * @author MK124
 */
public enum LogLevel
{
	TRACE,
	DEBUG,
	INFO,
	WARN,
	ERROR;
	
	
	public void log(Logger logger, String message)
	{
		switch (this)
		{
		case TRACE:
			logger.trace(message);
			break;
		
		case DEBUG:
			logger.debug(message);
			break;
		
		case INFO:
			logger.info(message);
			break;
		
		case WARN:
			logger.warn(message);
			break;
		
		case ERROR:
			logger.error(message);
			break;
		}
	}
}
