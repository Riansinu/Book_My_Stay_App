/**
 * Hotel Booking Management System
 * Version: 1.0
 *
 * This application demonstrates the entry point of a Java program
 * and prints a welcome message to the user.
 *
 * @author Rian
 * @version 1.0
 */

public class HotelBookingManagementApp {

    public static void main(String[] args) {

        // Welcome message
        System.out.println("=========================================");
        System.out.println("        Book My Stay App");
        System.out.println("   Hotel Booking Management System");
        System.out.println("=========================================\n");

        // Version info
        System.out.println("Version: 1.0");

        // Initialization message
        System.out.println("\nApplication started successfully...");

        /**
         * Hotel Booking Management System
         * Version: 2.1
         *
         * Demonstrates object-oriented modeling using abstraction and inheritance.
         */

        abstract class Room {
            String type;
            int beds;
            double price;

            Room(String type, int beds, double price) {
                this.type = type;
                this.beds = beds;
                this.price = price;
            }

            void displayDetails() {
                System.out.println(type + " | Beds: " + beds + " | Price: ₹" + price);
            }
        }

// Single Room
        class SingleRoom extends Room {
            SingleRoom() {
                super("Single Room", 1, 2000);
            }
        }

// Double Room
        class DoubleRoom extends Room {
            DoubleRoom() {
                super("Double Room", 2, 3500);
            }
        }

// Suite Room
        class SuiteRoom extends Room {
            SuiteRoom() {
                super("Suite Room", 3, 6000);
            }
        }

        public class HotelBookingManagementApp {

            public static void main(String[] args) {

                System.out.println("=========================================");
                System.out.println("        Book My Stay App");
                System.out.println("   Hotel Booking Management System");
                System.out.println("=========================================\n");

                System.out.println("Version: 2.1\n");

                // Create room objects (Polymorphism)
                Room r1 = new SingleRoom();
                Room r2 = new DoubleRoom();
                Room r3 = new SuiteRoom();

                // Static availability (simple variables)
                int singleAvailable = 5;
                int doubleAvailable = 3;
                int suiteAvailable = 2;

                // Display details
                System.out.println("Room Details & Availability:\n");

                r1.displayDetails();
                System.out.println("Available: " + singleAvailable + "\n");

                r2.displayDetails();
                System.out.println("Available: " + doubleAvailable + "\n");

                r3.displayDetails();
                System.out.println("Available: " + suiteAvailable + "\n");

                System.out.println("System ready...");

                import java.util.*;

/**
 * Hotel Booking Management System
 * Version: 3.1
 *
 * Demonstrates centralized room inventory using HashMap.
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

                        // Initialize availability
                        inventory.put("Single Room", 5);
                        inventory.put("Double Room", 3);
                        inventory.put("Suite Room", 2);
                    }

                    // Get availability
                    int getAvailability(String roomType) {
                        return inventory.getOrDefault(roomType, 0);
                    }

                    // Update availability
                    void updateAvailability(String roomType, int change) {
                        int current = inventory.getOrDefault(roomType, 0);
                        inventory.put(roomType, current + change);
                    }

                    // Display full inventory
                    void displayInventory(List<Room> rooms) {
                        System.out.println("Hotel Room Inventory Status\n");

                        for (Room room : rooms) {
                            room.displayDetails();
                            System.out.println("Available Rooms: " + getAvailability(room.type));
                            System.out.println();
                        }
                    }
                }

// ---------------- MAIN ----------------
                public class HotelBookingManagementApp {

                    public static void main(String[] args) {

                        // Create room objects
                        List<Room> rooms = new ArrayList<>();
                        rooms.add(new SingleRoom());
                        rooms.add(new DoubleRoom());
                        rooms.add(new SuiteRoom());

                        // Initialize inventory
                        RoomInventory inventory = new RoomInventory();

                        // Display inventory
                        inventory.displayInventory(rooms);
                    }
                }
            }
        }
    }
}