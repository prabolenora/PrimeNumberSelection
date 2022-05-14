package com.dc.assignement.primenumberselection.paxos.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.dc.assignement.primenumberselection.paxos.model.Bully;
import com.dc.assignement.primenumberselection.paxos.model.DevideRange;
import com.dc.assignement.primenumberselection.paxos.model.Node;
import com.dc.assignement.primenumberselection.paxos.model.PrimeNumber;

@RestController

public class NodeController {
	ServiceController serviceController = new ServiceController();
	
	@GetMapping("/get-node-details")
	public ResponseEntity<Node> getNodeDetails() {

		Node nodeData = new Node();

		nodeData.setNodeId(Bully.getNodeId());
		nodeData.setNodeName(Bully.getNodeName());
		nodeData.setPort(Bully.getPort());
		nodeData.setCoordinator(Bully.getCoordinator());
		nodeData.setElection(Bully.getElection());

		return new ResponseEntity<>(nodeData, HttpStatus.OK);
	}

	@PostMapping("/announce")
	public ResponseEntity<Bully> AnnounceCoordinator(@RequestBody String announceDto) {
		System.out.println("calling to Announce");
		try {
			System.out.println("Inside Announce");
			System.out.println(announceDto);
			// Node.setCoordinatorName(announceDto.get("coordinator").toString());
			System.out.println("Over Announce");
		} catch (Exception e) {
			System.out.println(e);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PostMapping("/acceptor")
	public ResponseEntity<String> acceptor(@RequestBody String role) {
		System.out.println("calling to acceptor");
		try {
			// System.out.println("Inside Announce");
			// System.out.println(role);
			// Node.setCoordinatorName(announceDto.get("coordinator").toString());
			System.out.println("Over Announce");
		} catch (Exception e) {
			System.out.println(e);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PostMapping("/learner")
	public ResponseEntity<String> learner(@RequestBody String role) {
		System.out.println("calling to learner");
		try {
			// System.out.println("Inside Announce");
			// System.out.println(role);
			// Node.setCoordinatorName(announceDto.get("coordinator").toString());
			System.out.println("Over Announce");
		} catch (Exception e) {
			System.out.println(e);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PostMapping("/proposer")
	public ResponseEntity<String> proposer(@RequestBody String role) {
		System.out.println("calling to proposer");
		try {
			// System.out.println("Inside Announce");
			// System.out.println(role);
			// Node.setCoordinatorName(announceDto.get("coordinator").toString());
			System.out.println("Over Announce");
		} catch (Exception e) {
			System.out.println(e);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PostMapping("/proposerSchedule")
	public ResponseEntity<String> proposer(@RequestBody DevideRange devideRange) {
		System.out.println("calling to proposer schedule");
		try {
			int start=devideRange.getStart();
			int end=devideRange.getEnd();
			int randomNumber=devideRange.getRandomPrimeNumber();
			System.out.println("Checking "+randomNumber+ "for prime number");
			boolean isPrime=serviceController.isPrimeNumber(randomNumber, start, end);
			if(isPrime) {
				System.out.println(randomNumber+" is a prime number");
			}else {
				System.out.println(randomNumber+" is not a prime number");
			}
			ProposerController proposerController= new ProposerController();
			String acceptorUrl=proposerController.readAcceptorsFromServiceRegistry();//readAcceptorsFromServiceRegistry

			SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
			requestFactory.setConnectTimeout(10 * 1000);
			requestFactory.setReadTimeout(10 * 1000);

			// RestTemplate rest = new RestTemplate(requestFactory);

			RestTemplate restTemplate = new RestTemplate(requestFactory);
			restTemplate.postForEntity(acceptorUrl,randomNumber, Object.class);
			
			// System.out.println("Inside Announce");
			// System.out.println(role);
			// Node.setCoordinatorName(announceDto.get("coordinator").toString());
			//System.out.println("Over Announce");
		} catch (Exception e) {
			System.out.println(e);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	
	@PostMapping("/primeResult")
	public ResponseEntity<String> primeResult(@RequestBody PrimeNumber primeNumber) {
		try {
			System.out.println("Prime number from the proposer "+primeNumber.getRandomNumber());
			AcceptorController  acceptorController=new AcceptorController();
			String url=acceptorController.getLearnerFromServiceRegistry();//primeResult
			
			SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
			requestFactory.setConnectTimeout(10 * 1000);
			requestFactory.setReadTimeout(10 * 1000);

			// sending result to learner

			RestTemplate restTemplate = new RestTemplate(requestFactory);
			if(primeNumber.isPrime()) {
				restTemplate.postForEntity(url,primeNumber.isPrime(), Object.class);
			}else {
				System.out.println("Verifying the result as it says not a prime number ");
				boolean rst=serviceController.isPrimeNumber(primeNumber.getRandomNumber(), 2, primeNumber.getRandomNumber()-1);
				restTemplate.postForEntity(url,rst, Object.class);
			}
			
		}catch (Exception e) {
			// TODO: handle exception
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PostMapping("/finalResult")
	public void finalResult(@RequestBody List<PrimeNumber> lstPrime) {
		try {
			int count=0;
			for(PrimeNumber val:lstPrime) {
				if (val.isPrime() == false) {
					count++;
				}
			}
			
			if(count>0) {
				System.out.println("Received number is a not prime number");
			}else {
				System.out.println("Received number is a prime number");
			}
			
		}catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	
	@PostMapping("/proxy")
	public ResponseEntity<Node> proxy(@RequestBody Node nodeDto) {

		// create thread
		Counter.createThread();

		Counter.executorService.submit(new Counter(nodeDto.getNodeId()));

		Counter.executorService.shutdown();

		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PostMapping("/response")
	public ResponseEntity<Node> responseNode(@RequestBody Node nodeDto) {

		serviceController.getNodeResponse(nodeDto);

		return new ResponseEntity<>(HttpStatus.OK);
	}
	

}
