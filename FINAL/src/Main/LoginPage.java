package Main;
import java.time.LocalDate;  // For java.time.LocalDate
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import javafx.scene.layout.*;

public class LoginPage extends Application {
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage; // Set the primary stage
        primaryStage.setTitle("E-Commerce System"); // Set window title
        showLoginScreen(); // Show the login screen

        // Set the confirmation dialog when the user tries to close the window
        primaryStage.setOnCloseRequest(event -> {
            event.consume();

            showAlert(Alert.AlertType.CONFIRMATION, "Confirm Exit", "Are you sure you want to exit?", () -> {
                // Exit the application if "Yes" is clicked
                System.exit(0);
            });
        });
    }

    private void showAlert(Alert.AlertType type, String title, String message, Runnable onConfirm) {
        // Create an alert with the specified type
        Alert alert = new Alert(type);

        // Set the title of the alert
        alert.setTitle(title);

        // Set the header text to null (you can customize it if needed)
        alert.setHeaderText(null);

        // Set the content text of the alert (the main message)
        alert.setContentText(message);

        // Set the buttons (Yes/No)
        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");

        // Add the buttons to the alert
        alert.getButtonTypes().setAll(yesButton, noButton);

        // Show the alert and wait for the user to respond
        alert.showAndWait().ifPresent(response -> {
            if (response == yesButton) {
                // Execute the onConfirm action if the user clicks "Yes"
                onConfirm.run();
            }
        });
    }

    // Show login screen with text fields for username and password
    private void showLoginScreen() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setVgap(10);
        grid.setHgap(10);

        // Center the GridPane content
        grid.setAlignment(Pos.CENTER);

        // Create the username and password fields
        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();

        // Create the Login button
        Button loginButton = new Button("Login");

        // Create the Sign Up button
        Button signUpButton = new Button("Sign Up");

        // Add action for the Sign Up button
        signUpButton.setOnAction(e -> showSignUpScreen());  // Navigate to the Sign Up screen

        // Add components to the GridPane
        grid.add(new Label("Username:"), 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(loginButton, 1, 2);  // Login button in column 1, row 2
        grid.add(signUpButton, 1, 3);  // Sign Up button below the Login button

        // Center the content in each cell (login button, fields, etc.)
        GridPane.setHalignment(loginButton, HPos.CENTER);  // Center the Login button
        GridPane.setHalignment(signUpButton, HPos.CENTER);  // Center the Sign Up button
        GridPane.setHalignment(usernameField, HPos.CENTER); // Center the username field
        GridPane.setHalignment(passwordField, HPos.CENTER); // Center the password field

        loginButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();

            // Fetch the user from the database
            User loggedInUser = Database.findUserByUsername(username);

            // Debug: Check if user is found
            if (loggedInUser == null) {
                System.out.println("User not found in the database.");
            } else {
                System.out.println("User found: " + loggedInUser.getUsername());
            }

            // Validate username and password
            if (loggedInUser != null && loggedInUser.getPassword().equals(password)) {
                if (loggedInUser instanceof Customer) {
                    Customer customer = (Customer) loggedInUser;
                    showCustomerMenu((Stage) ((Button) e.getSource()).getScene().getWindow(), customer); // Pass the current stage
                } else if (loggedInUser instanceof Admin) {
                    Admin admin = (Admin) loggedInUser;

                    // Debug: Check the role of the admin
                    System.out.println("Admin Role: " + admin.getRole());

                    // Check admin role and show the corresponding menu
                    switch (admin.getRole().toLowerCase()) {
                        case "user manager":
                            showUserManagerMenu((Stage) ((Button) e.getSource()).getScene().getWindow(), admin);
                            break;
                        case "product manager":
                            showProductManagerMenu((Stage) ((Button) e.getSource()).getScene().getWindow(), admin);
                            break;
                        case "category manager":
                            showCategoryManagerMenu((Stage) ((Button) e.getSource()).getScene().getWindow(), admin);
                            break;
                        default:
                            showAlert("Access Denied", "You do not have access to the database.");
                            break;
                    }
                }
            } else {
                showAlert("Login Failed", "Invalid username or password.");
            }
        });

        // Set the scene and show it
        Scene scene = new Scene(grid, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showSignUpScreen() {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        // Create UI components
        Label lblUsername = new Label("Username:");
        TextField txtUsername = new TextField();

        Label lblPassword = new Label("Password:");
        PasswordField txtPassword = new PasswordField();

        Label lblDOB = new Label("Date of Birth:");
        DatePicker datePickerDOB = new DatePicker(); // DatePicker for selecting DOB

        Label lblAddress = new Label("Address:");
        TextField txtAddress = new TextField();

        Label lblGender = new Label("Gender:");
        ComboBox<String> cmbGender = new ComboBox<>(); // ComboBox for gender selection
        cmbGender.getItems().addAll("Male", "Female");

        Label lblPremium = new Label("Premium Member:");
        CheckBox chkPremium = new CheckBox(); // CheckBox for premium membership

        Label lblBalance = new Label("Initial Balance:");
        TextField txtBalance = new TextField();

        Button btnSubmit = new Button("Submit");
        Button btnCancel = new Button("Cancel");

        // Add components to the grid
        gridPane.add(lblUsername, 0, 0);
        gridPane.add(txtUsername, 1, 0);

        gridPane.add(lblPassword, 0, 1);
        gridPane.add(txtPassword, 1, 1);

        gridPane.add(lblDOB, 0, 2);
        gridPane.add(datePickerDOB, 1, 2);

        gridPane.add(lblAddress, 0, 3);
        gridPane.add(txtAddress, 1, 3);

        gridPane.add(lblGender, 0, 4);
        gridPane.add(cmbGender, 1, 4);

        gridPane.add(lblPremium, 0, 5);
        gridPane.add(chkPremium, 1, 5);

        gridPane.add(lblBalance, 0, 6);
        gridPane.add(txtBalance, 1, 6);

        gridPane.add(btnSubmit, 0, 7);
        gridPane.add(btnCancel, 1, 7);

        // Set up the scene and stage
        Scene scene = new Scene(gridPane, 400, 400);
        primaryStage.setTitle("Sign Up Page");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Add event handlers
        btnSubmit.setOnAction(e -> {
            String username = txtUsername.getText();
            String password = txtPassword.getText();
            LocalDate dob = datePickerDOB.getValue();
            String address = txtAddress.getText();
            String gender = cmbGender.getValue();
            boolean premium = chkPremium.isSelected();
            String balanceInput = txtBalance.getText();

            // Validate inputs
            if (username.isEmpty() || password.isEmpty() || dob == null || address.isEmpty() ||
                    gender == null || balanceInput.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Validation Error", "All fields are required!");
                return;
            }

            if (Database.findUserByUsername(username) != null) {
                showAlert(Alert.AlertType.ERROR, "Sign Up Failed", "Username already exists. Choose another one.");
                return;
            }

            if (password.length() < 6 || !password.matches(".*[A-Za-z].*") || !password.matches(".*[0-9].*")) {
                showAlert(Alert.AlertType.ERROR, "Sign Up Failed", "Password must be at least 6 characters long, contain at least one letter and one number.");
                return;
            }

            if (dob.isAfter(LocalDate.now())) {
                showAlert(Alert.AlertType.ERROR, "Sign Up Failed", "Date of birth cannot be in the future.");
                return;
            }

            double balance;
            try {
                balance = Double.parseDouble(balanceInput);
                if (balance < 0 || balance > 5000) {
                    showAlert(Alert.AlertType.ERROR, "Sign Up Failed", "Balance must be between 0 and 5000.");
                    return;
                }
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Sign Up Failed", "Balance must be a valid number.");
                return;
            }

            // Create and save the customer
            Customer.Gender customerGender = Customer.Gender.valueOf(gender.toUpperCase());
            Customer newCustomer = new Customer(username, password, Date.from(dob.atStartOfDay(ZoneId.systemDefault()).toInstant()), address, customerGender, premium);
            newCustomer.setBalance(balance);
            Database.addUser(newCustomer);

            // Navigate directly to the customer menu after successful signup
            showCustomerMenu(primaryStage, newCustomer);
        });

        // Cancel button navigates back to the login screen
        btnCancel.setOnAction(e -> {
            showLoginScreen(); // Navigate back to the login screen
        });
    }



    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showCustomerMenu(Stage stage, Customer customer) {
        // Main layout
        BorderPane mainLayout = new BorderPane();

        // Navigation bar
        VBox navigationBar = new VBox(10);
        navigationBar.setPadding(new Insets(10));
        navigationBar.setStyle("-fx-background-color: #f4f4f4;");

        Button viewCartBtn = new Button("View Cart");
        Button viewWishlistBtn = new Button("View Wishlist");
        Button viewProfileBtn = new Button("View Profile");
        Button logoutBtn = new Button("Logout");

        navigationBar.getChildren().addAll(viewCartBtn, viewWishlistBtn, viewProfileBtn, logoutBtn);
        mainLayout.setLeft(navigationBar);

        // Product display area
        ScrollPane productScrollPane = new ScrollPane();
        GridPane productGrid = new GridPane();
        productGrid.setPadding(new Insets(10));
        productGrid.setHgap(10);
        productGrid.setVgap(10);

        // Category filter dropdown
        ComboBox<String> categoryDropdown = new ComboBox<>();
        categoryDropdown.setPromptText("Select Category");

        // Search bar
        TextField searchBar = new TextField();
        searchBar.setPromptText("Search by product name...");

        // Fetch categories from the database
        ArrayList<Category> categories = Database.getCategoryList();
        ArrayList<Product> products = Database.getProductList();
        categoryDropdown.getItems().add("All"); // Add "All" option to show all products
        for (Category category : categories) {
            categoryDropdown.getItems().add(category.getName());
        }

        // Event handler for category selection
        categoryDropdown.setOnAction(e -> {
            String selectedCategory = categoryDropdown.getValue();
            productGrid.getChildren().clear();

            if ("All".equals(selectedCategory)) {
                // Display all products if "All" is selected
                for (Product product : products) {
                        addProductToGrid(productGrid, product, customer);
                }
            } else if (selectedCategory != null) {
                // Display products of the selected category
                for (Category category : categories) {
                    if (category.getName().equals(selectedCategory)) {
                        for (Product product : category.getAllProducts()) {
                            addProductToGrid(productGrid, product, customer);
                        }
                        break;
                    }
                }
            }
        });

        // Event handler for search bar
        searchBar.setOnKeyReleased(e -> {
            String query = searchBar.getText().toLowerCase();
            productGrid.getChildren().clear();

            for (Product product : products) {
                if (product.getName().toLowerCase().contains(query)) {
                    addProductToGrid(productGrid, product, customer);
                }
            }
        });

        // Initial display: All products
        for (Product product : products) {
            addProductToGrid(productGrid, product, customer);
        }

        productScrollPane.setContent(productGrid);

        // Top bar with category filter and search bar
        VBox topBar = new VBox(10, categoryDropdown, searchBar);
        topBar.setPadding(new Insets(10));

        mainLayout.setTop(topBar);
        mainLayout.setCenter(productScrollPane);

        // Set the scene on the provided stage
        Scene scene = new Scene(mainLayout, 800, 600);
        stage.setTitle("Customer Menu");
        stage.setScene(scene);
        stage.show();

        // Handle View Cart Button click
        viewCartBtn.setOnAction(e -> {
            viewCart(stage, customer);  // Open the cart window
        });

        // Handle View Wishlist Button click
        viewWishlistBtn.setOnAction(e -> {
            viewWishlist(stage, customer);
        });  // Open the wishlist window

        // Handle View Profile Button click
        viewProfileBtn.setOnAction(e -> {
            showProfileWindowForCustomer(stage, customer);  // Show the profile window
        });

        // Handle Logout Button click
        logoutBtn.setOnAction(e -> {
            // After logout, return to the login page
            showLoginScreen();  // You will need to implement this method
        });
    }

    private void addProductToGrid(GridPane productGrid, Product product, Customer customer) {
        VBox productBox = new VBox(5);
        productBox.setStyle("-fx-border-color: #ccc; -fx-padding: 10;");

        Label productName = new Label(product.getName());
        Label productPrice = new Label("Price: " + product.getPrice() + " LE");
        Label productStock = new Label("Stock: " + product.getStockQuantity());

        Button addToCartBtn = new Button("Add to Cart");
        Button addToWishlistBtn = new Button("Add to Wishlist");

        // Add to Cart button functionality
        addToCartBtn.setOnAction(e -> {
            if (Database.isProductInStock(product.getID())) {
                customer.getCart().addItem(product);
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Product is out of stock.");
            }
        });

        // Add to Wishlist button functionality
        addToWishlistBtn.setOnAction(e -> {
            if (customer.getWishlist().contains(product)) {
                // Display a warning if the product is already in the wishlist
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Duplicate Entry");
                alert.setHeaderText(null);
                alert.setContentText(product.getName() + " is already in your wishlist.");
                alert.showAndWait();
            } else {
                customer.addToWishlist(product);
            }
        });

        productBox.getChildren().addAll(productName, productPrice, productStock, addToCartBtn, addToWishlistBtn);
        int row = productGrid.getChildren().size() / 3;
        int col = productGrid.getChildren().size() % 3;
        productGrid.add(productBox, col, row); // 3 products per row
    }

    private void showProfileWindowForCustomer(Stage stage, User user) {
        // Main layout
        BorderPane mainLayout = new BorderPane();

        // Navigation bar
        VBox navigationBar = new VBox(10);
        navigationBar.setPadding(new Insets(10));
        navigationBar.setStyle("-fx-background-color: #f4f4f4;");

        Button viewCartBtn = new Button("View Cart");
        Button viewWishlistBtn = new Button("View Wishlist");
        Button backToMenuBtn = new Button("Back to Menu");
        Button logoutBtn = new Button("Logout");

        navigationBar.getChildren().addAll(viewCartBtn, viewWishlistBtn, backToMenuBtn, logoutBtn);
        mainLayout.setLeft(navigationBar);

        // Profile layout
        VBox profileLayout = new VBox(10);
        profileLayout.setPadding(new Insets(20));

        // Create labels to display user data
        Label usernameLabel = new Label("Username: " + user.getUsername());
        Label dobLabel = new Label("Date of Birth: " + user.getDateOfBirth().toString());

        if (user instanceof Customer) {
            Customer customer = (Customer) user;

            // Display gender and premium status
            Label genderLabel = new Label("Gender: " + customer.getGender().toString());
            Label premiumLabel = new Label("Premium: " + (customer.isPremium() ? "Yes" : "No"));

            // Display additional information for Customer
            Label balanceLabel = new Label("Balance: $" + customer.getBalance());
            Label addressLabel = new Label("Address: " + customer.getAddress());

            profileLayout.getChildren().addAll(usernameLabel, dobLabel, genderLabel, premiumLabel, balanceLabel, addressLabel);
        } else if (user instanceof Admin) {
            Admin admin = (Admin) user;
            Label roleLabel = new Label("Role: " + admin.getRole());
            Label workingHoursLabel = new Label("Working Hours: " + admin.getWorkingHours());
            profileLayout.getChildren().addAll(usernameLabel, dobLabel, roleLabel, workingHoursLabel);
        }

        // Create the Edit Profile button
        Button editProfileBtn = new Button("Edit Profile");

        // When Edit Profile is clicked, allow editing
        editProfileBtn.setOnAction(e -> {
            showEditProfileWindowForCustomer(stage, user);  // Show the Edit Profile screen
        });

        profileLayout.getChildren().add(editProfileBtn);
        mainLayout.setCenter(profileLayout);

        // Button actions
        viewCartBtn.setOnAction(e -> viewCart(stage, (Customer) user));
        viewWishlistBtn.setOnAction(e -> viewWishlist(stage, (Customer) user));
        backToMenuBtn.setOnAction(e -> showCustomerMenu(stage, (Customer) user));
        logoutBtn.setOnAction(e -> showLoginScreen());

        // Create a scene and set it on the stage
        Scene profileScene = new Scene(mainLayout, 800, 600);
        stage.setScene(profileScene);
        stage.show();
    }


    private void showEditProfileWindowForCustomer(Stage stage, User user) {
        VBox editLayout = new VBox(10);
        editLayout.setPadding(new Insets(20));

        // Create text fields for each editable profile property
        TextField usernameField = new TextField(user.getUsername());
        PasswordField passwordField = new PasswordField();  // You can pre-fill if needed

        // Convert java.util.Date to LocalDate for DatePicker
        java.util.Date utilDate = user.getDateOfBirth();
        LocalDate localDate = utilDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        DatePicker dobPicker = new DatePicker(localDate);

        // For Customer, include gender, premium, balance, and address
        ComboBox<Customer.Gender> genderComboBox = new ComboBox<>();
        genderComboBox.getItems().setAll(Customer.Gender.values());
        genderComboBox.setValue(user instanceof Customer ? ((Customer) user).getGender() : Customer.Gender.MALE); // Default to MALE if Admin

        CheckBox premiumCheckBox = new CheckBox("Premium Member");
        premiumCheckBox.setSelected(((Customer) user).isPremium());

        TextField balanceField;
        TextField addressField;

        Customer customer = (Customer) user;
        balanceField = new TextField(String.valueOf(customer.getBalance()));
        addressField = new TextField(customer.getAddress());


        // Add a button to save the changes
        Button saveChangesBtn = new Button("Save Changes");

        // When Save Changes is clicked, validate and save the new profile data
        saveChangesBtn.setOnAction(e -> {
            String newUsername = usernameField.getText();
            String newPassword = passwordField.getText();
            LocalDate newDob = dobPicker.getValue();
            Customer.Gender newGender = genderComboBox.getValue();
            boolean newPremium = premiumCheckBox.isSelected();

            // Validate the fields before updating
            if (newUsername.trim().isEmpty()) {
                showAlert("Invalid Input", "Username cannot be empty.");
            } else if (newDob == null) {
                showAlert("Invalid Input", "Please select a valid date of birth.");
            } else if (newDob.isAfter(LocalDate.now())) {
                showAlert("Invalid Input", "Date of birth cannot be in the future.");
            } else if (!newUsername.equals(user.getUsername()) && Database.findUserByUsername(newUsername) != null) {
                showAlert("Invalid Input", "Username already exists. Please choose a different username.");
            } else {

                // Only update the password if a new one is provided
                if (!newPassword.trim().isEmpty()) {
                    if (newPassword.length() < 6) {
                        showAlert("Invalid Input", "Password must be at least 6 characters long.");
                        return;
                    }
                    user.setPassword(newPassword);
                }

                // Update the user object with new values
                user.setUsername(newUsername);
                user.setPassword(newPassword);

                // Convert LocalDate back to java.util.Date
                java.util.Date updatedDate = Date.from(newDob.atStartOfDay(ZoneId.systemDefault()).toInstant());
                user.setDateOfBirth(updatedDate);

                // For Customer, also update gender, premium, balance, and address
                customer.setGender(newGender);
                customer.setPremium(newPremium);

                if (balanceField != null && !balanceField.getText().trim().isEmpty()) {
                    try {
                        double newBalance = Double.parseDouble(balanceField.getText());
                        if (newBalance < 0 || newBalance > 5000) {
                            showAlert("Invalid Input", "Balance must be between 0 and 5000.");
                            return;
                        }
                        customer.setBalance(newBalance);
                    } catch (NumberFormatException ex) {
                        showAlert("Invalid Input", "Balance must be a valid number.");
                        return;
                    }
                }
                if (addressField != null && !addressField.getText().trim().isEmpty()) {
                    customer.setAddress(addressField.getText());
                }


                // After updating, show success message
                showAlert("Profile Updated", "Your profile has been successfully updated.");
                showProfileWindowForCustomer(stage, user);  // Return to the profile view
            }
        });

        // Add a button to go back to the profile view
        Button backToProfileBtn = new Button("Back to Profile");
        backToProfileBtn.setOnAction(e -> showProfileWindowForCustomer(stage, user));

        // Add all elements to the edit layout
        editLayout.getChildren().addAll(new Label("Username:"), usernameField,
                new Label("Password:"), passwordField,
                new Label("Date of Birth:"), dobPicker,
                new Label("Gender:"), genderComboBox,
                premiumCheckBox);

        // For Customer, add balance and address fields
        editLayout.getChildren().addAll(new Label("Balance:"), balanceField,
                new Label("Address:"), addressField);

        editLayout.getChildren().addAll(saveChangesBtn, backToProfileBtn);

        // Create a scene and set it on the stage
        Scene editScene = new Scene(editLayout, 600, 600);
        stage.setScene(editScene);
        stage.show();
    }



    private void viewCart(Stage stage, Customer customer) {
        // Check if the customer is null
        if (customer == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Customer not found.");
            return;
        }

        // Create the cart view layout
        BorderPane cartLayout = new BorderPane();

        // Navigation bar (reuse the one from the customer menu or create a new one)
        VBox navigationBar = new VBox(10);
        navigationBar.setPadding(new Insets(10));
        navigationBar.setStyle("-fx-background-color: #f4f4f4;");

        Button backToMenuBtn = new Button("Back to Menu");
        navigationBar.getChildren().addAll(backToMenuBtn);
        cartLayout.setLeft(navigationBar);

        // Cart display area
        VBox cartItemsBox = new VBox(10);
        cartItemsBox.setPadding(new Insets(10));

        // Check if the cart has items
        if (customer.getCart().getItems().isEmpty()) {
            Label emptyCartLabel = new Label("Your cart is currently empty.");
            cartItemsBox.getChildren().add(emptyCartLabel);
        } else {
            // Add the cart items
            for (Product product : customer.getCart().getItems()) {
                HBox productBox = new HBox(10);
                Label productLabel = new Label(product.getName() + " - " + product.getPrice());
                Button removeBtn = new Button("Remove from Cart");

                removeBtn.setOnAction(e -> {
                    customer.getCart().removeItem(product);
                    cartItemsBox.getChildren().remove(productBox); // Update the display immediately
                    System.out.println(product.getName() + " removed from cart.");
                });

                productBox.getChildren().addAll(productLabel, removeBtn);
                cartItemsBox.getChildren().add(productBox);
            }
        }

        cartLayout.setCenter(cartItemsBox);

        // Back to Menu button functionality
        backToMenuBtn.setOnAction(e -> showCustomerMenu(stage, customer));

        // Place Order button at the bottom of the layout
        Button placeOrderBtn = new Button("Place Order");
        BorderPane.setAlignment(placeOrderBtn, Pos.CENTER); // Centers the button horizontally
        cartLayout.setBottom(placeOrderBtn);

        // Disable "Place Order" button if cart is empty
        if (customer.getCart().getItems().isEmpty()) {
            placeOrderBtn.setDisable(true);
        }

        placeOrderBtn.setOnAction(e -> placeOrder(stage, customer));

        // Set the scene for the cart view
        Scene cartScene = new Scene(cartLayout, 800, 600);
        stage.setScene(cartScene);
        stage.setTitle("View Cart");
        stage.show();
    }

    private void placeOrder(Stage stage, Customer customer) {
        // Check if the cart is empty before placing the order
        if (customer.getCart().getItems().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Your cart is empty. Add products to the cart before placing an order.");
            return;
        }

        // Validate stock for each product in the cart
        boolean stockValid = true;
        StringBuilder stockIssues = new StringBuilder("The following products have insufficient stock:\n");

        for (Product product : customer.getCart().getItems()) {
            if (Collections.frequency(customer.getCart().getItems(), product) > product.getAvailableStock()) {
                stockValid = false;
                stockIssues.append(product.getName())
                        .append(" - Available: ")
                        .append(product.getAvailableStock())
                        .append(", In Cart: ")
                        .append(Collections.frequency(customer.getCart().getItems(), product))
                        .append("\n");
            }
        }

        if (!stockValid) {
            showAlert(Alert.AlertType.ERROR, "Stock Issues", stockIssues.toString());
            return;
        }

        // Create the order layout
        BorderPane orderLayout = new BorderPane();

        // Navigation bar
        VBox navigationBar = new VBox(10);
        navigationBar.setPadding(new Insets(10));
        navigationBar.setStyle("-fx-background-color: #f4f4f4;");

        Button backToMenuBtn = new Button("Back to Menu");
        navigationBar.getChildren().addAll(backToMenuBtn);
        orderLayout.setLeft(navigationBar);

        // Order details display area
        VBox orderDetailsBox = new VBox(10);
        orderDetailsBox.setPadding(new Insets(10));

        // Display the items in the order
        for (Product product : customer.getCart().getItems()) {
            Label productLabel = new Label(product.getName() + " - " + product.getPrice());
            orderDetailsBox.getChildren().add(productLabel);
        }

        // Display the total price
        customer.applyCartDiscount();
        double totalPrice = customer.getCart().getFinalCost();
        Label totalPriceLabel = new Label("Total Price: " + totalPrice + " $");
        orderDetailsBox.getChildren().add(totalPriceLabel);

        // Payment Method Selection
        Label paymentMethodLabel = new Label("Select Payment Method:");
        ComboBox<String> paymentMethodsComboBox = new ComboBox<>();
        paymentMethodsComboBox.getItems().addAll("Credit Card", "Application Balance");
        paymentMethodsComboBox.setValue("Credit Card");

        // Add the payment method combo box to the order details
        orderDetailsBox.getChildren().add(paymentMethodLabel);
        orderDetailsBox.getChildren().add(paymentMethodsComboBox);

        // Payment details fields
        VBox paymentDetailsBox = new VBox(10);
        TextField cardNumberField = new TextField();
        cardNumberField.setPromptText("Enter Credit Card Number");

        TextField cardExpiryField = new TextField();
        cardExpiryField.setPromptText("Enter Expiry Date (MM/YY)");

        TextField cardCvvField = new TextField();
        cardCvvField.setPromptText("Enter CVV");

        Button confirmOrderBtn = new Button("Confirm Order");

        // Create separate TextFormatter instances for each field
        TextFormatter<String> cardNumberFormatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            return newText.matches("\\d*") ? change : null; // Allow only numbers
        });

        TextFormatter<String> cardCvvFormatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            return newText.matches("\\d*") ? change : null; // Allow only numbers
        });

        TextFormatter<String> cardExpiryFormatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            return newText.matches("\\d{0,2}/?\\d{0,2}") ? change : null; // Allow MM/YY format
        });

        // Apply TextFormatters to the respective fields
        cardNumberField.setTextFormatter(cardNumberFormatter);
        cardCvvField.setTextFormatter(cardCvvFormatter);
        cardExpiryField.setTextFormatter(cardExpiryFormatter);

        // Add a listener to enforce length for the card number (16 digits)
        cardNumberField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 16) {
                cardNumberField.setText(newValue.substring(0, 16));
            }
        });

        // Add a listener to enforce length for CVV (3 digits)
        cardCvvField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 3) {
                cardCvvField.setText(newValue.substring(0, 3));
            }
        });

        // Validate expiry date format (MM/YY)
        cardExpiryField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,2}/?\\d{0,2}")) { // Allow MM/YY format
                cardExpiryField.setText(oldValue);
            }
        });

        // Show payment details based on the selected payment method
        paymentMethodsComboBox.setOnAction(e -> {
            String selectedPaymentMethod = paymentMethodsComboBox.getValue();
            paymentDetailsBox.getChildren().clear(); // Clear existing fields

            switch (selectedPaymentMethod) {
                case "Credit Card":
                    paymentDetailsBox.getChildren().addAll(cardNumberField, cardExpiryField, cardCvvField);
                    break;
                case "Application Balance":
                    // No additional details needed for application balance
                    break;
            }
        });

        // Set default fields for the initial selected payment method
        paymentMethodsComboBox.fireEvent(new ActionEvent()); // Trigger the action to set the fields initially

        // Add payment details section
        orderDetailsBox.getChildren().add(paymentDetailsBox);

        // Confirm Order Button functionality
        confirmOrderBtn.setOnAction(e -> {
            String selectedPaymentMethod = paymentMethodsComboBox.getValue();
            boolean isValid = true;
            PaymentMethod paymentMethod = null;

            switch (selectedPaymentMethod) {
                case "Credit Card":
                    // Validate credit card details
                    String cardNumber = cardNumberField.getText();
                    String expiryDate = cardExpiryField.getText();
                    String cvv = cardCvvField.getText();

                    if (cardNumber.length() != 16 || expiryDate.length() != 5 || !expiryDate.matches("\\d{2}/\\d{2}") || cvv.length() != 3) {
                        showAlert(Alert.AlertType.ERROR, "Validation Error", "Please enter valid credit card details.\n" +
                                "Card Number: 16 digits\nExpiry Date: MM/YY\nCVV: 3 digits");
                        isValid = false;
                    } else {
                        paymentMethod = new CreditCardPayment(customer, cardNumber, expiryDate, cvv);
                    }
                    break;

                case "Application Balance":
                    // Use application balance as payment method
                    double availableBalance = customer.getBalance();
                    if (availableBalance < totalPrice) {
                        showAlert(Alert.AlertType.ERROR, "Insufficient Balance", "Your application balance is insufficient to complete the purchase.");
                        isValid = false;
                    } else {
                        paymentMethod = new ApplicationPayment(customer);
                    }
                    break;

                default:
                    showAlert(Alert.AlertType.ERROR, "Invalid Payment Method", "Invalid payment method selected.");
                    isValid = false;
            }

            // If payment details are valid, create the order and place it
            if (isValid) {
                Order order = new Order(customer, paymentMethod);
                order.placeOrder(); // This will process the payment and complete the order
                Database.addOrder(order);

                if (order.getStatus() == Order.OrderStatus.COMPLETED) {
                    // After order is placed, clear the cart
                    customer.getCart().clearCart();
                    showAlert(Alert.AlertType.INFORMATION, "Order Placed", "Your order has been placed successfully!");
                    showCustomerMenu(stage, customer); // Go back to the customer menu
                }
            }
        });

        orderDetailsBox.getChildren().add(confirmOrderBtn);
        orderLayout.setCenter(orderDetailsBox);

        // Back to Menu button functionality
        backToMenuBtn.setOnAction(e -> showCustomerMenu(stage, customer));

        // Set the scene for the order view
        Scene orderScene = new Scene(orderLayout, 800, 600);
        stage.setScene(orderScene);
        stage.setTitle("Place Order");
        stage.show();
    }



    private void viewWishlist(Stage stage, Customer customer) {
        // Check if the customer is null
        if (customer == null) {
            System.out.println("Customer not found.");
            return;
        }

        // Create the wishlist view layout
        BorderPane wishlistLayout = new BorderPane();

        // Navigation bar (reuse the one from the customer menu)
        VBox navigationBar = new VBox(10);
        navigationBar.setPadding(new Insets(10));
        navigationBar.setStyle("-fx-background-color: #f4f4f4;");

        Button backToMenuBtn = new Button("Back to Menu");

        navigationBar.getChildren().addAll(backToMenuBtn);
        wishlistLayout.setLeft(navigationBar);

        // Wishlist display area
        VBox wishlistItemsBox = new VBox(10);
        wishlistItemsBox.setPadding(new Insets(10));

        // Check if the wishlist has items
        if (customer.getWishlist().isEmpty()) {
            Label emptyWishlistLabel = new Label("Your wishlist is currently empty.");
            wishlistItemsBox.getChildren().add(emptyWishlistLabel);
        } else {
            // Add the wishlist items
            for (Product product : customer.getWishlist()) {
                HBox productBox = new HBox(10);
                Label productLabel = new Label(product.getName() + " - " + product.getPrice());
                Button removeBtn = new Button("Remove from Wishlist");

                removeBtn.setOnAction(e -> {
                    customer.getWishlist().remove(product);
                    wishlistItemsBox.getChildren().remove(productBox); // Update the display immediately
                    System.out.println(product.getName() + " removed from wishlist.");
                });

                productBox.getChildren().addAll(productLabel, removeBtn);
                wishlistItemsBox.getChildren().add(productBox);
            }
        }

        wishlistLayout.setCenter(wishlistItemsBox);

        // Back to Menu button functionality
        backToMenuBtn.setOnAction(e -> showCustomerMenu(stage, customer));

        // Set the scene for the wishlist view
        Scene wishlistScene = new Scene(wishlistLayout, 800, 600);
        stage.setScene(wishlistScene);
        stage.setTitle("View Wishlist");
        stage.show();
    }


    public void showUserManagerMenu(Stage stage, Admin admin) {
        // Main layout
        BorderPane mainLayout = new BorderPane();

        // Navigation bar (Optional)
        VBox navigationBar = new VBox(10);
        navigationBar.setPadding(new Insets(10));
        navigationBar.setStyle("-fx-background-color: #f4f4f4;");

        Button addUserBtn = new Button("Add New User");
        Button logoutBtn = new Button("Logout");

        navigationBar.getChildren().addAll(addUserBtn, logoutBtn);
        mainLayout.setLeft(navigationBar);

        // Users display area
        ScrollPane userScrollPane = new ScrollPane();
        VBox userVBox = new VBox(10);
        userVBox.setPadding(new Insets(10));

        // Get the list of users from the Database
        ArrayList<User> userList = Database.getUserList();

        // Category filter dropdown (e.g., Filter by Role)
        ComboBox<String> roleDropdown = new ComboBox<>();
        roleDropdown.setPromptText("Select Role");
        roleDropdown.getItems().addAll("All", "Admin", "Customer"); // Adjust roles as necessary

        // Search bar
        TextField searchBar = new TextField();
        searchBar.setPromptText("Search by username...");

        // Add search and filter controls
        VBox filterBox = new VBox(10, roleDropdown, searchBar);
        filterBox.setPadding(new Insets(10));
        mainLayout.setTop(filterBox);

        // Create a method to update the user list and handle filters
        updateUserList(userVBox, userList, roleDropdown, searchBar, stage, admin);

        // Event handler for category selection
        roleDropdown.setOnAction(e -> {
            updateUserList(userVBox, userList, roleDropdown, searchBar, stage, admin);
        });

        // Event handler for search bar
        searchBar.setOnKeyReleased(e -> {
            updateUserList(userVBox, userList, roleDropdown, searchBar, stage, admin);
        });

        userScrollPane.setContent(userVBox);
        mainLayout.setCenter(userScrollPane);

        // Set the scene on the provided stage
        Scene scene = new Scene(mainLayout, 800, 600);
        stage.setTitle("User Manager Menu");
        stage.setScene(scene);
        stage.show();

        // Handle Add User Button click
        addUserBtn.setOnAction(e -> {
            showAddUserWindow(stage, admin); // Implement this method to show the add user window
        });

        // Handle Logout Button click
        logoutBtn.setOnAction(e -> {
            showLoginScreen(); // Implement this method to show the login screen
        });
    }


    private void updateUserList(VBox userVBox, ArrayList<User> userList, ComboBox<String> roleDropdown, TextField searchBar, Stage stage, Admin admin) {
        // Clear the current list of users
        userVBox.getChildren().clear();

        String selectedRole = roleDropdown.getValue();
        if (selectedRole == null) {
            selectedRole = "All"; // Default to "All" if null
        }

        String searchQuery = searchBar.getText().toLowerCase();

        for (User user : userList) {
            boolean matchesRole = false;

            if (selectedRole.equals("All")) {
                matchesRole = true;  // All users match if no role is selected
            } else if (selectedRole.equalsIgnoreCase("Admin") && user instanceof Admin) {
                // Match if the selected role is Admin and the user is an Admin
                matchesRole = true;
            } else if (selectedRole.equalsIgnoreCase("Customer") && user instanceof Customer) {
                // Match if the selected role is Customer and the user is a Customer
                matchesRole = true;
            }

            boolean matchesSearch = user.getUsername().toLowerCase().contains(searchQuery);

            // If the user matches both the selected role and search query, display them
            if (matchesRole && matchesSearch) {
                HBox userBox = new HBox(10);
                userBox.setStyle("-fx-border-color: #ccc; -fx-padding: 10;");

                Label userNameLabel = new Label(user.getUsername());

                // View Profile Button
                Button viewProfileBtn = new Button("View Profile");
                viewProfileBtn.setOnAction(e -> {
                    showProfileWindowForAdmin(stage, user, admin); // Implement this method to show the profile
                });

                // Edit Profile Button
                Button editProfileBtn = new Button("Edit Profile");
                editProfileBtn.setOnAction(e -> {
                    showEditProfileWindowForAdmin(stage, user, admin); // Implement this method to edit the profile
                });

                // Delete Button
                Button deleteBtn = new Button("Delete");
                deleteBtn.setOnAction(e -> {
                    // Remove the user from the database
                    Database.removeUser(user);

                    // Remove the user from the user list (in memory)
                    userList.remove(user);

                    // Remove the user from the display
                    userVBox.getChildren().remove(userBox);

                    // Log the deletion
                    System.out.println("Deleted user: " + user.getUsername());

                    // Optionally, re-update the user list (to reflect changes immediately)
                    updateUserList(userVBox, userList, roleDropdown, searchBar, stage, admin);
                });


                // Add buttons and username to the HBox
                userBox.getChildren().addAll(userNameLabel, viewProfileBtn, editProfileBtn, deleteBtn);
                userVBox.getChildren().add(userBox);
            }
        }
    }


    private void showEditProfileWindowForAdmin(Stage stage, User user, Admin adminn) {
        User currentUser = user;
        Admin currentAdmin = adminn;

        VBox editLayout = new VBox(10);
        editLayout.setPadding(new Insets(20));

        // Create text fields for each editable profile property
        TextField usernameField = new TextField(currentUser.getUsername());
        PasswordField passwordField = new PasswordField();

        // Convert java.util.Date to LocalDate for DatePicker
        java.util.Date utilDate = currentUser.getDateOfBirth();
        LocalDate localDate = utilDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        DatePicker dobPicker = new DatePicker(localDate);

        // For Customer, include gender, premium, and address (but not balance)
        ComboBox<Customer.Gender> genderComboBox;
        if (currentUser instanceof Customer) {
            genderComboBox = new ComboBox<>();
            genderComboBox.getItems().setAll(Customer.Gender.values());
            genderComboBox.setValue(((Customer) currentUser).getGender());
        } else {
            genderComboBox = null;
        }

        CheckBox premiumCheckBox = new CheckBox("Premium Member");
        TextField addressField;
        if (currentUser instanceof Customer) {
            Customer customer = (Customer) currentUser;
            premiumCheckBox.setSelected(customer.isPremium());
            addressField = new TextField(customer.getAddress());
        } else {
            addressField = null;
        }

        // For Admin, include role and working hours
        TextField roleField;
        TextField workingHoursField;
        if (currentUser instanceof Admin) {
            Admin admin = (Admin) currentUser;
            roleField = new TextField(admin.getRole());
            workingHoursField = new TextField(String.valueOf(admin.getWorkingHours()));
        } else {
            workingHoursField = null;
            roleField = null;
        }

        // Add a button to save the changes
        Button saveChangesBtn = new Button("Save Changes");

        // Validation and save changes logic
        saveChangesBtn.setOnAction(e -> {
            String newUsername = usernameField.getText();
            String newPassword = passwordField.getText();
            LocalDate newDob = dobPicker.getValue();

            if (newUsername.trim().isEmpty()) {
                showAlert("Invalid Input", "Username cannot be empty.");
            } else if (!newUsername.equals(currentUser.getUsername()) && Database.findUserByUsername(newUsername) != null) {
                showAlert("Invalid Input", "Username already exists. Please choose a different username.");
            } else if (newDob == null || newDob.isAfter(LocalDate.now())) {
                showAlert("Invalid Input", "Please select a valid date of birth (not in the future).");
            } else {
                currentUser.setUsername(newUsername);

                // Only update the password if a new one is provided
                if (!newPassword.trim().isEmpty()) {
                    if (newPassword.length() < 6) {
                        showAlert("Invalid Input", "Password must be at least 6 characters long.");
                        return;
                    }
                    currentUser.setPassword(newPassword);
                }

                // Convert LocalDate back to java.util.Date
                java.util.Date updatedDate = Date.from(newDob.atStartOfDay(ZoneId.systemDefault()).toInstant());
                currentUser.setDateOfBirth(updatedDate);

                // Handle Admin-specific updates
                if (currentUser instanceof Admin) {
                    Admin admin = (Admin) currentUser;

                    // Validate role field
                    if (roleField != null && !roleField.getText().trim().isEmpty()) {
                        String newRole = roleField.getText().trim();
                        if (!newRole.matches("[a-zA-Z]+( [a-zA-Z]+)*")) {
                            showAlert("Invalid Input", "Role must contain only letters and spaces, no special characters.");
                            return; // Stop saving if the role is invalid
                        }
                        admin.setRole(newRole);
                    }

                    // Validate working hours
                    if (workingHoursField != null && !workingHoursField.getText().trim().isEmpty()) {
                        try {
                            int workingHours = Integer.parseInt(workingHoursField.getText().trim());
                            if (workingHours <= 0) {
                                showAlert("Invalid Input", "Working hours cannot be less than or equal to 0.");
                                return;
                            }
                            admin.setWorkingHours(workingHours);
                        } catch (NumberFormatException ex) {
                            showAlert("Invalid Input", "Please enter a valid number for working hours.");
                            return;
                        }
                    }
                }

                // Handle Customer-specific updates
                if (currentUser instanceof Customer) {
                    Customer customer = (Customer) currentUser;
                    customer.setGender(genderComboBox.getValue());
                    customer.setPremium(premiumCheckBox.isSelected());
                    if (addressField != null && !addressField.getText().trim().isEmpty()) {
                        customer.setAddress(addressField.getText());
                    }
                }

                Database.updateUser(currentUser);
                showAlert("Profile Updated", "Profile has been successfully updated.");
                showUserManagerMenu(stage, currentAdmin);
            }
        });

        // Add a button to go back to the User Manager menu
        Button backToProfileBtn = new Button("Back to Menu");
        backToProfileBtn.setOnAction(e -> showUserManagerMenu(stage, currentAdmin));

        // Add all elements to the edit layout
        editLayout.getChildren().addAll(new Label("Username:"), usernameField,
                new Label("Password:"), passwordField,
                new Label("Date of Birth:"), dobPicker);

        // For Customer: Add gender, premium checkbox, and address
        if (currentUser instanceof Customer) {
            editLayout.getChildren().addAll(new Label("Gender:"), genderComboBox, premiumCheckBox, new Label("Address:"), addressField);
        }

        // For Admin: Add role and working hours
        if (currentUser instanceof Admin) {
            editLayout.getChildren().addAll(new Label("Role:"), roleField,
                    new Label("Working Hours:"), workingHoursField);
        }

        editLayout.getChildren().addAll(saveChangesBtn, backToProfileBtn);

        // Create a scene and set it on the stage
        Scene editScene = new Scene(editLayout, 600, 600);
        stage.setScene(editScene);
        stage.show();
    }




    private void showProfileWindowForAdmin(Stage stage, User user, Admin adminn) {
        // Main layout
        BorderPane mainLayout = new BorderPane();

        // Navigation bar
        VBox navigationBar = new VBox(10);
        navigationBar.setPadding(new Insets(10));
        navigationBar.setStyle("-fx-background-color: #f4f4f4;");

        Button backToMenuBtn = new Button("Back to Menu");
        navigationBar.getChildren().add(backToMenuBtn);
        mainLayout.setLeft(navigationBar);

        // Profile layout
        VBox profileLayout = new VBox(10);
        profileLayout.setPadding(new Insets(20));

        // Create labels to display user data
        Label usernameLabel = new Label("Username: " + user.getUsername());
        Label dobLabel = new Label("Date of Birth: " + user.getDateOfBirth().toString());

        if (user instanceof Customer) {
            Customer customer = (Customer) user;

            // Display gender and premium status
            Label genderLabel = new Label("Gender: " + customer.getGender().toString());
            Label premiumLabel = new Label("Premium: " + (customer.isPremium() ? "Yes" : "No"));

            // Display additional information for Customer
            Label balanceLabel = new Label("Balance: $" + customer.getBalance());
            Label addressLabel = new Label("Address: " + customer.getAddress());

            profileLayout.getChildren().addAll(usernameLabel, dobLabel, genderLabel, premiumLabel, balanceLabel, addressLabel);
        } else if (user instanceof Admin) {
            Admin admin = (Admin) user;
            Label roleLabel = new Label("Role: " + admin.getRole());
            Label workingHoursLabel = new Label("Working Hours: " + admin.getWorkingHours());
            profileLayout.getChildren().addAll(usernameLabel, dobLabel, roleLabel, workingHoursLabel);
        }

        mainLayout.setCenter(profileLayout);

        // Button actions
        backToMenuBtn.setOnAction(e -> {
                showUserManagerMenu(stage, adminn); // Ensure correct admin object is passed
        });

        // Create a scene and set it on the stage
        Scene profileScene = new Scene(mainLayout, 800, 600);
        stage.setScene(profileScene);
        stage.show();
    }



    private void showAddUserWindow(Stage stage, Admin admin) {
        // Create the main layout for adding a new user
        VBox addUserLayout = new VBox(10);
        addUserLayout.setPadding(new Insets(20));

        // ComboBox to choose user type (Customer or Admin)
        ComboBox<String> userTypeComboBox = new ComboBox<>();
        userTypeComboBox.getItems().addAll("customer", "admin");
        userTypeComboBox.setPromptText("Select User Type");

        // Input fields for common user details
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        DatePicker dobPicker = new DatePicker();
        dobPicker.setPromptText("Date of Birth (yyyy-MM-dd)");

        // Customer-specific fields
        TextField addressField = new TextField();
        addressField.setPromptText("Address");

        ComboBox<Customer.Gender> genderComboBox = new ComboBox<>();
        genderComboBox.getItems().setAll(Customer.Gender.values());
        genderComboBox.setPromptText("Select Gender");

        CheckBox premiumCheckBox = new CheckBox("Premium Member");

        // Admin-specific fields
        TextField roleField = new TextField();
        roleField.setPromptText("Role");

        TextField workingHoursField = new TextField();
        workingHoursField.setPromptText("Working Hours");

        // Add all common fields to the layout
        addUserLayout.getChildren().addAll(
                new Label("Select User Type:"), userTypeComboBox,
                new Label("Username:"), usernameField,
                new Label("Password:"), passwordField,
                new Label("Date of Birth:"), dobPicker
        );

        // Placeholder for customer or admin specific fields
        VBox userSpecificFields = new VBox(10);
        addUserLayout.getChildren().add(userSpecificFields); // Initially empty, will be populated based on user type

        // Add Cancel and Create User buttons at the bottom
        Button createUserButton = new Button("Create User");
        Button cancelButton = new Button("Cancel");

        // Disable Create User button until user type is selected
        createUserButton.setDisable(true);

        createUserButton.setOnAction(e -> {
            String userType = userTypeComboBox.getValue();
            String username = usernameField.getText();
            String password = passwordField.getText();
            LocalDate dob = dobPicker.getValue();

            // Validate user inputs
            if (userType == null || username.isEmpty() || password.isEmpty() || dob == null) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Please fill in all required fields.");
                return;
            }
            else if (dob.isAfter(LocalDate.now())) {
                // Ensure date of birth is not in the future
                showAlert(Alert.AlertType.ERROR, "Invalid Date of Birth", "Date of birth cannot be in the future.");
                return;
            }
            else if (Database.findUserByUsername(username) != null) {
                // Validate username uniqueness
                showAlert("Invalid Input", "Username already exists. Please choose a different username.");
                return;
            }

            // Validate password
            if (password.length() < 6 || !password.matches(".*[A-Za-z].*") || !password.matches(".*[0-9].*")) {
                showAlert(Alert.AlertType.ERROR, "Invalid Password", "Password must be at least 6 characters long and contain both letters and numbers.");
                return;
            }

            // Create the appropriate user based on the user type
            if (userType.equals("customer")) {
                // Gather customer-specific data
                String address = addressField.getText();
                Customer.Gender gender = genderComboBox.getValue();
                boolean isPremium = premiumCheckBox.isSelected();

                if (address.isEmpty() || gender == null) {
                    showAlert(Alert.AlertType.ERROR, "Input Error", "Please fill in all customer-specific fields.");
                    return;
                }

                // Convert LocalDate to java.util.Date for Customer
                java.util.Date utilDob = java.util.Date.from(dob.atStartOfDay(ZoneId.systemDefault()).toInstant()); // Convert LocalDate to java.util.Date

                // Create new customer and add to database
                Customer newCustomer = new Customer(username, password, utilDob, address, gender, isPremium);
                Database.addUser(newCustomer);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Customer account created successfully.");
            } else if (userType.equals("admin")) {
                // Gather admin-specific data
                String role = roleField.getText();
                String workingHoursStr = workingHoursField.getText();

                if (role.isEmpty() || workingHoursStr.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Input Error", "Please fill in all admin-specific fields.");
                    return;
                }

                // Validate working hours (must be a number and >= 0)
                int workingHours;
                try {
                    workingHours = Integer.parseInt(workingHoursStr);
                    if (workingHours <= 0) {
                        showAlert(Alert.AlertType.ERROR, "Invalid Working Hours", "Working hours cannot be less than or equal 0.");
                        return;
                    }
                } catch (NumberFormatException ex) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Working Hours", "Please enter a valid number for working hours.");
                    return;
                }

                // Convert LocalDate to java.sql.Date for Admin
                java.sql.Date sqlDob = java.sql.Date.valueOf(dob); // Convert LocalDate to java.sql.Date

                // Create new admin and add to database
                Admin newAdmin = new Admin(username, password, sqlDob, role, workingHours);
                Database.addUser(newAdmin);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Admin account created successfully.");
            }

            // Refresh the user manager menu and show it
            showUserManagerMenu(stage, admin);
        });

        // Cancel button action
        cancelButton.setOnAction(e -> showUserManagerMenu(stage, admin));  // Pass the stage and admin to showUserManagerMenu

        // Dynamically display user-specific fields based on the selected user type
        userTypeComboBox.setOnAction(e -> {
            userSpecificFields.getChildren().clear(); // Clear previous fields
            createUserButton.setDisable(false); // Enable the "Create User" button once a type is selected

            if (userTypeComboBox.getValue().equals("customer")) {
                // Add customer-specific fields
                userSpecificFields.getChildren().addAll(
                        new Label("Address:"), addressField,
                        new Label("Gender:"), genderComboBox,
                        premiumCheckBox
                );
            } else if (userTypeComboBox.getValue().equals("admin")) {
                // Add admin-specific fields
                userSpecificFields.getChildren().addAll(
                        new Label("Role:"), roleField,
                        new Label("Working Hours:"), workingHoursField
                );
            }
        });

        // Add buttons at the bottom
        VBox buttonLayout = new VBox(10);
        buttonLayout.getChildren().addAll(createUserButton, cancelButton);

        // Add the button layout at the bottom of the main layout
        addUserLayout.getChildren().add(buttonLayout);

        // Show the scene
        Scene scene = new Scene(addUserLayout, 500, 600);
        stage.setScene(scene);
        stage.show();
    }


    public void showProductManagerMenu(Stage stage, Admin admin) {
        // Main layout
        BorderPane mainLayout = new BorderPane();

        // Navigation bar (Optional)
        VBox navigationBar = new VBox(10);
        navigationBar.setPadding(new Insets(10));
        navigationBar.setStyle("-fx-background-color: #f4f4f4;");

        Button addProductBtn = new Button("Add New Product");
        Button logoutBtn = new Button("Logout");

        navigationBar.getChildren().addAll(addProductBtn, logoutBtn);
        mainLayout.setLeft(navigationBar);

        // Products display area
        ScrollPane productScrollPane = new ScrollPane();
        VBox productVBox = new VBox(10);
        productVBox.setPadding(new Insets(10));

        // Get the list of products from the Database
        ArrayList<Product> productList = Database.getProductList();

        // Search bar
        TextField searchBar = new TextField();
        searchBar.setPromptText("Search by product name...");

        // Add search control
        VBox filterBox = new VBox(10, searchBar);
        filterBox.setPadding(new Insets(10));
        mainLayout.setTop(filterBox);

        // Create a method to update the product list
        updateProductList(productVBox, productList, searchBar, stage, admin);

        // Event handler for search bar
        searchBar.setOnKeyReleased(e -> {
            updateProductList(productVBox, productList, searchBar, stage, admin);
        });

        productScrollPane.setContent(productVBox);
        mainLayout.setCenter(productScrollPane);

        // Set the scene on the provided stage
        Scene scene = new Scene(mainLayout, 800, 600);
        stage.setTitle("Product Manager Menu");
        stage.setScene(scene);
        stage.show();

        // Handle Add Product Button click
        addProductBtn.setOnAction(e -> {
            showAddProductWindow(stage, admin); // Implement this method to show the add product window
        });

        // Handle Logout Button click
        logoutBtn.setOnAction(e -> {
            showLoginScreen(); // Implement this method to show the login screen
        });
    }

    private void updateProductList(VBox productVBox, ArrayList<Product> productList, TextField searchBar, Stage stage, Admin admin) {
        // Clear the current list of products
        productVBox.getChildren().clear();

        String searchQuery = searchBar.getText().toLowerCase();

        for (Product product : productList) {
            boolean matchesSearch = product.getName().toLowerCase().contains(searchQuery);

            // If the product matches the search query, display it
            if (matchesSearch) {
                HBox productBox = new HBox(10);
                productBox.setStyle("-fx-border-color: #ccc; -fx-padding: 10;");

                Label productNameLabel = new Label(product.getName());
                Label productPriceLabel = new Label("Price: " + product.getPrice());
                Label productStockLabel = new Label("Stock: " + product.getStockQuantity());

                // View Product Button
                Button viewProductBtn = new Button("View Product");
                viewProductBtn.setOnAction(e -> {
                    showViewProductWindow(stage, product, admin); // Implement this method to show the product details
                });

                // Edit Product Button
                Button editProductBtn = new Button("Edit Product");
                editProductBtn.setOnAction(e -> {
                    showEditProductWindow(stage, product, admin); // Implement this method to edit the product
                });

                // Delete Button
                Button deleteBtn = new Button("Delete");
                deleteBtn.setOnAction(e -> {
                    Database.removeProduct(product);

                    productList.remove(product);

                    productVBox.getChildren().remove(productBox);

                    System.out.println("Deleted product: " + product.getName());
                    updateProductList(productVBox, productList, searchBar, stage, admin);
                });

                // Add buttons and product details to the HBox
                productBox.getChildren().addAll(productNameLabel, productPriceLabel, productStockLabel, viewProductBtn, editProductBtn, deleteBtn);
                productVBox.getChildren().add(productBox);
            }
        }
    }

    private void showEditProductWindow(Stage stage, Product product, Admin admin) {
        VBox editProductLayout = new VBox(10);
        editProductLayout.setPadding(new Insets(20));

        TextField nameField = new TextField(product.getName());
        nameField.setPromptText("Product Name");

        TextField priceField = new TextField(String.valueOf(product.getPrice()));
        priceField.setPromptText("Price");

        TextField stockQuantityField = new TextField(String.valueOf(product.getStockQuantity()));
        stockQuantityField.setPromptText("Stock Quantity");

        TextField ratingField = new TextField(String.valueOf(product.getRating()));
        ratingField.setPromptText("Rating");

        Button saveButton = new Button("Save Changes");
        Button cancelButton = new Button("Cancel");

        saveButton.setOnAction(e -> {
            String name = nameField.getText();
            String priceStr = priceField.getText();
            String stockQuantityStr = stockQuantityField.getText();
            String ratingStr = ratingField.getText();

            /*
            if (name.isEmpty() || priceStr.isEmpty() || stockQuantityStr.isEmpty() || ratingStr.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Please fill in all required fields.");
                return;
            }
             */

            try {
                // Parse input values, use old value if field is empty
                double price = priceStr.isEmpty() ? product.getPrice() : Double.parseDouble(priceStr);
                int stockQuantity = stockQuantityStr.isEmpty() ? product.getStockQuantity() : Integer.parseInt(stockQuantityStr);
                double rating = ratingStr.isEmpty() ? product.getRating() : Double.parseDouble(ratingStr);

                // Validation checks
                if (price <= 0) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Price", "Price must be greater than 0.");
                    return;
                }
                if (stockQuantity < 0) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Stock", "Stock quantity cannot be negative.");
                    return;
                }
                if (rating < 0 || rating > 5) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Rating", "Rating must be between 0 and 5.");
                    return;
                }

                // Check for duplicate product name (ignore the current product being edited)
                if (!name.equals(product.getName()) && Database.findProductByName(name) != null) {
                    showAlert(Alert.AlertType.ERROR, "Duplicate Product", "A product with the same name already exists.");
                    return;
                }

                // Update the product details
                product.setName(name);
                product.setPrice(price);
                product.setStockQuantity(stockQuantity);
                product.setRating(rating);

                // Update the product in the database
                Database.updateProduct(product);

                // Show success message and return to the product manager menu
                showAlert(Alert.AlertType.INFORMATION, "Success", "Product updated successfully.");
                showProductManagerMenu(stage, admin);
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter valid numbers for price, stock quantity, and rating.");
            }
        });

        cancelButton.setOnAction(e -> showProductManagerMenu(stage, admin));

        // Add UI components to the layout
        editProductLayout.getChildren().addAll(
                new Label("Edit Product Details"),
                new Label("Product Name:"), nameField,
                new Label("Price:"), priceField,
                new Label("Stock Quantity:"), stockQuantityField,
                new Label("Rating:"), ratingField,
                saveButton, cancelButton
        );

        // Create and show the scene
        Scene editProductScene = new Scene(editProductLayout, 400, 400);
        stage.setScene(editProductScene);
        stage.setTitle("Edit Product");
        stage.show();
    }


    private void showAddProductWindow(Stage stage, Admin admin) {
        VBox addProductLayout = new VBox(10);
        addProductLayout.setPadding(new Insets(20));

        TextField nameField = new TextField();
        nameField.setPromptText("Product Name");

        TextField priceField = new TextField();
        priceField.setPromptText("Price");

        TextField stockQuantityField = new TextField();
        stockQuantityField.setPromptText("Stock Quantity");

        TextField ratingField = new TextField();
        ratingField.setPromptText("Rating");

        ComboBox<String> categoryComboBox = new ComboBox<>();
        for (Category category : Database.getCategoryList()) {
            categoryComboBox.getItems().add(category.getName());
        }
        categoryComboBox.setPromptText("Select Category (Optional)");

        Button createProductButton = new Button("Create Product");
        Button cancelButton = new Button("Cancel");

        createProductButton.setOnAction(e -> {
            String name = nameField.getText();
            String priceStr = priceField.getText();
            String stockQuantityStr = stockQuantityField.getText();
            String ratingStr = ratingField.getText();

            if (name.isEmpty() || priceStr.isEmpty() || stockQuantityStr.isEmpty() || ratingStr.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Please fill in all required fields.");
                return;
            }

            try {
                double price = Double.parseDouble(priceStr);
                int stockQuantity = Integer.parseInt(stockQuantityStr);
                double rating = Double.parseDouble(ratingStr);

                if (price <= 0) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Price", "Price must be greater than 0.");
                    return;
                }
                if (stockQuantity < 0) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Stock", "Stock quantity cannot be negative.");
                    return;
                }
                if (rating < 0 || rating > 5) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Rating", "Rating must be between 0 and 5.");
                    return;
                }

                // Check for duplicate product name
                if (Database.findProductByName(name) != null) {
                    showAlert(Alert.AlertType.ERROR, "Duplicate Product", "A product with the same name already exists.");
                    return;
                }

                Product newProduct = new Product(name, price, stockQuantity, rating);
                Database.addProduct(newProduct);

                String selectedCategory = categoryComboBox.getValue();
                if (selectedCategory != null && !selectedCategory.isEmpty()) {
                    Category category = Database.findCategoryByName(selectedCategory);
                    if (category != null) {
                        category.addProduct(newProduct);
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Product created and added to category.");
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Category Error", "Category not found.");
                    }
                } else {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Product created successfully.");
                }
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter valid numbers for price, stock quantity, and rating.");
            }

            showProductManagerMenu(stage, admin);
        });

        cancelButton.setOnAction(e -> showProductManagerMenu(stage, admin));

        addProductLayout.getChildren().addAll(
                new Label("Product Name:"), nameField,
                new Label("Price:"), priceField,
                new Label("Stock Quantity:"), stockQuantityField,
                new Label("Rating:"), ratingField,
                new Label("Category:"), categoryComboBox,
                createProductButton, cancelButton
        );

        Scene addProductScene = new Scene(addProductLayout, 400, 400);
        stage.setScene(addProductScene);
        stage.show();
    }


    private void showViewProductWindow(Stage stage, Product product, Admin admin) {
        // Create an information alert to show the product details
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Product Details");
        alert.setHeaderText(product.getName()); // Set the product name as the header

        // Display product attributes in the content text
        alert.setContentText("ID: " + product.getID() + "\n" +
                "Name: " + product.getName() + "\n" +
                "Price: $" + product.getPrice() + "\n" +
                "Stock Quantity: " + product.getStockQuantity() + "\n" +
                "Rating: " + product.getRating());

        // Show the alert
        alert.showAndWait();
    }

    public void showCategoryManagerMenu(Stage stage, Admin admin) {
        BorderPane mainLayout = new BorderPane();

        // Navigation bar
        VBox navigationBar = new VBox(10);
        navigationBar.setPadding(new Insets(10));
        navigationBar.setStyle("-fx-background-color: #f4f4f4;");

        Button addCategoryBtn = new Button("Add New Category");
        Button logoutBtn = new Button("Logout");

        navigationBar.getChildren().addAll(addCategoryBtn, logoutBtn);
        mainLayout.setLeft(navigationBar);

        // Categories display area
        ScrollPane categoryScrollPane = new ScrollPane();
        VBox categoryVBox = new VBox(10);
        categoryVBox.setPadding(new Insets(10));

        // Get the list of categories from the Database
        ArrayList<Category> categoryList = Database.getCategoryList();

        // Search bar
        TextField searchBar = new TextField();
        searchBar.setPromptText("Search by category name...");

        // Add search control
        VBox filterBox = new VBox(10, searchBar);
        filterBox.setPadding(new Insets(10));
        mainLayout.setTop(filterBox);

        // Update category list based on the current search query
        updateCategoryList(categoryVBox, categoryList, searchBar, stage, admin);

        // Event handler for search bar
        searchBar.setOnKeyReleased(e -> {
            updateCategoryList(categoryVBox, categoryList, searchBar, stage, admin);
        });

        categoryScrollPane.setContent(categoryVBox);
        mainLayout.setCenter(categoryScrollPane);

        // Set the scene on the provided stage
        Scene scene = new Scene(mainLayout, 800, 600);
        stage.setTitle("Category Manager Menu");
        stage.setScene(scene);
        stage.show();

        // Handle Add Category Button click
        addCategoryBtn.setOnAction(e -> {
            showAddCategoryWindow(stage, admin); // Show the Add Category window
        });

        // Handle Logout Button click
        logoutBtn.setOnAction(e -> {
            showLoginScreen(); // Show login screen
        });
    }



    public void updateCategoryList(VBox categoryVBox, ArrayList<Category> categoryList, TextField searchBar, Stage stage, Admin admin) {
        // Clear the current list of categories
        categoryVBox.getChildren().clear();

        String searchQuery = searchBar.getText().toLowerCase();

        for (Category category : categoryList) {
            boolean matchesSearch = category.getName().toLowerCase().contains(searchQuery);

            // If the category matches the search query, display it
            if (matchesSearch) {
                HBox categoryBox = new HBox(10);
                categoryBox.setStyle("-fx-border-color: #ccc; -fx-padding: 10;");

                Label categoryNameLabel = new Label(category.getName());
                Label categoryProductCountLabel = new Label("Products: " + category.getProductCount());

                // View Category Button
                Button viewCategoryBtn = new Button("View Category");
                viewCategoryBtn.setOnAction(e -> {
                    showViewCategoryWindow(stage, category, admin); // Show the category details
                });

                // Edit Category Button
                Button editCategoryBtn = new Button("Edit Category");
                editCategoryBtn.setOnAction(e -> {
                    showEditCategoryWindow(stage, category, admin); // Show edit window
                });

                // Delete Button
                Button deleteBtn = new Button("Delete");
                deleteBtn.setOnAction(e -> {
                    admin.deleteCategory(category.getNum()); // Delete category using admin
                    Database.removeCategory(category); // Remove category from DB
                    categoryList.remove(category); // Remove from local list
                    categoryVBox.getChildren().remove(categoryBox); // Remove from display
                });

                // Add buttons and category details to the HBox
                categoryBox.getChildren().addAll(categoryNameLabel, categoryProductCountLabel, viewCategoryBtn, editCategoryBtn, deleteBtn);
                categoryVBox.getChildren().add(categoryBox);
            }
        }
    }


    public void showAddCategoryWindow(Stage stage, Admin admin) {
        VBox addCategoryLayout = new VBox(10);
        addCategoryLayout.setPadding(new Insets(20));

        // Category Number Field
        TextField numberField = new TextField();
        numberField.setPromptText("Category Number");

        // Category Name Field
        TextField nameField = new TextField();
        nameField.setPromptText("Category Name");

        // Create buttons for saving or canceling the category
        Button createCategoryButton = new Button("Create Category");
        Button cancelButton = new Button("Cancel");

        // Event handler for Create Category button
        createCategoryButton.setOnAction(e -> {
            String name = nameField.getText().trim();
            String numberText = numberField.getText().trim();

            // Check if the fields are empty
            if (name.isEmpty() || numberText.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter both category number and name.");
                return;
            }

            // Try to parse the category number
            int categoryNumber = -1;
            try {
                categoryNumber = Integer.parseInt(numberText);
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Category number must be a valid integer.");
                return;
            }

            // Check if the category number or name already exists
            for (Category existingCategory : Database.getCategoryList()) {
                if (existingCategory.getNum() == categoryNumber) {
                    showAlert(Alert.AlertType.ERROR, "Input Error", "Category number already exists.");
                    return;
                }
                if (existingCategory.getName().equalsIgnoreCase(name)) {
                    showAlert(Alert.AlertType.ERROR, "Input Error", "Category name already exists.");
                    return;
                }
            }

            // Create new category using admin's createCategory method
            Category newCategory = new Category(categoryNumber, name);
            Database.addCategory(newCategory); // Add category to DB

            // Show success message
            showAlert(Alert.AlertType.INFORMATION, "Success", "Category created successfully.");

            showCategoryManagerMenu(stage, admin); // Return to Category Manager Menu
        });


        // Event handler for Cancel button
        cancelButton.setOnAction(e -> showCategoryManagerMenu(stage, admin)); // Go back to Category Manager Menu

        // Add components to the layout
        addCategoryLayout.getChildren().addAll(
                new Label("Category Number:"), numberField,
                new Label("Category Name:"), nameField,
                createCategoryButton, cancelButton
        );

        // Set up and show the scene
        Scene addCategoryScene = new Scene(addCategoryLayout, 400, 300);
        stage.setScene(addCategoryScene);
        stage.show();
    }




    public void showViewCategoryWindow(Stage stage, Category category, Admin admin) {
        // Create the main layout for the category view
        VBox viewCategoryLayout = new VBox(15);
        viewCategoryLayout.setPadding(new Insets(15));

        // Display category details with the usual font style
        Label categoryDetails = new Label("Category: " + category.getName());
        categoryDetails.setStyle("-fx-font-size: 14px; -fx-font-family: 'System';");

        // Create a VBox for the product list with extra space between products
        VBox productListVBox = new VBox(15);  // Increased spacing between products
        productListVBox.setPadding(new Insets(10));

        // Loop through products and add them in a simpler format
        for (Product product : category.getAllProducts()) {
            // Create a label for each product with the usual font style
            Label productNameLabel = new Label("Product: " + product.getName());
            productNameLabel.setStyle("-fx-font-size: 14px; -fx-font-family: 'System';");

            Label productPriceLabel = new Label("Price: $" + String.format("%.2f", product.getPrice()));
            productPriceLabel.setStyle("-fx-font-size: 12px; -fx-font-family: 'System';");

            Label productRatingLabel = new Label("Rating: " + product.getRating() + " ★");
            productRatingLabel.setStyle("-fx-font-size: 12px; -fx-font-family: 'System';");

            // Add product details to the product list VBox
            productListVBox.getChildren().addAll(productNameLabel, productPriceLabel, productRatingLabel);
        }

        // Add the product list VBox to the main layout
        viewCategoryLayout.getChildren().addAll(categoryDetails, productListVBox);

        // Add the back button (no extra styling)
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> showCategoryManagerMenu(stage, admin)); // Go back to Category Manager Menu

        viewCategoryLayout.getChildren().add(backButton);

        // Wrap the entire layout in a ScrollPane to make it scrollable
        ScrollPane scrollPane = new ScrollPane(viewCategoryLayout);
        scrollPane.setFitToWidth(true); // Ensure the content fits the width of the ScrollPane
        scrollPane.setPrefHeight(600); // Set a preferred height for the ScrollPane

        // Set up and show the scene
        Scene viewCategoryScene = new Scene(scrollPane, 600, 600); // Set the scene to the ScrollPane
        stage.setScene(viewCategoryScene);
        stage.setTitle("Category Details");
        stage.show();
    }



    public void showEditCategoryWindow(Stage stage, Category category, Admin admin) {
        VBox editCategoryLayout = new VBox(10);
        editCategoryLayout.setPadding(new Insets(20));

        // Category Number Field (disabled, because the number can't be changed)
        TextField numberField = new TextField(String.valueOf(category.getNum()));
        numberField.setPromptText("Category Number");
        numberField.setDisable(true);  // Category number is not editable

        // Category Name Field (editable)
        TextField nameField = new TextField(category.getName());
        nameField.setPromptText("Category Name");

        // Display list of products in this category
        VBox productListVBox = new VBox(10);
        productListVBox.setPadding(new Insets(10));
        for (Product product : category.getAllProducts()) {
            HBox productBox = new HBox(10);
            productBox.setStyle("-fx-border-color: #ccc; -fx-padding: 10;");
            Label productNameLabel = new Label(product.getName());
            Label productPriceLabel = new Label("Price: " + product.getPrice());

            // Remove Product Button
            Button removeProductButton = new Button("Remove Product");
            removeProductButton.setOnAction(e -> {
                // Handle product removal
                category.removeProductFromCategory(product); // Remove from category
                Database.updateCategory(category); // Update the category in the database
                showEditCategoryWindow(stage, category, admin); // Refresh the window
            });

            productBox.getChildren().addAll(productNameLabel, productPriceLabel, removeProductButton);
            productListVBox.getChildren().add(productBox);
        }

        // ComboBox to add a product to the category
        ComboBox<Product> productComboBox = new ComboBox<>();
        productComboBox.getItems().addAll(Database.getProductList()); // Assuming Database.getProductList() returns all products

        Button addProductButton = new Button("Add Product");
        addProductButton.setOnAction(e -> {
            Product selectedProduct = productComboBox.getValue();
            if (selectedProduct != null) {
                // Check if the product is already in the category
                if (category.getAllProducts().contains(selectedProduct)) {
                    showAlert(Alert.AlertType.ERROR, "Product Already Exists", "This product is already in the category.");
                    return; // Don't add the product again
                }
                category.addProduct(selectedProduct); // Add the selected product to the category
                Database.updateCategory(category); // Update the category in the database
                showEditCategoryWindow(stage, category, admin); // Refresh the window
            } else {
                showAlert(Alert.AlertType.ERROR, "Selection Error", "Please select a product to add.");
            }
        });

        Button saveCategoryButton = new Button("Save Changes");
        Button cancelButton = new Button("Cancel");

        // Event handler for Save Changes button
        saveCategoryButton.setOnAction(e -> {
            String name = nameField.getText().trim();

            // Check if the name field is empty
            if (name.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter a category name.");
                return;
            }

            // Check if the category name already exists in another category
            for (Category existingCategory : Database.getCategoryList()) {
                if (!existingCategory.equals(category) && existingCategory.getName().equalsIgnoreCase(name)) {
                    showAlert(Alert.AlertType.ERROR, "Input Error", "Another category with the same name already exists.");
                    return;
                }
            }

            // Update the category's name
            category.setName(name);
            Database.updateCategory(category); // Update the category in the database

            // Show success message
            showAlert(Alert.AlertType.INFORMATION, "Success", "Category updated successfully.");

            showCategoryManagerMenu(stage, admin); // Return to Category Manager Menu
        });

        // Event handler for Cancel button
        cancelButton.setOnAction(e -> showCategoryManagerMenu(stage, admin)); // Go back to Category Manager Menu

        // Add components to the layout
        editCategoryLayout.getChildren().addAll(
                new Label("Category Number:"), numberField,
                new Label("Category Name:"), nameField,
                productListVBox, productComboBox, addProductButton, saveCategoryButton, cancelButton
        );

        // Set up and show the scene
        Scene editCategoryScene = new Scene(editCategoryLayout, 600, 600); // Increased size for vertical length
        stage.setScene(editCategoryScene);
        stage.show();
    }


    public static void main(String[] args) {
        launch(args); // Launch the JavaFX application
    }
}


