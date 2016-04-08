package mediator;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;

public class Mediator{

	static SerialReader reader;
	static SerialWriter writer;

	public static void main ( String[] args ) throws IOException, InterruptedException
	{
		ServerSocket server = null;
		BufferedReader ser;
		PrintWriter out = null;
		DataInputStream in = null;
		Socket clientSocket;		
		int command;
		ser = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("///////\t\tCHOOSE THE CORRECT SERIAL PORT\t\t///////");

		Enumeration portList = CommPortIdentifier.getPortIdentifiers();	
		while(portList.hasMoreElements()){
			CommPortIdentifier portId = (CommPortIdentifier) portList.nextElement();
			System.out.println(portId.getName());
		}
		String commP = ser.readLine();
		
		// try to open a socket on port 12000
		try{
			
			server = new ServerSocket(12000,5,InetAddress.getLocalHost());
			Mediator mediator = new Mediator();
			mediator.serialConnect(commP);
			System.out.println("Serial connection is successful...");
			System.out.println("Server bootup is successful...");
			System.out.println("Mediator is booted up...");
		
		}catch(Exception e){
			System.out.println("error creating a server or connecting through serial.");
			System.exit(0);
		}

		while(true){
			// try to create a socket object from the ServerSocket
			// and listen and accept connections
			// Open the input and output streams
			try{

				clientSocket = server.accept();
				if(clientSocket.isConnected()){
					System.out.println("a connection was made from: " + clientSocket.getRemoteSocketAddress());
					out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()),true);
					in = new DataInputStream(clientSocket.getInputStream());
				}

				// available stream to be read
				int length = in.available();

				// create buffer
				byte[] b = new byte[length];

				// read the full data into the buffer
				in.readFully(b);
				char c = 0;

				// for each byte in the buffer
				for (byte a:b)
				{
					// convert byte to char
					c = (char)a; 
				}
				command = Character.getNumericValue(c);
				System.out.println("received command: " + command);
				//if commands arent empty, 
				switch(command){
				case 1:
					writer.wakeUp("1");
					Thread.sleep(200);
					out.println(reader.getString());
                                        break;
				case 2:
					writer.wakeUp("2");
					Thread.sleep(200);
					out.println(reader.getString());
					break;
				case 3:
					writer.wakeUp("3");
					Thread.sleep(200);
					out.println(reader.getString());
					break;
				case 4:
					writer.wakeUp("4");
					Thread.sleep(200);
					out.println(reader.getString());
					break;
				default:
					out.println("Command was unable to be processed.");
					break;
				}
			}catch(IOException e){
				System.out.println("error writing to clients." + e);
			}

		}

	}


	void serialConnect ( String portName ) throws Exception{
		CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
		if ( portIdentifier.isCurrentlyOwned() )
		{
			System.out.println("Error: Port is currently in use");
			System.exit(0);
		}
		else
		{
			CommPort commPort = portIdentifier.open(this.getClass().getName(),6000);

			if ( commPort instanceof SerialPort )
			{
				SerialPort serialPort = (SerialPort) commPort;
				serialPort.setSerialPortParams(115200,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
				//				System.out.println("BaudRate: " + serialPort.getBaudRate());
				//				System.out.println("DataBIts: " + serialPort.getDataBits());
				//				System.out.println("StopBits: " + serialPort.getStopBits());
				//				System.out.println("Parity: " + serialPort.getParity());
				//				System.out.println("FlowControl: " + serialPort.getFlowControlMode());

				//get input output stream
				InputStream in = serialPort.getInputStream();
				OutputStream out = serialPort.getOutputStream();

				//start the reader writer threads for serial communication
				reader = new SerialReader(in);
				Thread r = new Thread(reader);
				r.start();
				writer = new SerialWriter(out);
				Thread w = new Thread(writer);
				w.start();
			}
			else
			{
				System.out.println("Error: serial ports only");
			}
		}     
	}
}