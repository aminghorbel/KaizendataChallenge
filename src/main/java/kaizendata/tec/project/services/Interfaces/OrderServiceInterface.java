package kaizendata.tec.project.services.Interfaces;

import java.util.List;

import kaizendata.tec.project.entities.Client;
import kaizendata.tec.project.entities.Order;

public interface OrderServiceInterface {

	Order addOrder(Order order);
        
        Order findOrderById(Order order) ; 

	Order updateOrder(Order order);

	List<Order> findAllOrders();

	void removeOrder(Order order);
	
	

	void addClientToOrder(Client client, Order order,boolean called);

	List<Client> findClientsByOrder(Order order);

	List<Client> findClientsNotInOrder(Order order);
	
	void deleteClientFromOrders(Client client) ; 
	
	void deleteClientFromOrder(Client client, Order order,boolean called) ; 
        
        void updateClientInOrders(Client client); 

}
