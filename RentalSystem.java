import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;

public class RentalSystem {
	
	private static RentalSystem instance;
	
    private List<Vehicle> vehicles = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();
    private List<RentalRecord> rentalRecords = new ArrayList<>();
    private RentalHistory rentalHistory = new RentalHistory();
    
    
    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
        
        saveVehicle();
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
        
        saveCustomer();
    }

    public void rentVehicle(Vehicle vehicle, Customer customer, LocalDate date, double amount) {
        if (vehicle.getStatus() == Vehicle.VehicleStatus.AVAILABLE) {
            vehicle.setStatus(Vehicle.VehicleStatus.RENTED);
            rentalHistory.addRecord(new RentalRecord(vehicle, customer, date, amount, "RENT"));
            System.out.println("Vehicle rented to " + customer.getCustomerName());
        }
        else {
            System.out.println("Vehicle is not available for renting.");
        }
    }

    public void returnVehicle(Vehicle vehicle, Customer customer, LocalDate date, double extraFees) {
        if (vehicle.getStatus() == Vehicle.VehicleStatus.RENTED) {
            vehicle.setStatus(Vehicle.VehicleStatus.AVAILABLE);
            rentalHistory.addRecord(new RentalRecord(vehicle, customer, date, extraFees, "RETURN"));
            System.out.println("Vehicle returned by " + customer.getCustomerName());
        }
        else {
            System.out.println("Vehicle is not rented.");
        }
        
        saveRecord();
    }    

    public void displayAvailableVehicles() {
    	System.out.println("|     Type         |\tPlate\t|\tMake\t|\tModel\t|\tYear\t|");
    	System.out.println("---------------------------------------------------------------------------------");
    	 
        for (Vehicle v : vehicles) {
            if (v.getStatus() == Vehicle.VehicleStatus.AVAILABLE) {
                System.out.println("|     " + (v instanceof Car ? "Car          " : "Motorcycle   ") + "|\t" + v.getLicensePlate() + "\t|\t" + v.getMake() + "\t|\t" + v.getModel() + "\t|\t" + v.getYear() + "\t|\t");
            }
        }
        System.out.println();
    }
    
    public void displayAllVehicles() {
        for (Vehicle v : vehicles) {
            System.out.println("  " + v.getInfo());
        }
    }

    public void displayAllCustomers() {
        for (Customer c : customers) {
            System.out.println("  " + c.toString());
        }
    }
    
    public void displayRentalHistory() {
        for (RentalRecord record : rentalHistory.getRentalHistory()) {
            System.out.println(record.toString());
        }
    }
    
    public Vehicle findVehicleByPlate(String plate) {
        for (Vehicle v : vehicles) {
            if (v.getLicensePlate().equalsIgnoreCase(plate)) {
                return v;
            }
        }
        return null;
    }
    
    public Customer findCustomerById(int id) {
        for (Customer c : customers)
            if (c.getCustomerId() == id)
                return c;
        return null;
    }

    public Customer findCustomerByName(String name) {
        for (Customer c : customers)
            if (c.getCustomerName().equalsIgnoreCase(name))
                return c;
        return null;
    }
    
    public static RentalSystem getInstance() {
        if (instance == null) {
            instance = new RentalSystem();
        }
        return instance;
    }
    
    public void saveVehicle() {
        try (PrintWriter writer = new PrintWriter("vehicles.txt")) {
            for (Vehicle v : vehicles) {
                writer.println(v.getLicensePlate() + "," + v.getMake() + "," + v.getModel() + "," + v.getYear());
            }
        } catch (IOException e) {
            System.out.println("Error saving vehicles: " + e.getMessage());
        }
    }

    public void saveCustomer() {
        try (PrintWriter writer = new PrintWriter("customers.txt")) {
            for (Customer c : customers) {
                writer.println(c.getCustomerId() + "," + c.getCustomerName());
            }
        } catch (IOException e) {
            System.out.println("Error saving customers: " + e.getMessage());
        }
    }
    
    public void saveRecord() {
        try (PrintWriter writer = new PrintWriter("rental_records.txt")) {
            for (RentalRecord r : rentalRecords) {
                writer.println(r.getCustomer().getCustomerId() + "," + r.getVehicle().getLicensePlate());
            }
        } catch (IOException e) {
            System.out.println("Error saving records: " + e.getMessage());
        }
    }
    
    
    
}