package com.example.backendspringboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class ApiApplication {

	@GetMapping("/hello")
	public String sayHello(){
	
		return "Hi, you've found my API!";
	
	}

	@GetMapping("/hello/{name}")
	public String greetByName(@PathVariable String name){

		return "Hi "+name+", you've found my API!";
	}

	@GetMapping("/hostname")
    public ResponseEntity<String> getHostname() {
        String hostname = System.getenv("HOSTNAME");
        if (hostname == null || hostname.isEmpty()) {
            hostname = "HOSTNAME environment variable is not set";
        }
        return ResponseEntity.ok(hostname);
    }

	@GetMapping("/crash")
	public String crashTheApp(){

		//System.exit(1);
		return "crash fixed";

	}

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

}
