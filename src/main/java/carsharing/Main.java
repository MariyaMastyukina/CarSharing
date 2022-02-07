package main.java.carsharing;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        // write your code here
        DBConnection dbConnection = null;
        Scanner scanner = new Scanner(System.in);
        if (args.length == 2 && args[0].equals("-databaseFileName")) {
            dbConnection = new DBConnection(args[1]);
        } else {
            dbConnection = new DBConnection("defaultDB");
        }
        CarSharing carSharing = new CarSharing(dbConnection.connectDB());
        Customer customer = new Customer(dbConnection.getConnection());
        while (true) {
            System.out.println("1. Log in as a manager");
            System.out.println("2. Log in as a customer");
            System.out.println("3. Create a customer");
            System.out.println("0. Exit");
            String line = scanner.nextLine();
            switch (line) {
                case "1":
                    carSharing.start();
                    break;
                case "2":
                    customer.logIn();
                    break;
                case "3":
                    customer.createCustomer();
                    break;
                case "0":
                    return;
            }
        }
    }
}
