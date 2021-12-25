package control;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import entity.Airplane;
import entity.Airport;
import entity.Flight;

public class importControl {
	
	private static importControl instance;
	public static importControl getInstance() 
	{
		if (instance == null)
			instance = new importControl();
		return instance;
	}
	
	
	private static ArrayList<Flight> importFlight() {
		
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
						
				Flight flight = new Flight(flightNum, departureTime, landingTime, status, tailNumber, departureAirportCode, landingAirportCode);
				Airport from = new Airport(departureAirportCode, departureCity, departureCountry);
				Airport to = new Airport(landingAirportCode, landingCity, landingCountry);
				Airplane airplane = new Airplane(tailNumber);

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
		
		for(int i=14; i<15; i++)
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
}
