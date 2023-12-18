package dp.websocket.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import dp.websocket.server.socket.ServiceClass;
import dp.websocket.server.socket.WebSocketServer;
@SpringBootApplication
@EnableWebSocket
public class BankServer extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(BankServer.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(BankServer.class);
    }

    /**
     * This is required for connecting with websocket endpoint. As websocket is annotated with
     * serverendpoint
     * 
     * @return
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Bean
    public WebSocketServer webSocketEndpoint() {
        return new WebSocketServer();
    }


}
