package core.backend;

public class Client {
    private String lastName;
    private String name;
    private String phone;

    // Constructeur
    public Client(String lastName, String name, String phone) {
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
