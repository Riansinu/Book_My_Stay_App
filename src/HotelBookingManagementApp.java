import java.util.*;
import java.io.*;

/**
 * Hotel Booking Management System
 * Version: 12.1
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
class RoomInventory implements Serializable {
    private HashMap<String, Integer> inventory = new HashMap<>();

    RoomInventory() {
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    synchronized int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    synchronized void updateAvailability(String roomType, int change) {
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
class Reservation implements Serializable {
    String guestName;
    String roomType;

    Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

// ---------------- QUEUE ----------------
class BookingQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    void addRequest(Reservation r) {
        queue.add(r);
    }

    Queue<Reservation> getQueue() {
        return queue;
    }
}

// ---------------- HISTORY ----------------
class BookingHistory implements Serializable {
    private List<Reservation> history = new ArrayList<>();

    void addBooking(Reservation r) {
        history.add(r);
    }

    List<Reservation> getHistory() {
        return history;
    }
}

// ---------------- BOOKING ----------------
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

                String roomId = r.roomType.split(" ")[0] + "-" + count;

                if (!allocatedRooms.contains(roomId)) {
                    allocatedRooms.add(roomId);
                    inventory.updateAvailability(r.roomType, -1);
                    history.addBooking(r);
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
        System.out.println("\nBooking History Report");
        for (Reservation r : history) {
            System.out.println("Guest: " + r.guestName +
                    ", Room Type: " + r.roomType.split(" ")[0]);
        }
    }
}

// ---------------- ADD-ON ----------------
class Service {
    String name;
    double cost;

    Service(String name, double cost) {
        this.name = name;
        this.cost = cost;
    }
}

class AddOnServiceManager {
    private HashMap<String, List<Service>> serviceMap = new HashMap<>();

    void addService(String id, Service s) {
        serviceMap.putIfAbsent(id, new ArrayList<>());
        serviceMap.get(id).add(s);
    }

    double calculateTotalCost(String id) {
        double total = 0;
        for (Service s : serviceMap.getOrDefault(id, new ArrayList<>())) {
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
    static void validate(String name, String type) throws InvalidBookingException {
        if (name == null || name.isEmpty())
            throw new InvalidBookingException("Invalid name");

        if (!Arrays.asList("Single", "Double", "Suite").contains(type))
            throw new InvalidBookingException("Invalid room type selected.");
    }
}

// ---------------- CANCELLATION ----------------
class CancellationService {
    private Stack<String> stack = new Stack<>();
    private Set<String> active = new HashSet<>();

    void registerReservation(String id) {
        active.add(id);
    }

    void cancelBooking(String id, RoomInventory inv) {
        System.out.println("\nBooking Cancellation");

        if (!active.contains(id)) {
            System.out.println("Invalid cancellation.");
            return;
        }

        active.remove(id);
        stack.push(id);

        String type = id.split("-")[0] + " Room";
        inv.updateAvailability(type, 1);

        System.out.println("Cancelled: " + id);
    }
}

// ---------------- CONCURRENCY ----------------
class ConcurrentBookingProcessor {
    private Queue<Reservation> q;
    private RoomInventory inv;

    ConcurrentBookingProcessor(Queue<Reservation> q, RoomInventory inv) {
        this.q = q;
        this.inv = inv;
    }

    public synchronized void process() {
        if (q.isEmpty()) return;

        Reservation r = q.poll();
        if (r == null) return;

        if (inv.getAvailability(r.roomType) > 0) {
            inv.updateAvailability(r.roomType, -1);
            System.out.println(Thread.currentThread().getName() + " booked " + r.guestName);
        }
    }
}

class BookingThread extends Thread {
    private ConcurrentBookingProcessor p;

    BookingThread(ConcurrentBookingProcessor p, String name) {
        super(name);
        this.p = p;
    }

    public void run() {
        p.process();
    }
}

// ---------------- PERSISTENCE ----------------
class PersistenceService {

    void save(RoomInventory inv, BookingHistory hist) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("data.ser"))) {
            oos.writeObject(inv);
            oos.writeObject(hist);
            System.out.println("\nData saved.");
        } catch (Exception e) {
            System.out.println("Save error.");
        }
    }

    Object[] load() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("data.ser"))) {
            return new Object[]{ois.readObject(), ois.readObject()};
        } catch (Exception e) {
            System.out.println("No previous data.");
            return null;
        }
    }
}

// ---------------- MAIN ----------------
public class HotelBookingManagementApp {

    public static void main(String[] args) {

        PersistenceService ps = new PersistenceService();

        RoomInventory inventory;
        BookingHistory history;

        Object[] data = ps.load();

        if (data != null) {
            inventory = (RoomInventory) data[0];
            history = (BookingHistory) data[1];
        } else {
            inventory = new RoomInventory();
            history = new BookingHistory();
        }

        List<Room> rooms = Arrays.asList(new SingleRoom(), new DoubleRoom(), new SuiteRoom());

        new RoomSearchService().searchAvailableRooms(rooms, inventory);

        BookingQueue queue = new BookingQueue();
        queue.addRequest(new Reservation("Abhi", "Single Room"));
        queue.addRequest(new Reservation("Subha", "Double Room"));

        CancellationService cancel = new CancellationService();

        new BookingService().processBookings(queue.getQueue(), inventory, history, cancel);

        AddOnServiceManager mgr = new AddOnServiceManager();
        mgr.addService("Single-1", new Service("Breakfast", 500));

        new BookingReportService().generateReport(history.getHistory());

        cancel.cancelBooking("Single-1", inventory);

        // Concurrency
        Queue<Reservation> q = new LinkedList<>();
        q.add(new Reservation("G1", "Single Room"));
        q.add(new Reservation("G2", "Single Room"));

        ConcurrentBookingProcessor proc = new ConcurrentBookingProcessor(q, inventory);

        new BookingThread(proc, "T1").start();
        new BookingThread(proc, "T2").start();

        // Save state
        ps.save(inventory, history);
    }
}