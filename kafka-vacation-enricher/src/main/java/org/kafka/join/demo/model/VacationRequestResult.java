package org.kafka.join.demo.model;

public class VacationRequestResult {
    private String employee;
    private int daysRequested;
    private int daysAvailable;
    private String status;
    private String reason;

    public VacationRequestResult(String employee, int daysRequested, int daysAvailable, String status, String reason) {
        this.employee = employee;
        this.daysRequested = daysRequested;
        this.daysAvailable = daysAvailable;
        this.status = status;
        this.reason = reason;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public int getDaysRequested() {
        return daysRequested;
    }

    public void setDaysRequested(int daysRequested) {
        this.daysRequested = daysRequested;
    }

    public int getDaysAvailable() {
        return daysAvailable;
    }

    public void setDaysAvailable(int daysAvailable) {
        this.daysAvailable = daysAvailable;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
