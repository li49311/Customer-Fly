package boundary;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JFrame;
import control.ControlReport;
import control.importControl;
import entity.Airplane;
import entity.Airport;
import entity.Flight;
import entity.FlightTicket;
import entity.Seat;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;
import util.FlightStatus;
import util.SeatClass;

public class ImportUpdates {
	@FXML
	private AnchorPane popUpPane;
	
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
	
	private static List<FlightTicket> customersCanSeat = importControl.getCustmersCantSeat();
	
	
	
	
	@FXML
	public void initialize() {

		notifyButton.setDisable(true);
	  	
	}
	@FXML
	public void importJSON(ActionEvent event) {
		HashMap<Flight, ArrayList<FlightTicket>> flightsAndTickets = importControl.getAllNeedToCall();
		addToList(flightsAndTickets);
		
		
		notifyButton.setDisable(false);
	}
	
	//this method is for adding to listView the customers
	@FXML
	private void addToList(HashMap<Flight, ArrayList<FlightTicket>> flightsAndTickets)
	{
		
		ArrayList<Flight> problematicFlights = new ArrayList<>();
		ArrayList<FlightTicket> problematicTickets = new ArrayList<>();
		for(Flight flight: flightsAndTickets.keySet()) {
			problematicFlights.add(flight);
			problematicTickets.addAll(flightsAndTickets.get(flight));
		}
		
		
		CustomerList.setItems(FXCollections.observableArrayList(problematicTickets));	
					
		//fill table
		
		allFlightsTable.getItems().addAll(problematicFlights);
		
		flightIDCol.setCellValueFactory(flight -> new ReadOnlyObjectWrapper<String>(flight.getValue().getFlightNum()));
		departureCol.setCellValueFactory(flight -> new ReadOnlyObjectWrapper<LocalDateTime>(flight.getValue().getDepartureTime().toLocalDateTime()));
		landingCol.setCellValueFactory(flight -> new ReadOnlyObjectWrapper<LocalDateTime>(flight.getValue().getLandingTime().toLocalDateTime()));	
		fromCol.setCellValueFactory(flight -> {
			Airport origin = flight.getValue().getDepartureAirport();
			String originAirport = origin.getCity() + ", " + origin.getCountry();
			return new ReadOnlyStringWrapper(originAirport);
		});
		
		toCol.setCellValueFactory(flight -> {
			Airport destination = flight.getValue().getLandingAirport();
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
		ArrayList<Flight> flights = null;
		messageLbl.setText("");
		String message;
		String updateMessage;
		FlightTicket selectCustomer = (FlightTicket) CustomerList.getSelectionModel().getSelectedItem();
		if(selectCustomer != null) // select from list
		{	
			//TODO Add a test that checks whether the flight is also in the "canceled" status
			if(customersCanSeat.contains(selectCustomer) || selectCustomer.getFlight().getStatus().equals(FlightStatus.cancelled))
			{//if the flight is canceled 
				updateMessage="Unfornatnatly we had to cancel your order";			
			}
			else
			{
				updateMessage="Unfornatnatly we had to update your order Details for flight: " + selectCustomer.getFlight().getFlightNum();
			}
		
			message = "Dear " + selectCustomer.getCustomer().getFirstName() + ",\n"+updateMessage+"\nThis is your flight recommendations if you would like to choose a new flight: \n";
			
			flights = importControl.recommendUserNewDetails(selectCustomer);
			System.out.println(flights);
			System.out.println(message);
			
			if(flights.isEmpty())
			{
				message="Dear " + selectCustomer.getCustomer().getFirstName() + ",\n"+updateMessage+"\nUnfortently we don't have recommendations for you :( \n";
			}
		}
		else
		{
			message="Please select custumer to call";
		}	
		
		Stage newStage = new Stage();
		VBox comp = new VBox();
		comp.setPrefHeight(600);
		comp.setPrefWidth(400);
		Label nameField = new Label(message);
		nameField.setFont(new Font("Arial", 18));
		TableView<Flight> tableRecommendation = new TableView<Flight>();
		tableRecommendation.setPrefWidth(400);
		tableRecommendation.setPrefHeight(200);
		TableColumn<Flight, String> flightNum = new TableColumn<>("FlightID");;
		TableColumn<Flight, LocalDateTime> departureTime = new TableColumn<>("DepartureTime");;
		TableColumn<Flight, LocalDateTime> landingTime = new TableColumn<>("LandingTime");;
		TableColumn<Flight, String> departureAirport = new TableColumn<>("DepartureAirport");;
		TableColumn<Flight, String> landingAirport = new TableColumn<>("LandingAirport");;
		
		flightNum.setCellValueFactory(flight -> new ReadOnlyObjectWrapper<String>(flight.getValue().getFlightNum()));
		departureTime.setCellValueFactory(flight -> new ReadOnlyObjectWrapper<LocalDateTime>(flight.getValue().getDepartureTime().toLocalDateTime()));
		landingTime.setCellValueFactory(flight -> new ReadOnlyObjectWrapper<LocalDateTime>(flight.getValue().getLandingTime().toLocalDateTime()));	
		departureAirport.setCellValueFactory(flight -> {
			Airport origin = flight.getValue().getDepartureAirport();
			String originAirport = origin.getCity() + ", " + origin.getCountry();
			return new ReadOnlyStringWrapper(originAirport);
		});
		
		landingAirport.setCellValueFactory(flight -> {
			Airport destination = flight.getValue().getLandingAirport();
			String destinationAirport = destination.getCity() + ", " + destination.getCountry();
			return new ReadOnlyStringWrapper(destinationAirport);
		});
		
		tableRecommendation.getColumns().addAll(flightNum, departureTime, landingTime, departureAirport, landingAirport);
		tableRecommendation.setItems(FXCollections.observableArrayList(flights));	
		

		comp.getChildren().add(new Label());
		comp.getChildren().add(nameField);
		comp.getChildren().add(new Label());
		if(!flights.isEmpty())
			comp.getChildren().add(tableRecommendation);
		comp.getChildren().add(new Label());
		comp.getChildren().add(new Label("This message sent to: " + selectCustomer.getCustomer().getEmail()));
		Scene stageScene = new Scene(comp, 630, 400);
		newStage.setTitle("Alternative flight recommendations");
		newStage.setScene(stageScene);
		newStage.show();
	}
	
	
	@FXML
	void Report(ActionEvent event) {
		JFrame reportFrame = ControlReport.getInstance().produceReport();
		reportFrame.setVisible(true);
	}
	
}
