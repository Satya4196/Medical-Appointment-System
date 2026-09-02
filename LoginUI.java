package ui;

import service.AuthService;
import service.AdminService;
import service.PatientService;
import model.User;
import java.util.Scanner;

public class LoginUI {
    private AuthService authService;
    private AdminService adminService;
    private PatientService patientService;
    private Scanner scanner;

    public LoginUI(AuthService authService, AdminService adminService, PatientService patientService) {
        this.authService = authService;
        this.adminService = adminService;
        this.patientService = patientService;
        this.scanner = new Scanner(System.in);
    }

    public User showLoginMenu() {
        System.out.println("\n=== Login Menu ===");
        System.out.println("1. Admin Login");
        System.out.println("2. Patient Login");
        System.out.print("Select an option: ");
        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                return login("admin");
            case "2":
                return login("patient");
            default:
                System.out.println("Invalid option.");
                return null;
        }
    }

    private User login(String role) {
        System.out.print("Enter username: ");
        String name = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        User user = null;
        if (role.equals("admin")) {
            user = authService.loginAdmin(name, password);
        } else {
            user = authService.loginPatient(name, password);
        }
        if (user == null) {
            System.out.println("Login failed. Please check your credentials.");
        } else {
            System.out.println("Login successful. Welcome, " + user.getName() + "!");
        }
        return user;
    }

    public void showRegisterMenu() {
        System.out.println("\n=== Registration Menu ===");
        System.out.println("1. Register as Admin");
        System.out.println("2. Register as Patient");
        System.out.print("Select an option: ");
        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                register("admin");
                break;
            case "2":
                register("patient");
                break;
            default:
                System.out.println("Invalid option.");
        }
    }

    private void register(String role) {
        System.out.print("Enter username: ");
        String name = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        boolean success = false;
        if (role.equals("admin")) {
            success = authService.registerAdmin(name, password);
        } else {
            success = authService.registerPatient(name, password);
        }
        if (success) {
            System.out.println("Registration successful. You can now log in.");
        } else {
            System.out.println("Registration failed. Username may already exist.");
        }
    }
} 