package control;

import java.io.FileReader;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import entity.Airplane;
import entity.Airport;
import entity.Customer;
import entity.Flight;
import entity.Seat;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import util.Consts;

public class importControl {
	
	public static ArrayList<Customer> custmersCantSeat = new ArrayList<Customer>();
	private static importControl instance;
	public static importControl getInstance() 
	{
		if (instance == null)
			instance = new importControl();
		return instance;
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
			
				Airport from = new Airport(departureAirportCode, departureCity, departureCountry);
				Airport to = new Airport(landingAirportCode, landingCity, landingCountry);
				Airplane airplane = new Airplane(tailNumber);
				
				Flight flight = new Flight(flightNum, departureTime, landingTime, status, airplane, from, to);

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
	//	System.out.println(year);
		//System.out.println(month);
	//	System.out.println(day);
		LocalDateTime date2 = LocalDateTime.of(yearDate, monthDate, dayDate, hourDate, minutesDate);
	//	System.out.println("date 222 " + date2);
		Timestamp myTime =Timestamp.valueOf(date2);
	//	System.out.println(myDate);
		return myTime;		
	}
	
	
	//This method will give us all people we need to call them because of updates
		public static ArrayList<Customer> getAllNeedToCall() {
			ArrayList<Flight> ourJsonResult = importFlights();
			ArrayList<Flight> toUpdate = new ArrayList<>();
			ArrayList<Flight> toInsert = new ArrayList<>();
			HashSet<String> flightIDs = getAllIDFlights(); //the ids that exist
			int counterUpdate = 0;
			int counterInsert = 0;

			for(Flight value : ourJsonResult)
			{
				String flightNum = value.getFlightNum();
				//System.out.println(value);
				
				List<String> myKey = new ArrayList<String>();
				myKey.add(flightNum);
				
				Boolean isExist = flightIDs.contains(myKey);

			try {	
				if(isExist) //update
				{
					//System.out.println("update "+ value);
					custmersCantSeat.addAll(isProblemWithUpdateSeats(value)); // this will save all the problematic customers 
					
				}
				else // insert
				{
					toInsert.add(value);
					insertNewShowInTheater(value);
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
			
			ArrayList <Customer> tb = new ArrayList<Customer>();
			for(Flight shIn: toUpdate)
			{
				tb.addAll(getAllTicketByIDS(shIn));
			}
			
			return tb;		
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
		
		
		private static ArrayList<Customer> isProblemWithUpdateSeats(Flight flight) {
			int maxCapacity = getMaxCapacity(flight.getAirplane());	
			ArrayList<Customer> arr = getAllTicketByIDS(flight);
			ArrayList<Customer> toReturn = new ArrayList<Customer>(); 
		
			for(int i = 0; i < arr.size(); i++)
			{
				if(i > maxCapacity)
					toReturn.add(arr.get(i));
			}
			return toReturn;
		}
		
		private static int getMaxCapacity(Airplane airplane) {
			ArrayList<Seat> seatsInPlane = new ArrayList<Seat>();
			int rowNum;
			String seatNum;
			
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
							
							Seat seat = new Seat(rowNum, seatNum, airplane.getTailNumber());
							//System.out.println(tB);
							seatsInPlane.add(seat);
						}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
			return seatsInPlane.size();	
		}


		public static ArrayList<Customer> getAllTicketByIDS(Flight flight)
		{
			String flightNum = flight.getFlightNum();
			ArrayList<Customer> buyersList = new ArrayList<Customer>();
			int passportNum;
			String fname;
			String lname;
			String mail;
			String phoneNum;
			
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
							phoneNum = (rs.getString(i++));
							
							Customer cust = new Customer(passportNum, fname, lname, mail, phoneNum);
							//System.out.println(tB);
							buyersList.add(cust);
						}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
			return buyersList;	
		}
		
		public static boolean insertNewShowInTheater(Flight flight) throws ClassNotFoundException, SQLException
		{
			
//			if(!isExistAirplane(flight.getAirplane()))
//			{
//				insertAirplane(b.getShow());
//			}
//			
//			if(!isExistAirport(flight.getDepartureAirport()))
//			{
//				insertAirport(b.getTheater());
//			}
//			
//			if(!isExistAirport(flight.getLandingAirport()))
//			{
//				insertAirport(b.getTheater());
//			}
//						
//			//inserting to flight
//			Class.forName(Consts.JDBC_STR);
//			try (Connection conn = DriverManager.getConnection(util.Consts.CONN_STR);
//					CallableStatement stmt =  conn.prepareCall(util.Consts.SQL_INS_SHOW_IN_THEATER)){
//				int i = 1;
//				stmt.setInt(i++, b.getTheater().getTheaterID());
//				stmt.setInt(i++, b.getShow().getId());
//				stmt.setInt(i++, b.getBasicTicketPrice());
//				stmt.setDate(i++, b.getDate());
//				stmt.setTime(i++, b.getStartHour());
//				stmt.setString(i++, b.getStatus());
//				stmt.setDate(i++, b.getUpdateDate());
//				stmt.executeUpdate();
//			}
//			return true;	
			return false;
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
			
			if(airportCode.isEmpty())
				return false;
			return true;
		}
}
