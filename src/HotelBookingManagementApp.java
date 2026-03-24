import java.util.*;

/**
 * Hotel Booking Management System
 * Version: 5.1
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

            if (available > 0) {
                room.displayDetails();
                System.out.println("Available: " + available);
                System.out.println();
            }
        }
    }
}

// ---------------- RESERVATION ----------------
class Reservation {
    String guestName;
    String roomType;

    Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

// ---------------- BOOKING QUEUE ----------------
class BookingQueue {

    private Queue<Reservation> queue;

    BookingQueue() {
        queue = new LinkedList<>();
    }

    void addRequest(Reservation r) {
        queue.add(r);
    }

    void processRequests() {

        System.out.println("Booking Request Queue");

        while (!queue.isEmpty()) {
            Reservation r = queue.poll();

            System.out.println("Processing booking for Guest: "
                    + r.guestName + ", Room Type: " + r.roomType);
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

        // UC4 - Search
        RoomSearchService search = new RoomSearchService();
        search.searchAvailableRooms(rooms, inventory);

        // UC5 - Booking Queue
        BookingQueue bookingQueue = new BookingQueue();

        bookingQueue.addRequest(new Reservation("Abhi", "Single"));
        bookingQueue.addRequest(new Reservation("Subha", "Double"));
        bookingQueue.addRequest(new Reservation("Vanmathi", "Suite"));

        System.out.println();
        bookingQueue.processRequests();
    }
}