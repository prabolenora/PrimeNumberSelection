

2. version of consul. 
```
consul --version
```

3. Start the service registry  
```
consul agent -dev


create proj-https://start.spring.io/
run - mvn spring-boot:run

change port 

mvn spring-boot:run -Dspring-boot.run.arguments="'paxos 1'  --server.port=4000"
mvn spring-boot:run -Dspring-boot.run.arguments="'paxos 2'  --server.port=4002"
mvn spring-boot:run -Dspring-boot.run.arguments="'paxos 3'  --server.port=4003"
mvn spring-boot:run -Dspring-boot.run.arguments="'paxos 4'  --server.port=4004"
mvn spring-boot:run -Dspring-boot.run.arguments="'paxos 5'  --server.port=4005"
  

 
