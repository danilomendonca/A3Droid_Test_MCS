package it.polimi.mediasharing.a3.roles;

import it.polimi.mediasharing.activities.MainActivity;
import it.polimi.mediasharing.util.FileUtil;
import a3.a3droid.A3FollowerRole;
import a3.a3droid.A3Message;
import android.os.Environment;

public class ExperimentFollowerRole extends A3FollowerRole{

	@SuppressWarnings("unused")
	private int currentExperiment;
	
	public ExperimentFollowerRole() {
		super();		
	}

	@Override
	public void onActivation() {
		currentExperiment = Integer.valueOf(getGroupName().split("_")[1]);
	}

	@Override
	public void logic() {
		showOnScreen("[" + getGroupName() + "_FolRole]");
		active = false;
	}

	@Override
	public void receiveApplicationMessage(A3Message message) {

		switch(message.reason){
			case MainActivity.MEDIA_DATA_SHARE:
				showOnScreen("Persisting the MC locally");
				String response [] = ((String)message.object).split("#");
				FileUtil.storeFile(response[1], Environment.getExternalStorageDirectory() + "/a3droid/image.jpg");
				break;
	
			default: break;
		}
	}
}
