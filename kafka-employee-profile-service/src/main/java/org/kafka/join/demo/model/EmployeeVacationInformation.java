package org.kafka.join.demo.model;

import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntity;

import java.time.LocalDate;

public class EmployeeVacationInformation implements DataStreamRecord{
    private String firstName;
    private String lastName;
    private LocalDate hireDate;
    private int daysAvailable;


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public int getDaysAvailable() {
        return daysAvailable;
    }

    public void setDaysAvailable(int daysAvailable) {
        this.daysAvailable = daysAvailable;
    }

    @Override
    public String deserializeStreamRecord() {
        return "firstName: " + firstName +
               " lastName: " + lastName + " hiredDate: " + hireDate + " daysAvailable: " + daysAvailable;
    }
}
