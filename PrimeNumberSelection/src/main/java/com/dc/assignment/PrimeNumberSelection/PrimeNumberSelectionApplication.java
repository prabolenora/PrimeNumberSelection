package com.dc.assignment.PrimeNumberSelection;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import com.dc.assignment.PrimeNumberSelection.controller.ServiceController;
import com.dc.assignment.PrimeNumberSelection.model.Bully;
import com.dc.assignment.PrimeNumberSelection.model.ServerDetails;
import com.dc.assignment.PrimeNumberSelection.model.StaticBully;

@SpringBootApplication
public class PrimeNumberSelectionApplication {

	static ArrayList<Integer> nodeList = new ArrayList<>();
	

	public static void main(String[] args) {
		ConfigurableApplicationContext applicationContext =SpringApplication.run(PrimeNumberSelectionApplication.class, args);
		ServiceController serviceController=new ServiceController();
		boolean wait=true;
		//get node name
		String nodeName= args[0];
		System.out.println("First arguement : " + args[0]);
		//System.out.println("Second arguement : " + args[1]);
		
		//Get running port 
		ServerDetails serverDetails = applicationContext.getBean(ServerDetails.class);
		int port = serverDetails.getServer().getWebServer().getPort();	
		System.out.println("************************************");
		System.out.printf("Web server is running on %d\n", port);
		System.out.println("************************************");
		
		//generate node ID
		Random r = new Random( System.currentTimeMillis() );
	    int nodeId= ((1 + r.nextInt(2)) * 10000 + r.nextInt(10000));
		 
	    //Insert detail to model
	    Bully bully=new Bully();
	    bully.setNodeName(nodeName);
	    bully.setNodeId(nodeId);
	    bully.setPort(port);
	    bully.setElection(false);
	    bully.setCoordinator(false);
	    
	    StaticBully staticBully = new StaticBully(nodeName,nodeId,port);
		
	    //register node in consul
	    serviceController.registerNewNode(bully);
	    nodeList=serviceController.getRegisteredPorts();	 
	    List<Bully> lstNodes=serviceController.getNodeDetails(nodeList);
	    boolean redyForElection=serviceController.readyForElection(nodeList,bully.getElection(),bully.getCoordinator());
	    if (redyForElection || !wait) {
	    	System.out.println("************************************");
	    	System.out.println("Starting election in " + nodeName);
	    	bully.setElection(true);
	    	List<Integer> lstHigherNodes=serviceController.getHigherNodes(lstNodes, nodeId);
	    	System.out.println("Higher node list...");
	    	lstHigherNodes.forEach(System.out::println);
	    	if(lstHigherNodes.size() == 0) {
	    		bully.setCoordinator(true);
	    		bully.setElection(false);
	    		serviceController.announce(nodeName);
	    		System.out.println("Coordinator is "+nodeName);
	    		System.out.println("Election is completed.");
	    	}else {
	    		int status=serviceController.election(lstHigherNodes,nodeId);
	    		System.out.println("Election status " + status);
	    	}
	    	System.out.println("************************************");
	    }
	
	}
	

}


