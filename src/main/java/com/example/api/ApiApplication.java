package com.example.api;

import org.apache.commons.lang3.StringUtils;
import java.net.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class ApiApplication {
	
	private static final Logger log = LoggerFactory.getLogger(ApiApplication.class);

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(ApiApplication.class);
	    Environment env = app.run(args).getEnvironment();
	    logApplicationStartup(env);
	}
	
	private static void logApplicationStartup(Environment env) {
	    String protocol = "http";
	    if (env.getProperty("server.ssl.key-store") != null) {
	      protocol = "https";
	    }
	    String serverPort = env.getProperty("server.port");
	    String contextPath = env.getProperty("server.servlet.context-path");
	    if (StringUtils.isBlank(contextPath)) {
	      contextPath = "/";
	    }
	    String hostAddress = "localhost";
	    try {
	      hostAddress = InetAddress.getLocalHost().getHostAddress();
	    } catch (UnknownHostException e) {
	      log.warn("The host name could not be determined, using `localhost` as fallback");
	    }
	    log.info(
	        "\n----------------------------------------------------------\n\t"
	            + "Application '{}' is running! Access URLs:\n\t"
	            + "Local: \t\t{}://localhost:{}{}\n\t"
	            + "External: \t{}://{}:{}{}\n\t"
	            + "Swagger: \t{}://localhost:{}{}swagger-ui.html\n\t"
	            //                    + "Token: \t{}://localhost:{}{}oauth/token\n\t"
	            + "Profile(s): \t{}\n----------------------------------------------------------",
	        env.getProperty("spring.application.name"),
	        protocol, serverPort, contextPath,
	        protocol, hostAddress, serverPort,
	        contextPath, protocol, serverPort,
	        //            contextPath, protocol, serverPort,
	        contextPath,
	        env.getActiveProfiles());
	  }

}
