package com.dc.assignement.primenumberselection.paxos.controller;

import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;

import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.dc.assignement.primenumberselection.paxos.model.Role;

public class ProposerController {
	// get_acceptors_from_service_registry
	public String readAcceptorsFromServiceRegistry() {
		List<Role> lstAcceptor = new ArrayList<>();
		String url = null;
		try {
			System.out.println("\n************************************");
			System.out.println("Getting registered nodes");

			String consulUrl = "http://127.0.0.1:8500/v1/agent/services";

			RestTemplate restTemplate = new RestTemplate();

			Map<String, Object> map = new HashMap<String, Object>();
			ResponseEntity<String> objPorts = restTemplate.getForEntity(consulUrl, String.class);
			JSONParser parser = new JSONParser(objPorts.toString());
			JSONObject json = (JSONObject) parser.parse();
			// dev-need to read values from API
			Role role = new Role();
			role.setRole("Acceptor");
			role.setPort(3098);
			role.setNodeName("paxos 1");
			lstAcceptor.add(role);
			// Map<String, Object> o = (LinkedHashMap<String, Object>) objPorts.getBody();
			url = "http://localhost:" + role.getPort() + "/proposerSchedule";
		} catch (Exception e) {
			// TODO: handle exception
		}
		return url;
	}

}
