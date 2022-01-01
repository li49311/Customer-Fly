package util;

import java.net.URLDecoder;


public class Consts 
{
	private Consts() {
		throw new AssertionError();
	}
	
	
	protected static final String DB_FILEPATH = getDBPath();
	public static final String CONN_STR = "jdbc:ucanaccess://" + DB_FILEPATH + ";COLUMNORDER=DISPLAY";
	public static final String JDBC_STR = "net.ucanaccess.jdbc.UcanaccessDriver";
	
	
	public static final String SQL_SEL_FLIGHT ="SELECT tbl_Flight.FlightNum FROM tbl_Flight;";
	public static final String SQL_SEL_FLIGHTTICKET_BY_CUSTOMER = "call quer_tickets_by_customer(?)";
	public static final String SQL_SEL_CUSTOMERS_BY_FLIGHT = "call sql_customers_for_flight(?)";
	public static final String SQL_SEL_SEATS_BY_PLANE = "call quer_seats_by_plane(?)";
	public static final String SQL_SEL_SEATS_BY_CLASS = "call quer_seats_by_class(?)";
	public static final String SQL_SEL_ALL_SHOWS_RECOM = "call quer_recommend_New_Flights(?)";
	public static final String SQL_SEL_FLIGHT_BY_NUMBER = "call quer_flight_by_number(?)";
	public static final String SQL_SEL_AIRPORTS = "SELECT tbl_Airport.* FROM tbl_Airport";
	public static final String SQL_SEL_AIRPLANE = "SELECT tbl_Airplane.* FROM tbl_Airplane";
	
	public static final String SQL_AIRPLANE_EXIST = "call quer_is_airplane_exist(?)";
	public static final String SQL_AIRPORT_EXIST = "call quer_is_airport_exist(?)";
	
	public static final String SQL_INS_FLIGHT = "{ call quer_insert_flight(?,?,?,?,?,?,?,?) }";
	public static final String SQL_INS_AIRPLANE = "{ call quer_insert_airplane(?) }";
	public static final String SQL_INS_AIRPORT = "{ call quer_insert_airport(?,?,?) }";
	public static final String SQL_INS_SEAT = "{ call quer_insert_seat(?,?,?,?) }";
	
	public static final String SQL_UPDATE_AIRPORT = "{ call quer_update_airport(?,?,?) }";
	public static final String SQL_UPDATE_FLIGHT = "{ call quer_update_flight(?,?,?,?,?,?,?,?) }";

	public static final String TICKETS_IN_ORDER = "{ call quer_amount_of_tickets_in_order(?) }";
	public static final String SEAT_IS_AVAILABLE = "{ call quer_seat_is_avialable(?,?) }";
	public static final String FLIGHT_RECOMMENDATION = "{ call quer_recommendation(?,?,?,?,?,?) }";
	
/*---------------------------------------------------------------------------------------*/
	/**
	 * find the correct path of the DB file
     * @return the path of the DB file (from eclipse or with runnable file)
	 */
	private static String getDBPath() {
		try {
			String path = Consts.class.getProtectionDomain().getCodeSource().getLocation().getPath();
			String decoded = URLDecoder.decode(path, "UTF-8");
			if (decoded.contains(".jar")) {
				decoded = decoded.substring(0, decoded.lastIndexOf('/'));
				return decoded + "/src/entity/db.accdb";
			} else {
				decoded = decoded.substring(0, decoded.lastIndexOf("bin/"));
				System.out.println(decoded);
				return decoded + "/src/entity/db.accdb";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	public enum Manipulation {
    	UPDATE, INSERT, DELETE;
    }
}


