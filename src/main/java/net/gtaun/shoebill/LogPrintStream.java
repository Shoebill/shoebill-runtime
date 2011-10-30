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

package net.gtaun.shoebill;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author MK124
 *
 */

public class LogPrintStream extends PrintStream
{
	static DateFormat fileFormat = new SimpleDateFormat("yyyy-MM-dd");
	static DateFormat directoryFormat = new SimpleDateFormat("yyyy-MM");
	static DateFormat timestampFormat = new SimpleDateFormat("[HH:mm:ss] ");
	
	static String makeFilePath()
	{
		Date date = new Date();
		String path = "logs/" + directoryFormat.format(date);
		File file = new File(path);
		if( !file.exists() ) file.mkdirs();
		
		path += "/" + fileFormat.format(date) + ".log";
		return path;
	}
	
	
//---------------------------------------------------------
	
	PrintStream consoleStream;
	boolean timestamp = false;
	
	
	public  LogPrintStream( PrintStream console ) throws FileNotFoundException, UnsupportedEncodingException
	{
		super( new FileOutputStream(makeFilePath(), true), true, "UTF-8" );
		this.consoleStream = console;
	}
	
	private void timestamp()
	{
		if( timestamp ) return;
		
		super.print( timestampFormat.format(new Date()) );
		timestamp = true;
	}
	
	public void log( String s )
	{
		timestamp();
		
		super.print( s );
		super.println();
		
		timestamp = false;
	}

	@Override
	public void print( boolean b )
	{
		consoleStream.print( b );
		
		timestamp();
		super.print( b );
	}

	@Override
	public void print( char c )
	{
		consoleStream.print( c );

		timestamp();
		super.print( c );
	}

	@Override
	public void print( int i )
	{
		consoleStream.print( i );

		timestamp();
		super.print( i );
	}

	@Override
	public void print( long l )
	{
		consoleStream.print( l );

		timestamp();
		super.print( l );
	}

	@Override
	public void print( float f )
	{
		consoleStream.print( f );

		timestamp();
		super.print( f );
	}

	@Override
	public void print( double d )
	{
		consoleStream.print( d );

		timestamp();
		super.print( d );
	}

	@Override
	public void print( char[] s )
	{
		consoleStream.print( s );

		timestamp();
		super.print( s );
	}

	@Override
	public void print( String s )
	{
		consoleStream.print( s );

		timestamp();
		super.print( s );
		
		if( s.charAt(s.length()-1) == '\n' ) timestamp = false;
	}

	@Override
	public void print( Object obj )
	{
		consoleStream.print( obj );

		timestamp();
		super.print( obj );
	}

	@Override
	public void println()
	{
		consoleStream.println();

		timestamp();
		super.println();
		
		timestamp = false;
	}

	@Override
	public void println( boolean x )
	{
		consoleStream.println( x );

		timestamp();
		super.print( x );
		super.println();
		
		timestamp = false;
	}

	@Override
	public void println( char x )
	{
		consoleStream.println( x );

		timestamp();
		super.print( x );
		super.println();
		
		timestamp = false;
	}

	@Override
	public void println( int x )
	{
		consoleStream.println( x );

		timestamp();
		super.print( x );
		super.println();
		
		timestamp = false;
	}

	@Override
	public void println( long x )
	{
		consoleStream.println( x );

		timestamp();
		super.print( x );
		super.println();
		
		timestamp = false;
	}

	@Override
	public void println( float x )
	{
		consoleStream.println( x );

		timestamp();
		super.print( x );
		super.println();
		
		timestamp = false;
	}

	@Override
	public void println( double x )
	{
		consoleStream.println( x );

		timestamp();
		super.print( x );
		super.println();
		
		timestamp = false;
	}

	@Override
	public void println( char[] x )
	{
		consoleStream.println( x );

		timestamp();
		super.print( x );
		super.println();
		
		timestamp = false;	}

	@Override
	public void println( String x )
	{
		consoleStream.println( x );

		timestamp();
		super.print( x );
		super.println();
		
		timestamp = false;
	}

	@Override
	public void println( Object x )
	{
		consoleStream.println( x );

		timestamp();
		super.print( x );
		super.println();
		
		timestamp = false;
	}

	@Override
	public PrintStream printf( String format, Object... args )
	{
		String s = String.format(format, args);
		print( s );
		
		return this;
	}

	@Override
	public PrintStream printf( Locale l, String format, Object... args )
	{
		String s = String.format(l, format, args);
		print( s );
		
		return this;
	}

	@Override
	public PrintStream format( String format, Object... args )
	{
		String s = String.format(format, args);
		print( s );
		
		return this;
	}

	@Override
	public PrintStream format( Locale l, String format, Object... args )
	{
		String s = String.format(l, format, args);
		print( s );
		
		return this;
	}
}
