package boundary;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JFrame;
import control.ControlReport;
import control.importControl;
import entity.Airport;
import entity.Flight;
import entity.FlightTicket;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import util.FlightStatus;

public class ImportUpdates {
	
	@FXML
	ListView CustomerList;
	
	@FXML
	ListView ticketsList;
	
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
	private TableView<Flight> allFlightsTable;
	@FXML
	private TableColumn<Flight, String> flightIDCol;
	@FXML
	private TableColumn<Flight, LocalDateTime> departureCol;
	@FXML
	private TableColumn<Flight, LocalDateTime> landingCol;
	@FXML
	private TableColumn<Flight, String> fromCol;
	@FXML
	private TableColumn<Flight, String> toCol;
	@FXML
	private TableColumn<Flight, FlightStatus> statusCol;
	@FXML
	private TableColumn<Flight, String> airplaneIDCol;

	List<Flight> allFlights = new ArrayList<Flight>();
	
	
	
	
	@FXML
	public void initialize() {

		notifyButton.setDisable(true);
		
	  	
	}


	@FXML
	public void importJSON(ActionEvent event)
	{
		HashMap<Flight, ArrayList<FlightTicket>> flightsAndTickets = importControl.getAllNeedToCall();
		addToList(flightsAndTickets);
		
		System.out.println(importControl.getCustmersCantSeat());
		
		CustomerList.setItems(FXCollections.observableArrayList(importControl.getCustmersCantSeat()));	
		
		
		notifyButton.setDisable(false);
	}
	
	//this method is for adding to listView the customers
	@FXML
	private void addToList(HashMap<Flight, ArrayList<FlightTicket>> flightsAndTickets)
	{
		ArrayList<Flight> problematicFlights = new ArrayList<>();
		for(Flight flight: flightsAndTickets.keySet()) 
			problematicFlights.add(flight);
			
		//fill table
		
		allFlightsTable.getItems().addAll(problematicFlights);
		
		flightIDCol.setCellValueFactory(flight -> new ReadOnlyObjectWrapper<String>(flight.getValue().getFlightNum()));
		departureCol.setCellValueFactory(flight -> new ReadOnlyObjectWrapper<LocalDateTime>(flight.getValue().getDepartureTime().toLocalDateTime()));
		landingCol.setCellValueFactory(flight -> new ReadOnlyObjectWrapper<LocalDateTime>(flight.getValue().getLandingTime().toLocalDateTime()));	
		fromCol.setCellValueFactory(flight -> {
			Airport origin = importControl.getInstance().getAirportByID(flight.getValue().getDepartureAirport().getAirportCode());
			String originAirport = origin.getCity() + ", " + origin.getCountry();
			return new ReadOnlyStringWrapper(originAirport);
		});
		
		toCol.setCellValueFactory(flight -> {
			Airport destination = importControl.getInstance().getAirportByID(flight.getValue().getLandingAirport().getAirportCode());
			String destinationAirport = destination.getCity() + ", " + destination.getCountry();
			return new ReadOnlyStringWrapper(destinationAirport);
		});
		
		statusCol.setCellValueFactory(flight -> new ReadOnlyObjectWrapper<FlightStatus>(flight.getValue().getStatus()));
		
		airplaneIDCol.setCellValueFactory(flight -> new ReadOnlyObjectWrapper<String>(flight.getValue().getAirplane().getTailNumber()));	
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
				updateMessage="Unfornatnatly we had to update your order Details. \nThis is the new details: \n" +selectCustomer.getFlight().getFlightNum();
			}
		
			message = "Dear " + selectCustomer.getCustomer().getFirstName() + ",\n"+updateMessage+"\nThis is your flight recommendations if you would like to choose a new flight: \n";
			
			ArrayList<Flight> flights = importControl.recommendUserNewDetails(selectCustomer);
			System.out.println(message);
		
			for(Flight f :flights)
			{
				message+= f+"\n";
			}
			
			if(flights.isEmpty())
			{
				message="Dear " + selectCustomer.getCustomer().getFirstName() + ",\n"+updateMessage+"\nUnfortently we don't have recommendations for you :( \n";
			}
		}
		else
		{
			message="Please select custumer to call";
		}
			
		Alert alert = new Alert(AlertType.INFORMATION, "" + message);
		alert.setHeaderText("Alternative flight recommendations");
		alert.setTitle("Alternative flight recommendations");
		alert.showAndWait();
		messageLbl.setText(message);	
	}
	
	
	@FXML
	void Report(ActionEvent event) {
		JFrame reportFrame = ControlReport.getInstance().produceReport();
		reportFrame.setVisible(true);
	}
	
	

}
