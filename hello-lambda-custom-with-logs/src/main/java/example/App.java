package example;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaRuntime;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;

public class App {
	
	final static private Logger logger = LoggerFactory.getLogger(App.class);
	
    public static String sayHello(Context context) {
    	try {
    		logger.info("test1 on 1 line\n");
    		logger.info("test2 on\n 2 lines\n");
    		logger.info("test3\n on\n 3 lines\n");
    		
    		DynamoDbClient client = DynamoDbClient.builder().build();
    		Map<String,AttributeValue> key = new HashMap<String, AttributeValue>();
    		key.put("PK", AttributeValue.builder().s("no-pk").build());
    		key.put("SK", AttributeValue.builder().s("no-sk").build());
    		
    		GetItemRequest getItemRequest = GetItemRequest.builder()
    											.tableName("TABLE_GENERIC")
    											.key(key)
    											.build();
    		GetItemResponse getItemResponse = client.getItem(getItemRequest);
    		if (getItemResponse.hasItem()) {
    			return getItemResponse.item().get("creation_time").s();
    		}
    		else {
    			return "Not found";
    		}
    	}
    	catch(Exception ex) {
    		logger.error("An error occurred", ex);
            return "An error occurred";
    	}
    }
    
    
    public static void main(String[] args) {
      System.out.println(sayHello(null));
    }
}