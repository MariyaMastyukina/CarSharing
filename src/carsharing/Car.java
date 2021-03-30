package carsharing;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Car {
    private int ID;
    private int car_id;
    private List<Car> carList;
    private Scanner scanner = new Scanner(System.in);
    private String name;
    private Connection connection;
    public Car(int car_id, String name, int company_id) {
        this.car_id = car_id;
        this.name = name;
        ID = company_id;
    }
    public Car(int ID, String name, Connection connection) {
        this.ID = ID;
        this.name = name;
        this.connection = connection;
    }

    public String getName() {
        return name;
    }

    public int getCar_id() {
        return car_id;
    }

    public void start() throws SQLException {
        while (true) {
            getMenu();
            String option = scanner.nextLine();
            switch (option) {
                case "1":
                    getList();
                    break;
                case "2":
                    createCar();
                    break;
                case "0":
                    return;
            }
        }
    }
    private void getList() throws SQLException {
        carList = new ArrayList<>();
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM CAR WHERE COMPANY_ID = ?");
        ps.setInt(1, ID);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            System.out.println("'" + name + "' cars:");
            carList.add(new Car(rs.getInt("ID"), rs.getString("NAME"), rs.getInt("COMPANY_ID")));
            while (rs.next()) {
                carList.add(new Car(rs.getInt("ID"), rs.getString("NAME"), rs.getInt("COMPANY_ID")));
            }
            for (int i = 0; i < carList.size(); i++) {
                System.out.println(i + 1 + ". " + carList.get(i).getName());
            }
        } else {
            System.out.println("The car list is empty!");
        }
    }
    private void createCar() throws SQLException {
        System.out.println("Enter the car name:");
        String name = scanner.nextLine();
        PreparedStatement ps = connection.prepareStatement("INSERT INTO CAR (NAME, COMPANY_ID) VALUES (?,?)");
        ps.setString(1, name);
        ps.setInt(2, ID);
        ps.executeUpdate();
        System.out.println("The car was added!");

    }
    private void getMenu() {
        System.out.println("'" + name + "' company:");
        System.out.println("1. Car list");
        System.out.println("2. Create a car");
        System.out.println("0. Back");
    }
}
