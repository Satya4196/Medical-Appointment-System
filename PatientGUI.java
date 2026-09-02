package gui;

import service.PatientService;
import model.User;
import model.Appointment;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import gui.ModernUIComponents.*;

public class PatientGUI extends JFrame {
    private PatientService patientService;
    private User patient;
    private MainMenuGUI mainMenu;

    // UI elements
    private CardLayout cardLayout;
    private JPanel rightPanel;
    private SidebarButton apptsTab, bookTab, doctorsTab, profileTab;

    // Table Models
    private DefaultTableModel appointmentsModel;
    private DefaultTableModel doctorsModel;

    // Book Form Fields
    private JComboBox<DoctorComboItem> doctorBox;
    private JComboBox<String> dateBox;
    private JButton[] slotButtons;
    private String selectedSlotTime = null;
    private final String[] timeSlots = {"09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00"};
    private JLabel bookingMsg;

    // Profile Fields
    private ModernTextField nameField;
    private ModernPasswordField passField;
    private JLabel profileMsg;

    // Doctor Combo Box Item Wrapper
    private static class DoctorComboItem {
        private User doctor;
        public DoctorComboItem(User doctor) { this.doctor = doctor; }
        public User getDoctor() { return doctor; }
        @Override
        public String toString() {
            return doctor.getName() + " (" + (doctor.getSpecialization() != null ? doctor.getSpecialization() : "General") + ")";
        }
    }

    public PatientGUI(PatientService patientService, User patient, MainMenuGUI mainMenu) {
        this.patientService = patientService;
        this.patient = patient;
        this.mainMenu = mainMenu;
        setTitle("Patient Dashboard - " + patient.getName());
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
        JPanel brandPanel = new JPanel(new GridLayout(2, 1));
        brandPanel.setOpaque(false);
        brandPanel.setBorder(new EmptyBorder(25, 20, 25, 20));
        JLabel patientNameLabel = new JLabel(patient.getName());
        patientNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        patientNameLabel.setForeground(Color.WHITE);
        JLabel patientTitleLabel = new JLabel("MedConnect Patient");
        patientTitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        patientTitleLabel.setForeground(new Color(148, 163, 184));
        brandPanel.add(patientNameLabel);
        brandPanel.add(patientTitleLabel);
        sidebar.add(brandPanel, BorderLayout.NORTH);

        // Navigation Menu
        JPanel navPanel = new JPanel();
        navPanel.setOpaque(false);
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

        apptsTab = new SidebarButton("My Appointments");
        bookTab = new SidebarButton("Book Appointment");
        doctorsTab = new SidebarButton("Find Doctors");
        profileTab = new SidebarButton("My Profile");

        navPanel.add(apptsTab);
        navPanel.add(Box.createVerticalStrut(5));
        navPanel.add(bookTab);
        navPanel.add(Box.createVerticalStrut(5));
        navPanel.add(doctorsTab);
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

        // 2. RIGHT MAIN PANELS (Card Layout)
        cardLayout = new CardLayout();
        rightPanel = new JPanel(cardLayout);
        rightPanel.setBackground(new Color(248, 250, 252));

        setupAppointmentsCard();
        setupBookCard();
        setupDoctorsCard();
        setupProfileCard();

        mainPanel.add(rightPanel, BorderLayout.CENTER);

        // Navigation tab trigger
        apptsTab.addActionListener(e -> switchTab("appts"));
        bookTab.addActionListener(e -> switchTab("book"));
        doctorsTab.addActionListener(e -> switchTab("doctors"));
        profileTab.addActionListener(e -> switchTab("profile"));

        switchTab("appts"); // Default
        setContentPane(mainPanel);
    }

    private void switchTab(String tabName) {
        apptsTab.setActive("appts".equals(tabName));
        bookTab.setActive("book".equals(tabName));
        doctorsTab.setActive("doctors".equals(tabName));
        profileTab.setActive("profile".equals(tabName));

        cardLayout.show(rightPanel, tabName);

        if ("appts".equals(tabName)) {
            loadAppointmentsData();
        } else if ("book".equals(tabName)) {
            loadDoctorsListCombo();
            refreshSlotsAvailability();
        } else if ("doctors".equals(tabName)) {
            loadDoctorsData(null);
        }
    }

    // 1. Setup Appointments Tab
    private void setupAppointmentsCard() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(new Color(248, 250, 252));
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel title = new JLabel("My Active Appointments");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(15, 23, 42));

        appointmentsModel = new DefaultTableModel(new String[]{"ID", "Doctor ID", "Time Slot", "Status"}, 0) {
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
        ModernButton cancelBtn = new ModernButton("Cancel Appointment", true);
        cancelBtn.setForeground(new Color(239, 68, 68));
        ModernButton rescheduleBtn = new ModernButton("Reschedule");

        controls.add(cancelBtn);
        controls.add(rescheduleBtn);

        cancelBtn.addActionListener(e -> cancelAppointmentAction(table));
        rescheduleBtn.addActionListener(e -> rescheduleAppointmentAction(table));

        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);
        topRow.add(title, BorderLayout.WEST);
        topRow.add(controls, BorderLayout.EAST);

        panel.add(topRow, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        rightPanel.add(panel, "appts");
    }

    private void loadAppointmentsData() {
        appointmentsModel.setRowCount(0);
        List<Appointment> appts = patientService.viewAppointmentHistory(patient.getId());
        for (Appointment a : appts) {
            appointmentsModel.addRow(new Object[]{a.getId(), a.getDoctorId(), a.getTimeSlot(), a.getStatus()});
        }
    }

    private void cancelAppointmentAction(JTable table) {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select an appointment to cancel.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int apptId = (int) appointmentsModel.getValueAt(row, 0);
        String status = (String) appointmentsModel.getValueAt(row, 3);

        if ("cancelled".equalsIgnoreCase(status)) {
            JOptionPane.showMessageDialog(this, "This appointment is already cancelled.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel appointment ID " + apptId + "?", "Confirm Cancel", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = patientService.cancelAppointment(apptId);
            if (success) {
                JOptionPane.showMessageDialog(this, "Appointment cancelled successfully.");
                loadAppointmentsData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to cancel appointment.");
            }
        }
    }

    private void rescheduleAppointmentAction(JTable table) {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select an appointment to reschedule.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int apptId = (int) appointmentsModel.getValueAt(row, 0);
        String currentTimeSlot = (String) appointmentsModel.getValueAt(row, 2);

        String newTimeSlot = JOptionPane.showInputDialog(this, "Enter new time slot:", currentTimeSlot);
        if (newTimeSlot != null && !newTimeSlot.trim().isEmpty()) {
            boolean success = patientService.updateAppointment(apptId, newTimeSlot.trim());
            if (success) {
                JOptionPane.showMessageDialog(this, "Appointment rescheduled successfully!");
                loadAppointmentsData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to reschedule. (Time slot might conflict)");
            }
        }
    }

    // 2. Setup Book Card
    private void setupBookCard() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(new Color(248, 250, 252));
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Header
        JPanel headerPanel = new JPanel(new GridLayout(2, 1));
        headerPanel.setOpaque(false);
        JLabel title = new JLabel("Book an Appointment");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(15, 23, 42));
        JLabel subtitle = new JLabel("Choose specialist, select date, and pick a 24-hour time slot");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(new Color(100, 116, 139));
        headerPanel.add(title);
        headerPanel.add(subtitle);
        panel.add(headerPanel, BorderLayout.NORTH);

        // Card Container
        RoundedCard card = new RoundedCard(16, Color.WHITE);
        card.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 10, 10);

        // --- LEFT COLUMN: Inputs ---
        JPanel leftCol = new JPanel(new GridBagLayout());
        leftCol.setOpaque(false);
        GridBagConstraints leftGbc = new GridBagConstraints();
        leftGbc.fill = GridBagConstraints.HORIZONTAL;
        leftGbc.weightx = 1.0;
        leftGbc.gridx = 0;

        // Doctor Combo Box
        leftGbc.gridy = 0;
        leftGbc.insets = new Insets(0, 0, 4, 0);
        leftCol.add(createFormLabel("Select Specialist"), leftGbc);
        
        doctorBox = new JComboBox<>();
        doctorBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        doctorBox.setBackground(Color.WHITE);
        doctorBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            BorderFactory.createEmptyBorder(6, 6, 6, 6)
        ));
        leftGbc.gridy = 1;
        leftGbc.insets = new Insets(0, 0, 20, 0);
        leftCol.add(doctorBox, leftGbc);

        // Date Box
        leftGbc.gridy = 2;
        leftGbc.insets = new Insets(0, 0, 4, 0);
        leftCol.add(createFormLabel("Select Appointment Date"), leftGbc);

        dateBox = new JComboBox<>();
        dateBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateBox.setBackground(Color.WHITE);
        dateBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            BorderFactory.createEmptyBorder(6, 6, 6, 6)
        ));
        // Populate next 7 days
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd");
        java.time.LocalDate today = java.time.LocalDate.now();
        for (int i = 0; i < 7; i++) {
            dateBox.addItem(today.plusDays(i).format(formatter));
        }
        leftGbc.gridy = 3;
        leftGbc.insets = new Insets(0, 0, 10, 0);
        leftCol.add(dateBox, leftGbc);

        // Add Left Column to Card
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.4;
        gbc.weighty = 1.0;
        card.add(leftCol, gbc);

        // --- RIGHT COLUMN: Time Slots Grid ---
        JPanel rightCol = new JPanel(new BorderLayout(5, 5));
        rightCol.setOpaque(false);
        JLabel slotsLabel = createFormLabel("Available Time Slots (24h Format)");
        slotsLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        rightCol.add(slotsLabel, BorderLayout.NORTH);

        // Grid of slots
        JPanel gridPanel = new JPanel(new GridLayout(2, 4, 8, 8));
        gridPanel.setOpaque(false);
        
        slotButtons = new JButton[timeSlots.length];
        for (int i = 0; i < timeSlots.length; i++) {
            final String time = timeSlots[i];
            JButton btn = new JButton(time);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
            btn.setFocusPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setBackground(Color.WHITE);
            btn.setForeground(new Color(71, 85, 105));
            btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));
            
            btn.addActionListener(e -> {
                selectedSlotTime = time;
                highlightSelectedSlot();
            });
            
            slotButtons[i] = btn;
            gridPanel.add(btn);
        }
        rightCol.add(gridPanel, BorderLayout.CENTER);

        // Add Right Column to Card
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.6;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(10, 20, 10, 10);
        card.add(rightCol, gbc);

        // --- BOTTOM ROW: Messaging & Booking button ---
        JPanel bottomRow = new JPanel(new GridBagLayout());
        bottomRow.setOpaque(false);
        GridBagConstraints bottomGbc = new GridBagConstraints();
        bottomGbc.fill = GridBagConstraints.HORIZONTAL;
        bottomGbc.weightx = 1.0;

        bookingMsg = new JLabel("", SwingConstants.CENTER);
        bookingMsg.setFont(new Font("Segoe UI", Font.BOLD, 12));
        bottomGbc.gridy = 0;
        bottomGbc.insets = new Insets(5, 5, 8, 5);
        bottomRow.add(bookingMsg, bottomGbc);

        ModernButton bookBtn = new ModernButton("Confirm Booking");
        bookBtn.addActionListener(e -> bookAppointmentAction());
        bottomGbc.gridy = 1;
        bottomGbc.insets = new Insets(0, 5, 5, 5);
        bottomRow.add(bookBtn, bottomGbc);

        // Add Bottom Row to Card across both columns
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(15, 10, 10, 10);
        card.add(bottomRow, gbc);

        // Selection listener to update available slot buttons dynamically
        doctorBox.addActionListener(e -> refreshSlotsAvailability());
        dateBox.addActionListener(e -> refreshSlotsAvailability());

        // Center Wrapper
        JPanel cardWrapper = new JPanel(new GridBagLayout());
        cardWrapper.setOpaque(false);
        GridBagConstraints wrapperGbc = new GridBagConstraints();
        wrapperGbc.weightx = 1.0;
        wrapperGbc.weighty = 1.0;
        wrapperGbc.fill = GridBagConstraints.HORIZONTAL;
        cardWrapper.add(card, wrapperGbc);

        panel.add(cardWrapper, BorderLayout.CENTER);
        rightPanel.add(panel, "book");
    }

    private void highlightSelectedSlot() {
        for (int i = 0; i < timeSlots.length; i++) {
            JButton btn = slotButtons[i];
            if (!btn.isEnabled()) {
                continue;
            }
            if (timeSlots[i].equals(selectedSlotTime)) {
                btn.setBackground(new Color(99, 102, 241));
                btn.setForeground(Color.WHITE);
                btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(79, 70, 229), 2),
                    BorderFactory.createEmptyBorder(9, 9, 9, 9)
                ));
            } else {
                btn.setBackground(Color.WHITE);
                btn.setForeground(new Color(71, 85, 105));
                btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
            }
        }
    }

    private void refreshSlotsAvailability() {
        selectedSlotTime = null;
        DoctorComboItem selectedDocItem = (DoctorComboItem) doctorBox.getSelectedItem();
        String selectedDate = (String) dateBox.getSelectedItem();

        if (selectedDocItem == null || selectedDate == null) {
            for (JButton btn : slotButtons) {
                btn.setEnabled(false);
                btn.setText("N/A");
            }
            return;
        }

        int docId = selectedDocItem.getDoctor().getId();
        List<Appointment> doctorAppts = patientService.getAppointmentsByDoctor(docId);

        for (int i = 0; i < timeSlots.length; i++) {
            String time = timeSlots[i];
            String fullSlotString = selectedDate + " " + time;
            boolean occupied = false;

            for (Appointment a : doctorAppts) {
                if (a.getTimeSlot().equals(fullSlotString) && 
                    !a.getStatus().equalsIgnoreCase("cancelled") && 
                    !a.getStatus().equalsIgnoreCase("rejected")) {
                    occupied = true;
                    break;
                }
            }

            JButton btn = slotButtons[i];
            if (occupied) {
                btn.setEnabled(false);
                btn.setText(time + " (Booked)");
                btn.setBackground(new Color(241, 245, 249));
                btn.setForeground(new Color(148, 163, 184));
                btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
            } else {
                btn.setEnabled(true);
                btn.setText(time);
                btn.setBackground(Color.WHITE);
                btn.setForeground(new Color(71, 85, 105));
                btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
            }
        }
    }

    private void loadDoctorsListCombo() {
        doctorBox.removeAllItems();
        List<User> doctors = patientService.viewAllDoctors();
        for (User d : doctors) {
            doctorBox.addItem(new DoctorComboItem(d));
        }
    }

    private void bookAppointmentAction() {
        DoctorComboItem selectedItem = (DoctorComboItem) doctorBox.getSelectedItem();
        String selectedDate = (String) dateBox.getSelectedItem();

        if (selectedItem == null || selectedDate == null || selectedSlotTime == null) {
            bookingMsg.setForeground(new Color(239, 68, 68));
            bookingMsg.setText("Please select doctor, date, and an available time slot.");
            return;
        }

        int docId = selectedItem.getDoctor().getId();
        String fullTimeSlot = selectedDate + " " + selectedSlotTime;
        boolean success = patientService.bookAppointment(patient.getId(), docId, fullTimeSlot);

        if (success) {
            bookingMsg.setForeground(new Color(34, 197, 94));
            bookingMsg.setText("Appointment booked successfully!");
            selectedSlotTime = null;
            refreshSlotsAvailability();
            
            Timer timer = new Timer(1500, evt -> switchTab("appts"));
            timer.setRepeats(false);
            timer.start();
        } else {
            bookingMsg.setForeground(new Color(239, 68, 68));
            bookingMsg.setText("Booking failed. (Time slot conflict)");
        }
    }

    // 3. Setup Doctors Tab
    private void setupDoctorsCard() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(new Color(248, 250, 252));
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel title = new JLabel("Search Doctors directory");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(15, 23, 42));

        // Filter Controls
        JPanel filterRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        filterRow.setOpaque(false);
        ModernTextField searchField = new ModernTextField("Filter by Specialization...");
        searchField.setPreferredSize(new Dimension(200, 36));
        ModernButton filterBtn = new ModernButton("Filter");
        ModernButton clearBtn = new ModernButton("Clear", true);

        filterRow.add(searchField);
        filterRow.add(filterBtn);
        filterRow.add(clearBtn);

        filterBtn.addActionListener(e -> loadDoctorsData(searchField.getText().trim()));
        clearBtn.addActionListener(e -> {
            searchField.setText("");
            loadDoctorsData(null);
        });

        doctorsModel = new DefaultTableModel(new String[]{"ID", "Name", "Specialization", "Contact Phone"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable table = new JTable(doctorsModel);
        ModernUIComponents.styleTable(table);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(241, 245, 249)));
        scrollPane.getViewport().setBackground(Color.WHITE);

        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);
        topRow.add(title, BorderLayout.WEST);
        topRow.add(filterRow, BorderLayout.EAST);

        panel.add(topRow, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        rightPanel.add(panel, "doctors");
    }

    private void loadDoctorsData(String filterSpec) {
        doctorsModel.setRowCount(0);
        List<User> doctors;
        if (filterSpec != null && !filterSpec.isEmpty()) {
            doctors = patientService.filterDoctorsBySpecialization(filterSpec);
        } else {
            doctors = patientService.viewAllDoctors();
        }
        for (User d : doctors) {
            doctorsModel.addRow(new Object[]{d.getId(), d.getName(), d.getSpecialization(), d.getPhone() != null ? d.getPhone() : "N/A"});
        }
    }

    // 4. Setup Profile Tab
    private void setupProfileCard() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(new Color(248, 250, 252));
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Header
        JPanel headerPanel = new JPanel(new GridLayout(2, 1));
        headerPanel.setOpaque(false);
        JLabel title = new JLabel("My Account Settings");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(15, 23, 42));
        JLabel subtitle = new JLabel("Manage your patient profile and credentials");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(new Color(100, 116, 139));
        headerPanel.add(title);
        headerPanel.add(subtitle);
        panel.add(headerPanel, BorderLayout.NORTH);

        // Card Container
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
        nameField.setText(patient.getName());
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 6, 15, 6);
        card.add(nameField, gbc);

        // Password
        gbc.gridy = 2;
        gbc.insets = new Insets(6, 6, 6, 6);
        card.add(createFormLabel("Password"), gbc);
        passField = new ModernPasswordField("New Password");
        passField.setText(patient.getPassword());
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 6, 15, 6);
        card.add(passField, gbc);

        // Message and Action button
        profileMsg = new JLabel("", SwingConstants.CENTER);
        profileMsg.setFont(new Font("Segoe UI", Font.BOLD, 12));
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 6, 10, 6);
        card.add(profileMsg, gbc);

        ModernButton saveBtn = new ModernButton("Save Profile Changes");
        saveBtn.addActionListener(e -> saveProfileChanges());
        gbc.gridy = 5;
        gbc.insets = new Insets(6, 6, 6, 6);
        card.add(saveBtn, gbc);

        // Center Wrapper
        JPanel cardWrapper = new JPanel(new GridBagLayout());
        cardWrapper.setOpaque(false);
        GridBagConstraints wrapperGbc = new GridBagConstraints();
        wrapperGbc.weightx = 1.0;
        wrapperGbc.weighty = 1.0;
        wrapperGbc.fill = GridBagConstraints.HORIZONTAL;
        cardWrapper.add(card, wrapperGbc);

        panel.add(cardWrapper, BorderLayout.CENTER);
        rightPanel.add(panel, "profile");
    }

    private void saveProfileChanges() {
        String name = nameField.getText().trim();
        String password = new String(passField.getPassword()).trim();

        if (name.isEmpty() || password.isEmpty()) {
            profileMsg.setForeground(new Color(239, 68, 68));
            profileMsg.setText("All fields are required.");
            return;
        }

        boolean success = patientService.updateProfile(patient.getId(), name, password);
        if (success) {
            profileMsg.setForeground(new Color(34, 197, 94));
            profileMsg.setText("Profile updated successfully!");
            patient.setName(name);
            patient.setPassword(password);
            
            setTitle("Patient Dashboard - " + name);
            initUI(); // Rebuild top header immediately
        } else {
            profileMsg.setForeground(new Color(239, 68, 68));
            profileMsg.setText("Failed to update profile.");
        }
    }

    private JLabel createFormLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(new Color(71, 85, 105));
        return label;
    }
}