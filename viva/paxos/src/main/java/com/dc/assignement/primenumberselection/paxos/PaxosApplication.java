package com.dc.assignement.primenumberselection.paxos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.dc.assignement.primenumberselection.paxos.controller.MasterController;
import com.dc.assignement.primenumberselection.paxos.controller.ServiceController;
import com.dc.assignement.primenumberselection.paxos.model.Bully;
import com.dc.assignement.primenumberselection.paxos.model.Node;
import com.dc.assignement.primenumberselection.paxos.model.Role;
import com.dc.assignement.primenumberselection.paxos.model.ServerDetails;

@EnableSidecar 
@SpringBootApplication
public class PaxosApplication {
	static ArrayList<Integer> nodeList = new ArrayList<>();
	static ArrayList<Integer> primeNumberList = new ArrayList<>(Arrays.asList(12739, 19001, 3000, 4362, 18149, 31333));

	public static void main(String[] args) {
		try {
			ConfigurableApplicationContext applicationContext = SpringApplication.run(PaxosApplication.class, args);
			boolean wait = true;
			ServiceController serviceController = new ServiceController();
			// get node name
			String nodeName = args[0];
			System.out.println("First arguement : " + args[0]);
		
			// Get running port
			ServerDetails serverDetails = applicationContext.getBean(ServerDetails.class);
			int port = serverDetails.getServer().getWebServer().getPort();
			System.out.println("************************************");
			System.out.printf("Web server is running on %d\n", port);
			System.out.println("************************************");

			// generate node ID
			Random r = new Random(System.currentTimeMillis());
			int nodeId = ((1 + r.nextInt(2)) * 10000 + r.nextInt(10000));

			// Insert detail to model
			Bully.setNodeName(nodeName);
			Bully.setNodeId(nodeId);
			Bully.setPort(port);
			Bully.setElection(false);
			Bully.setCoordinator(false);

			// Register the node in service registry
			int status = serviceController.registerPort();

			if (status == 200) {

				if (wait) {
					long start = System.currentTimeMillis();
					try {
						//Thread.sleep(30000);
						Thread.sleep(3000);
						System.out.println("Waiting for " + (System.currentTimeMillis() - start) + "ms");
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				nodeList = serviceController.getRegisteredPorts();
				// Get all node details
				List<Node> lstNodes = serviceController.getNodeDetails(nodeList);
				System.out.println("\n************************************");
				for (Node node : lstNodes) {
					System.out.println("Node Name : " + node.getNodeName() + "	Node ID : " + node.getNodeId()
							+ "	Port : " + node.getPort());
				}
				System.out.println("************************************");

				// Checking ongoing election
				boolean redyForElection = serviceController.readyForElection(nodeList, Bully.getElection(),
						Bully.getCoordinator());

				if (redyForElection || !wait) {
					System.out.println("\n************************************");
					System.out.println("Starting election on " + nodeName);
					Bully.setElection(true);

					List<Integer> lstHigherNodes = serviceController.getHigherNodes(lstNodes, nodeId);
					System.out.println("Higher node list...");
					lstHigherNodes.forEach(System.out::println);

					if (lstHigherNodes.size() == 0) {
						Bully.setCoordinator(true);
						Bully.setElection(false);
						Bully.setCoordinatorName(nodeName);
						
						serviceController.announce(nodeName);
						System.out.println("*****Coordinator is " + Bully.getCoordinatorName());
						System.out.println("Election is completed.");
						masterWorks();
					} else {
						Bully.setCoordinator(false);
						Bully.setElection(true);
						serviceController.election(nodeList);
					}

					System.out.println("************************************");

				}
			}
		} catch (BeansException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
		}
	}
	
	public static void selectCoordinator() {
		try {
			
		}catch(Exception e) {
			
		}
	}
	
	public static void masterWorks() {
		try {
			MasterController master=new MasterController();
			ArrayList<String> lstActiveNodes=master.checkActiveNodes(Bully.getCoordinatorName());
			List<Role> roles=master.roleDecission(lstActiveNodes);
			List<Role> combinedRoles = master.informRoles(roles, Bully.getCoordinatorName());
			master.updateServiceRegistry(combinedRoles);
			master.scheduleProporsersWork(combinedRoles);
			
		}catch (Exception e) {
			System.out.println(e);
		}
	}
}
