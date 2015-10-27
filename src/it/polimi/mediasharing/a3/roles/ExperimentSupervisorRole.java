package it.polimi.mediasharing.a3.roles;

import it.polimi.mediasharing.activities.MainActivity;
import it.polimi.mediasharing.sockets.Client;
import it.polimi.mediasharing.sockets.Server;
import it.polimi.mediasharing.util.FileUtil;

import java.io.IOException;

import a3.a3droid.A3Message;
import a3.a3droid.A3SupervisorRole;
import android.os.Environment;

public class ExperimentSupervisorRole extends A3SupervisorRole {

	private boolean startExperiment;
	private Server server;
	private Client client;
	
	public ExperimentSupervisorRole() {
		super();		
	}

	@Override
	public void onActivation() {
		client = new Client();
		startFileServer();
		startExperiment = true;		
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
				//message.object = lcat + "#" + (String)message.object;
				channel.sendBroadcast(message);
				break;
				
			case MainActivity.MEDIA_DATA_SHARE:
				showOnScreen("Persisting the MC locally");
				String response [] = ((String)message.object).split("#");
				remoteAddress = response[0].replaceAll("/|:\\d*", "");
				FileUtil.storeFile(response[1], Environment.getExternalStorageDirectory() + "/a3droid/image.jpg");
				try {					
					client.sendMessage(remoteAddress, 4444, MainActivity.MCR, "");
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
				
			case MainActivity.START_EXPERIMENT:
				if(startExperiment){
					startExperiment = false;
					channel.sendBroadcast(message);
				}
				else
					startExperiment = true;
				
				break;
			}
	}
}