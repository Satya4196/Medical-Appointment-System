package gui;

import service.AdminService;
import model.User;
import model.Appointment;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import gui.ModernUIComponents.*;

public class AdminGUI extends JFrame {
    private AdminService adminService;
    private MainMenuGUI mainMenu;

    // UI elements
    private CardLayout cardLayout;
    private JPanel rightPanel;
    private SidebarButton overviewTab, doctorsTab, patientsTab, appointmentsTab;

    // Table Models
    private DefaultTableModel doctorsModel;
    private DefaultTableModel patientsModel;
    private DefaultTableModel appointmentsModel;

    // Stat card labels (to update dynamically)
    private StatCard docStat, patientStat, apptStat;
    private JPanel overviewCard;

    public AdminGUI(AdminService adminService, MainMenuGUI mainMenu) {
        this.adminService = adminService;
        this.mainMenu = mainMenu;
        setTitle("Admin Dashboard");
        setSize(900, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        // Main container panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(248, 250, 252)); // Light background

        // 1. LEFT SIDEBAR PANEL
        GradientPanel sidebar = new GradientPanel(
            new Color(15, 23, 42), // Deep dark slate
            new Color(30, 41, 59)  // Lighter slate
        );
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setLayout(new BorderLayout());

        // Sidebar Header
        JPanel brandPanel = new JPanel(new GridLayout(2, 1));
        brandPanel.setOpaque(false);
        brandPanel.setBorder(new EmptyBorder(25, 20, 25, 20));
        JLabel brandName = new JLabel("MedConnect");
        brandName.setFont(new Font("Segoe UI", Font.BOLD, 22));
        brandName.setForeground(new Color(99, 102, 241)); // Indigo accent
        JLabel roleName = new JLabel("Administrator");
        roleName.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        roleName.setForeground(new Color(148, 163, 184));
        brandPanel.add(brandName);
        brandPanel.add(roleName);
        sidebar.add(brandPanel, BorderLayout.NORTH);

        // Sidebar Navigation Container
        JPanel navPanel = new JPanel();
        navPanel.setOpaque(false);
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

        overviewTab = new SidebarButton("Dashboard");
        doctorsTab = new SidebarButton("Doctors");
        patientsTab = new SidebarButton("Patients");
        appointmentsTab = new SidebarButton("Appointments");

        navPanel.add(overviewTab);
        navPanel.add(Box.createVerticalStrut(5));
        navPanel.add(doctorsTab);
        navPanel.add(Box.createVerticalStrut(5));
        navPanel.add(patientsTab);
        navPanel.add(Box.createVerticalStrut(5));
        navPanel.add(appointmentsTab);

        sidebar.add(navPanel, BorderLayout.CENTER);

        // Sidebar Footer (Logout)
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setOpaque(false);
        footerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        ModernButton logoutBtn = new ModernButton("Log Out");
        logoutBtn.setBackground(new Color(239, 68, 68)); // Reddish background
        logoutBtn.addActionListener(e -> {
            dispose();
            mainMenu.setVisible(true);
        });
        footerPanel.add(logoutBtn, BorderLayout.SOUTH);
        sidebar.add(footerPanel, BorderLayout.SOUTH);

        mainPanel.add(sidebar, BorderLayout.WEST);

        // 2. RIGHT MAIN PANELS (Card Layout)
        cardLayout = new CardLayout();
        rightPanel = new JPanel(cardLayout);
        rightPanel.setBackground(new Color(248, 250, 252));

        // Create cards
        setupOverviewCard();
        setupDoctorsCard();
        setupPatientsCard();
        setupAppointmentsCard();

        mainPanel.add(rightPanel, BorderLayout.CENTER);

        // Navigation actions
        overviewTab.addActionListener(e -> switchTab("overview"));
        doctorsTab.addActionListener(e -> switchTab("doctors"));
        patientsTab.addActionListener(e -> switchTab("patients"));
        appointmentsTab.addActionListener(e -> switchTab("appointments"));

        switchTab("overview"); // Default tab
        setContentPane(mainPanel);
    }

    private void switchTab(String tabName) {
        overviewTab.setActive("overview".equals(tabName));
        doctorsTab.setActive("doctors".equals(tabName));
        patientsTab.setActive("patients".equals(tabName));
        appointmentsTab.setActive("appointments".equals(tabName));
        
        cardLayout.show(rightPanel, tabName);

        // Refresh data dynamically
        if ("overview".equals(tabName)) {
            refreshStats();
        } else if ("doctors".equals(tabName)) {
            loadDoctorsData();
        } else if ("patients".equals(tabName)) {
            loadPatientsData();
        } else if ("appointments".equals(tabName)) {
            loadAppointmentsData();
        }
    }

    // 1. Setup Overview Tab
    private void setupOverviewCard() {
        overviewCard = new JPanel(new BorderLayout(20, 20));
        overviewCard.setBackground(new Color(248, 250, 252));
        overviewCard.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Welcome Header
        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        headerPanel.setOpaque(false);
        JLabel title = new JLabel("System Overview");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(15, 23, 42));
        JLabel subtitle = new JLabel("Real-time stats and management actions.");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(new Color(100, 116, 139));
        headerPanel.add(title);
        headerPanel.add(subtitle);
        overviewCard.add(headerPanel, BorderLayout.NORTH);

        // Stats Panel (Horizontal Card Grid)
        JPanel statsGrid = new JPanel(new GridLayout(1, 3, 20, 0));
        statsGrid.setOpaque(false);

        docStat = new StatCard("Doctors", "0", new Color(99, 102, 241));
        patientStat = new StatCard("Patients", "0", new Color(34, 197, 94));
        apptStat = new StatCard("Appointments", "0", new Color(234, 179, 8));

        statsGrid.add(docStat);
        statsGrid.add(patientStat);
        statsGrid.add(apptStat);
        
        // Center Section: Greeting & Brand image
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        centerPanel.add(statsGrid, BorderLayout.NORTH);

        // Brand Banner Card
        RoundedCard welcomeBanner = new RoundedCard(16, Color.WHITE);
        welcomeBanner.setLayout(new BorderLayout());
        welcomeBanner.setBorder(new EmptyBorder(20, 25, 20, 25));
        JLabel bannerTitle = new JLabel("MedConnect Administration");
        bannerTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        bannerTitle.setForeground(new Color(99, 102, 241));
        
        JTextArea bannerDesc = new JTextArea("Welcome to the Administrative portal. Here you can configure doctors, view comprehensive list of registered patients, manage system-wide appointments, and generate aggregate schedule reports.\n\nUse the sidebar navigation tabs to view lists or execute specific admin actions.");
        bannerDesc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        bannerDesc.setForeground(new Color(100, 116, 139));
        bannerDesc.setEditable(false);
        bannerDesc.setLineWrap(true);
        bannerDesc.setWrapStyleWord(true);
        bannerDesc.setOpaque(false);

        welcomeBanner.add(bannerTitle, BorderLayout.NORTH);
        welcomeBanner.add(bannerDesc, BorderLayout.CENTER);
        centerPanel.add(welcomeBanner, BorderLayout.CENTER);

        overviewCard.add(centerPanel, BorderLayout.CENTER);
        rightPanel.add(overviewCard, "overview");
    }

    private void refreshStats() {
        int docs = adminService.viewAllDoctors().size();
        int patients = adminService.viewAllPatients().size();
        int appts = adminService.viewAllAppointments().size();

        // Remove and recreate statsGrid to update (simplest way in Swing without binding)
        overviewCard.remove(1); // Remove centerPanel
        
        JPanel statsGrid = new JPanel(new GridLayout(1, 3, 20, 0));
        statsGrid.setOpaque(false);

        docStat = new StatCard("Doctors", String.valueOf(docs), new Color(99, 102, 241));
        patientStat = new StatCard("Patients", String.valueOf(patients), new Color(34, 197, 94));
        apptStat = new StatCard("Appointments", String.valueOf(appts), new Color(234, 179, 8));

        statsGrid.add(docStat);
        statsGrid.add(patientStat);
        statsGrid.add(apptStat);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        centerPanel.add(statsGrid, BorderLayout.NORTH);

        RoundedCard welcomeBanner = new RoundedCard(16, Color.WHITE);
        welcomeBanner.setLayout(new BorderLayout());
        welcomeBanner.setBorder(new EmptyBorder(20, 25, 20, 25));
        JLabel bannerTitle = new JLabel("MedConnect Administration");
        bannerTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        bannerTitle.setForeground(new Color(99, 102, 241));
        
        JTextArea bannerDesc = new JTextArea("Welcome to the Administrative portal. Here you can configure doctors, view comprehensive list of registered patients, manage system-wide appointments, and generate aggregate schedule reports.\n\nUse the sidebar navigation tabs to view lists or execute specific admin actions.");
        bannerDesc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        bannerDesc.setForeground(new Color(100, 116, 139));
        bannerDesc.setEditable(false);
        bannerDesc.setLineWrap(true);
        bannerDesc.setWrapStyleWord(true);
        bannerDesc.setOpaque(false);

        welcomeBanner.add(bannerTitle, BorderLayout.NORTH);
        welcomeBanner.add(bannerDesc, BorderLayout.CENTER);
        centerPanel.add(welcomeBanner, BorderLayout.CENTER);

        overviewCard.add(centerPanel, BorderLayout.CENTER);
        overviewCard.revalidate();
        overviewCard.repaint();
    }

    // 2. Setup Doctors Tab
    private void setupDoctorsCard() {
        JPanel docPanel = new JPanel(new BorderLayout(15, 15));
        docPanel.setBackground(new Color(248, 250, 252));
        docPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel title = new JLabel("Doctor Directory");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(15, 23, 42));

        doctorsModel = new DefaultTableModel(new String[]{"ID", "Name", "Specialization"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable table = new JTable(doctorsModel);
        ModernUIComponents.styleTable(table);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(241, 245, 249)));
        scrollPane.getViewport().setBackground(Color.WHITE);

        // Control Panel
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        controls.setOpaque(false);
        ModernButton addBtn = new ModernButton("Add New Doctor");
        ModernButton deleteBtn = new ModernButton("Delete Doctor", true);
        deleteBtn.setForeground(new Color(239, 68, 68));
        controls.add(addBtn);
        controls.add(deleteBtn);

        addBtn.addActionListener(e -> addDoctorAction());
        deleteBtn.addActionListener(e -> deleteDoctorAction(table));

        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);
        topRow.add(title, BorderLayout.WEST);
        topRow.add(controls, BorderLayout.EAST);

        docPanel.add(topRow, BorderLayout.NORTH);
        docPanel.add(scrollPane, BorderLayout.CENTER);

        rightPanel.add(docPanel, "doctors");
    }

    private void loadDoctorsData() {
        doctorsModel.setRowCount(0);
        List<User> doctors = adminService.viewAllDoctors();
        for (User d : doctors) {
            doctorsModel.addRow(new Object[]{d.getId(), d.getName(), d.getSpecialization()});
        }
    }

    private void addDoctorAction() {
        JTextField nameField = new JTextField();
        JPasswordField passField = new JPasswordField();
        JTextField specField = new JTextField();
        Object[] message = {
            "Doctor Name:", nameField,
            "Password:", passField,
            "Specialization:", specField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Add Doctor Profile", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (option == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String password = new String(passField.getPassword()).trim();
            String specialization = specField.getText().trim();

            if (name.isEmpty() || password.isEmpty() || specialization.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean success = adminService.addDoctor(name, password, specialization);
            if (success) {
                JOptionPane.showMessageDialog(this, "Doctor registration successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadDoctorsData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to register doctor.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteDoctorAction(JTable table) {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a doctor row to delete.", "Select Doctor", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) doctorsModel.getValueAt(row, 0);
        String name = (String) doctorsModel.getValueAt(row, 1);

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete Dr. " + name + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = adminService.deleteDoctor(id);
            if (success) {
                JOptionPane.showMessageDialog(this, "Doctor deleted successfully.");
                loadDoctorsData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete doctor.");
            }
        }
    }

    // 3. Setup Patients Tab
    private void setupPatientsCard() {
        JPanel patientPanel = new JPanel(new BorderLayout(15, 15));
        patientPanel.setBackground(new Color(248, 250, 252));
        patientPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel title = new JLabel("Registered Patients");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(15, 23, 42));

        patientsModel = new DefaultTableModel(new String[]{"ID", "Name", "Role"}, 0) {
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
        List<User> patients = adminService.viewAllPatients();
        for (User p : patients) {
            patientsModel.addRow(new Object[]{p.getId(), p.getName(), p.getRole()});
        }
    }

    // 4. Setup Appointments Tab
    private void setupAppointmentsCard() {
        JPanel apptPanel = new JPanel(new BorderLayout(15, 15));
        apptPanel.setBackground(new Color(248, 250, 252));
        apptPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel title = new JLabel("System Appointment Schedule");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(15, 23, 42));

        appointmentsModel = new DefaultTableModel(new String[]{"ID", "Patient ID", "Doctor ID", "Time Slot", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable table = new JTable(appointmentsModel);
        ModernUIComponents.styleTable(table);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(241, 245, 249)));
        scrollPane.getViewport().setBackground(Color.WHITE);

        // Control Panel
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        controls.setOpaque(false);
        ModernButton cancelBtn = new ModernButton("Cancel Appointment");
        cancelBtn.setBackground(new Color(239, 68, 68)); // Red color
        ModernButton reportBtn = new ModernButton("Doctor Workload Report", true);
        controls.add(reportBtn);
        controls.add(cancelBtn);

        cancelBtn.addActionListener(e -> cancelAppointmentAction(table));
        reportBtn.addActionListener(e -> generateReportAction(table));

        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);
        topRow.add(title, BorderLayout.WEST);
        topRow.add(controls, BorderLayout.EAST);

        apptPanel.add(topRow, BorderLayout.NORTH);
        apptPanel.add(scrollPane, BorderLayout.CENTER);

        rightPanel.add(apptPanel, "appointments");
    }

    private void loadAppointmentsData() {
        appointmentsModel.setRowCount(0);
        List<Appointment> appts = adminService.viewAllAppointments();
        for (Appointment a : appts) {
            appointmentsModel.addRow(new Object[]{a.getId(), a.getPatientId(), a.getDoctorId(), a.getTimeSlot(), a.getStatus()});
        }
    }

    private void cancelAppointmentAction(JTable table) {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select an appointment from the table to cancel.", "Select Appointment", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) appointmentsModel.getValueAt(row, 0);
        String status = (String) appointmentsModel.getValueAt(row, 4);

        if ("cancelled".equalsIgnoreCase(status)) {
            JOptionPane.showMessageDialog(this, "This appointment is already cancelled.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel appointment ID " + id + "?", "Confirm Cancel", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = adminService.cancelAppointment(id);
            if (success) {
                JOptionPane.showMessageDialog(this, "Appointment cancelled successfully.");
                loadAppointmentsData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to cancel appointment.");
            }
        }
    }

    private void generateReportAction(JTable table) {
        int row = table.getSelectedRow();
        int defaultDocId = -1;
        if (row >= 0) {
            defaultDocId = (int) appointmentsModel.getValueAt(row, 2);
        }

        String input = JOptionPane.showInputDialog(this, "Enter doctor ID for report:", defaultDocId != -1 ? String.valueOf(defaultDocId) : "");
        if (input != null) {
            try {
                int id = Integer.parseInt(input);
                int total = adminService.getTotalAppointmentsPerDoctor(id);
                JOptionPane.showMessageDialog(this, "Total booked/pending appointments for Doctor ID " + id + ": " + total, "Workload Report", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid ID format.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}