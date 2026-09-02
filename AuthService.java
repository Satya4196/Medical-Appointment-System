package service;

import dao.UserDAO;
import model.User;

public class AuthService {
    private UserDAO userDAO;

    public AuthService() {
        this.userDAO = new UserDAO();
    }

    public boolean registerPatient(String name, String password) {
        User user = new User(0, name, password, "patient", null);
        return userDAO.register(user);
    }

    public boolean registerAdmin(String name, String password) {
        User user = new User(0, name, password, "admin", null);
        return userDAO.register(user);
    }

    public boolean registerDoctor(String name, String password, String specialization, String phone) {
        User user = new User(0, name, password, "doctor", specialization, phone);
        return userDAO.register(user);
    }

    public User loginPatient(String name, String password) {
        return userDAO.login(name, password, "patient");
    }

    public User loginAdmin(String name, String password) {
        return userDAO.login(name, password, "admin");
    }

    public User loginDoctor(String name, String password) {
        return userDAO.login(name, password, "doctor");
    }

    public boolean validateUser(String name, String role) {
        return userDAO.getUserByNameAndRole(name, role) != null;
    }
} 