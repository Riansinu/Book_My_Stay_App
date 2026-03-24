import java.util.*;

/**
 * Hotel Booking Management System
 * Version: 4.1
 */

// ---------------- ROOM DOMAIN ----------------
abstract class Room {
    String type;
    int beds;
    int size;
    double price;

    Room(String type, int beds, int size, double price) {
        this.type = type;
        this.beds = beds;
        this.size = size;
        this.price = price;
    }

    void displayDetails() {
        System.out.println(type + ":");
        System.out.println("Beds: " + beds);
        System.out.println("Size: " + size + " sqft");
        System.out.println("Price per night: " + price);
    }
}

class SingleRoom extends Room {
    SingleRoom() {
        super("Single Room", 1, 250, 1500.0);
    }
}

class DoubleRoom extends Room {
    DoubleRoom() {
        super("Double Room", 2, 400, 2500.0);
    }
}

class SuiteRoom extends Room {
    SuiteRoom() {
        super("Suite Room", 3, 750, 5000.0);
    }
}

// ---------------- INVENTORY ----------------
class RoomInventory {

    private HashMap<String, Integer> inventory;

    RoomInventory() {
        inventory = new HashMap<>();

        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }
}

// ---------------- SEARCH SERVICE ----------------
class RoomSearchService {

    void searchAvailableRooms(List<Room> rooms, RoomInventory inventory) {

        System.out.println("Room Search\n");

        for (Room room : rooms) {
            int available = inventory.getAvailability(room.type);

            // Only show available rooms
            if (available > 0) {
                room.displayDetails();
                System.out.println("Available: " + available);
                System.out.println();
            }
        }
    }
}

// ---------------- MAIN ----------------
public class HotelBookingManagementApp {

    public static void main(String[] args) {

        System.out.println("=========================================");
        System.out.println("        Book My Stay App");
        System.out.println("=========================================\n");

        // Create room objects
        List<Room> rooms = new ArrayList<>();
        rooms.add(new SingleRoom());
        rooms.add(new DoubleRoom());
        rooms.add(new SuiteRoom());

        // Inventory
        RoomInventory inventory = new RoomInventory();

        // Search (READ ONLY)
        RoomSearchService search = new RoomSearchService();
        search.searchAvailableRooms(rooms, inventory);
    }
}