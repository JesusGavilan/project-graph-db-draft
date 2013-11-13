package dxat.project.draft.graphdb;


import java.io.File;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexManager;
import org.neo4j.graphdb.traversal.Evaluator;
import org.neo4j.graphdb.traversal.Evaluators;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.graphdb.traversal.Traverser;
import org.neo4j.kernel.Traversal;


public class ControllerImp implements ControllerInterface {
	
	private static final String DB_PATH= "target/graph-network";
	private GraphDatabaseService graphDb;
	private static Index<Node> listIfaceDevices; //In order to make indexes over the nodes
	private static Node root;
	
	
	private static enum RelTypes implements RelationshipType{
		ELEMENT, 
		HAS,
		LINK,	
	}


	@Override
	public void addSwitch(Switch device) {
		
		// TODO Auto-generated method stub
		setUp();
		
		Transaction tx = graphDb.beginTx();
		
		try
		{	
			
			Node dev = graphDb.createNode();
			dev.setProperty("inventoryId", device.getInventoryId());
			dev.setProperty("status", true);
			dev.setProperty("type", device.getType());
			dev.setProperty("ifaces", device.getNports());
			dev.setProperty("ipConfig", device.getOfAddr().split(":")[1]);
			dev.setProperty("portConfig", device.getOfAddr().split(":")[0]);
			dev.setProperty("portCom", device.getOfAddr().split(":")[2]);
			
			dev.setProperty("software", device.getSoftware());
			dev.setProperty("hardware", device.getHardware());
			dev.setProperty("manufacturer", device.getManufacturer());
			dev.setProperty("serialNumber", device.getSerialNum());
			dev.setProperty("datapath", device.getDatapath());
			dev.setProperty("model", "RouterBOARD 750GL");
			
			listIfaceDevices.add(dev, "inventoryId", device.getInventoryId());
			
			//Getting the parameters of the configuration interface
			String [] ifaceConfigParameters = device.getOfAddr().split(":");//id:ip:port
			
			//Creating interfaces nodes of the sw
			for(Interface iface: device.getInterfaces().getInterfaces()){
				//Create the device --> controller relationship
				
				Node nodeIface;
				System.out.println("********** Creando ifaces " + Integer.valueOf(ifaceConfigParameters[0])+ "  " + iface.getPortId());
				if (Integer.valueOf(ifaceConfigParameters[0])==iface.getPortId()) {
		          
					nodeIface = createNodeInterface(iface, device.getType(), device.getOfAddr());
					
				}
				else {
					
					nodeIface = createNodeInterface(iface, device.getType(), null);
				}
				
				//creating the switch --> switch interface relationship
				System.out.println("******sw: " + dev.getProperty("inventoryId")+ " connected to " + nodeIface.getProperty("inventoryId")  );
				dev.createRelationshipTo(nodeIface, RelTypes.HAS);				

			}
			
			tx.success();
			
		}
		finally
		{
			tx.finish();
			graphDb.shutdown();
		}
		//registerShutdownHook();
		
	}

	@Override
	public void updateSwitch(Switch sw) {
		Transaction tx = graphDb.beginTx();
		try
		{
			
		}
		finally
		{
			tx.finish();
		}

	}

	@Override
	public void deleteSwitch(String swId) {
		setUp();
		Transaction tx = graphDb.beginTx();
		try
		{
			System.out.println("****** Swithc name that pass to delete" + swId);
			Node sw = listIfaceDevices.get("inventoryId",swId).getSingle();
			System.out.println("****** Switch name to delete" + sw.getProperty("inventoryId").toString());
			//Get the interfaces from this node
			Traverser elementsTraverserNode = getInterfaceSwitch(sw);
			for(Path elementNode: elementsTraverserNode){
				//Deleting interface--> interface relationships. Deleting all the LINKS related to this node.
				Relationship pathLink = elementNode.endNode().getSingleRelationship(RelTypes.LINK, Direction.BOTH);
				Relationship pathHas  = elementNode.endNode().getSingleRelationship(RelTypes.HAS, Direction.BOTH);
                if (pathLink!= null){
                	elementNode.endNode().getSingleRelationship(RelTypes.LINK, Direction.BOTH).delete();
    				//Deleting interface--> switch relationship
    				elementNode.endNode().getSingleRelationship(RelTypes.HAS, Direction.BOTH).delete();
                }
                else elementNode.endNode().getSingleRelationship(RelTypes.HAS, Direction.BOTH).delete();
				
                //Deleting interface
				elementNode.endNode().delete();
			}
			
			//Deleting interface to Controller
			String idCT= "CT-" + sw.getProperty("inventoryId") + ":" + sw.getProperty("portConfig");
			System.out.println("**********Name of the controller interface to delete " + idCT);
			
			Traverser elementsTraverserController = getInterfaceController(root);
			for(Path elementController: elementsTraverserController){
				System.out.println("***** Name of the controller interfaces " + elementController.endNode().getProperty("inventoryId"));
				if(idCT.equals(elementController.endNode().getProperty("inventoryId"))){
					//Deleting interface controller-->controller relationship
					elementController.endNode().getSingleRelationship(RelTypes.HAS, Direction.INCOMING).delete();
					//Deleting interface controller
					elementController.endNode().delete();
					break;
				}
			}
			//Deleting switch
			listIfaceDevices.remove(sw);
			sw.delete();
			
			
			tx.success();
			
		}
		finally
		{
			tx.finish();
			graphDb.shutdown();
		}
		
	}

	
	@Override
	public void addLink(Link lnk) {
		setUp();
		
		Transaction tx = graphDb.beginTx();
		try
		{
			System.out.println("Parameters link object: (source: sw,port, dest: sw,por, inventory) " + lnk.getSrcSwitch() + " " + lnk.getSrcPort() + " " + lnk.getDstSwitch() + " " + lnk.getDstPort() + " " + lnk.getInventoryId() );
			//Getting switches that affects the link
			Node srcSwitch = listIfaceDevices.get("inventoryId", lnk.getSrcSwitch()).getSingle();
			System.out.println("*****Name source node LINK:" + srcSwitch.getProperty("inventoryId"));
			Node dstSwitch = listIfaceDevices.get("inventoryId", lnk.getDstSwitch()).getSingle();
			System.out.println("*****Name source node LINK:" + dstSwitch.getProperty("inventoryId"));
			Node srcIface = null;
			Node dstIface = null;
			
			//Getting the interfaces of the switches that affects the links
			Traverser elementsTraverserSrc = getInterfaceSwitch(srcSwitch);
			Traverser elementsTraverserDst = getInterfaceSwitch(dstSwitch);
			
			//Building the interfaces id
			String src = lnk.getSrcSwitch() + ":" + lnk.getSrcPort();
			String dst = lnk.getDstSwitch() + ":" + lnk.getDstPort();
			System.out.println("**********SRC building interfaces" + src);
			System.out.println("**********DST building interfaces" + dst);
			//Looking for a interface switch of the src
			for (Path elementPathSrc : elementsTraverserSrc){
				System.out.println("****** Name of the source interfaces: " + elementPathSrc.endNode().getProperty("inventoryId"));
				if ( src.equals(elementPathSrc.endNode().getProperty("inventoryId"))){
					System.out.println("****** Found the interface of the source *******");
					srcIface = elementPathSrc.endNode();
					break;
				}
			}
			
			//Looking for a interface switch of th dst
			for (Path elementPathDst : elementsTraverserDst){
				System.out.println("****** Name of the destination interfaces: " + elementPathDst.endNode().getProperty("inventoryId"));
				if( dst.equals(elementPathDst.endNode().getProperty("inventoryId"))){
					dstIface = elementPathDst.endNode();
					break;
				}
			}
			System.out.println("****Switch interface of the source: "+ srcIface.getProperty("inventoryId"));
			System.out.println("****Switch interface of the destination: "+ dstIface.getProperty("inventoryId"));
			//Creating the switch interface --> switch interface (LINK) relationship
			Relationship link = srcIface.createRelationshipTo(dstIface, RelTypes.LINK);
			
			link.setProperty("id", "777");
			link.setProperty("status", true);
			link.setProperty("srcSwitch", lnk.getSrcSwitch());
			link.setProperty("srcPort", lnk.getSrcPort());
			link.setProperty("dstSwitch", lnk.getDstSwitch());
			link.setProperty("dstPort", lnk.getDstPort());
			link.setProperty("inventoryId", lnk.getInventoryId());
			link.setProperty("type", lnk.getType());
			
			tx.success();
			
		}
		
		finally
		{
			tx.finish();
			graphDb.shutdown();
		}
		

	}
	
	//Implementation for SW-Control link
	public void addInternalLink(Node interface1, Node interface2){
		Relationship link = interface1.createRelationshipTo(interface2, RelTypes.LINK);
		System.out.println("*** internal link: " + interface1.getProperty("inventoryId")+ "  " + interface2.getProperty("inventoryId"));
		link.setProperty("id", (String) interface1.getProperty("inventoryId") + "-"+ (String) interface2.getProperty("inventoryId"));
		
		link.setProperty("status", true);
		link.setProperty("srcSwitch", "");
		link.setProperty("srcPort","");
		link.setProperty("dstSwitch","");
		link.setProperty("dstPort", "");
		link.setProperty("type", "");
	}
	
	
	@Override
	public void updateLink(Link lnk) {
		// TODO Auto-generated method stub
		

	}

	@Override
	public void deleteLink(Link lnk) {
		// TODO Auto-generated method stub
		setUp();
		Transaction tx = graphDb.beginTx();
		try
		{
			
			//Getting the parameters
			String src = lnk.getSrcSwitch() + ":" + lnk.getSrcPort();
			
			//Getting Switch nodes that affects links
			Node srcSwitch = listIfaceDevices.get("inventoryId", lnk.getSrcSwitch()).getSingle();
			
			
			//Getting interfaces of the sw that affects links
			Traverser elementsTraverserSrc = getInterfaceSwitch(srcSwitch);
			//Finding and deleting
			for(Path elementSrc : elementsTraverserSrc){
				if( src.equals(elementSrc.endNode().getProperty("inventoryId"))){
					//Deleting link
					Relationship pathLink = elementSrc.endNode().getSingleRelationship(RelTypes.LINK, Direction.BOTH);
					if(pathLink!=null) pathLink.delete();
				}
			}
			
			tx.success();
		}
		finally{
			tx.finish();
			graphDb.shutdown();
		}

	}

	@Override
	public void addHost(Host host) {
		// TODO Auto-generated method stub
		setUp();
		Transaction tx = graphDb.beginTx();
		
		try
		{
			Node pc = graphDb.createNode();
			
			pc.setProperty("inventoryId", host.getHostId());
			pc.setProperty("IP", host.getIpv4().get(0));
			//pc.setProperty("MAC", host.getMac());
			pc.setProperty("DHCP", host.getDhcpName());
			//pc.setProperty("PortId", host.getPortId());
			pc.setProperty("SwitchId", host.getSwId().get(0));
			pc.setProperty("VLAN", host.getVlan().get(0));
			//Added to index
			listIfaceDevices.add(pc, "inventoryId", host.getHostId());
			//Create host interface
			
			Node iface = graphDb.createNode();
			iface.setProperty("inventoryId", host.getHostId() + ":" + host.getPortId().get(0)); //SW-XX:PORT
			iface.setProperty("portId", host.getPortId().get(0));
			iface.setProperty("status",  false);
			iface.setProperty("enabled", false);
			iface.setProperty("mac", host.getMac());
			iface.setProperty("ip", host.getIpv4().get(0)); //NGTH
			iface.setProperty("currentSpeed", "");//10/100/1000 Mbps
				
			//Creating the host --> host interface relationship
			pc.createRelationshipTo(iface, RelTypes.HAS);
			
			//Looking for a interface switch to LINK with interface host
			Traverser elementsTraverserSw = getInterfaceSwitch(listIfaceDevices.get("inventoryId", host.getSwId().get(0)).getSingle());
			
			//Looking for a interface switch of the src
			Node swIface = null;
			String swInterface = host.getSwId().get(0).toString() + ":" + host.getPortId().get(0).toString();
			System.out.println("****** Name of the switch interface build it: " + swInterface);
			for (Path elementPathSw : elementsTraverserSw){
				System.out.println("****** Name of the switch interfaces: " + elementPathSw.endNode().getProperty("inventoryId"));
				if ( swInterface.equals(elementPathSw.endNode().getProperty("inventoryId").toString())){
					System.out.println("****** Found the interface of switch to connect pc *******");
					swIface = elementPathSw.endNode();
					break;
				}
			}
			
			//Creating LINK interface switch --> controllerIface relationship
			addInternalLink(iface, swIface);
			
			tx.success();
		}
		finally{
			tx.finish();
			graphDb.shutdown();
		}
	}

	@Override
	public void updateHost(Host host) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteHost(Host host) {
		// TODO Auto-generated method stub
		setUp();
		Transaction tx = graphDb.beginTx();
		try
		{
			//Getting the host node
			Node pc = listIfaceDevices.get("inventoryId", host.getHostId()).getSingle();
			
			//Getting inventoryId of the interface
			String idIface = host.getSwId() + ":" + host.getPortId();
			
			//Getting the interface of the host node
			Traverser elementsTraverserIfaces = getInterfaceSwitch(pc);
			
			for(Path elementIface: elementsTraverserIfaces){
				if(elementIface.endNode().getProperty("inventoryId")==idIface){
					//Deleting links
					elementIface.endNode().getSingleRelationship(RelTypes.LINK, Direction.BOTH);
					//Deleting host --> interface host relationship
					elementIface.endNode().getSingleRelationship(RelTypes.HAS, Direction.BOTH).delete();
					//Deleting interface host
					elementIface.endNode().delete();
				}
			}
			//Removing host from the index
			listIfaceDevices.remove(pc);
			//Removing host
			pc.delete();
			
			
		}
		finally{
			tx.finish();
		}
		
	}
	
	public void setUp(){
		
		deleteFileOrDirectory( new File(DB_PATH));
		graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);
		listIfaceDevices = graphDb.index().forNodes("inventoryId");
		System.out.println("***** DENTRO DEL setUp:nombre del SDNnetwork"+ listIfaceDevices.get("inventoryId","SDNnetwork").getSingle());
		root = getControllerNode();
				
	}
	
	public void shutDown(){
		
		graphDb.shutdown();
	}
	
	private void registerShutdownHook(){
		
		Runtime.getRuntime().addShutdownHook(new Thread()
		{
			@Override
			public void run()
			{
				graphDb.shutdown();
			}
		});
	}
	
	private static void deleteFileOrDirectory( final File file){
		
		if( !file.exists())
		{
			return;
		}
		if(file.isDirectory())
		{   
			for (File child : file.listFiles())
			{
				deleteFileOrDirectory(child);
			}
			
		}
		if( file.exists()){
			return;
		}
		else
		{   			
			file.delete();
		}
		
	}
	
	private Node getControllerNode()
	{
		if (listIfaceDevices.get("inventoryId", "SDNnetwork").getSingle()==null){
			System.out.println("*********** Creando el nodo network, controllador *********" + listIfaceDevices.get("inventoryId","SDNcontroller").getSingle());
			Transaction tx = graphDb.beginTx();
			
			try
			{				
			
				//Create base network node
				Node network = graphDb.createNode();
				network.setProperty("name", "SDNnetwork");
				network.setProperty("inventoryId", "SDNnetwork");
				listIfaceDevices.add(network, "inventoryId", "SDNnetwork");
				//SDNNodeId = network.getId();
			
				//Create controller node
				Node controller = graphDb.createNode();
				controller.setProperty("inventoryId", "SDNcontroller");
				listIfaceDevices.add(controller, "inventoryId", "SDNcontroller");
			
				//Network-controller relationship
				network.createRelationshipTo(controller, RelTypes.ELEMENT);
				
				
				tx.success();	
				
				return controller;

			}
			finally
			{
				tx.finish();;	
			}
		}
		
		else{
			System.out.println("************** Existe el controlador ****************" + listIfaceDevices.get("inventoryId","SDNcontroller").getSingle().getProperty("inventoryId"));
			return listIfaceDevices.get("inventoryId", "SDNcontroller").getSingle();	
		}
			
			
	}
	
	
	public Node createNodeInterface(Interface interf, String type, String confIface ){
		
		Node iface = graphDb.createNode();
		iface.setProperty("inventoryId", interf.getInventoryId());
		iface.setProperty("portId", interf.getPortId());
		iface.setProperty("status", interf.getStatus());
		iface.setProperty("enabled", interf.getEnabled());
		iface.setProperty("mac", interf.getMac());
		if(type=="RT" || type=="PC") iface.setProperty("ip", "66.66.66.66"); //NGTH
		iface.setProperty("currentSpeed", interf.getCurrentSpeed());//10/100/1000 Mbps
		if (confIface!=null){
			iface.setProperty("ipConfig", confIface.split(":")[1]);
			iface.setProperty("portConfig",confIface.split(":")[2]);
			
			//Creating a interface for the controller
			Node controllerIface = createInterfaceController("CT-" +  interf.getInventoryId());
			
			//Creating the controller -->  controllerIface relationship
			root.createRelationshipTo(controllerIface, RelTypes.HAS);
			
			//Creating LINK interface switch --> controllerIface relationship
			System.out.println("******* switch iface: " + iface.getProperty("inventoryId") + "  controller iface: " + controllerIface.getProperty("inventoryId"));
			iface.createRelationshipTo(controllerIface, RelTypes.LINK);
			
		}
		
		return iface;
		
	}
	
	
	public Node createInterfaceController(String name){
		Node contIface = graphDb.createNode();
		contIface.setProperty("inventoryId", name);
		contIface.setProperty("port",6969);
		contIface.setProperty("ip","66.66.66.66");
		
		return contIface;
		
	} 
	
	private static Traverser getInterfaceSwitch( final Node element){
		TraversalDescription td = Traversal.description()
				.breadthFirst()
				.relationships(RelTypes.HAS, Direction.OUTGOING)
				.evaluator(Evaluators.excludeStartPosition());
		td.traverse(element);
		return td.traverse(element);
	}
	
	private static Traverser getInterfaceController( final Node element){
		TraversalDescription td = Traversal.description()
				.breadthFirst()
				.relationships(RelTypes.HAS,Direction.OUTGOING)
				.evaluator(Evaluators.excludeStartPosition());
		td.traverse(element);
		return td.traverse(element);
	}
	
	

}
