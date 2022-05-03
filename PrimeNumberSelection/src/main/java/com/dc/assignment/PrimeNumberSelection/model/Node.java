package com.dc.assignment.PrimeNumberSelection.model;

public class Node {
	private static String nodeName;
	private static Integer nodeId;
	private static Integer port;
	private static Boolean election;
	private static Boolean coordinator;

	public Node() {
		
	}
	
	public Node(String nodeName, Integer nodeId, Integer port) {
		super();
		this.nodeName = nodeName;
		this.nodeId = nodeId;
		this.port = port;
		this.election = Boolean.FALSE;
		this.coordinator = Boolean.FALSE;
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
