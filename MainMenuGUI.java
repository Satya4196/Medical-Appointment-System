package gui;

import service.AuthService;
import service.AdminService;
import service.PatientService;
import model.User;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import gui.ModernUIComponents.*;

public class MainMenuGUI extends JFrame {
    private AuthService authService;
    private AdminService adminService;
    private PatientService patientService;

    private service.DoctorService doctorService;

    public MainMenuGUI(AuthService authService, AdminService adminService, PatientService patientService, service.DoctorService doctorService) {
        this.authService = authService;
        this.adminService = adminService;
        this.patientService = patientService;
        this.doctorService = doctorService;
        setTitle("Medical Appointment System");
        setSize(480, 420);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        // Outer Gradient Background
        GradientPanel bgPanel = new GradientPanel(
            new Color(15, 23, 42),  // Deep Dark Slate
            new Color(30, 41, 59)   // Lighter Slate
        );
        bgPanel.setLayout(new GridBagLayout());
        bgPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Rounded Card container
        RoundedCard card = new RoundedCard(24, new Color(255, 255, 255, 245));
        card.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.gridx = 0;

        // Branding / Title
        JLabel titleLabel = new JLabel("MedConnect", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(99, 102, 241)); // Indigo Accent
        gbc.gridy = 0;
        card.add(titleLabel, gbc);

        JLabel subtitleLabel = new JLabel("Medical Appointment System", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitleLabel.setForeground(new Color(100, 116, 139)); // Slate secondary
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 8, 25, 8);
        card.add(subtitleLabel, gbc);

        // Buttons
        ModernButton loginBtn = new ModernButton("Log In");
        gbc.gridy = 2;
        gbc.insets = new Insets(8, 8, 8, 8);
        card.add(loginBtn, gbc);

        ModernButton registerBtn = new ModernButton("Create Account", true);
        gbc.gridy = 3;
        card.add(registerBtn, gbc);

        ModernButton exitBtn = new ModernButton("Exit Application", true);
        // Stylize Exit button to be distinct (reddish hover or soft)
        exitBtn.setForeground(new Color(239, 68, 68)); // Red text
        gbc.gridy = 4;
        gbc.insets = new Insets(16, 8, 8, 8);
        card.add(exitBtn, gbc);

        // Action Listeners
        loginBtn.addActionListener(e -> {
            new LoginGUI(authService, adminService, patientService, this).setVisible(true);
            setVisible(false);
        });
        registerBtn.addActionListener(e -> {
            new LoginGUI(authService, adminService, patientService, this, true).setVisible(true);
            setVisible(false);
        });
        exitBtn.addActionListener(e -> System.exit(0));

        // Center card inside the gradient background
        GridBagConstraints cardGbc = new GridBagConstraints();
        cardGbc.gridx = 0;
        cardGbc.gridy = 0;
        cardGbc.weightx = 1.0;
        cardGbc.weighty = 1.0;
        cardGbc.fill = GridBagConstraints.NONE;
        bgPanel.add(card, cardGbc);

        setContentPane(bgPanel);
    }

    public void routeToDashboard(User user) {
        if (user.getRole().equals("admin")) {
            new AdminGUI(adminService, this).setVisible(true);
        } else if (user.getRole().equals("patient")) {
            new PatientGUI(patientService, user, this).setVisible(true);
        } else if (user.getRole().equals("doctor")) {
            new DoctorGUI(user, doctorService, this).setVisible(true);
        }
        setVisible(false);
    }
}