package ui;

import service.AdminService;
import model.User;
import model.Appointment;
import java.util.List;
import java.util.Scanner;

public class AdminUI {
    private AdminService adminService;
    private Scanner scanner;

    public AdminUI(AdminService adminService) {
        this.adminService = adminService;
        this.scanner = new Scanner(System.in);
    }

    public void showAdminMenu() {
        while (true) {
            System.out.println("\n=== Admin Menu ===");
            System.out.println("1. Add Doctor");
            System.out.println("2. Update Doctor");
            System.out.println("3. Delete Doctor");
            System.out.println("4. View All Doctors");
            System.out.println("5. View All Patients");
            System.out.println("6. View All Appointments");
            System.out.println("7. Cancel Appointment");
            System.out.println("8. Generate Doctor Report");
            System.out.println("9. Logout");
            System.out.print("Select an option: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    addDoctor();
                    break;
                case "2":
                    updateDoctor();
                    break;
                case "3":
                    deleteDoctor();
                    break;
                case "4":
                    viewAllDoctors();
                    break;
                case "5":
                    viewAllPatients();
                    break;
                case "6":
                    viewAllAppointments();
                    break;
                case "7":
                    cancelAppointment();
                    break;
                case "8":
                    generateDoctorReport();
                    break;
                case "9":
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void addDoctor() {
        System.out.print("Enter doctor name: ");
        String name = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter specialization: ");
        String specialization = scanner.nextLine();
        boolean success = adminService.addDoctor(name, password, specialization);
        System.out.println(success ? "Doctor added successfully." : "Failed to add doctor.");
    }

    private void updateDoctor() {
        System.out.print("Enter doctor ID to update: ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter new name: ");
        String name = scanner.nextLine();
        System.out.print("Enter new password: ");
        String password = scanner.nextLine();
        System.out.print("Enter new specialization: ");
        String specialization = scanner.nextLine();
        boolean success = adminService.updateDoctor(id, name, password, specialization);
        System.out.println(success ? "Doctor updated successfully." : "Failed to update doctor.");
    }

    private void deleteDoctor() {
        System.out.print("Enter doctor ID to delete: ");
        int id = Integer.parseInt(scanner.nextLine());
        boolean success = adminService.deleteDoctor(id);
        System.out.println(success ? "Doctor deleted successfully." : "Failed to delete doctor.");
    }

    private void viewAllDoctors() {
        List<User> doctors = adminService.viewAllDoctors();
        System.out.println("\n--- Doctors ---");
        for (User doctor : doctors) {
            System.out.println(doctor);
        }
    }

    private void viewAllPatients() {
        List<User> patients = adminService.viewAllPatients();
        System.out.println("\n--- Patients ---");
        for (User patient : patients) {
            System.out.println(patient);
        }
    }

    private void viewAllAppointments() {
        List<Appointment> appts = adminService.viewAllAppointments();
        System.out.println("\n--- Appointments ---");
        for (Appointment appt : appts) {
            System.out.println(appt);
        }
    }

    private void cancelAppointment() {
        System.out.print("Enter appointment ID to cancel: ");
        int id = Integer.parseInt(scanner.nextLine());
        boolean success = adminService.cancelAppointment(id);
        System.out.println(success ? "Appointment cancelled." : "Failed to cancel appointment.");
    }

    private void generateDoctorReport() {
        System.out.print("Enter doctor ID for report: ");
        int id = Integer.parseInt(scanner.nextLine());
        int total = adminService.getTotalAppointmentsPerDoctor(id);
        System.out.println("Total appointments for doctor ID " + id + ": " + total);
    }
} 