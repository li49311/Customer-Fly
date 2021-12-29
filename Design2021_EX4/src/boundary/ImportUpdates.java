package boundary;

import java.util.ArrayList;
import control.importControl;
import entity.FlightTicket;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class ImportUpdates {
	
	@FXML
	ListView CustomerList;
	
	@FXML
	Button importJson;
	
	@FXML
	Button btnSend;
	
	@FXML
	Button notifyButton;
	
	@FXML
	Label messageLbl;
	
	@FXML
	Label label;
	
	@FXML
	public void initialize() {

		notifyButton.setDisable(true);
	}


	@FXML
	public void importJSON(ActionEvent event)
	{
		ArrayList<FlightTicket> customerList = importControl.getAllNeedToCall();
		addToList(customerList);
		
		
		notifyButton.setDisable(false);
	}
	
	//this method is for adding to listView the customers
	@FXML
	private void addToList(ArrayList<FlightTicket> custList)
	{
		CustomerList.setItems(FXCollections.observableArrayList(custList));	
	}
	
	//Update the customer on the changes in his order details or on the cancellation of his flight
	@FXML
	private void notifyUser(ActionEvent event)
	{
		messageLbl.setText("");
		String message;
		String updateMessage;
		
		if(CustomerList.getSelectionModel().getSelectedItem() != null) // select from list
		{
			FlightTicket selectCustomer = (FlightTicket) CustomerList.getSelectionModel().getSelectedItem();
		
			//TODO Add a test that checks whether the flight is also in the "canceled" status
			if(importControl.getCustmersCantSeat().contains(selectCustomer))
			{//if the flight is canceled 
				updateMessage="Unfornatnatly we had to cancel your order";			
			}
			else
			{
				updateMessage="Unfornatnatly we had to update your order Details. \nThis is the new details: \n" +selectCustomer.getFlightNum();
			}
			message = "Dear " + selectCustomer.getCustomer().getFirstName() + ",\n"+updateMessage+"\nThis is your show recommendations if you would like to choose a new show: \n";
//			ArrayList<Flight> flights = importControl.recommendUserNewDetails(selectCustomer);
//			
//			for(Flight f :flights)
//			{
//				message+= f+"\n";
//			}
//			
//			if(flights.isEmpty())
//			{
//				message="Dear " + selectCustomer.getFirstName() + ",\n"+updateMessage+"\nUnfortently we don't have recommendations for you :( \n";
//			}
//		}
//		else
//		{
//			message="Please select custumer to call";
//		}
//		
		messageLbl.setText(message);	
	}
	}
	
	

}
