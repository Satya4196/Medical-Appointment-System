package model;

public class User {
    private int id;
    private String name;
    private String password;
    private String role; // "admin" or "patient"
    private String specialization; // for doctors, null for patients
    private String phone; // for doctors, null for patients

    public User(int id, String name, String password, String role, String specialization, String phone) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.role = role;
        this.specialization = specialization;
        this.phone = phone;
    }

    public User(int id, String name, String password, String role, String specialization) {
        this(id, name, password, role, specialization, null);
    }

    public User() {}

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", role='" + role + '\'' +
                (specialization != null ? ", specialization='" + specialization + '\'' : "") +
                (phone != null ? ", phone='" + phone + '\'' : "") +
                '}';
    }
} 