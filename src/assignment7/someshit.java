package assignment7;
/* From Daniel Liang's book */

import java.io.*; 
import java.net.*; 
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
	private static String ID = "";

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
		
		TextField name = new TextField(); 
		name.setAlignment(Pos.BOTTOM_RIGHT); 
		paneforname.setCenter(name); 

		BorderPane mainPane = new BorderPane(); 
		// Text area to display contents 
		ta = new TextArea(); 
		mainPane.setBottom(new ScrollPane(ta)); 
		mainPane.setCenter(paneForTextField); 
		mainPane.setTop(paneforname);


		// Create a scene and place it in the stage 
		Scene scene = new Scene(mainPane, 450, 200); 
		primaryStage.setTitle("Client"); // Set the stage title 
		primaryStage.setScene(scene); // Place the scene in the stage 
		primaryStage.show(); // Display the stage 
		
		//Thread readerThread = new Thread(new IncomingReader());
		//readerThread.start();
		
		name.setOnAction(e -> {
			try {  
				ID = name.getText();
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
				writer.println(ID + " " + message);
				writer.flush();
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
					String[] splited = message.split("\\s+");
					for(int i = 0; i < splited.length; i++){
						if(splited[i].equals(this.ID)){
							ta.appendText(message + "\n");
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
