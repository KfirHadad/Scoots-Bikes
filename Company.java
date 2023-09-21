import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Company {
    public static void main(String[] args) throws InvalidInputException {
        // Load ElectricVehicle stock from a file
        loadVehicleStockFromFile("stock.txt");

        // Create and start employee threads
        Thread juniorTechnicianThread = new Thread(new JuniorTechnician("1", 1));
        Thread seniorTechnicianThread1 = new Thread(new SeniorTechnician("4", true));
        Thread seniorTechnicianThread2 = new Thread(new SeniorTechnician("5", false));
        Thread salesmanThread1 = new Thread(new Salesman("6"));
        Thread salesmanThread2 = new Thread(new Salesman("7"));
        Thread customerManagerThread1 = new Thread(new CustomerManager("8", 10));

        juniorTechnicianThread.start();
        seniorTechnicianThread1.start();
        seniorTechnicianThread2.start();
        salesmanThread1.start();
        salesmanThread2.start();
        customerManagerThread1.start();

        // Start the cashier thread
        Thread cashierThread = new Thread(new Cashier("Cashier1"));
        cashierThread.start();

        // Simulate customer arrivals and interactions (you need to implement this logic)

        // When the simulation is done, signal the end of the day to stop employee threads
        CustomerManager.setManagerDayOver();
        Clerk.setClerkDayOver();
        JuniorTechnician.setJuniorDayOver();
        SeniorTechnician.setSeniorDayOver();
        Salesman.setSalesmanDayOver();
        Cashier.setCashierDayOver();

        // Wait for all threads to finish
        try {
            juniorTechnicianThread.join();
            seniorTechnicianThread1.join();
            seniorTechnicianThread2.join();
            salesmanThread1.join();
            salesmanThread2.join();
            customerManagerThread1.join();
            cashierThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Print final summary and perform any cleanup

        // Exit the program
    }

    private static void loadVehicleStockFromFile(String fileName) throws InvalidInputException {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Parse each line and create ElectricVehicle objects
                ElectricVehicle vehicle = ElectricVehicle.parseVehicleFromLine(line);
                if (vehicle != null) {
                    // Add the vehicle to the stock
                    ElectricVehicle.addToStock(vehicle);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
