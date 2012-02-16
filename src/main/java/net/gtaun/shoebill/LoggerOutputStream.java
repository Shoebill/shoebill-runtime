package net.gtaun.shoebill;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class LoggerOutputStream extends ByteArrayOutputStream 
{
	private final String SEPARATOR = System.getProperty("line.separator");
	
	private Logger logger;
	private Level level;
	
	
	public LoggerOutputStream( Logger logger, Level level )
	{
		this.logger = logger;
		this.level = level;
	}

	@Override
	public void flush() throws IOException
	{
		super.flush();
		String message = this.toString();
		super.reset();

        if( message.length() > 0 && !message.equals(SEPARATOR) )
        {
            logger.log( Shoebill.class.getName(), level, message, null );
        }
	}
}