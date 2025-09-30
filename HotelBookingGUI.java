import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
                " | Type: " + roomType +
                " | Price/Night: ₹" + pricePerNight +
                " | Status: " + (isBooked ? "Booked" : "Available");
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
        return "Customer ID: " + customerId + " | Name: " + name + " | Phone: " + phone;
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
                " | Customer: " + customer.getName() +
                " | Room: " + room.getRoomType() +
                " | Nights: " + nights +
                " | Total: ₹" + totalAmount;
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

    public List<Room> getRooms() { return rooms; }
    public List<Booking> getBookings() { return bookings; }

    public String viewAvailableRooms() {
        StringBuilder sb = new StringBuilder();
        for (Room r : rooms) {
            if (!r.isBooked()) sb.append(r).append("\n");
        }
        return sb.length() == 0 ? "No available rooms." : sb.toString();
    }

    public String bookRoom(String name, String phone, int roomId, int nights) {
        for (Room r : rooms) {
            if (r.getRoomId() == roomId && !r.isBooked()) {
                Customer customer = new Customer(customerCounter++, name, phone);
                Booking booking = new Booking(bookingCounter++, r, customer, nights);
                bookings.add(booking);
                r.setBooked(true);
                return "Booking Successful!\n" + booking;
            }
        }
        return "Room not available or invalid Room ID.";
    }

    public String cancelBooking(int bookingId) {
        Iterator<Booking> iterator = bookings.iterator();
        while (iterator.hasNext()) {
            Booking b = iterator.next();
            if (b.getBookingId() == bookingId) {
                b.getRoom().setBooked(false);
                iterator.remove();
                return " Booking ID " + bookingId + " cancelled successfully.";
            }
        }
        return " Booking ID not found.";
    }

    public String viewBookings() {
        if (bookings.isEmpty()) return "No bookings found.";
        StringBuilder sb = new StringBuilder();
        for (Booking b : bookings) sb.append(b).append("\n");
        return sb.toString();
    }
}
public class HotelBookingGUI extends JFrame implements ActionListener {
    private HotelService service;
    private JTextArea outputArea;
    private JTextField nameField, phoneField, roomIdField, nightsField, bookingIdField;
    private JButton viewRoomsBtn, bookBtn, cancelBtn, viewBookingsBtn, clearBtn;
    public HotelBookingGUI() {
        service = new HotelService();
        setTitle("Hotel Booking System");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Booking Details"));
        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Phone:"));
        phoneField = new JTextField();
        inputPanel.add(phoneField);
        inputPanel.add(new JLabel("Room ID:"));
        roomIdField = new JTextField();
        inputPanel.add(roomIdField);
        inputPanel.add(new JLabel("Nights:"));
        nightsField = new JTextField();
        inputPanel.add(nightsField);
        inputPanel.add(new JLabel("Booking ID (for cancel):"));
        bookingIdField = new JTextField();
        inputPanel.add(bookingIdField);
        add(inputPanel, BorderLayout.NORTH);
        JPanel buttonPanel = new JPanel(new GridLayout(1, 5, 5, 5));
        viewRoomsBtn = new JButton("View Rooms");
        bookBtn = new JButton("Book Room");
        cancelBtn = new JButton("Cancel Booking");
        viewBookingsBtn = new JButton("View Bookings");
        clearBtn = new JButton("Clear");
        JButton[] buttons = {viewRoomsBtn, bookBtn, cancelBtn, viewBookingsBtn, clearBtn};
        for (JButton btn : buttons) {
            btn.addActionListener(this);
            buttonPanel.add(btn);
        }
        add(buttonPanel, BorderLayout.SOUTH);
        setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == viewRoomsBtn) {
            outputArea.setText(service.viewAvailableRooms());
        } else if (e.getSource() == bookBtn) {
            String name = nameField.getText();
            String phone = phoneField.getText();
            int roomId = Integer.parseInt(roomIdField.getText());
            int nights = Integer.parseInt(nightsField.getText());
            outputArea.setText(service.bookRoom(name, phone, roomId, nights));
        } else if (e.getSource() == cancelBtn) {
            int bookingId = Integer.parseInt(bookingIdField.getText());
            outputArea.setText(service.cancelBooking(bookingId));
        } else if (e.getSource() == viewBookingsBtn) {
            outputArea.setText(service.viewBookings());
        } else if (e.getSource() == clearBtn) {
            nameField.setText("");
            phoneField.setText("");
            roomIdField.setText("");
            nightsField.setText("");
            bookingIdField.setText("");
            outputArea.setText("");
        }
    }
    public static void main(String[] args) {
        new HotelBookingGUI();
    }
}

