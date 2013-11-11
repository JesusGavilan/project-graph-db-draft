package dxat.project.draft.graphdb;

public class Command {
	// Switches Commands
	public final static String ADD_SWITCH = "ADD_SWITCH";
	public final static String DELETE_SWITCH = "DELETE_SWITCH";
	public final static String UPDATE_SWITCH = "UPDATE_SWITCH";

	// Hosts Commands
	public final static String ADD_HOST = "ADD_HOST";
	public final static String UPDATE_HOST = "UPDATE_HOST";
	public final static String DELETE_HOST = "DELETE_HOST";

	// Links Commands
	public final static String ADD_LINK = "ADD_LINK";
	public final static String DELETE_LINK = "DELETE_LINK";
	public final static String UPDATE_LINK = "UPDATE_LINK";

	// Attributes
	private String event = ""; // Available commands
	private String object = ""; // Serialized object 
	private String source = ""; // Serialized object class

	/*
	 * For testing
	 */
	private Switch sw = null;
	private Link lnk  = null;
	private Host host = null;

	/*
	 *End of testing 
	 */
	
	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}
	
	/*
	 * For testing
	 */
	
	public void setSwitch(Switch sw){
		this.sw=sw;
	}
	public Switch getSwitch(){
		return sw;
	}
	public void setLink(Link lnk){
		this.lnk= lnk;
	}
	public Link getLink(){
		return lnk;
	}
	public void setHost(Host host){
		this.host=host;
	}
	public Host getHost(){
		return host;
	}

}
