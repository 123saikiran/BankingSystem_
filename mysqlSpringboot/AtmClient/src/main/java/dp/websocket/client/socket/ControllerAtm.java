package dp.websocket.client.socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.TextMessage;

@RestController
public class ControllerAtm {
    @Autowired
    private WebSocketHandler handler;
//	

	
	 @PutMapping(path="/debit")
	 public @ResponseBody String debit(@RequestParam Integer id , @RequestParam String balance) throws Exception {
			handler.session_client.sendMessage(new TextMessage(id+"-"+balance));
			Thread.sleep(100);
			return handler.result;
	 }
	

	@GetMapping(path="/checkBalance")
	public String checkBalance(@RequestParam String  id) throws Exception {
		handler.session_client.sendMessage(new TextMessage(id));
		Thread.sleep(100);
		return handler.result;
	
	}
	
	@RequestMapping(method=RequestMethod.GET ,value="/atm")
	public String getResult() {
		
		return handler.result;
	}
	@GetMapping("/hello")
	public String hi() {
		return "Hello";
	}
	
	
	
	

}
