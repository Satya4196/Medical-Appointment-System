import service.AuthService;
import service.AdminService;
import service.PatientService;
import service.DoctorService;
import gui.MainMenuGUI;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        AuthService authService = new AuthService();
        AdminService adminService = new AdminService();
        PatientService patientService = new PatientService();
        DoctorService doctorService = new DoctorService();
        SwingUtilities.invokeLater(() -> {
            MainMenuGUI mainMenuGUI = new MainMenuGUI(authService, adminService, patientService, doctorService);
            mainMenuGUI.setVisible(true);
        });
    }
} 