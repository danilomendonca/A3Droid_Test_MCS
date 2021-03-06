package it.polimi.mediasharing.a3.roles;

import it.polimi.mediasharing.activities.MainActivity;
import it.polimi.mediasharing.util.FileUtil;

import java.io.File;

import a3.a3droid.A3FollowerRole;
import a3.a3droid.A3Message;
import android.os.Environment;

public class ReceiverFollowerRole extends A3FollowerRole{

	@SuppressWarnings("unused")
	private int currentExperiment;
	
	public ReceiverFollowerRole() {
		super();		
	}

	@Override
	public void onActivation() {
		currentExperiment = Integer.valueOf(getGroupName().split("_")[1]);
		channel.sendToSupervisor(new A3Message(MainActivity.NEW_PHONE, node.getUUID()));
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
				String remoteAddress = message.object.replaceAll("/|:\\d*", "");
				FileUtil.storeFile(message.bytes, Environment.getExternalStorageDirectory() + "/a3droid", "image.jpg");
				channel.sendToSupervisor(new A3Message(MainActivity.MCR, remoteAddress));
				break;
	
			default: break;
		}
	}
}
