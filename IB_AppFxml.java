/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ib_app;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import com.ib.client.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;
import javafx.stage.WindowEvent;


/**
 *
 * @author Bishal
 */
public class IB_AppFxml extends Application {
    
    TextArea textArea;
    static IB_AppFxmlController controller;
    static EClientSocket m_client;
    static EReaderSignal m_signal;
    public EClientSocket getClient()
    {
        return m_client;
    }
    public void setClient( EClientSocket client)
    {
        m_client = client;
    }
    
    public EReaderSignal getSignal()
    {
        return m_signal;
    }
    public void setSignal( EReaderSignal signal)
    {
        m_signal = signal;
    }
    
    @Override
    public void start(Stage primaryStage) throws InterruptedException, IOException {
             
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("IB_AppFxml.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("IB_AppFxml.fxml"));
        loader.setController(this);
        Scene scene = new Scene(root);
        
        primaryStage.setScene(scene);
        primaryStage.show();
        textArea = new TextArea();
        EWrapperImpl wrapper = new EWrapperImpl(textArea);
		
	final EClientSocket m_client = wrapper.getClient();
        final EReaderSignal m_signal = wrapper.getSignal();
        

        /*primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
        @Override
        public void handle(final WindowEvent event) {
            wrapper.getClient().eDisconnect();
            //m_client.eDisconnect();
            System.out.println("closing the app");
            //IB_AppFxmlController fxmlController = (IB_AppFxmlController)loader.getController();
            //fxmlController.m_client.eDisconnect();
            Platform.exit();
            primaryStage.close();
            
        }
        });*/
        
        Button btn = new Button();
        btn.setText("Next Valid ID");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                //! [connect]
		/*m_client.eConnect("127.0.0.1", 7497, 2);
		//! [connect]
		//! [ereader]
		final EReader reader = new EReader(m_client, m_signal);   
		
		reader.start();
		//An additional thread is created in this program design to empty the messaging queue
		new Thread(() -> {
		    while (m_client.isConnected()) {
		        m_signal.waitForSignal();
		        try {
		            reader.processMsgs();
		        } catch (Exception e) {
		            System.out.println("Exception: "+e.getMessage());
		        }
		    }
		}).start();
                try {
                    //! [ereader]
                    // A pause to give the application time to establish the connection
                    // In a production application, it would be best to wait for callbacks to confirm the connection is complete
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(IB_AppFxml.class.getName()).log(Level.SEVERE, null, ex);
                }*/
            }
        });
        
        //StackPane root = new StackPane();
        //root.getChildren().add(btn);
        textArea.setEditable(false);
        VBox vbox = new VBox();
        vbox.getChildren().add(btn);
        vbox.getChildren().add(textArea);
        //textArea.appendText("test");
        //root.getChildren().add(vbox);
        /*Scene scene = new Scene(vbox, 300, 250);
        
        primaryStage.setTitle("IBApp");
        primaryStage.setScene(scene);
        primaryStage.show();*/
    }

    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
