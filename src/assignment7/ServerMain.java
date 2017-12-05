package assignment7;

/*import java.io.IOException;
import java.net.ServerSocket;*/
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class ServerMain {
	
	public ArrayList<String> AllClients;
	//private ArrayList<ClientMain> clientArray = new ArrayList<ClientMain>();
	//String message = null;
	
	ConnectSocketClient connection; 
	TheUnObservables fakeserver = new TheUnObservables();
	
	
	public static void main(String[] args){
		
		try {
			new ServerMain().setUpNetworking();
			} catch (Exception e) {
				System.out.println("Waiting!");
				}
		}
	
	private void setUpNetworking() throws Exception {
		//clientOutputStreams = new ArrayList<PrintWriter>();
		@SuppressWarnings("resource")
		ServerSocket serverSock = new ServerSocket(4242); 
		while (true) {
			Socket clientSocket = serverSock.accept();
			connection = new ConnectSocketClient(clientSocket);
			fakeserver.addObserver(connection);
			/*ClientMain client_new = new ClientMain();
			clientArray.add(client_new);
			clientArray.get(clientArray.size()-1).addObserver((Observer) client_new);*/
			//PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
			//clientOutputStreams.add(writer);
			//this.addObserver(clientSocket.getClass());
			Thread t = new Thread(new ClientHandler(clientSocket));
			//Thread t = new Thread(connection);
			t.start();
			System.out.println("got a connection");
		}

	}
	
	/*private void notifyClients(String message) {


		for (PrintWriter writer : clientOutputStreams) {
			writer.println(message);
			writer.flush();
		}
	}*/

	class ClientHandler implements Runnable {
		private BufferedReader reader;

		public ClientHandler(Socket clientSocket) throws IOException {
			Socket sock = clientSocket;
			reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		}

		public void run() {
			String message;
			try {
				while ((message = reader.readLine()) != null) {
					System.out.println("read " + message);
					fakeserver.Change_message(message);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
		
		
		/*try {
		      ServerSocket myService = new ServerSocket(4242);
		      Socket clientSocket = myService.accept();
		        }
		        catch (IOException e) {
		           System.out.println(e);
		        }*/

		
		
		
		
}

