package com.dc.assignement.primenumberselection.paxos.model;

public class Node {
	private  String nodeName;
	private  Integer nodeId;
	private  Integer port;
	private  Boolean election;
	private  Boolean coordinator;
	
	public Node() {
		// TODO Auto-generated constructor stub
	}

	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	public Integer getNodeId() {
		return nodeId;
	}
	public void setNodeId(Integer nodeId) {
		this.nodeId = nodeId;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public Boolean getElection() {
		return election;
	}
	public void setElection(Boolean election) {
		this.election = election;
	}
	public Boolean getCoordinator() {
		return coordinator;
	}
	public void setCoordinator(Boolean coordinator) {
		this.coordinator = coordinator;
	}
}