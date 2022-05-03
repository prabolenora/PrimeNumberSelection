package com.dc.assignment.PrimeNumberSelection.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ServerDetails {
 public ServletWebServerApplicationContext getServer() {
  return server;
 }

 public void setServer(ServletWebServerApplicationContext server) {
  this.server = server;
 }

 @Autowired
 private ServletWebServerApplicationContext server;
 
}