package dp.websocket.client;

import java.io.IOException;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;

import dp.websocket.client.socket.SocketConnector;
import dp.websocket.client.socket.WebSocketHandler;

@SpringBootApplication
public class AtmClient {
	

	
	
    public static void main(String[] args) {
    	
        SpringApplication.run(AtmClient.class, args);
    }
}





