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
	//private static final String DB_PATH ="/var/lib/neo4j/data/graph.db";
	//For Windows
	//private static final String DB_PATH ="/path";
	private static final String DB_PATH= "target/graph-network";
	private GraphDatabaseService graphDb;
	private long SDNNodeId;
	private HashMap<String, Node> listIfaceDevices = new HashMap<String, Node>();
	
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
			int numIfaces;
			
			//Create switch and ports nodes
			for(int i=0;i<4;i++){
			
				Node sw = CreateNodeDevices(Integer.toString(i), "SW",5);
				//Define network-->element relationship
				network.createRelationshipTo(sw, RelTypes.ELEMENT);
				numIfaces = (Integer) sw.getProperty("interfaces");
				for(int j=0; j<numIfaces; j++){
					Node iface = CreateNodeIfaces("SW-" + Integer.toString(i) + "_P" + Integer.toString(j), "SW");
					//Define sw-->port relationship;
					CreateRelDevToIfaces(sw, iface);
					//Added DevicePort to HashMap
					listIfaceDevices.put((String) iface.getProperty("inventoryId"), iface);
				}
			}
			
			//Create pc nodes
			for(int i=0; i<4;i++){
				Node pc = CreateNodeDevices(Integer.toString(i), "PC", 1);
				Node iface = CreateNodeIfaces("PC-" + Integer.toString(i)+ "_P0","PC");
				//Define pc-->port relationship
				CreateRelDevToIfaces(pc, iface);
				//Added DevicePort to HashMap
				listIfaceDevices.put((String) iface.getProperty("inventoryId"), iface);
			}
			
			//CREATE LINKS**********
			System.out.println("*************" + listIfaceDevices.keySet());
			
			CreateLinks(listIfaceDevices.get("PC-0_P0"),listIfaceDevices.get("SW-0_P0"));
			CreateLinks(listIfaceDevices.get("PC-1_P0"),listIfaceDevices.get("SW-0_P1"));
			CreateLinks(listIfaceDevices.get("SW-0_P2"),listIfaceDevices.get("SW-1_P0"));
			CreateLinks(listIfaceDevices.get("SW-0_P3"),listIfaceDevices.get("SW-2_P0"));
			CreateLinks(listIfaceDevices.get("SW-1_P1"),listIfaceDevices.get("SW-2_P1"));
			CreateLinks(listIfaceDevices.get("SW-1_P2"),listIfaceDevices.get("SW-3_P0"));
			CreateLinks(listIfaceDevices.get("SW-2_P2"),listIfaceDevices.get("SW-3_P1"));
			CreateLinks(listIfaceDevices.get("SW-3_P2"),listIfaceDevices.get("PC-2_P0"));
			CreateLinks(listIfaceDevices.get("SW-3_P3"),listIfaceDevices.get("PC-3_P0"));			
			
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
		device.setProperty("inventoryId", type + "-" + name);
		device.setProperty("status", true);
		device.setProperty("type", type );
		device.setProperty("interfaces", ports);
		if(type=="SW" || type=="RT")	device.setProperty("model", "RouterBOARD 750GL");
		else device.setProperty("model", "PC");
		return device;
	}
	
	public Node CreateNodeIfaces(String name, String type){
		//id---> sw0_p0, pc0_p1....
		Node iface = graphDb.createNode();
		iface.setProperty("inventoryId", name );
		iface.setProperty("status", true);
		iface.setProperty("mac", "00:34:22:33:33:69");
		if(type=="RT" || type=="PC") iface.setProperty("ip", "66.66.66.66");
		iface.setProperty("ethernet", 100);//10/100/1000 Mbps
		return iface;
	}
	
	public void CreateRelDevToIfaces(Node sw, Node iface){
		sw.createRelationshipTo(iface, RelTypes.HAS);
		
	}
	
	public void CreateLinks(Node interface1, Node interface2){
		Relationship link = interface1.createRelationshipTo(interface2, RelTypes.LINK);
		link.setProperty("id", (String) interface1.getProperty("inventoryId") + "-"+ (String) interface2.getProperty("inventoryId"));
		
		int capacity1 = (Integer) interface1.getProperty("ethernet");
		int capacity2 = (Integer) interface2.getProperty("ethernet");
		int bandwidth;
		
		if (capacity1>capacity2) bandwidth = capacity2;
		else bandwidth = capacity1;
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
									+ elementPath.endNode().getProperty("inventoryId") + "\n";
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
