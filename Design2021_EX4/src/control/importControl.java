package control;

import java.io.FileReader;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import entity.Airplane;
import entity.Airport;
import entity.Customer;
import entity.Flight;
import entity.FlightTicket;
import entity.Seat;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import util.Consts;
import util.FlightStatus;
import util.SeatClass;

public class importControl {
	
	public static ArrayList<FlightTicket> custmersCantSeat = new ArrayList<FlightTicket>();
	
	private static importControl instance;
	public static importControl getInstance() 
	{
		if (instance == null)
			instance = new importControl();
		return instance;
	}
	
	public static  ArrayList<FlightTicket> getCustmersCantSeat()
	{
		return custmersCantSeat;
	}
	
	
	private static ArrayList<Flight> importFlights() {
		
		ArrayList<Flight> ourJsonResult = new ArrayList<Flight>();

		try 
		{

			JSONObject obj = (JSONObject) new JSONParser().parse(new FileReader("json/Flights.json"));
			JSONArray jo = (JSONArray) obj.get("Flights") ; 
			Iterator<JSONObject> iterator = jo.iterator();
			while(iterator.hasNext())
			{
				JSONObject item = iterator.next();
			
				//Flight
				String flightNum = (String) item.get("FlightNum");	
				String departure = (String)item.get("DepartureTime");	
				Timestamp departureTime = saveTheDate(departure);
				String landing = (String)item.get("LandingTime");	
				Timestamp landingTime = saveTheDate(landing);
				String status = (String)item.get("Status");
				String tailNumber = (String)item.get("TailNumber");		
				String departureAirportCode = (String)item.get("DepartureAirportCode");
				String departureCity = (String)item.get("DepartureCity");
				String departureCountry = (String)item.get("DepartureCountry");
				String landingAirportCode = (String)item.get("LandingAirportCode");
				String landingCity = (String)item.get("DestinationCity");
				String landingCountry = (String)item.get("DestinationCountry");
				
				JSONArray jo2 = (JSONArray) item.get("SeatsInFlight");
				Iterator<JSONObject> iterator2 = jo2.iterator();
				HashSet<Seat> seatsInAirplane = new HashSet<Seat>();
				while(iterator2.hasNext())
				{
					JSONObject item2 = iterator2.next();
				
					//Flight
					int rowNum = Integer.parseInt((String)item2.get("Row"));
					String seatNum = (String)item2.get("Seat");	
					String seatClass = (String)item2.get("Class");	
					
					Seat seat = new Seat(rowNum, seatNum, seatClass, tailNumber);
					
					seatsInAirplane.add(seat);
				}
			
				Airport from = new Airport(departureAirportCode, departureCity, departureCountry);
				Airport to = new Airport(landingAirportCode, landingCity, landingCountry);
				Airplane airplane = new Airplane(tailNumber, seatsInAirplane);
				System.out.println(seatsInAirplane);
				
				Flight flight = new Flight(flightNum, departureTime, landingTime, FlightStatus.valueOf(status), airplane, from, to);

				ourJsonResult.add(flight);		
			}
		
		return ourJsonResult;
		
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//System.out.println(e.getMessage());
		} 
		return ourJsonResult;		
	}
	
	public static Timestamp saveTheDate(String date)
	{
		String year="";
		String month="";
		String day="";
		String hour="";
		String minutes="";
		
		char[] myStr = date.toCharArray();

		
		for(int i=0; i<4; i++)
		{
			year += myStr[i];
		}
		
		for(int i=5; i<7; i++)
		{
			month += myStr[i];
		}
		
		for(int i=8; i<10; i++)
		{
			day += myStr[i];
		}
		
		for (int i=11; i<13; i++)
			hour += myStr[i];
		
		for(int i=14; i<16; i++)
			minutes += myStr[i];
		
		int yearDate = Integer.parseInt(year);
		int monthDate = Integer.parseInt(month);
		int dayDate = Integer.parseInt(day);
		int hourDate = Integer.parseInt(hour);
		int minutesDate = Integer.parseInt(minutes);
		LocalDateTime date2 = LocalDateTime.of(yearDate, monthDate, dayDate, hourDate, minutesDate);
		Timestamp myTime = Timestamp.valueOf(date2);
		return myTime;		
	}
	
	
	//This method will give us all people we need to call them because of updates
		public static HashMap<Flight, ArrayList<FlightTicket>> getAllNeedToCall() {
			ArrayList<Flight> ourJsonResult = importFlights();
			ArrayList<Flight> toUpdate = new ArrayList<>();
			ArrayList<Flight> toInsert = new ArrayList<>();
			HashSet<String> flightIDs = getAllIDFlights(); //the ids that exist
			int counterUpdate = 0;
			int counterInsert = 0;

			for(Flight value : ourJsonResult)
			{
				String flightNum = value.getFlightNum();
				//check if the imported flights are exists
				Boolean isExist = flightIDs.contains(flightNum);

			try {	
				if(isExist) //need to update
				{
					//get the same flight from db
					Flight beforeUpdate = getFlightByNum(flightNum);
					if(!value.getAirplane().equals(beforeUpdate.getAirplane())) {
						if(!isExistAirplane(value.getAirplane()))
							insertAirplane(value.getAirplane());
					}
					if(!value.getDepartureAirport().equals(beforeUpdate.getDepartureAirport())) {
						if(!isExistAirport(value.getDepartureAirport()))
							insertAirport(value.getDepartureAirport());
					}
					if(!value.getLandingAirport().equals(beforeUpdate.getLandingAirport())) {
						if(!isExistAirport(value.getLandingAirport()))
							insertAirport(value.getLandingAirport());
					}					
					toUpdate.add(value);
					updateFlight(value);
					++counterUpdate;
					//System.out.println("update "+ value);
					custmersCantSeat.addAll(isProblemWithUpdateSeats(value)); // this will save all the problematic customers 				
				}
				else // insert
				{
					toInsert.add(value);
					insertFlight(value);
					++counterInsert;
				}
			}
			catch(ClassNotFoundException| SQLException e)
			{
				//System.out.println(e.getMessage());
				e.printStackTrace();
			}

			}
			Alert alert = new Alert(AlertType.INFORMATION, "We updated "+ counterUpdate+"\nWe inserted " + counterInsert);
			alert.setHeaderText("Success");
			alert.setTitle("Success");
			alert.showAndWait();
			
			HashMap<Flight, ArrayList<FlightTicket>> tb = new HashMap<>();
			for(Flight flight: toUpdate)
			{
				ArrayList<FlightTicket> allTicketPerFlight = getAllTicketByIDS(flight);
				tb.put(flight, allTicketPerFlight);
			}
			
			return tb;		
		}
		
		private static Flight getFlightByNum(String flightNum) {
			Flight flight = null;
			String flightID;
			String tailNumber;
			String departure;
			String destination;
			
			try {
				Class.forName(Consts.JDBC_STR);
				try (Connection conn = DriverManager.getConnection(util.Consts.CONN_STR);
						CallableStatement callst = conn.prepareCall(Consts.SQL_SEL_FLIGHT_BY_NUMBER))
						{
						int k=1;
						callst.setString(k++, flightNum);
						
						ResultSet rs = callst.executeQuery();
						while (rs.next()) 
						{
							int i =1;
							flightID = (rs.getString(i++));
							tailNumber = (rs.getString(i++));
							departure = (rs.getString(i++));
							destination = (rs.getString(i++));
							
							flight = new Flight(flightID, tailNumber, departure, destination);
						}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
			return flight;	
		}
		
		public static Airport getAirportByID(String id) {
			ArrayList<Airport> airports = new ArrayList<Airport>();
			airports.addAll(getairports());
			for(Airport ap : airports)
			{
				if(ap.getAirportCode().equals(id))
				{
					return ap;
				}
			}
			return null;
		}
		
		public static Airplane getAirplaneByID(String id) {
			ArrayList<Airplane> airplanes = new ArrayList<Airplane>();
			airplanes.addAll(getairplanes());
			for(Airplane ap : airplanes)
			{
				if(ap.getTailNumber().equals(id))
				{
					return ap;
				}
			}
			return null;
		}
		
		public static ArrayList<Airplane> getairplanes() {
			ArrayList<Airplane> airplaneList = new ArrayList<Airplane>();

			try {
				Class.forName(Consts.JDBC_STR);
				try (Connection conn = DriverManager.getConnection(util.Consts.CONN_STR);
						PreparedStatement stmt = conn.prepareStatement(util.Consts.SQL_SEL_AIRPLANE);
						ResultSet rs = stmt.executeQuery()) {
					while (rs.next()) {
						int i = 1;
						airplaneList.add(new Airplane(rs.getString(i++)));
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			return airplaneList;
		}

		public static ArrayList<Airport> getairports() {
			ArrayList<Airport> airportList = new ArrayList<Airport>();

			try {
				Class.forName(Consts.JDBC_STR);
				try (Connection conn = DriverManager.getConnection(util.Consts.CONN_STR);
						PreparedStatement stmt = conn.prepareStatement(util.Consts.SQL_SEL_AIRPORTS);
						ResultSet rs = stmt.executeQuery()) {
					while (rs.next()) {
						int i = 1;
						airportList.add(new Airport(rs.getString(i++),rs.getString(i++), rs.getString(i++)));
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			return airportList;	
		}

		public static HashSet<String> getAllIDFlights()
		{
			HashSet<String> flightIDs = new HashSet<String>();

			try {
				Class.forName(Consts.JDBC_STR);
				try (Connection conn = DriverManager.getConnection(util.Consts.CONN_STR);
						PreparedStatement stmt = conn.prepareStatement(util.Consts.SQL_SEL_FLIGHT);
						ResultSet rs = stmt.executeQuery()) {
					while (rs.next()) {
						int i = 1;
						flightIDs.add(rs.getString(i++));
					}
					
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			return flightIDs;
		}
		
		
		private static HashSet<FlightTicket> isProblemWithUpdateSeats(Flight flight) {
			HashMap<String, Integer> maxCapacity = getSeatsByClass(flight.getAirplane());	
			ArrayList<FlightTicket> arr = getAllTicketByIDS(flight);
			HashSet<FlightTicket> toReturn = new HashSet<FlightTicket>(); 
			HashMap<Integer, ArrayList<FlightTicket>> orders = new HashMap<>();
			HashMap<Integer, ArrayList<FlightTicket>> economyTickets = new HashMap<>();
			HashMap<Integer, ArrayList<FlightTicket>> buisnessTickets = new HashMap<>();
			HashMap<Integer, ArrayList<FlightTicket>> firstTickets = new HashMap<>();
			
			int economyCount = 0;
			int buisnessCount = 0;
			int firstCount = 0;
			
			for(FlightTicket ticket: arr) {
				if(orders.containsKey(ticket.getOrderNum()))
						orders.get(ticket.getOrderNum()).add(ticket);
				else {
					ArrayList<FlightTicket> ticketPerOrder = new ArrayList<>();
					ticketPerOrder.add(ticket);
					orders.put(ticket.getOrderNum(), ticketPerOrder);
				}
				if(ticket.getSeatClass().equals("economy")) {
					if(economyTickets.containsKey(ticket.getOrderNum()))
						economyTickets.get(ticket.getOrderNum()).add(ticket);
					else {
						ArrayList<FlightTicket> economy = new ArrayList<>();
						economy.add(ticket);
						economyTickets.put(ticket.getOrderNum(), economy);
					}
				}
				
				if(ticket.getSeatClass().equals("buisness")) {
					if(buisnessTickets.containsKey(ticket.getOrderNum()))
						buisnessTickets.get(ticket.getOrderNum()).add(ticket);
					else {
						ArrayList<FlightTicket> buisness = new ArrayList<>();
						buisness.add(ticket);
						buisnessTickets.put(ticket.getOrderNum(), buisness);
					}
				}
				if(ticket.getSeatClass().equals("first")) {
					if(firstTickets.containsKey(ticket.getOrderNum()))
						firstTickets.get(ticket.getOrderNum()).add(ticket);
					else {
						ArrayList<FlightTicket> first = new ArrayList<>();
						first.add(ticket);
						firstTickets.put(ticket.getOrderNum(), first);
					}
				}
			}
			
//			for(Integer orderNum: orders.keySet()) {
//				if(economyCount + economyTickets.get(orderNum).size() <= maxCapacity.get("economy")) 
//					economyCount += economyTickets.get(orderNum).size();
//				if(buisnessCount + buisnessTickets.get(orderNum).size() <= maxCapacity.get("buisness")) 
//					buisnessCount += buisnessTickets.get(orderNum).size();
//				if(firstCount + firstTickets.get(orderNum).size() <= maxCapacity.get("first")) 
//					firstCount += firstTickets.get(orderNum).size();
//				
//				if(economyCount > maxCapacity.get("economy") || buisnessCount > maxCapacity.get("buisness") || firstCount > maxCapacity.get("first")) {
//					toReturn.addAll(orders.get(orderNum));
//				}
//			}
			
			for(Integer order: orders.keySet()) {
				SeatClass seatClass = orders.get(order).get(0).getSeatClass();
				if(seatClass.equals("economy") && economyCount + orders.get(order).size() <= maxCapacity.get("economy")) 
					economyCount+=orders.get(order).size();
	
				if(seatClass.equals("buisness") && buisnessCount + orders.get(order).size() <= maxCapacity.get("buisness")) 
					buisnessCount+=orders.get(order).size();

				if(seatClass.equals("first") && firstCount + orders.get(order).size() <= maxCapacity.get("first")) 
					firstCount+=orders.get(order).size();

				if(economyCount + orders.get(order).size() > maxCapacity.get("economy"))
					toReturn.addAll(orders.get(order));
				
				if(buisnessCount + orders.get(order).size() > maxCapacity.get("buisness"))
					toReturn.addAll(orders.get(order));
				
				if(firstCount + orders.get(order).size() > maxCapacity.get("first"))
					toReturn.addAll(orders.get(order));
			}
			return toReturn;
		}

		private static HashMap<String, Integer> getSeatsByClass(Airplane airplane) {
			HashMap<String, Integer> seatsInPlane = new HashMap<String, Integer>();
			int firstCount;
			int buisnessCount;
			int economyCount;
			
			try {
				Class.forName(Consts.JDBC_STR);
				try (Connection conn = DriverManager.getConnection(util.Consts.CONN_STR);
						CallableStatement callst = conn.prepareCall(Consts.SQL_SEL_SEATS_BY_CLASS))
						{
						int k=1;
						callst.setString(k++, airplane.getTailNumber());
						
						
						ResultSet rs = callst.executeQuery();
						while (rs.next()) 
						{
							int i =1;
							economyCount = (rs.getInt(i++));
							buisnessCount = (rs.getInt(i++));
							firstCount = (rs.getInt(i++));
							seatsInPlane.put("economy", economyCount);
							seatsInPlane.put("buisness", buisnessCount);
							seatsInPlane.put("first", firstCount);
						}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
			return seatsInPlane;	
		}

		private static ArrayList<Seat> getSeatsByAirplane(Airplane airplane) {
			ArrayList<Seat> seatsInPlane = new ArrayList<Seat>();
			int rowNum;
			String seatNum;
			String seatClass;
			
			try {
				Class.forName(Consts.JDBC_STR);
				try (Connection conn = DriverManager.getConnection(util.Consts.CONN_STR);
						CallableStatement callst = conn.prepareCall(Consts.SQL_SEL_SEATS_BY_PLANE))
						{
						int k=1;
						callst.setString(k++, airplane.getTailNumber());
						
						
						ResultSet rs = callst.executeQuery();
						while (rs.next()) 
						{
							int i =1;
							rowNum = (rs.getInt(i++));
							seatNum = (rs.getString(i++));
							seatClass = (rs.getString(i++));
							
							Seat seat = new Seat(rowNum, seatNum, seatClass, airplane.getTailNumber());
							//System.out.println(tB);
							seatsInPlane.add(seat);
						}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
			return seatsInPlane;	
		}



		public static ArrayList<FlightTicket> getAllTicketByIDS(Flight flight)
		{
			String flightNum = flight.getFlightNum();
			ArrayList<FlightTicket> buyersList = new ArrayList<FlightTicket>();
			int passportNum;
			String fname;
			String lname;
			String mail;
			int orderNum;
			int ticketNum;
			String seatClass;
			
			try {
				Class.forName(Consts.JDBC_STR);
				try (Connection conn = DriverManager.getConnection(util.Consts.CONN_STR);
						CallableStatement callst = conn.prepareCall(Consts.SQL_SEL_CUSTOMERS_BY_FLIGHT))
						{
						int k=1;
						callst.setString(k++, flightNum);
						
						
						ResultSet rs = callst.executeQuery();
						while (rs.next()) 
						{
							int i =1;
							passportNum = (rs.getInt(i++));
							fname = (rs.getString(i++));
							lname = (rs.getString(i++));
							mail = (rs.getString(i++));
							orderNum = (rs.getInt(i++));
							ticketNum = (rs.getInt(i++));
							seatClass = (rs.getString(i++));
							
							Customer cust = new Customer(passportNum, fname, lname, mail);
							FlightTicket ticket = new FlightTicket(orderNum, ticketNum, SeatClass.valueOf(seatClass), cust, flight);;
							
							buyersList.add(ticket);
						}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			return buyersList;	
		}
		
		public static boolean updateFlight(Flight flight) throws ClassNotFoundException, SQLException
		{
			if(!updateAirport(flight.getDepartureAirport()) || !updateAirport(flight.getLandingAirport()))
			{
				return false;
			}	
			Class.forName(Consts.JDBC_STR);
			try (Connection conn = DriverManager.getConnection(util.Consts.CONN_STR);
					CallableStatement stmt =  conn.prepareCall(util.Consts.SQL_UPDATE_FLIGHT)){
				int i = 1;
				stmt.setTimestamp(i++,flight.getDepartureTime());
				stmt.setTimestamp(i++,flight.getLandingTime());
				stmt.setString(i++, flight.getStatus().toString());
				stmt.setString(i++, flight.getAirplane().getTailNumber());
				stmt.setString(i++, flight.getDepartureAirport().getAirportCode());
				stmt.setString(i++, flight.getLandingAirport().getAirportCode());
				stmt.setDate(i++, Date.valueOf(LocalDate.now()));
				stmt.setString(i++, flight.getFlightNum());
				
				stmt.executeUpdate();
			}
			return true;
		}
		
		//this method UPDATING new Show to DB
		private static boolean updateAirport(Airport airport) throws ClassNotFoundException, SQLException
		{
			Class.forName(Consts.JDBC_STR);
			try (Connection conn = DriverManager.getConnection(util.Consts.CONN_STR);
					CallableStatement stmt =  conn.prepareCall(util.Consts.SQL_UPDATE_AIRPORT)){
				int i = 1;
				stmt.setString(i++, airport.getCity());
				stmt.setString(i++, airport.getCountry());
				stmt.setString(i++, airport.getAirportCode());
				stmt.executeUpdate();		
			}
			return true;	
		}
		
		public static boolean insertFlight(Flight flight) throws ClassNotFoundException, SQLException
		{
			
			if(!isExistAirplane(flight.getAirplane()))
			{
				insertAirplane(flight.getAirplane());
			}
			
			if(!isExistAirport(flight.getDepartureAirport()))
			{
				insertAirport(flight.getDepartureAirport());
			}
			
			if(!isExistAirport(flight.getLandingAirport()))
			{
				System.out.println(flight.getLandingAirport().getAirportCode());
				insertAirport(flight.getLandingAirport());
			}
						
			//inserting to flight
			Class.forName(Consts.JDBC_STR);
			try (Connection conn = DriverManager.getConnection(util.Consts.CONN_STR);
					CallableStatement stmt =  conn.prepareCall(util.Consts.SQL_INS_FLIGHT)){
				int i = 1;
				stmt.setString(i++,flight.getFlightNum());
				stmt.setTimestamp(i++, flight.getDepartureTime());
				stmt.setTimestamp(i++, flight.getLandingTime());
				stmt.setString(i++, flight.getStatus().toString());
				stmt.setString(i++, flight.getAirplane().getTailNumber());
				stmt.setString(i++, flight.getDepartureAirport().getAirportCode());
				stmt.setString(i++, flight.getLandingAirport().getAirportCode());
				stmt.setDate(i++, Date.valueOf(LocalDate.now()));
				
				stmt.executeUpdate();
			}
			return true;	
		}
		
		private static boolean insertAirplane(Airplane plane) throws ClassNotFoundException, SQLException
		{		
			Class.forName(Consts.JDBC_STR);
			try (Connection conn = DriverManager.getConnection(util.Consts.CONN_STR);
					CallableStatement stmt =  conn.prepareCall(util.Consts.SQL_INS_AIRPLANE)){
				int i = 1;	
				stmt.setString(i++, plane.getTailNumber());
				stmt.executeUpdate();
				
			}
			
			ArrayList<Seat> seats = getSeatsByAirplane(plane);
			for(Seat s: plane.getSeats()) {
				if(!seats.contains(s))
					insertSeat(s);
			}
			return true;	
		}
		
		private static boolean insertSeat(Seat s) throws ClassNotFoundException, SQLException {
			Class.forName(Consts.JDBC_STR);
			try (Connection conn = DriverManager.getConnection(util.Consts.CONN_STR);
					CallableStatement stmt =  conn.prepareCall(util.Consts.SQL_INS_SEAT)){
				int i = 1;	
				stmt.setInt(i++, s.getRowNum());
				stmt.setString(i++, s.getSeatNum());
				stmt.setString(i++, s.getTailNumber());
				stmt.setString(i++, s.getSeatClass().toString());
				
				stmt.executeUpdate();
				
			}
			return true;	
			
		}

		private static boolean insertAirport(Airport airport) throws ClassNotFoundException, SQLException
		{
			Class.forName(Consts.JDBC_STR);
			try (Connection conn = DriverManager.getConnection(util.Consts.CONN_STR);
					CallableStatement stmt =  conn.prepareCall(util.Consts.SQL_INS_AIRPORT)){
				int i = 1;	
				stmt.setString(i++, airport.getAirportCode());
				stmt.setString(i++, airport.getCity());
				stmt.setString(i++, airport.getCountry());
				
				stmt.executeUpdate();
			}
			return true;	
		}

		private static boolean isExistAirplane(Airplane myAirplane)
		{
			String id = myAirplane.getTailNumber();
			String tailNum = "";
			try {
				Class.forName(Consts.JDBC_STR);
				try (Connection conn = DriverManager.getConnection(util.Consts.CONN_STR);
						CallableStatement callst = conn.prepareCall(Consts.SQL_AIRPLANE_EXIST))
						{
						int k=1;
						callst.setString(k++, id);
						
						ResultSet rs = callst.executeQuery();
						while (rs.next()) 
							tailNum = rs.getString(1);
						
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
			if(tailNum.isEmpty())
				return false;
			return true;
			
		}
		//this method get Theater and return if exist
		private static boolean isExistAirport(Airport myAirport)
		{
			String id = myAirport.getAirportCode();
			String airportCode = "";
			try {
				Class.forName(Consts.JDBC_STR);
				try (Connection conn = DriverManager.getConnection(util.Consts.CONN_STR);
						CallableStatement callst = conn.prepareCall(Consts.SQL_AIRPORT_EXIST))
						{
						int k=1;
						callst.setString(k++, id);
						
						ResultSet rs = callst.executeQuery();
						while (rs.next()) 
							airportCode = rs.getString(1);
						
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
			if(airportCode.equals(""))
				return false;
			return true;
		}
		
		
	//This method will notify the user on the new details of the show
	public static ArrayList<Flight> recommendUserNewDetails(FlightTicket customerFlightTicket)
	{		
			cancelledTicket(customerFlightTicket);
			Airport from = customerFlightTicket.getFlight().getDepartureAirport();
			Airport to = customerFlightTicket.getFlight().getLandingAirport();
			Timestamp oldFlightDate = customerFlightTicket.getFlight().getDepartureTime(); //because we want the oldDate				
			ArrayList<Flight> flightList = getAllRecomByParmWithoutSeats(oldFlightDate, from.getCity(), from.getCountry(), to.getCity(),
					to.getCountry(), customerFlightTicket.getFlight().getFlightNum());
			ArrayList<Flight> toReturn = new ArrayList<Flight>();
				
			for(Flight fl: flightList)
			{
				if(canBeThere(fl ,customerFlightTicket.getSeatClass()))
				{
					toReturn.add(fl);
				}
			}	
			return toReturn;					
	}
	
	private static boolean cancelledTicket(FlightTicket customerFlightTicket) {
		try {
			Class.forName(Consts.JDBC_STR);
			try (Connection conn = DriverManager.getConnection(util.Consts.CONN_STR);
					CallableStatement stmt =  conn.prepareCall(util.Consts.SQL_UPDATE_TICKET)){
				int i = 1;
				stmt.setInt(i++, customerFlightTicket.getOrderNum());
				stmt.setInt(i++, customerFlightTicket.getTicketNum());

				
				stmt.executeUpdate();
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	private static ArrayList<Flight> getFlightByUpdateDate() {
		ArrayList<Flight> flightList = new ArrayList<Flight>();
				
		try {
			Class.forName(Consts.JDBC_STR);
			try (Connection conn = DriverManager.getConnection(util.Consts.CONN_STR);
					PreparedStatement callst = conn.prepareStatement(Consts.SQL_FLIGHT_BY_UPDATE_DATE))
			{			
				ResultSet rs = callst.executeQuery();
				while (rs.next()) 
				{
					int i =1;
					flightList.add(new Flight(rs.getString(i++),rs.getTimestamp(i++), rs.getTimestamp(i++), FlightStatus.valueOf(rs.getString(i++)),
							getAirplaneByID(rs.getString(i++)),getAirportByID(rs.getString(i++)), getAirportByID(rs.getString(i++))));

				}

			} catch (SQLException e) {

				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return flightList;
	}
	
	//This method get airports and dates, then return the all flights between the parm
	private static ArrayList<Flight> getAllRecomByParmWithoutSeats(Timestamp oldFlightDate, String city,
			String country, String city2, String country2, String flightNumber) {
		
		ArrayList<Flight> FlightsList = new ArrayList<Flight>();
		
		String flightNum;
		Timestamp departureTime;
		Timestamp landingTime;
		Airport departureAirport;
		Airport landingAirport;
		Airplane airplane;
		
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		String timeFormat = format.format(oldFlightDate);
		
				
		try {
			Class.forName(Consts.JDBC_STR);
			try (Connection conn = DriverManager.getConnection(util.Consts.CONN_STR);
					CallableStatement callst = conn.prepareCall(Consts.SQL_FLIGHT_RECOMMENDATION))
			{
				int k=1;
				callst.setString(k++, timeFormat);
				callst.setString(k++, city);
				callst.setString(k++, country);
				callst.setString(k++, city2);
				callst.setString(k++, country2);
				callst.setString(k++, flightNumber);
			
				ResultSet rs = callst.executeQuery();
				while (rs.next()) 
				{
					int i =1;
					flightNum = (rs.getString(i++));
					departureTime = (rs.getTimestamp(i++));
					landingTime = (rs.getTimestamp(i++));
					
					departureAirport = getAirportByID(rs.getString(i++));
					landingAirport = getAirportByID(rs.getString(i++));
					airplane = getAirplaneByID(rs.getString(i++));
					
					Flight flight = new Flight(flightNum, departureTime, landingTime, airplane, departureAirport, landingAirport);

					FlightsList.add(flight); 
				}

			} catch (SQLException e) {

				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return FlightsList;
	}

	//this method will get flight, amount of tickets, and the seat type and return if someone can sit in this flight
	public static boolean canBeThere(Flight flight, SeatClass seatClass)
	{
		ArrayList<Seat> seats = getSeatsByAirplane(flight.getAirplane());
		ArrayList<Seat> seatsToReturn = seats;
		
		
		for (Seat seat: seats) {
			//If the seat is not from the desired class, delete it from the list
			if (seat.getSeatClass().equals(seatClass.toString())) {
				seatsToReturn.remove(seat);
			}
			
		}

		int availableSeatsByClass = seatsToReturn.size(); 
		
		if (availableSeatsByClass >= 1 )	 {
			//if the number of available seats is bigger then the amount we need
			return true;
		}
		
		return false;
	}

}
