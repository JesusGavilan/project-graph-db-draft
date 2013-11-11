package dxat.project.draft.graphdb;

public interface ControllerInterface {
	
	// Switches operations
	public void addSwitch(Switch sw);

	public void updateSwitch(Switch sw);

	public void deleteSwitch(String swId);

	// Links Operations
	public void addLink(Link lnk);

	public void updateLink(Link lnk);

	public void deleteLink(Link lnk);

	// Hosts Operations
	public void addHost(Host host);

	public void updateHost(Host host);

	public void deleteHost(Host host);
}
