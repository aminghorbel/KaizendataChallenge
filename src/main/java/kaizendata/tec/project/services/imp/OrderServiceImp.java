package kaizendata.tec.project.services.imp;

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

public class OrderServiceImp implements OrderServiceInterface {

    private DababaseInteractions dbi;
    private MongoCollection<Document> orders;

    private static final ClientServiceInterface csi = new ClientServiceImp();

    public OrderServiceImp() {
        dbi = DababaseInteractions.getInstance();
        orders = dbi.getMgDB().getCollection("orders");

    }

    public Order addOrder(Order order) {
        Document orderDoc = new Document().append("description",
                order.getDescription());
        List<Document> clientDocs = new ArrayList<Document>();

        orderDoc.append("clients", clientDocs);

        orders.insertOne(orderDoc);

        ObjectId idOrder = (ObjectId) orderDoc.get("_id");
        order.setId(idOrder);

        return order;
    }

    public Order updateOrder(Order order) {
        orders.updateOne(Filters.eq("_id", order.getId()), new Document(
                "$set", new Document("description", order.getDescription())));
        csi.updateOrderInClients(order);
        return null;
    }

    @Override
    public List<Order> findAllOrders() {
        List<Document> ordersDoc = orders.find().into(
                new ArrayList<>());

        List<Order> ords = new ArrayList<>();

        for (Document od : ordersDoc) {

            Order or = new Order((ObjectId) od.get("_id"), od.getString("description"));
            List<Document> listDoc = (List<Document>) od.get("clients");
            List<Client> clts = new ArrayList<>();
            for (Document doc : listDoc) {
                clts.add(new Client((ObjectId) doc.get("_id"), doc.getString("fullName")));
            }
            or.setClients(clts);
            ords.add(or);
        }

        return ords;
    }

    public void removeOrder(Order order) {
        csi.deleteOrderFromClients(order);
        orders.deleteMany(Filters.eq("_id", order.getId()));

    }

    public void addClientToOrder(Client client, Order order, boolean called) {

        BasicDBObject query = new BasicDBObject("_id", order.getId());
        DBObject listItem = new BasicDBObject("clients", new BasicDBObject().append("_id", client.getId()).append("fullName", client.getFullName()));
        BasicDBObject update = new BasicDBObject("$push", listItem);
        orders.updateOne(query, update);
        if (!called) {
            csi.addOrderToClient(order, client, true);
        }

    }

    public List<Client> findClientsByOrder(Order order) {

        return order.getClients();
    }

    public List<Client> findClientsNotInOrder(Order order) {
        List<Client> clients = csi.findAllClients();
        clients.removeAll(order.getClients());
        return clients;
    }

    public void deleteClientFromOrders(Client client) {
        if (client.getOrders() != null) {
            for (Order o : client.getOrders()) {
                BasicDBObject query = new BasicDBObject("_id", o.getId());
                BasicDBObject fields = new BasicDBObject("clients",
                        new BasicDBObject("_id", client.getId()));
                BasicDBObject update = new BasicDBObject("$pull", fields);

                orders.updateOne(query, update);
            }
        }

    }

    public void deleteClientFromOrder(Client client, Order order, boolean called) {

        BasicDBObject query = new BasicDBObject("_id", order.getId());
        BasicDBObject fields = new BasicDBObject("clients",
                new BasicDBObject("_id", client.getId()));
        BasicDBObject update = new BasicDBObject("$pull", fields);

        orders.updateOne(query, update);

        if (!called) {
            csi.deleteOrderFromClient(order, client, true);
        }

    }

    @Override
    public Order findOrderById(Order order) {
        List<Document> ordDocs = orders.find(Filters.eq("_id", order.getId())).into(new ArrayList<>());
        Document ordDoc = ordDocs.get(0);
        Order or = new Order((ObjectId) ordDoc.get("_id"), ordDoc.getString("description"));
        List<Document> listDoc = (List<Document>) ordDoc.get("clients");
        List<Client> clts = new ArrayList<>();
        for (Document doc : listDoc) {
            clts.add(new Client((ObjectId) doc.get("_id"), doc.getString("fullName")));
        }
        or.setClients(clts);
        return or;
    }

    @Override
    public void updateClientInOrders(Client client) {
        Document query = new Document().append("clients._id", client.getId());
        Document update = new Document("$set", new Document().append("clients.$.fullName", client.getFullName()));

        orders.updateMany(query, update);
    }

}
