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
            }
        }
    }
}