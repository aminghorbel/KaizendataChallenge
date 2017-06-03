package kaizendata.tec.project.services.imp;

import com.mongodb.BasicDBList;
import java.util.ArrayList;
import java.util.List;

import kaizendata.tec.project.entities.Client;
import kaizendata.tec.project.entities.Order;
import kaizendata.tec.project.services.Interfaces.ClientServiceInterface;
import kaizendata.tec.project.services.Interfaces.OrderServiceInterface;
import kaizendata.tec.project.technical.DababaseInteractions;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

public class ClientServiceImp implements ClientServiceInterface {
    
    private DababaseInteractions dbi;
    private MongoCollection<Document> clients;
    
    private static final OrderServiceInterface osi = new OrderServiceImp();
    
    public ClientServiceImp() {
        dbi = DababaseInteractions.getInstance();
        clients = dbi.getMgDB().getCollection("clients");
        
    }
    
    public Client addClient(Client client) {
        
        Document clientDoc = new Document();
        clientDoc.append("fullName", client.getFullName());
        List<Document> ordersDocs = new ArrayList<Document>();
        
        clientDoc.append("orders", ordersDocs);
        
        clients.insertOne(clientDoc);
        
        ObjectId idClient = (ObjectId) clientDoc.get("_id");
        client.setId(idClient);
        
        return client;
    }
    
    public Client updateClient(Client client) {
        
        clients.updateOne(Filters.eq("_id", client.getId()), new Document(
                "$set", new Document("fullName", client.getFullName())));
        osi.updateClientInOrders(client);
        
        return null;
    }
    
    public void updateOrderInClients(Order order) {
        
        Document query = new Document().append("orders._id", order.getId());
        Document update = new Document("$set", new Document().append("orders.$.description", order.getDescription()));
        
        clients.updateMany(query, update);
        
    }
    
    public List<Client> findAllClients() {
        List<Document> clientsDoc = clients.find().into(
                new ArrayList<>());
        List<Client> clts = new ArrayList<>();
        for (Document cd : clientsDoc) {
            
            Client cl = new Client((ObjectId) cd.get("_id"), cd.getString("fullName"));            
            List<Document> listDoc = (List<Document>) cd.get("orders");
            List<Order> ordrs = new ArrayList<>();            
            for (Document doc : listDoc) {
                ordrs.add(new Order((ObjectId) doc.get("_id"), doc.getString("description")));
            }
            cl.setOrders(ordrs);
            clts.add(cl);
            
        }
        
        return clts;
    }
    
    public void removeClient(Client client) {
        osi.deleteClientFromOrders(client);
        clients.deleteOne(Filters.eq("_id", client.getId()));
        
    }
    
    public void addOrderToClient(Order order, Client client, boolean called) {
        
        BasicDBObject query = new BasicDBObject("_id", client.getId());
        DBObject listItem = new BasicDBObject("orders", new BasicDBObject()
                .append("_id", order.getId()).append("description",
                order.getDescription()));
        BasicDBObject update = new BasicDBObject("$push", listItem);
        clients.updateOne(query, update);
        if (!called) {
            osi.addClientToOrder(client, order, true);
        }
        
    }
    
    public List<Order> findOrdersByClient(Client client) {
        
        return client.getOrders();
    }
    
    public List<Order> findOrdersNotInClient(Client client) {
        List<Order> orders = osi.findAllOrders();
        orders.removeAll(client.getOrders());
        return orders;
    }
    
    public void deleteOrderFromClients(Order order) {
        if (order.getClients() != null) {
            for (Client c : order.getClients()) {
                BasicDBObject query = new BasicDBObject("_id", c.getId());
                BasicDBObject fields = new BasicDBObject("orders",
                        new BasicDBObject("_id", order.getId()));
                BasicDBObject update = new BasicDBObject("$pull", fields);
                
                clients.updateOne(query, update);
            }
        }
        
    }
    
    public void deleteOrderFromClient(Order order, Client client, boolean called) {
        
        BasicDBObject query = new BasicDBObject("_id", client.getId());
        BasicDBObject fields = new BasicDBObject("orders", new BasicDBObject(
                "_id", order.getId()));
        BasicDBObject update = new BasicDBObject("$pull", fields);
        
        clients.updateOne(query, update);
        if (!called) {
            osi.deleteClientFromOrder(client, order, true);
        }
        
    }
    
    @Override
    public Client findClientById(Client client) {
        List<Document> cltDocs = clients.find(Filters.eq("_id", client.getId())).into(new ArrayList<>());
        Document cltDoc = cltDocs.get(0);
        Client cl = new Client((ObjectId) cltDoc.get("_id"), cltDoc.getString("fullName"));        
        List<Document> listDoc = (List<Document>) cltDoc.get("orders");
        List<Order> ordrs = new ArrayList<>();        
        for (Document doc : listDoc) {
            ordrs.add(new Order((ObjectId) doc.get("_id"), doc.getString("description")));
        }
        cl.setOrders(ordrs);
        return cl;        
    }
    
}
