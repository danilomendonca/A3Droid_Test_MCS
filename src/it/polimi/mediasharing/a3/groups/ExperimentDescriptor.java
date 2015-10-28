package it.polimi.mediasharing.a3.groups;

import it.polimi.mediasharing.a3.roles.ReceiverFollowerRole;
import it.polimi.mediasharing.a3.roles.ReceiverSupervisorRole;
import a3.a3droid.GroupDescriptor;

public class ExperimentDescriptor extends GroupDescriptor {

	public ExperimentDescriptor() {
		super("A3Test3", ReceiverSupervisorRole.class.getName(), ReceiverFollowerRole.class.getName());
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getSupervisorFitnessFunction() {
		// TODO Auto-generated method stub
		return 0;
	}

	/*@Override
	public int getSupervisorFitnessFunction() {
		// TODO Auto-generated method stub
		return 0;
	}*/

}
