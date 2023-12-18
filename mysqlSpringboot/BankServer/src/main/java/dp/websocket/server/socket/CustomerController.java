package dp.websocket.server.socket;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path="/bank")


public class CustomerController {
	@Autowired
public 	ServiceClass service; 

	
	//this will add the customer into repo
	@PostMapping(path="/addCustomer") // Map ONLY POST Requests
	  public @ResponseBody String addNewCustomer (@RequestBody Customer customer) {
		
		
		return service.addCustomer(customer);
	  }
	
	
	
	//this will update customer details 
	
	@PutMapping(path="/updateCustomer")
	public @ResponseBody String updateCustomer(@RequestBody Customer customer) {
		
		return service.updateCustomer(customer);
	}
	
	
	//gets all customers present in repo
	
	 @GetMapping(path="/getAllCustomers")
	  public @ResponseBody Iterable<Customer> getAllUsers() {
	    // This returns a JSON 
	    return 	service.getAllCustomers();
	  }
	 
	
	 
	 
	 //this will get particular customer using id 
	 @GetMapping(path="/getCustomer")
	 public @ResponseBody Optional<Customer> getCustomer(@RequestParam Integer id) {
		 return service.getCustomer(id);
	 }
	 
	 //This method getBalance
	 
	 @GetMapping(path="/getBalance")
	 public @ResponseBody String getBalance(@RequestParam Integer id) {
		 return service.checkBalance(id);
	 }
	 
	 //this will credits balance 
	 
	 
	 @PutMapping(path="/credit")
	 public @ResponseBody  String  credit(@RequestParam Integer id,@RequestParam double balance) {
		return service.credit(id, balance);
	 }
	 
	 //this will debit balance from account
	 @PutMapping(path="/debit")
	 public @ResponseBody String debit(@RequestParam Integer id , @RequestParam double balance) {
		 return 		service.debit(id, balance); 
	 }
	 
	 
	 //this deletes customer 
	 
	@DeleteMapping(path="/deleteCustomer")
	 public @ResponseBody String deleteCustomer(@RequestParam Integer id)
	 {
		
		return service.deleteCustomer(id);
	 }
		}
