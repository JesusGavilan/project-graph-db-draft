/*********
 author: DXAT group
 * 
 * 
 */


package dxat.project.draft.graphdb;


public class SingletonNode {
	
	
	public static SingletonNode instance;
	
	private SingletonNode(){
			
		
	}
	
	public static SingletonNode getInstance(){
		if(instance == null)
			instance = new SingletonNode();
		return instance;
	}
	
	public void execCommand(Command command){
		
		ControllerImp action = new ControllerImp();
		switch(command.getEvent()){
			case "ADD_SWITCH":
				jsonToSwitch(command.getObject(), command.getSwitch());
				action.addSwitch(command.getSwitch());
				break;
			
			case "DELETE_SWITCH":
				jsonToSwitch(command.getObject(), command.getSwitch());
				action.deleteHost(command.getHost());
				break;
			
			case "UPDATE_SWITCH":
				jsonToSwitch(command.getObject(), command.getSwitch());
				action.updateHost(command.getHost());
				break;
			
			case "ADD_HOST":
				jsonToHost(command.getObject(), command.getHost());
				action.addHost(command.getHost());
				break;
			
			case "UPDATE_HOST":
				jsonToHost(command.getObject(), command.getHost());
				break;
			
			case "DELETE_HOST":
				jsonToHost(command.getObject(), command.getHost());
				break;
			
			case "ADD_LINK":
				jsonToLink(command.getObject(), command.getLink());
				break;
				
			case "DELETE_LINK":
				jsonToLink(command.getObject(), command.getLink());
				break;
				
			case "UPDATE_LINK":
				jsonToLink(command.getObject(), command.getLink());
				break;
		}
		
	}
	
	/***
	 * 
	 * For testing the next methods have a extra argument
	 * to neglect the deserealization.
	 */
	public Command jsonToCommand(String jsonSwitch, Command cmd){
		//Here parse json to Command
		//for testing: passing a command
		
		return cmd;
		
	}

	public Switch jsonToSwitch(String jsonSwitch, Switch sw){
		//Here parse json to Switch
		//for testing: passing a switch
		return sw;	
	}
	
	
	public Host jsonToHost(String jsonHost, Host host ){
		//Here parse json to Host
		//for testing: passing a Host
		
		return host;
	}
	
	public Link jsonToLink(String jsonLink, Link link){
		//Here parse json to Link
		//for testing: passing a Link
		return link;
	}
	
	public Interface jsonToInterface(String jsonInterface, Interface iface){
		//Here parse json to Interface
		//for testing: passing a Interface
		return iface;
	}
	
	
	
	
}
