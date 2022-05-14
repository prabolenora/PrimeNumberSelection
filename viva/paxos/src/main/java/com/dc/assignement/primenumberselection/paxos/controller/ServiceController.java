package com.dc.assignement.primenumberselection.paxos.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.dc.assignement.primenumberselection.paxos.model.Bully;
import com.dc.assignement.primenumberselection.paxos.model.Node;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ServiceController {
	// This method is used to register the service in the service registry
	public int registerPort() {
		int returnval = 404;
		try {
			System.out.println("\n************************************");
			System.out.println("Registering the new node");

			String payload = "{\n" + "\"Name\": \"" + Bully.getNodeName() + "\",\n" + "\"ID\":\"" + Bully.getNodeId()
					+ "\",\n" + "\"port\": " + Bully.getPort() + ",\n" + "\"check\": {\n"
					+ "\"name\": \"Check Counter health " + Bully.getPort() + "\",\n" + "\"tcp\": \"localhost:"
					+ Bully.getPort() + "\",\n" + "\"interval\": \"10s\",\n" + " \"timeout\": \"1s\"\n" + "}\n" + "}";

			String consulUrl = "http://localhost:8500/v1/agent/service/register";
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.put(consulUrl, payload);
			returnval = 200;
			System.out.println("Node successfully registered.");
			System.out.println("************************************");
		} catch (Exception e) {
			// TODO: handle exception
		}
		return returnval;
	}

	// # get ports of all the registered nodes from the service registry
	public ArrayList<Integer> getRegisteredPorts() {

		System.out.println("\n************************************");
		System.out.println("Getting registered nodes");

		String consulUrl = "http://127.0.0.1:8500/v1/agent/services";

		RestTemplate restTemplate = new RestTemplate();

		Map<String, Object> map = new HashMap<String, Object>();
		ResponseEntity<Object> objPorts = restTemplate.getForEntity(consulUrl, Object.class);

		Map<String, Object> o = (LinkedHashMap<String, Object>) objPorts.getBody();

		ObjectMapper mapper = new ObjectMapper();
		ArrayList<Integer> nodeList = new ArrayList<>();
		ArrayList<String> nodeServiceList = new ArrayList<>();

		for (String key : o.keySet()) {
			// System.out.println(key);
			// Map<String, Object> map3 =mapper.convertValue(o.get(key), Map.class);
			Map<String, Object> map2 = mapper.convertValue(o.get(key), Map.class);

			map.put(map2.get("Service").toString(), map2.get("Port"));
			nodeList.add((Integer) map2.get("Port"));
			nodeServiceList.add((String) map2.get("Service"));
		}

		for (int i = 0; i < nodeList.size(); i++)
			System.out.print(nodeList.get(i) + " ");

		System.out.println("\nprinting services");
		for (int i = 0; i < nodeServiceList.size(); i++)
			System.out.print(nodeServiceList.get(i) + " ");

		System.out.println();
		System.out.println("************************************");
		return nodeList;
	}

	public ArrayList<Node> getRegisteredPortsV1() {

		System.out.println("\n************************************");
		System.out.println("Getting registered nodes");

		String consulUrl = "http://127.0.0.1:8500/v1/agent/services";

		RestTemplate restTemplate = new RestTemplate();

		Map<String, Object> map = new HashMap<String, Object>();
		ResponseEntity<Object> objPorts = restTemplate.getForEntity(consulUrl, Object.class);

		Map<String, Object> o = (LinkedHashMap<String, Object>) objPorts.getBody();

		ObjectMapper mapper = new ObjectMapper();
		// ArrayList<Integer> nodeList = new ArrayList<>();
		// ArrayList<String> nodeServiceList = new ArrayList<>();
		ArrayList<Node> lstRegNodes = new ArrayList<>();

		for (String key : o.keySet()) {
			// System.out.println(key);
			// Map<String, Object> map3 =mapper.convertValue(o.get(key), Map.class);
			Map<String, Object> map2 = mapper.convertValue(o.get(key), Map.class);

			map.put(map2.get("Service").toString(), map2.get("Port"));
			Node node = new Node();
			node.setPort((Integer) map2.get("Port"));
			node.setNodeName((String) map2.get("Service"));
			lstRegNodes.add(node);
			// nodeList.add((Integer) map2.get("Port"));
			// nodeServiceList.add((String) map2.get("Service"));
		}



		System.out.println("\nprinting services");
		for (Node n : lstRegNodes) {
			System.out.println("port : " + n.getPort() + "	node name : " + n.getNodeName());
		}
		
		System.out.println();
		System.out.println("************************************");
		return lstRegNodes;
	}

	// getting all node details from each nodes
	public List<Node> getNodeDetails(ArrayList<Integer> nodeList) {
		// System.out.println();
		System.out.println("\n************************************");
		System.out.println("Reading all nodes details");
		List<Node> nodeDtos = new LinkedList<Node>();
		try {
			for (int i = 0; i < nodeList.size(); i++) {
				String consulUrl = "http://localhost:" + nodeList.get(i) + "/get-node-details";
				RestTemplate restTemplate = new RestTemplate();
				Node nodeDto = restTemplate.getForObject(consulUrl, Node.class);

				System.out.print("Port Name : " + nodeDto.getNodeName());
				System.out.print("  Port : " + nodeDto.getPort().toString());
				System.out.println();
				nodeDtos.add(nodeDto);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		System.out.println("************************************");
		return nodeDtos;
	}

	// this method returns if the cluster is ready for the election
	public boolean readyForElection(ArrayList<Integer> nodeList, Boolean election, Boolean coordinator) {
		boolean returnVal;
		try {
			System.out.println("\n************************************");
			System.out.println("Checking ongoing elections");
			List<Node> lstNodes = getNodeDetails(nodeList);
			List<Boolean> lstCoordinator = new ArrayList<Boolean>();
			List<Boolean> lstElection = new ArrayList<Boolean>();

			lstCoordinator.add(coordinator);
			lstElection.add(election);

			for (Node node : lstNodes) {
				Boolean coordinator_node = node.getCoordinator();
				Boolean election_node = node.getElection();
				lstCoordinator.add(coordinator_node);
				lstElection.add(election_node);
			}

			if (lstCoordinator.contains(true) || lstElection.contains(true)) {
				System.out.println("There ongoing elections");
				returnVal = false;
			} else {
				System.out.println("There is no ongoing elections");
				returnVal = true;
			}

		} catch (Exception e) {
			System.out.println("Error : " + e);
			returnVal = false;
		}
		System.out.println("************************************");
		return returnVal;

	}

	// get the highest node list
	public List<Integer> getHigherNodes(List<Node> lstNodes, int nodeId) {

		System.out.println("Getting higher node list");
		System.out.println("Comparing to node " + nodeId);
		List<Integer> lstHigherNodes = new ArrayList<>();
		try {
			for (Node node : lstNodes) {
				System.out.println("Readinng node " + node.getNodeId());
				if (node.getNodeId() > nodeId) {
					lstHigherNodes.add(node.getNodeId());
				}
			}

		} catch (Exception ex) {

		}

		return lstHigherNodes;
	}

	// update other nodes with coordinator
	public void announce(String nodeName) {
		try {
			ArrayList<Integer> allNodes = getRegisteredPorts();

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("coordinator", nodeName);
			String nodeName1 = nodeName;

			for (int val : allNodes) {
				String url = "http://localhost:" + val + "/announce";
				// System.out.println(url);
				// System.out.println(jsonObject.get("coordinator").toString());
				SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
				requestFactory.setConnectTimeout(10 * 1000);
				requestFactory.setReadTimeout(10 * 1000);

				// RestTemplate rest = new RestTemplate(requestFactory);

				RestTemplate restTemplate = new RestTemplate(requestFactory);
				restTemplate.postForEntity(url, nodeName1, Object.class);

				// need to complete - announce
			}
		} catch (Exception e) {
			System.out.println("inside controller");
			System.out.println(e);
		}

	}

	//
	public void election(List<Integer> higherNodes) {
		System.out.println("\n************************************");
		System.out.println("Starting elections");
		List<Integer> statusCodes;

		for (Integer higherNode : higherNodes) {

			String url = Constant.SERVER_URL + higherNode + Constant.PROXY_URL;

			System.out.println("proxy url : " + url);

			Node nodeDto = new Node();

			nodeDto.setNodeId(Bully.getNodeId());

			RestTemplate restTemplate = new RestTemplate();

			restTemplate.postForObject(url, nodeDto, Object.class);
			System.out.println("************************************");
		}

	}
	// Check health of nodes
	public String healthCheck(String service) {
		String serviceStatus = null;
		try {
			System.out.println("\n************************************");
			System.out.println("checking health of " + service);
			String consulUrl = "http://localhost:8500/v1/agent/health/service/name/" + service;
			RestTemplate restTemplate = new RestTemplate();

			// Map<String, Object> map = new HashMap<String, Object>();
			ResponseEntity<Object> objPorts = restTemplate.getForEntity(consulUrl, Object.class);
			// ResponseEntity<JASONObj> objPorts = restTemplate.getForEntity(consulUrl,
			// Object.class);

			Map<String, Object> o = (LinkedHashMap<String, Object>) objPorts.getBody();

			ObjectMapper mapper = new ObjectMapper();
			// ArrayList<Integer> nodeList = new ArrayList<>();
			String lstAggregatedStatus = null;

			for (String key : o.keySet()) {
				System.out.println("printing key");
				System.out.println(key);
				// Map<String, Object> map3 =mapper.convertValue(o.get(key), Map.class);
				Map<String, Object> map2 = mapper.convertValue(o.get(key), Map.class);

				// map.put(map2.get("Service").toString(), map2.get("Port"));
				// nodeList.add((Integer) map2.get("Port"));
				lstAggregatedStatus = map2.get("AggregatedStatus").toString();
			}

			if (objPorts.getStatusCodeValue() == 503 && lstAggregatedStatus == "critical") {
				serviceStatus = "crashed";
			}
			// System.out.println("Service status is "+serviceStatus);

		} catch (Exception e) {
			// generating an error
			// System.out.println(e);
		}
		System.out.println("************************************");
		return serviceStatus;
	}

	// check it is prime number
	public boolean isPrimeNumber(int randomNumber, int start, int end) {
		System.out.println("\n************************************");
		System.out.println("checking number whether it is a prime or not");
		boolean isPrime = true;
		try {

			for (int i = start; i < randomNumber; i++) {
				if ((randomNumber % i) == 0 && randomNumber != i) {
					isPrime = false;
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		System.out.println("************************************");
		return isPrime;
	}
	
	public void getNodeResponse(Node nodeDto) {

		Integer incomingNodeId = nodeDto.getNodeId();

		if (Bully.getNodeId() > incomingNodeId) {
			Bully.setElection(Boolean.FALSE);
		}

	}
}
