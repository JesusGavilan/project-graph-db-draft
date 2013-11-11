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
	
	public void execCommand(String cmd, Command comando){
		
		//Deserealizing and converting to object Command		
		Command command = jsonToCommand(cmd,comando);
		
		
		
		ControllerImp action = new ControllerImp();
		switch(command.getEvent()){
			case "ADD_SWITCH":
				//Deserializing and converting to Switch object
				Switch sw = jsonToSwitch(command.getObject(), command.getSwitch());
				//Executing add Switch
				action.addSwitch(sw);
				break;
			
			case "DELETE_SWITCH":
				//Getting Switch Id 
				String swId = command.getObject();
				//Executing delete Switch
				action.deleteSwitch(swId);
				break;
			
			case "UPDATE_SWITCH":
				/***** TO-DO *********/
				//jsonToSwitch(command.getObject(), command.getSwitch());
				//action.updateHost(command.getHost());
				break;
			
			case "ADD_HOST":
				//Deserealizing and converting to Host object
				Host host = jsonToHost(command.getObject(), command.getHost());
				//Executing add Host
				action.addHost(host);
				break;
			
			case "UPDATE_HOST":
				/******* TO-DO *****************/
				//jsonToHost(command.getObject(), command.getHost());
				break;
			
			case "DELETE_HOST":
				//Deserealizing and converting to Host object
				Host host1 = jsonToHost(command.getObject(), command.getHost());
				//Executing delete host
				action.deleteHost(host1);
				break;
			
			case "ADD_LINK":
				//Deserealizing and converting to Link object
				Link lnk = jsonToLink(command.getObject(), command.getLink());
				//Executing add link
				action.addLink(lnk);
				break;
				
			case "DELETE_LINK":
				//Deseralizing and converting to Link object
				Link lnk1 = jsonToLink(command.getObject(), command.getLink());	
				action.deleteLink(lnk1);
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
