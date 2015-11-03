package it.polimi.mediasharing.a3.roles;

import it.polimi.mediasharing.activities.MainActivity;
import it.polimi.mediasharing.sockets.Client;
import it.polimi.mediasharing.sockets.Server;
import it.polimi.mediasharing.util.FileUtil;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import a3.a3droid.A3Message;
import a3.a3droid.A3SupervisorRole;
import android.os.Environment;

public class ReceiverSupervisorRole extends A3SupervisorRole {

	private boolean startExperiment;
	private Server server;
	private Client client;
	private Set <String> group;
	private int dataToWait;
	
	public ReceiverSupervisorRole() {
		super();		
	}

	@Override
	public void onActivation() {
		client = new Client();
		startFileServer();
		startExperiment = true;		
		group = Collections.synchronizedSet(new HashSet<String>()); 
		group.add(node.getUUID());
		dataToWait = group.size();
	}	
	
	private void startFileServer(){
		try {
			server = new Server(4444, this);
			server.start();
		} catch (IOException e) {
			showOnScreen("Error creating the file server.");
		}
	}

	@Override
	public void logic() {
		showOnScreen("[" + getGroupName() + "_SupRole]");
		active = false;
	}

	@Override
	public void receiveApplicationMessage(A3Message message) {
		switch(message.reason){
		
			case MainActivity.NEW_PHONE:
				group.add(message.object);
				break;
			case MainActivity.RFS:
				showOnScreen("Received a Request for Sharing (RFS)");
				String [] msgs = ((String) message.object).split("#");
				String supervisorAddress = msgs[0].replaceAll("/|:\\d*", "");
				String remoteAddress = msgs[1].replaceAll("/|:\\d*", "");
				try {					
					client.sendMessage(remoteAddress, 4444, MainActivity.SID, supervisorAddress);
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			
			case MainActivity.MEDIA_DATA:
				showOnScreen("Sharing the Media Conent (MC) with followers");
				message.reason = MainActivity.MEDIA_DATA_SHARE;
				channel.sendBroadcast(message);
				break;
				
			case MainActivity.MEDIA_DATA_SHARE:
				showOnScreen("Persisting the MC locally");
				remoteAddress = ((String)message.object).replaceAll("/|:\\d*", "");
				FileUtil.storeFile(message.bytes, Environment.getExternalStorageDirectory() + "/a3droid", "image.jpg");
				receiveApplicationMessage(new A3Message(MainActivity.MCR, remoteAddress));
				break;
				
			case MainActivity.MCR:
				if(--dataToWait <= 0){
					try {					
						client.sendMessage((String) message.object, 4444, MainActivity.MCR, "");
						dataToWait = group.size();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				break;
				
			case MainActivity.START_EXPERIMENT:
				if(startExperiment){
					startExperiment = false;
					dataToWait = group.size();
					channel.sendBroadcast(message);
				}
				else
					startExperiment = true;
				break;
			}
	}
}
