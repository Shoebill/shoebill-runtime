package net.gtaun.samp;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class LogPrintStream extends PrintStream
{
	static DateFormat format = new SimpleDateFormat("[HH:mm:ss] ");
	
	
	PrintStream consoleStream;
	boolean timestamp = false;
	
	
	public LogPrintStream( String file, PrintStream console ) throws FileNotFoundException, UnsupportedEncodingException
	{
		super( new FileOutputStream(file, true), true, "UTF-8" );
		this.consoleStream = console;
	}
	
	private void timestamp()
	{
		if( timestamp ) return;
		
		super.print( format.format(new Date()) );
		timestamp = true;
	}

	public void print( boolean b )
	{
		consoleStream.print( b );
		
		timestamp();
		super.print( b );
	}

	public void print( char c )
	{
		consoleStream.print( c );

		timestamp();
		super.print( c );
	}

	public void print( int i )
	{
		consoleStream.print( i );

		timestamp();
		super.print( i );
	}

	public void print( long l )
	{
		consoleStream.print( l );

		timestamp();
		super.print( l );
	}

	public void print( float f )
	{
		consoleStream.print( f );

		timestamp();
		super.print( f );
	}

	public void print( double d )
	{
		consoleStream.print( d );

		timestamp();
		super.print( d );
	}

	public void print( char[] s )
	{
		consoleStream.print( s );

		timestamp();
		super.print( s );
	}

	public void print( String s )
	{
		consoleStream.print( s );

		timestamp();
		super.print( s );
		
		if( s.charAt(s.length()-1) == '\n' ) timestamp = false;
	}

	public void print( Object obj )
	{
		consoleStream.print( obj );

		timestamp();
		super.print( obj );
	}

	public void println()
	{
		consoleStream.println();

		timestamp();
		super.println();
		
		timestamp = true;
	}

	public void println( boolean x )
	{
		consoleStream.println( x );

		timestamp();
		super.print( x );
		super.println();
		
		timestamp = true;
	}

	public void println( char x )
	{
		consoleStream.println( x );

		timestamp();
		super.print( x );
		super.println();
		
		timestamp = true;
	}

	public void println( int x )
	{
		consoleStream.println( x );

		timestamp();
		super.print( x );
		super.println();
		
		timestamp = true;
	}

	public void println( long x )
	{
		consoleStream.println( x );

		timestamp();
		super.print( x );
		super.println();
		
		timestamp = true;
	}

	public void println( float x )
	{
		consoleStream.println( x );

		timestamp();
		super.print( x );
		super.println();
		
		timestamp = true;
	}

	public void println( double x )
	{
		consoleStream.println( x );

		timestamp();
		super.print( x );
		super.println();
		
		timestamp = true;
	}

	public void println( char[] x )
	{
		consoleStream.println( x );

		timestamp();
		super.print( x );
		super.println();
		
		timestamp = true;	}

	public void println( String x )
	{
		consoleStream.println( x );

		timestamp();
		super.print( x );
		super.println();
		
		timestamp = true;
	}

	public void println( Object x )
	{
		consoleStream.println( x );

		timestamp();
		super.print( x );
		super.println();
		
		timestamp = true;
	}

	public PrintStream printf( String format, Object... args )
	{
		String s = String.format(format, args);
		print( s );
		
		return this;
	}

	public PrintStream printf( Locale l, String format, Object... args )
	{
		String s = String.format(l, format, args);
		print( s );
		
		return this;
	}

	public PrintStream format( String format, Object... args )
	{
		String s = String.format(format, args);
		print( s );
		
		return this;
	}

	public PrintStream format( Locale l, String format, Object... args )
	{
		String s = String.format(l, format, args);
		print( s );
		
		return this;
	}
}
