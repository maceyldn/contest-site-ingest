package uk.co.andymace.radio.siteingest;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.net.server.UdpSocketServer;


public class NetworkThread implements Runnable 
{

	private static final Logger logger = LogManager.getLogger(NetworkThread.class.getName());

	private InetAddress ni;
	private DatagramSocket socket;
	protected byte[] buf = new byte[6144];
	//private InetAddress group;
	private List<NewMessageListener> listeners = new ArrayList<NewMessageListener>();
	
		
	public NetworkThread (InetAddress multicastAddress, int multicastPort, InetAddress sourceip )
	{
		logger.debug("Instatiating a MulticastListenerThread");
		try {
			 
			 socket = new DatagramSocket(multicastPort, sourceip);
			//group = multicastAddress;
           
			
			if (sourceip == null)
			{
				logger.fatal("Cannot bind to network interface");
				System.exit(0);
			}
			
			//socket.setNetworkInterface(ni);
			//socket.joinGroup(group); 
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	@Override
	public void run() 
	{		
		logger.info("The NetworkThread thread is now spinning.");				
		while(true) 
		{
			listenformulticastpackets();
		}

	}
	
	
	private void listenformulticastpackets()
	{
		try 
		{
			  DatagramPacket packet = new DatagramPacket(buf, buf.length);
			  socket.receive(packet);
			
			  String caller = packet.getAddress().getHostAddress();
			  logger.info("Received message from: " + caller);
			  String received = new String(packet.getData(), 0, packet.getLength());
	          notifyListeners(received, caller);
 	           //logger.info(received);
 	           

		} catch (IOException e) {
			//Logger.error("Exception caught when trying to listen on IP:port ["+ localIPAddress+":"+ port + "] inetSocket: [" + inetSocket.getHostString() + "] or listening for a connection. It *might* be in use. Trying again in " + retryTimeInMS + " msecs. Error is ["+e.getMessage()+"]");
			try {
				Thread.sleep(1000);
			}
			catch (InterruptedException exception) { // We do this as part of graceful close-down
				logger.error("Received an InterruptedException. [" + exception.getMessage() + "]. Closing down the NetworkThread socket.");
				closeDownSocket();
			}
		}			
	}
	
	private void closeDownSocket() 
	{
		 //			socket.leaveGroup(group);
		 socket.close();
	    
		
	}
	
	private void notifyListeners(String pcstring, String sending_ip) {
        for (NewMessageListener listener : listeners) {
            listener.newEventFired(pcstring, sending_ip);
        }
    }

    public void addListener(NewMessageListener newListener) {
        listeners.add(newListener);
    }


	

}
