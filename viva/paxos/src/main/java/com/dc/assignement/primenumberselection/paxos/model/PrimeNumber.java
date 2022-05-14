package com.dc.assignement.primenumberselection.paxos.model;

public class PrimeNumber {
	private boolean isPrime;
	public boolean isPrime() {
		return isPrime;
	}
	public void setPrime(boolean isPrime) {
		this.isPrime = isPrime;
	}
	public Integer getRandomNumber() {
		return randomNumber;
	}
	public void setRandomNumber(Integer randomNumber) {
		this.randomNumber = randomNumber;
	}
	private Integer randomNumber;
}
