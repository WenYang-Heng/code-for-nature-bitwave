package com.codefornature;

import com.codefornature.dao.CartDAO;
import com.codefornature.dao.MerchandiseDAO;
import com.codefornature.model.CartModel;
import com.codefornature.model.MerchandiseModel;
import com.codefornature.model.UserModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PointShopController {
    private UserModel user;
    private CartModel cart;
    private CartDAO cartDAO = new CartDAO();
    private List<MerchandiseModel> merchList;
    private Map<Integer, Label> counterLabels = new HashMap<>();
    @FXML
    FlowPane merchContent;
    @FXML private BorderPane mainContainer;

    @FXML
    public void initialize() throws IOException, SQLException {
        String rootPath = System.getProperty("user.dir");
        MerchandiseDAO merchDAO = new MerchandiseDAO();
        merchList = merchDAO.getAllMerchandise();
//        BufferedReader in = new BufferedReader(new FileReader(rootPath + "/src/main/resources/assets/text/merchandise.txt"));
//        String line;
        for(int i = 0; i < merchList.size(); i++){
            VBox vbox = new VBox();
            vbox.setPrefSize(252, 268);
            String imageUrl = "@../../assets/images/";
            Label itemName = new Label(merchList.get(i).getMerchandise_name());
//            String price = in.readLine();
            Label itemPrice = new Label("$" + merchList.get(i).getCost());
            itemName.setStyle("-fx-text-fill: #E4EAF1");
            itemPrice.setStyle("-fx-text-fill: #E4EAF1");
            itemPrice.setFont(new Font(20));
//            String imageName = in.readLine();
            ImageView merchImage = new ImageView(new Image(imageUrl + merchList.get(i).getImage_name()));
            merchImage.setFitWidth(252);
            merchImage.setFitHeight(200);
            Region region = new Region();
            HBox.setHgrow(region, Priority.ALWAYS);
            HBox labelBox = new HBox(itemName, region, itemPrice);
            labelBox.setAlignment(Pos.CENTER);
            labelBox.setPadding(new Insets(0, 5, 0 ,5));

            HBox buttonBox = new HBox(
                    createStyledButton("Buy Now", merchList.get(i)),
                    createStyledButton("Add to Cart", merchList.get(i)),
                    createCounterBox(merchList.get(i).getMerchandise_id())
            );
            buttonBox.setPadding(new Insets(0, 5, 0 ,5));
            buttonBox.setSpacing(10);
            vbox.getChildren().addAll(merchImage, labelBox, buttonBox);
            vbox.setSpacing(10);
            merchContent.getChildren().add(vbox);
        }
//        in.close();
    }

    private Button createStyledButton(String text, MerchandiseModel merch) {
        Button button = new Button(text);
        button.setFont(new Font(11));
        button.setStyle(
                "-fx-background-radius: 20;" +
                (text.equals("Buy Now")? "-fx-background-color: #359424;-fx-text-fill: #ffffff;" : "-fx-background-color: #A9E79C; -fx-text-fill: #1E4B15")
        );
        button.setPrefSize(200, 27);

        if(text.equals("Buy Now")){
            button.setOnAction(event -> {
                try {
                    buyNow(merch);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        else{
            button.setOnAction((event -> {
                try {
                    addToCart(merch);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }));
        }
        return button;
    }

    public void setCart(CartModel cart){
        this.cart = cart;
    }

    public void getCart() throws SQLException {
        if(!cartDAO.cartExists(user.getUser_id())){
            if(cartDAO.addNewCart(user.getUser_id())){
                System.out.println("cart created");
            }
        }
        cart = cartDAO.getCart(user.getUser_id());
    }

    public int getQuantity(int merchandise_id){
        Label label = counterLabels.get(merchandise_id);
        if(label != null){
            return Integer.parseInt(label.getText());
        }
        return 0;
    }

    private void addToCart(MerchandiseModel merch) throws SQLException {
        int quantity = getQuantity(merch.getMerchandise_id());

        if(quantity == 0){
            System.out.println("Please indicate the quantity of the items");
            AlertController.showAlert("QUANTITY NOT SPECIFIED","Please indicate the quantity of the items", 0);
            return;
        }

        System.out.println(user.getUser_id());
        getCart();

        System.out.printf("Add %d %s%n", quantity, merch.getMerchandise_name());
        if (cartDAO.itemExist(merch.getMerchandise_id(), CartModel.getCart_id())) {
            // Update existing item quantity
            System.out.println(merch.getMerchandise_name() + " exists, update quantity");
            int quantityInDb = cartDAO.getItemQuantity(merch.getMerchandise_id(), CartModel.getCart_id());
            if(quantityInDb == 5){
                AlertController.showAlert("QUANTITY EXCEEDED","Only 5 items is allowed", 0);
            }
            else{
                int allowedAdditionalQuantity = 5 - quantityInDb;

                // If the requested addition does not exceed the maximum limit
                if (quantity <= allowedAdditionalQuantity) {
                    cartDAO.updateItemQuantity(merch.getMerchandise_id(), CartModel.getCart_id(), quantityInDb + quantity);
                    AlertController.showAlert("CART", "Item is added to the cart", 1);
                }
                else{
                    AlertController.showAlert("QUANTITY EXCEEDED","Cannot add more than 5 items in the cart.", 0);
                }
            }
        } else {
            // Add new cart item
            System.out.println("Item does not exists, add into database");
            boolean success = cartDAO.addCartItems(CartModel.getCart_id(), merch.getMerchandise_id(), quantity);
            String message = "Item is not added to cart";
            int alertType = 0;
            if (success) {
                message = "Item is added to cart";
                alertType = 1;
            }
            AlertController.showAlert("CART", message, alertType);
        }
    }

    private void buyNow(MerchandiseModel merch) throws SQLException, IOException {
        System.out.println("Buy now: " + merch.getMerchandise_name());

        getCart();

        if(!cartDAO.itemExist(merch.getMerchandise_id(), CartModel.getCart_id())){
            cartDAO.addCartItems(cart.getCart_id(), merch.getMerchandise_id(), 1);
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("shopping-cart-view.fxml"));
        Parent root = loader.load();
        ShoppingCartController shoppingCartController = loader.getController();
        shoppingCartController.setUser(user);
        shoppingCartController.setMainContainer(mainContainer);
        mainContainer.setCenter(root);
    }

    private HBox createCounterBox(int merchandise_id) {
        String iconUrl = "@../../assets/icons/";
        Button minusButton = createImageButton(iconUrl + "minus.png");
        Label counterLabel = new Label("0");
        counterLabel.setStyle("-fx-text-fill: #ffffff");
        Button plusButton = createImageButton(iconUrl + "plus.png");

        counterLabels.put(merchandise_id, counterLabel);

        minusButton.setOnAction(event -> {
            int count = Integer.parseInt(counterLabel.getText());
            if(count > 0){
                counterLabel.setText(Integer.toString(--count));
            }
        });

        plusButton.setOnAction(event -> {
            int count = Integer.parseInt(counterLabel.getText());
            if(count < 5)
                counterLabel.setText(Integer.toString(++count));
        });

        HBox counterBox = new HBox(minusButton, counterLabel, plusButton);
        counterBox.setAlignment(Pos.CENTER);
        return counterBox;
    }

    private Button createImageButton(String imagePath) {
        ImageView imageView = new ImageView(new Image(imagePath));
        imageView.setFitWidth(12);
        imageView.setFitHeight(12);
        ColorAdjust white = new ColorAdjust();
        white.setBrightness(1.0);
        imageView.setEffect(white);
        Button button = new Button();
        button.setStyle("-fx-background-color: transparent");
        button.setGraphic(imageView);

        return button;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public void setBorderPane(BorderPane mainContainer) {
        this.mainContainer = mainContainer;
    }
}
