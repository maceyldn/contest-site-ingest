package uk.co.andymace.radio.siteingest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MinosADIFMessage {

	public String qso_date;
	public String time_on;
	public String band;
	public String freq;
	public String station_callsign;
	public String operator;
	public String my_gridsquare;
	public String ituz;
	public String cqz;
	public String mode;
	public String call;
	public String rst_sent;
	public String stx;
	public String stx_string;
	public String rst_rcvd;
	public String srx;
	public String srx_string;
	public String gridsquare;
	public String tx_pwr;
	public String qso_pts;
	public String qso_compl;
	
	private static final Logger Logger = LogManager.getLogger(MinosADIFMessage.class.getName());
	
	public void parse(String message) {
		
		qso_date = PullValue(message, "<QSO_DATE");
		time_on = PullValue(message, "<TIME_ON");
		band = PullValue(message, "<BAND");
		freq = PullValue(message, "<FREQ");
		station_callsign = PullValue(message, "<STATION_CALLSIGN");
		operator = PullValue(message, "<OPERATOR");
		my_gridsquare = PullValue(message, "<MY_GRIDSQUARE");
		ituz = PullValue(message, "<ITUZ");
		cqz = PullValue(message, "<CQZ");
		mode = PullValue(message, "<MODE");
		call = PullValue(message, "<CALL");
		rst_sent = PullValue(message, "<RST_SENT");
		stx = PullValue(message, "<STX");
		stx_string = PullValue(message, "<STX_STRING");
		rst_rcvd = PullValue(message, "<RST_RCVD");
		srx = PullValue(message, "<SRX");
		srx_string = PullValue(message, "<SRZ_STRING");
		gridsquare = PullValue(message, "<GRIDSQUARE");
		tx_pwr = PullValue(message, "<TX_POWER");
		qso_pts = PullValue(message, "<QSO_PTS");
		qso_compl = PullValue(message, "<QSO_COMPL");
		
		Logger.info(this.toString());
		
	}

	private String PullValue(String message, String search) {
		String msg = null;
		int length = -1;
		int pos = message.indexOf(search);
		int start = pos+search.length();
		if (pos != -1)
		{
			length = Integer.parseInt(message.substring(start+1, start+2));
			return message.substring(start+3, start+3+length);
		}
		return msg;
	}

	@Override
	public String toString() {
		return "MinosADIFMessage [qso_date=" + qso_date + ", time_on=" + time_on + ", band=" + band + ", freq=" + freq
				+ ", station_callsign=" + station_callsign + ", operator=" + operator + ", my_gridsquare="
				+ my_gridsquare + ", ituz=" + ituz + ", cqz=" + cqz + ", mode=" + mode + ", call=" + call
				+ ", rst_sent=" + rst_sent + ", stx=" + stx + ", stx_string=" + stx_string + ", rst_rcvd=" + rst_rcvd
				+ ", srx=" + srx + ", srx_string=" + srx_string + ", gridsquare=" + gridsquare + ", tx_pwr=" + tx_pwr
				+ ", qso_pts=" + qso_pts + ", qso_compl=" + qso_compl + "]";
	}

	

}
