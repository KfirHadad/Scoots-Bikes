//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.LinkedList;
//
//public class Company {
//    public static void main(String[] args) throws Exception {
//        // Load ElectricVehicle stock from a file
//        loadVehicleStockFromFile("stock.txt");
//
//        loadCustomersFromFile("customers.txt");
//        
//        Queue<SummaryDetails> summaryQueue = new Queue<SummaryDetails>();
//
//        // Start the cashier thread
//        PrintWriter output = new PrintWriter(new FileWriter("summaryFile.txt"));
//
//        Cashier cashier1 = new Cashier("Cashier1", output, summaryQueue);
//        Cashier cashier2 = new Cashier("Cashier2", output, summaryQueue);
//
//        // Start Cashier threads
//        Thread cashierThread1 = new Thread(cashier1);
//        Thread cashierThread2 = new Thread(cashier2);
//
//        cashierThread1.start();
//        cashierThread2.start();
//        
//
//
//        // Create and start employee threads
//        Thread juniorTechnicianThread = new Thread(new JuniorTechnician("1", 1));
//        Thread seniorTechnicianThread1 = new Thread(new SeniorTechnician("4", true));
//        Thread seniorTechnicianThread2 = new Thread(new SeniorTechnician("5", false));
//        Thread salesmanThread1 = new Thread(new Salesman("6"));
//        Thread salesmanThread2 = new Thread(new Salesman("7"));
//        Thread customerManagerThread1 = new Thread(new CustomerManager("8", 10));
//
//        juniorTechnicianThread.start();
//        seniorTechnicianThread1.start();
//        seniorTechnicianThread2.start();
//        salesmanThread1.start();
//        salesmanThread2.start();
//        customerManagerThread1.start();
//
//
//        // Simulate customer arrivals and interactions (you need to implement this logic)
//
//        // When the simulation is done, signal the end of the day to stop employee threads
//        CustomerManager.setManagerDayOver();
//        Clerk.setClerkDayOver();
//        JuniorTechnician.setJuniorDayOver();
//        SeniorTechnician.setSeniorDayOver();
//        Salesman.setSalesmanDayOver();
//        Cashier.setCashierDayOver();
//
//        // Wait for all threads to finish
//        try {
//            juniorTechnicianThread.join();
//            seniorTechnicianThread1.join();
//            seniorTechnicianThread2.join();
//            salesmanThread1.join();
//            salesmanThread2.join();
//            customerManagerThread1.join();
//            cashierThread1.join();
//            cashierThread2.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        // Print final summary and perform any cleanup
//
//        // Exit the program
//    }
//
//    private static void loadVehicleStockFromFile(String fileName) throws Exception {
//        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                // Parse each line and create ElectricVehicle objects
//                ElectricVehicle vehicle = ElectricVehicle.parseVehicleFromLine(line);
//                if (vehicle != null) {
//                    // Add the vehicle to the stock
//                    ElectricVehicle.addToStock(vehicle);
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//    
//    private static void loadCustomersFromFile(String fileName) throws Exception {
//        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
//            String line;
//            boolean isFirstLine = true; // Add this flag to skip the header row
//            while ((line = reader.readLine()) != null) {
//                if (isFirstLine) {
//                    isFirstLine = false;
//                    continue; // Skip the header row
//                }
//                // Parse each line and create Customer objects (you need to implement this logic)
//                Customer customer = Customer.parseCustomerFromLine(line);
//                if (customer != null) {
//                    // Process the customer (simulate arrivals and interactions)
//                    // You can send customers to appropriate queues or simulate their actions here
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//    
//}
