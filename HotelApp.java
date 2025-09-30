import java.util.*;
class Room {
    private int roomId;
    private String roomType;
    private double pricePerNight;
    private boolean isBooked;
    public Room(int roomId, String roomType, double pricePerNight) {
        this.roomId = roomId;
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
        this.isBooked = false;
    }
    public int getRoomId() { return roomId; }
    public String getRoomType() { return roomType; }
    public double getPricePerNight() { return pricePerNight; }
    public boolean isBooked() { return isBooked; }
    public void setBooked(boolean booked) { isBooked = booked; }
    @Override
    public String toString() {
        return "Room ID: " + roomId +
                ", Type: " + roomType +
                ", Price/Night: ₹" + pricePerNight +
                ", Status: " + (isBooked ? "Booked" : "Available");
    }
}
class Customer {
    private int customerId;
    private String name;
    private String phone;

    public Customer(int customerId, String name, String phone) {
        this.customerId = customerId;
        this.name = name;
        this.phone = phone;
    }
    public int getCustomerId() { return customerId; }
    public String getName() { return name; }
    public String getPhone() { return phone; }

    @Override
    public String toString() {
        return "Customer ID: " + customerId + ", Name: " + name + ", Phone: " + phone;
    }
}
class Booking {
    private int bookingId;
    private Room room;
    private Customer customer;
    private int nights;
    private double totalAmount;
    public Booking(int bookingId, Room room, Customer customer, int nights) {
        this.bookingId = bookingId;
        this.room = room;
        this.customer = customer;
        this.nights = nights;
        this.totalAmount = room.getPricePerNight() * nights;
    }
    public int getBookingId() { return bookingId; }
    public Room getRoom() { return room; }
    public Customer getCustomer() { return customer; }
    public double getTotalAmount() { return totalAmount; }
    @Override
    public String toString() {
        return "Booking ID: " + bookingId +
                ", Customer: " + customer.getName() +
                ", Room: " + room.getRoomType() +
                ", Nights: " + nights +
                ", Total: ₹" + totalAmount;
    }
}
class HotelService {
    private List<Room> rooms = new ArrayList<>();
    private List<Booking> bookings = new ArrayList<>();
    private int bookingCounter = 1;
    private int customerCounter = 1;
    public HotelService() {
        // Preload some rooms
        rooms.add(new Room(101, "Single", 1500));
        rooms.add(new Room(102, "Double", 2500));
        rooms.add(new Room(103, "Suite", 4000));
    }
    public void viewAvailableRooms() {
        System.out.println("\n--- Available Rooms ---");
        for (Room r : rooms) {
            if (!r.isBooked()) {
                System.out.println(r);
            }
        }
    }

    public void bookRoom(String name, String phone, int roomId, int nights) {
        for (Room r : rooms) {
            if (r.getRoomId() == roomId && !r.isBooked()) {
                Customer customer = new Customer(customerCounter++, name, phone);
                Booking booking = new Booking(bookingCounter++, r, customer, nights);
                bookings.add(booking);
                r.setBooked(true);
                System.out.println("\n Booking Successful!");
                System.out.println(booking);
                return;
            }
        }
        System.out.println(" Room not available or invalid Room ID.");
    }

    public void cancelBooking(int bookingId) {
        Iterator<Booking> iterator = bookings.iterator();
        while (iterator.hasNext()) {
            Booking b = iterator.next();
            if (b.getBookingId() == bookingId) {
                b.getRoom().setBooked(false);
                iterator.remove();
                System.out.println(" Booking ID " + bookingId + " cancelled successfully.");
                return;
            }
        }
        System.out.println(" Booking ID not found.");
    }

    public void viewBookings() {
        System.out.println("\n--- All Bookings ---");
        if (bookings.isEmpty()) {
            System.out.println("No bookings found.");
        } else {
            for (Booking b : bookings) {
                System.out.println(b);
            }
        }
    }
}
public class HotelApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        HotelService service = new HotelService();
        int choice;

        do {
            System.out.println("\n========= Hotel Booking System =========");
            System.out.println("1. View Available Rooms");
            System.out.println("2. Book a Room");
            System.out.println("3. Cancel Booking");
            System.out.println("4. View All Bookings");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            switch (choice) {
                case 1:
                    service.viewAvailableRooms();
                    break;
                case 2:
                    System.out.print("Enter your name: ");
                    sc.nextLine(); // consume newline
                    String name = sc.nextLine();
                    System.out.print("Enter your phone: ");
                    String phone = sc.nextLine();
                    System.out.print("Enter Room ID to book: ");
                    int roomId = sc.nextInt();
                    System.out.print("Enter number of nights: ");
                    int nights = sc.nextInt();
                    service.bookRoom(name, phone, roomId, nights);
                    break;
                case 3:
                    System.out.print("Enter Booking ID to cancel: ");
                    int bookingId = sc.nextInt();
                    service.cancelBooking(bookingId);
                    break;
                case 4:
                    service.viewBookings();
                    break;
                case 5:
                    System.out.println(" Thank you for using Hotel Booking System!");
                    break;
                default:
                    System.out.println(" Invalid choice. Try again.");
            }
        } while (choice != 5);
    }
}

