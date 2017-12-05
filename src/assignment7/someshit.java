package assignment7;
/* From Daniel Liang's book */

import java.io.*; 
import java.net.*;
import java.util.ArrayList;

import javafx.application.Application; 
import javafx.geometry.Insets; 
import javafx.geometry.Pos; 
import javafx.scene.Scene; 
import javafx.scene.control.Label; 
import javafx.scene.control.ScrollPane; 
import javafx.scene.control.TextArea;

import javafx.scene.control.TextField; 
import javafx.scene.layout.BorderPane; 
import javafx.stage.Stage; 


public class someshit extends Application{ 
	// IO streams 
	private static  BufferedReader reader;
	private static PrintWriter writer;
	static TextArea ta;
	static TextArea list;
	private static String ID = "";
	String target = null;
	//ArrayList<String> target = new ArrayList<String>();

	@SuppressWarnings("static-access")
	@Override // Override the start method in the Application class 
	public void start(Stage primaryStage) { 
		// Panel p to hold the label and text field 
		BorderPane paneForTextField = new BorderPane(); 
		paneForTextField.setPadding(new Insets(5, 5, 5, 5)); 
		paneForTextField.setStyle("-fx-border-color: green"); 
		paneForTextField.setLeft(new Label("Enter a message: ")); 

		TextField tf = new TextField(); 
		tf.setAlignment(Pos.BOTTOM_RIGHT); 
		paneForTextField.setCenter(tf); 
		
		BorderPane paneforname = new BorderPane();
		paneforname.setPadding(new Insets(5, 5, 5, 5));
		paneforname.setStyle("-fx-border-color: blue");
		paneforname.setLeft(new Label(" Enter Name: "));
		
		/*BorderPane paneforname1 = new BorderPane();
		paneforname1.setPadding(new Insets(5, 5, 5, 5));
		paneforname1.setStyle("-fx-border-color: blue");
		paneforname1.setLeft(new Label(" Enter Name: "));*/
		
		TextField name = new TextField(); 
		name.setAlignment(Pos.BOTTOM_RIGHT); 
		paneforname.setCenter(name); 

		BorderPane mainPane = new BorderPane(); 
		// Text area to display contents 
		ta = new TextArea(); 
		list = new TextArea();
		//mainPane.setAlignment(list, Pos.BOTTOM_RIGHT);
		mainPane.setBottom(new ScrollPane(ta)); 
		mainPane.setRight(new ScrollPane(list));
		mainPane.setCenter(paneForTextField); 
		mainPane.setTop(paneforname);


		// Create a scene and place it in the stage 
		Scene scene = new Scene(mainPane, 800, 200); 
		primaryStage.setTitle("Client"); // Set the stage title 
		primaryStage.setScene(scene); // Place the scene in the stage 
		primaryStage.show(); // Display the stage 
		
		//Thread readerThread = new Thread(new IncomingReader());
		//readerThread.start();
		
		name.setOnAction(e -> {
			try {  
				ID = name.getText();
				People.add(ID);
				Thread readerThread = new Thread(new IncomingReader(ID));
				readerThread.start();
			} 
			catch (Exception ex) { 
				System.err.println(ex); 
			} 
		}); 

		tf.setOnAction(e -> { 
			try { 
				String message = tf.getText();
				String[] splited = message.split("rs/");
				if(splited.length == 2){
					target = splited[0] + ID;
					message = splited[1];
					writer.println(target + " rs/ " + ID + " sd/" + message);
					writer.flush();
				}
				else if(splited.length == 1){
					if(target.equals(null)){
						tf.setText("Invalid message. If you want to send a message to a new group, first type '{recipients} /rs' followed by your message!");
					}
					writer.println(target + " rs/ " + ID + " sd/" + message);
					writer.flush();
				}
				else{
					tf.setText("Invalid message. If you want to send a message to a new group, first type '{recipients} /rs' followed by your message!");
				}
				//target = splited[0];
				//writer.println(ID + " " + message);
				//writer.flush();
				tf.setText("");
			} 
			catch (Exception ex) { 
				System.err.println(ex); 
			} 
		}); 

	}
	
	public static void main(String[] args) {
		//launch(args);
		try {
			setUpNetworking();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		launch(args);
	}
	
	private static void setUpNetworking() throws Exception {
		@SuppressWarnings("resource")
		Socket sock = new Socket("localhost", 4242);
		InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
		reader = new BufferedReader(streamReader);
		writer = new PrintWriter(sock.getOutputStream());
		System.out.println("networking established");
		//Thread readerThread = new Thread(new IncomingReader(controller));
		//Thread readerThread = new Thread(new IncomingReader());
		//Thread readerThread = new Thread(new someshit());
		//readerThread.start();
	}
	
	static class IncomingReader implements Runnable {
		String ID;
		
		public IncomingReader (String name){
			this.ID = name;
		}
		
		public void run() {
			String message = "";
			try {
				while ((message = reader.readLine()) != null) {
					String[] splited = message.split("rs/");
					System.out.println(splited[0]);
					System.out.println(splited[1]);
					/*for(int i = 0; i < splited.length; i++){
						if(splited[i].equals(this.ID)){
							ta.appendText(message + "\n");
						}
					}*/
					String[] names = splited[0].split("\\s+");
					for(int i = 0; i < names.length; i++){
						System.out.println(names[i]);
						if(names[i].equals(this.ID)){
							String[] truemessage = splited[1].split("sd/");
							ta.appendText(splited[0] + "(" + truemessage[0] + ")" + ":" + truemessage[1] + "\n");
						}
					}
					//ta.appendText(message + "\n");
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}
