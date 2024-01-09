package com.codefornature;

import com.codefornature.dao.CartDAO;
import com.codefornature.dao.OrderDAO;
import com.codefornature.dao.UserDAO;
import com.codefornature.model.CartItemsModel;
import com.codefornature.model.CartModel;
import com.codefornature.model.OrderModel;
import com.codefornature.model.UserModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class OrderController {
    @FXML
    public ImageView backIcon;
    @FXML
    private Label addressErrorMessage;
    @FXML
    private Label cityErrorMessage;
    @FXML
    private Label stateErrorMessage;
    @FXML
    private Label postCodeErrorMessage;

    @FXML
    public TextField addressTxt;
    @FXML
    public TextField cityTxt;
    @FXML
    public TextField stateTxt;
    @FXML
    public TextField postCodeTxt;
    @FXML
    public Button orderNowBtn;
    @FXML
    public Button backBtn;
    private List<CartItemsModel> cartItems;
    private OrderDAO orderDAO;
    private UserModel user;
    private BorderPane mainContainer;
    private int grandTotalCost;

    public void initialize(){
        ColorAdjust white = new ColorAdjust();
        white.setBrightness(1.0);
        backIcon.setEffect(white);
    }

    public void setCartItems(List<CartItemsModel> cartItems) {
        this.cartItems = cartItems;
        System.out.println(this.cartItems);
    }

    public void onOrderNowClick(ActionEvent actionEvent) throws SQLException, IOException {
        if(isTextFieldValid()) {
            //When all text fields are valid
            String address = combineAddress();

            //Get current date
            String currentOrderDate = convertDateFormat(new java.util.Date());
            System.out.println("Order Date: " + currentOrderDate);

            orderDAO = new OrderDAO(address, currentOrderDate, user);
            orderDAO.createOrder();
            OrderModel order = orderDAO.getOrder(user.getUser_id(), currentOrderDate);
            orderDAO.addItemsToOrder(cartItems, order.getOrder_id());
            orderDAO.writeOrdersToFile(cartItems);
            UserDAO userDAO = new UserDAO();
            int pointsLeftOver = user.getPoints() - getGrandTotalCost();
            System.out.println();
            user.setPoints(pointsLeftOver);
            userDAO.updatePoints(user.getUser_id(), -grandTotalCost);
            removeCart();
            AlertController.showAlert("Order", "Order is successfully made", 1);
            loadPage();
        } else {
            System.out.println("Text field invalid");
        }
    }

    public boolean isTextFieldValid() {
        boolean isValid = true;
        if (addressTxt.getText().isEmpty()) {
            addressErrorMessage.setText("Address is empty!");
            isValid = false;
        }

        if (cityTxt.getText().isEmpty()) {
            cityErrorMessage.setText("City is empty!");
            isValid = false;
        }
        if (stateTxt.getText().isEmpty()) {
            stateErrorMessage.setText("State is empty!");
            isValid = false;
        }
        if (postCodeTxt.getText().isEmpty()) {
            postCodeErrorMessage.setText("Post Code is empty");
            isValid = false;
        }
        else{
            if(!postCodeTxt.getText().matches("\\d{5}")){
                postCodeErrorMessage.setText("Post Code should be in 5 digits!");
                isValid = false;
            }
        }
        return isValid;
    }

    private String combineAddress() {
        String address = addressTxt.getText().trim();
        String city = cityTxt.getText().trim();
        String state = stateTxt.getText().trim();
        String postCode = postCodeTxt.getText().trim();

        // Combine the address components into a single string
        return String.format("%s, %s, %s, %s", address, city, state, postCode);
    }

    public void loadPage() throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("home-view.fxml"));
        Parent root = loader.load();
        HomeController homeController = loader.getController();
        homeController.setUser(user);
        mainContainer.setCenter(root);
    }

    public void removeCart() throws SQLException {
        CartDAO cartDAO = new CartDAO();
        cartDAO.removeAllCartItems(CartModel.getCart_id());
        cartDAO.removeCart(CartModel.getCart_id());
        CartModel cart = new CartModel(0, user.getUser_id());
    }

    public String convertDateFormat(java.util.Date currentDate){
        DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("EEE MMM dd kk:mm:ss z yyyy");
        // Parsing the date
        LocalDateTime date = LocalDateTime.parse(currentDate.toString(), inputFormat);
        DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return date.format(outputFormat);
    }

    public void setUser(UserModel user){
        this.user = user;
    }

    public void setMainContainer(BorderPane mainContainer) {
        this.mainContainer = mainContainer;
    }

    public void setGrandTotal(int grandTotalCost) {
        this.grandTotalCost = grandTotalCost;
    }

    public int getGrandTotalCost(){
        return grandTotalCost;
    }
}
