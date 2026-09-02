package ui;

import service.AuthService;
import service.AdminService;
import service.PatientService;
import model.User;
import java.util.Scanner;

public class MainMenuUI {
    private AuthService authService;
    private AdminService adminService;
    private PatientService patientService;
    private Scanner scanner;

    public MainMenuUI(AuthService authService, AdminService adminService, PatientService patientService) {
        this.authService = authService;
        this.adminService = adminService;
        this.patientService = patientService;
        this.scanner = new Scanner(System.in);
    }

    public void showMainMenu() {
        while (true) {
            System.out.println("\n=== Medical Appointment System ===");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Select an option: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    LoginUI loginUI = new LoginUI(authService, adminService, patientService);
                    User user = loginUI.showLoginMenu();
                    if (user != null) {
                        if (user.getRole().equals("admin")) {
                            AdminUI adminUI = new AdminUI(adminService);
                            adminUI.showAdminMenu();
                        } else if (user.getRole().equals("patient")) {
                            PatientUI patientUI = new PatientUI(patientService, user);
                            patientUI.showPatientMenu();
                        }
                    }
                    break;
                case "2":
                    LoginUI regUI = new LoginUI(authService, adminService, patientService);
                    regUI.showRegisterMenu();
                    break;
                case "3":
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
} 