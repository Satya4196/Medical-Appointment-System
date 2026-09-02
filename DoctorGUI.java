package gui;

import service.DoctorService;
import model.User;
import model.Appointment;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import gui.ModernUIComponents.*;

public class DoctorGUI extends JFrame {
    private User doctor;
    private DoctorService doctorService;
    private MainMenuGUI mainMenu;

    // UI elements
    private CardLayout cardLayout;
    private JPanel rightPanel;
    private SidebarButton scheduleTab, patientsTab, profileTab;

    // Table Models
    private DefaultTableModel scheduleModel;
    private DefaultTableModel patientsModel;

    // Profile fields
    private ModernTextField nameField;
    private ModernPasswordField passField;
    private ModernTextField specField;
    private ModernTextField phoneField;
    private JLabel statusMsg;

    public DoctorGUI(User doctor, DoctorService doctorService, MainMenuGUI mainMenu) {
        this.doctor = doctor;
        this.doctorService = doctorService;
        this.mainMenu = mainMenu;
        setTitle("Doctor Dashboard - Dr. " + doctor.getName());
        setSize(900, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(248, 250, 252));

        // 1. LEFT SIDEBAR PANEL
        GradientPanel sidebar = new GradientPanel(
            new Color(15, 23, 42),
            new Color(30, 41, 59)
        );
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setLayout(new BorderLayout());

        // Sidebar Header
        JPanel brandPanel = new JPanel(new GridLayout(3, 1));
        brandPanel.setOpaque(false);
        brandPanel.setBorder(new EmptyBorder(25, 20, 25, 20));
        JLabel doctorNameLabel = new JLabel("Dr. " + doctor.getName());
        doctorNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        doctorNameLabel.setForeground(Color.WHITE);
        
        JLabel docTitleLabel = new JLabel("MedConnect Specialist");
        docTitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        docTitleLabel.setForeground(new Color(148, 163, 184));

        JLabel specLabel = new JLabel(doctor.getSpecialization() != null ? doctor.getSpecialization() : "General Practice");
        specLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        specLabel.setForeground(new Color(99, 102, 241)); // Indigo Accent

        brandPanel.add(doctorNameLabel);
        brandPanel.add(docTitleLabel);
        brandPanel.add(specLabel);
        sidebar.add(brandPanel, BorderLayout.NORTH);

        // Sidebar Navigation Container
        JPanel navPanel = new JPanel();
        navPanel.setOpaque(false);
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

        scheduleTab = new SidebarButton("My Schedule");
        patientsTab = new SidebarButton("Patient Records");
        profileTab = new SidebarButton("Update Profile");

        navPanel.add(scheduleTab);
        navPanel.add(Box.createVerticalStrut(5));
        navPanel.add(patientsTab);
        navPanel.add(Box.createVerticalStrut(5));
        navPanel.add(profileTab);

        sidebar.add(navPanel, BorderLayout.CENTER);

        // Sidebar Footer (Logout)
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setOpaque(false);
        footerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        ModernButton logoutBtn = new ModernButton("Log Out");
        logoutBtn.setBackground(new Color(239, 68, 68));
        logoutBtn.addActionListener(e -> {
            dispose();
            mainMenu.setVisible(true);
        });
        footerPanel.add(logoutBtn, BorderLayout.SOUTH);
        sidebar.add(footerPanel, BorderLayout.SOUTH);

        mainPanel.add(sidebar, BorderLayout.WEST);

        // 2. RIGHT MAIN PANEL (Card Layout)
        cardLayout = new CardLayout();
        rightPanel = new JPanel(cardLayout);
        rightPanel.setBackground(new Color(248, 250, 252));

        setupScheduleCard();
        setupPatientsCard();
        setupProfileCard();

        mainPanel.add(rightPanel, BorderLayout.CENTER);

        // Navigation Actions
        scheduleTab.addActionListener(e -> switchTab("schedule"));
        patientsTab.addActionListener(e -> switchTab("patients"));
        profileTab.addActionListener(e -> switchTab("profile"));

        switchTab("schedule");
        setContentPane(mainPanel);
    }

    private void switchTab(String tabName) {
        scheduleTab.setActive("schedule".equals(tabName));
        patientsTab.setActive("patients".equals(tabName));
        profileTab.setActive("profile".equals(tabName));
        
        cardLayout.show(rightPanel, tabName);

        if ("schedule".equals(tabName)) {
            loadScheduleData();
        } else if ("patients".equals(tabName)) {
            loadPatientsData();
        }
    }

    // 1. Setup Schedule Tab
    private void setupScheduleCard() {
        JPanel schedulePanel = new JPanel(new BorderLayout(15, 15));
        schedulePanel.setBackground(new Color(248, 250, 252));
        schedulePanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel title = new JLabel("My Appointment Schedule");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(15, 23, 42));

        scheduleModel = new DefaultTableModel(new String[]{"Appt ID", "Patient ID", "Time Slot", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable table = new JTable(scheduleModel);
        ModernUIComponents.styleTable(table);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(241, 245, 249)));
        scrollPane.getViewport().setBackground(Color.WHITE);

        // Accept/Reject controls
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        controls.setOpaque(false);
        ModernButton acceptBtn = new ModernButton("Accept");
        acceptBtn.setBackground(new Color(34, 197, 94)); // Green Accent
        ModernButton rejectBtn = new ModernButton("Reject", true);
        rejectBtn.setForeground(new Color(239, 68, 68)); // Red Accent

        controls.add(rejectBtn);
        controls.add(acceptBtn);

        acceptBtn.addActionListener(e -> updateStatusAction(table, "accepted"));
        rejectBtn.addActionListener(e -> updateStatusAction(table, "rejected"));

        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);
        topRow.add(title, BorderLayout.WEST);
        topRow.add(controls, BorderLayout.EAST);

        schedulePanel.add(topRow, BorderLayout.NORTH);
        schedulePanel.add(scrollPane, BorderLayout.CENTER);

        rightPanel.add(schedulePanel, "schedule");
    }

    private void loadScheduleData() {
        scheduleModel.setRowCount(0);
        List<Appointment> appts = doctorService.getAppointmentsForDoctor(doctor.getId());
        for (Appointment a : appts) {
            scheduleModel.addRow(new Object[]{a.getId(), a.getPatientId(), a.getTimeSlot(), a.getStatus()});
        }
    }

    private void updateStatusAction(JTable table, String newStatus) {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select an appointment from the table.", "Select Appointment", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int apptId = (int) scheduleModel.getValueAt(row, 0);

        boolean success = doctorService.updateAppointmentStatus(apptId, newStatus);
        if (success) {
            JOptionPane.showMessageDialog(this, "Appointment status updated to " + newStatus + ".", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadScheduleData();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update appointment status.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 2. Setup Patients Tab
    private void setupPatientsCard() {
        JPanel patientPanel = new JPanel(new BorderLayout(15, 15));
        patientPanel.setBackground(new Color(248, 250, 252));
        patientPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel title = new JLabel("Registered Patients Directory");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(15, 23, 42));

        patientsModel = new DefaultTableModel(new String[]{"Patient ID", "Name", "Contact Phone"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable table = new JTable(patientsModel);
        ModernUIComponents.styleTable(table);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(241, 245, 249)));
        scrollPane.getViewport().setBackground(Color.WHITE);

        patientPanel.add(title, BorderLayout.NORTH);
        patientPanel.add(scrollPane, BorderLayout.CENTER);

        rightPanel.add(patientPanel, "patients");
    }

    private void loadPatientsData() {
        patientsModel.setRowCount(0);
        List<User> patients = doctorService.getPatientsForDoctor(doctor.getId());
        for (User p : patients) {
            patientsModel.addRow(new Object[]{p.getId(), p.getName(), p.getPhone() != null ? p.getPhone() : "N/A"});
        }
    }

    // 3. Setup Profile Card
    private void setupProfileCard() {
        JPanel profilePanel = new JPanel(new BorderLayout(15, 15));
        profilePanel.setBackground(new Color(248, 250, 252));
        profilePanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Header
        JPanel headerPanel = new JPanel(new GridLayout(2, 1));
        headerPanel.setOpaque(false);
        JLabel title = new JLabel("My Profile Settings");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(15, 23, 42));
        JLabel subtitle = new JLabel("Update your professional profile and credentials");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(new Color(100, 116, 139));
        headerPanel.add(title);
        headerPanel.add(subtitle);
        profilePanel.add(headerPanel, BorderLayout.NORTH);

        // Card Container for Form
        RoundedCard card = new RoundedCard(16, Color.WHITE);
        card.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;
        gbc.insets = new Insets(6, 6, 6, 6);

        // Name
        gbc.gridy = 0;
        card.add(createFormLabel("Full Name"), gbc);
        nameField = new ModernTextField("Full Name");
        nameField.setText(doctor.getName());
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 6, 12, 6);
        card.add(nameField, gbc);

        // Password
        gbc.gridy = 2;
        gbc.insets = new Insets(6, 6, 6, 6);
        card.add(createFormLabel("Password"), gbc);
        passField = new ModernPasswordField("New Password");
        passField.setText(doctor.getPassword());
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 6, 12, 6);
        card.add(passField, gbc);

        // Specialization
        gbc.gridy = 4;
        gbc.insets = new Insets(6, 6, 6, 6);
        card.add(createFormLabel("Specialization"), gbc);
        specField = new ModernTextField("e.g. Cardiology");
        specField.setText(doctor.getSpecialization());
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 6, 12, 6);
        card.add(specField, gbc);

        // Phone
        gbc.gridy = 6;
        gbc.insets = new Insets(6, 6, 6, 6);
        card.add(createFormLabel("Phone Number"), gbc);
        phoneField = new ModernTextField("e.g. 9876543210");
        phoneField.setText(doctor.getPhone());
        gbc.gridy = 7;
        gbc.insets = new Insets(0, 6, 12, 6);
        card.add(phoneField, gbc);

        // Message & Button Row
        statusMsg = new JLabel("", SwingConstants.CENTER);
        statusMsg.setFont(new Font("Segoe UI", Font.BOLD, 12));
        gbc.gridy = 8;
        gbc.insets = new Insets(4, 6, 12, 6);
        card.add(statusMsg, gbc);

        ModernButton saveBtn = new ModernButton("Save Changes");
        saveBtn.addActionListener(e -> saveProfileChanges());
        gbc.gridy = 9;
        gbc.insets = new Insets(6, 6, 6, 6);
        card.add(saveBtn, gbc);

        // Center card
        JPanel cardWrapper = new JPanel(new GridBagLayout());
        cardWrapper.setOpaque(false);
        GridBagConstraints wrapperGbc = new GridBagConstraints();
        wrapperGbc.weightx = 1.0;
        wrapperGbc.weighty = 1.0;
        wrapperGbc.fill = GridBagConstraints.HORIZONTAL;
        wrapperGbc.gridx = 0;
        wrapperGbc.gridy = 0;
        cardWrapper.add(card, wrapperGbc);

        profilePanel.add(cardWrapper, BorderLayout.CENTER);
        rightPanel.add(profilePanel, "profile");
    }

    private void saveProfileChanges() {
        String name = nameField.getText().trim();
        String password = new String(passField.getPassword()).trim();
        String specialization = specField.getText().trim();
        String phone = phoneField.getText().trim();

        if (name.isEmpty() || password.isEmpty() || specialization.isEmpty() || phone.isEmpty()) {
            statusMsg.setForeground(new Color(239, 68, 68));
            statusMsg.setText("All fields are required.");
            return;
        }

        boolean success = doctorService.updateProfile(doctor.getId(), name, password, specialization, phone);
        if (success) {
            statusMsg.setForeground(new Color(34, 197, 94));
            statusMsg.setText("Profile updated successfully!");
            // Update local memory user object
            doctor.setName(name);
            doctor.setPassword(password);
            doctor.setSpecialization(specialization);
            doctor.setPhone(phone);
            
            // Re-render brand name immediately
            setTitle("Doctor Dashboard - Dr. " + name);
            initUI(); // Re-initialize UI to refresh layout headers
        } else {
            statusMsg.setForeground(new Color(239, 68, 68));
            statusMsg.setText("Failed to update profile.");
        }
    }

    private JLabel createFormLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(new Color(71, 85, 105));
        return label;
    }
}