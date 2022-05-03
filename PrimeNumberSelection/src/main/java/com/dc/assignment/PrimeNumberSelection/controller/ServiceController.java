package com.dc.assignment.PrimeNumberSelection.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.dc.assignment.PrimeNumberSelection.model.Bully;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ServiceController {
	public void registerNewNode(Bully bully) {
		System.out.println("************************************");
		System.out.println("Registering the new node");
		try {
			String payload = "{\n" + "\"Name\": \"" + bully.getNodeName() + "\",\n" 
			  		+ "\"ID\":\""+ bully.getNodeId() + "\",\n" 
			        + "\"port\": " + bully.getPort() + ",\n"
				    + "\"check\": {\n" 
			        	+ "\"name\": \"Check Counter health " + bully.getPort() + "\",\n"
			        	+ "\"tcp\": \"localhost:" + bully.getPort() + "\",\n"
			        	+ "\"interval\": \"10s\",\n" 
			        	+ " \"timeout\": \"1s\"\n" + "}\n"
			    	+ "}";
			
			String consulUrl = "http://localhost:8500/v1/agent/service/register";
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.put(consulUrl, payload);
			
			System.out.println("Node successfully registered.");
		}catch (Exception e) {
			System.out.println("Error : "+e);
		}
		System.out.println("************************************");
	}
	
	public ArrayList<Integer> getRegisteredPorts() {
		
		System.out.println("************************************");
		System.out.println("Getting registered nodes");
		
		String consulUrl = "http://127.0.0.1:8500/v1/agent/services";

		RestTemplate restTemplate = new RestTemplate();
					
		Map<String, Object> map = new HashMap<String, Object>();
		ResponseEntity<Object> objPorts = restTemplate.getForEntity(consulUrl, Object.class);

		Map<String, Object> o = (LinkedHashMap<String, Object>) objPorts.getBody();

		ObjectMapper mapper = new ObjectMapper();
		ArrayList<Integer> nodeList = new ArrayList<>();

		for (String key : o.keySet()) {
			//System.out.println(key);

			Map<String, Object> map2 = mapper.convertValue(o.get(key), Map.class);

			map.put(map2.get("Service").toString(), map2.get("Port"));
			nodeList.add((Integer)map2.get("Port"));

		}
		
		for (int i = 0; i < nodeList.size(); i++)
            System.out.print(nodeList.get(i) + " ");
		
		System.out.println();
		System.out.println("************************************");
		return nodeList;
		
//		ObjectMapper mapper = new ObjectMapper();
//		String jsonString = mapper.writeValueAsString(obj);
//		{16477={ID=16477, Service=prabodha, Tags=[], Meta={}, Port=4001, Address=, Weights={Passing=1, Warning=1}, EnableTagOverride=false, Datacenter=dc1},
//		20382={ID=20382, Service=prabodha, Tags=[], Meta={}, Port=4001, Address=, Weights={Passing=1, Warning=1}, EnableTagOverride=false, Datacenter=dc1}, 
//		23576={ID=23576, Service=prabodha, Tags=[], Meta={}, Port=4000, Address=, Weights={Passing=1, Warning=1}, EnableTagOverride=false, Datacenter=dc1}, 
//		26675={ID=26675, Service=prabodha, Tags=[], Meta={}, Port=4001, Address=, Weights={Passing=1, Warning=1}, EnableTagOverride=false, Datacenter=dc1}}
	}
	
	public List<Bully> getNodeDetails(ArrayList<Integer> nodeList) {
		System.out.println("************************************");
		System.out.println("Getting all nodes details");
		List<Bully> nodeDtos = new LinkedList<Bully>();
		try {
			
			Bully nodeDto1 = new Bully();
			nodeDto1.setNodeId(20245);
			nodeDto1.setNodeName("prabodha");
			nodeDto1.setPort(4000);
			nodeDto1.setCoordinator(false);
			nodeDto1.setElection(false);
			nodeDtos.add(nodeDto1);
			
			Bully nodeDto2 = new Bully();			
			nodeDto2.setNodeId(32456);
			nodeDto2.setNodeName("prabodha 2");
			nodeDto2.setPort(4001);
			nodeDto2.setCoordinator(false);
			nodeDto2.setElection(false);
			nodeDtos.add(nodeDto2);
			
			Bully nodeDto3 = new Bully();
			nodeDto3.setNodeId(20249);
			nodeDto3.setNodeName("prabodha 3");
			nodeDto3.setPort(4003);
			nodeDto3.setCoordinator(false);
			nodeDto3.setElection(false);
			nodeDtos.add(nodeDto3);		
			
			Bully nodeDto4 = new Bully();
			nodeDto4.setNodeId(54324);
			nodeDto4.setNodeName("prabodha 4");
			nodeDto4.setPort(4003);
			nodeDto4.setCoordinator(false);
			nodeDto4.setElection(false);
			nodeDtos.add(nodeDto4);		
			 
            System.out.print("Port Name : "+nodeDto1.getNodeName());
            System.out.print("  Port : "+nodeDto1.getPort().toString());
            System.out.println();
            System.out.print("Port Name : "+nodeDto2.getNodeName());
            System.out.print("  Port : "+nodeDto2.getPort().toString());
            System.out.println();
            System.out.print("Port Name : "+nodeDto3.getNodeName());
            System.out.print("  Port : "+nodeDto3.getPort().toString());
            System.out.println();
            System.out.print("Port Name : "+nodeDto4.getNodeName());
            System.out.print("  Port : "+nodeDto4.getPort().toString());
            System.out.println();
            
           
            
		//	for (int i = 0; i < nodeList.size(); i++) {
//	            System.out.println("Getting node details for port "+nodeList.get(i) + " ");
//	            String consulUrl = "http://localhost:"+nodeList.get(i)+"/get-node-details";
//	            System.out.print(consulUrl);
//	            RestTemplate restTemplate = new RestTemplate();

//	            Node nodeDto = restTemplate.getForObject(consulUrl, Node.class);

//				Node nodeDto = new Node();
//
//				nodeDto.setNodeId(StaticBully.getNodeId());
//				nodeDto.setNodeName(StaticBully.getNodeName());
//				nodeDto.setPort(StaticBully.getPort());
//				nodeDto.setCoordinator(StaticBully.getCoordinator());
//				nodeDto.setElection(StaticBully.getElection());
//				
//	            System.out.print("Port Name : "+nodeDto.getNodeName());
//	            System.out.print("  Port : "+nodeDto.getPort().toString());
//	            System.out.println();
//	            nodeDtos.add(nodeDto);
			//}		
		}catch (Exception e) {
			// TODO: handle exception
		}
		System.out.println("************************************");
		return nodeDtos;
	}

	public boolean readyForElection(ArrayList<Integer> nodeList,Boolean election,Boolean coordinator) {
		boolean returnVal;
		try {
			System.out.println("************************************");	
			System.out.println("Ready for election");	
			List<Bully> lstNodes=getNodeDetails(nodeList);
			List<Boolean> lstCoordinator= new ArrayList<Boolean>();
			List<Boolean> lstElection= new ArrayList<Boolean>();
			
			lstCoordinator.add(coordinator);
		    lstElection.add(election);
			
			for (Bully node : lstNodes) 
			{ 
			    Boolean coordinator_node=node.getCoordinator();
			    Boolean election_node=node.getElection();
			    lstCoordinator.add(coordinator_node);
			    lstElection.add(election_node);
			}
			
			if(lstCoordinator.contains(true) || lstElection.contains(true)) {
				returnVal= false;
			}else {
				returnVal= true;
			}
			
		}catch (Exception e) {
			System.out.println("Error : "+e);	
			returnVal= false;
		}
		System.out.println("************************************");	
		return returnVal;
		
	}
	public List<Integer> getHigherNodes(List<Bully> lstNodes,int nodeId) {
		System.out.println("Getting higher node");
		System.out.println("Compared to node "+nodeId);
		List<Integer> lstHigherNodes= new ArrayList<>();
		try {
			for (Bully node : lstNodes) 
			{ 
				System.out.println("Readinng node "+node.getNodeId());
				if(node.getNodeId() > nodeId) {
					lstHigherNodes.add(node.getNodeId());
				}					
			}						
				    		
		}catch(Exception ex) {
			
		}
		return lstHigherNodes;
	}
	public void announce (String nodeName) {
		ArrayList<Integer> allNodes = getRegisteredPorts();
		
		String details = "{\n" + "\"coordinator\": \"" + nodeName + "\",\n" 
		    			+ "}";
		for (int val : allNodes) {
			String url="http://localhost:"+val+"/announce";
			System.out.println(url);
			RestTemplate restTemplate = new RestTemplate();
	       // String nodeDto = restTemplate.postForEntity(url, details,String.class);
			//need to complete - announce
		}

	}
	public int election(List<Integer> lstHigherNodes ,int nodeId) {
		int returnVal=0;
		try {
			List<Integer> lstStatusCode= new ArrayList<>();
			for(int node : lstHigherNodes) {
				String url="http://localhost:"+node+"/proxy";
				String payload= "{\n" + "\"node_id\": \"" + nodeId + "\",\n" 
								+ "}";
//				post_response = requests.post(url, json=data)
//				status_code_array.append(post_response.status_code)	
				
			}
			lstStatusCode.add(200);
			lstStatusCode.add(404);
			lstStatusCode.add(500);
			lstStatusCode.add(200);
			if(lstStatusCode.contains(200)) {
				returnVal=200;
			}
		}catch(Exception ex) {
			
		}
		return returnVal;
	}
}
