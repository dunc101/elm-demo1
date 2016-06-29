package com.example;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.ipacc.enterprise.elm.logging.InfinityLogger;
import com.ipacc.enterprise.elm.logging.InfinityLoggerFactory;
import com.ipacc.enterprise.elm.logging.InfinityTransaction;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class ElmDemo1Application {
	private InfinityLogger logger = InfinityLoggerFactory.getInstance();
	
	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping("/begin")
	public String getExampleConsulConfigProperty() {
			String gtid = UUID.randomUUID().toString();
		    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		    params.add("globalTransId", gtid);
		    params.add("clientId", "elm-demo1");
		    System.out.println("Transaction part 1 => Global Trans ID is " + gtid + " clientId starting transaction => elm-demo1");
		    String result = "";
			InfinityTransaction trans = null;
			try {
				trans = new InfinityTransaction.Builder()
					.setGlobalTransactionId(gtid)
					.setTransactionType("TransactionExample")
					.build();
				logger.startTransaction(trans);
				logger.info("message");
				Thread.sleep((long)(Math.random() * 1000));
			    result = restTemplate.postForObject( "http://elm-demo2/orchestration", params, String.class);
			    if (result.contains("Exception")) {
			    	trans.setResolutionFlag(false);
			    	logger.error(result);
			    } else {
					trans.setResolutionFlag(true);
			    }
			} catch (Exception e) {
				trans.setResolutionFlag(false);
				result = e.getMessage();
				logger.error(e.getMessage());
			}
			finally {
				logger.endTransaction(trans);
			}

		    return result;
	}

	@RequestMapping("/loop")
	public String loop() {
		//return "Hello World";
		int i = 0;
		while (true) {
			logger.info("test");
			i++;
			try {
				String result = getExampleConsulConfigProperty();
				//String result = restTemplate.getForObject("http://elm-demo1/begin", String.class);
				System.out.println("Excecuted: " + i + " times => result: " + result);
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(ElmDemo1Application.class, args);
		ElmDemo1Application app = new ElmDemo1Application();
		//app.loop();
	}
}


