/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ib_app;
import ib_app.SimpleMovingAverage;
import com.ib.client.Contract;
import com.ib.client.EClientSocket;
import com.ib.client.EReader;
import com.ib.client.EReaderSignal;
import com.ib.contracts.StkContract;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
//import org.joda.time.DateTime;


/**
 * FXML Controller class
 *
 * @author Bishal
 */
public class IB_AppFxmlController implements Initializable {

    //TextArea textArea = new TextArea();
    @FXML private Label label;
    @FXML private TextField instr;
    @FXML private TextField TimeInt;
    @FXML private TextField ObsTimeRange;
    @FXML private ComboBox<String> timeIntCbx;
    @FXML public TextArea Logs;
    
    private ObservableList<String> timeIntList = FXCollections.observableArrayList("1 min","2 min", "5 min");
    
    public EClientSocket m_client;// = wrapper.getClient();
    public EReaderSignal m_signal;// = wrapper.getSignal();
    
    @FXML
    private void handleDisconnect(ActionEvent event) {
        System.out.println("disconnecting\n");
        m_client.eDisconnect();    
    }   
    
        @FXML
    private void handleButtonAction(ActionEvent event) {
        //System.out.println("values are - " + instr.getText() + " " + ObsTimeRange.getText() + " " + timeIntCbx.getSelectionModel().getSelectedItem());
        m_client.eConnect("127.0.0.1", 7497, 2);
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
                }
                Date date = new Date();
                String modifiedDate= new SimpleDateFormat("yyyyMMdd").format(date);
                String queryTime = modifiedDate + " 09:30:00";
                System.out.println("date is " + queryTime);
                //Logs.appendText("date is " + queryTime + "\n");
                Contract contract = new Contract();
		/*contract.symbol("AAPL");
		contract.secType("STK");
		contract.currency("USD");
		contract.exchange("SMART");*/
		// Specify the Primary Exchange attribute to avoid contract ambiguity
		// (there is an ambiguity because there is also a MSFT contract with primary exchange = "AEB")
		//contract.primaryExch("ISLAND");
                contract.symbol("EUR");
		contract.secType("CASH");
		contract.currency("GBP");
		contract.exchange("IDEALPRO");
                m_client.reqHistoricalData(4001, contract, "20201211 11:30:00", "3600 S", "30 secs", "MIDPOINT", 1, 1, false, null);
    }
    
    
    @FXML
    public void exitApplication(ActionEvent event) {
        System.out.println("exiting app");
        Platform.exit();
    }
    
    public void test(){}
    /**
     * Initializes the controller class.
     */
    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        timeIntCbx.setItems(timeIntList);
        timeIntCbx.getSelectionModel().selectFirst();
        ib_app.EWrapperImpl wrapper = new ib_app.EWrapperImpl(this.Logs);
        m_client = wrapper.getClient();
        //final EReaderSignal m_signal = wrapper.getSignal();
        m_signal = wrapper.getSignal();
        Logs.setScrollTop(Double.MAX_VALUE);
    }
}
