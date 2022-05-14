package com.dc.assignement.primenumberselection.paxos.model;

public class Bully {
	private static String nodeName;
	private static Integer nodeId;
	private static Integer port;
	private static Boolean election;
	private static Boolean coordinator;
	private static String coordinatorName;
	public static String getNodeName() {
		return nodeName;
	}
	public static void setNodeName(String nodeName) {
		Bully.nodeName = nodeName;
	}
	public static Integer getNodeId() {
		return nodeId;
	}
	public static void setNodeId(Integer nodeId) {
		Bully.nodeId = nodeId;
	}
	public static Integer getPort() {
		return port;
	}
	public static void setPort(Integer port) {
		Bully.port = port;
	}
	public static Boolean getElection() {
		return election;
	}
	public static void setElection(Boolean election) {
		Bully.election = election;
	}
	public static Boolean getCoordinator() {
		return coordinator;
	}
	public static void setCoordinator(Boolean coordinator) {
		Bully.coordinator = coordinator;
	}
	public static String getCoordinatorName() {
		return coordinatorName;
	}
	public static void setCoordinatorName(String coordinatorName) {
		Bully.coordinatorName = coordinatorName;
	}
	

}
