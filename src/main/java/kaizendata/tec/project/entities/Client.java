package kaizendata.tec.project.entities;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

public class Client {
	
	
	private ObjectId id ; 
	private String fullName ;
	private List<Order> orders ; 
	
	
	
	public Client() {
		super();
		orders = new ArrayList<Order>() ; 
	
	}
	
	
	
	public Client(ObjectId id, String fullName) {
		super();
		this.id = id;
		this.fullName = fullName;
	}
	
	
	



	public Client(ObjectId id, String fullName, List<Order> orders) {
		super();
		this.id = id;
		this.fullName = fullName;
		this.orders = orders;
	}



	public ObjectId getId() {
		return id;
	}
	public void setId(ObjectId id) {
		this.id = id;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}



	public List<Order> getOrders() {
		return orders;
	}



	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}



	@Override
	public String toString() {
		return "Client [id=" + id + ", fullName=" + fullName + ", orders="
				+ orders + "]";
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Client other = (Client) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	
	

}
