package dxat.project.draft.graphdb;


public class testing {

	public static void main(String[] args) {
		/**
		 * Test1: 2 sw linked
		 */
		//Creating a collection of interfaces
		InterfacesCollection ifaceCollection0 = new InterfacesCollection();
		InterfacesCollection ifaceCollection1 = new InterfacesCollection();

		//Populating a iface collection for sw0
		for (int i=0;i<5;i++){
			Interface iface = new Interface();
			iface.setInventoryId("SW-0:" + i);
			iface.setMac("00:1e:68:bf:81:0"+ i);
			iface.setPortId(0);
			iface.setCurrentSpeed(100);
			iface.setEnabled(true);
			ifaceCollection0.addInterface(iface);
		}
		//Populating a iface collection for sw1
		for (int i=0; i<5;i++){
			Interface iface1 = new Interface();
			iface1.setInventoryId("SW-1:" + i);
			iface1.setMac("00:1e:68:bf:81:1"+ i);
			iface1.setPortId(0);
			iface1.setCurrentSpeed(100);
			iface1.setEnabled(true);
			ifaceCollection1.addInterface(iface1);		
		}
		
		//Creating a switch collection
		SwitchCollection swCollection = new SwitchCollection();
 		
		for (int i=0;i<2;i++){
			Switch sw = new Switch();
			sw.setInventoryId("SW-"+i);
			if (i==0) sw.setInterfaces(ifaceCollection0);
			else	sw.setInterfaces(ifaceCollection1);
			
			sw.setHardware("Microtik");
			sw.setManufacturer("RouterBOARD 750GL");
			sw.setDatapath("");
			sw.setNports(5);
			sw.setOfAddr("0:192.168.1."+i+":125");
			sw.setSerialNum("666" + i);
			sw.setSoftware("mikrotik os");
			sw.setType("");
			swCollection.addDevice(sw);
		}
		
		//Creating a LINK instance
		Link lnk = new Link();
		lnk.setSrcSwitch("SW-0");
		lnk.setSrcPort(1);
		lnk.setDstSwitch("SW-1");
		lnk.setDstPort(1);
		lnk.setInventoryId("SW-0:1_SW-1:1");
		lnk.setType("");
		
		//Creating command --> add sw0
		Command cmd0 = new Command();
		cmd0.setEvent("ADD_SWITCH");
		cmd0.setObject("here comes object serialized");
		cmd0.setSource("SWITCH");
		cmd0.setSwitch(swCollection.getDevices().get(0));
		
		//Creating command --> add sw1
		Command cmd1 = new Command();
		cmd0.setEvent("ADD_SWITCH");
		cmd0.setObject("here comes object serialized");
		cmd0.setSource("SWITCH");
		cmd0.setSwitch(swCollection.getDevices().get(1));
		
		//Creating command --> add LINK
		Command cmd2 = new Command();
		cmd2.setEvent("ADD_LINK");
		cmd2.setObject("here comes object serialized");
		cmd2.setSource("LINK");
		
		SingletonNode singleton = SingletonNode.getInstance();
		//Executing command add sw0
		singleton.execCommand(cmd0);
		
		//Executing command add sw1
		//singleton.execCommand(cmd1);
		
		//Executing command add LINK
		//singleton.execCommand(cmd2);
		
		
		
	}

}
