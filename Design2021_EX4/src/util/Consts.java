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
	public static final String SQL_SEL_CUSTOMERS_BY_FLIGHT = "call sql_customers_for_flight(?)";
	public static final String SQL_SEL_SEATS_BY_PLANE = "call quer_seats_by_plane(?)";
	
	public static final String SQL_AIRPLANE_EXIST = "call quer_is_airplane_exist(?)";
	public static final String SQL_AIRPORT_EXIST = "call quer_is_airport_exist(?)";

	
	
	
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


