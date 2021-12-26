package control;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;

import entity.*;
import util.Consts;


public class ControlReport 
{
	public static ControlReport instance;
	
	public static ControlReport getInstance() {
		if (instance == null)
			instance = new ControlReport();
		return instance;
	}
	

	/*public JFrame produceReport(Customer customer) 
	{
		Show s = events.getShow();
		Theater t = events.getTheater();
		try {
			Class.forName(Consts.JDBC_STR);
			try (Connection conn = DriverManager.getConnection(Consts.CONN_STR))
			{
				HashMap<String, Object> params = new HashMap<>();

				params.put("tId", t.getTheaterID());
				params.put("sId", s.getId());
				JasperPrint print = JasperFillManager.fillReport(
						getClass().getResourceAsStream("/boundary/FoodReport.jasper"),
						params, conn);
				JFrame frame = new JFrame("Show Report for " + LocalDate.now());
				frame.getContentPane().add(new JRViewer(print));
				frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
				frame.pack();
				return frame;
			}
			catch (Exception e) {
				e.printStackTrace();
			}

		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;

	}*/
}