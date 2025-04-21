package core.backend;

public class Client {
    private static int autoID = 0;
    private int id;
    private String lastName;
    private String name;
    private String phone;

    // Constructeur
    public Client(String lastName, String name, String phone) {
        this.id = autoID++;
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
}
