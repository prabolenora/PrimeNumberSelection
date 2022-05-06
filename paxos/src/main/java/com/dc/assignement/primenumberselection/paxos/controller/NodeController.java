package com.dc.assignement.primenumberselection.paxos.controller;

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
			//dev-need to call to api for acceptorUrl (proposer_schedule)
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
	public ResponseEntity<String> primeResult(@RequestBody int randomNumber) {
		try {
			System.out.println("Prime number from the proposer "+randomNumber);
			AcceptorController  acceptorController=new AcceptorController();
			String url=acceptorController.getLearnerFromServiceRegistry();//primeResult
			
			SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
			requestFactory.setConnectTimeout(10 * 1000);
			requestFactory.setReadTimeout(10 * 1000);

			// RestTemplate rest = new RestTemplate(requestFactory);

			RestTemplate restTemplate = new RestTemplate(requestFactory);
			restTemplate.postForEntity(url,randomNumber, Object.class);
		}catch (Exception e) {
			// TODO: handle exception
		}
		return new ResponseEntity<>(HttpStatus.OK);
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
	
//	@app.route('/primeResult', methods=['POST'])
//	def prime_result():
//	    data = request.get_json()
//	    print('prime result from proposer', data['primeResult'])
//	    url = get_learner_from_service_registry()
//	    result = data['primeResult']
//	    result_string = {"result": result}
//	    print('Sending the result to learner: %s' % url)
//	    if 'is a prime number' in result:
//	        requests.post(url, json=result_string)
//	    else:
//	        print("Verifying the result as it says not a prime number......")
//	        number = int(result.split()[0])
//	        verified_result = is_prime_number(number, 2, number - 1)
//	        verified_result_string = {"result": verified_result}
//	        requests.post(url, json=verified_result_string)
//	    return jsonify({'response': 'OK'}), 200
	
//	def final_result():
//	    data = request.get_json()
//	    number = data['result'].split()[0]
//	    print('prime result from acceptor', data['result'])
//
//	    learner_result_array.append(data['result'])
//	    print(learner_result_array)
//
//	    count = 0
//	    for each_result in learner_result_array:
//	        if 'not a prime number' in each_result:
//	            count = count + 1
//
//	    if count > 0:
//	        final = '%s is not prime' % number
//	        print(final)
//	    else:
//	        final = '%s is prime' % number
//	        print(final)
//
//	    print('-------Final Result-----------')
//	    number_of_msgs = len(learner_result_array)
//	    print('Number of messages received from acceptors: %s' % number_of_msgs)
//	    print('Number of messages that says number is not prime: %s' % count)
//	    print(final)
//
//	    return jsonify({'response': final}), 200
}
