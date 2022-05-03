package com.dc.assignment.PrimeNumberSelection.controller;

import javax.security.auth.message.callback.PrivateKeyCallback.Request;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.dc.assignment.PrimeNumberSelection.model.Node;
import com.dc.assignment.PrimeNumberSelection.model.StaticBully;
import com.dc.assignment.PrimeNumberSelection.model.Bully;


@RestController
public class NodeController {

	@GetMapping("/get-node-details")
	public ResponseEntity<Node> getNodeDetails() {
		
		Node node = new Node();

		node.setNodeId(StaticBully.getNodeId());
		node.setNodeName(StaticBully.getNodeName());
		node.setPort(StaticBully.getPort());
		node.setCoordinator(StaticBully.getCoordinator());
		node.setElection(StaticBully.getElection());
		
		return new ResponseEntity<>(node, HttpStatus.OK);
	}
	
	public void coordinatorAnnouncement() {
//		@app.route('/announce', methods=['POST'])
//		def announce_coordinator():
//		    data = request.get_json()
//		    coordinator = data['coordinator']
//		    bully.coordinator = coordinator
//		    print('Coordinator is %s ' % coordinator)
//		    return jsonify({'response': 'OK'}), 200
	}
	
}
