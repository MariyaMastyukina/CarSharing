package main.java.carsharing;

public class Company {
    private int id;
    private String name;
    public Company(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
