package main.java.carsharing;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CarSharing {
    private Connection connection;
    private List<Company> companyList;
    public CarSharing(Connection connection) throws SQLException {
        this.connection = connection;
        createTable(connection.createStatement());
        updateTable(connection.createStatement());
    }
    private void createTable(Statement statement) throws SQLException {
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS COMPANY(" +
                "ID INTEGER PRIMARY KEY," +
                "NAME VARCHAR(255))");
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS CAR(" +
                "ID INT PRIMARY KEY AUTO_INCREMENT," +
                "NAME VARCHAR(255) UNIQUE NOT NULL," +
                "COMPANY_ID INT NOT NULL," +
                "FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY(ID) ON DELETE CASCADE ON UPDATE CASCADE)");
        statement.execute("CREATE TABLE IF NOT EXISTS CUSTOMER(" +
                "ID INT PRIMARY KEY AUTO_INCREMENT," +
                "NAME VARCHAR(255) UNIQUE NOT NULL," +
                "RENTED_CAR_ID INT," +
                "FOREIGN KEY (RENTED_CAR_ID) REFERENCES CAR(ID))");
    }
    private void updateTable(Statement statement) throws SQLException {
//        statement.executeUpdate("ALTER TABLE COMPANY ADD UNIQUE(ID)");
//        statement.executeUpdate("ALTER TABLE COMPANY ALTER COLUMN ID SET NOT NULL");
//        statement.executeUpdate("ALTER TABLE COMPANY ADD PRIMARY KEY(ID)");
        statement.executeUpdate("ALTER TABLE COMPANY ALTER COLUMN ID INTEGER AUTO_INCREMENT");
        statement.executeUpdate("ALTER TABLE COMPANY ADD UNIQUE(NAME)");
        statement.executeUpdate("ALTER TABLE COMPANY ALTER COLUMN NAME SET NOT NULL");
    }
    public void start() throws SQLException {
        Scanner scanner= new Scanner(System.in);
        while(true) {
            getMenu();
            String line = scanner.nextLine();
            switch (line) {
                case "1":
                    getCompanyList();
                    break;
                case "2":
                    createCompany();
                    break;
                case "0":
                    return;
            }
        }
    }
    public void getMenu() {
        System.out.println("1. Company list");
        System.out.println("2. Create a company");
        System.out.println("0. Back");
    }
    public List<Company> getCompanyList() throws SQLException {
        companyList = new ArrayList<>();
        Scanner scanner= new Scanner(System.in);
        PreparedStatement ps = connection.prepareStatement("Select * from COMPANY");
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            System.out.println("Choose a company:");
            companyList.add(new Company(rs.getInt("ID"), rs.getString("NAME")));
            while (rs.next()){
                companyList.add(new Company(rs.getInt("ID"), rs.getString("NAME")));
            }
            for (int i = 0; i < companyList.size(); i++) {
                System.out.println(i + 1 + ". " + companyList.get(i).getName());
            }
            System.out.println("0. Back");
            int id = scanner.nextInt();
            if (id == 0) return null;
            ps = connection.prepareStatement("SELECT * FROM COMPANY WHERE ID = ?");
            ps.setInt(1, companyList.get(id - 1).getId());
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("NAME");
                Car car = new Car(companyList.get(id - 1).getId(), name, connection);
                car.start();
            }
            return companyList;
        } else {
            System.out.println("The company list is empty!");
            return null;
        }
    }

    private void createCompany() throws SQLException {
        Scanner scanner= new Scanner(System.in);
        System.out.println("Enter the company name:");
        String line = scanner.nextLine();
        PreparedStatement ps = connection.prepareStatement("Insert into COMPANY (NAME) VALUES (?)");
        ps.setString(1, line);
        ps.executeUpdate();
        System.out.println("The company was created!");
    }
}
