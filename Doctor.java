package model;

import java.util.List;

public class Doctor extends User {
    private List<String> availableTimeSlots;

    public Doctor(int id, String name, String password, String specialization, List<String> availableTimeSlots) {
        super(id, name, password, "doctor", specialization);
        this.availableTimeSlots = availableTimeSlots;
    }

    public List<String> getAvailableTimeSlots() { return availableTimeSlots; }
    public void setAvailableTimeSlots(List<String> availableTimeSlots) { this.availableTimeSlots = availableTimeSlots; }

    @Override
    public String toString() {
        return super.toString() + ", availableTimeSlots=" + availableTimeSlots;
    }
} 