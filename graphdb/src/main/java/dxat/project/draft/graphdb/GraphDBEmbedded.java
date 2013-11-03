package dxat.project.draft.graphdb;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.traversal.Evaluators;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.graphdb.traversal.Traverser;
import org.neo4j.kernel.Traversal;


public class GraphDBEmbedded {

	//For Linux
	//private static final String DB_PATH ="/path";
	//For Windows
	//private static final String DB_PATH ="/path";
	private static final String DB_PATH= "target/graph-network";
	private GraphDatabaseService graphDb;
	private long SDNNodeId;
	private HashMap<String, Node> listPortDevices = new HashMap<String, Node>();
	
	//LIST OF RELATION TYPES
	private static enum RelTypes implements RelationshipType{
		ELEMENT, 
		HAS,
		LINK,	
	}
	public void setUp() throws IOException
	{
		deleteFileOrDirectory( new File(DB_PATH));
		graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);
		registerShutdownHook();
		createNodespace();
	}
	
	public void shutdown()
	{
		graphDb.shutdown();
	}
	public void createNodespace()
	{
		Transaction tx = graphDb.beginTx();
		try
		{				
			
			//Create base network node
			Node network = graphDb.createNode();
			network.setProperty("name", "SDNnetwork");
			SDNNodeId = network.getId();
			int numports;
			
			//Create switch and ports nodes
			for(int i=0;i<4;i++){
			
				Node sw = CreateNodeDevices(Integer.toString(i), "sw",5);
				//Define network-->element relationship
				network.createRelationshipTo(sw, RelTypes.ELEMENT);
				numports = (Integer) sw.getProperty("ports");
				for(int j=0; j<numports; j++){
					Node port = CreateNodePorts("sw" + Integer.toString(i) + "_p" + Integer.toString(j));
					//Define sw-->port relationship;
					CreateRelDevToPorts(sw, port);
					//Added DevicePort to HashMap
					listPortDevices.put((String) port.getProperty("id"), port);
				}
			}
			
			//Create pc nodes
			for(int i=0; i<4;i++){
				Node pc = CreateNodeDevices(Integer.toString(i), "pc", 1);
				Node port = CreateNodePorts("pc" + Integer.toString(i)+ "_p0");
				//Define pc-->port relationship
				CreateRelDevToPorts(pc, port);
				//Added DevicePort to HashMap
				listPortDevices.put((String) port.getProperty("id"), port);
			}
			
			//CREATE LINKS**********
			System.out.println("*************" + listPortDevices.keySet());
			
			CreateLinks(listPortDevices.get("pc0_p0"),listPortDevices.get("sw0_p0"));
			CreateLinks(listPortDevices.get("pc1_p0"),listPortDevices.get("sw0_p1"));
			CreateLinks(listPortDevices.get("sw0_p2"),listPortDevices.get("sw1_p0"));
			CreateLinks(listPortDevices.get("sw0_p3"),listPortDevices.get("sw2_p0"));
			CreateLinks(listPortDevices.get("sw1_p1"),listPortDevices.get("sw2_p1"));
			CreateLinks(listPortDevices.get("sw1_p2"),listPortDevices.get("sw3_p0"));
			CreateLinks(listPortDevices.get("sw2_p2"),listPortDevices.get("sw3_p1"));
			CreateLinks(listPortDevices.get("sw3_p2"),listPortDevices.get("pc2_p0"));
			CreateLinks(listPortDevices.get("sw3_p3"),listPortDevices.get("pc3_p0"));			
			
			tx.success();	
		}
		/*
		catch (Exception e)
		{
			tx.failure();
		}*/
		finally
		{
			tx.finish();
		}
	
	}
	
	public Node CreateNodeDevices( String name, String type, int ports){
		//id---> sw0,pc0,....
		Node device = graphDb.createNode();
		device.setProperty("id", type + name);
		device.setProperty("status", true);
		device.setProperty("type", type );
		device.setProperty("ports", ports);
		if(type=="sw" || type=="rt")	device.setProperty("model", "RouterBOARD 750GL");
		else device.setProperty("model", "PC");
		return device;
	}
	
	public Node CreateNodePorts(String name){
		//id---> sw0_p0, pc0_p1....
		Node port = graphDb.createNode();
		port.setProperty("id", name );
		port.setProperty("status", true);
		port.setProperty("behaviour", "port");
		port.setProperty("ethernet", 100);//10/100/1000 Mbps
		return port;
	}
	
	public void CreateRelDevToPorts(Node sw, Node port){
		Relationship DevToPort = sw.createRelationshipTo(port, RelTypes.HAS);
		
	}
	
	public void CreateLinks(Node port1, Node port2){
		Relationship link = port1.createRelationshipTo(port2, RelTypes.LINK);
		link.setProperty("id", (String) port1.getProperty("id") + "-"+ (String) port2.getProperty("id"));
		
		int iface1 = (Integer) port1.getProperty("ethernet");
		int iface2 = (Integer) port2.getProperty("ethernet");
		int bandwidth;
		
		if (iface1>iface2) bandwidth = iface2;
		else bandwidth = iface1;
		link.setProperty("BW", bandwidth);
		link.setProperty("status", true);
		
	}
	
	private Node getNetworkNode()
	{
		return graphDb.getNodeById(SDNNodeId);
	}
	
	public String printNetworkElements(){
		Transaction tx = graphDb.beginTx();
		try
		{
			Node networkNode = getNetworkNode();
			int numberOfElements = 0;
			String output = networkNode.getProperty("name") + "'s elements:\n";
			Traverser elementsTraverser = getElements(networkNode);
			
			for (Path elementPath : elementsTraverser){
				output +="Ath depth " + elementPath.length() + " =>"
									+ elementPath.endNode().getProperty("id") + "\n";
				numberOfElements++;
			}
			output += "Number of elements found: " + numberOfElements + "\n";
			return output;
		}
		
		finally
		{
			tx.finish();
		}
		//return null;
		
	}
	private static Traverser getElements( final Node element){
		TraversalDescription td = Traversal.description()
				.breadthFirst()
				.relationships(RelTypes.ELEMENT, Direction.OUTGOING)
				.evaluator(Evaluators.excludeStartPosition());
		td.traverse(element);
		return td.traverse(element);
	}
	
	private void registerShutdownHook()
	{
		Runtime.getRuntime().addShutdownHook(new Thread()
		{
			@Override
			public void run()
			{
				graphDb.shutdown();
			}
		});
	}
	private static void deleteFileOrDirectory( final File file)
	{
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
		else
		{
			file.delete();
		}
	}
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		GraphDBEmbedded SDNNetwork = new GraphDBEmbedded();
		SDNNetwork.setUp();
		System.out.println(SDNNetwork.printNetworkElements());
		
	}

}
