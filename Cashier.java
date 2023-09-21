import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Cashier implements Runnable {

	private String id;
	private PrintWriter outputFile;
	private static Queue<SummaryDetails> summaryQueue;
	private static volatile boolean isDayOver = false;
	private int totalCustomersTreated = 0;
	private int totalProfit = 0;
	private int maxSeller = 0;
	private int maxHandler = 0;
	private String bestSalesman = "";
	private String bestTechnician = "";

	public Cashier(String id, PrintWriter outputFile, Queue<SummaryDetails> summaryQueue) {
		this.id = id;
		this.outputFile = outputFile;
		Cashier.summaryQueue = summaryQueue;

	}

	@Override
	public void run() {
		try {
			while (!isDayOver) {
				// Wait for summary documents
				SummaryDetails summaryRow = getRow();

				// Write the summary to the text file
				writeSummaryToFile(summaryRow);

				// Update statistics
				updateStatistics(summaryRow);

				// Sleep for one second (feed time)
				Thread.sleep(1000);
			}

			// Print the final summary when the day is over
			printFinalSummary();

			// Close the text file
			outputFile.close();
		} catch (InterruptedException e) {
			e.printStackTrace(); // Handle interrupted exception if needed
		}
	}

	private synchronized SummaryDetails getRow() throws InterruptedException {
		SummaryDetails row = summaryQueue.remove();
		return row;
	}

	private synchronized SummaryDetails waitForSummaryDocument() throws InterruptedException {
		while (summaryQueue.isEmpty() && !isDayOver) {
			summaryQueue.wait();
		}
		if (isDayOver) {
			return null; // Return null to indicate the end of the day
		}
		return summaryQueue.remove();

	}

	private void writeSummaryToFile(SummaryDetails summary) {
		if (summary != null) {
			outputFile.println(summary.toString());
		}
	}

	private void updateStatistics(SummaryDetails summary) {
		if (summary != null) {
			totalCustomersTreated++;
			totalProfit += summary.getTotal();

			// Update best sales representative and technician
			if (summary.getEmployee() instanceof Salesman) {
				if (((Salesman) summary.getEmployee()).getTotal() > maxSeller) {
					maxSeller = ((Salesman) summary.getEmployee()).getTotal();
					bestSalesman = summary.getEmployee().getID();
				}
			} else if (summary.getEmployee() instanceof JuniorTechnician
					|| summary.getEmployee() instanceof SeniorTechnician) {
				if(summary.getEmployee().getCustomerCount() > maxHandler) {
					maxHandler = summary.getEmployee().getCustomerCount();
					bestTechnician = summary.getEmployee().getID();
				}	
			}
		}
	}

	private void printFinalSummary() {
		outputFile.println("Number of customers treated: " + totalCustomersTreated);
		outputFile.println("Total profit: " + totalProfit + " NIS");
		outputFile.println("Best Sales Representative: " + bestSalesman);
		outputFile.println("Best Technician: " + bestTechnician);
	}

	protected static synchronized void setCashierDayOver() {
		isDayOver = true;
	}

	protected static synchronized void addToQueue(SummaryDetails row) {
		summaryQueue.add(row);
		Cashier.class.notifyAll();
	}

}
