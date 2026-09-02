package service;

import dao.UserDAO;
import dao.DoctorDAO;
import dao.AppointmentDAO;
import model.User;
import model.Appointment;
import java.util.List;

public class AdminService {
    private UserDAO userDAO;
    private DoctorDAO doctorDAO;
    private AppointmentDAO appointmentDAO;

    public AdminService() {
        this.userDAO = new UserDAO();
        this.doctorDAO = new DoctorDAO();
        this.appointmentDAO = new AppointmentDAO();
    }

    public boolean addDoctor(String name, String password, String specialization) {
        User doctor = new User(0, name, password, "doctor", specialization);
        return doctorDAO.addDoctor(doctor);
    }

    public boolean updateDoctor(int doctorId, String name, String password, String specialization) {
        User doctor = new User(doctorId, name, password, "doctor", specialization);
        return doctorDAO.updateDoctor(doctor);
    }

    public boolean deleteDoctor(int doctorId) {
        return doctorDAO.deleteDoctor(doctorId);
    }

    public List<User> viewAllDoctors() {
        return doctorDAO.getAllDoctors();
    }

    public List<User> viewAllPatients() {
        return userDAO.getAllPatients();
    }

    public List<Appointment> viewAllAppointments() {
        return appointmentDAO.getAllAppointments();
    }

    public boolean cancelAppointment(int appointmentId) {
        return appointmentDAO.cancelAppointment(appointmentId);
    }

    public List<Appointment> getAppointmentsByDoctor(int doctorId) {
        return appointmentDAO.getAppointmentsByDoctor(doctorId);
    }

    public int getTotalAppointmentsPerDoctor(int doctorId) {
        return appointmentDAO.getAppointmentsByDoctor(doctorId).size();
    }
} 