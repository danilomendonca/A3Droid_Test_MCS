package it.polimi.mediasharing.activities;

import it.polimi.mediasharing.a3.groups.ControlDescriptor;
import it.polimi.mediasharing.a3.groups.ExperimentDescriptor;
import it.polimi.mediasharing.a3.roles.ControlFollowerRole;
import it.polimi.mediasharing.a3.roles.ControlSupervisorRole;
import it.polimi.mediasharing.a3.roles.ReceiverFollowerRole;
import it.polimi.mediasharing.a3.roles.ReceiverSupervisorRole;
import it.polimit.mediasharing.R;

import java.util.ArrayList;

import a3.a3droid.A3DroidActivity;
import a3.a3droid.A3Message;
import a3.a3droid.A3Node;
import a3.a3droid.GroupDescriptor;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends A3DroidActivity{
	
	public static final String EXPERIMENT_PREFIX = "A3Test3_";
	public static final int NUMBER_OF_EXPERIMENTS = 32;
	
	public static final int CREATE_GROUP = 31;
	public static final int STOP_EXPERIMENT = 32;
	public static final int CREATE_GROUP_USER_COMMAND = 33;
	public static final int LONG_RTT = 34;
	public static final int STOP_EXPERIMENT_COMMAND = 35;
	public static final int PING = 36;
	public static final int PONG = 37;
	public static final int NEW_PHONE = 38;
	public static final int START_EXPERIMENT_USER_COMMAND = 39;
	public static final int START_EXPERIMENT = 40;
	public static final int DATA = 41;
	public static final int MEDIA_DATA = 42;
	public static final int MEDIA_DATA_SHARE = 43;
	public static final int RFS = 50;
	public static final int MC = 51;
	public static final int SID = 52;
	public static final int MCR = 53;
	public static final int RFSD = 54;
	
	private A3Node node;
	private EditText inText;
	private Handler toGuiThread;
	private Handler fromGuiThread;
	private EditText experiment;
	public static int runningExperiment;
	
	public static void setRunningExperiment(int runningExperiment) {
		MainActivity.runningExperiment = runningExperiment;
	}

	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		toGuiThread = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				
				inText.append(msg.obj + "\n");
			}
		};
		
		HandlerThread thread = new HandlerThread("Handler");
		thread.start();
		fromGuiThread = new Handler(thread.getLooper()){
			@Override
			public void handleMessage(Message msg){
				switch (msg.what) {
					case CREATE_GROUP_USER_COMMAND:
						node.sendToSupervisor(new A3Message(MainActivity.CREATE_GROUP_USER_COMMAND, experiment.getText().toString()), "control");
						break;
					case STOP_EXPERIMENT_COMMAND:
						node.sendToSupervisor(new A3Message(MainActivity.LONG_RTT, ""), "control");
						break;
					case START_EXPERIMENT_USER_COMMAND:
						node.sendToSupervisor(new A3Message(MainActivity.START_EXPERIMENT_USER_COMMAND, ""), "control");
						break;
					default:
						break;
				}					
			}
		};

		inText=(EditText)findViewById(R.id.oneInEditText);
		experiment = (EditText)findViewById(R.id.editText1);
		
		ArrayList<String> roles = new ArrayList<String>();
		roles.add(ControlSupervisorRole.class.getName());
		roles.add(ControlFollowerRole.class.getName());
		roles.add(ReceiverSupervisorRole.class.getName());
		roles.add(ReceiverFollowerRole.class.getName());
		
		
		ArrayList<GroupDescriptor> groupDescriptors = new ArrayList<GroupDescriptor>();
		groupDescriptors.add(new ControlDescriptor());
		groupDescriptors.add(new ExperimentDescriptor());
		
		node = new A3Node(this, roles, groupDescriptors);
		node.connect("control", false, true);
	}

	public void start1(View v){
		fromGuiThread.sendEmptyMessage(CREATE_GROUP_USER_COMMAND);
	}
	
	public void stopExperiment(View v){
		fromGuiThread.sendEmptyMessage(STOP_EXPERIMENT_COMMAND);
	}
	
	public void startExperiment(View v){
		fromGuiThread.sendEmptyMessage(START_EXPERIMENT_USER_COMMAND);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onDestroy(){
		node.disconnect("control", true);
		System.exit(0);
	}
	
	@Override
	public void showOnScreen(String message) {
		toGuiThread.sendMessage(toGuiThread.obtainMessage(0, message));
	}
}
