package com.dc.assignment.PrimeNumberSelection.model;

public class StaticBully {
	private static String nodeName;
	private static Integer nodeId;
	private static Integer port;
	private static Boolean election;
	private static Boolean coordinator;

	public StaticBully(String nodeName, Integer nodeId, Integer port) {
		super();
		this.nodeName = nodeName;
		this.nodeId = nodeId;
		this.port = port;
		this.election = Boolean.FALSE;
		this.coordinator = Boolean.FALSE;
	}

	public static String getNodeName() {
		return nodeName;
	}

	public static Integer getNodeId() {
		return nodeId;
	}

	public static Integer getPort() {
		return port;
	}

	public static Boolean getElection() {
		return election;
	}

	public static Boolean getCoordinator() {
		return coordinator;
	}
}
