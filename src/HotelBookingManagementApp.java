import java.util.*;

/**
 * Hotel Booking Management System
 * Version: 10.1
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

    private HashMap<String, Integer> inventory = new HashMap<>();

    RoomInventory() {
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    void updateAvailability(String roomType, int change) {
        inventory.put(roomType, inventory.getOrDefault(roomType, 0) + change);
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

    void processBookings(Queue<Reservation> queue, RoomInventory inventory,
                         BookingHistory history, CancellationService cancelService) {

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

                    // Register for cancellation
                    cancelService.registerReservation(roomId);

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
            System.out.println("Guest: " + r.guestName +
                    ", Room Type: " + r.roomType.split(" ")[0]);
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

// ---------------- VALIDATION ----------------
class InvalidBookingException extends Exception {
    InvalidBookingException(String msg) {
        super(msg);
    }
}

class BookingValidator {

    private static final Set<String> valid =
            new HashSet<>(Arrays.asList("Single", "Double", "Suite"));

    static void validate(String name, String type) throws InvalidBookingException {

        if (name == null || name.trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty");
        }

        if (!valid.contains(type)) {
            throw new InvalidBookingException("Invalid room type selected.");
        }
    }
}

// ---------------- CANCELLATION ----------------
class CancellationService {

    private Stack<String> rollbackStack = new Stack<>();
    private Set<String> activeReservations = new HashSet<>();

    void registerReservation(String roomId) {
        activeReservations.add(roomId);
    }

    void cancelBooking(String roomId, RoomInventory inventory) {

        System.out.println("\nBooking Cancellation");

        if (!activeReservations.contains(roomId)) {
            System.out.println("Cancellation failed: Invalid or already cancelled reservation.");
            return;
        }

        activeReservations.remove(roomId);
        rollbackStack.push(roomId);

        String roomType = roomId.split("-")[0] + " Room";
        inventory.updateAvailability(roomType, +1);

        System.out.println("Booking cancelled successfully. Inventory restored for room type: "
                + roomType.split(" ")[0]);

        System.out.println("\nRollback History (Most Recent First):");
        for (int i = rollbackStack.size() - 1; i >= 0; i--) {
            System.out.println("Released Reservation ID: " + rollbackStack.get(i));
        }

        System.out.println("\nUpdated " + roomType.split(" ")[0] +
                " Room Availability: " + inventory.getAvailability(roomType));
    }
}

// ---------------- MAIN ----------------
public class HotelBookingManagementApp {

    public static void main(String[] args) {

        System.out.println("=========================================");
        System.out.println("        Book My Stay App");
        System.out.println("=========================================\n");

        List<Room> rooms = Arrays.asList(
                new SingleRoom(),
                new DoubleRoom(),
                new SuiteRoom()
        );

        RoomInventory inventory = new RoomInventory();

        // UC4
        new RoomSearchService().searchAvailableRooms(rooms, inventory);

        // UC5
        BookingQueue queue = new BookingQueue();
        queue.addRequest(new Reservation("Abhi", "Single Room"));
        queue.addRequest(new Reservation("Subha", "Double Room"));
        queue.addRequest(new Reservation("Vanmathi", "Suite Room"));

        // UC6 + UC8 + UC10
        BookingHistory history = new BookingHistory();
        CancellationService cancelService = new CancellationService();

        System.out.println();
        new BookingService().processBookings(
                queue.getQueue(), inventory, history, cancelService
        );

        // UC7
        System.out.println("\nAdd-On Service Selection");
        AddOnServiceManager mgr = new AddOnServiceManager();
        mgr.addService("Single-1", new Service("Breakfast", 500));
        mgr.addService("Single-1", new Service("Pickup", 1000));

        System.out.println("Reservation ID: Single-1");
        System.out.println("Total Add-On Cost: " + mgr.calculateTotalCost("Single-1"));

        // UC8
        new BookingReportService().generateReport(history.getHistory());

        // UC9
        System.out.println("\nBooking Validation");
        Scanner sc = new Scanner(System.in);

        try {
            System.out.print("Enter guest name: ");
            String name = sc.nextLine();

            System.out.print("Enter room type (Single/Double/Suite): ");
            String type = sc.nextLine();

            BookingValidator.validate(name, type);
            System.out.println("Booking successful!");

        } catch (InvalidBookingException e) {
            System.out.println("Booking failed: " + e.getMessage());
        }

        // UC10
        cancelService.cancelBooking("Single-1", inventory);
    }
}