package dxat.project.draft.graphdb;
import java.util.ArrayList;
import java.util.List;

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
			Interface iface2 = new Interface();
			iface2.setInventoryId("SW-2:" + i);
			iface2.setMac("00:1e:68:bf:81:0"+ i);
			iface2.setPortId(i);
			iface2.setCurrentSpeed(100);
			iface2.setEnabled(true);
			ifaceCollection2.addInterface(iface2);
		}
		//Populating a iface collection for sw3
		for (int i=0; i<5;i++){
			Interface iface3 = new Interface();
			iface3.setInventoryId("SW-3:" + i);
			iface3.setMac("00:1e:68:bf:81:1"+ i);
			iface3.setPortId(i);
			iface3.setCurrentSpeed(100);
			iface3.setEnabled(true);
			ifaceCollection3.addInterface(iface3);		
		}
		
		//Creating a switch collection
		SwitchCollection swCollection = new SwitchCollection();
 		
		for (int i=0;i<4;i++){
			Switch sw = new Switch();
			sw.setInventoryId("SW-"+i);
			if (i==0) sw.setInterfaces(ifaceCollection0);
			if (i==1) sw.setInterfaces(ifaceCollection1);
			if (i==2) sw.setInterfaces(ifaceCollection2);
			if (i==3) sw.setInterfaces(ifaceCollection3);
			
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
		System.out.println("Posicion 0 de collection sw: " + swCollection.getDevices().get(0).getInventoryId());
		System.out.println("Posicion 1 de collection sw: " + swCollection.getDevices().get(1).getInventoryId());
		System.out.println("Posicion 2 de collection sw: " + swCollection.getDevices().get(2).getInventoryId());
		System.out.println("Posicion 3 de collection sw: " + swCollection.getDevices().get(3).getInventoryId());
		
		//Creating a host instances
		//Host 0 parameters

		
		List<String> ip0 = new ArrayList<String>();
		List<Short> vlan0= new ArrayList<Short>();
		List<String> swId0 = new ArrayList<String>();
		List<Integer> portId0 = new ArrayList<Integer>();
		
		ip0.add("192.168.8.0");
		vlan0.add((short) 1);
		swId0.add("SW-0");
		portId0.add(1);
		
		//Host 1 parameters
		List<String> ip1 = new ArrayList<String>();
        List<Short> vlan1= new ArrayList<Short>();
        List<String> swId1 = new ArrayList<String>();
        List<Integer> portId1 = new ArrayList<Integer>();

        ip1.add("192.168.8.1");
        vlan1.add((short)1);           
        swId1.add("SW-0");      
        portId1.add(2); 		
		
		//Host 2 parameters
		List<String> ip2 = new ArrayList<String>();
		List<Short> vlan2= new ArrayList<Short>();
        List<String> swId2 = new ArrayList<String>();
        List<Integer> portId2 = new ArrayList<Integer>();

        ip2.add("192.168.8.2");
        vlan2.add((short)1);           
        swId2.add("SW-3");      
        portId2.add(4); 	

		//Host 3 parameters
		List<String> ip3 = new ArrayList<String>();
        List<Short> vlan3 = new ArrayList<Short>();
        List<String> swId3 = new ArrayList<String>();
        List<Integer> portId3 = new ArrayList<Integer>();

        ip3.add("192.168.8.3");
        vlan3.add((short)1);           
        swId3.add("SW-3");      
        portId3.add(3); 
		
		//Host0 instance	
		Host pc0 = new Host();
		pc0.setHostId("PC-0");
		pc0.setDhcpName("162.168.8.8");
		pc0.setMac("84:2b:2b:ab:7f:40");
		pc0.setIpv4(ip0);
		pc0.setVlan(vlan0);
		pc0.setSwId(swId0);
		pc0.setPortId(portId0); 
		
		//Host1 instance
		Host pc1 = new Host();
        pc1.setHostId("PC-1");
        pc1.setDhcpName("162.168.8.8");
        pc1.setMac("84:2b:2b:ab:7f:41");
        pc1.setIpv4(ip1);
        pc1.setVlan(vlan1);
        pc1.setSwId(swId1);
        pc1.setPortId(portId1);


		//Host2 instance
		Host pc2 = new Host();
		pc2.setHostId("PC-2");
		pc2.setDhcpName("162.168.8.8");
        pc2.setMac("84:2b:2b:ab:7f:42");
        pc2.setIpv4(ip2);
        pc2.setVlan(vlan2);
        pc2.setSwId(swId2);
        pc2.setPortId(portId2);
		
        //Host3 instance
        Host pc3 = new Host();
        pc3.setHostId("PC-3");
        pc3.setDhcpName("162.168.8.8");
        pc3.setMac("84:2b:2b:ab:7f:40");
        pc3.setIpv4(ip3);
        pc3.setVlan(vlan3);
        pc3.setSwId(swId3);
        pc3.setPortId(portId3);

	
		//Creating a LINK instances
		//Creatin LINK SW-0_SW-1
		Link lnk1 = new Link();
		lnk1.setSrcSwitch("SW-0");
		lnk1.setSrcPort(3);
		lnk1.setDstSwitch("SW-1");
		lnk1.setDstPort(1);
		lnk1.setInventoryId("SW-0:3_SW-1:1");
		lnk1.setType("");

		//Creatin LINK SW-0_SW-2
		Link lnk2 = new Link();
		lnk2.setSrcSwitch("SW-0");
		lnk2.setSrcPort(4);
		lnk2.setDstSwitch("SW-2");
		lnk2.setDstPort(1);
		lnk2.setInventoryId("SW-0:4_SW-1:1");
		lnk2.setType("");

		//Creatin LINK SW-1_SW-2
		Link lnk3 = new Link();
		lnk3.setSrcSwitch("SW-1");
		lnk3.setSrcPort(4);
		lnk3.setDstSwitch("SW-2");
		lnk3.setDstPort(2);
		lnk3.setInventoryId("SW-1:4_SW-2:2");
		lnk3.setType("");

		//Creatin LINK SW-1_SW-3
		Link lnk4 = new Link();
		lnk4.setSrcSwitch("SW-1");
		lnk4.setSrcPort(3);
		lnk4.setDstSwitch("SW-3");
		lnk4.setDstPort(1);
		lnk4.setInventoryId("SW-1:3_SW-3:1");
		lnk4.setType("");

		//Creatin LINK SW-2_SW-3
		Link lnk5 = new Link();
		lnk5.setSrcSwitch("SW-2");
		lnk5.setSrcPort(4);
		lnk5.setDstSwitch("SW-3");
		lnk5.setDstPort(2);
		lnk5.setInventoryId("SW-2:4_SW-3:2");
		lnk5.setType("");
		
		//Creating command --> add sw0
		Command cmd0 = new Command();
		cmd0.setEvent("ADD_SWITCH");
		cmd0.setObject("here comes object serialized");
		cmd0.setSource("SWITCH");
		cmd0.setSwitch(swCollection.getDevices().get(0));
		
		//Creating command --> add sw1
		Command cmd1 = new Command();
		cmd1.setEvent("ADD_SWITCH");
		cmd1.setObject("here comes object serialized");
		cmd1.setSource("SWITCH");
		cmd1.setSwitch(swCollection.getDevices().get(1));
		
		//Creating command --> add sw2
		Command cmd2 = new Command();
		cmd2.setEvent("ADD_SWITCH");
		cmd2.setObject("here comes object serialized");
		cmd2.setSource("SWITCH");
		cmd2.setSwitch(swCollection.getDevices().get(2));

		//Creating command --> add sw3
		Command cmd3 = new Command();
		cmd3.setEvent("ADD_SWITCH");
		cmd3.setObject("here comes object serialized");
		cmd3.setSource("SWITCH");
		cmd3.setSwitch(swCollection.getDevices().get(3));

		
		//Creating command --> add LINK SW-0_SW-1
		Command cmd4 = new Command();
		cmd4.setEvent("ADD_LINK");
		cmd4.setObject("here comes object serialized");
		cmd4.setSource("LINK");
		cmd4.setLink(lnk1);
	
		//Creating command --> add LINK SW-0_SW-2
		Command cmd5 = new Command();
		cmd5.setEvent("ADD_LINK");
		cmd5.setObject("here comes object serialized");
		cmd5.setSource("LINK");
		cmd5.setLink(lnk2);
		
		//Creating command --> add LINK SW-1_SW-2
		Command cmd6 = new Command();
		cmd6.setEvent("ADD_LINK");
		cmd6.setObject("here comes object serialized");
		cmd6.setSource("LINK");
		cmd6.setLink(lnk3);
		
		//Creating command --> add LINK SW-1_SW-3
		Command cmd7 = new Command();
		cmd7.setEvent("ADD_LINK");
		cmd7.setObject("here comes object serialized");
		cmd7.setSource("LINK");
		cmd7.setLink(lnk4);
		
		//Creating command --> add LINK SW-0_SW-2
		Command cmd8 = new Command();
		cmd8.setEvent("ADD_LINK");
		cmd8.setObject("here comes object serialized");
		cmd8.setSource("LINK");
		cmd8.setLink(lnk5);
		
		//Creating command --> add HOST pc0
		Command cmd9 = new Command();
		cmd9.setEvent("ADD_HOST");
		cmd9.setObject("here comes object serialized");
		cmd9.setSource("HOST");
		cmd9.setHost(pc0);
		
		//Creating command --> add HOST pc1
		Command cmd10 = new Command();
		cmd10.setEvent("ADD_HOST");
		cmd10.setObject("here comes object serialized");
		cmd10.setSource("HOST");
		cmd10.setHost(pc1);
				
		//Creating command --> add HOST pc2
		Command cmd11 = new Command();
		cmd11.setEvent("ADD_HOST");
		cmd11.setObject("here comes object serialized");
		cmd11.setSource("HOST");
		cmd11.setHost(pc2);

		//Creating command --> add HOST pc0
		Command cmd12 = new Command();
		cmd12.setEvent("ADD_HOST");
		cmd12.setObject("here comes object serialized");
		cmd12.setSource("HOST");
		cmd12.setHost(pc3);
		
		//Creating command --> delete SW-0
		Command cmd13 = new Command();
		cmd13.setEvent("DELETE_SWITCH");
		cmd13.setObject("SW-0");
		cmd13.setSource("SWITCH");

		//Creating command --> delete SW-1
		Command cmd14 = new Command();
		cmd14.setEvent("DELETE_SWITCH");
		cmd14.setObject("SW-1");
		cmd14.setSource("SWITCH");
		
		//Creating command --> delete SW-3
		Command cmd15 = new Command();
		cmd15.setEvent("DELETE_SWITCH");
		cmd15.setObject("SW-3");
		cmd15.setSource("SWITCH");
		
		//Creating command --> delete sw-1_sw-3
		Command cmd16 = new Command();
		cmd16.setEvent("DELETE_LINK");
		cmd16.setObject("Here comes object serialized");
		cmd16.setLink(lnk4);
		cmd16.setSource("LINK");

		//Creating command --> delete sw-0_sw-2
		Command cmd17 = new Command();
		cmd17.setEvent("DELETE_LINK");
		cmd17.setObject("Here comes object serialized");
		cmd17.setLink(lnk2);
		cmd17.setSource("LINK");
		
		
		SingletonNode singleton = SingletonNode.getInstance();
		/*
		//Executing command add sw0
		singleton.execCommand("Here comes ", cmd0);
		System.out.println("Creando sw: " + cmd0.getSwitch().getInventoryId());
		
	
		//Executing command add sw1
		singleton.execCommand("Here comes ", cmd1);
		System.out.println("Creando sw: " + cmd1.getSwitch().getInventoryId());
		
		//Executing command add sw2
		singleton.execCommand("Here comes", cmd2);
		System.out.println("Creando sw: " + cmd2.getSwitch().getInventoryId());
		
		//Executing command add sw3
		singleton.execCommand("Here comes", cmd3);
		System.out.println("Creando sw: " + cmd3.getSwitch().getInventoryId());
		
		//Executing command add link sw-0_sw-1
		singleton.execCommand("Here comes", cmd4);
		System.out.println("Create link"  + cmd4.getLink().getInventoryId());
		
		
		//Executing command add link sw-0_sw-2
		singleton.execCommand("Here comes", cmd5);
		
		//Executing command add link sw-1_sw-2
		singleton.execCommand("Here comes", cmd6);
		
		//Executing command add link sw-1_sw-3
		singleton.execCommand("Here comes", cmd7);
		
		//Executing command add link sw-2_sw-3
		singleton.execCommand("Here comes", cmd8);
		
		
		//Executing command add host pc0
		singleton.execCommand("here comes", cmd9);
		
		//Executing command add host pc1
		singleton.execCommand("here comes", cmd10);
		
		//Executing command add host pc2
		singleton.execCommand("here comes", cmd11);
		
		//Executing command add host pc3
		singleton.execCommand("here comes", cmd12);
		
		//Executing command delete sw-0
		singleton.execCommand("here comes", cmd13);
		
		//Executing command delete sw-1
		singleton.execCommand("here comes", cmd14);
		
		//Executing command delete sw-2
		singleton.execCommand("here comes", cmd15 );
		
		//Executing command add sw0 again
		singleton.execCommand("Here comes ", cmd0);
		*/
		
		//Executing command delete link sw-1_sw-3
		singleton.execCommand("Here comes", cmd16);
		
		//Executing command delete link sw-0_sw-2
		singleton.execCommand("Here comes", cmd17);
		
	}

}
