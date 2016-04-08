package mediator;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/** */
public class SerialWriter implements Runnable 
{
	OutputStream out;
	String command = " ";
	int num;

	public SerialWriter(OutputStream out)
	{
		this.out = out;
	}

	public void run ()
	{
		while(true){
                    try {
                        //if command is not just a space but a number
                        Thread.sleep(200);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(SerialWriter.class.getName()).log(Level.SEVERE, null, ex);
                    }
			if(command != " "){
				try {
					//parse string into int
					num = Integer.parseInt(command);
					System.out.println("writer woke up, command is " + command.charAt(0));
					
					//Note: needed to send at least 3 times to atmel stk500 so it actually receives it
					for(int i=0;i<3;i++){
						//send command to atmel
						out.write(command.charAt(0));
						out.flush();
					}
					Thread.sleep(200);
				}catch (IOException | InterruptedException e) {
					//
				}
				//reset command back to spaces
				command = " ";
			}
		}

	}

	//when called by mediator, the if in the while loop will run
	void wakeUp(String c){
		command = c;
	}
}