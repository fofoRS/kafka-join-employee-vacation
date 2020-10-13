package org.kafka.join.demo.model;

import java.time.LocalDate;

public class VacationRequestAggregate {

    private String employee;
    private int vacations;
    private LocalDate hiredDate;
    private int daysAvailable;

    public VacationRequestAggregate(String employee, int vacations, LocalDate hiredDate, int daysAvailable) {
        this.employee = employee;
        this.vacations = vacations;
        this.hiredDate = hiredDate;
        this.daysAvailable = daysAvailable;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public int getVacations() {
        return vacations;
    }

    public void setVacations(int vacations) {
        this.vacations = vacations;
    }

    public LocalDate getHiredDate() {
        return hiredDate;
    }

    public void setHiredDate(LocalDate hiredDate) {
        this.hiredDate = hiredDate;
    }

    public int getDaysAvailable() {
        return daysAvailable;
    }

    public void setDaysAvailable(int daysAvailable) {
        this.daysAvailable = daysAvailable;
    }
}
