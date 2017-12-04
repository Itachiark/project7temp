package assignment7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

public class ConnectSocketClient implements Observer{
	
	private BufferedReader reader;
	private PrintWriter writer;
	Socket sock = null;
	
	public ConnectSocketClient(Socket s){
		this.sock = s;
	}
	
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		try {
			writer = new PrintWriter(sock.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		writer.println(arg); //writer.println(arg);
		writer.flush(); //writer.flush();}
	}

	/*@Override
	public void run() {
		// TODO Auto-generated method stub
		String message;
		try {
			while ((message = reader.readLine()) != null) {
					System.out.println(message + "\n");	
					//fakeserver.Change_message();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}*/
	
}
