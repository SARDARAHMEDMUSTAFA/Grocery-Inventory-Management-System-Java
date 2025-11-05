import java.io.*;
import java.util.*;

class Product {
    int id;
    String name;
    int quantity;
    double price;
    String category;
    double total;

    Product(int id, String name, int quantity, double price, String category) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.category = category;
        this.total = quantity * price;
    }
}

public class InventoryManager {
    private static Stack<Product> inventory = new Stack<>();
    private static int nextId = 1;
    private static final String FILE_NAME = "inventory.txt";

    // ✅ Load when program starts
    public InventoryManager() {
        loadFromFile();
    }

    // ✅ Add new product
    public void addProduct(String name, int quantity, double price, String category) {
        Product p = new Product(nextId++, name, quantity, price, category);
        inventory.push(p);
        saveToFile();
    }

    // ✅ Get all
    public Stack<Product> getAllProducts() {
        return inventory;
    }

    // ✅ Update product by name (from GUI fields)
    public boolean updateProduct(String name, int newQty, double newPrice, String newCategory) {
        boolean found = false;
        Stack<Product> temp = new Stack<>();

        while (!inventory.isEmpty()) {
            Product p = inventory.pop();
            if (p.name.equalsIgnoreCase(name)) {
                p.quantity = newQty;
                p.price = newPrice;
                p.category = newCategory;
                p.total = newQty * newPrice;
                found = true;
            }
            temp.push(p);
        }

        while (!temp.isEmpty()) {
            inventory.push(temp.pop());
        }

        if (found) saveToFile();
        return found;
    }

    // ✅ Delete last
    public boolean deleteLastProduct() {
        if (!inventory.isEmpty()) {
            inventory.pop();
            saveToFile();
            return true;
        }
        return false;
    }

    // ✅ Save
    private void saveToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Product p : inventory) {
                bw.write(p.id + "," + p.name + "," + p.quantity + "," +
                        p.price + "," + p.category + "," + p.total);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("❌ Error saving file: " + e.getMessage());
        }
    }

    // ✅ Load
    void loadFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        inventory.clear();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                int id = Integer.parseInt(data[0]);
                String name = data[1];
                int quantity = Integer.parseInt(data[2]);
                double price = Double.parseDouble(data[3]);
                String category = data[4];
                double total = Double.parseDouble(data[5]);

                Product p = new Product(id, name, quantity, price, category);
                p.total = total;
                inventory.push(p);
                nextId = Math.max(nextId, id + 1);
            }
        } catch (IOException e) {
            System.out.println("❌ Error loading file: " + e.getMessage());
        }
    }

    // ✅ Public refresh for GUI
    public void refreshData() {
        loadFromFile();
    }
}
