package dxat.project.draft.graphdb;


public class testing {

	public static void main(String[] args) {
		/**
		 * Test1: 2 sw linked
		 */
		//Creating a collection of interfaces
		InterfacesCollection ifaceCollection0 = new InterfacesCollection();
		InterfacesCollection ifaceCollection1 = new InterfacesCollection();
		InterfacesCollection ifaceCollection2 = new InterfacesCollection();
		InterfacesCollection ifaceCollection3 = new InterfacesCollection();

		//Populating a iface collection for sw0
		for (int i=0;i<5;i++){
			Interface iface = new Interface();
			iface.setInventoryId("SW-0:" + i);
			iface.setMac("00:1e:68:bf:81:0"+ i);
			iface.setPortId(i);
			iface.setCurrentSpeed(100);
			iface.setEnabled(true);
			ifaceCollection0.addInterface(iface);
		}
		//Populating a iface collection for sw1
		for (int i=0; i<5;i++){
			Interface iface1 = new Interface();
			iface1.setInventoryId("SW-1:" + i);
			iface1.setMac("00:1e:68:bf:81:1"+ i);
			iface1.setPortId(i);
			iface1.setCurrentSpeed(100);
			iface1.setEnabled(true);
			ifaceCollection1.addInterface(iface1);		
		}
		
		//Populating a iface collection for sw2
		for (int i=0;i<5;i++){
			Interface iface = new Interface();
			iface.setInventoryId("SW-2:" + i);
			iface.setMac("00:1e:68:bf:81:0"+ i);
			iface.setPortId(i);
			iface.setCurrentSpeed(100);
			iface.setEnabled(true);
			ifaceCollection0.addInterface(iface2);
		}
		//Populating a iface collection for sw3
		for (int i=0; i<5;i++){
			Interface iface1 = new Interface();
			iface1.setInventoryId("SW-3:" + i);
			iface1.setMac("00:1e:68:bf:81:1"+ i);
			iface1.setPortId(i);
			iface1.setCurrentSpeed(100);
			iface1.setEnabled(true);
			ifaceCollection1.addInterface(iface3);		
		}
		
		//Creating a switch collection
		SwitchCollection swCollection = new SwitchCollection();
 		
		for (int i=0;i<4;i++){
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
		
		//Creating a host instances
		//Host 0 parameters
		List<String> ip0 = new List<String>();
		List<Short> vlan0= new List<Short>();
		List<String> swId0 = new List<String>();
		List<Integer> portId0 = new List<Integer();
		
		ip0.add("192.168.8.0");
		vlan0.add(1);
		swId0.add("SW-0");
		portId0.add(1);
		
		//Host 1 parameters
		List<String> ip1 = new List<String>();
                List<Short> vlan1= new List<Short>();
                List<String> swId1 = new List<String>();
                List<Integer> portId1 = new List<Integer();

                ip1.add("192.168.8.1");
                vlan1.add(1);           
                swId1.add("SW-0");      
                portId1.add(2); 		
		
		//Host 2 parameters
		List<String> ip2 = new List<String>();
                List<Short> vlan2= new List<Short>();
                List<String> swId2 = new List<String>();
                List<Integer> portId2 = new List<Integer();

                ip2.add("192.168.8.2");
                vlan2.add(1);           
                swId2.add("SW-3");      
                portId2.add(4); 	

		//Host 3 parameters
		List<String> ip3 = new List<String>();
                List<Short> vlan3 = new List<Short>();
                List<String> swId3 = new List<String>();
                List<Integer> portId3 = new List<Integer();

                ip3.add("192.168.8.3");
                vlan3.add(1);           
                swId3.add("SW-3");      
                portId3.add(3); 
		
		//Host0 instance	
		Host pc0 = new Host();
		pc0.setHostId("PC-0");
		pc0.setDhcpName("162.168.8.8");
		pc0.setMac("84:2b:2b:ab:7f:40");
		pc0.setIpv4(ip0)
		pc0.setVlan(vlan0)
		pc0.setSwId(swId0)
		pc0.setPortId(portId0) 
		
		//Host1 instance
		Host pc1 = new Host();
                pc1.setHostId("PC-1");
                pc1.setDhcpName("162.168.8.8");
                pc1.setMac("84:2b:2b:ab:7f:41");
                pc1.setIpv4(ip1)
                pc1.setVlan(vlan1)
                pc1.setSwId(swId1)
                pc1.setPortId(portId1)


		//Host2 instance
		Host pc2 = new Host();
		pc2.setHostId("PC-2");
                pc2.setDhcpName("162.168.8.8");
                pc2.setMac("84:2b:2b:ab:7f:42");
                pc2.setIpv4(ip2)
                pc2.setVlan(vlan2)
                pc2.setSwId(swId2)
                pc2.setPortId(portId2)
		
		//Host3 instance
                Host pc3 = new Host();
		pc3.setHostId("PC-3");
                pc3.setDhcpName("162.168.8.8");
                pc3.setMac("84:2b:2b:ab:7f:40");
                pc3.setIpv4(ip3)
                pc3.setVlan(vlan3)
                pc3.setSwId(swId3)
                pc3.setPortId(portId3)
			
	
		//Creating a LINK instances
		//Creatin LINK SW-0_SW-1
		Link lnk1 = new Link();
		lnk.setSrcSwitch("SW-0");
		lnk.setSrcPort(1);
		lnk.setDstSwitch("SW-1");
		lnk.setDstPort(1);
		lnk.setInventoryId("SW-0:1_SW-1:1");
		lnk.setType("");
		
		//Creatin LINK SW-0_SW-2
                Link lnk2 = new Link();
                lnk.setSrcSwitch("SW-0");
                lnk.setSrcPort(1);
                lnk.setDstSwitch("SW-2");
                lnk.setDstPort(1);
                lnk.setInventoryId("SW-0:1_SW-1:1");
                lnk.setType("");

		//Creatin LINK SW-1_SW-2
                Link lnk3 = new Link();
                lnk.setSrcSwitch("SW-0");
                lnk.setSrcPort(1);
                lnk.setDstSwitch("SW-1");
                lnk.setDstPort(1);
                lnk.setInventoryId("SW-0:1_SW-1:1");
                lnk.setType("");
		
		//Creatin LINK SW-1_SW-3
                Link lnk4 = new Link();
                lnk.setSrcSwitch("SW-0");
                lnk.setSrcPort(1);
                lnk.setDstSwitch("SW-1");
                lnk.setDstPort(1);
                lnk.setInventoryId("SW-0:1_SW-1:1");
                lnk.setType("");

		//Creatin LINK SW-2_SW-3
                Link lnk5 = new Link();
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
		System.out.println("name collection 0: " + swCollection.getDevices().get(0).getInventoryId());
		//Creating command --> add sw1
		Command cmd1 = new Command();
		cmd1.setEvent("ADD_SWITCH");
		cmd1.setObject("here comes object serialized");
		cmd1.setSource("SWITCH");
		cmd1.setSwitch(swCollection.getDevices().get(1));
		
		//Creating command --> add LINK
		Command cmd2 = new Command();
		cmd2.setEvent("ADD_LINK");
		cmd2.setObject("here comes object serialized");
		cmd2.setSource("LINK");
		
		SingletonNode singleton = SingletonNode.getInstance();
		//Executing command add sw0
		//singleton.execCommand("Here comes ", cmd0);
		
		//Executing command add sw1
		singleton.execCommand("Here comes ", cmd1);
		
		//Executing command add LINK
		//singleton.execCommand(cmd2);
		
		
		
	}

}
