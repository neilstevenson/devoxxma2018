package neil.demo.devoxx;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IQueue;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("account")
@Slf4j
public class AccountController {

	@Autowired
	private HazelcastInstance hazelcastInstance;

	@Value("${my.service1.url}")
	private String service1URL;
	@Value("${my.service2.url}")
	private String service2URL;

    @GetMapping("/index")
    public ModelAndView index(HttpSession httpSession) {
        log.info("index(), session={}", httpSession.getId());

        ModelAndView modelAndView = new ModelAndView("account/index");

        List<String> columns = new ArrayList<>();
        columns.add("Account");
        modelAndView.addObject("columns", columns);
        
        List<String> data = new ArrayList<>();
        String failureReason = "";
        
        RestTemplate restTemplate = new RestTemplate();
        
        String url = "http://" + this.service1URL + "/";
        
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        URI uri = builder.build().encode().toUri();

    	long before = System.currentTimeMillis();
        try {		
        	/*TODO Sort generic on Collection.class
            ResponseEntity<Collection<Integer>> responseEntity =
                    restTemplate.getForEntity(uri, Collection.class);
            */
            @SuppressWarnings("rawtypes")
            ResponseEntity<Collection> responseEntity =
                    restTemplate.getForEntity(uri, Collection.class);
            
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
            	for (Object accountId : responseEntity.getBody()) {
            		data.add(accountId.toString());
            	}
            } else {
                failureReason = responseEntity.getStatusCode().toString();
            }
        } catch (Exception e) {
        	failureReason = e.getMessage();
        	log.error("'{}' : {}", url, failureReason);
        }

        // Allow for clock jump
    	long elapsed = Math.abs(System.currentTimeMillis() - before);
    	log.info("{}ms to call {}", elapsed, url);
        
        modelAndView.addObject("reason1", failureReason);
        modelAndView.addObject("data", data);
        
        return modelAndView;
    }
    
    @GetMapping("/index2")
    public ModelAndView index2(HttpServletRequest httpServletRequest, HttpSession httpSession) {
        String account = httpServletRequest.getParameter("account");
        if (account==null) {
        	account="";
        }
        log.info("index2({}), session={}", account, httpSession.getId());

        ModelAndView modelAndView = new ModelAndView("account/index2");
        modelAndView.addObject("account", account);

        Map<String, String> data1 = new HashMap<>();
    	List<String> columns2 = new ArrayList<>();
    	List<Map<String, String>> data2 = new ArrayList<>();
        String failure1Reason = "";
        String failure2Reason = "";

        RestTemplate restTemplate = new RestTemplate();
        
        String url1 = "http://" + this.service1URL + "/" + account;
        
        UriComponentsBuilder builder1 = UriComponentsBuilder.fromHttpUrl(url1);
        URI uri1 = builder1.build().encode().toUri();

    	long before1 = System.currentTimeMillis();
        try {		
            ResponseEntity<String> responseEntity =
                    restTemplate.getForEntity(uri1, String.class);
            
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
            	ObjectMapper objectMapper = new ObjectMapper();
            	
            	String body = responseEntity.getBody();
            	            	
            	JsonNode jsonNode = objectMapper.readTree(body);

            	Iterator<String> keys = jsonNode.fieldNames();
            	while (keys.hasNext()) {
            		String key = keys.next();
            		
            		JsonNode value = jsonNode.get(key);
            		
            		data1.put(key, value.asText());
            	}
            	
            } else {
                failure1Reason = responseEntity.getStatusCode().toString();
            }
        } catch (Exception e) {
        	failure1Reason = e.getMessage();
        	log.error("'{}' : {}", url1, failure1Reason);
        }
    	long elapsed1 = Math.abs(System.currentTimeMillis() - before1);
    	log.info("{}ms to call {}", elapsed1, url1);

        String url2 = "http://" + this.service2URL + "/accountId/fast/" + account;
        
        UriComponentsBuilder builder2 = UriComponentsBuilder.fromHttpUrl(url2);
        URI uri2 = builder2.build().encode().toUri();

    	long before2 = System.currentTimeMillis();
        try {
        	//TODO Sort generic on Collection.class
            @SuppressWarnings("rawtypes")
			ResponseEntity<Collection> responseEntity =
                    restTemplate.getForEntity(uri2, Collection.class);
            
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
            	ObjectMapper objectMapper = new ObjectMapper();

            	for (Object transaction : responseEntity.getBody()) {
            		
                	JsonNode jsonNode = objectMapper.readTree(transaction.toString());

                    Map<String, String> datum2 = new HashMap<>();

                	Iterator<String> keys = jsonNode.fieldNames();
                	while (keys.hasNext()) {
                		String key = keys.next();

                		// Keep field names, but don't use TreeSet as don't want ordered. HashSet?
                		if (!columns2.contains(key)) {
                    		columns2.add(key);
                		}
                		
                		JsonNode value = jsonNode.get(key);
                		
                		datum2.put(key, value.asText());
                	}
                	
                	data2.add(datum2);
            	}
            	
            } else {
                failure2Reason = responseEntity.getStatusCode().toString();
            }
        } catch (Exception e) {
        	failure2Reason = e.getMessage();
        	log.error("'{}' : {}", url2, failure2Reason);
        }
    	long elapsed2 = Math.abs(System.currentTimeMillis() - before2);
    	log.info("{}ms to call {}", elapsed2, url2);
    	
        modelAndView.addObject("data1", data1);
        modelAndView.addObject("columns2", columns2);
        modelAndView.addObject("data2", data2);
        modelAndView.addObject("reason1", failure1Reason);
        modelAndView.addObject("reason2", failure2Reason);
        
        return modelAndView;
    }
    
    
    @GetMapping("/index3")
    public ModelAndView index3(HttpServletRequest httpServletRequest, HttpSession httpSession) {
        String account = httpServletRequest.getParameter("account");
        String transaction = httpServletRequest.getParameter("transaction");
        if (account==null) {
        	account="";
        }
        if (transaction==null) {
        	transaction="";
        }
        log.info("index3({},{}), session={}", account, transaction, httpSession.getId());

        ModelAndView modelAndView = new ModelAndView("account/index3");
        modelAndView.addObject("account", account);
        modelAndView.addObject("transaction", transaction);
        
        String transactionKey = account + "," + transaction;
        String failure1Reason = "";
        
        IQueue<String> disputesQueue = this.hazelcastInstance.getQueue("disputes");
        
        try {
            boolean success = disputesQueue.offer(transactionKey);
            
            if (!success) {
            	failure1Reason = "Queue '" + disputesQueue.getName() + "' full.";
            }
        } catch (Exception e) {
        	failure1Reason = e.getMessage();
        	log.error("{}", failure1Reason);
        }
        modelAndView.addObject("reason1", failure1Reason);
        
        return modelAndView;
    }
}
