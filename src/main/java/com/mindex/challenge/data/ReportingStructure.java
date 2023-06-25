package com.mindex.challenge.data;

public class ReportingStructure {
    private int numberOfReports;
    private Employee employee;

    public ReportingStructure(Employee employee, int numberOfReports) {
        this.numberOfReports = numberOfReports;
        this.employee = employee;
    }

    public int getNumberOfReports() {
        return numberOfReports;
    }

    public void setNumberOfReports(int numberOfReports) {
        this.numberOfReports = numberOfReports;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Employee getEmployee() {
        return employee;
    }
}
