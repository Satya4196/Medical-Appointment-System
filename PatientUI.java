package ui;

import service.PatientService;
import model.User;
import model.Appointment;
import java.util.List;
import java.util.Scanner;

public class PatientUI {
    private PatientService patientService;
    private User patient;
    private Scanner scanner;

    public PatientUI(PatientService patientService, User patient) {
        this.patientService = patientService;
        this.patient = patient;
        this.scanner = new Scanner(System.in);
    }

    public void showPatientMenu() {
        while (true) {
            System.out.println("\n=== Patient Menu ===");
            System.out.println("1. View All Doctors");
            System.out.println("2. Filter Doctors by Specialization");
            System.out.println("3. Book Appointment");
            System.out.println("4. Cancel Appointment");
            System.out.println("5. Update Appointment");
            System.out.println("6. View Appointment History");
            System.out.println("7. Update Profile");
            System.out.println("8. Logout");
            System.out.print("Select an option: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    viewAllDoctors();
                    break;
                case "2":
                    filterDoctorsBySpecialization();
                    break;
                case "3":
                    bookAppointment();
                    break;
                case "4":
                    cancelAppointment();
                    break;
                case "5":
                    updateAppointment();
                    break;
                case "6":
                    viewAppointmentHistory();
                    break;
                case "7":
                    updateProfile();
                    break;
                case "8":
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void viewAllDoctors() {
        List<User> doctors = patientService.viewAllDoctors();
        System.out.println("\n--- Doctors ---");
        for (User doctor : doctors) {
            System.out.println(doctor);
        }
    }

    private void filterDoctorsBySpecialization() {
        System.out.print("Enter specialization: ");
        String specialization = scanner.nextLine();
        List<User> doctors = patientService.filterDoctorsBySpecialization(specialization);
        System.out.println("\n--- Doctors with specialization: " + specialization + " ---");
        for (User doctor : doctors) {
            System.out.println(doctor);
        }
    }

    private void bookAppointment() {
        System.out.print("Enter doctor ID: ");
        int doctorId = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter time slot (e.g., 2024-06-01 10:00): ");
        String timeSlot = scanner.nextLine();
        boolean success = patientService.bookAppointment(patient.getId(), doctorId, timeSlot);
        System.out.println(success ? "Appointment booked successfully." : "Failed to book appointment (possible conflict or invalid data)." );
    }

    private void cancelAppointment() {
        System.out.print("Enter appointment ID to cancel: ");
        int id = Integer.parseInt(scanner.nextLine());
        boolean success = patientService.cancelAppointment(id);
        System.out.println(success ? "Appointment cancelled." : "Failed to cancel appointment.");
    }

    private void updateAppointment() {
        System.out.print("Enter appointment ID to update: ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter new time slot: ");
        String newTimeSlot = scanner.nextLine();
        boolean success = patientService.updateAppointment(id, newTimeSlot);
        System.out.println(success ? "Appointment updated." : "Failed to update appointment (possible conflict or invalid data)." );
    }

    private void viewAppointmentHistory() {
        List<Appointment> appts = patientService.viewAppointmentHistory(patient.getId());
        System.out.println("\n--- Appointment History ---");
        for (Appointment appt : appts) {
            System.out.println(appt);
        }
    }

    private void updateProfile() {
        System.out.print("Enter new name: ");
        String newName = scanner.nextLine();
        System.out.print("Enter new password: ");
        String newPassword = scanner.nextLine();
        boolean success = patientService.updateProfile(patient.getId(), newName, newPassword);
        System.out.println(success ? "Profile updated." : "Failed to update profile.");
    }
} 