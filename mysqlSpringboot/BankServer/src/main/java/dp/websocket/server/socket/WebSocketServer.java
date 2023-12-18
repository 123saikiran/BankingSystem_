package dp.websocket.server.socket;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;


@ServerEndpoint("/websocket")
@Component
public class WebSocketServer {
	
	
	private ServiceClass service = SpringContext.getBean(ServiceClass.class);
	    protected Logger logger = LoggerFactory.getLogger(WebSocketServer.class);
	    public Session serverSession;
	    String AccountId="";
	    String AccBal="";
	    @PostConstruct
	    public void init(){
	        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	    }

	@OnMessage
    public void handleMessage(Session session, String message) throws IOException {
        logger.info("Message received from client {}", message);
        
        
        
        
        if(message.matches("[0-9]+")){
        session.getBasicRemote().sendText( service.checkBalance(Integer.parseInt(message)));
        }
        
        else  if(message.matches(".*[a-zA-Z].*") && message.matches(".*[0-9].*")) {
       	 session.getBasicRemote().sendText("Received from Server: " + new StringBuilder(message));
       }
        
        else if(message.matches(".*[a-zA-Z].*")) {
        	session.getBasicRemote().sendText(" Invalid input");
        }
        else {
        	 String[] res=message.split("-");

     		String accountID = res[0];

     		String b = res[1];
     		Double balance = Double.parseDouble(b);
     		if(service.searchbyID(Integer.parseInt(accountID))){
     			session.getBasicRemote().sendText("Received from Server After debiting: " + new StringBuilder(service.debit(Integer.parseInt(accountID), balance)));
     		}
     		else{
     			 session.getBasicRemote().sendText("Received from Serve Cause it is : " + new StringBuilder("Invalid Accont id"));
     		}
        }
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        logger.info("Socket has opened with Session {} \n EndpointConfig {}", session, config);
        this.serverSession=session;
        
    }

    @OnError
    public void errorOcured(Throwable throwable) throws IOException {
        logger.error("Error occured on socket connection", throwable);
    }

    @OnClose
    public void onClose(CloseReason closeReason) {
        logger.info("Socket has closed. Reason {}", closeReason);
    }

}
