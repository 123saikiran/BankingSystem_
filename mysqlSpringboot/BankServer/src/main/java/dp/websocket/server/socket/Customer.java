package dp.websocket.server.socket;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
@Entity
public class Customer {
	@Id
	  @GeneratedValue(strategy=GenerationType.AUTO)
	  private Integer accountID;
	String Name;
	String Address;
	double Balance;
	
	
	public Customer(String name, String address, double balance) {
		super();
		Name = name;
		Address = address;
		Balance = balance;
	}
	
	public Customer() {
		
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getAddress() {
		return Address;
	}
	public void setAddress(String address) {
		Address = address;
	}
	public Integer getAccountID() {
		return accountID;
	}
	public void setAccountID(Integer accountID) {
		this.accountID = accountID;
	}
	public double getBalance() {
		return Balance;
	}
	public void setBalance(double balance) {
		Balance = balance;
	}
	
	
	public String Show() {
		return "Name "+this.getName()+"  Address "+this.getAddress()
				+" Account id "+this.getAccountID()+" balance "+this.getBalance();
	}

	
	  public void debitAmount(double balance){
	        this.Balance=this.Balance-balance;
	    }
	  
	  public void creditAmount(double balance) {
		  this.Balance=this.Balance+balance;
	  }
	

}
