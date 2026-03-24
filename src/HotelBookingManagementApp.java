import java.util.*;

public class HotelBookingManagementApp {

    public static void main(String[] args) {

        // ================= UC1 =================
        System.out.println("=================================");
        System.out.println("   Hotel Booking System v1.0");
        System.out.println("=================================");
        System.out.println("Welcome to Book My Stay App\n");

        // ================= UC2 =================
        System.out.println("Room Types & Availability:");

        String[] roomTypes = {"Single", "Double", "Suite"};
        int[] prices = {1500, 2500, 5000};
        int[] availability = {5, 3, 2};

        for (int i = 0; i < roomTypes.length; i++) {
            System.out.println(roomTypes[i] + " Room - Price: " + prices[i]
                    + " | Available: " + availability[i]);
        }

        // ================= UC3 =================
        System.out.println("\nCentralized Inventory (HashMap):");

        HashMap<String, Integer> inventory = new HashMap<>();
        inventory.put("Single", 5);
        inventory.put("Double", 3);
        inventory.put("Suite", 2);

        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }

        // ================= UC4 =================
        System.out.println("\nAvailable Rooms (Search):");

        for (String type : inventory.keySet()) {
            if (inventory.get(type) > 0) {
                System.out.println(type + " Room is available");
            }
        }

        // ================= UC5 =================
        System.out.println("\nBooking Request Queue:");

        Queue<String> bookingQueue = new LinkedList<>();

        bookingQueue.add("Abhi - Single");
        bookingQueue.add("Subha - Double");
        bookingQueue.add("Vanmathi - Suite");

        while (!bookingQueue.isEmpty()) {
            System.out.println("Processing: " + bookingQueue.poll());
        }
    }
}