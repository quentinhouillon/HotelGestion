package src.controler;

public class Client {
    private int id;
    private String lastName;
    private String name;
    private String phone;

    // Constructeur
    public Client(int id, String lastName, String name, String phone) {
        this.id = id;
        this.lastName = lastName;
        this.name = name;
        this.phone = phone;
    }

    // Getters
    public String getLastName() {
        return lastName;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public int getID() {
        return id;
    }

    // Setters
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setID(int id) {
        this.id = id;
    }
}
