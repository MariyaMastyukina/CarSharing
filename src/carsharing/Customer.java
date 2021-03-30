package carsharing;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Customer {
    private List<Customer> customerList;
    private List<Company> companyList;
    private List<Car> carList;
    private Connection connection;
    private Scanner scanner = new Scanner(System.in);
    private Integer car_id;
    private int customer_id;
    private String name;

    public Customer(Connection connection) {
        this.connection = connection;
    }

    public Customer(int customer_id, String name, Integer car_id) {
        this.customer_id = customer_id;
        this.name = name;
        this.car_id = car_id;
    }

    public String getName() {
        return name;
    }

    public void setCar_id(Integer car_id) {
        this.car_id = car_id;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public Integer getCar_id() {
        return car_id;
    }

    public void logIn() throws SQLException {
        customerList = new ArrayList<>();
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM CUSTOMER");
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            System.out.println("Choose a customer:");
            customerList.add(new Customer(rs.getInt("ID"), rs.getString("NAME"), rs.getInt("RENTED_CAR_ID")));
            while (rs.next()) {
                customerList.add(new Customer(rs.getInt("ID"), rs.getString("NAME"), rs.getInt("RENTED_CAR_ID")));
            }
            for (int i = 0; i < customerList.size(); i++) {
                System.out.println(i + 1 + ". " + customerList.get(i).getName());
            }
            System.out.println("0. Back");
            int id = scanner.nextInt();
            if (id == 0) return;
            workAccount(customerList.get(id - 1));
        } else {
            System.out.println("Customer list is empty!");
        }
    }

    private void workAccount(Customer customer) throws SQLException {
        Scanner scanner= new Scanner(System.in);
        while (true) {
            getMenu();
            String option = scanner.nextLine();
            switch (option) {
                case "1":
                    rentCar(customer);
                    break;
                case "2":
                    returnCar(customer);
                    break;
                case "3":
                    getRentedCar(customer);
                    break;
                case "0":
                    return;
            }
        }
    }

    private void rentCar(Customer customer) throws SQLException {
        if (customer.getCar_id() == 0) {
            companyList = new ArrayList<>();
            Scanner scanner= new Scanner(System.in);
            PreparedStatement ps = connection.prepareStatement("Select * from COMPANY");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println("Choose a company:");
                companyList.add(new Company(rs.getInt("ID"), rs.getString("NAME")));
                while (rs.next()) {
                    companyList.add(new Company(rs.getInt("ID"), rs.getString("NAME")));
                }
                for (int i = 0; i < companyList.size(); i++) {
                    System.out.println(i + 1 + ". " + companyList.get(i).getName());
                }
                System.out.println("0. Back");
                int company_id = scanner.nextInt();
                if (company_id == 0) return;
                String companyName = companyList.get(company_id - 1).getName();
                carList = new ArrayList<>();
                ps = connection.prepareStatement("SELECT * FROM CAR WHERE COMPANY_ID = ?");
                ps.setInt(1, companyList.get(company_id - 1).getId());
                rs = ps.executeQuery();
                if (rs.next()) {
                    System.out.println("Choose a car:");
                    carList.add(new Car(rs.getInt("ID"), rs.getString("NAME"), rs.getInt("COMPANY_ID")));
                    while (rs.next()) {
                        carList.add(new Car(rs.getInt("ID"), rs.getString("NAME"), rs.getInt("COMPANY_ID")));
                    }
                    for (int i = 0; i < carList.size(); i++) {
                        System.out.println(i + 1 + ". " + carList.get(i).getName());
                    }
                    System.out.println("0. Back");
                    int chosenCarId = scanner.nextInt();
                    if (chosenCarId == 0) return;
                    ps = connection.prepareStatement("UPDATE CUSTOMER SET RENTED_CAR_ID = ? WHERE ID = ?");
                    ps.setInt(1, carList.get(chosenCarId - 1).getCar_id());
                    ps.setInt(2, customer.getCustomer_id());
                    ps.executeUpdate();
                    customer.setCar_id(carList.get(chosenCarId - 1).getCar_id());
                    System.out.println("You rented '" + carList.get(chosenCarId - 1).getName() + "'");
                } else {
                    System.out.println("No available cars in the '" + companyName + "' company");
                }
            } else {
                System.out.println("The company list is empty!");
            }
        } else {
            System.out.println("You've already rented a car!");
        }
    }

    private void returnCar(Customer customer) throws SQLException {
        if (customer.getCar_id() != 0) {
            PreparedStatement ps = connection.prepareStatement("UPDATE CUSTOMER SET RENTED_CAR_ID = NULL WHERE ID = ?");
            ps.setInt(1, customer.getCustomer_id());
            ps.executeUpdate();
            customer.setCar_id(0);
            System.out.println("You've returned a rented car!");
        } else {
            System.out.println("You didn't rent a car!");
        }
    }
    private void getRentedCar(Customer customer) throws SQLException {
        if (customer.getCar_id() != 0) {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM CAR WHERE ID = ?");
            ps.setInt(1, customer.getCar_id());
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                String carName = rs.getString("NAME");
                int company_id = rs.getInt("COMPANY_ID");
                ps = connection.prepareStatement("SELECT * FROM COMPANY WHERE ID = ?");
                ps.setInt(1, company_id);
                rs = ps.executeQuery();
                String companyName;
                if (rs.next()) {
                    companyName = rs.getString("NAME");
                    System.out.println("Your rented car:");
                    System.out.println(carName);
                    System.out.println("Company:");
                    System.out.println(companyName);
                }
            }
        } else {
            System.out.println("You didn't rent a car!");
        }
    }
    public void createCustomer() throws SQLException {
        System.out.println("Enter the customer name:");
        String name = scanner.nextLine();
        PreparedStatement ps = connection.prepareStatement("INSERT INTO CUSTOMER (NAME, RENTED_CAR_ID) VALUES(?, NULL)");
        ps.setString(1, name);
        ps.execute();
        System.out.println("The customer was added!");
    }
    private void getMenu() {
        System.out.println("1. Rent a car");
        System.out.println("2. Return a rented car");
        System.out.println("3. My rented car");
        System.out.println("0. Back");
    }
}
