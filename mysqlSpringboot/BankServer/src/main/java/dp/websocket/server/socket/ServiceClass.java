package dp.websocket.server.socket;
import java.util.*;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;



@Component
public class ServiceClass {
	@Autowired
public  	CustomerRepository customerRepository; 



public String addCustomer(Customer customer) {
		customerRepository.save(customer);
	return "Saved";

}


public String debit(Integer id,double balance) {
	
	if(customerRepository.existsById(id)) {
		Customer  customer= customerRepository.findById(id).get();
		customer.debitAmount(balance);
		customerRepository.save(customer);
		return "balance After debited  "+customer.getBalance();
	}
	return "Invalid Account ID";
}

public Iterable<Customer> getAllCustomers(){
	
	return customerRepository.findAll();
}

public String credit(Integer id,double balance )
{ 
	
	
	if(customerRepository.existsById(id)) {
	 Customer customer;
	 customer= customerRepository.findById(id).get();
	 customer.creditAmount(balance);
	 
	customerRepository.save(customer);
	return "Balance Updated "+customerRepository.findById(id).get().getBalance();
}
return "Account ID is not Exists";
	
}


public String deleteCustomer(Integer id) {
	if( customerRepository.existsById(id)) {
		customerRepository.deleteById(id);
		return "deleted";
	}
	return "Invalid Account ID or Customer Not Present";
 }


public   String checkBalance(Integer id){
	
	if(customerRepository.existsById(id)) {
	return 	"Current balance is "+customerRepository.findById(id).get().getBalance();
	}
	return "Invalid Account Number";	
}


public Optional<Customer> getCustomer(Integer id) {
	return customerRepository.findById(id);
}


public  boolean searchbyID(Integer id) {

	return customerRepository.existsById(id);
}


public String hello() {

	return "hello";
}



@PostConstruct
public void init(){
    SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
}


public String updateCustomer(Customer customer) {
customerRepository.save(customer);
	return "Updated Details";
}





}
