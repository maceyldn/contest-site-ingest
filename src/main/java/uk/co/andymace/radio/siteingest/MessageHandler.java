package uk.co.andymace.radio.siteingest;


import java.io.BufferedOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Charsets;

public class MessageHandler implements NewMessageListener {

	private static boolean alreadyInstantiated = false;
	Connection conn = null;

	private static final Logger Logger = LogManager.getLogger(MessageHandler.class.getName());
	private myProperties properties;
	
	public MessageHandler (myProperties properties) 
	{
		Logger.info("Starting MessageHandler Controller");
		this.properties = properties;
		
		
		// This is a quick and dirty check that we're definitely a singleton.
		if (alreadyInstantiated == true) 
		{ 
			Logger.error("Looks like you're trying to spin up two Message Handler Controllers. Not good. Exiting.");
			System.exit(-1);
		}
		
		
		
		try {
			String host = properties.getProperty("mysqlhost");
			String database = properties.getProperty("mysqldatabase");
		    String user = properties.getProperty("mysqluser");
		    String pass = properties.getProperty("mysqlpass");
		    
			conn =
		       DriverManager.getConnection("jdbc:mysql://"+host+"/"+database+"?" +
		                                   "user="+user+"&password="+pass);

		    // Do something with the Connection
		    Logger.info("Connected to DB");

		   
		} catch (SQLException ex) {
		    // handle any errors
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		}
		

		alreadyInstantiated = true;
	}
	

	@Override
	public void newEventFired(String message, String sending_ip) {
//		Logger.debug("SEND: " + message);
		
		MinosADIFMessage msg = ParseMessage(message);
		try {
			WriteMessageToDB(msg, sending_ip);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	private void WriteMessageToDB(MinosADIFMessage msg, String sending_ip) throws SQLException {
		Statement st=conn.createStatement();
        st.executeUpdate("INSERT INTO `qsos` (`qso_date`, `time_on`, `band`, `freq`, `station_callsign`, `operator`, `my_gridsquare`, `ituz`, `cqz`, `mode`, `callsign`, `rst_sent`, `stx`, `stx_string`, `rst_rcvd`, `srx`, `srx_string`, `gridsquare`, `tx_pwr`, `qso_pts`, `qso_compl`, `sending_ip`) VALUES ('"+msg.qso_date+"', '"+msg.time_on+"', '"+msg.band+"', '"+msg.freq+"', '"+msg.station_callsign+"', '"+msg.operator+"', '"+msg.my_gridsquare+"', '"+msg.ituz+"', '"+msg.cqz+"', '"+msg.mode+"', '"+msg.call+"', '"+msg.rst_sent+"', '"+msg.stx+"', '"+msg.stx_string+"', '"+msg.rst_rcvd+"', '"+msg.srx+"', '"+msg.srx_string+"', '"+msg.gridsquare+"', '"+msg.tx_pwr+"', '"+msg.qso_pts+"', '"+msg.qso_compl+"', '"+sending_ip+"')");
        Logger.info("Added to DB");		
	}


	private MinosADIFMessage ParseMessage(String message) {
		
		if (message.startsWith("Exported by Minos VHF logging"))
		{
			String msg = null;
			int pos = message.indexOf("<QSO_DATE");
			if (pos != -1)
			{
				msg = message.substring(pos, message.length());
			}
			
			if (msg != null)
			{

				MinosADIFMessage fs = new MinosADIFMessage();
				fs.parse(msg);
				
				return fs;
		}
			else {
				Logger.info("Recieved a lot of old crap");
			}
		}
		return null;
	}
	
}
