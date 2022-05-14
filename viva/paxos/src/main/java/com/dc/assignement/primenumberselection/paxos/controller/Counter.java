package com.dc.assignement.primenumberselection.paxos.controller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.web.client.RestTemplate;

import com.dc.assignement.primenumberselection.paxos.model.Bully;
import com.dc.assignement.primenumberselection.paxos.model.Node;

public class Counter implements Runnable {

	public static int counter = 0;
	private static final Object lock = new Object();
	public static ExecutorService executorService;
	private Integer nodeId;

	public static void createThread() {
		executorService = Executors.newFixedThreadPool(5);
	}

	public Counter(Integer nodeId) {
		super();
		this.nodeId = nodeId;
	}

	@Override
	public void run() {
		increaseCounter();

	}

	private void increaseCounter() {
		synchronized (lock) {

			counter++;

			System.out.println(Thread.currentThread().getName() + " : " + counter);

			if (Counter.counter == 1) {

				System.out.println("proxy node id : " + nodeId);

				String url = Constant.SERVER_URL + Bully.getPort() + Constant.GET_NODE_RESPONSE_URL;

				System.out.println("response url : " + url);

				Node nodeDto = new Node();

				nodeDto.setNodeId(nodeId);

				RestTemplate restTemplate = new RestTemplate();

				restTemplate.postForObject(url, nodeDto, Object.class);

			}

		}
	}
}
