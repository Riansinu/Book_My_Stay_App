import java.util.*;

/**
 * Hotel Booking Management System
 * Version: 8.1
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

    void updateAvailability(String roomType, int change) {
        int current = inventory.getOrDefault(roomType, 0);
        inventory.put(roomType, current + change);
    }
}

// ---------------- SEARCH ----------------
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

    private Queue<Reservation> queue = new LinkedList<>();

    void addRequest(Reservation r) {
        queue.add(r);
    }

    Queue<Reservation> getQueue() {
        return queue;
    }
}

// ---------------- BOOKING HISTORY ----------------
class BookingHistory {

    private List<Reservation> history = new ArrayList<>();

    void addBooking(Reservation r) {
        history.add(r);
    }

    List<Reservation> getHistory() {
        return history;
    }
}

// ---------------- BOOKING SERVICE ----------------
class BookingService {

    private Set<String> allocatedRooms = new HashSet<>();
    private HashMap<String, Integer> counters = new HashMap<>();

    void processBookings(Queue<Reservation> queue, RoomInventory inventory, BookingHistory history) {

        System.out.println("Room Allocation Processing");

        while (!queue.isEmpty()) {

            Reservation r = queue.poll();
            int available = inventory.getAvailability(r.roomType);

            if (available > 0) {

                int count = counters.getOrDefault(r.roomType, 0) + 1;
                counters.put(r.roomType, count);

                String prefix = r.roomType.split(" ")[0];
                String roomId = prefix + "-" + count;

                if (!allocatedRooms.contains(roomId)) {

                    allocatedRooms.add(roomId);
                    inventory.updateAvailability(r.roomType, -1);

                    history.addBooking(r);

                    System.out.println("Booking confirmed for Guest: "
                            + r.guestName + ", Room ID: " + roomId);
                }

            } else {
                System.out.println("Booking failed for " + r.guestName);
            }
        }
    }
}

// ---------------- REPORT ----------------
class BookingReportService {

    void generateReport(List<Reservation> history) {

        System.out.println("\nBooking History and Reporting\n");
        System.out.println("Booking History Report");

        for (Reservation r : history) {
            String room = r.roomType.split(" ")[0];
            System.out.println("Guest: " + r.guestName + ", Room Type: " + room);
        }
    }
}

// ---------------- SERVICE ----------------
class Service {
    String name;
    double cost;

    Service(String name, double cost) {
        this.name = name;
        this.cost = cost;
    }
}

// ---------------- ADD-ON ----------------
class AddOnServiceManager {

    private HashMap<String, List<Service>> serviceMap = new HashMap<>();

    void addService(String reservationId, Service service) {
        serviceMap.putIfAbsent(reservationId, new ArrayList<>());
        serviceMap.get(reservationId).add(service);
    }

    double calculateTotalCost(String reservationId) {
        double total = 0;

        for (Service s : serviceMap.getOrDefault(reservationId, new ArrayList<>())) {
            total += s.cost;
        }

        return total;
    }
}

// ---------------- MAIN ----------------
public class HotelBookingManagementApp {

    public static void main(String[] args) {

        System.out.println("=========================================");
        System.out.println("        Book My Stay App");
        System.out.println("=========================================\n");

        // Rooms
        List<Room> rooms = new ArrayList<>();
        rooms.add(new SingleRoom());
        rooms.add(new DoubleRoom());
        rooms.add(new SuiteRoom());

        // Inventory
        RoomInventory inventory = new RoomInventory();

        // UC4 - Search
        new RoomSearchService().searchAvailableRooms(rooms, inventory);

        // UC5 - Queue
        BookingQueue bookingQueue = new BookingQueue();
        bookingQueue.addRequest(new Reservation("Abhi", "Single Room"));
        bookingQueue.addRequest(new Reservation("Subha", "Double Room"));
        bookingQueue.addRequest(new Reservation("Vanmathi", "Suite Room"));

        // UC6 + UC8
        BookingHistory history = new BookingHistory();
        BookingService bookingService = new BookingService();

        System.out.println();
        bookingService.processBookings(bookingQueue.getQueue(), inventory, history);

        // UC7 - Add-on
        System.out.println("\nAdd-On Service Selection");

        String reservationId = "Single-1";

        AddOnServiceManager manager = new AddOnServiceManager();
        manager.addService(reservationId, new Service("Breakfast", 500));
        manager.addService(reservationId, new Service("Pickup", 1000));

        System.out.println("Reservation ID: " + reservationId);
        System.out.println("Total Add-On Cost: " + manager.calculateTotalCost(reservationId));

        // UC8 - Report
        new BookingReportService().generateReport(history.getHistory());
    }
}