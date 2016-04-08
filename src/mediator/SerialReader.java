package mediator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/** */
public class SerialReader implements Runnable 
{
	InputStream in;
	String string; 		//global string to return to website

	public SerialReader(InputStream in)
	{
		this.in = in;
	}

	public void run ()
	{
		BufferedReader buf = new BufferedReader(new InputStreamReader(in));
		while(true){
			try
			{	string = buf.readLine();

			}
			catch ( IOException e )
			{
				//if empty, no data read
			}    
		}

	}

	//returns string, even if null
	//set string into null
	String getString(){
		String temp = string;
		string = null;
		return temp;
	}

}