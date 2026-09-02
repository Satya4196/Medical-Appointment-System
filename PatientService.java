package service;

import dao.UserDAO;
import dao.DoctorDAO;
import dao.AppointmentDAO;
import model.User;
import model.Appointment;
import java.util.List;

public class PatientService {
    private UserDAO userDAO;
    private DoctorDAO doctorDAO;
    private AppointmentDAO appointmentDAO;

    public PatientService() {
        this.userDAO = new UserDAO();
        this.doctorDAO = new DoctorDAO();
        this.appointmentDAO = new AppointmentDAO();
    }

    public List<User> viewAllDoctors() {
        return doctorDAO.getAllDoctors();
    }

    public List<User> filterDoctorsBySpecialization(String specialization) {
        return doctorDAO.getDoctorsBySpecialization(specialization);
    }

    public boolean bookAppointment(int patientId, int doctorId, String timeSlot) {
        if (appointmentDAO.checkConflict(doctorId, timeSlot)) {
            return false; // Conflict detected
        }
        Appointment appt = new Appointment(0, patientId, doctorId, timeSlot, "booked");
        return appointmentDAO.bookAppointment(appt);
    }

    public boolean cancelAppointment(int appointmentId) {
        return appointmentDAO.cancelAppointment(appointmentId);
    }

    public boolean updateAppointment(int appointmentId, String newTimeSlot) {
        Appointment appt = appointmentDAO.getAppointmentById(appointmentId);
        if (appt == null) return false;
        if (appointmentDAO.checkConflict(appt.getDoctorId(), newTimeSlot)) {
            return false; // Conflict detected
        }
        appt.setTimeSlot(newTimeSlot);
        appt.setStatus("booked");
        return appointmentDAO.updateAppointment(appt);
    }

    public List<Appointment> viewAppointmentHistory(int patientId) {
        return appointmentDAO.getAppointmentsByPatient(patientId);
    }

    public List<Appointment> getAppointmentsByDoctor(int doctorId) {
        return appointmentDAO.getAppointmentsByDoctor(doctorId);
    }

    public boolean updateProfile(int patientId, String newName, String newPassword) {
        User user = userDAO.getUserById(patientId);
        if (user == null) return false;
        user.setName(newName);
        user.setPassword(newPassword);
        return userDAO.updateUser(user);
    }
} 