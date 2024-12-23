package Main;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class Main {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Admin admin1 = new Admin("admin1", "AdminPass1", dateFormat.parse("1985-01-15"), "User Manager", 40);
        Admin admin2 = new Admin("admin2", "AdminPass2", dateFormat.parse("1990-02-25"), "Product Manager", 35);
        Admin admin3 = new Admin("admin3", "AdminPass3", dateFormat.parse("1987-02-13"), "Category Manager", 40);
        Admin admin4 = new Admin("admin4", "AdminPass4", dateFormat.parse("1985-12-28"), "Order Manager", 35);
        Database.addUser(admin1);
        Database.addUser(admin2);
        Database.addUser(admin3);
        Database.addUser(admin4);

        Customer customer1 = new Customer("customer1", "CustPass1", dateFormat.parse("2000-03-10"), "123 Main St", Customer.Gender.MALE, true);
        customer1.setBalance(1000);
        Customer customer2 = new Customer("customer2", "CustPass2", dateFormat.parse("1995-07-22"), "456 Elm St", Customer.Gender.FEMALE, false);
        customer2.setBalance(500);
        Customer customer3 = new Customer("customer3", "CustPass3", dateFormat.parse("1998-04-15"), "789 Oak St", Customer.Gender.MALE, true);
        customer3.setBalance(700);
        Customer customer4 = new Customer("customer4", "CustPass4", dateFormat.parse("1993-12-05"), "321 Pine St", Customer.Gender.FEMALE, false);
        customer4.setBalance(300);
        Database.addUser(customer1);
        Database.addUser(customer2);
        Database.addUser(customer3);
        Database.addUser(customer4);


        Product product1 = new Product("Laptop", 1200.00, 10, 4.5);
        Product product2 = new Product("Smartphone", 800.00, 20, 4.7);
        Product product3 = new Product("Tablet", 400.00, 15, 4.3);
        Product product4 = new Product("Smartwatch", 250.00, 30, 4.6);
        Product product5 = new Product("Headphones", 150.00, 25, 4.4);
        Product product6 = new Product("Gaming Console", 500.00, 8, 4.8);
        Product product7 = new Product("Camera", 700.00, 12, 4.2);
        Database.addProduct(product1);
        Database.addProduct(product2);
        Database.addProduct(product3);
        Database.addProduct(product4);
        Database.addProduct(product5);
        Database.addProduct(product6);
        Database.addProduct(product7);


        Category category1 = new Category(401, "Electronics");
        category1.addProduct(product1);
        category1.addProduct(product2);
        category1.addProduct(product3);
        Database.addCategory(category1);

        Category category2 = new Category(402, "Wearables");
        category2.addProduct(product4);
        category2.addProduct(product5);
        Database.addCategory(category2);

        Category category3 = new Category(403, "Gaming");
        category3.addProduct(product6);
        category3.addProduct(product7);
        Database.addCategory(category3);

        LoginPage.main(args);

        User loggedInUser = null;

        while (true) {
            if (loggedInUser == null) {
                System.out.println("Welcome! Please choose an option:");
                System.out.println("1. Login");
                System.out.println("2. Sign Up");
                System.out.println("3. Exit");

                int mainChoice = -1; // Default value for invalid input
                while (mainChoice == -1) {
                    System.out.print("Enter your choice: ");
                    try {
                        mainChoice = Integer.parseInt(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a valid number.");
                    }
                }
                switch (mainChoice) {
                    case 1: // Login
                        loggedInUser = logIn();

                        if (loggedInUser instanceof Customer) {
                            Customer customer = (Customer) loggedInUser;
                            showCustomerMenu(customer); // Customer menu
                        } else if (loggedInUser instanceof Admin) {
                            Admin admin = (Admin) loggedInUser;

                            // Check admin role and show the corresponding menu
                            switch (admin.getRole().toLowerCase()) {
                                case "user manager":
                                    showUserManagerMenu(admin);
                                    break;
                                case "product manager":
                                    showProductManagerMenu(admin);
                                    break;
                                case "category manager":
                                    showCategoryManagerMenu(admin);
                                    break;
                                default:
                                    System.out.println("No access to database.");
                                    break;
                            }
                        }

                        loggedInUser = null; // Reset logged-in user to return to login prompt
                        break;

                    case 2: // Sign Up
                        signUp();
                        break;

                    case 3: // Exit
                        System.out.println("Thank you for using the system. Goodbye!");
                        System.exit(0);
                        break;

                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        }
    }

    public static User logIn() {
        Scanner scanner = new Scanner(System.in);
        User loggedInUser = null;

        while (loggedInUser == null) {
            System.out.print("Enter username: ");
            String username = scanner.nextLine();

            // Validate username
            if (username.isEmpty()) {
                System.out.println("Username cannot be empty. Please try again.");
                continue;
            }

            // Find the user by username
            loggedInUser = Database.findUserByUsername(username);

            if (loggedInUser != null) {
                boolean passwordValid = false;
                int attempts = 0; // Track password attempts

                // Ask for password if username is valid
                while (!passwordValid) {
                    System.out.print("Enter password: ");
                    String password = scanner.nextLine();
                    attempts++;

                    // Validate password
                    if (password.isEmpty()) {
                        System.out.println("Password cannot be empty. Please try again.");
                        continue;
                    }

                    if (password.length() < 6) {
                        System.out.println("Password must be at least 6 characters long. Please try again.");
                        continue;
                    }

                    // Check password correctness
                    if (loggedInUser.getPassword().equals(password)) {
                        System.out.println("Login successful!");
                        passwordValid = true;
                    } else {
                        System.out.println("Incorrect password.");

                        // Check if the user forgot their password after 3 failed attempts
                        if (attempts >= 3) {
                            System.out.println("Have you forgotten your password? (Y/N)");
                            String forgotPasswordChoice = scanner.nextLine().trim().toLowerCase();

                            if (forgotPasswordChoice.equals("y")) {
                                System.out.println("Your password is: " + loggedInUser.getPassword());
                                System.out.println("Please try logging in again.");
                            } else if (!forgotPasswordChoice.equals("n")) {
                                System.out.println("Invalid input. Please enter 'Y' for Yes or 'N' for No.");
                            }
                        }
                    }
                }
            } else {
                // Handle invalid username
                System.out.println("Invalid username.");

                // Ask the user if they want to sign up
                System.out.println("Would you like to sign up? (Y/N)");
                String signUpChoice = scanner.nextLine().trim().toLowerCase();

                if (signUpChoice.equals("y")) {
                    signUp(); // Call sign-up method
                    return null; // Restart login after sign-up
                } else if (!signUpChoice.equals("n")) {
                    System.out.println("Invalid input. Please enter 'Y' or 'N'.");
                }
            }
        }

        return loggedInUser;
    }


    public static void signUp() {
        System.out.println("Sign Up for a new Customer Account:");
        Admin.createCustomer();

        User lastUser = Database.getLastAddedUser();
        if (lastUser instanceof Customer) {
            Customer newCustomer = (Customer) lastUser;
            System.out.println("Sign-up successful!");
            showCustomerMenu(newCustomer);
        }
    }

    public static void showCustomerMenu(Customer customer) {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("Welcome, " + customer.getUsername() + "!");
            System.out.println("Available options:");
            System.out.println("1. Edit Profile");
            System.out.println("2. View Products");
            System.out.println("3. View Cart");
            System.out.println("4. View Wishlist");
            System.out.println("5. Place Order");
            System.out.println("6. Logout");

            Scanner scanner = new Scanner(System.in);

            int choice = -1; // Default value for invalid input
            while (choice == -1) {
                System.out.print("Enter your choice: ");
                try {
                    choice = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                }
            }
            switch (choice) {
                case 1:
                    editProfile(customer);
                    break;
                case 2:
                    viewProducts(customer);
                    break;
                case 3:
                    viewCart(customer);
                    break;
                case 4:
                    viewWishlist(customer);
                    break;
                case 5:
                    placeOrder(customer);
                    break;
                case 6:
                    System.out.println("Logging out. Returning to the login screen.");
                    loggedIn = false;
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    public static void placeOrder(Customer customer) {

        if (customer.getCart().getItems().isEmpty()) {
            System.out.println("Your cart is empty. Add products to the cart before placing an order.");
            return;
        }

        // Ask for the payment method
        System.out.println("Choose a payment method:");
        System.out.println("1. Credit Card");
        System.out.println("2. Application Balance");

        Scanner scanner = new Scanner(System.in);

        int paymentChoice = -1; // Default value for invalid input
        while (paymentChoice == -1) {
            System.out.print("Enter your choice: ");
            try {
                paymentChoice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
        PaymentMethod paymentMethod = null;

        switch (paymentChoice) {
            case 1:
                // Ask for credit card details
                System.out.print("Enter your credit card number: ");
                String cardNumber = scanner.next();
                System.out.print("Enter the expiration date (MM/YY): ");
                String expiryDate = scanner.next();
                System.out.print("Enter the CVV: ");
                String cvv = scanner.next();

                paymentMethod = new CreditCardPayment(customer, cardNumber, expiryDate, cvv);
                break;

            case 2:
                // Use the customer's application balance
                paymentMethod = new ApplicationPayment(customer);
                break;

            default:
                System.out.println("Invalid choice.");
        }


        Order order = new Order(customer, paymentMethod);
        order.placeOrder();  // This will process the payment and complete the order
        Database.addOrder(order);

        // After order is placed, clear the cart
        customer.getCart().clearCart();

        System.out.println("Your order has been placed successfully!");
    }

    public static void removeProductFromWishList(Customer customer) {

        if (customer.getWishlist().isEmpty()) {
            System.out.println("Your wishlist is currently empty.");
        } else {
            System.out.print("Enter the ID of the product you want to remove: ");
            Scanner scanner = new Scanner(System.in);

            int productId = -1; // Default value for invalid input
            while (productId == -1) {
                try {
                    productId = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                    System.out.print("Enter your choice: ");
                }
            }

            // Check if the product exists in the customer's wishlist
            Product selectedProduct = Database.findProductByID(productId);
            if (selectedProduct != null) {
                if (customer.getWishlist().contains(selectedProduct)) {
                    customer.removeFromWishlist(selectedProduct);
                    System.out.println(selectedProduct.getName() + " has been removed successfully.");
                } else {
                    System.out.println("The product with ID " + productId + " is not in your wishlist.");
                }
            } else {
                System.out.println("Invalid product ID.");
            }
        }
    }


    public static void viewWishlist(Customer customer) {
        if (customer == null) {
            System.out.println("Customer not found.");
            return;
        }

        ArrayList<Product> wishlist = customer.getWishlist();

        if (wishlist.isEmpty()) {
            System.out.println("Your wishlist is currently empty.");
        } else {
            System.out.println("Your wishlist contains the following items:");
            for (Product product : wishlist) {
                System.out.println("Product ID: " + product.getID() + " - " + product.getName() + " - " + product.getPrice() + " $");
            }
        }

        boolean editingWishlist = true;
        while (editingWishlist) {
            System.out.println("\nDo you want to edit your wishlist?");
            System.out.println("1. Add a product");
            System.out.println("2. Remove a product");
            System.out.println("3. Exit wishlist");
            Scanner scanner = new Scanner(System.in);

            int choice = -1; // Default value for invalid input
            while (choice == -1) {
                System.out.print("Enter your choice: ");
                try {
                    choice = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                }
            }

            switch (choice) {
                case 1:
                    viewProducts(customer);
                    break;

                case 2:
                    removeProductFromWishList(customer);
                    break;

                case 3:
                    // Exit the cart editing
                    editingWishlist = false;
                    System.out.println("Exiting wishlist.");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }


    public static void viewCart(Customer customer) {
        if (customer == null) {
            System.out.println("Customer not found.");
            return;
        }

        if (customer.getCart().getItems().isEmpty()) {
            System.out.println("Your cart is currently empty.");
        } else {
            customer.getCart().showCart();
        }

        boolean editingCart = true;
        while (editingCart) {
            System.out.println("\nDo you want to edit your cart?");
            System.out.println("1. Add a product");
            System.out.println("2. Remove a product");
            System.out.println("3. Exit Cart");

            Scanner scanner = new Scanner(System.in);

            int choice = -1; // Default value for invalid input
            while (choice == -1) {
                System.out.print("Enter your choice: ");
                try {
                    choice = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                }
            }

            switch (choice) {
                case 1:
                    viewProducts(customer);
                    break;

                case 2:
                    removeProductFromCart(customer);
                    break;

                case 3:
                    // Exit the cart editing
                    editingCart = false;
                    System.out.println("Exiting cart.");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public static void removeProductFromCart(Customer customer) {
        if (customer.getCart().getItems().isEmpty()) {
            System.out.println("Your cart is currently empty.");
        } else {
            Scanner scanner = new Scanner(System.in);
            int productId = -1;

            // Prompt for product ID until a valid number is entered
            while (productId == -1) {
                System.out.print("Enter the ID of the product you want to remove: ");
                try {
                    productId = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                }
            }

            Product selectedProduct = Database.findProductByID(productId);

            // Check if the product exists and is in the cart
            if (selectedProduct != null) {
                if (customer.getCart().getItems().contains(selectedProduct)) {
                    customer.getCart().removeItem(selectedProduct);
                    System.out.println(selectedProduct.getName() + " has been removed from your cart.");
                } else {
                    System.out.println("The product with ID " + productId + " is not in your cart.");
                }
            } else {
                System.out.println("Invalid product ID.");
            }
        }
    }


    public static void viewProducts(Customer customer) {
        if (customer == null) {
            System.out.println("Customer not found.");
            return;
        }

        ArrayList<Category> categories = Database.getCategoryList();

        System.out.println("Here are the available categories:");

        if (categories.isEmpty()) {
            System.out.println("No categories available.");
        } else {
            for (Category category : categories) {
                System.out.println("Category: " + category.getName());

                ArrayList<Product> products = category.getAllProducts();
                if (products.isEmpty()) {
                    System.out.println("   No products in this category.");
                } else {
                    for (Product product : products) {
                        System.out.println("   ID: " + product.getID() + " - " + product.getName() + " - " + product.getPrice() + " LE" + " - quantity is: " + product.getStockQuantity());
                    }
                }
            }
            boolean addingProducts = true;
            while (addingProducts) {
                System.out.print("Do you want to add a product to your cart, wishlist, or exit? (cart/wishlist/exit): ");
                Scanner scanner = new Scanner(System.in);
                String answer = scanner.nextLine().toLowerCase();

                if (answer.equals("cart")){
                    int productId = -1; // Default value for invalid input
                    while (productId == -1) {
                        System.out.print("Enter the ID of the product you want to add to your cart: ");
                        try {
                            productId = Integer.parseInt(scanner.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please enter a valid number.");
                        }
                    }

                    Product selectedProduct = Database.findProductByID(productId);
                    if (selectedProduct != null) {
                        if(Database.isProductInStock(productId)) {
                            customer.getCart().addItem(selectedProduct);
                            System.out.println(selectedProduct.getName() + " has been added to your cart.");
                        } else{
                            System.out.println("Product out of stock.");
                        }
                    } else {
                        System.out.println("Invalid product ID.");
                    }
                } else if (answer.equals("wishlist")) {

                    int productId = -1; // Default value for invalid input
                    while (productId == -1) {
                        System.out.print("Enter the ID of the product you want to add to your wishlist: ");
                        try {
                            productId = Integer.parseInt(scanner.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please enter a valid number.");
                        }
                    }

                    Product selectedProduct = Database.findProductByID(productId);
                    if (selectedProduct != null) {
                        customer.addToWishlist(selectedProduct);  // Add to wishlist
                        System.out.println(selectedProduct.getName()  + "Product added to your wishlist.");
                    } else {
                        System.out.println("Invalid product ID.");
                    }
                } else if (answer.equals("exit")) {
                    addingProducts = false;  // Exit the loop
                } else {
                    System.out.println("Invalid choice. Please enter 'cart', 'wishlist', or 'exit'.");
                }
            }
        }
    }


    public static void editProfile(User user) {
        Scanner scanner = new Scanner(System.in);
        boolean validate = false;

        while (!validate) {
            user.viewProfile();
            System.out.println("\nWhat would you like to edit?");
            System.out.println("1. Change username");
            System.out.println("2. Change password");
            System.out.println("3. Change date of birth");

            if (user instanceof Customer) {
                System.out.println("4. Change address");
                System.out.println("5. Change gender");
                System.out.println("6. Toggle premium status");
                System.out.println("7. Add funds");
                System.out.println("8. Remove funds");
            } else if (user instanceof Admin) {
                System.out.println("4. Change role");
                System.out.println("5. Change working hours");
            }

            System.out.println("9. Exit");

            int choice = -1; // Default value for invalid input
            while (choice == -1) {
                System.out.print("Enter your choice: ");
                try {
                    choice = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                }
            }

            switch (choice) {
                case 1:
                    boolean validInput = false;

                    while (!validInput) {
                        System.out.print("Enter username: ");
                        String inputUsername = scanner.nextLine();

                        // Validate the username (e.g., it shouldn't be empty)
                        if (inputUsername == null || inputUsername.trim().isEmpty()) {
                            System.out.println("Username cannot be empty. Please try again.");
                        } else if (Database.findUserByUsername(inputUsername) != null) {
                            // Check if the username already exists in the database
                            System.out.println("Username already exists. Please choose another username.");
                        } else {
                            // If username is valid, set it
                            user.setUsername(inputUsername);
                            System.out.println("Username updated successfully.");
                            validInput = true; // Break the loop since the input is valid
                        }
                    }
                    break;


                case 2:
                    boolean validPassword = false;

                    while (!validPassword) {
                        System.out.print("Enter new password: ");
                        String newPassword = scanner.nextLine();

                        // Password validation
                        if (newPassword.isEmpty()) {
                            System.out.println("Password cannot be empty.");
                        } else if (newPassword.length() < 6) {
                            System.out.println("Password must be at least 6 characters long.");
                        } else if (!newPassword.matches(".*[A-Za-z].*")) {
                            System.out.println("Password must contain at least one letter.");
                        } else if (!newPassword.matches(".*[0-9].*")) {
                            System.out.println("Password must contain at least one number.");
                        } else {
                            user.setPassword(newPassword);
                            System.out.println("Password updated successfully.");
                            validPassword = true;
                        }
                    }
                    break;


                case 3:
                    boolean validDateOfBirth = false;

                    while (!validDateOfBirth) {
                        System.out.print("Enter new date of birth (yyyy-MM-dd): ");
                        String dobInput = scanner.nextLine();

                        // Validate the format using a regex pattern for yyyy-MM-dd
                        if (!dobInput.matches("\\d{4}-\\d{2}-\\d{2}")) {
                            System.out.println("Invalid date format. Please enter the date in YYYY-MM-DD format.");
                        } else {
                            try {

                                Date newDateOfBirth = new SimpleDateFormat("yyyy-MM-dd").parse(dobInput);

                                user.setDateOfBirth(newDateOfBirth);
                                System.out.println("Date of birth updated successfully.");
                                validDateOfBirth = true;

                            } catch (ParseException e) {
                                System.out.println("Error parsing date. Please enter the date in the correct format.");
                            }
                        }
                    }
                    break;

                case 4:
                    if (user instanceof Customer customer) {
                        boolean validAddress = false;

                        while (!validAddress) {
                            System.out.print("Enter new address: ");
                            String newAddress = scanner.nextLine();

                            if (newAddress != null && !newAddress.trim().isEmpty() && newAddress.length() > 5) {
                                customer.setAddress(newAddress);
                                System.out.println("Address updated successfully.");
                                validAddress = true;    // Exit loop on successful update
                            } else {
                                System.out.println("Invalid address. Please provide a valid address with more than 5 characters.");
                            }
                        }
                    }
                    else if (user instanceof Admin admin) {
                        boolean validRole = false;

                        while (!validRole) {
                            System.out.print("Enter new role: ");
                            String newRole = scanner.nextLine();

                            // Regular expression to check if the role contains only letters and spaces
                            // Ensures that each word starts with a letter and spaces are allowed between words
                            if (newRole.matches("[a-zA-Z]+( [a-zA-Z]+)*")) {
                                admin.setRole(newRole);
                                System.out.println("Role updated successfully.");
                                validRole = true;   // Exit loop on successful update
                            } else {
                                System.out.println("Invalid role. Please enter a valid role (letters and spaces only, no special characters).");
                            }
                        }
                    }

                    break;

                case 5:
                    if (user instanceof Customer customer) {
                        boolean validGender = false;

                        while (!validGender) {
                            System.out.print("Enter new gender (male/female): ");
                            String genderInput = scanner.nextLine().toLowerCase();

                            // Validate gender input
                            if (genderInput.equals("male") || genderInput.equals("female")) {
                                Customer.Gender newGender = Customer.Gender.valueOf(genderInput.toUpperCase());
                                customer.setGender(newGender);
                                System.out.println("Gender updated successfully.");
                                validGender = true;  // Exit loop on successful update
                            } else {
                                System.out.println("Invalid gender input. Please enter 'male' or 'female'.");
                            }
                        }
                    } else if (user instanceof Admin admin) {
                        boolean validWorkingHours = false;

                        while (!validWorkingHours) {
                            System.out.print("Enter new working hours: ");
                            String input = scanner.nextLine();

                            if (input.matches("\\d+")) {
                                int newHours = Integer.parseInt(input);
                                admin.setWorkingHours(newHours);
                                System.out.println("Working hours updated successfully.");
                                validWorkingHours = true;  // Exit loop on successful update
                            } else {
                                System.out.println("Invalid input. Please enter a valid number.");
                            }
                        }
                    }
                    break;

                case 6: // Toggle premium status
                    if (user instanceof Customer customer) {
                        boolean newPremium = false;
                        boolean validPremium = false;

                        do {
                            System.out.print("Is the customer a premium member? (true/false)  ");
                            String premiumInput = scanner.nextLine().trim();

                            if (premiumInput.equalsIgnoreCase("true")) {
                                newPremium = true;
                                customer.setPremium(newPremium);
                                validPremium = true;
                            } else if (premiumInput.equalsIgnoreCase("false")) {
                                customer.setPremium(newPremium);
                                validPremium = true;
                            } else {
                                System.out.println("Invalid input for premium status. Please enter 'true' or 'false'.");
                            }
                        } while (!validPremium);
                    }
                        System.out.println("Premium status updated successfully.");
                    break;

                case 7: // Add funds for Customer
                    if (user instanceof Customer customer) {
                        double amount = -1;

                        while (amount <= 0 || customer.getBalance() + amount > 5000) {
                            System.out.print("Enter amount to add: ");
                            String input = scanner.nextLine();

                            try {
                                // Try parsing the input as a double
                                amount = Double.parseDouble(input);

                                // Check if amount is positive and doesn't exceed balance limit
                                if (amount <= 0) {
                                    System.out.println("Amount to add must be positive. Provided amount: " + amount);
                                } else if (customer.getBalance() + amount > 5000) {
                                    System.out.println("Adding this amount would exceed the balance limit of 5,000. Provided amount: " + amount);
                                } else {
                                    // Add funds to the customer's balance
                                    customer.addFunds(amount);
                                    System.out.println("Funds added successfully. Current balance: " + customer.getBalance());
                                    break; // Exit the loop after a successful transaction
                                }
                            } catch (NumberFormatException e) {
                                // Handle case where an input mismatch is encountered
                                System.out.println("Invalid input. Please enter a valid number.");
                                scanner.nextLine(); // Consume the invalid input
                            }
                        }

                    }
                    break;


                case 8: // Remove funds for Customer
                    if (user instanceof Customer customer) {
                        double amount = 0;
                        boolean validAmount = false;

                        while (!validAmount) {
                            System.out.print("Enter the amount to remove: ");

                            try {
                                amount = scanner.nextDouble();

                                // Validate the amount entered
                                if (amount > 0) {
                                    // Check if the balance is sufficient before removing funds
                                    if (customer.getBalance() >= amount) {
                                        customer.removeFunds(amount);
                                        System.out.println("Funds removed successfully.");
                                        validAmount = true; // Exit the loop after a successful transaction
                                    } else {
                                        System.out.println("Insufficient funds. Cannot remove the specified amount.");
                                    }
                                } else {
                                    System.out.println("Invalid amount. Please enter an amount greater than 0.");
                                }
                            } catch (InputMismatchException e) {
                                // Handle the case where input is not a valid number
                                System.out.println("Invalid input. Please enter a valid number.");
                                scanner.nextLine();
                            }
                        }
                    }
                    break;

                case 9: // Exit
                    validate = true;
                    System.out.println("Exiting profile editing.");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }


    // Menu for admins
    public static void showUserManagerMenu(Admin admin) {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("Welcome, User Manager!");
            System.out.println("Available options:");
            System.out.println("1. Edit Profile");
            System.out.println("2. Add User");
            System.out.println("3. Remove User");
            System.out.println("4. View All Users");
            System.out.println("5. View User");
            System.out.println("6. Read User");
            System.out.println("7. Exit");

            Scanner scanner = new Scanner(System.in);

            int choice = -1; // Default value for invalid input
            while (choice == -1) {
                System.out.print("Enter your choice: ");
                try {
                    choice = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                }
            }

            switch (choice) {
                case 1:
                    editProfile(admin);
                    break;
                case 2:
                    admin.createUser();
                    break;
                case 3:
                    int userID = -1; // Default value for invalid input
                    while (userID == -1) {
                        System.out.println("Enter id of the user you want to delete:");
                        try {
                            userID = Integer.parseInt(scanner.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please enter a valid number.");
                        }
                    }
                    admin.deleteUser(userID);
                    break;
                case 4:
                    admin.showAllUsers();
                    break;
                case 5:
                    int userID1 = -1; // Default value for invalid input
                    while (userID1 == -1) {
                        System.out.println("Enter id of the user you want to update:");
                        try {
                            userID1 = Integer.parseInt(scanner.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please enter a valid number.");
                        }
                    }
                    admin.updateUser(userID1);
                    break;

                case 6:
                    int userID2 = -1; // Default value for invalid input
                    while (userID2 == -1) {
                        System.out.println("Enter id of the user you want to view:");
                        try {
                            userID2 = Integer.parseInt(scanner.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please enter a valid number.");
                        }
                    }
                    admin.readUser(userID2);
                    break;
                case 7:
                    loggedIn = false;
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }


    public static void showProductManagerMenu(Admin admin) {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("Welcome, Product Manager!");
            System.out.println("Available options:");
            System.out.println("1. Edit Profile");
            System.out.println("2. Add Product");
            System.out.println("3. Remove Product");
            System.out.println("4. View All Products");
            System.out.println("5. Update Product");
            System.out.println("6. View Product");
            System.out.println("7. Exit");

            Scanner scanner = new Scanner(System.in);

            int choice = -1; // Default value for invalid input
            while (choice == -1) {
                System.out.print("Enter your choice: ");
                try {
                    choice = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                }
            }

            switch (choice) {
                case 1:
                    editProfile(admin);
                    break;
                case 2:
                    admin.createProduct();
                    break;
                case 3:
                    int productID = -1; // Default value for invalid input
                    while (productID == -1) {
                        System.out.println("Enter id of the product you want to delete:");
                        try {
                            productID = Integer.parseInt(scanner.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please enter a valid number.");
                        }
                    }
                    admin.deleteProduct(productID);
                    break;
                case 4:
                    admin.showAllProducts();
                    break;
                case 5:
                    int productID1 = -1; // Default value for invalid input
                    while (productID1 == -1) {
                        System.out.println("Enter id of the product you want to update:");
                        try {
                            productID1 = Integer.parseInt(scanner.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please enter a valid number.");
                        }
                    }
                    admin.updateProduct(productID1);
                    break;

                case 6:
                    int productID2 = -1; // Default value for invalid input
                    while (productID2 == -1) {
                        System.out.println("Enter id of the product you want to view:");
                        try {
                            productID2 = Integer.parseInt(scanner.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please enter a valid number.");
                        }
                    }
                    admin.readProduct(productID2);
                    break;
                case 7:
                    loggedIn = false;
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }


    public static void showCategoryManagerMenu(Admin admin) {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("Welcome, Category Manager!");
            System.out.println("Available options:");
            System.out.println("1. Edit Profile");
            System.out.println("2. Add Category");
            System.out.println("3. Remove Category");
            System.out.println("4. View All Categories");
            System.out.println("5. Update Category");
            System.out.println("6. View Category");
            System.out.println("7. Exit");

            Scanner scanner = new Scanner(System.in);

            int choice = -1; // Default value for invalid input
            while (choice == -1) {
                System.out.print("Enter your choice: ");
                try {
                    choice = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                }
            }

            switch (choice) {
                case 1:
                    editProfile(admin);
                    break;
                case 2:
                    admin.createCategory();
                    break;
                case 3:
                    int categoryID = -1; // Default value for invalid input
                    while (categoryID == -1) {
                        System.out.println("Enter id of the category you want to delete:");
                        try {
                            categoryID = Integer.parseInt(scanner.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please enter a valid number.");
                        }
                    }
                    admin.deleteCategory(categoryID);
                    break;
                case 4:
                    admin.showAllCategories();
                    break;
                case 5:
                    int categoryID1 = -1; // Default value for invalid input
                    while (categoryID1 == -1) {
                        System.out.println("Enter id of the category you want to update:");
                        try {
                            categoryID1 = Integer.parseInt(scanner.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please enter a valid number.");
                        }
                    }
                    admin.updateCategory(categoryID1);
                    break;

                case 6:
                    int categoryID2 = -1; // Default value for invalid input
                    while (categoryID2 == -1) {
                        System.out.println("Enter id of the category you want to view:");
                        try {
                            categoryID2 = Integer.parseInt(scanner.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please enter a valid number.");
                        }
                    }
                    admin.readCategory(categoryID2);
                    break;
                case 7:
                    loggedIn = false;
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

}


interface CRUDOperations{
    void createUser();
    void readUser(int id);
    void updateUser(int id);
    void deleteUser(int id);

    void createProduct();
    void readProduct(int id);
    void updateProduct(int id);
    void deleteProduct(int id);

    void createCategory();
    void readCategory(int id);
    void updateCategory(int id);
    void deleteCategory(int id);
}

abstract class User{
    Scanner scanner = new Scanner(System.in);

    protected String username;
    protected String password;
    protected Date dateOfBirth;
    protected int id = (int) (Math.random()*1000);


    User(){}
    User(String username, String password, Date dateOfBirth){
        this.username = username;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
    }

    public int getID() {
        return id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername(){
        return username;
    }

    public void setPassword(String newPassword) {
        this.password = newPassword;
    }


    public String getPassword() {
        return password;
    }

    public void setDateOfBirth(Date newDateOfBirth) {
        this.dateOfBirth = newDateOfBirth;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public abstract void viewProfile();
}


class Customer extends User{
    Scanner scanner = new Scanner(System.in);

    public enum Gender{ MALE , FEMALE }

    private double balance;
    private String address;
    private Gender gender;
    private boolean premium;
    private Cart cart;
    private final ArrayList<Product> wishlist = new ArrayList<>();

    Customer(){}
    Customer(String username, String password, Date dateOfBirth, String address, Gender gender, boolean premium) {
        super(username, password, dateOfBirth);
        this.address = address;
        this.gender = gender;
        this.premium = premium;
        this.cart = new Cart();
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public String getAddress() {
        return address;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Gender getGender() {
        return gender;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }
    public boolean isPremium() {
        return premium;
    }

    public Cart getCart() {
        return cart;
    }

    public ArrayList<Product> getWishlist() {
        return new ArrayList<>(wishlist);
    }
    public void addToWishlist(Product product) {
            if (wishlist.contains(product)) {
                System.out.println(product.getName() + " is already in your wishlist.");
            } else {
                wishlist.add(product);
                System.out.println(product.getName() + " added to your wishlist.");
        }
    }
    public void removeFromWishlist(Product product) {
        wishlist.remove(product);
    }

    public void addFunds(double amountToAdd) {
        this.balance += amountToAdd;
    }

    public void removeFunds(double amountToRemove) {
        this.balance -= amountToRemove;
    }

    public void applyCartDiscount() {
        if (cart != null) {
            if (premium) {
                double premiumDiscountRate = 10.0;
                cart.applyPremiumDiscount(premiumDiscountRate);
            } else {
                System.out.println("No premium discount applied. This customer is not a premium member.");
            }
        } else {
            System.out.println("Cart is not initialized.");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Customer other = (Customer) obj;
        return username.equals(other.username) &&
                dateOfBirth.equals(other.dateOfBirth) &&
                balance == other.balance &&
                address.equals(other.address) &&
                gender == other.gender &&
                premium == other.premium;
    }


    @Override
    public String toString(){
        return "Username : " +username +
                "\nDate of birth: " + dateOfBirth+
                "\nBalance: " + balance +
                "\nAddress: " + address +
                "\nGender: " + gender +
                "\nMembership: " + premium + "\n";

    }
    @Override
    public void viewProfile(){
        System.out.println("===== Customer Profile =====");
        System.out.println(this.toString());
        System.out.println("============================");
    }
}

class Admin extends User implements CRUDOperations{
    Scanner scanner = new Scanner(System.in);

    private String role;
    private int workingHours;

    Admin(){}
    Admin(String username, String password, Date dateOfBirth, String role, int workingHours){
        super(username, password, dateOfBirth);
        this.role=role;
        this.workingHours=workingHours;
    }

    public void setRole(String newRole) {
        this.role = newRole;
    }

    public String getRole() {
        return role.toLowerCase();
    }

    public void setWorkingHours(int newWorkingHours) {
        this.workingHours = newWorkingHours;
    }

    public int getWorkingHours() {
        return workingHours;
    }


    public void showAllUsers(){
        ArrayList<User> users = Database.getUserList();
        if(users.isEmpty()){
            System.out.println("No users available");
        }
        else{
            System.out.println("===== List of Users =====");
            for (User user : users) {
                user.viewProfile();
                System.out.println("------------------------");
            }
        }
    }

    public void showAllProducts(){
        ArrayList<Product> products = Database.getProductList();
        if(products.isEmpty()){
            System.out.println("No products available");
        }
        else {
            System.out.println("===== List of Products =====");
            for (Product product : products) {
                product.viewProduct();
                System.out.println("------------------------");
            }
        }
    }

    public void showAllCategories(){
        ArrayList<Category> categories = Database.getCategoryList();
        if(categories.isEmpty()){
            System.out.println("No categories available");
        }else{
            System.out.println("===== List of Categories =====");
            for (Category category : categories) {
                category.viewCategory();
                System.out.println("------------------------");
            }
        }
    }

    public void showAllOrders(){
        ArrayList<Order> orders = Database.getOrderList();
        if(orders.isEmpty()){
            System.out.println("No products available");
        } else{
            System.out.println("===== List of Orders =====");
            for (Order order : orders) {
                order.showOrder();
                System.out.println("------------------------");
            }
        }
    }

    @Override
    public String toString(){
        return "Username: " + username +
                "\nDate Of Birth: " + dateOfBirth +
                "\nRole: " + role +
                "\nWorking Hours: " + workingHours + "\n";
    }

    @Override
    public void viewProfile(){
        System.out.println("===== Admin Profile =====");
        System.out.println(this.toString());
        System.out.println("=========================");
    }

    @Override
    public void createUser() {
        Scanner scanner = new Scanner(System.in);
        String userType = "";
        boolean validInput1 = false;

        while (!validInput1) {
            System.out.print("What type of user would you like to create? (customer/admin): ");
            userType = scanner.nextLine().trim().toLowerCase();

            if (userType.equals("customer") || userType.equals("admin")) {
                validInput1 = true;
            } else {
                System.out.println("Invalid input. Please enter 'customer' or 'admin'.");
            }
        }

        switch (userType) {
            case "customer":
                // Gather customer details
                System.out.println("Creating a new Customer...");
                System.out.print("Enter username: ");
                String username = scanner.nextLine();
                while (Database.findUserByUsername(username) != null || username.isEmpty()) {
                    System.out.println("Invalid username or username already exists. Please try again.");
                    System.out.print("Enter username: ");
                    username = scanner.nextLine();
                }

                System.out.print("Enter password: ");
                String password = scanner.nextLine();
                while (password.length() < 6 || !password.matches(".*[A-Za-z].*") || !password.matches(".*[0-9].*")) {
                    System.out.println("Invalid password. Must be at least 6 characters long, and contain at least one letter and one number.");
                    System.out.print("Enter password: ");
                    password = scanner.nextLine();
                }

                Date dateOfBirth = null;
                boolean validDate = false;
                String dobInput = "";
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                dateFormat.setLenient(false); // Ensure strict parsing

                while (!validDate) {
                    System.out.print("Enter date of birth (yyyy-MM-dd): ");
                    dobInput = scanner.nextLine();

                    if (dobInput.isEmpty()) {
                        System.out.println("Date of birth cannot be empty. Please try again.");
                    } else {
                        try {
                            dateOfBirth = dateFormat.parse(dobInput); // Parse the input

                            // Check if the date is in the future
                            if (dateOfBirth.after(new Date())) {
                                System.out.println("Date of birth cannot be in the future. Please try again.");
                            } else {
                                validDate = true; // Valid date
                            }
                        } catch (ParseException e) {
                            System.out.println("Invalid date format. Please enter the date in yyyy-MM-dd format.");
                        }
                    }
                }

                System.out.print("Enter address: ");
                String address = scanner.nextLine();
                while (address.isEmpty()) {
                    System.out.println("Address cannot be empty. Please try again.");
                    System.out.print("Enter address: ");
                    address = scanner.nextLine();
                }

                System.out.print("Enter gender (male/female): ");
                Customer.Gender gender = null;
                while (gender == null) {
                    String genderInput = scanner.nextLine().toUpperCase();
                    if (genderInput.equals("MALE") || genderInput.equals("FEMALE")) {
                        gender = Customer.Gender.valueOf(genderInput);
                    } else {
                        System.out.println("Invalid gender. Please enter 'male' or 'female'.");
                    }
                }

                boolean premium = false; // Default value
                boolean validInput = false;

                while (!validInput) {
                    System.out.print("Is the customer a premium member? (true/false): ");
                    String input = scanner.nextLine().trim().toLowerCase();

                    if (input.equals("true")) {
                        premium = true;
                        validInput = true;
                    } else if (input.equals("false")) {
                        validInput = true;
                    } else {
                        System.out.println("Invalid input. Please enter 'true' or 'false'.");
                    }
                }

                Customer newCustomer = new Customer(username, password, dateOfBirth, address, gender, premium);
                Database.addUser(newCustomer);
                System.out.println("Account created successfully.");
                break;

            case "admin":
                // Gather admin details
                System.out.println("Creating a new Admin...");
                System.out.print("Enter username: ");
                username = scanner.nextLine();
                while (Database.findUserByUsername(username) != null || username.isEmpty()) {
                    System.out.println("Invalid username or username already exists. Please try again.");
                    System.out.print("Enter username: ");
                    username = scanner.nextLine();
                }

                System.out.print("Enter password: ");
                password = scanner.nextLine();
                while (password.length() < 6 || !password.matches(".*[A-Za-z].*") || !password.matches(".*[0-9].*")) {
                    System.out.println("Invalid password. Must be at least 6 characters long, and contain at least one letter and one number.");
                    System.out.print("Enter password: ");
                    password = scanner.nextLine();
                }

                Date dateOfBirth1 = null;
                boolean validDate1 = false;
                String dobInput1 = "";
                SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
                dateFormat1.setLenient(false); // Ensure strict parsing

                while (!validDate1) {
                    System.out.print("Enter date of birth (yyyy-MM-dd): ");
                    dobInput1 = scanner.nextLine();

                    if (dobInput1.isEmpty()) {
                        System.out.println("Date of birth cannot be empty. Please try again.");
                    } else {
                        try {
                            dateOfBirth1 = dateFormat1.parse(dobInput1); // Parse the input

                            // Check if the date is in the future
                            if (dateOfBirth1.after(new Date())) {
                                System.out.println("Date of birth cannot be in the future. Please try again.");
                            } else {
                                validDate1 = true; // Valid date
                            }
                        } catch (ParseException e) {
                            System.out.println("Invalid date format. Please enter the date in yyyy-MM-dd format.");
                        }
                    }
                }

                boolean validRole = false;
                String newRole = "";

                while (!validRole) {
                    System.out.print("Enter new role: ");
                    newRole = scanner.nextLine();

                    // Regular expression to check if the role contains only letters and spaces
                    // Ensures that each word starts with a letter and spaces are allowed between words
                    if (newRole.matches("[a-zA-Z]+( [a-zA-Z]+)*")) {
                        validRole = true;   // Exit loop on successful update
                    } else {
                        System.out.println("Invalid role. Please enter a valid role (letters and spaces only, no special characters).");
                    }
                }

                System.out.print("Enter working hours: ");
                int workingHours = -1;
                while (workingHours < 0) {
                    try {
                        workingHours = Integer.parseInt(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a valid number for working hours.");
                    }
                }

                Admin newAdmin = new Admin(username, password, dateOfBirth1, newRole, workingHours);
                Database.addUser(newAdmin);
                System.out.println("Admin created successfully.");
                break;

            default:
                System.out.println("Invalid user type. Please enter 'customer' or 'admin'.");
                break;
        }
    }


    @Override
    public void readUser(int id) {
        User user = Database.findUserByID(id);

        if (user == null) {
            System.out.println("User with ID " + id + " not found.");
            return;
        }

        System.out.println(user.toString());

        // Retrieve and display orders made by this user
        ArrayList<Order> userOrders = Database.getOrderList();
        boolean hasOrders = false;

        System.out.println("\n===== User Orders =====");
        for (Order order : userOrders) {
            if (order.getCustomer().getID() == id) {
                System.out.println(order.toString());
                hasOrders = true;
            }
        }

        if (!hasOrders) {
            System.out.println("No orders found for this user.");
        }
    }


    @Override
    public void updateUser(int id) {
        User user = Database.findUserByID(id);
        if (user == null) {
            System.out.println("User with ID " + id + " not found.");
            return;
        }

        System.out.println("Updating user: " + user.getUsername());

        // Update Username
        String newUsername = "";
        System.out.print("Enter new username (or press Enter to skip): ");
        newUsername = scanner.nextLine();
        if (!newUsername.trim().isEmpty()) {
            // Check if the username already exists
            User existingUser = Database.findUserByUsername(newUsername);
            if (existingUser != null) {
                System.out.println("Username '" + newUsername + "' already exists. Please choose a different username.");
            } else {
                user.setUsername(newUsername);
                System.out.println("Username updated successfully.");
            }
        }

        // Update Password
        String newPassword = "";
        System.out.print("Enter new password (or press Enter to skip): ");
        newPassword = scanner.nextLine();

        if (!newPassword.trim().isEmpty()) {
            boolean validPassword = false;

            while (!validPassword) {
                System.out.print("Enter new password: ");
                newPassword = scanner.nextLine();

                if (newPassword.isEmpty()) {
                    System.out.println("Password cannot be empty.");
                } else if (newPassword.length() < 6) {
                    System.out.println("Password must be at least 6 characters long.");
                } else if (!newPassword.matches(".*[A-Za-z].*")) {
                    System.out.println("Password must contain at least one letter.");
                } else if (!newPassword.matches(".*[0-9].*")) {
                    System.out.println("Password must contain at least one number.");
                } else {
                    user.setPassword(newPassword);
                    System.out.println("Password updated successfully.");
                    validPassword = true;
                }
            }
        } else {
            System.out.println("Password change skipped.");
        }

        // Update Date of Birth
        Date newDateOfBirth = null;
        boolean validDateOfBirth = false;
        String dobInput = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false); // Ensure strict parsing

        while (!validDateOfBirth) {
            System.out.print("Enter new date of birth (yyyy-MM-dd) (or press Enter to skip): ");
            dobInput = scanner.nextLine();

            if (dobInput.trim().isEmpty()) {
                validDateOfBirth = true; // Skip if input is empty
            } else {
                try {
                    newDateOfBirth = dateFormat.parse(dobInput); // Parse the input

                    // Check if the date is in the future
                    if (newDateOfBirth.after(new Date())) {
                        System.out.println("Date of birth cannot be in the future. Please try again.");
                    } else {
                        user.setDateOfBirth(newDateOfBirth); // Set the new date of birth
                        validDateOfBirth = true; // Valid date
                    }
                } catch (ParseException e) {
                    System.out.println("Invalid date format. Please enter the date in yyyy-MM-dd format.");
                }
            }
        }



        // Check if user is an Admin
        if (user instanceof Admin) {
            Admin admin = (Admin) user;

            // Update Role
            boolean validRole = false;

            while (!validRole) {
                System.out.print("Enter new role: ");
                String newRole = scanner.nextLine();

                // Regular expression to check if the role contains only letters and spaces
                // Ensures that each word starts with a letter and spaces are allowed between words
                if (newRole.matches("[a-zA-Z]+( [a-zA-Z]+)*")) {
                    admin.setRole(newRole);
                    System.out.println("Role updated successfully.");
                    validRole = true;   // Exit loop on successful update
                } else {
                    System.out.println("Invalid role. Please enter a valid role (letters and spaces only, no special characters).");
                }
            }

            // Update Working Hours
            int newWorkingHours = 0;
            boolean validWorkingHours = false;

            while (!validWorkingHours) {
                System.out.print("Enter new working hours (or press Enter to skip): ");
                String workingHoursInput = scanner.nextLine();

                if (workingHoursInput.trim().isEmpty()) {
                    validWorkingHours = true;
                } else {
                    try {
                        newWorkingHours = Integer.parseInt(workingHoursInput);
                        admin.setWorkingHours(newWorkingHours);
                        validWorkingHours = true;
                    } catch (NumberFormatException  e) {
                        System.out.println("Invalid input for working hours. Please enter a valid number.");
                    }
                }
            }

        } else if (user instanceof Customer) {
            Customer customer = (Customer) user;

            // Update Address
            String newAddress = "";
            System.out.print("Enter new address (or press Enter to skip): ");
            newAddress = scanner.nextLine();
            if (!newAddress.trim().isEmpty()) {
                customer.setAddress(newAddress);
            }

            // Update Gender
            Customer.Gender newGender = null;
            boolean validGender = false;

            do {
                System.out.print("Enter new gender (male/female) (or press Enter to skip): ");
                String genderInput = scanner.nextLine().toLowerCase();

                if (genderInput.trim().isEmpty()) {
                    validGender = true;
                } else if (genderInput.equals("male") || genderInput.equals("female")) {
                    newGender = genderInput.equals("male") ? Customer.Gender.MALE : Customer.Gender.FEMALE;
                    customer.setGender(newGender);
                    validGender = true;
                } else {
                    System.out.println("Invalid gender input. Please enter 'male' or 'female'.");
                }
            } while (!validGender);

            // Update Premium Status
            boolean newPremium = false;
            boolean validPremium = false;

            do {
                System.out.print("Will the customer become a premium member? (true/false) (or press Enter to skip): ");
                String premiumInput = scanner.nextLine().trim();

                if (premiumInput.isEmpty()) {
                    validPremium = true;
                } else if (premiumInput.equalsIgnoreCase("true")) {
                    newPremium = true;
                    customer.setPremium(newPremium);
                    validPremium = true;
                } else if (premiumInput.equalsIgnoreCase("false")) {
                    customer.setPremium(newPremium);
                    validPremium = true;
                } else {
                    System.out.println("Invalid input for premium status. Please enter 'true' or 'false'.");
                }
            } while (!validPremium);
        }

        System.out.println("User updated successfully.");
    }


    @Override
    public void deleteUser(int id) {
        User user = Database.findUserByID(id);
        if (user != null) {
            Database.removeUser(user);
            System.out.println("User with ID: " + id + " and username: " + user.getUsername() + " removed successfully.");
        } else {
            System.out.println("User with ID: " + id + " not found.");
        }
    }


    @Override
    public void createProduct() {

        // Product Name Input
        String name = "";
        boolean validName = false;
        while (!validName) {
            System.out.println("Enter Product Name: ");
            name = scanner.nextLine();

            if (name == null || name.trim().isEmpty()) {
                System.out.println("Product name cannot be empty.");
            } else {
                // Check if product with the same name already exists
                Product existingProduct = Database.findProductByName(name);
                if (existingProduct != null) {
                    System.out.println("Product with the name '" + name + "' already exists. Please choose a different name.");
                } else {
                    validName = true;
                }
            }
        }

        // Product Price Input
        double price = 0.0;
        boolean validPrice = false;

        System.out.println("Enter Product Price: ");
        while (!validPrice) {
            String priceInput = scanner.nextLine();
            try {
                // Try to parse the input to an integer
                price = Double.parseDouble(priceInput);
                if (price < 0) {
                    System.out.println("Price cannot be negative. Please enter a valid price.");
                } else {
                    validPrice = true;  // Valid price, proceed
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid price.");
            }
        }


        // Stock Quantity Input
        int stock = 0;
        boolean validQuantity = false;

        System.out.println("Enter Stock Quantity: ");
        while (!validQuantity) {
            String stockInput = scanner.nextLine();

            if (stockInput.trim().isEmpty()) {
                System.out.println("Stock quantity cannot be empty. Please enter a valid stock quantity.");
            } else {
                try {
                    // Try to parse the input to an integer
                    stock = Integer.parseInt(stockInput);

                    // Validate if the stock quantity is non-negative
                    if (stock < 0) {
                        System.out.println("Stock quantity cannot be negative. Please enter a valid stock quantity.");
                    } else {
                        validQuantity = true;  // Valid stock quantity, proceed
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid stock quantity.");
                }
            }
        }

        // Product Rating Input
        double rating = 0.0;
        boolean validRating = false;

        while (!validRating) {
            System.out.println("Enter Product Rating (0-10): ");
            String ratingInput = scanner.nextLine();

            // Check if the input is empty
            if (ratingInput.trim().isEmpty()) {
                System.out.println("Rating cannot be empty. Please enter a valid rating.");
            } else {
                try {
                    // Try to parse the rating input
                    rating = Double.parseDouble(ratingInput);

                    // Validate the rating range
                    if (rating < 0 || rating > 10) {
                        System.out.println("Rating must be between 0 and 10.");
                    } else {
                        validRating = true;  // Valid rating, proceed
                    }
                } catch (NumberFormatException e) {
                    // Handle invalid number format
                    System.out.println("Invalid input. Please enter a valid number for the rating.");
                }
            }
        }

        // Create the new Product and add to Database
        Product newProduct = new Product(name, price, stock, rating);
        Database.addProduct(newProduct);
        System.out.println("Product added successfully!");

        System.out.println("Would you like to add this product to a category? (yes/no)");
        Scanner scanner = new Scanner(System.in);
        String choice = scanner.nextLine().trim();

        while (!choice.equalsIgnoreCase("yes") && !choice.equalsIgnoreCase("no")) {
            System.out.println("Invalid input. Please enter 'yes' or 'no'.");
            System.out.println("Would you like to add this product to a category? (yes/no)");
            choice = scanner.nextLine().trim();
        }

        if (choice.equalsIgnoreCase("yes")) {
            boolean validCategory = false;
            while (!validCategory) {
                try {
                    System.out.println("Enter category number: ");
                    int categoryNum = scanner.nextInt();
                    scanner.nextLine(); // Consume newline character

                    // Attempt to retrieve the category
                    Category category = Database.getCategoryByNumber(categoryNum);

                    if (category != null) {
                        category.addProduct(newProduct);
                        System.out.println("Product added to category: " + category.getName());
                        validCategory = true; // Exit loop if valid category is found
                    } else {
                        System.out.println("Category not found. Please enter a valid category number.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a valid numeric category number.");
                    scanner.nextLine(); // Clear the invalid input
                }
            }
        } else {
            System.out.println("Product creation completed without assigning to a category.");
        }
    }



    @Override
    public void readProduct(int id) {
        Product product = Database.findProductByID(id);
        if (product != null) {
            System.out.println(product.toString());
        } else {
            System.out.println("Product with ID " + id + " not found");
        }
    }

    @Override
    public void updateProduct(int id) {
        Product product = Database.findProductByID(id);
        if (product == null) {
            System.out.println("Product with ID " + id + " not found");
            return;
        }

        System.out.println("Updating product: " + product.getName());

        // Update Product Name
        String newName = "";
        boolean validName = false;
        System.out.print("Enter new name (or press Enter to skip): ");
        newName = scanner.nextLine();

        if (newName.trim().isEmpty()) {
            validName = true; // Skip if the user presses Enter without input
        } else {
            // Check if the product name already exists
            Product existingProduct = Database.findProductByName(newName);
            if (existingProduct != null) {
                System.out.println("Product with the name '" + newName + "' already exists. Please choose a different name.");
            } else {
                validName = true; // Valid name input if the name is unique
            }
        }

        if (validName && !newName.trim().isEmpty()) {
            product.setName(newName);
            System.out.println("Product name updated successfully.");
        } else if (newName.trim().isEmpty()) {
            System.out.println("Product name change skipped.");
        }

        // Update Product Price
        double newPrice = 0.0;
        boolean validPrice = false;
        do {
            System.out.print("Enter new price (or press Enter to skip): ");
            String priceInput = scanner.nextLine();

            if (priceInput.trim().isEmpty()) {
                validPrice = true;  // Skip if empty input
            } else {
                try {
                    // Try parsing the input to a double
                    newPrice = Double.parseDouble(priceInput);

                    // Validate if the price is non-negative
                    if (newPrice < 0) {
                        System.out.println("Price cannot be negative. Please try again.");
                    } else {
                        validPrice = true; // Valid price input
                        product.setPrice(newPrice); // Assuming `product` is already defined
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid price entered. Please enter a valid number.");
                }
            }
        } while (!validPrice);



        // Update Stock Quantity
        int newStock = 0;
        boolean validStock = false;

        do {
            System.out.print("Enter new stock quantity (or press Enter to skip): ");
            String stockInput = scanner.nextLine();

            if (stockInput.trim().isEmpty()) {
                validStock = true; // Skip if empty input
            } else {
                try {
                    // Attempt to parse the stock input
                    newStock = Integer.parseInt(stockInput);

                    if (newStock < 0) {
                        System.out.println("Stock quantity cannot be negative. Please try again.");
                    } else {
                        validStock = true; // Valid stock input
                        product.setStockQuantity(newStock);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid integer for stock quantity.");
                }
            }
        } while (!validStock);


        // Update Product Rating
        double newRating = 0.0;
        boolean validRating = false;

        do {
            System.out.print("Enter new rating (or press Enter to skip): ");
            String ratingInput = scanner.nextLine();

            if (ratingInput.trim().isEmpty()) {
                validRating = true;  // Skip if empty input
            } else {
                try {
                    // Attempt to parse the input to a double
                    newRating = Double.parseDouble(ratingInput);

                    // Validate if the rating is within the valid range
                    if (newRating < 0 || newRating > 10) {
                        System.out.println("Rating must be between 0 and 10. Please try again.");
                    } else {
                        validRating = true; // Valid rating input
                        product.setRating(newRating); // Assuming `product` is already defined
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid rating entered. Please enter a valid number.");
                }
            }
        } while (!validRating);

        System.out.println("Product updated successfully.");
    }


    @Override
    public void deleteProduct(int id) {
        Product product = Database.findProductByID(id);
        if (product != null) {
            ArrayList<Category> categories = Database.getCategoryList();

            for (Category category : categories) {
                if (category.hasProduct(id)) {
                    // Remove the product from the category
                    category.removeProductFromCategory(product);
                    break;
                }
            }
            // Remove the product from the database
            Database.removeProduct(product);
            System.out.println("Product with ID " + id + " removed successfully.");
        } else {
            System.out.println("Product with ID " + id + " not found.");
        }
    }

    @Override
    public void createCategory() {
        // Validate Category Number input
        int num = 0;
        boolean validNum = false;
        do {
            System.out.print("Enter category number (positive integer): ");
            String numInput = scanner.nextLine();
            try {
                num = Integer.parseInt(numInput);

                // Check if category number already exists
                Category existingCategoryByNum = Database.getCategoryByNumber(num);
                if (existingCategoryByNum != null) {
                    System.out.println("Category number " + num + " already exists. Please choose a different number.");
                } else if (num <= 0) {
                    System.out.println("Category number must be a positive integer. Please try again.");
                } else {
                    validNum = true; // Valid number input
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        } while (!validNum);

        // Validate Category Name input
        String name = "";
        boolean validName = false;

        do {
            System.out.print("Enter category name: ");
            name = scanner.nextLine();

            // Check if category name already exists
            Category existingCategoryByName = Database.findCategoryByName(name);
            if (existingCategoryByName != null) {
                System.out.println("Category with the name '" + name + "' already exists. Please choose a different name.");
            } else if (name.isEmpty() || name.trim().isEmpty()) {
                System.out.println("Category name cannot be empty. Please try again.");
            } else if (!name.matches("[a-zA-Z]+")) { // Check if name contains only letters
                System.out.println("Category name can only contain letters. Please try again.");
            } else {
                validName = true;
            }
        } while (!validName);


        // Proceed with creating the new category
        Category newCategory = new Category(num, name);
        Database.addCategory(newCategory); // Assuming there's an addCategory method to store the new category
        System.out.println("Category created successfully.");

        String updateProductsChoice;
        do {
            System.out.println("Would you like to update products in this category? (yes/no)");
            updateProductsChoice = scanner.nextLine().trim().toLowerCase();

            if (!updateProductsChoice.equals("yes") && !updateProductsChoice.equals("no")) {
                System.out.println("Invalid input. Please enter 'yes' or 'no'.");
            }
        } while (!updateProductsChoice.equals("yes") && !updateProductsChoice.equals("no"));

        if (updateProductsChoice.equals("yes")) {
            boolean addMoreProducts = true;
            while (addMoreProducts) {
                // Add new products to category
                System.out.println("Enter product ID to add to this category (or 'exit' to stop): ");
                String productIDInput = scanner.nextLine();
                if (productIDInput.equalsIgnoreCase("exit")) {
                    addMoreProducts = false;
                } else {
                    try {
                        int productID = Integer.parseInt(productIDInput);
                        Product product = Database.findProductByID(productID);
                        if (product == null) {
                            System.out.println("Product with ID " + productID + " not found.");
                        } else {
                            newCategory.addProduct(product);
                            System.out.println("Product added successfully.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid product ID. Please enter a valid number.");
                    }
                }
            }
        }


    }



    @Override
    public void readCategory(int id){
        Category category = Database.getCategoryByNumber(id);
        if (category != null){
            System.out.println(category.toString());
        } else {
            System.out.println("Category with id: " + id + " not found");
        }
    }

    @Override
    public void updateCategory(int id) {
        // Find the category by ID
        Category category = Database.getCategoryByNumber(id);
        if (category == null) {
            System.out.println("Category with ID " + id + " not found");
            return;
        }

        System.out.println("Updating category: " + category.getName());

        // Update Category Name
        String newCategoryName = "";
        boolean validCategoryName = false;
        System.out.print("Enter new category name (or press Enter to skip): ");
        newCategoryName = scanner.nextLine();

        if (newCategoryName.trim().isEmpty()) {
            validCategoryName = true; // Skip if the user presses Enter without input
        } else {
            // Check if the category name already exists
            Category existingCategoryByName = Database.findCategoryByName(newCategoryName);
            if (existingCategoryByName != null) {
                System.out.println("Category with the name '" + newCategoryName + "' already exists. Please choose a different name.");
            } else {
                validCategoryName = true; // Valid name input
                category.setName(newCategoryName);
            }
        }

        // Update Category Number
        int newCategoryNum = 0;
        boolean validCategoryNum = false;
        do {
            System.out.print("Enter new category number (or press Enter to skip): ");
            String categoryNumInput = scanner.nextLine();
            if (categoryNumInput.trim().isEmpty()) {
                validCategoryNum = true;  // Skip if empty input
            } else {
                try {
                    newCategoryNum = Integer.parseInt(categoryNumInput);

                    // Check if the category number already exists
                    Category existingCategoryByNum = Database.getCategoryByNumber(newCategoryNum);
                    if (existingCategoryByNum != null) {
                        System.out.println("Category number " + newCategoryNum + " already exists. Please choose a different number.");
                    } else if (newCategoryNum <= 0) {
                        System.out.println("Category number must be positive. Please try again.");
                    } else {
                        validCategoryNum = true; // Valid category number input
                        category.setNum(newCategoryNum);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input for category number. Please enter a valid number.");
                }
            }
        } while (!validCategoryNum);

        // Update Products in the Category
        String updateProductsChoice;
        do {
            System.out.println("Would you like to update products in this category? (yes/no)");
            updateProductsChoice = scanner.nextLine().trim().toLowerCase();

            if (!updateProductsChoice.equals("yes") && !updateProductsChoice.equals("no")) {
                System.out.println("Invalid input. Please enter 'yes' or 'no'.");
            }
        } while (!updateProductsChoice.equals("yes") && !updateProductsChoice.equals("no"));

        if (updateProductsChoice.equals("yes")) {
            boolean addMoreProducts = true;
            while (addMoreProducts) {
                // Add new products to category
                System.out.println("Enter product ID to add to this category (or 'exit' to stop): ");
                String productIDInput = scanner.nextLine();
                if (productIDInput.equalsIgnoreCase("exit")) {
                    addMoreProducts = false;
                } else {
                    try {
                        int productID = Integer.parseInt(productIDInput);
                        Product product = Database.findProductByID(productID);
                        if (product == null) {
                            System.out.println("Product with ID " + productID + " not found.");
                        } else {
                            category.addProduct(product);
                            System.out.println("Product added successfully.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid product ID. Please enter a valid number.");
                    }
                }
            }
        }


        System.out.println("Category updated successfully.");
    }

    @Override
    public void deleteCategory(int id){
        Category category = Database.getCategoryByNumber(id);
        if(category != null){
            Database.removeCategory(category);
        } else{
            System.out.println("Category with id: " + id + " not found");
        }
    }
    public static void createCustomer() {
        Scanner scanner = new Scanner(System.in);

        // Validate username input
        String username = "";
        boolean validUsername = false;
        while (!validUsername) {
            System.out.print("Enter username: ");
            username = scanner.nextLine();
            if (username.isEmpty()) {
                System.out.println("Username cannot be empty. Please try again.");
            } else if (Database.findUserByUsername(username) != null) {
                System.out.println("User with the same username already exists. Please choose another username.");
            } else {
                validUsername = true;
            }
        }

        // Validate password input
        boolean validPassword = false;
        String password = "";

        while (!validPassword) {
            System.out.print("Enter new password: ");
            password = scanner.nextLine();

            // Password validation
            if (password.isEmpty()) {
                System.out.println("Password cannot be empty.");
            } else if (password.length() < 6) {
                System.out.println("Password must be at least 6 characters long.");
            } else if (!password.matches(".*[A-Za-z].*")) {
                System.out.println("Password must contain at least one letter.");
            } else if (!password.matches(".*[0-9].*")) {
                System.out.println("Password must contain at least one number.");
            } else {
                // If password is valid, break the loop
                System.out.println("Password is valid.");
                validPassword = true;
            }
        }

        Date dateOfBirth = null;
        boolean validDate = false;
        String dobInput = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false); // Ensure strict parsing

        while (!validDate) {
            System.out.print("Enter date of birth (yyyy-MM-dd): ");
            dobInput = scanner.nextLine();

            if (dobInput.isEmpty()) {
                System.out.println("Date of birth cannot be empty. Please try again.");
            } else {
                try {
                    dateOfBirth = dateFormat.parse(dobInput); // Parse the input

                    // Check if the date is in the future
                    if (dateOfBirth.after(new Date())) {
                        System.out.println("Date of birth cannot be in the future. Please try again.");
                    } else {
                        validDate = true; // Valid date
                    }
                } catch (ParseException e) {
                    System.out.println("Invalid date format. Please enter the date in yyyy-MM-dd format.");
                }
            }
        }


        // Validate address input
        String address = "";
        boolean validAddress = false;
        while (!validAddress) {
            System.out.print("Enter address: ");
            address = scanner.nextLine();
            if (address.isEmpty()) {
                System.out.println("Address cannot be empty. Please try again.");
            } else {
                validAddress = true;
            }
        }

        // Validate gender input
        Customer.Gender gender = null;
        boolean validGender = false;
        while (!validGender) {
            System.out.print("Enter gender (male/female): ");
            String genderInput = scanner.nextLine().toUpperCase(); // Convert to uppercase for enum matching
            if (genderInput.isEmpty()) {
                System.out.println("Gender cannot be empty. Please try again.");
            } else if (genderInput.equals("MALE") || genderInput.equals("FEMALE")) {
                gender = Customer.Gender.valueOf(genderInput);
                validGender = true;
            } else {
                System.out.println("Invalid input for gender. Please try again.");
            }
        }

        // Validate premium status
        boolean premium = false;
        boolean validPremium = false;
        while (!validPremium) {
            System.out.print("Is the customer a premium member? (true/false): ");
            String premiumInput = scanner.nextLine();
            if (premiumInput.equalsIgnoreCase("true") || premiumInput.equalsIgnoreCase("false")) {
                premium = Boolean.parseBoolean(premiumInput);
                validPremium = true;
            } else {
                System.out.println("Invalid input for premium status. Please enter 'true' or 'false'.");
            }
        }

        // Create and add the customer to the database
        Customer newCustomer = new Customer(username, password, dateOfBirth, address, gender, premium);
        Database.addUser(newCustomer);

        System.out.println("Set balance");
        double balance = -1;
        boolean validBalance = false;

        while (!validBalance) {
            try {
                balance = scanner.nextDouble();
                // Check if the balance is within the valid range
                if (balance < 0) {
                    System.out.println("Balance cannot be negative. Please enter a valid balance.");
                } else if (balance > 5000) {
                    System.out.println("Balance cannot exceed 5000. Please enter a valid balance.");
                } else {
                    newCustomer.setBalance(balance);
                    validBalance = true;
                }
            } catch (InputMismatchException e) {
                // Handle invalid input (not a double)
                System.out.println("Invalid input. Please enter a valid number for the balance.");
                scanner.next(); // Clear the invalid input
            }
        }

        System.out.println("Customer created successfully.");
    }
}

class Product{
    Scanner scanner = new Scanner(System.in);

    private int ID;
    private String name;
    private double price;
    private int stockQuantity;
    private double rating;
    private int reservedQuantity;

    Product() {
    }

    Product(String name, double price, int stockQuantity, double rating) {
        this.ID = (int) (Math.random()*1000);
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.rating = rating;
    }

    public int getID() {
        return ID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getRating() {
        return rating;
    }

    public int getReservedQuantity() {
        return reservedQuantity;
    }

    public void setReservedQuantity(int reservedQuantity) {
        this.reservedQuantity = reservedQuantity;
    }

    public int getAvailableStock() {
        return stockQuantity - reservedQuantity;
    }

    public void viewProduct() {
        System.out.println("===== Product Info =====");
        System.out.println(this.toString());
        System.out.println("============================");}

    @Override
    public String toString() {
        return "ID: " + ID +
                "\nName: " + name +
                "\nPrice: " + price +
                "\nStock Quantity: " + stockQuantity +
                "\nRating: " + rating + "\n";
    }

}

class Category{
    Scanner scanner = new Scanner(System.in);
    private final ArrayList<Product> list = new ArrayList<>();
    private int num;
    private String name;

    Category(){}
    Category(int num, String name){
        this.num = num;
        this.name = name;
    }

    public void setNum(int num) {
        this.num = num;
    }
    public int getNum() {
        return num;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Product> getAllProducts(){
        return new ArrayList<>(list);
    }

    public void addProduct(Product p) {
        list.add(p);
    }

    public void removeProductFromCategory(Product p) {
        list.remove(p);
    }

    public void viewCategory(){
        System.out.println("===== Category =====");
        System.out.println(this.toString());
        System.out.println("====================");}


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Category: ").append(name)
                .append("\nNumber: ").append(num).append("\n")
                .append("Total Products in Stock: ").append(getProductCount()).append("\n");
                System.out.println();
        sb.append("\nProducts: \n");

        if (list.isEmpty()) {
            sb.append("No products available.");
        } else {
            for (Product product : list) {
                sb.append(product.toString()).append("\n");
            }
        }
        return sb.toString();
    }

    public int getProductCount(){
        int sum = 0;
        for (Product product : list) {
            if (product.getStockQuantity() < 0) {
                System.out.println("Stock quantity cannot be negative for product: " + product.getName());
                return sum;
            }
            sum += product.getStockQuantity();
        }
        return sum;
    }

    public boolean hasProduct(int id){
        if (id <= 0) {
            System.out.println("Invalid Product ID.");
            return false;
        }
        for (Product product : list) {
            if (product.getID() == id) {
                return true;
            }
        }
        return false;
    }
}

class Cart {
    private final ArrayList<Product> items = new ArrayList<>();
    private double totalCost;
    private double discount;

    Cart() {
    }

    public void applyPremiumDiscount(double premiumDiscountRate) {
        this.discount = premiumDiscountRate;
        System.out.println("A premium discount of " + premiumDiscountRate + "% has been applied to the cart.");
    }

    public void addItem(Product product) {
        items.add(product); // Add the product to the cart
        totalCost += product.getPrice();
        product.setReservedQuantity(product.getReservedQuantity() + 1);
    }

    public void removeItem(Product product) {
        items.remove(product); // Remove the product from the cart
        totalCost -= product.getPrice();
        product.setReservedQuantity(product.getReservedQuantity() - 1);
    }


    public ArrayList<Product> getItems() {
        return new ArrayList<>(items);
    }

    public void showCart() {
        System.out.println("Your cart contains the following items:");
        for (Product product : this.getItems()) {
            System.out.println("Product ID: " + product.getID() + " - " + product.getName() + " - " + product.getPrice() + " LE");
        }
    }

    @Override
    public String toString() {
        StringBuilder cartDetails = new StringBuilder();

        // Add basic cart information
        cartDetails.append("Cart Details:\n");
        cartDetails.append("Total Cost (before discount): $").append(totalCost).append("\n");
        cartDetails.append("Discount applied: ").append(discount).append("%\n");

        // Calculate and display the final cost after applying the discount
        double finalCost = totalCost - (totalCost * (discount / 100));
        cartDetails.append("Final Cost (after discount): $").append(String.format("%.2f", finalCost)).append("\n");

        // Display the item count
        cartDetails.append("Total Items in Cart: ").append(getItemCount()).append("\n");

        // Display all items in the cart
        if (items.isEmpty()) {
            cartDetails.append("Your cart is empty.\n");
        } else {
            cartDetails.append("Items in your cart:\n");
            for (Product product : items) {
                cartDetails.append("- ").append(product.getName())
                        .append(" ($").append(product.getPrice())
                        .append(")\n");
            }
        }

        return cartDetails.toString();
    }

    public int getItemCount() {
        return items.size();
    }

    public double getFinalCost() {
        return totalCost - (totalCost * (discount / 100));
    }

    public void clearCart() {
        items.clear();
        totalCost = 0;
        System.out.println("The cart has been cleared.");
    }
}


class Order{

    public enum OrderStatus { PENDING, COMPLETED }

    private final int orderID = (int) (Math.random()*1000);
    private OrderStatus status;
    private final Customer customer;
    private final Cart cart;
    private PaymentMethod paymentMethod;
    private final Date orderDate;

    public Order(Customer customer, PaymentMethod paymentMethod) {
        this.customer = customer;
        this.cart = customer.getCart();
        this.paymentMethod = paymentMethod;
        this.status = OrderStatus.PENDING;
        this.orderDate = new Date();
    }

    public int getOrderID() {
        return orderID;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(String status) {
        try {
            this.status = OrderStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid status: " + status);
        }
    }

    public Customer getCustomer() {
        return customer;
    }

    public Cart getCart() {
        return cart;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void placeOrder() {
        if (paymentMethod.validatePaymentMethod()) {
            for(Product product : customer.getCart().getItems()){
                product.setStockQuantity(product.getStockQuantity() -1);
            }

            customer.applyCartDiscount();
            double totalCost = cart.getFinalCost();

            if (paymentMethod instanceof ApplicationPayment) {
                if (customer.getBalance() >= totalCost) {
                    paymentMethod.processPayment(totalCost);
                    status = OrderStatus.COMPLETED;
                    System.out.println("Order placed successfully for customer: " + customer.getUsername());
                } else {
                    System.out.println("Insufficient balance. Order cannot be placed.");
                }
            }

            else if (paymentMethod instanceof CreditCardPayment) {
                paymentMethod.processPayment(totalCost);
                status = OrderStatus.COMPLETED;
                System.out.println("Order placed successfully for customer: " + customer.getUsername());
            }
        } else {
            System.out.println("Payment validation failed.");
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // Order details
        sb.append("Order ID: ").append(orderID)
                .append("\nCustomer: ").append(customer.getUsername())
                .append("\nStatus: ").append(status)
                .append("\nOrder Date: ").append(orderDate)
                .append("\nTotal Cost: $").append(cart.getFinalCost())
                .append("\n\nItems in the Cart:\n");

        // Displaying items in the cart
        for (Product product : cart.getItems()) {
            sb.append("- ").append(product.getName())
                    .append(" ($").append(product.getPrice())
                    .append(")\n");
        }

        return sb.toString();
    }


    public void showOrder() {
        System.out.println(this.toString());
    }
}


interface PaymentMethod{
    public void processPayment(double amount);
    public boolean validatePaymentMethod();
}

class CreditCardPayment implements PaymentMethod {
    private final String cardNumber;
    private final String cardHolderName;
    private final String expiryDate;
    private final String cvv;
    private final Customer customer; // Link to the customer

    public CreditCardPayment(Customer customer, String cardNumber, String expiryDate, String cvv) {
        this.customer = customer;
        this.cardNumber = cardNumber;
        this.cardHolderName = customer.getUsername();
        this.expiryDate = expiryDate;
        this.cvv = cvv;
    }

    @Override
    public void processPayment(double amount) {
        if (validatePaymentMethod()) {
            System.out.println("Processing payment of $" + amount + " with credit card for customer: " + customer.getUsername());
        } else {
            System.out.println("Invalid card details. Payment failed for customer: " + customer.getUsername());
        }
    }

    @Override
    public boolean validatePaymentMethod() {
        // Validate card details are not null or empty
        if (cardNumber == null || cardNumber.isEmpty() ||
                expiryDate == null || expiryDate.isEmpty() ||
                cvv == null || cvv.isEmpty()) {
            return false;
        }

        if (!cardHolderName.equals(customer.getUsername())) {
            System.out.println("Validation failed: Card holder's name does not match the customer's username.");
            return false;
        }
        return true;
    }
}

class ApplicationPayment implements PaymentMethod {
    private final Customer customer;

    public ApplicationPayment(Customer customer) {
        this.customer = customer;
    }

    @Override
    public void processPayment(double amount) {
        if (validatePaymentMethod()) {
            double availableCash = customer.getBalance();
            if (availableCash >= amount) {
                customer.removeFunds(amount);
                System.out.println("Processing cash payment of $" + amount + " for customer: " + customer.getUsername());
                System.out.println("Remaining balance: $" + customer.getBalance());
            } else {
                System.out.println("Insufficient cash for payment. Payment failed for customer: " + customer.getUsername());
            }
        } else {
            System.out.println("Payment validation failed for customer: " + customer.getUsername());
        }
    }

    @Override
    public boolean validatePaymentMethod() {
        return customer.getBalance() > 0;
    }
}

class Database{
    private static final ArrayList<User> UserList = new ArrayList<>();
    private static final ArrayList<Product> ProductList = new ArrayList<>();
    private static final ArrayList<Order> OrderList = new ArrayList<>();
    private static final ArrayList<Category> CategoryList = new ArrayList<>();
    private static User lastAddedUser;


    public static ArrayList<User> getUserList() {
        return new ArrayList<>(UserList);
    }

    public static ArrayList<Product> getProductList() {
        return new ArrayList<>(ProductList);
    }

    public static ArrayList<Order> getOrderList() {
        return new ArrayList<>(OrderList);
    }

    public static ArrayList<Category> getCategoryList() {
        return new ArrayList<>(CategoryList);
    }

    public static User getLastAddedUser() {
        return lastAddedUser;
    }

    public static void addUser(User user) {
        UserList.add(user);
        lastAddedUser = user;
    }

    public static void removeUser(User user) {
        UserList.remove(user);
    }

    public static void addProduct(Product product) {
        ProductList.add(product);
    }

    public static void removeProduct(Product product) {
        ProductList.remove(product);

        for(Category  category : CategoryList){
            for(Product product1 : category.getAllProducts()){
                if(product1.getID() == product.getID()){
                    category.removeProductFromCategory(product1);
                }
            }
        }

    }

    public static void addOrder(Order order) {
        OrderList.add(order);
    }

    public static void removeOrder(Order order) {
        OrderList.remove(order);
    }

    public static void addCategory(Category category) {
        CategoryList.add(category);
    }

    public static void removeCategory(Category category) {
        CategoryList.remove(category);
    }

    public static User findUserByUsername(String username) {
        for (User user : UserList) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return user;
            }
        }
        return null;
    }

    public static Product findProductByID(int ID) {
        for (Product product : ProductList) {
            if (product.getID() == ID) {
                return product;
            }
        }
        return null;
    }


    public static Product findProductByName(String name) {
        for (Product product : ProductList) {
            if (product.getName().equalsIgnoreCase(name)) {
                return product;
            }
        }
        return null;
    }

    public static boolean isProductInStock(int ID) {
        Product product = findProductByID(ID);
        return product != null && product.getAvailableStock() > 0; // Use available stock
    }

    public static Order findOrderByID(int ID) {
        for (Order order : OrderList) {
            if (order.getOrderID() == ID) {
                return order;
            }
        }
        return null;
    }

    public static User findUserByID(int ID) {
        for (User user : UserList) {
            if (user.getID() == ID) {
                return user;
            }
        }
        return null;
    }

    public static Category getCategoryByNumber(int num) {
        for (Category category : CategoryList) {
            if (category.getNum() == num) {
                return category;
            }
        }
        return null;
    }

    public static ArrayList<Order> findOrdersByStatus(String status) {
        Order.OrderStatus orderStatus;
        orderStatus = Order.OrderStatus.valueOf(status.toUpperCase());
        ArrayList<Order> result = new ArrayList<>();
        for (Order order : OrderList) {
            if (order.getStatus() == orderStatus) {
                result.add(order);
            }
        }
        return result;
    }

    public static Category findCategoryByName(String name) {
        for (Category category : CategoryList) {
            if (category.getName().equalsIgnoreCase(name)) {
                return category;
            }
        }
        return null;
    }

    public static void updateUser(User user) {
        ArrayList<User> userList = getUserList();
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getID() == user.getID()) {
                userList.set(i, user);
                break;
            }
        }
    }

    public static void updateProduct(Product product) {
        ArrayList<Product> productList = getProductList();

        for (int i = 0; i < productList.size(); i++) {
            if (productList.get(i).getID() == product.getID()) {
                productList.set(i, product);
                System.out.println("Product updated: " + product.getName());
                break;
            }
        }
    }

    public static void updateCategory(Category category) {

        for (int i = 0; i < CategoryList.size(); i++) {
            if (CategoryList.get(i).getNum() == category.getNum()) {
                CategoryList.set(i, category);
                System.out.println("Category updated: " + category.getName());
                break;
            }
        }
    }



}
