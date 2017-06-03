package kaizendata.tec.project.services.Interfaces;

import java.util.List;

import kaizendata.tec.project.entities.Client;
import kaizendata.tec.project.entities.Order;

public interface ClientServiceInterface {

    Client addClient(Client client);

    Client updateClient(Client client);

    Client findClientById(Client client);

    public void updateOrderInClients(Order order);

    List<Client> findAllClients();

    void removeClient(Client client);

    void addOrderToClient(Order order, Client client, boolean called);

    List<Order> findOrdersByClient(Client client);

    List<Order> findOrdersNotInClient(Client client);

    void deleteOrderFromClients(Order order);

    void deleteOrderFromClient(Order order, Client client, boolean called);

}
