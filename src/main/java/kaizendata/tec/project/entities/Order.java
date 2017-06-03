package kaizendata.tec.project.entities;

import java.util.List;

import org.bson.types.ObjectId;

public class Order {

    private ObjectId id;
    private String description;
    private List<Client> clients;

    public Order() {

    }

    public Order(ObjectId id, String description) {
        super();
        this.id = id;
        this.description = description;
    }

    public Order(ObjectId id, String description, List<Client> clients) {
        super();
        this.id = id;
        this.description = description;
        this.setClients(clients);
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Order [id=" + id + ", description=" + description + "]";
    }

    public List<Client> getClients() {
        return clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
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
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Order other = (Order) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

}
