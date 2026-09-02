package gui;

import service.AuthService;
import service.AdminService;
import service.PatientService;
import model.User;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import gui.ModernUIComponents.*;

public class LoginGUI extends JFrame {
    private AuthService authService;
    private AdminService adminService;
    private PatientService patientService;
    private MainMenuGUI mainMenu;
    private boolean isRegisterMode;

    public LoginGUI(AuthService authService, AdminService adminService, PatientService patientService, MainMenuGUI mainMenu) {
        this(authService, adminService, patientService, mainMenu, false);
    }

    public LoginGUI(AuthService authService, AdminService adminService, PatientService patientService, MainMenuGUI mainMenu, boolean isRegisterMode) {
        this.authService = authService;
        this.adminService = adminService;
        this.patientService = patientService;
        this.mainMenu = mainMenu;
        this.isRegisterMode = isRegisterMode;
        setTitle(isRegisterMode ? "Create Account" : "Log In");
        setSize(480, isRegisterMode ? 580 : 480);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
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
        bgPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Card Container
        RoundedCard card = new RoundedCard(24, new Color(255, 255, 255, 245));
        card.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;
        gbc.insets = new Insets(4, 4, 4, 4);

        // 1. Header
        JLabel titleLabel = new JLabel(isRegisterMode ? "Get Started" : "Welcome Back", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(new Color(99, 102, 241));
        gbc.gridy = 0;
        card.add(titleLabel, gbc);

        JLabel subtitleLabel = new JLabel(isRegisterMode ? "Fill in the details to register" : "Enter your credentials to login", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitleLabel.setForeground(new Color(100, 116, 139));
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 4, 20, 4);
        card.add(subtitleLabel, gbc);

        // Reset insets for the form fields
        gbc.insets = new Insets(4, 4, 4, 4);

        // 2. Form Fields
        // Role Selection
        JLabel roleLabel = createFormLabel("Select Role");
        gbc.gridy = 2;
        card.add(roleLabel, gbc);

        JComboBox<String> roleBox = new JComboBox<>(new String[]{"patient", "doctor", "admin"});
        roleBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        roleBox.setBackground(Color.WHITE);
        roleBox.setForeground(new Color(30, 41, 59));
        // Flat style look for combo box
        roleBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            BorderFactory.createEmptyBorder(6, 6, 6, 6)
        ));
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 4, 12, 4);
        card.add(roleBox, gbc);

        // Username
        JLabel userLabel = createFormLabel("Username");
        gbc.gridy = 4;
        gbc.insets = new Insets(4, 4, 4, 4);
        card.add(userLabel, gbc);

        ModernTextField userField = new ModernTextField("Username");
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 4, 12, 4);
        card.add(userField, gbc);

        // Password
        JLabel passLabel = createFormLabel("Password");
        gbc.gridy = 6;
        gbc.insets = new Insets(4, 4, 4, 4);
        card.add(passLabel, gbc);

        ModernPasswordField passField = new ModernPasswordField("Password");
        gbc.gridy = 7;
        gbc.insets = new Insets(0, 4, 12, 4);
        card.add(passField, gbc);

        // Specialization (Hidden by default, shown if doctor in Register)
        JLabel specLabel = createFormLabel("Specialization");
        gbc.gridy = 8;
        gbc.insets = new Insets(4, 4, 4, 4);
        card.add(specLabel, gbc);
        specLabel.setVisible(false);

        ModernTextField specField = new ModernTextField("e.g. Cardiology");
        gbc.gridy = 9;
        gbc.insets = new Insets(0, 4, 12, 4);
        card.add(specField, gbc);
        specField.setVisible(false);

        // Phone (Hidden by default, shown if doctor in Register)
        JLabel phoneLabel = createFormLabel("Phone Number");
        gbc.gridy = 10;
        gbc.insets = new Insets(4, 4, 4, 4);
        card.add(phoneLabel, gbc);
        phoneLabel.setVisible(false);

        ModernTextField phoneField = new ModernTextField("e.g. 9876543210");
        gbc.gridy = 11;
        gbc.insets = new Insets(0, 4, 12, 4);
        card.add(phoneField, gbc);
        phoneField.setVisible(false);

        // Messages label
        JLabel msgLabel = new JLabel("", SwingConstants.CENTER);
        msgLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        gbc.gridy = 12;
        gbc.insets = new Insets(4, 4, 12, 4);
        card.add(msgLabel, gbc);

        // Buttons Panel (Horizontal layout)
        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonsPanel.setOpaque(false);
        ModernButton submitBtn = new ModernButton(isRegisterMode ? "Sign Up" : "Log In");
        ModernButton backBtn = new ModernButton("Cancel", true);
        buttonsPanel.add(submitBtn);
        buttonsPanel.add(backBtn);

        gbc.gridy = 13;
        gbc.insets = new Insets(8, 4, 4, 4);
        card.add(buttonsPanel, gbc);

        // Listeners for dynamic doctor fields
        roleBox.addActionListener(e -> {
            String role = (String) roleBox.getSelectedItem();
            boolean isDoctor = "doctor".equals(role);
            boolean showExtra = isRegisterMode && isDoctor;
            specLabel.setVisible(showExtra);
            specField.setVisible(showExtra);
            phoneLabel.setVisible(showExtra);
            phoneField.setVisible(showExtra);
            card.revalidate();
            card.repaint();
            pack();
            setSize(480, getHeight()); // Maintain fixed width
            setLocationRelativeTo(null); // Keep it centered
        });

        // Submit action
        submitBtn.addActionListener(e -> {
            String role = (String) roleBox.getSelectedItem();
            String username = userField.getText().trim();
            String password = new String(passField.getPassword()).trim();
            String specialization = specField.getText().trim();
            String phone = phoneField.getText().trim();

            if (username.isEmpty() || password.isEmpty()) {
                msgLabel.setForeground(new Color(239, 68, 68)); // Red error
                msgLabel.setText("Please fill out all fields.");
                return;
            }

            if (isRegisterMode) {
                boolean success;
                if (role.equals("admin")) {
                    success = authService.registerAdmin(username, password);
                } else if (role.equals("doctor")) {
                    if (specialization.isEmpty() || phone.isEmpty()) {
                        msgLabel.setForeground(new Color(239, 68, 68));
                        msgLabel.setText("Specialization and phone required for doctor.");
                        return;
                    }
                    success = authService.registerDoctor(username, password, specialization, phone);
                } else {
                    success = authService.registerPatient(username, password);
                }

                if (success) {
                    msgLabel.setForeground(new Color(34, 197, 94)); // Green success
                    msgLabel.setText("Registration successful!");
                    // Delay return to main menu or login
                    Timer timer = new Timer(1500, evt -> {
                        dispose();
                        mainMenu.setVisible(true);
                    });
                    timer.setRepeats(false);
                    timer.start();
                } else {
                    msgLabel.setForeground(new Color(239, 68, 68));
                    msgLabel.setText("Registration failed (user might exist).");
                }
            } else {
                User user;
                if (role.equals("admin")) {
                    user = authService.loginAdmin(username, password);
                } else if (role.equals("doctor")) {
                    user = authService.loginDoctor(username, password);
                } else {
                    user = authService.loginPatient(username, password);
                }

                if (user != null) {
                    msgLabel.setForeground(new Color(34, 197, 94));
                    msgLabel.setText("Login successful!");
                    Timer timer = new Timer(1000, evt -> {
                        dispose();
                        mainMenu.routeToDashboard(user);
                    });
                    timer.setRepeats(false);
                    timer.start();
                } else {
                    msgLabel.setForeground(new Color(239, 68, 68));
                    msgLabel.setText("Invalid username or password.");
                }
            }
        });

        // Cancel/Back action
        backBtn.addActionListener(e -> {
            dispose();
            mainMenu.setVisible(true);
        });

        // Place card in center of background gradient
        GridBagConstraints cardGbc = new GridBagConstraints();
        cardGbc.gridx = 0;
        cardGbc.gridy = 0;
        cardGbc.weightx = 1.0;
        cardGbc.weighty = 1.0;
        cardGbc.fill = GridBagConstraints.HORIZONTAL;
        bgPanel.add(card, cardGbc);

        setContentPane(bgPanel);
    }

    private JLabel createFormLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(new Color(71, 85, 105)); // Slate Gray Label
        return label;
    }
}