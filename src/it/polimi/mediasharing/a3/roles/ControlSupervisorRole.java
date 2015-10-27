package it.polimi.mediasharing.a3.roles;

import it.polimi.mediasharing.activities.MainActivity;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import a3.a3droid.A3Message;
import a3.a3droid.A3SupervisorRole;

/**
 * This class is the role the supervisor of "control" group plays.
 * @author Danilo F.M.
 *
 */
public class ControlSupervisorRole extends A3SupervisorRole {

	private int runningExperiment;
	private Set<String> vmIds;
	private volatile int launchedGroups;
	
	public ControlSupervisorRole(){
		super();
	}
	
	@Override
	public void onActivation() {
		runningExperiment = 0;
		vmIds = Collections.synchronizedSet(new HashSet<String>());
		launchedGroups = 1;
	}	

	@Override
	public void logic() {
		showOnScreen("[CtrlSupRole]");
		node.sendToSupervisor(new A3Message(MainActivity.NEW_PHONE, ""), "control");		
		active = false;
	}

	@Override
	public void receiveApplicationMessage(A3Message message) {
		switch(message.reason){
			
			case MainActivity.NEW_PHONE:
				
				vmIds.add(message.senderAddress);
				showOnScreen("Telefoni connessi: " + vmIds.size());
				if(launchedGroups > 1)
					channel.sendUnicast(new A3Message(MainActivity.CREATE_GROUP, runningExperiment + "_" + launchedGroups), message.senderAddress);
				break;
				
			case MainActivity.CREATE_GROUP_USER_COMMAND:
				
				int temp = Integer.valueOf((String)message.object);
				
				if(runningExperiment != temp){
					runningExperiment = temp;
					launchedGroups = 1;
				}
				
				channel.sendBroadcast(new A3Message(MainActivity.CREATE_GROUP, runningExperiment + "_" + launchedGroups));
				break;
				
			case MainActivity.CREATE_GROUP:
				node.connect(MainActivity.EXPERIMENT_PREFIX + message.object, true, true);
				launchedGroups ++;
				break;						
		}	
	}
}
