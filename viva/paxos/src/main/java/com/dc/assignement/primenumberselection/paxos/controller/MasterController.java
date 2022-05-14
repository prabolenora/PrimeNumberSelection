package com.dc.assignement.primenumberselection.paxos.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.dc.assignement.primenumberselection.paxos.model.Bully;
import com.dc.assignement.primenumberselection.paxos.model.DevideRange;
import com.dc.assignement.primenumberselection.paxos.model.Node;
import com.dc.assignement.primenumberselection.paxos.model.Role;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MasterController {
	ServiceController serviceController = new ServiceController();
	static ArrayList<Integer> primeNumberList = new ArrayList<>(Arrays.asList(12739, 19001, 3000, 4362, 18149, 31333));

	public ArrayList<String> checkActiveNodes(String coordinator) {
		ArrayList<String> lstHealthNodes = new ArrayList<>();
		try {
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
				nodeServiceList.add(map2.get("Service").toString());

			}
			nodeServiceList.remove(coordinator);		

			for (int i = 0; i < nodeServiceList.size(); i++) {
				System.out.print(nodeServiceList.get(i) + " ");				
				if (serviceController.healthCheck(nodeServiceList.get(i)) == "passing") {
					lstHealthNodes.add(nodeServiceList.get(i));
				}
			}
			System.out.println("Active nodes are ");
			lstHealthNodes.forEach(System.out::println);
			System.out.println("************************************");

		} catch (Exception e) {
			// System.out.println(e);
			// TODO: handle exception
		}
		return lstHealthNodes;

	}

	// This method is used to decide the roles for the other nodes.
	public List<Role> roleDecission(ArrayList<String> lstActiveNodes) {
		System.out.println("\n************************************");
		System.out.println("Deciding roles of nodes");
		String key, learner, node, role, value;
		List<Role> lstRoles = new ArrayList<>();
		lstActiveNodes.clear();
	
		try {
			for (int i = 0; i < 2; i++) {
				Role accepterRole = new Role();
				node = lstActiveNodes.get(i);
				role = "Acceptor";
				key = node;
				value = role;
				accepterRole.setNodeName(key);
				accepterRole.setRole(role);
				lstRoles.add(accepterRole);
			}

			learner = lstActiveNodes.get(2);
			Role learnerRole = new Role();
			learnerRole.setNodeName(learner);
			learnerRole.setRole("Learner");
			lstRoles.add(learnerRole);

			for (int i = 3; i < lstActiveNodes.size(); i++) {
				Role proposerRole = new Role();
				node = lstActiveNodes.get(i);
				role = "Proposer";
				key = node;
				value = role;
				proposerRole.setNodeName(key);
				proposerRole.setRole(role);
				lstRoles.add(proposerRole);
			}

			System.out.println("Roles of nodes");
			for (Role val : lstRoles) {
				System.out.println(val.getNodeName() + " - " + val.getRole());
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		System.out.println("************************************");
		return lstRoles;
	}

	// This method is used to inform each node about their role.
	// dev-need more development
	public List<Role> informRoles(List<Role> roles, String nodeName) {
		System.out.println("\n************************************");
		System.out.println("Inform roles for nodes");
		List<Role> roles2 = new ArrayList<>();
		try {
			// this has port and the node name
			ArrayList<Node> lstRegisteredPorts = serviceController.getRegisteredPortsV1();

			for (Node n : lstRegisteredPorts) {
				for (Role r : roles) {
					if (r.getNodeName().compareTo(n.getNodeName()) == 0) {
						Role role = new Role();
						role.setPort(n.getPort());
						role.setNodeName(n.getNodeName());
						role.setRole(r.getRole());
						roles2.add(role);
						// r.setPort(n.getPort());
					}
				}
			}

			for (Role r : roles2) {
				System.out.println("Port : " + r.getPort() + "	Role : " + r.getRole());
				String url = null;
				String role;
				if (r.getRole() == "Acceptor") {
					url = "http://localhost:" + r.getPort() + "/acceptor";
					role = "Acceptor";
					System.out.println("Calling to " + url);
				} else if (r.getRole() == "Learner") {
					url = "http://localhost:" + r.getPort() + "/learner";
					role = "Learner";
					System.out.println("Calling to " + url);
				} else {
					url = "http://localhost:" + r.getPort() + "/proposer";
					role = "proposer";
					System.out.println("Calling to " + url);
				}
				SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
				requestFactory.setConnectTimeout(10 * 1000);
				requestFactory.setReadTimeout(10 * 1000);

				// RestTemplate rest = new RestTemplate(requestFactory);

				RestTemplate restTemplate = new RestTemplate(requestFactory);
				restTemplate.postForEntity(url, role, Object.class);
				System.out.println();
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		System.out.println("************************************");
		return roles2;
	}

	// This method is used to update the Service Registry after deciding the roles.
	public void updateServiceRegistry(List<Role> roles) {
		System.out.println("\n************************************");
		System.out.println("Update service registry with role details");
		try {
			String consulUrl = "http://localhost:8500/v1/agent/service/register";
			for (Role r : roles) {
				String payload = "{\n" + "\"Name\": \"" + r.getNodeName() + "\",\n" + "\"ID\":\"" + r.getNodeId()
						+ "\",\n" + "\"port\": " + r.getPort() + ",\n" + "\"check\": {\n"
						+ "\"name\": \"Check Counter health " + r.getPort() + "\",\n" + "\"tcp\": \"localhost:"
						+ r.getPort() + "\",\n" + "\"interval\": \"10s\",\n" + " \"timeout\": \"1s\"\n" + "}\n" + "}";

				RestTemplate restTemplate = new RestTemplate();
				restTemplate.put(consulUrl, payload);
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		System.out.println("Successfully Updated service registry with role details");
		System.out.println("************************************");
	}

	// this method is used to schedule the range that they should start dividing
	// based on the number.
	public void scheduleProporsersWork(List<Role> combinedRoles) {
		System.out.println("\n************************************");
		System.out.println("Start scheduling proposer work");
		try {
			int count = 0;
			List<Integer> lstProposerRange = new ArrayList<>();
			for (Role r : combinedRoles) {
				if (r.getRole() == "Proposer") {
					lstProposerRange.add(r.getPort());
					count++;
				}
			}
			lstProposerRange.forEach(System.out::println);
			primeNumberList.forEach(System.out::println);
			Random rand = new Random();
			int randomElement = primeNumberList.get(rand.nextInt(primeNumberList.size()));
			int lengthLstProposerRange = lstProposerRange.size();
			int numberRange = (int) Math.floor(randomElement / lengthLstProposerRange);
			int start = 2;
			for (int each = 0; each < lengthLstProposerRange; each++) {
				DevideRange devideRange = new DevideRange();
				devideRange.setStart(start);
				devideRange.setEnd(start + numberRange);
				devideRange.setRandomPrimeNumber(randomElement);

				String url = "http://localhost:" + lstProposerRange.get(each) + "/proposerSchedule";
				System.out.println("calling to " + url);
				System.out.println("Start : " + start + "	End : " + (start + numberRange) + "		Random Number : "
						+ randomElement);

				// SimpleClientHttpRequestFactory requestFactory = new
				// SimpleClientHttpRequestFactory();
				// requestFactory.setConnectTimeout(10 * 1000);
				// requestFactory.setReadTimeout(10 * 1000);
				//
				// RestTemplate restTemplate = new RestTemplate(requestFactory);
				// restTemplate.postForEntity(url, devideRange, Object.class);
				start += numberRange + 1;
			}
			// start need to remove those
			boolean isPrime = serviceController.isPrimeNumber(randomElement, start, start + numberRange);
			if (isPrime) {
				System.out.println(randomElement + " is a prime number");
			} else {
				System.out.println(randomElement + " is not a prime number");
			}
			// end
		} catch (Exception e) {
			// TODO: handle exception
		}
		System.out.println("************************************");
	}



}
