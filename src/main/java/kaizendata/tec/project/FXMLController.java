package kaizendata.tec.project;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import kaizendata.tec.project.entities.Client;
import kaizendata.tec.project.entities.Order;
import kaizendata.tec.project.helpers.SquareDetails;
import kaizendata.tec.project.services.Interfaces.ClientServiceInterface;
import kaizendata.tec.project.services.Interfaces.OrderServiceInterface;
import kaizendata.tec.project.services.imp.ClientServiceImp;
import kaizendata.tec.project.services.imp.OrderServiceImp;

public class FXMLController implements Initializable {


    private ClientServiceInterface csi;
    private OrderServiceInterface osi;

    @FXML
    private StackPane stackPane;
    @FXML
    private Pane mainPane;
    @FXML
    private AnchorPane mainAnchor;
    @FXML
    private VBox clientsContiener;
    private JFXRadioButton radioNom;
    @FXML
    private JFXButton cancelButton;
    @FXML
    private JFXButton addUpdateClient;
    private Client selectedClient;
    @FXML
    private JFXTextField clientFullNameTF;
    @FXML
    private JFXTextField orderDescriptionTF;
    @FXML
    private JFXButton cancelButtonOrder;
    @FXML
    private JFXButton addUpdateOrderB;
    @FXML
    private VBox orderContiener;
    private Order selectedOrder;
    @FXML
    private Pane paneOrderSelection;
    @FXML
    private JFXButton returnHomeOrder;
    @FXML
    private VBox associetedClientVB;
    @FXML
    private VBox notAssocietedClientVB;
    @FXML
    private Label productNameLabel;
    @FXML
    private Pane paneClientSelection;
    @FXML
    private JFXButton returnHomeClient;
    @FXML
    private Label clientNameLabel;
    @FXML
    private VBox associetedOrderVB;
    @FXML
    private VBox notAssocietedOrderVB1;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        selectedClient = new Client();
        selectedOrder = new Order();

        osi = new OrderServiceImp();
        csi = new ClientServiceImp();

        loadClientData();
        loadOrderData();

    }

    /**
     *
     *
     * @param event : donwn up scrolling clients
     *
     *
     *
     */
    @FXML
    private void moveUpDown(ScrollEvent event) {

        if (clientsContiener.getHeight() > 990) {

            if (event.getDeltaY() < 0) {

                if ((clientsContiener.getLayoutY() + clientsContiener.getHeight()) > 990) {
                    clientsContiener.setLayoutY(event.getDeltaY() + clientsContiener.getLayoutY());
                }

            } else {
                if (clientsContiener.getLayoutY() < 116) {
                    clientsContiener.setLayoutY(event.getDeltaY() + clientsContiener.getLayoutY());
                }
            }

        }

    }

    /**
     *
     * @param object : this is the object that you will delete
     * @param x : where the popup should be displayed
     * @param y : where the popup should be displayed
     * @param pm : defenition of the method
     */
    private void initPopup(Object object, Double x, Double y) {
        JFXPopup popup = new JFXPopup();
        JFXButton update = new JFXButton("update");
        JFXButton delete = new JFXButton("delete");
        JFXButton select = new JFXButton("select");

        update.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            popup.hide();
            if (object.getClass().equals(Client.class)) {
                cancelButton.setVisible(true);
                addUpdateClient.setText("update");
                addUpdateClient.setStyle("-fx-background-color : #e51c23 ; -fx-text-fill: white ;");
                clientFullNameTF.setText(((Client) object).getFullName());
                selectedClient = (Client) object;

            } else {

                cancelButtonOrder.setVisible(true);
                addUpdateOrderB.setText("update");
                addUpdateOrderB.setStyle("-fx-background-color : #e51c23 ; -fx-text-fill: white ;");
                orderDescriptionTF.setText(((Order) object).getDescription());
                selectedOrder = (Order) object;

            }

        });

        delete.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            if (object.getClass().equals(Client.class)) {
                selectedClient = (Client) object;
            } else {
                selectedOrder = (Order) object;
            }

            popup.hide();
            String classMessage;
            if (object.getClass().equals(Client.class)) {
                classMessage = "client";
            } else {
                classMessage = "order";
            }
            JFXDialogLayout content = new JFXDialogLayout();

            JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);

            content.setHeading(new Text("Do you really want to delete this " + classMessage + " ?"));
            content.setBody(new Text("\n\nBy confirming this option your " + classMessage + " will definitely \ndeleted from your database \n \nDo you really want to continue?\n\n"));

            JFXButton confirm = new JFXButton("confirm");
            confirm.setOnAction(ev -> {
                if (classMessage.equals("client")) {
                    selectedClient = csi.findClientById(selectedClient);
                    csi.removeClient(selectedClient);
                    loadClientData();
                } else {
                    selectedOrder = osi.findOrderById(selectedOrder);
                    osi.removeOrder(selectedOrder);
                    loadOrderData();
                }
                dialog.close();

            });

            JFXButton cancel = new JFXButton("cancel");
            cancel.setOnAction(ev -> {
                selectedClient = new Client();
                selectedOrder = new Order();
                dialog.close();
            });

            content.setActions(cancel, confirm);

            dialog.show();

        });

        select.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {

            popup.hide();

            if (object.getClass().equals(Client.class)) {

                paneClientSelection.setVisible(true);
                selectedClient = (Client) object;
                clientNameLabel.setText(selectedClient.getFullName());
                loadAssocietedOrderVB();
                loadNotassocietedOrderVB();

            } else {
                paneOrderSelection.setVisible(true);
                selectedOrder = (Order) object;
                productNameLabel.setText(selectedOrder.getDescription());
                loadAssocietedClientVB();
                loadNotassocietedClientVB();

            }

        });

        select.setPrefSize(100, 50);
        update.setPrefSize(100, 50);
        delete.setPrefSize(100, 50);

        popup.setPopupContent(new VBox(update, delete, select));

        popup.show(mainPane, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT, x, y);

    }

    @FXML
    private void cancelEdit(ActionEvent event) {
        cancelButton.setVisible(false);
        addUpdateClient.setText("add");
        addUpdateClient.setStyle("-fx-background-color : #FFF ; -fx-text-fill: black ;");
        selectedClient = new Client();
        clientFullNameTF.setText("");

    }

    @FXML
    private void addUpdateClient(ActionEvent event) {

        if (cancelButton.isVisible()) {
            cancelButton.setVisible(false);
            addUpdateClient.setText("add");
            addUpdateClient.setStyle("-fx-background-color : #FFF ; -fx-text-fill: black ;");

            selectedClient.setFullName(clientFullNameTF.getText());

            csi.updateClient(selectedClient);

        } else {
            selectedClient.setFullName(clientFullNameTF.getText());
            csi.addClient(selectedClient);
        }

        clientFullNameTF.setText("");
        loadClientData();

    }

    private void loadClientData() {

        List<Client> clients = csi.findAllClients();

        clientsContiener.getChildren().remove(0, clientsContiener.getChildren().size());

        try {
            for (Client c : clients) {

                HBox hbox = SquareDetails.squareCreator(c);

                hbox.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {

                    initPopup(c, e.getSceneX(), e.getSceneY());

                });
                clientsContiener.getChildren().add(hbox);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void cancelEditOrder(ActionEvent event) {

        cancelButtonOrder.setVisible(false);
        addUpdateOrderB.setText("add");
        addUpdateOrderB.setStyle("-fx-background-color : #FFF ; -fx-text-fill: black ;");
        selectedOrder = new Order();
        orderDescriptionTF.setText("");
    }

    @FXML
    private void addUpdateOrder(ActionEvent event) {

        if (cancelButtonOrder.isVisible()) {
            cancelButtonOrder.setVisible(false);
            addUpdateOrderB.setText("add");
            addUpdateOrderB.setStyle("-fx-background-color : #FFF ; -fx-text-fill: black ;");
            selectedOrder.setDescription(orderDescriptionTF.getText());
            osi.updateOrder(selectedOrder);

        } else {
            selectedOrder.setDescription(orderDescriptionTF.getText());
            osi.addOrder(selectedOrder);
        }

        orderDescriptionTF.setText("");
        loadOrderData();
    }

    @FXML
    private void moveUpDownOrder(ScrollEvent event) {

        if (orderContiener.getHeight() > 990) {

            if (event.getDeltaY() < 0) {

                if ((orderContiener.getLayoutY() + orderContiener.getHeight()) > 990) {
                    orderContiener.setLayoutY(event.getDeltaY() + orderContiener.getLayoutY());
                }

            } else {
                if (orderContiener.getLayoutY() < 116) {
                    orderContiener.setLayoutY(event.getDeltaY() + orderContiener.getLayoutY());
                }
            }

        }

    }

    private void loadOrderData() {
        List<Order> orders = osi.findAllOrders();

        orderContiener.getChildren().remove(0, orderContiener.getChildren().size());

        try {
            for (Order o : orders) {

                HBox hbox = SquareDetails.squareCreator(o);

                hbox.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {

                    initPopup(o, e.getSceneX(), e.getSceneY());

                });
                orderContiener.getChildren().add(hbox);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     */
    @FXML
    private void returnHome(ActionEvent event) {
        paneOrderSelection.setVisible(false);
        paneClientSelection.setVisible(false);

    }

    private void loadAssocietedClientVB() {
        selectedOrder = osi.findOrderById(selectedOrder);
        associetedClientVB.getChildren().remove(0, associetedClientVB.getChildren().size());

        List<Client> clients = osi.findClientsByOrder(selectedOrder);

        for (Client c : clients) {

            try {
                HBox hbox = SquareDetails.squareCreator(c);
                hbox.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                    initPopupForRemoveItem(c, e.getSceneX(), e.getSceneY());
                });

                associetedClientVB.getChildren().add(hbox);

            } catch (IllegalArgumentException ex) {
                Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void loadNotassocietedClientVB() {
        selectedOrder = osi.findOrderById(selectedOrder);
        notAssocietedClientVB.getChildren().remove(0, notAssocietedClientVB.getChildren().size());
        List<Client> clients = osi.findClientsNotInOrder(selectedOrder);

        for (Client c : clients) {

            try {
                HBox hbox = SquareDetails.squareCreator(c);
                hbox.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                    initPopupForAddItem(c, e.getSceneX(), e.getSceneY());
                });

                notAssocietedClientVB.getChildren().add(hbox);

            } catch (IllegalArgumentException ex) {
                Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void initPopupForRemoveItem(Object object, double x, double y) {
        JFXPopup popup = new JFXPopup();
        JFXButton remove = new JFXButton("remove");

        remove.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            popup.hide();
            if (object.getClass().equals(Client.class)) {
                osi.deleteClientFromOrder((Client) object, selectedOrder, false);
                loadAssocietedClientVB();
                loadNotassocietedClientVB();

            } else {
                csi.deleteOrderFromClient((Order) object, selectedClient, false);
                loadAssocietedOrderVB();
                loadNotassocietedOrderVB();

            }

        });

        remove.setPrefSize(100, 50);

        popup.setPopupContent(new VBox(remove));

        popup.show(paneOrderSelection, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT, x, y);
    }

    private void initPopupForAddItem(Object object, double x, double y) {
        JFXPopup popup = new JFXPopup();
        JFXButton add = new JFXButton("add");

        add.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            popup.hide();
            if (object.getClass().equals(Client.class)) {
                osi.addClientToOrder((Client) object, selectedOrder, false);
                loadAssocietedClientVB();
                loadNotassocietedClientVB();

            } else {
                csi.addOrderToClient((Order) object, selectedClient, false);
                loadAssocietedOrderVB();
                loadNotassocietedOrderVB();

            }

        });

        add.setPrefSize(100, 50);

        popup.setPopupContent(new VBox(add));

        popup.show(paneOrderSelection, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT, x, y);
    }

    /**
     * ************************
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     */
    private void loadAssocietedOrderVB() {
        selectedClient = csi.findClientById(selectedClient);
        associetedOrderVB.getChildren().remove(0, associetedOrderVB.getChildren().size());
        List<Order> orders = csi.findOrdersByClient(selectedClient);

        for (Order o : orders) {

            try {
                HBox hbox = SquareDetails.squareCreator(o);
                hbox.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                    initPopupForRemoveItem(o, e.getSceneX(), e.getSceneY());
                });

                associetedOrderVB.getChildren().add(hbox);

            } catch (IllegalArgumentException ex) {
                Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void loadNotassocietedOrderVB() {
        selectedClient = csi.findClientById(selectedClient);
        notAssocietedOrderVB1.getChildren().remove(0, notAssocietedOrderVB1.getChildren().size());
        List<Order> orders = csi.findOrdersNotInClient(selectedClient);

        for (Order o : orders) {

            try {
                HBox hbox = SquareDetails.squareCreator(o);
                hbox.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                    initPopupForAddItem(o, e.getSceneX(), e.getSceneY());
                });

                notAssocietedOrderVB1.getChildren().add(hbox);

            } catch (IllegalArgumentException ex) {
                Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}
