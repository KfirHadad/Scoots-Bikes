import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

public class GUI {
    private JFrame frame;
    private JTextField arrivalTimeField;
    private JTextField numManagersField;

    public GUI() {
        frame = new JFrame("Store Simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        JLabel arrivalTimeLabel = new JLabel("   Checking Time:");
        arrivalTimeField = new JTextField("2");
        JLabel numManagersLabel = new JLabel("   Number of Managers:");
        numManagersField = new JTextField("1");

        JButton startButton = new JButton("Start");
        JButton exitButton = new JButton("Exit");

        panel.add(arrivalTimeLabel);
        panel.add(arrivalTimeField);
        panel.add(numManagersLabel);
        panel.add(numManagersField);
        panel.add(startButton);
        panel.add(exitButton);

        frame.add(panel);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double checkingTime;
                int numManagers;

                try {
                    checkingTime = Double.parseDouble(arrivalTimeField.getText());
                    numManagers = Integer.parseInt(numManagersField.getText());

                    if (checkingTime >= 0 && checkingTime <= 5 && numManagers >= 0) {
                        // Initialize and start the simulation with the provided values
                        startSimulation(checkingTime, numManagers);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Invalid input values. Please enter valid numbers.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid input values. Please enter valid numbers.");
                } catch (InvalidInputException e1) {
					e1.printStackTrace();
				}
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        frame.setVisible(true);
    }

    private void startSimulation(double CheckingTime, int numManagers) throws InvalidInputException {
        try {
            // Load ElectricVehicle stock from a file
            loadVehicleStockFromFile("stock.txt");

            loadCustomersFromFile("customers.txt");

            Queue<SummaryDetails> summaryQueue = new Queue<>();
            // Start the cashier thread
            PrintWriter output = new PrintWriter(new FileWriter("summaryFile.txt"));

            Cashier cashier1 = new Cashier("Cashier1", output, summaryQueue);
            Cashier cashier2 = new Cashier("Cashier2", output, summaryQueue);

            // Start Cashier threads
            Thread cashierThread1 = new Thread(cashier1);
            Thread cashierThread2 = new Thread(cashier2);

            cashierThread1.start();
            cashierThread2.start();

            // Create and start employee threads
            Thread juniorTechnicianThread = new Thread(new JuniorTechnician("1", 1));
            Thread seniorTechnicianThread1 = new Thread(new SeniorTechnician("4", true));
            Thread seniorTechnicianThread2 = new Thread(new SeniorTechnician("5", false));
            Thread salesmanThread1 = new Thread(new Salesman("6"));
            Thread salesmanThread2 = new Thread(new Salesman("7"));

            for (int i = 0; i < numManagers; i++) {
                Thread customerManagerThread = new Thread(new CustomerManager("" + i));
                customerManagerThread.start();
            }

            juniorTechnicianThread.start();
            seniorTechnicianThread1.start();
            seniorTechnicianThread2.start();
            salesmanThread1.start();
            salesmanThread2.start();

            // Simulate customer arrivals and interactions (you need to implement this
            // logic)

            // When the simulation is done, signal the end of the day to stop employee
            // threads
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
                cashierThread1.join();
                cashierThread2.join();
                for (int i = 0; i < numManagers; i++) {
                    Thread customerManagerThread = new Thread(new CustomerManager("" + i));
                    customerManagerThread.join();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void loadVehicleStockFromFile(String fileName) throws IOException, InvalidInputException {
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
        }
    }

    private static void loadCustomersFromFile(String fileName) throws IOException, InvalidInputException {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            boolean isFirstLine = true; // Add this flag to skip the header row
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip the header row
                }
                // Parse each line and create Customer objects (you need to implement this
                // logic)
                Customer customer = Customer.parseCustomerFromLine(line);
                if (customer != null) {
                    // Process the customer (simulate arrivals and interactions)
                    // You can send customers to appropriate queues or simulate their actions here
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GUI();
            }
        });
    }
}
