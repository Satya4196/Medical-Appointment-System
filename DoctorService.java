package service;

import dao.UserDAO;
import dao.AppointmentDAO;
import model.User;
import model.Appointment;
import java.util.List;

public class DoctorService {
    private UserDAO userDAO;
    private AppointmentDAO appointmentDAO;

    public DoctorService() {
        this.userDAO = new UserDAO();
        this.appointmentDAO = new AppointmentDAO();
    }

    public List<Appointment> getAppointmentsForDoctor(int doctorId) {
        return appointmentDAO.getAppointmentsByDoctor(doctorId);
    }

    public boolean updateAppointmentStatus(int apptId, String status) {
        return appointmentDAO.setStatus(apptId, status);
    }

    public List<User> getPatientsForDoctor(int doctorId) {
        return userDAO.getAllPatients();
    }

    public boolean updateProfile(int doctorId, String name, String password, String specialization, String phone) {
        User doctor = userDAO.getUserById(doctorId);
        if (doctor == null) return false;
        doctor.setName(name);
        doctor.setPassword(password);
        doctor.setSpecialization(specialization);
        doctor.setPhone(phone);
        return userDAO.updateUser(doctor);
    }
}
