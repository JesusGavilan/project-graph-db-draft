package dxat.project.draft.graphdb;

import java.util.ArrayList;
import java.util.List;

public class SwitchCollection {
	private List<Switch> devices = null;
	
	public SwitchCollection (){
		devices = new ArrayList<Switch>();
	}

	public List<Switch> getDevices() {
		return devices;
	}

	public void setDevices(List<Switch> devices) {
		this.devices = devices;
	}
	
	public void addDevice (Switch dev){
		devices.add(dev);
	}
}
