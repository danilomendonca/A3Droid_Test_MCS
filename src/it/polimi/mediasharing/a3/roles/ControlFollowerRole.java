package it.polimi.mediasharing.a3.roles;

import it.polimi.mediasharing.activities.MainActivity;
import a3.a3droid.A3FollowerRole;
import a3.a3droid.A3Message;

/**
 * This class is the role the followers of "control" group plays.
 * @author Danilo F.M.
 *
 */
public class ControlFollowerRole extends A3FollowerRole {

	private int runningExperiment;
	private Integer launchedGroups;
	
	public ControlFollowerRole() {
		super();
	}

	@Override
	public void onActivation() {
		runningExperiment = 0;		
	}

	@Override
	public void logic() {
		showOnScreen("[CtrlFolRole]");
		channel.sendToSupervisor(new A3Message(MainActivity.NEW_PHONE, node.getUUID()));
		active = false;
	}

	@Override
	public void receiveApplicationMessage(A3Message message) {
		
		switch(message.reason){
			case MainActivity.CREATE_GROUP:
				synchronized(this){
					try {
						wait(2000);
					} catch (InterruptedException e) {}
				}
				String[] splittedObject = ((String)message.object).split("_");
				runningExperiment = Integer.valueOf(splittedObject[0]);
				launchedGroups = Integer.valueOf(splittedObject[1]);
				node.connect(MainActivity.EXPERIMENT_PREFIX + message.object, false, true);
				break;
				
			case MainActivity.STOP_EXPERIMENT:
	
				showOnScreen("--- STOP_EXPERIMENT: ATTENDERE 10s CIRCA ---");
				
				for(int i = 1; i <= launchedGroups; i ++){
					if(!node.isSupervisor(MainActivity.EXPERIMENT_PREFIX + runningExperiment + "_" + i))
						node.disconnect(MainActivity.EXPERIMENT_PREFIX + runningExperiment + "_" + i, true);
				}
							
				synchronized(this){
					try {
						wait(10000);
					} catch (InterruptedException e) {}
				}
	
				showOnScreen("--- DISCONNESSIONE SUPERVISORI IN CORSO ---");
				
				for(int i = 1; i <= launchedGroups; i ++)
					node.disconnect(MainActivity.EXPERIMENT_PREFIX + runningExperiment + "_" + i, true);
				
				showOnScreen("--- ESPERIMENTO TERMINATO ---");
				break;
		}
	}
}
