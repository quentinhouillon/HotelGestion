package core.backend;

public class Client {
    String lastName;
    String name;
    String phone;

    public Client(String lastName, String name, String phone) {
        this.lastName = lastName;
        this.name = name;
        this.phone = phone;
    }

    public String getLastName() {
        return lastName;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }
}
