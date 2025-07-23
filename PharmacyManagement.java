import java.util.*;

class Medication {
    private String name;
    private String id;
    private double price;
    private int stock;
    private double rating;

    public Medication(String name, String id, double price, int stock) {
        this.name = name;
        this.id = id;
        this.price = price;
        this.stock = stock;
        this.rating = -1;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public void updateStock(int amount) {
        this.stock += amount;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Medication{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", rating=" + (rating == -1 ? "No rating yet" : rating) +
                '}';
    }
}

class Order {
    private Medication medication;
    private int quantity;
    private double totalCost;
    private String address;
    private boolean isCanceled;

    public Order(Medication medication, int quantity) {
        this.medication = medication;
        this.quantity = quantity;
        this.totalCost = medication.getPrice() * quantity;
        this.address = "";
        this.isCanceled = false;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isCanceled() {
        return isCanceled;
    }

    public void cancel() {
        this.isCanceled = true;
        medication.updateStock(quantity);
    }

    @Override
    public String toString() {
        return "Order{" +
                "medication=" + medication.getName() +
                ", quantity=" + quantity +
                ", totalCost=$" + totalCost +
                ", deliveryAddress='" + address + '\'' +
                ", canceled=" + isCanceled +
                '}';
    }
}

class PharmacyManagementSystem {
    private List<Medication> medications = new ArrayList<>();
    private List<Order> orders = new ArrayList<>();

    public PharmacyManagementSystem() {
        medications.add(new Medication("Paracetamol", "M101", 25.0, 100));
        medications.add(new Medication("Ibuprofen", "M102", 35.0, 80));
        medications.add(new Medication("Cough Syrup", "M103", 45.0, 50));
    }

    public void addMedication(String name, String id, double price, int stock) {
        medications.add(new Medication(name, id, price, stock));
        System.out.println("Medication added: " + name);
    }

    public void updateStock(String id, int amount) {
        for (Medication med : medications) {
            if (med.getId().equals(id)) {
                med.updateStock(amount);
                System.out.println("Updated stock for " + med.getName() + ": " + med.getStock());
                return;
            }
        }
        System.out.println("Medication not found.");
    }

    public void displayMedications() {
        for (Medication med : medications) {
            System.out.println(med);
        }
    }

    public void orderMedication(String id, int quantity, Scanner scanner) {
        for (Medication med : medications) {
            if (med.getId().equals(id)) {
                if (med.getStock() >= quantity) {
                    med.updateStock(-quantity);
                    Order order = new Order(med, quantity);
                    orders.add(order);
                    System.out
                            .println("Order placed for " + med.getName() + ", total = $" + (med.getPrice() * quantity));

                    System.out.print("Enter delivery address: ");
                    scanner.nextLine();
                    String address = scanner.nextLine();
                    order.setAddress(address);

                    System.out.print("Rate this medication (1 to 5): ");
                    double rating = scanner.nextDouble();
                    if (rating >= 1 && rating <= 5) {
                        med.setRating(rating);
                        System.out.println("Thank you for rating!");
                    } else {
                        System.out.println("Invalid rating.");
                    }
                } else {
                    System.out.println("Insufficient stock.");
                }
                return;
            }
        }
        System.out.println("Medication not found.");
    }

    public void displayOrders() {
        if (orders.isEmpty()) {
            System.out.println("No orders placed.");
            return;
        }
        for (Order order : orders) {
            if (!order.isCanceled()) {
                System.out.println(order);
            }
        }
    }

    public void cancelOrder(Scanner scanner) {
        List<Order> active = new ArrayList<>();
        for (Order o : orders) {
            if (!o.isCanceled())
                active.add(o);
        }

        if (active.isEmpty()) {
            System.out.println("No active orders.");
            return;
        }

        for (int i = 0; i < active.size(); i++) {
            System.out.println((i + 1) + ". " + active.get(i));
        }

        System.out.print("Enter order number to cancel: ");
        int num = scanner.nextInt();
        scanner.nextLine();
        if (num >= 1 && num <= active.size()) {
            active.get(num - 1).cancel();
            System.out.println("Order cancelled.");
        } else {
            System.out.println("Invalid order number.");
        }
    }
}

class User {
    private String username;
    private String password;
    private String role;

    public User(String u, String p, String r) {
        username = u;
        password = p;
        role = r.toLowerCase();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }
}

public class PharmacyManagement {
    private static List<User> users = new ArrayList<>();
    private static User currentUser = null;

    public static User login(String u, String p) {
        for (User user : users) {
            if (user.getUsername().equals(u) && user.getPassword().equals(p)) {
                return user;
            }
        }
        return null;
    }

    public static void register(String u, String p, String r) {
        users.add(new User(u, p, r));
        System.out.println("Registration successful for " + r);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        PharmacyManagementSystem pms = new PharmacyManagementSystem();

        System.out.print("Enter your role (seller/buyer): ");
        String role = sc.nextLine().toLowerCase();

        if (!role.equals("seller") && !role.equals("buyer")) {
            System.out.println("Invalid role. Exiting.");
            return;
        }

        System.out.print("Register - Enter username: ");
        String username = sc.nextLine();
        System.out.print("Enter password: ");
        String password = sc.nextLine();
        register(username, password, role);

        System.out.println("\nLogin to continue:");
        boolean loggedIn = false;
        while (!loggedIn) {
            System.out.print("Username: ");
            String uname = sc.nextLine();
            System.out.print("Password: ");
            String pass = sc.nextLine();
            User u = login(uname, pass);
            if (u != null) {
                currentUser = u;
                loggedIn = true;
                System.out.println("Login successful!");
            } else {
                System.out.println("Invalid credentials. Try again.");
            }
        }

        boolean exit = false;
        while (!exit) {
            if (currentUser.getRole().equals("seller")) {
                System.out.println("\n--- Seller Menu ---");
                System.out.println("1. Add Medication");
                System.out.println("2. Update Stock");
                System.out.println("3. Display Medications");
                System.out.println("4. Logout");
                System.out.print("Choose: ");
                int ch = sc.nextInt();
                sc.nextLine();

                switch (ch) {
                    case 1:
                        System.out.print("Name: ");
                        String name = sc.nextLine();
                        System.out.print("ID: ");
                        String id = sc.nextLine();
                        System.out.print("Price: ");
                        double price = sc.nextDouble();
                        System.out.print("Stock: ");
                        int stock = sc.nextInt();
                        sc.nextLine();
                        pms.addMedication(name, id, price, stock);
                        break;
                    case 2:
                        System.out.print("Enter ID to update stock: ");
                        String updateId = sc.nextLine();
                        System.out.print("Amount (+/-): ");
                        int amt = sc.nextInt();
                        sc.nextLine();
                        pms.updateStock(updateId, amt);
                        break;
                    case 3:
                        pms.displayMedications();
                        break;
                    case 4:
                        exit = true;
                        System.out.println("Logged out.");
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }

            } else {
                System.out.println("\n--- Buyer Menu ---");
                System.out.println("1. Display Medications");
                System.out.println("2. Order Medication");
                System.out.println("3. View Orders");
                System.out.println("4. Cancel Order");
                System.out.println("5. Logout");
                System.out.print("Choose: ");
                int ch = sc.nextInt();
                sc.nextLine();

                switch (ch) {
                    case 1:
                        pms.displayMedications();
                        break;
                    case 2:
                        System.out.print("Enter Medication ID: ");
                        String oid = sc.nextLine();
                        System.out.print("Quantity: ");
                        int qty = sc.nextInt();
                        sc.nextLine();
                        pms.orderMedication(oid, qty, sc);
                        break;
                    case 3:
                        pms.displayOrders();
                        break;
                    case 4:
                        pms.cancelOrder(sc);
                        break;
                    case 5:
                        exit = true;
                        System.out.println("Logged out.");
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
            }
        }
        sc.close();
    }
}
